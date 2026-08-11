# Guia de uso dos dumps — FinApp

## Arquivos gerados

| Arquivo | Formato | Conteúdo | Uso |
|---|---|---|---|
| `finapp_schema.sql` | SQL puro | DDL + seeds + usuário demo | Ponto de partida em ambiente novo |
| `finapp_dump_completo.sql` | SQL puro | Schema + todos os dados | Backup legível / portabilidade |
| `finapp_dump_schema_only.sql` | SQL puro | Apenas DDL (sem dados) | CI/CD, migrations |
| `finapp_dump.pgdump` | Binário custom | Schema + dados comprimidos | Backup de produção (mais rápido) |

---

## 1. Pré-requisitos

```bash
# Ubuntu / Debian
sudo apt install postgresql postgresql-client

# macOS (Homebrew)
brew install postgresql@16

# Versão mínima necessária
psql --version   # PostgreSQL 14+
```

---

## 2. Criando o banco de dados

Sempre crie o banco e o usuário antes de restaurar qualquer dump.

```bash
# Acessa o prompt do postgres
sudo -u postgres psql

-- Dentro do psql:
CREATE DATABASE finapp;
CREATE USER finapp_app WITH PASSWORD 'senha_forte_aqui';
GRANT ALL PRIVILEGES ON DATABASE finapp TO finapp_app;
\c finapp
GRANT ALL ON SCHEMA public TO finapp_app;
\q
```

---

## 3. Cenários de uso

### 3.1 Ambiente local do zero (desenvolvimento)

Use o `finapp_schema.sql` — ele cria tudo em uma só passagem:
extensões, tabelas, índices, partições, seeds e o usuário demo.

```bash
psql \
  --host=localhost \
  --port=5432 \
  --username=finapp_app \
  --dbname=finapp \
  --file=finapp_schema.sql
```

**Quando usar:** primeira configuração da máquina de um desenvolvedor,
criação de um banco de testes zerado, reset completo do ambiente.

---

### 3.2 Restaurar backup completo legível

```bash
psql \
  --host=localhost \
  --port=5432 \
  --username=finapp_app \
  --dbname=finapp \
  --file=finapp_dump_completo.sql
```

**Quando usar:** você tem um banco em branco e quer restaurar um estado
salvo anteriormente de forma transparente (o arquivo é texto, você pode
abrir e inspecionar antes de rodar).

---

### 3.3 Restaurar backup binário comprimido (produção)

O arquivo `.pgdump` é gerado com `--format=custom`, por isso
**só funciona com `pg_restore`**, não com `psql`.

```bash
pg_restore \
  --host=localhost \
  --port=5432 \
  --username=finapp_app \
  --dbname=finapp \
  --no-owner \
  --no-acl \
  --verbose \
  finapp_dump.pgdump
```

**Flags úteis:**

```bash
# Restaurar apenas uma tabela específica
pg_restore ... --table=transacoes finapp_dump.pgdump

# Restaurar em paralelo (8 threads — acelera muito em bases grandes)
pg_restore ... --jobs=8 finapp_dump.pgdump

# Ver o conteúdo do dump sem restaurar
pg_restore --list finapp_dump.pgdump
```

**Quando usar:** restauração de produção ou staging, onde velocidade e
compressão importam.

---

### 3.4 Aplicar apenas o schema (CI/CD / migrations)

```bash
psql \
  --host=localhost \
  --port=5432 \
  --username=finapp_app \
  --dbname=finapp_test \
  --file=finapp_dump_schema_only.sql
```

**Quando usar:** pipelines de integração contínua, onde você quer criar
a estrutura limpa e depois rodar os seeds via código da aplicação.

---

## 4. Gerando novos dumps

### Backup completo (use isto para produção)

```bash
PGPASSWORD='sua_senha' pg_dump \
  --host=seu_host \
  --port=5432 \
  --username=finapp_app \
  --dbname=finapp \
  --format=custom \
  --no-owner \
  --no-acl \
  --encoding=UTF8 \
  --file="finapp_$(date +%Y%m%d_%H%M%S).pgdump"
```

### Backup agendado (cron — diário às 03h00)

