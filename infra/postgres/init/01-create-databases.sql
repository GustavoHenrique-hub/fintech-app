-- ============================================================================
--  Executado UMA ÚNICA VEZ, no primeiro boot do container postgres (quando o
--  volume pgdata ainda está vazio). Se precisar rodar de novo:
--      docker compose down -v && docker compose up -d
--  (o -v apaga o volume — e junto todos os dados)
--
--  O POSTGRES_DB do compose já cria fintech_app_dev. Aqui criamos os outros
--  dois, para que trocar SPRING_PROFILE=hml/prd no .env funcione sem passo
--  manual (application-hml.yaml e application-prd.yaml apontam para eles).
--
--  A extensão pgcrypto acompanha scripts/fintechapp_schema.sql.
-- ============================================================================

CREATE DATABASE fintech_app_hml;
CREATE DATABASE fintech_app_prd;

\connect fintech_app_dev
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

\connect fintech_app_hml
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

\connect fintech_app_prd
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
