# Rodando o monorepo inteiro com Docker

Guia para subir backend, frontend e banco como um único projeto via Docker Compose.
Escrito a partir da configuração real do repositório em 2026-08-13.

---

## 1. O que o monorepo tem hoje

| Peça | Caminho | Fato relevante |
|---|---|---|
| Backend | `backend/fintech_app/` | Spring Boot 4.0.5, Java 17, Maven wrapper, porta `${SERVER_PORT:8082}` |
| Frontend | `frontend/fintech_app/` | Vite 5 + React 18, dev server na porta 3000 |
| Banco | — | PostgreSQL, DB `fintech_app_dev`, via `DB_HOST` / `DB_PORT` / `DB_USERNAME` / `DB_PASSWORD` |
| SQL | `scripts/*.sql` | `fintechapp_schema.sql` e `fintechapp_carga.sql` são PostgreSQL |
| Uploads | `backend/fintech_app/uploads/extratos` | `${APP_UPLOAD_DIR}` — precisa de volume para persistir |

Dois pontos definem todo o desenho:

1. `frontend/fintech_app/src/services/api.js:5` → `const BASE_URL = import.meta.env.VITE_API_URL ?? "/api";`
2. `frontend/fintech_app/vite.config.js` proxia `/api` → `http://localhost:8082` **e remove o prefixo `/api`** no `rewrite`.

O item 2 existe **apenas no dev server do Vite**. Ao containerizar o frontend como build
estático, esse proxy desaparece — é o ponto onde a maioria das migrações quebra.
Quem assume esse papel dentro do Docker é o nginx.

---

## 2. Arquivos a criar

Todos novos, nenhum arquivo existente precisa ser alterado.

```
fintech-app/
├── docker-compose.yml                      ← raiz, orquestra tudo
├── .env                                    ← credenciais (adicione ao .gitignore)
├── backend/fintech_app/Dockerfile
├── backend/fintech_app/.dockerignore
├── frontend/fintech_app/Dockerfile
├── frontend/fintech_app/.dockerignore
└── frontend/fintech_app/nginx.conf
```

---

## 3. Backend — `backend/fintech_app/Dockerfile`

Multi-stage: compila com Maven, roda apenas com JRE.

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B        # cacheia deps; só reexecuta se o pom mudar
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
```

O `COPY pom.xml` separado do `COPY src` é o que faz o cache de camadas funcionar:
alterar código-fonte não invalida o download das dependências.

---

## 4. Frontend — `frontend/fintech_app/Dockerfile`

```dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build                        # gera dist/

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

---

## 5. `frontend/fintech_app/nginx.conf` — o substituto do proxy do Vite

```nginx
server {
    listen 80;

    location /api/ {
        proxy_pass http://backend:8082/;   # a barra final remove o /api — igual ao rewrite do Vite
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;  # obrigatório: react-router usa rotas client-side
    }
}
```

Duas coisas aqui **não são opcionais**:

- A **barra final** em `proxy_pass http://backend:8082/`. Sem ela o backend recebe
  `/api/usuarios` em vez de `/usuarios`.
- O `try_files`. Sem ele, dar F5 em `/dashboard` retorna 404.

---

## 6. `docker-compose.yml` (raiz do monorepo)

```yaml
services:
  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: fintech_app_dev
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./scripts/fintechapp_schema.sql:/docker-entrypoint-initdb.d/01_schema.sql
      - ./scripts/fintechapp_carga.sql:/docker-entrypoint-initdb.d/02_carga.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USERNAME} -d fintech_app_dev"]
      interval: 5s
      retries: 10

  backend:
    build: ./backend/fintech_app
    environment:
      SPRING_PROFILE: dev
      DB_HOST: db              # nome do serviço, NÃO localhost
      DB_PORT: 5432
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}
      APP_UPLOAD_DIR: /app/uploads/extratos
    ports:
      - "8082:8082"
    volumes:
      - uploads:/app/uploads
    depends_on:
      db:
        condition: service_healthy

  frontend:
    build: ./frontend/fintech_app
    ports:
      - "3000:80"
    depends_on:
      - backend

volumes:
  pgdata:
  uploads:
```

---

## 7. `.env` (raiz)

```
DB_USERNAME=finapp_app
DB_PASSWORD=troque_essa_senha
```

Adicione `.env` ao `.gitignore`.

---

## 8. `.dockerignore` — não pule este passo

O repositório tem `node_modules/` e `target/` presentes no disco. Sem `.dockerignore`
o build context sobe centenas de MB, e o `node_modules` compilado no Windows contamina
o container Linux.

`frontend/fintech_app/.dockerignore`:

```
node_modules
dist
.git
```

`backend/fintech_app/.dockerignore`:

```
target
uploads
.git
```

---

## 9. Comandos

```bash
docker compose up --build -d      # primeira execução
docker compose logs -f backend    # acompanhar o backend
docker compose ps                 # status dos serviços
docker compose down               # parar (mantém o banco)
docker compose down -v            # parar E apagar o banco (recria o schema no próximo up)
```

- Frontend: <http://localhost:3000>
- Backend: <http://localhost:8082>

---

## 10. Armadilhas específicas deste projeto

**`DB_HOST` precisa ser `db`.**
Dentro da rede do Compose, `localhost` é o próprio container do backend. O default
`localhost:5432` do `application-dev.yaml` só funciona fora do Docker.

**Não monte `fintech_app_DBCREATION.sql` no initdb.**
Esse arquivo é DDL de **SQL Server 2012** (declarado no próprio cabeçalho). O Postgres
falha ao executá-lo. Apenas `fintechapp_schema.sql` e `fintechapp_carga.sql` são Postgres.

**`ddl-auto: update` conflita com os scripts de initdb.**
O Hibernate tentará ajustar o schema que o SQL já criou. Escolha um dos dois caminhos:
montar os `.sql` e trocar para `ddl-auto: validate`, ou manter `update` e não montar nada.
Manter os dois funciona por sorte, não por desenho.

**Os scripts de initdb rodam uma única vez**, na criação do volume vazio. Editar o `.sql`
depois não muda nada — é preciso `docker compose down -v` para recriar.

**`show-sql: true` + logging em `TRACE`** no `application.yaml` geram volume alto de log
em container. Vale sobrescrever via variável de ambiente no Compose se incomodar.

---

## 11. Variante: hot-reload no frontend

Para desenvolver com hot-reload em vez de build estático, o serviço `frontend` muda:
monta o código como volume e roda `npm run dev`, e nesse caso o proxy do Vite volta a
funcionar — o `nginx.conf` passa a ser usado só em produção.

```yaml
  frontend:
    image: node:20-alpine
    working_dir: /app
    command: sh -c "npm ci && npm run dev -- --host 0.0.0.0"
    volumes:
      - ./frontend/fintech_app:/app
      - /app/node_modules          # volume anônimo: isola o node_modules do host
    ports:
      - "3000:3000"
    depends_on:
      - backend
```

Nessa variante o `target` do proxy em `vite.config.js` precisa apontar para
`http://backend:8082` em vez de `http://localhost:8082`.
