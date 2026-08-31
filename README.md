# 💸 FinTech App

![CI](https://github.com/SEU_USUARIO/NOME_DO_REPO/actions/workflows/ci.yml/badge.svg)
![Deploy](https://github.com/SEU_USUARIO/NOME_DO_REPO/actions/workflows/cd.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![React](https://img.shields.io/badge/React-18-61DAFB?logo=react)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)

> Sistema de controle financeiro pessoal com importação automática de extratos bancários via PDF, classificação por IA e relatórios detalhados.

---

## 📁 Estrutura do repositório

```
fintech-app/
├── backend/fintech_app/    # API REST — Java 17 + Spring Boot 4
├── frontend/fintech_app/   # Interface — React 18 + Vite
├── infra/                  # Docker Compose, Dockerfiles e nginx
├── automacao/n8n/          # Workflows de importação de extratos (JSON)
├── docs/                   # Documentação, ADRs e diagramas
├── scripts/                # SQL de schema/carga e auto-commit
└── organizacao/            # Roadmap, to-do list e planejamento
```

---

## ⚙ Pré-requisitos

Antes de rodar o projeto, instale:

| Ferramenta | Versão | Download |
|---|---|---|
| Java JDK | 17+ | [adoptium.net](https://adoptium.net) |
| Maven | 3.9+ | [maven.apache.org](https://maven.apache.org) |
| Node.js | 20+ | [nodejs.org](https://nodejs.org) |
| Docker Desktop | Latest | [docker.com](https://www.docker.com/products/docker-desktop) |
| Git | Latest | [git-scm.com](https://git-scm.com) |

---

## 🚀 Setup local

### Opção A — tudo em Docker (recomendado)

Sobe backend, frontend, PostgreSQL e n8n de uma vez. Detalhes completos em
[`docs/docker-local.md`](docs/docker-local.md).

```bash
git clone https://github.com/SEU_USUARIO/NOME_DO_REPO.git
cd fintech-app/infra

cp .env.example .env      # preencha os campos marcados <<< PREENCHER >>>
docker compose up -d --build
```

| O quê | Onde |
|---|---|
| App | [localhost:3000](http://localhost:3000) |
| Swagger | [localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| Health | [localhost:8082/actuator/health](http://localhost:8082/actuator/health) |
| n8n | [localhost:5678](http://localhost:5678) |

Na primeira subida, importe os workflows de automação no n8n:

```bash
docker compose exec n8n n8n import:workflow --separate --input=/workflows
```

Depois cadastre a credencial **Anthropic API** na UI do n8n e ative o workflow
`Extratos · Entrada App (backend)`. Credenciais não vêm no export do JSON.

### Opção B — rodar na máquina

Útil para desenvolver com hot-reload. O banco e o n8n continuam vindo do Docker.

```bash
cd infra && docker compose up -d postgres n8n

# backend  → http://localhost:8082
cd backend/fintech_app
./mvnw spring-boot:run

# frontend → http://localhost:3000
cd frontend/fintech_app
npm install && npm run dev
```

O backend precisa de `DB_USERNAME`, `DB_PASSWORD` e `INTERNAL_API_KEY` no
ambiente — os mesmos valores que estão em `infra/.env`.

---

## 🔑 Onde ficam as chaves e segredos

Tudo em **`infra/.env`** (copiado de `infra/.env.example`, ignorado pelo Git),
com uma exceção: as credenciais de terceiros do n8n ficam na UI dele.

| Segredo | Onde | Para quê |
|---|---|---|
| `POSTGRES_PASSWORD` | `infra/.env` | banco + datasource do backend |
| `INTERNAL_API_KEY` | `infra/.env` | header `X-Internal-Api-Key` entre backend e n8n |
| `N8N_CALLBACK_SECRET` | `infra/.env` | HMAC do header `X-N8N-Signature` |
| `N8N_ENCRYPTION_KEY` | `infra/.env` | criptografa as credenciais salvas no n8n |
| Chave da Anthropic | UI do n8n → Credentials | nó *Claude - Extrair e Classificar* |
| Token do bot Telegram | UI do n8n → Credentials | workflow 03 |
| Chave da Evolution API | UI do n8n → Credentials | workflow 04 |

---

## 🧪 Rodar os testes

```bash
# Backend — testes unitários e de integração
cd backend/fintech_app
./mvnw verify

# Frontend — testes de componentes
cd frontend/fintech_app
npm test
```

---

## 🐳 Serviços do Docker Compose

Definidos em [`infra/docker-compose.yml`](infra/docker-compose.yml).

| Serviço | Porta | Imagem / build |
|---|---|---|
| `frontend` | 3000 | build do Vite servido por nginx (proxy `/api` → backend) |
| `backend` | 8082 | Spring Boot 4 / Java 17, WAR executável |
| `postgres` | 5432 | `postgres:16-alpine` |
| `n8n` | 5678 | `n8nio/n8n` — self-hosted, Community Edition (gratuito) |

---

## 🌿 Estratégia de branches

| Branch | Uso |
|---|---|
| `main` | Produção — deploy automático |
| `develop` | Integração — CI automático |
| `feature/*` | Novas funcionalidades |
| `hotfix/*` | Correções urgentes em produção |

**Regra:** nunca commitar diretamente em `main` ou `develop`. Sempre abrir um Pull Request.

---

## 📦 Tecnologias utilizadas

**Backend**
- Java 17 + Spring Boot 3
- PostgreSQL 16
- Redis 7
- RabbitMQ 3
- Flyway (migrations)
- JWT (autenticação)

**Frontend**
- React 18 + TypeScript
- Vite
- Tailwind CSS
- React Query
- Recharts

**Automação**
- N8N (integração WhatsApp/Telegram)
- Claude API (classificação de transações)
- GitHub Actions (CI/CD)

---

## 👥 Time

| Nome | Papel |
|---|---|
| Gustavo | Desenvolvimento |
| [Sócio] | [Papel] |

---

> Dúvidas? Abra uma [issue](https://github.com/SEU_USUARIO/NOME_DO_REPO/issues) ou entre em contato com o time.
