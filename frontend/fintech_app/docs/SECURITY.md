# Segurança — Integração SPA React (Vite) ↔ Spring Boot REST

Aplicação fintech sujeita à LGPD. Stack: **Vite + React + axios** (front) / **Spring Boot + Spring Security + JPA** (back) / **PostgreSQL**.

> Este documento consolida as recomendações de proteção das requisições HTTP entre o SPA e o backend. Foi escrito em resposta à preocupação válida de que "qualquer pessoa pode acessar a pasta source do projeto pelo DevTools".

---

## A) O source-map do Vite/React NÃO é uma falha de segurança

Quando o usuário abre **DevTools → Sources** e vê a árvore de arquivos `.jsx`, isso é **comportamento esperado de toda SPA**, não vulnerabilidade.

- Todo JavaScript que roda no navegador é, por definição, público. Minificação e tree-shaking dificultam leitura, mas **não escondem nada**.
- Source-maps apenas reconstroem a forma legível do código que já está no bundle. Em produção, prática comum desabilitá-los (`build.sourcemap: false` no `vite.config.js`) — não porque protejam segredos, mas para reduzir engenharia reversa e tamanho de deploy.
- O risco real não é "o atacante leu meu `TransacaoCard.jsx`". É **o que está embutido nesse JS**. Lógica de UI, nomes de rotas e estrutura de componentes não são segredos. Chaves de API, tokens, credenciais e regras de negócio sensíveis são.

**Regra de ouro:** trate o frontend como cliente hostil. Toda autorização, validação e regra crítica vive no backend.

---

## B) Onde NÃO colocar segredos

Nada do que está listado abaixo continua "secreto" depois do build:

- **`VITE_*` em `.env`** — são inlineadas no bundle final. `VITE_API_SECRET=xyz` aparece literal no JS servido. Use `VITE_*` apenas para configuração pública (URL base da API, flag de ambiente).
- **Código-fonte JS/JSX** — strings, constantes, objetos de configuração.
- **Comentários** — `// TODO: trocar essa chave depois`.
- **localStorage / sessionStorage** — acessível por qualquer script via XSS. Nunca armazene JWT de longa duração, refresh tokens, ou PII.
- **IndexedDB sem cifragem** — mesmo problema.
- **URLs de endpoints "secretos"** — security through obscurity não conta como segurança.

Segredos (JWT signing key, credenciais de DB, chaves de provedores de pagamento) vivem em **variáveis de ambiente do backend** ou em secret manager (AWS Secrets Manager, Vault, Doppler).

---

## C) Modelo de autenticação recomendado

### Comparativo

| Estratégia | Vulnerável a XSS? | Vulnerável a CSRF? | Complexidade |
|---|---|---|---|
| JWT em `localStorage` | **Sim** (qualquer script lê) | Não | Baixa |
| JWT em cookie `HttpOnly + Secure + SameSite=Strict` | Não (JS não acessa) | Mitigado por SameSite | Média |
| Access token curto + refresh token rotativo | Depende do armazenamento | Mitigável | Alta (**recomendada**) |

### Recomendação concreta

**Access token JWT curto (15 min) + refresh token rotativo (7 dias), ambos em cookies HttpOnly.**

- Access token: `HttpOnly; Secure; SameSite=Strict; Path=/`, expiração 15 min, assinado com HS256/RS256.
- Refresh token: `HttpOnly; Secure; SameSite=Strict; Path=/auth/refresh`, expiração 7 dias, **rotacionado a cada uso** (o antigo é invalidado e persistido em tabela `refresh_tokens` com `revoked_at`).
- **Reuse detection**: se um refresh token já consumido for reapresentado, **revogue toda a família de tokens daquele usuário** — sinal forte de roubo.

Justificativa: cookies HttpOnly neutralizam o vetor mais explorado (XSS roubando token). Rotação com reuse detection protege contra exfiltração silenciosa do refresh token. Tokens curtos limitam janela de abuso.

---

## D) CSRF — mitigação quando se usa cookie

Cookies viajam automaticamente em qualquer requisição cross-site → exposto a CSRF.

Use **duas camadas**:

1. **`SameSite=Strict`** no cookie — bloqueia envio em navegação cross-site. Suficiente para a maioria dos fluxos de uma SPA hospedada no mesmo eTLD+1 do backend.
2. **CSRF token double-submit** para endpoints que modificam estado (`POST`, `PATCH`, `DELETE`):
   - Backend gera token aleatório e envia em cookie legível (`XSRF-TOKEN`, **sem** `HttpOnly`).
   - Frontend lê esse cookie e ecoa em header `X-XSRF-TOKEN` em cada request. (Já implementado em [`src/services/api.js`](../src/services/api.js).)
   - Spring Security tem `CookieCsrfTokenRepository.withHttpOnlyFalse()` pronto.
   - O servidor compara header vs cookie; um atacante CSRF não consegue ler o cookie de outro site (Same-Origin Policy) → não consegue forjar o header.