```bash
# Edite o crontab: crontab -e
0 3 * * * PGPASSWORD='senha' pg_dump \
  -h localhost -U finapp_app -d finapp \
  -Fc --no-owner \
  -f "/backups/finapp_$(date +\%Y\%m\%d).pgdump" \
  && find /backups -name "*.pgdump" -mtime +30 -delete
```

---

## 5. Usando com Docker (mais comum em dev)

### docker-compose.yml mínimo

```yaml
version: "3.9"
services:
  db:
    image: postgres:16-alpine
    restart: unless-stopped
    environment:
      POSTGRES_DB: finapp
      POSTGRES_USER: finapp_app
      POSTGRES_PASSWORD: senha_forte_aqui
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./finapp_schema.sql:/docker-entrypoint-initdb.d/01_schema.sql
      # O Postgres executa automaticamente todo .sql em initdb.d na 1ª inicialização

volumes:
  pgdata:
```

```bash
# Subir o banco (já executa o schema automaticamente)
docker compose up -d db

# Verificar se funcionou
docker compose exec db psql -U finapp_app -d finapp -c "\dt"
```

### Restaurar dump em container já rodando

```bash
# Copiar o arquivo para dentro do container
docker cp finapp_dump.pgdump <nome_container>:/tmp/

# Executar pg_restore dentro do container
docker exec <nome_container> pg_restore \
  -U finapp_app -d finapp --no-owner /tmp/finapp_dump.pgdump
```

---

## 6. Usando com Supabase / Neon / Railway

Todos expõem uma connection string no formato:

```
postgresql://finapp_app:senha@host:5432/finapp
```

```bash
# Substituir pela connection string do painel do serviço
psql "postgresql://finapp_app:senha@db.xxxx.supabase.co:5432/postgres" \
  --file=finapp_schema.sql

# Ou com pg_restore (dump binário)
pg_restore \
  --dbname="postgresql://finapp_app:senha@host:5432/finapp" \
  --no-owner finapp_dump.pgdump
```

**Supabase:** desabilite Row Level Security nas tabelas ou configure
as policies antes de restaurar dados.

---

## 7. Variáveis de ambiente recomendadas

Evite expor senhas na linha de comando; prefira o `.env`:

```bash
# .env (nunca comite este arquivo)
PGHOST=localhost
PGPORT=5432
PGDATABASE=finapp
PGUSER=finapp_app
PGPASSWORD=senha_forte_aqui

# Com as variáveis exportadas, os comandos ficam limpos:
export $(cat .env | xargs)
psql --file=finapp_schema.sql
pg_dump --format=custom --file=backup.pgdump
```

---

## 8. Checklist pós-restauração

Execute para confirmar que tudo está certo:

```sql
-- Contagem geral
SELECT schemaname, tablename, n_live_tup AS linhas
FROM pg_stat_user_tables
ORDER BY tablename;

-- Índices criados
SELECT indexname, tablename FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename;

-- Partições de auditoria
SELECT inhrelid::regclass AS particao
FROM pg_inherits
WHERE inhparent = 'auditoria_eventos'::regclass;

-- Usuário demo
SELECT codigo, nome_completo, email, moeda
FROM usuarios WHERE codigo = 'FIN-DEMO01';

-- Transações do usuário demo (direção agora vem da categoria, não de transacoes.tipo)
SELECT t.valor, t.data_transacao, t.status_revisao, c.tipo AS categoria_tipo
FROM transacoes t
JOIN categorias c ON c.categoria_id = t.categoria_id
ORDER BY t.data_transacao;
```

---

## 9. Observações de segurança importantes

- O campo `cpf` está marcado como `ENCRYPTED_CPF_PLACEHOLDER` — em
  produção, use **AES-256** via `pgcrypto` (`pgp_sym_encrypt`) ou
  criptografe na camada da aplicação antes de inserir.
- O `senha_hash` do usuário demo é um placeholder; **nunca insira
  senhas reais via SQL em produção**.
- A tabela `auditoria_eventos` é imutável por design — nunca conceda
  `UPDATE` ou `DELETE` nessa tabela ao usuário da aplicação.
- Adicione novas partições mensais em `auditoria_eventos` com
  antecedência (recomendado: 2 meses à frente).
