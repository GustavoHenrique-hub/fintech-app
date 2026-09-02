# n8n sem o Cloud: self-hosted + stack Docker local

Registro da consulta de **31/08/2026**: o que motivou a mudança, as alternativas
avaliadas e descartadas, o que foi implementado e o que ficou pendente.

O passo a passo operacional está em [`docker-local.md`](docker-local.md). Este
documento guarda o **porquê** de cada decisão — o que o passo a passo não conta.

---

## Contexto

O free trial do **n8n Cloud** acabou e a automação de importação de extratos
parou. Os quatro workflows envolvidos:

| Arquivo | Papel |
|---|---|
| `01-extratos-core-ia.json` | Sub-workflow. Chama a Claude API, normaliza os lançamentos, assina e devolve pelo callback. |
| `02-extratos-entrada-app.json` | Webhook que o backend chama no upload. **É o único necessário para o fluxo do app.** |
| `03-extratos-entrada-telegram.json` | Entrada por bot do Telegram. |
| `04-extratos-entrada-whatsapp.json` | Entrada por WhatsApp via Evolution API. |

---

## 1. Existe alternativa gratuita?

**Sim, e não exige trocar de ferramenta: o que acabou foi o trial do n8n
*Cloud*, não do n8n.**

A Community Edition self-hosted é fair-code (Sustainable Use License), roda em
Docker, e não tem limite de execuções nem de workflows. Os quatro JSONs importam
como estão — zero reescrita.

### O self-hosted é melhor para estes workflows, não só mais barato

O nó ⚙️ Config de `01-extratos-core-ia` lê variáveis de ambiente:

```
$env.FINTECH_API_URL   $env.INTERNAL_API_KEY
$env.N8N_CALLBACK_SECRET   $env.CLAUDE_MODEL
```

**O n8n Cloud bloqueia acesso a `$env` em expressões.** O self-hosted libera,
desde que `N8N_BLOCK_ENV_ACCESS_IN_NODE=false`. Ou seja, esses workflows foram
escritos para o ambiente self-hosted.

Nenhum nó usado exige licença paga: `executeWorkflow`, `crypto`, `telegramTrigger`,
`httpRequest` e `code` são todos community.

### Alternativas avaliadas e descartadas

| Opção | Por que não |
|---|---|
| **Activepieces** (MIT) / **Windmill** (AGPL) | Self-host gratuito, mas os JSONs **não** são compatíveis — reconstrução nó a nó dos quatro fluxos. Ganho zero sobre self-hostar o próprio n8n. |
| **Node-RED / Huginn** | Mesma reescrita, com ecossistema de nós mais pobre para este caso. |
| **Migrar o Core IA para o backend** | Tecnicamente a melhor opção a médio prazo (ver abaixo), mas é refactor, não resposta a uma emergência. |

### A opção "matar o n8n" (adiada, não descartada)

`01-extratos-core-ia` é, na prática:

```
base64 → POST api.anthropic.com/v1/messages (tool schema registrar_extrato)
       → normalizar datas/valores → HMAC → POST /extratos/{id}/callback
```

Isso cabe em ~1 service Java em `adapters/out/`. O backend já tem
`RegistrarResultadoExtratoUseCase`, o parser local de fallback e o
`AutenticacaoCallbackN8n` prontos. Removeria o n8n do caminho do app, deixando-o
só para Telegram/WhatsApp — que é onde ele realmente agrega.

**Decisão:** tratar como refactor separado. Subir o Docker resolve hoje.

---

## 2. E hospedar no Heroku?

