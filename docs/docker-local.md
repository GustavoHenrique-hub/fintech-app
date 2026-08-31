# Stack local em Docker — backend + frontend + banco + n8n

Um único `docker compose up` sobe as quatro peças do monorepo na mesma rede.
Substitui o roteiro antigo de "abre três terminais: mvn, npm, n8n".

Tudo mora em [`infra/`](../infra).

## Por que isso resolve o problema do n8n

O trial que acabou era do **n8n Cloud**. O n8n em si é fair-code: a Community
Edition self-hosted é gratuita, sem limite de execuções nem de workflows, e os
JSONs de `automacao/n8n/` importam sem nenhuma alteração.

Há inclusive um ganho: os workflows leem `$env.FINTECH_API_URL`,
`$env.INTERNAL_API_KEY` e `$env.CLAUDE_MODEL` no nó ⚙️ Config. **O n8n Cloud
bloqueia acesso a variáveis de ambiente em expressões**; o self-hosted, não —
desde que `N8N_BLOCK_ENV_ACCESS_IN_NODE=false`, que o compose já define.

## Os serviços

| Serviço | Imagem / build | Porta no host | Nome na rede Docker |
|---|---|---|---|
| `postgres` | `postgres:16-alpine` | 5432 | `postgres` |
| `backend` | `infra/backend.Dockerfile` | 8082 | `backend` |
| `frontend` | `infra/frontend.Dockerfile` | 3000 | `frontend` |
| `n8n` | `docker.n8n.io/n8nio/n8n:latest` | 5678 | `n8n` |

As portas foram mantidas **iguais às do setup local** (8082 / 3000 / 5678) de
propósito: `docs/n8n-extratos.md`, o `vite.config.js` e os defaults do
`application.yaml` continuam válidos sem edição.

### Quem enxerga quem

Esse é o ponto que quebra a maioria das tentativas de containerizar esse fluxo:

```
navegador  →  localhost:3000   (nginx do frontend)
                  │
                  ├─ /            → arquivos estáticos do build do Vite
                  └─ /api/*       → http://backend:8082/*     (proxy, tira o /api)

backend    →  http://n8n:5678/webhook/extratos/processar      (envia o PDF)
n8n        →  http://backend:8082/extratos/{id}/status        (PATCH)
              http://backend:8082/extratos/{id}/callback      (POST)
backend    →  jdbc:postgresql://postgres:5432/fintech_app_dev
```

Dentro da rede do compose, `localhost` é **o próprio container**. Por isso
`FINTECH_API_URL` é `http://backend:8082` e não `http://localhost:8082`, e por
isso não é preciso `host.docker.internal` em lugar nenhum — os quatro estão na
mesma rede.

Como o navegador fala só com `localhost:3000` e o nginx repassa `/api`, browser
e API ficam na mesma origem. **Isso é o que evita CORS**: o backend não tem
nenhuma configuração de CORS hoje, então servir o front de outra origem
quebraria as chamadas do axios.

## Subir

```bash
cd infra
cp .env.example .env      # preencha os campos <<< PREENCHER >>>
docker compose up -d --build
```

O primeiro build demora (baixa Maven, JDK, node, nginx e resolve as dependências
do `pom.xml`). Os seguintes reaproveitam cache: enquanto `pom.xml` e
`package-lock.json` não mudarem, as camadas pesadas não são refeitas.

```bash
docker compose ps        # todos devem ficar "running"/"healthy"
docker compose logs -f backend
```

| O quê | Onde |
|---|---|
| App | http://localhost:3000 |
| Swagger | http://localhost:8082/swagger-ui.html |
| Health | http://localhost:8082/actuator/health |
| n8n | http://localhost:5678 |

Derrubar: `docker compose down` (mantém os dados) ou `docker compose down -v`
(**apaga** banco, uploads e tudo que estiver salvo no n8n).

## Importar os workflows no n8n

O `docker-compose.yml` monta `automacao/n8n/` como `/workflows` (somente
leitura) dentro do container. Na primeira subida:

1. Abra http://localhost:5678 e crie a conta do owner (é local, fica no volume).
2. Importe os quatro workflows — pela UI (**Workflows → ⋯ → Import from File**)
   ou de uma vez pelo CLI:

```bash
docker compose exec n8n n8n import:workflow --separate --input=/workflows
```

3. **Cadastre a credencial da Anthropic.** Credenciais nunca vão no export do
   JSON, então o nó *Claude - Extrair e Classificar* vem sem chave. Vá em
   **Credentials → New → Anthropic API** e cole a `sk-ant-...`.