Endpoints idempotentes de leitura (`GET`) podem ser isentos.

---

## E) CORS — configuração Spring

```java
config.setAllowedOrigins(List.of("https://app.suafintech.com.br"));
config.setAllowCredentials(true);
```

Regras:

- **Nunca `allowedOrigins("*")` com `allowCredentials(true)`** — o navegador rejeita a combinação. Pior: se você contornar com `allowedOriginPatterns("*")`, abre o backend para qualquer origem enviar cookies. **Liste origens explicitamente.**
- `allowedMethods`: apenas os usados (`GET, POST, PATCH, DELETE, OPTIONS`).
- `allowedHeaders`: `Authorization`, `Content-Type`, `X-XSRF-TOKEN`.
- `maxAge`: 3600 para reduzir preflight.
- Origens diferentes por ambiente: dev (`http://localhost:8080`), staging, prod — nunca compartilhar lista única.

---

## F) Rate limiting

Camadas (defesa em profundidade):

- **Edge (nginx / API Gateway)** — limite global por IP, ex.: 100 req/min. Protege contra varredura e DDoS volumétrico.
- **Aplicação (Bucket4j + filtro Spring)** — limites por endpoint e por usuário autenticado.

Sugestões por endpoint sensível:

| Endpoint | Limite | Chave |
|---|---|---|
| `POST /auth/login` | 5 tentativas / 15 min | IP + email |
| `POST /auth/forgot-password` | 3 / hora | email |
| `POST /auth/refresh` | 30 / hora | usuarioId |
| `POST /transacoes` | 10 / min | usuarioId |
| `PATCH /transacoes/{id}/{code}/estornar` | 5 / min | usuarioId |
| `POST /usuarios` (cadastro) | 3 / hora | IP |

Lockout progressivo no login: após 5 falhas, exigir captcha; após 10, bloquear conta por 30 min com notificação ao usuário.

Retorne `429 Too Many Requests` com header `Retry-After`. O axios interceptor em `api.js` propaga o erro ao chamador.

---

## G) Sanitização e validação no backend

O frontend nunca é fonte de verdade. Toda validação é **re-executada** no servidor.

- **Bean Validation** (`jakarta.validation`) nos DTOs: `@NotBlank`, `@Email`, `@Size`, `@Pattern(regexp = "\\d{11}")` para CPF, `@DecimalMin("0.01")` para valores monetários.
- **`@Valid`** em todo `@RequestBody`.
- **`@PreAuthorize`** para autorização (ex.: `@PreAuthorize("#id == principal.id or hasRole('ADMIN')")` em `GET /usuarios/{id}`). **IDOR (Insecure Direct Object Reference) é o risco número um aqui** — só porque o `id` veio na URL não significa que o usuário pode acessá-lo.
- **JPA com parâmetros nomeados ou Criteria API** — nunca concatenar string em JPQL/SQL nativo.
- **Validação de domínio**: estorno só permitido em transações do próprio usuário, dentro de janela X, ainda não estornadas — valide na service, não confie no frontend.

---

## H) Tratamento de erros sem vazamento

`@RestControllerAdvice` global retornando estrutura mínima:

```json
{ "code": "TRANSACAO_INVALIDA", "message": "Operação não pode ser realizada.", "traceId": "..." }
```

Regras:

- **5xx**: log completo no servidor com `traceId`; resposta ao cliente contém apenas `traceId` e mensagem genérica. **Nunca** stacktrace, nome de classe, query SQL ou path interno.
- **4xx**: mensagem específica o suficiente para o usuário corrigir (`"E-mail inválido"`), mas sem revelar existência de recurso. Em login falho, sempre `"Credenciais inválidas"` — nunca `"Usuário não existe"` (user enumeration).
- **404 vs 403**: para recursos sensíveis (transações, contas), retorne 404 mesmo quando o recurso existe mas o usuário não tem acesso — evita confirmação de existência.
- Desabilite `server.error.include-stacktrace` e `include-message=always` em produção.

---

## I) Headers de segurança

Configure via Spring Security (`HttpSecurity.headers()`) ou no reverse proxy:

- `Strict-Transport-Security: max-age=31536000; includeSubDomains; preload` — força HTTPS por 1 ano.
- `X-Content-Type-Options: nosniff` — bloqueia MIME sniffing.
- `X-Frame-Options: DENY` ou `Content-Security-Policy: frame-ancestors 'none'`.
- `Referrer-Policy: strict-origin-when-cross-origin` — não vaza path interno para terceiros.
- `Content-Security-Policy: default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; connect-src 'self' https://api.suafintech.com.br; frame-ancestors 'none'; base-uri 'self'; form-action 'self'`. Itere com `Content-Security-Policy-Report-Only` antes de aplicar em modo enforcing.
- `Permissions-Policy: geolocation=(), camera=(), microphone=()` — desliga APIs do navegador que o app não usa.
- Remova `Server` e `X-Powered-By`.

---

## J) Configuração do axios (já implementada)

[`src/services/api.js`](../src/services/api.js):