Pergunta específica: usando o [guia oficial do n8n para Heroku](https://docs.n8n.io/deploy/host-n8n/install-options/use-a-cloud-provider/deploy-to-heroku),
seria equivalente ao n8n Cloud, já que ambos teriam URL pública?

**Resolve a URL pública, mas não é gratuito — e cria um bloqueio maior do que
resolve.**

### Não é grátis

O Heroku removeu o free tier em **28/11/2022**. Custo mínimo:

| Item | Por quê | Custo |
|---|---|---|
| Eco dyno | dorme após 30min de inatividade | US$ 5/mês (1.000h compartilhadas) |
| Basic dyno | se não puder dormir | US$ 7/mês |
| Heroku Postgres Essential-0 | **obrigatório** — filesystem efêmero; o `heroku.yml` do n8n já força o addon | US$ 5/mês |

**~US$ 10–12/mês.** Mais barato que o n8n Cloud, mas não é o "de graça" pedido.

### O bloqueio real

O n8n no Heroku **não alcança o backend**. Hoje `FINTECH_API_URL` aponta para
`localhost:8082`. Da nuvem, as chamadas que fecham o fluxo inteiro —

```
PATCH {apiBaseUrl}/extratos/{id}/status
POST  {apiBaseUrl}/extratos/{id}/callback
```

— falhariam 100% das vezes. Subir o n8n para o Heroku **obriga** a subir o
backend Spring Boot junto, dobrando custo e escopo.

### Outras armadilhas

- **`N8N_ENCRYPTION_KEY` fixo é crítico.** Se regenerar, a credencial da
  Anthropic vira ilegível a cada redeploy.
- **Router do Heroku: timeout de 30s (inbound).** O workflow 02 passa porque
  responde 202 na hora via `respondToWebhook`. Trocar para `responseMode: lastNode`
  quebraria — o nó da Claude tem `timeout: 300000`.
- **Dynos reciclam ~1x/dia.** Execução em andamento morre sem callback, deixando
  o extrato preso em `extraindo`.
- **512MB de RAM.** O n8n já usa ~250–400MB, e o pipeline passa PDF em base64
  (que incha 33%) por webhook + 3 Code nodes.

### Se o objetivo for URL pública gratuita

1. **Cloudflare Tunnel** (gratuito, sem cartão) na frente do Docker local:
   `https://algo.trycloudflare.com` → `localhost:5678`. Telegram e WhatsApp
   passam a funcionar e o `FINTECH_API_URL` continua válido, porque n8n e
   backend seguem na mesma máquina. **Menor esforço.**
2. **Oracle Cloud Always Free** (VM ARM, 4 vCPU / 24GB, gratuito permanente) se
   precisar de 24/7 sem depender do PC ligado. Sobe o mesmo Docker Compose.

O Heroku só faz sentido se o backend também for para lá — aí os dois no mesmo
lugar resolvem, por ~US$ 20/mês.

---

## 3. O que foi implementado

Um `docker compose up` sobe as quatro peças do monorepo. Detalhes operacionais
em [`docker-local.md`](docker-local.md).

```
infra/
├── docker-compose.yml              postgres + backend + frontend + n8n
├── .env.example                    todos os segredos vão aqui
├── backend.Dockerfile              maven → JRE, WAR executável
├── frontend.Dockerfile             node build → nginx
├── nginx.conf                      SPA + proxy /api → backend
└── postgres/init/01-create-databases.sql
```

Mais `docs/docker-local.md`, os dois `.dockerignore`, o `README.md` reescrito nas
seções de setup e uma guarda de `gh` no `scripts/auto-commit.ps1`.

### Três decisões que não são cosméticas

**1. `FINTECH_API_URL=http://backend:8082`, não `localhost`.**
Dentro do container do n8n, `localhost` é o próprio n8n. Como os quatro serviços
estão na mesma rede do compose, não é preciso `host.docker.internal` em lugar
nenhum.

**2. nginx com proxy `/api`, em vez de servir o front direto.**
O backend **não tem nenhuma configuração de CORS** — não existe
`SecurityFilterChain` nem `WebMvcConfigurer` no projeto. Servir o front de outra
origem quebraria todas as chamadas do axios. Com o proxy, browser e API ficam na
mesma origem e o problema não existe. O `proxy_pass` com barra final replica
exatamente o `rewrite` do `vite.config.js`.

**3. Portas idênticas ao setup local (8082 / 3000 / 5678).**
Mantém `docs/n8n-extratos.md`, o `vite.config.js` e os defaults do
`application.yaml` válidos sem edição.

### Topologia

```
navegador → localhost:3000 (nginx)
              ├─ /       → build estático do Vite
              └─ /api/*  → http://backend:8082/*   (remove o prefixo /api)

backend → http://n8n:5678/webhook/extratos/processar
n8n     → http://backend:8082/extratos/{id}/status    (PATCH)
          http://backend:8082/extratos/{id}/callback  (POST)
backend → jdbc:postgresql://postgres:5432/fintech_app_dev
```

### Onde vai cada segredo

Tudo em `infra/.env` (copiado de `.env.example`, coberto pela regra `.env` do
`.gitignore`), com uma exceção: **as credenciais de terceiros ficam na UI do
n8n**, porque credenciais não viajam no export do JSON.

| Segredo | Onde | Obrigatório |
|---|---|---|
| `POSTGRES_PASSWORD` | `infra/.env` | sim |
| `INTERNAL_API_KEY` | `infra/.env` (injetado nos dois containers) | sim |
| `N8N_ENCRYPTION_KEY` | `infra/.env` | sim — se mudar, perde as credenciais salvas |
| `N8N_CALLBACK_SECRET` | `infra/.env` | deixar **vazio** (ver pendência abaixo) |
| Chave da Anthropic | UI do n8n → Credentials → Anthropic API | sim |
| Token Telegram / Evolution | UI do n8n → Credentials | só workflows 03/04 |

### O que foi verificado

**Validado:** `mvn package` roda e o `spring-boot-maven-plugin` gera um WAR
executável (`Main-Class: org.springframework.boot.loader.launch.WarLauncher`,
Tomcat em `WEB-INF/lib-provided/`). Era o risco real do `packaging: war` com
Tomcat em escopo `provided` — confirma que o `java -jar app.war` do Dockerfile
funciona. Também conferido que toda variável sem default no compose existe no
`.env.example`.

**Não validado:** **o Docker não está instalado na máquina** — `docker` não
existe no PATH. Nenhum `docker compose up` foi executado. O primeiro
`--build` é o teste de verdade.

---

## 4. Pendências conhecidas

### HMAC do callback assinado com segredo vazio

Os nós `Assinar payload sucesso` e `Assinar payload erro` (tipo
`n8n-nodes-base.crypto`) estão com o campo **Secret vazio**. O nó ⚙️ Config
expõe `callbackSecret`, mas ninguém o consome.

Consequência: preencher `N8N_CALLBACK_SECRET` faz o backend calcular um HMAC
diferente do enviado e **rejeitar todo callback**. Enquanto não corrigir, deixe
a variável vazia — o backend pula a verificação, com aviso no log.

Correção: no campo Secret dos dois nós, usar a expressão que lê o Config —
`{{ $('Config').first().json.callbackSecret }}`.

### Churn do graphify no auto-commit

O `watch-and-commit.ps1` faz `git add .` e commita tudo, inclusive os artefatos
que o hook de pós-commit do graphify regenera a cada commit. Isso cria um ciclo:
commit → rebuild → diretório sujo → novo commit (~44k linhas de churn em JSON
por rodada). Cortar adicionando `graphify-out/cache/` ao `.gitignore` ou à lista
`$IGNORE` do watcher. Não alterado por não ter sido pedido — o `graphify-out/`
versionado parece intencional.

### Migrar o Core IA para o backend

Ver seção 1. Removeria o n8n do caminho crítico do app.

---

## Referências

- [Host n8n — documentação oficial](https://docs.n8n.io/deploy/host-n8n)
- [Deploy n8n to Heroku](https://docs.n8n.io/deploy/host-n8n/install-options/use-a-cloud-provider/deploy-to-heroku)
- [Eco Dyno Hours — Heroku Dev Center](https://devcenter.heroku.com/articles/eco-dyno-hours)
- [Heroku Postgres Essential plans](https://www.heroku.com/blog/heroku-postgres-essential-launch/)
- [`docker-local.md`](docker-local.md) — passo a passo operacional
- [`n8n-extratos.md`](n8n-extratos.md) — o caminho do arquivo, do upload à revisão