4. Ative o workflow `Extratos · Entrada App (backend)` — só com ele ativo o
   webhook `POST /webhook/extratos/processar` passa a responder.

Os workflows `03-telegram` e `04-whatsapp` precisam de URL **pública** (o
Telegram não alcança `localhost`). Enquanto você usar só o fluxo do app, deixe
os dois desativados. Para expor depois, veja a seção 5 do `.env.example`.

## Onde vai cada segredo

Resumo — os detalhes estão comentados em [`infra/.env.example`](../infra/.env.example).

| Segredo | Onde preencher | Consumido por |
|---|---|---|
| `POSTGRES_PASSWORD` | `infra/.env` | container postgres + `DB_PASSWORD` do backend |
| `INTERNAL_API_KEY` | `infra/.env` | backend **e** n8n (header `X-Internal-Api-Key`) |
| `N8N_CALLBACK_SECRET` | `infra/.env` | HMAC do header `X-N8N-Signature` |
| `N8N_ENCRYPTION_KEY` | `infra/.env` | criptografa as credenciais salvas no n8n |
| Chave da Anthropic | **UI do n8n** (recomendado) ou `ANTHROPIC_API_KEY` no `.env` | nó *Claude - Extrair e Classificar* |
| Token do bot Telegram | **UI do n8n** (Credentials → Telegram API) | workflow 03 |
| Chave da Evolution API | **UI do n8n** (Credentials → Header Auth) | workflow 04 |

`infra/.env` é coberto pela regra `.env` do `.gitignore` da raiz — não vai para
o Git. O `.env.example` é versionado e só tem placeholders.

### Pendência conhecida: HMAC do callback

Os nós `Assinar payload sucesso` e `Assinar payload erro` (tipo `crypto`) estão
com o campo **Secret vazio** — o nó ⚙️ Config expõe `callbackSecret`, mas
ninguém o consome. Consequência prática: se você preencher
`N8N_CALLBACK_SECRET`, o backend vai calcular um HMAC diferente do que o n8n
enviou e **rejeitar todo callback**.

Enquanto não corrigir, deixe `N8N_CALLBACK_SECRET` vazio (o backend pula a
verificação, com aviso no log). Para corrigir, edite os dois nós na UI e ponha
no campo Secret a expressão que lê `callbackSecret` do nó Config:

```
{{ $('Config').first().json.callbackSecret }}
```

## Desenvolver com hot-reload

O container do frontend serve um build **estático** — ótimo para rodar a stack
inteira, ruim para editar componente. Para desenvolver, suba tudo pelo compose
e rode o Vite fora:

```bash
docker compose up -d postgres backend n8n
cd frontend/fintech_app && npm run dev      # http://localhost:3000
```

O proxy do `vite.config.js` aponta para `http://localhost:8082`, que é a porta
publicada do container do backend — funciona sem mudar nada.

## Banco de dados

`ddl-auto: update` faz o Hibernate criar/atualizar as tabelas no boot, então o
banco sobe utilizável e vazio. O script de init cria os três databases
(`fintech_app_dev`, `_hml`, `_prd`) e a extensão `pgcrypto`.

Para carregar a massa de teste **depois** que o backend subiu pelo menos uma vez
(a carga depende das tabelas já existirem):

```bash
docker compose exec -T postgres psql -U fintech -d fintech_app_dev < ../scripts/fintechapp_carga.sql
```

## Problemas comuns

| Sintoma | Causa / solução |
|---|---|
| `port is already allocated` | Algo já usa 3000/8082/5678/5432. Ajuste `*_PORT` no `.env`. |
| Backend reinicia em loop | Senha do banco mudou depois do 1º boot. `docker compose down -v` e suba de novo. |
| n8n pede login toda hora | Falta `N8N_SECURE_COOKIE=false` (o compose já define) ou você acessa por IP em vez de `localhost`. |
| Credenciais do n8n "sumiram" | `N8N_ENCRYPTION_KEY` mudou. Volte o valor anterior ou recadastre. |
| Extrato fica preso em `na_fila` | Workflow 02 não está **ativo** no n8n, ou `INTERNAL_API_KEY` difere entre os dois lados. |
| Extrato cai sempre no parser local | `N8N_ENABLED=false`, ou o backend não alcançou o n8n (veja `docker compose logs backend`). |
| Callback rejeitado (401) | `INTERNAL_API_KEY` diferente, ou o problema do HMAC descrito acima. |
| Upload devolve 413 | Arquivo acima de 10MB (`spring.servlet.multipart.max-file-size`). |
