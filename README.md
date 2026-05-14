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
├── backend/        # API REST — Java 17 + Spring Boot
├── frontend/       # Interface — React 18 + TypeScript
├── infra/          # Docker Compose e configurações de infraestrutura
├── docs/           # Documentação, ADRs e diagramas
└── organizacao/    # Roadmap, to-do list e planejamento
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

## 🚀 Setup local em 5 passos

### 1. Clonar o repositório

```bash
git clone https://github.com/SEU_USUARIO/NOME_DO_REPO.git
cd fintech-app
```

### 2. Configurar variáveis de ambiente

```bash
cp infra/.env.example infra/.env
```

Abra o arquivo `infra/.env` e preencha os valores:

```env
# Banco de dados
POSTGRES_USER=fintech
POSTGRES_PASSWORD=fintech123
POSTGRES_DB=fintech_db

# JWT
JWT_SECRET=sua_chave_secreta_muito_longa_aqui

# IA
AI_API_KEY=sua_api_key_claude_ou_openai

# Interno (N8N → API)
INTERNAL_API_KEY=chave_interna_para_n8n
```

### 3. Subir a infraestrutura

```bash
cd infra
docker-compose up -d
```

Aguarde todos os serviços ficarem saudáveis:

```bash
docker-compose ps
# PostgreSQL, Redis e RabbitMQ devem estar com status "healthy"
```

### 4. Rodar o backend

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Acesse a documentação da API em: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Health check: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

### 5. Rodar o frontend

```bash
cd frontend
npm install
npm run dev
```

Acesse o sistema em: [http://localhost:5173](http://localhost:5173)

---

## 🧪 Rodar os testes

```bash
# Backend — testes unitários e de integração
cd backend
mvn verify

# Frontend — testes de componentes
cd frontend
npm test
```

---

## 🐳 Serviços do Docker Compose

| Serviço | Porta | Painel |
|---|---|---|
| PostgreSQL | 5432 | — |
| Redis | 6379 | — |
| RabbitMQ | 5672 | [localhost:15672](http://localhost:15672) (guest/guest) |
| pgAdmin *(dev)* | 5050 | [localhost:5050](http://localhost:5050) |

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