- `withCredentials: true` — cookies HttpOnly viajam em todas as chamadas.
- `timeout: 10_000` — operações financeiras não devem ficar penduradas.
- **Request interceptor**: injeta `X-XSRF-TOKEN` lido do cookie `XSRF-TOKEN` em métodos não-idempotentes.
- **Response 401**: tenta `POST /auth/refresh` **uma única vez** (flag `_retried`). Se falhar, dispara `CustomEvent("auth:logout")` que o app escuta para redirecionar.
- **Response 403**: dispara `CustomEvent("auth:forbidden")`.
- **`refreshInFlight`**: deduplica refresh concorrente quando várias requests 401 acontecem em paralelo.

---

## K) LGPD — específico para esta fintech

Dados pessoais tratados: CPF, e-mail, telefone, saldo, histórico de transações.

- **Princípio da finalidade**: só colete o que precisa. Documente o motivo de cada PII.
- **Mascaramento em respostas**:
  - CPF para o próprio usuário: completo. Para terceiros: `***.***.***-12`.
  - E-mail: `g****@gmail.com` em contextos de listagem.
  - Telefone: `(11) ****-1234`.
  - Implemente via `@JsonView` ou DTOs distintos por contexto (`UsuarioOwnerDTO`, `UsuarioPublicDTO`).
- **Logs sem PII**: nunca logue CPF, senha, token, ou corpo completo de requests. Use `usuarioId` (UUID). Configure filtros no Logback para mascarar campos sensíveis se aparecerem por engano.
- **Direitos do titular** (Art. 18 LGPD): endpoints para exportação (`GET /me/dados`) e exclusão (`DELETE /me`). Exclusão deve ser **anonimização** (manter trilha de auditoria financeira com `usuarioId` nullificado), não DELETE físico em registros de transações (obrigação contábil).
- **Retenção**: logs de aplicação 90 dias, logs de auditoria financeira 5 anos (CVM/BACEN), tokens revogados 30 dias.
- **Criptografia em repouso**: PostgreSQL com TDE ou cifragem em coluna para CPF (`pgcrypto` com chave em secret manager).
- **DPO e canal de contato** publicados na política de privacidade.

---

## L) Auditoria

Tabela `auditoria_evento` imutável (sem `UPDATE`/`DELETE` permitidos via role de aplicação):

| Campo | Exemplo |
|---|---|
| `id` | UUID |
| `evento` | `TRANSACAO_ESTORNADA`, `SENHA_ALTERADA`, `LOGIN_SUCESSO`, `LOGIN_FALHA`, `DADOS_PESSOAIS_ATUALIZADOS` |
| `usuario_id` | UUID |
| `ip` | `203.0.113.42` (do `X-Forwarded-For` validado) |
| `user_agent` | string |
| `recurso_id` | id da transação, etc. |
| `metadados` | JSONB com diff antes/depois (sem PII bruta) |
| `criado_em` | timestamptz |

Implemente via AOP (`@Auditavel` em métodos de service) ou eventos do Spring (`ApplicationEventPublisher`). Escrita assíncrona em fila (outbox pattern). Diferencie `LOGIN_FALHA` por motivo (`SENHA_INVALIDA`, `USUARIO_BLOQUEADO`) — útil para detecção de fraude.

---

## M) Checklist final pré-produção

1. `build.sourcemap: false` no Vite e nenhum `VITE_*` contém segredo.
2. JWT em cookie `HttpOnly + Secure + SameSite=Strict`; access 15 min, refresh 7 dias rotativo com reuse detection.
3. CSRF token double-submit ativo em todos os endpoints não-idempotentes; CSRF habilitado no Spring Security.
4. CORS com `allowedOrigins` explícito por ambiente, sem wildcard com credenciais.
5. Rate limiting aplicado em login, refresh, forgot-password, transações e estornos.
6. `@PreAuthorize` em todos os endpoints que acessam recursos por id (anti-IDOR), incluindo `GET /usuarios/{id}` e `PATCH /transacoes/{id}/...`.
7. `GlobalExceptionHandler` retorna `{ code, message, traceId }`, sem stacktrace; `include-stacktrace=never` em prod.
8. Headers `HSTS`, `CSP`, `X-Content-Type-Options`, `Referrer-Policy`, `Permissions-Policy` ativos e testados em [securityheaders.com](https://securityheaders.com).
9. Logs sem CPF/senha/token; CPF cifrado em repouso; mascaramento em DTOs para contextos não-owner.
10. Auditoria imutável escrevendo todos os eventos sensíveis com `usuarioId`, `ip`, `userAgent`, `timestamp`; dependências escaneadas no CI (OWASP Dependency-Check ou `mvn dependency-check:check`).

---

**Resumo executivo:** o source-map exposto não é o problema — é o sintoma de uma preocupação válida: "o que do meu sistema é público?" A resposta é: **todo o JS é público; o backend é a fronteira de confiança**. Este documento descreve essa fronteira para o app FinSight sob LGPD.
