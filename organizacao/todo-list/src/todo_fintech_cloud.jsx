import { useState, useEffect, useCallback, useMemo } from "react";

// ─── DADOS (idênticos ao original) ───────────────────────────────────────────

const SECTIONS = [
  {
    id: "infra", label: "Fundação & DevOps", icon: "🏗",
    color: "#64748B", light: "#F1F5F9", text: "#1E293B",
    items: [
      { id: "infra-repo", title: "Repositório GitHub — monorepo", subs: [
        { id: "infra-repo-1", text: "Criar monorepo com estrutura: /backend · /frontend · /infra · /docs" },
        { id: "infra-repo-2", text: ".gitignore para Java (*.class, target/, .env) e Node (node_modules, dist)" },
        { id: "infra-repo-3", text: "README.md inicial com badges de CI e instruções de setup" },
        { id: "infra-repo-4", text: "Branch protection: PR obrigatório para 'main' e 'develop'" },
        { id: "infra-repo-5", text: "Conventional commits via commitlint (.commitlintrc.json)" },
        { id: "infra-repo-6", text: "Pre-commit hooks: Checkstyle (Java) + ESLint + Prettier (JS/TS)" },
        { id: "infra-repo-7", text: "Strategy de branches: main → develop → feature/* e hotfix/*" },
      ]},
      { id: "infra-docker", title: "Docker Compose — ambiente local completo", subs: [
        { id: "infra-docker-1", text: "postgres:16-alpine (porta 5432, volume nomeado fintech_pgdata)" },
        { id: "infra-docker-2", text: "redis:7-alpine (porta 6379)" },
        { id: "infra-docker-3", text: "rabbitmq:3-management (5672 + painel admin 15672)" },
        { id: "infra-docker-4", text: "Healthcheck em todos os 3 serviços (depends_on com condition)" },
        { id: "infra-docker-5", text: ".env.example versionado com todas as variáveis e valores de exemplo" },
        { id: "infra-docker-6", text: "pgAdmin no perfil 'dev' para inspeção do banco" },
        { id: "infra-docker-7", text: "Redis Commander no perfil 'dev' para inspecionar cache" },
        { id: "infra-docker-8", text: "Verificar: 'docker-compose up --build' sem erros em máquina limpa" },
      ]},
      { id: "infra-ci", title: "Pipeline CI/CD — GitHub Actions", subs: [
        { id: "infra-ci-1", text: "ci.yml: roda em todo PR → develop (mvn verify + npm test + docker build)" },
        { id: "infra-ci-2", text: "cd.yml: roda no merge → main (build imagem multi-stage + push + deploy Railway)" },
        { id: "infra-ci-3", text: "Secrets no GitHub Actions: DATABASE_URL, REDIS_URL, JWT_SECRET, AI_API_KEY, RABBITMQ_URL" },
        { id: "infra-ci-4", text: "Badge de status do CI no README (verde/vermelho)" },
        { id: "infra-ci-5", text: "Smoke tests automatizados pós-deploy (login, health check, upload de teste)" },
        { id: "infra-ci-6", text: "OWASP Dependency Check no CI — falha se vulnerabilidade crítica" },
        { id: "infra-ci-7", text: "Rollback documentado: railway rollback {deployment-id} em < 2 minutos" },
      ]},
    ],
  },
  {
    id: "db-tabelas", label: "Banco de Dados — Tabelas", icon: "🗄",
    color: "#7C3AED", light: "#EDE9FE", text: "#4C1D95",
    items: [
      { id: "db-usuarios", title: "Tabela: usuarios", subs: [
        { id: "db-u-1",  text: "id UUID PK DEFAULT gen_random_uuid()" },
        { id: "db-u-2",  text: "codigo VARCHAR(12) UNIQUE NOT NULL  — gerado: FIN-XXXXXX" },
        { id: "db-u-3",  text: "cpf VARCHAR(255) NOT NULL  — AES-256 criptografado em repouso" },
        { id: "db-u-4",  text: "cpf_hash VARCHAR(64) UNIQUE  — SHA-256 para lookup sem descriptografar" },
        { id: "db-u-5",  text: "nome_completo VARCHAR(150) NOT NULL" },
        { id: "db-u-6",  text: "email VARCHAR(255) UNIQUE NOT NULL" },
        { id: "db-u-7",  text: "senha_hash VARCHAR(255) NOT NULL  — bcrypt" },
        { id: "db-u-8",  text: "telefone VARCHAR(20)" },
        { id: "db-u-9",  text: "telegram_chat_id BIGINT" },
        { id: "db-u-10", text: "whatsapp_id VARCHAR(100)" },
        { id: "db-u-11", text: "saldo_inicial_definido BOOLEAN DEFAULT FALSE  — gatekeeper do dashboard" },
        { id: "db-u-12", text: "moeda CHAR(3) NOT NULL DEFAULT 'BRL'" },
        { id: "db-u-13", text: "ativo BOOLEAN NOT NULL DEFAULT TRUE" },
        { id: "db-u-14", text: "email_verificado BOOLEAN NOT NULL DEFAULT FALSE" },
        { id: "db-u-15", text: "token_verificacao VARCHAR(100) · token_verificacao_expira TIMESTAMPTZ" },
        { id: "db-u-16", text: "ultimo_login TIMESTAMPTZ" },
        { id: "db-u-17", text: "tentativas_login SMALLINT DEFAULT 0 · bloqueado_ate TIMESTAMPTZ" },
        { id: "db-u-18", text: "refresh_token_hash VARCHAR(255)  — hash bcrypt do refresh token ativo" },
        { id: "db-u-19", text: "api_key_hash VARCHAR(255)  — hash bcrypt da API Key do bot" },
        { id: "db-u-20", text: "deleted_at TIMESTAMPTZ  — soft delete para pipeline LGPD" },
        { id: "db-u-21", text: "versao INT NOT NULL DEFAULT 1  — optimistic locking" },
        { id: "db-u-22", text: "criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW() · atualizado_em TIMESTAMPTZ" },
      ]},
      { id: "db-contas", title: "Tabela: contas_financeiras", subs: [
        { id: "db-cf-1", text: "id UUID PK" },
        { id: "db-cf-2", text: "usuario_id UUID FK NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE" },
        { id: "db-cf-3", text: "nome VARCHAR(100) NOT NULL  — ex: 'Nubank', 'Itaú CC', 'Carteira'" },
        { id: "db-cf-4", text: "tipo VARCHAR(20) CHECK(corrente|poupanca|cartao|dinheiro|investimento)" },
        { id: "db-cf-5", text: "banco VARCHAR(100)  — nome do banco" },
        { id: "db-cf-6", text: "saldo_inicial NUMERIC(15,2) NOT NULL DEFAULT 0" },
        { id: "db-cf-7", text: "padrao BOOLEAN DEFAULT FALSE  — conta padrão nos lançamentos" },
        { id: "db-cf-8", text: "ativa BOOLEAN DEFAULT TRUE" },
        { id: "db-cf-9", text: "criado_em TIMESTAMPTZ · atualizado_em TIMESTAMPTZ" },
      ]},
      { id: "db-categorias", title: "Tabela: categorias", subs: [
        { id: "db-cat-1", text: "id UUID PK" },
        { id: "db-cat-2", text: "nome VARCHAR(100) NOT NULL" },
        { id: "db-cat-3", text: "categoria_pai_id UUID FK REFERENCES categorias(id)  — hierarquia pai/filho" },
        { id: "db-cat-4", text: "tipo VARCHAR(10) CHECK(receita|gasto|ambos)" },
        { id: "db-cat-5", text: "icone VARCHAR(50)  — emoji ou nome de ícone" },
        { id: "db-cat-6", text: "cor_hex CHAR(7)  — ex: #DC2626" },
        { id: "db-cat-7", text: "padrao BOOLEAN DEFAULT FALSE  — TRUE = categoria global imutável" },
        { id: "db-cat-8", text: "usuario_id UUID FK  — NULL = global do sistema" },
        { id: "db-cat-9", text: "criado_em TIMESTAMPTZ" },
      ]},
      { id: "db-thresholds", title: "Tabela: categoria_thresholds", subs: [
        { id: "db-th-1", text: "id UUID PK" },
        { id: "db-th-2", text: "categoria_id UUID FK UNIQUE REFERENCES categorias(id)" },
        { id: "db-th-3", text: "threshold_auto SMALLINT DEFAULT 70 CHECK(0-100)  — % mínimo para confirmar sem revisão" },
        { id: "db-th-4", text: "threshold_alerta SMALLINT DEFAULT 50 CHECK(0-100)  — abaixo = alerta vermelho" },
        { id: "db-th-5", text: "ambiguidade_alta BOOLEAN DEFAULT FALSE  — força revisão mesmo com alta confiança" },
        { id: "db-th-6", text: "Seed: Streaming=90, Alimentação/Delivery=70, Outros=100 (nunca auto)" },
      ]},
      { id: "db-transacoes", title: "Tabela: transacoes", subs: [
        { id: "db-tr-1",  text: "id UUID PK" },
        { id: "db-tr-2",  text: "usuario_id UUID FK NOT NULL" },
        { id: "db-tr-3",  text: "conta_id UUID FK NOT NULL REFERENCES contas_financeiras(id)" },
        { id: "db-tr-4",  text: "extrato_id UUID FK NULLABLE  — NULL se lançamento manual" },
        { id: "db-tr-5",  text: "transacao_estorno_id UUID FK NULLABLE  — auto-referência para estornos" },
        { id: "db-tr-6",  text: "tipo VARCHAR(10) NOT NULL CHECK(receita|gasto)" },
        { id: "db-tr-7",  text: "descricao_original TEXT  — texto bruto do extrato" },
        { id: "db-tr-8",  text: "descricao_usuario VARCHAR(255)  — digitada manualmente" },
        { id: "db-tr-9",  text: "descricao_normalizada VARCHAR(255)  — uppercase, sem acentos, trim" },
        { id: "db-tr-10", text: "valor NUMERIC(15,2) NOT NULL CHECK(valor > 0)" },
        { id: "db-tr-11", text: "data_transacao DATE NOT NULL  — data real do evento" },
        { id: "db-tr-12", text: "data_lancamento TIMESTAMPTZ NOT NULL DEFAULT NOW()  — imutável" },
        { id: "db-tr-13", text: "categoria_id UUID FK REFERENCES categorias(id)" },
        { id: "db-tr-14", text: "subcategoria VARCHAR(100)" },
        { id: "db-tr-15", text: "estabelecimento VARCHAR(255)" },
        { id: "db-tr-16", text: "origem VARCHAR(20) NOT NULL DEFAULT 'manual' CHECK(manual|pdf|api)" },
        { id: "db-tr-17", text: "status_revisao VARCHAR(30) NOT NULL DEFAULT 'extraida' CHECK(extraida|classificada|pendente_revisao|confirmada|ignorada|arquivada)" },
        { id: "db-tr-18", text: "confianca_ia SMALLINT CHECK(0-100)" },
        { id: "db-tr-19", text: "recorrente BOOLEAN DEFAULT FALSE · periodo_recorrencia VARCHAR(20)" },
        { id: "db-tr-20", text: "observacao TEXT" },
        { id: "db-tr-21", text: "deleted_at TIMESTAMPTZ  — soft delete" },
        { id: "db-tr-22", text: "versao INT NOT NULL DEFAULT 1  — optimistic locking" },
        { id: "db-tr-23", text: "criado_em TIMESTAMPTZ · atualizado_em TIMESTAMPTZ" },
      ]},
      { id: "db-extratos", title: "Tabela: extratos", subs: [
        { id: "db-ex-1",  text: "id UUID PK" },
        { id: "db-ex-2",  text: "usuario_id UUID FK NOT NULL" },
        { id: "db-ex-3",  text: "conta_id UUID FK NOT NULL  — conta vinculada ao extrato" },
        { id: "db-ex-4",  text: "arquivo_nome VARCHAR(255)  — nome original (apenas para exibição)" },
        { id: "db-ex-5",  text: "arquivo_uuid VARCHAR(255) NOT NULL  — UUID gerado pelo sistema (path no storage)" },
        { id: "db-ex-6",  text: "hash_arquivo VARCHAR(64) NOT NULL  — SHA-256 para deduplicação" },
        { id: "db-ex-7",  text: "banco_detectado VARCHAR(100)" },
        { id: "db-ex-8",  text: "parser_versao_id UUID FK REFERENCES parser_versoes(id)" },
        { id: "db-ex-9",  text: "score_extracao NUMERIC(4,3)  — qualidade: linhas_extraidas/linhas_esperadas" },
        { id: "db-ex-10", text: "periodo_inicio DATE · periodo_fim DATE" },
        { id: "db-ex-11", text: "status VARCHAR(30) NOT NULL DEFAULT 'upload_recebido' CHECK(upload_recebido|validando|na_fila|extraindo|classificando|aguardando_ia|pendente_revisao|parcialmente_revisado|concluido|erro_formato|erro_extracao|erro_classificacao|erro_timeout|cancelado|reprocessando)" },
        { id: "db-ex-12", text: "total_lancamentos INT DEFAULT 0" },
        { id: "db-ex-13", text: "lancamentos_confirmados INT DEFAULT 0 · lancamentos_pendentes INT DEFAULT 0 · lancamentos_ignorados INT DEFAULT 0" },
        { id: "db-ex-14", text: "versao INT NOT NULL DEFAULT 1  — optimistic locking" },
        { id: "db-ex-15", text: "criado_em TIMESTAMPTZ · atualizado_em TIMESTAMPTZ" },
      ]},
      { id: "db-regras", title: "Tabela: regras_classificacao", subs: [
        { id: "db-rc-1",  text: "id UUID PK" },
        { id: "db-rc-2",  text: "usuario_id UUID FK NOT NULL" },
        { id: "db-rc-3",  text: "termo VARCHAR(255) NOT NULL  — trecho normalizado da descrição" },
        { id: "db-rc-4",  text: "categoria_id UUID FK NOT NULL" },
        { id: "db-rc-5",  text: "subcategoria VARCHAR(100)" },
        { id: "db-rc-6",  text: "score_confianca NUMERIC(4,3) NOT NULL DEFAULT 1.000  — decai com erros" },
        { id: "db-rc-7",  text: "vezes_aplicada INT DEFAULT 0 · vezes_corretas INT DEFAULT 0 · vezes_incorretas INT DEFAULT 0" },
        { id: "db-rc-8",  text: "correcoes_consecutivas SMALLINT DEFAULT 0  — anti-loop: suspende ao atingir 3" },
        { id: "db-rc-9",  text: "congelada BOOLEAN DEFAULT FALSE · congelada_motivo TEXT · congelada_ate DATE" },
        { id: "db-rc-10", text: "criada_por VARCHAR(20) DEFAULT 'usuario' CHECK(usuario|ia_auto|admin)" },
        { id: "db-rc-11", text: "ultima_aplicacao TIMESTAMPTZ · ultima_correcao TIMESTAMPTZ" },
        { id: "db-rc-12", text: "criado_em TIMESTAMPTZ" },
      ]},
      { id: "db-clas-logs", title: "Tabela: classificacao_logs", subs: [
        { id: "db-cl-1",  text: "id UUID PK" },
        { id: "db-cl-2",  text: "transacao_id UUID FK NOT NULL" },
        { id: "db-cl-3",  text: "usuario_id UUID FK NOT NULL" },
        { id: "db-cl-4",  text: "estrategia VARCHAR(20) NOT NULL CHECK(regra_usuario|dicionario|ia_api|fallback)" },
        { id: "db-cl-5",  text: "modelo_ia_versao VARCHAR(50)  — ex: claude-sonnet-4-20250514" },
        { id: "db-cl-6",  text: "prompt_versao VARCHAR(20)  — ex: v1.3" },
        { id: "db-cl-7",  text: "prompt_hash VARCHAR(64)  — SHA-256 do prompt enviado" },
        { id: "db-cl-8",  text: "resposta_raw TEXT  — resposta bruta da IA (para auditoria)" },
        { id: "db-cl-9",  text: "categoria_sugerida UUID FK  — categoria proposta pela classificação" },
        { id: "db-cl-10", text: "confianca SMALLINT NOT NULL CHECK(0-100)" },
        { id: "db-cl-11", text: "tokens_usados INT · latencia_ms INT  — custo e performance da IA" },
        { id: "db-cl-12", text: "categoria_final UUID FK  — categoria após revisão do usuário" },
        { id: "db-cl-13", text: "corrigida BOOLEAN DEFAULT FALSE · corrigida_em TIMESTAMPTZ · motivo_correcao TEXT" },
        { id: "db-cl-14", text: "criado_em TIMESTAMPTZ" },
      ]},
      { id: "db-jobs", title: "Tabela: processamento_jobs", subs: [
        { id: "db-j-1",  text: "id UUID PK" },
        { id: "db-j-2",  text: "extrato_id UUID FK REFERENCES extratos(id)" },
        { id: "db-j-3",  text: "tipo VARCHAR(30) NOT NULL CHECK(extracao_pdf|classificacao_ia|notificacao|geracao_pdf|snapshot|anonimizacao)" },
        { id: "db-j-4",  text: "status VARCHAR(30) NOT NULL DEFAULT 'enfileirado' CHECK(enfileirado|iniciando|processando|aguardando_ia|concluido|falha_ia|falha_parser|timeout|retry_1|retry_2|retry_3|dead_letter|cancelado)" },
        { id: "db-j-5",  text: "tentativas SMALLINT DEFAULT 0 · max_tentativas SMALLINT DEFAULT 3" },
        { id: "db-j-6",  text: "payload JSONB  — dados para reprocessamento" },
        { id: "db-j-7",  text: "erro_mensagem TEXT · worker_id VARCHAR(100)" },
        { id: "db-j-8",  text: "lock_expires_at TIMESTAMPTZ  — expiração do lock distribuído Redis" },
        { id: "db-j-9",  text: "correlation_id UUID  — rastreabilidade ponta a ponta" },
        { id: "db-j-10", text: "enfileirado_em TIMESTAMPTZ · iniciado_em TIMESTAMPTZ · concluido_em TIMESTAMPTZ · proximo_retry TIMESTAMPTZ" },
      ]},
      { id: "db-snapshots", title: "Tabela: snapshots_financeiros", subs: [
        { id: "db-sn-1", text: "id UUID PK" },
        { id: "db-sn-2", text: "usuario_id UUID FK NOT NULL" },
        { id: "db-sn-3", text: "conta_id UUID FK  — NULL = consolidado de todas as contas" },
        { id: "db-sn-4", text: "ano SMALLINT NOT NULL · mes SMALLINT NOT NULL CHECK(1-12)" },
        { id: "db-sn-5", text: "saldo_inicial NUMERIC(15,2) NOT NULL  — saldo no início do mês" },
        { id: "db-sn-6", text: "total_receitas NUMERIC(15,2) NOT NULL DEFAULT 0" },
        { id: "db-sn-7", text: "total_gastos NUMERIC(15,2) NOT NULL DEFAULT 0" },
        { id: "db-sn-8", text: "saldo_final NUMERIC(15,2) NOT NULL  — saldo_inicial + receitas - gastos" },
        { id: "db-sn-9", text: "fechado BOOLEAN DEFAULT FALSE · fechado_em TIMESTAMPTZ  — bloqueia edição retroativa" },
        { id: "db-sn-10", text: "UNIQUE(usuario_id, conta_id, ano, mes)" },
      ]},
      { id: "db-auditoria", title: "Tabela: auditoria_eventos", subs: [
        { id: "db-au-1", text: "id UUID PK" },
        { id: "db-au-2", text: "correlation_id UUID NOT NULL  — rastreabilidade distribuída" },
        { id: "db-au-3", text: "usuario_id UUID FK NULLABLE  — NULL para eventos de sistema" },
        { id: "db-au-4", text: "entidade VARCHAR(50) NOT NULL  — ex: 'transacao', 'extrato', 'usuario'" },
        { id: "db-au-5", text: "entidade_id UUID NOT NULL" },
        { id: "db-au-6", text: "acao VARCHAR(30) NOT NULL CHECK(CREATE|UPDATE|DELETE|LOGIN|LOGOUT|UPLOAD|CLASSIFY|CONFIRM|REJECT|EXPORT|REPROCESS|API_KEY_GEN|PASSWORD_CHANGE)" },
        { id: "db-au-7", text: "dados_anteriores JSONB  — snapshot before (NUNCA incluir CPF, senha, tokens)" },
        { id: "db-au-8", text: "dados_novos JSONB  — snapshot after" },
        { id: "db-au-9", text: "ip_origem INET  — mascarar último octeto" },
        { id: "db-au-10", text: "user_agent TEXT" },
        { id: "db-au-11", text: "origem VARCHAR(20) CHECK(web|bot_whatsapp|bot_telegram|api|sistema)" },
        { id: "db-au-12", text: "criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()  — IMUTÁVEL: sem UPDATE ou DELETE" },
        { id: "db-au-13", text: "Tabela particionada por mês (PARTITION BY RANGE criado_em)" },
      ]},
      { id: "db-lgpd", title: "Tabelas: consentimentos_lgpd e notificacoes", subs: [
        { id: "db-lg-1", text: "consentimentos_lgpd: usuario_id, tipo CHECK(tratamento_dados_financeiros|uso_ia|armazenamento_extrato|bot_whatsapp|bot_telegram), versao_politica, consentido, ip_origem, criado_em, revogado_em, revogado_motivo" },
        { id: "db-lg-2", text: "notificacoes: usuario_id, canal CHECK(whatsapp|telegram|email), tipo, titulo, mensagem, enviada BOOLEAN, enviada_em, erro, tentativas, criado_em" },
        { id: "db-lg-3", text: "parser_versoes: banco, versao, ativo, score_qualidade, total_usos, total_erros, descricao, depreciado_em. UNIQUE(banco, versao)" },
      ]},
    ],
  },
  {
    id: "db-indices", label: "Banco de Dados — Índices e Seeds", icon: "⚡",
    color: "#0369A1", light: "#E0F2FE", text: "#0C4A6E",
    items: [
      { id: "idx-criticos", title: "Índices críticos de performance", subs: [
        { id: "idx-1",  text: "idx_transacao_usuario_data: ON transacoes (usuario_id, data_transacao DESC)" },
        { id: "idx-2",  text: "idx_extrato_hash_usuario: UNIQUE PARTIAL ON extratos (usuario_id, hash_arquivo) WHERE status NOT IN ('cancelado','erro_formato')" },
        { id: "idx-3",  text: "idx_regra_usuario_termo: UNIQUE ON regras_classificacao (usuario_id, termo)" },
        { id: "idx-4",  text: "idx_audit_corr: ON auditoria_eventos (correlation_id)" },
        { id: "idx-5",  text: "idx_audit_entidade: ON auditoria_eventos (entidade, entidade_id)" },
        { id: "idx-6",  text: "idx_jobs_status: PARTIAL ON processamento_jobs (status, proximo_retry) WHERE status IN ('enfileirado','retry_1','retry_2','dead_letter')" },
        { id: "idx-7",  text: "idx_snap_usuario_periodo: ON snapshots_financeiros (usuario_id, ano DESC, mes DESC)" },
        { id: "idx-8",  text: "idx_cl_corrigida: PARTIAL ON classificacao_logs (usuario_id, corrigida) WHERE corrigida = TRUE" },
        { id: "idx-9",  text: "idx_consentimento_ativo: PARTIAL ON consentimentos_lgpd (usuario_id, tipo) WHERE revogado_em IS NULL" },
        { id: "idx-10", text: "FK indexes: criar índice em TODA coluna _id FK que não seja PK" },
        { id: "idx-11", text: "Validar EXPLAIN ANALYZE nas 5 queries mais críticas — confirmar Index Scan (não Seq Scan)" },
      ]},
      { id: "seeds", title: "Seeds e dados de desenvolvimento", subs: [
        { id: "seed-1", text: "V2: 8 categorias pai + 24 subcategorias padrão (padrao=TRUE, usuario_id NULL)" },
        { id: "seed-2", text: "V3: categoria_thresholds — Streaming=90, Outros=100, Alimentação/Delivery=70, Transporte/App=85" },
        { id: "seed-3", text: "V4 (perfil=dev apenas): 2 usuários, 3 extratos cada, 50 transações por extrato, regras de amostra" },
        { id: "seed-4", text: "V5: parser_versoes — nubank/v1.0.0, itau/v1.0.0, bradesco/v1.0.0, inter/v1.0.0, generic/v1.0.0" },
        { id: "seed-5", text: "ERD final: gerar com DBeaver ou dbdocs.io; publicar no GitHub Wiki; aprovar formalmente" },
      ]},
    ],
  },
  {
    id: "be-core", label: "Backend — Core e Auth", icon: "🔧",
    color: "#2563EB", light: "#EFF6FF", text: "#1E3A8A",
    items: [
      { id: "be-estrutura", title: "Estrutura do projeto Spring Boot", subs: [
        { id: "be-s-1", text: "Pacotes: controller, service, repository, domain (entidades + enums), dto, mapper (MapStruct), config, exception, security, event, scheduler" },
        { id: "be-s-2", text: "Dependências: spring-boot-starter-web, data-jpa, security, validation, actuator, flyway-core, lombok, mapstruct, springdoc-openapi, bucket4j-spring-boot-starter" },
        { id: "be-s-3", text: "GlobalExceptionHandler com ProblemDetail (RFC 7807) — retornar correlation_id em todo erro" },
        { id: "be-s-4", text: "Perfis Spring: dev (DEBUG + seed), staging, prod (INFO + sem seed)" },
        { id: "be-s-5", text: "GET /actuator/health retornando {status: 'UP'} antes de avançar" },
      ]},
      { id: "be-logging", title: "Correlation ID e logging estruturado", subs: [
        { id: "be-l-1", text: "CorrelationFilter: gerar UUID ou propagar X-Correlation-ID; MDC.put('correlation_id'); adicionar ao response header" },
        { id: "be-l-2", text: "logback-spring.xml com JsonEncoder — campos: timestamp, level, correlation_id, usuario_id, evento, duracao_ms" },
        { id: "be-l-3", text: "NUNCA logar: CPF, senha, valor de transação, descrição completa, refresh token, API key" },
        { id: "be-l-4", text: "Log levels: dev=DEBUG, prod=INFO" },
      ]},
      { id: "be-auth", title: "Autenticação JWT — access + refresh token", subs: [
        { id: "be-a-1", text: "POST /auth/register: validar unicidade CPF e e-mail; gerar codigo FIN-XXXXXX; bcrypt na senha; registrar 5 consentimentos LGPD; enviar e-mail de verificação" },
        { id: "be-a-2", text: "POST /auth/login: verificar ativo + email_verificado + bloqueado_ate; 5 falhas → bloquear 15min; access token JWT 30min com JTI; refresh token 7 dias em cookie httpOnly" },
        { id: "be-a-3", text: "POST /auth/refresh: validar hash do refresh token; rotação (invalida anterior); emitir novo par" },
        { id: "be-a-4", text: "POST /auth/logout: JTI → blacklist Redis com TTL; invalidar refresh token no banco" },
        { id: "be-a-5", text: "JwtAuthFilter: validar assinatura + expiração + JTI na blacklist Redis" },
        { id: "be-a-6", text: "ApiKeyAuthFilter: X-Api-Key → bcrypt hash; rate limit 10/dia Redis; role BOT_UPLOAD" },
        { id: "be-a-7", text: "SecurityConfig: CORS allowlist explícita; rotas públicas /auth/**; ADMIN /admin/**; BOT_UPLOAD /api/extratos/upload" },
      ]},
      { id: "be-usuario", title: "UsuarioService — regras de negócio", subs: [
        { id: "be-u-1", text: "definirSaldoInicial(): verificar saldo_inicial_definido == FALSE; aceitar ≥0; rejeitar negativo; bloquear acesso ao dashboard até configurado" },
        { id: "be-u-2", text: "alterarEmail(): verificar senha atual; enviar confirmação; invalidar TODOS os tokens ativos" },
        { id: "be-u-3", text: "alterarSenha(): bcrypt senha atual; validar complexidade nova; invalidar todos os refresh tokens; auditoria PASSWORD_CHANGE" },
        { id: "be-u-4", text: "vincularTelegram(): código de 6 dígitos no Redis (exp 10min); verificar chatId não vinculado a outro usuário" },
        { id: "be-u-5", text: "excluirConta(): soft delete; enfileirar job ANONIMIZACAO com delay 30 dias; revogar consentimentos; auditoria" },
        { id: "be-u-6", text: "anonimizarConta() [job async]: CPF → hash irreversível; nome → 'Usuário Removido'; email → NULL; telefone → NULL" },
        { id: "be-u-7", text: "exportarDadosLgpd(): coletar transações + consentimentos + auditoria; ZIP JSON; auditoria DADOS_EXPORTADOS" },
      ]},
    ],
  },
  {
    id: "be-negocio", label: "Backend — Regras de Negócio", icon: "⚙",
    color: "#065F46", light: "#D1FAE5", text: "#064E3B",
    items: [
      { id: "be-transacao", title: "TransacaoService", subs: [
        { id: "be-tr-1", text: "criarManual(): valor > 0; data_transacao obrigatória; origem=MANUAL; status=CONFIRMADA; BigDecimal HALF_UP; alerta (não bloqueio) se saldo negativo" },
        { id: "be-tr-2", text: "atualizar(): verificar versao (optimistic lock → 409 se divergir); bloquear edição de valor/data se origem=PDF; bloquear se snapshot do mês fechado (→ 422); auditoria com dados_anteriores e dados_novos" },
        { id: "be-tr-3", text: "excluir(): soft delete (deleted_at = now()); auditoria TRANSACAO_DELETED" },
        { id: "be-tr-4", text: "estornar(): criar transação com tipo oposto, mesmo valor, data=hoje, transacao_estorno_id=original; marcar original como ARQUIVADA" },
        { id: "be-tr-5", text: "calcularSaldo(): NUNCA armazenar. Fórmula: snapshot_anterior.saldo_final + SUM(receitas confirmadas do mês) - SUM(gastos confirmados do mês). SEMPRE BigDecimal HALF_UP" },
      ]},
      { id: "be-extrato", title: "ExtratoService", subs: [
        { id: "be-ex-1", text: "receberUpload(): magic bytes %PDF-; tamanho ≤10MB; sem /JS embutido; ≤5 uploads simultâneos (Redis); SHA-256 + UNIQUE index; salvar com UUID; criar extrato (UPLOAD_RECEBIDO) + job; publicar RabbitMQ; retornar 202" },
        { id: "be-ex-2", text: "reprocessar(): verificar que não há job ativo; status → REPROCESSANDO; criar novo job; 409 se já em processamento" },
        { id: "be-ex-3", text: "ExtratorPdfWorker: lock Redis (SET NX PX 300000); detectar banco; estratégia de parser; score_extracao; status → CLASSIFICANDO" },
        { id: "be-ex-4", text: "ParserFactory: NubankParser, ItauParser, BradescoParser, InterParser, GenericParser (fallback). Detectar banco por metadata + heurística de texto" },
        { id: "be-ex-5", text: "Dead letter queue: após 3 retries com backoff exponencial (1s, 4s, 16s) → status=dead_letter; preservar payload para reprocessamento manual" },
      ]},
      { id: "be-classificacao", title: "ClassificacaoService", subs: [
        { id: "be-cl-1", text: "Pipeline cascata: (1) Regras do usuário (match exato → fuzzy Levenshtein ≥85%) → (2) Dicionário global (Redis cache 24h, 200+ termos) → (3) Claude/OpenAI API → (4) fallback PENDENTE" },
        { id: "be-cl-2", text: "Registrar classificacao_logs em QUALQUER caso: estrategia, modelo_ia_versao, prompt_versao, prompt_hash, resposta_raw, tokens_usados, latencia_ms" },
        { id: "be-cl-3", text: "JAMAIS enviar CPF, nome ou e-mail no prompt da IA — apenas descricao_normalizada + lista de categorias" },
        { id: "be-cl-4", text: "Timeout IA: 30s → fallback PENDENTE com confianca=0. NUNCA chamar IA se já há classificacao_logs para a transação" },
        { id: "be-cl-5", text: "Confiança ≥70 E score_extracao ≥0.7 → status_revisao=CONFIRMADA. Demais → PENDENTE_REVISAO" },
        { id: "be-cl-6", text: "confirmarCategoria(): atualizar transação; registrar log; atualizarScoreRegra(); criar regra se solicitado" },
        { id: "be-cl-7", text: "confirmarLote(): verificar versao do extrato (optimistic lock); confirmar todas com confianca ≥70; 409 se versao divergiu" },
        { id: "be-cl-8", text: "atualizarScoreRegra(): correto → +0.02; incorreto → -0.10; score <0.5 → congelada=TRUE; 3 erros consecutivos → suspensão + alerta admin" },
      ]},
      { id: "be-relatorio", title: "RelatorioService e SnapshotService", subs: [
        { id: "be-rl-1", text: "gerarMensal(): buscar snapshot fechado do mês anterior; agregar confirmadas do período; comparativo; cache Redis 5min se mês fechado" },
        { id: "be-rl-2", text: "gerarPersonalizado(): validar que fim - inicio ≤ 6 meses (→ 400 se exceder)" },
        { id: "be-rl-3", text: "exportarPdf(): enfileirar job; retornar 202 + job_id; worker iText 7; URL assinada expira 24h" },
        { id: "be-rl-4", text: "@Scheduled(cron='0 0 1 * * *'): gerar snapshot de cada conta no primeiro dia do mês; marcar fechado=TRUE; bloquear edição retroativa (→ 422 com sugestão de estorno)" },
      ]},
      { id: "be-auditoria", title: "AuditoriaService e Segurança HTTP", subs: [
        { id: "be-au-1", text: "AuditoriaService.registrar() @Async: não bloqueia request; capturar correlation_id, ip_origem (mascarar último octeto), user_agent" },
        { id: "be-au-2", text: "Eventos obrigatórios (retenção 5 anos): LOGIN, LOGIN_FAIL, LOGOUT, PASSWORD_CHANGE, EMAIL_CHANGE, ACCOUNT_CREATED, ACCOUNT_DELETED, UPLOAD, TRANSACAO_CREATE, TRANSACAO_UPDATE, TRANSACAO_DELETE, DADOS_EXPORTADOS" },
        { id: "be-au-3", text: "Headers HTTP: Content-Security-Policy, X-Content-Type-Options: nosniff, X-Frame-Options: DENY, HSTS (prod)" },
        { id: "be-au-4", text: "Rate limiting Bucket4j: /auth/login 10req/min; /api/extratos/upload 10/dia por API Key; global 200req/min por IP" },
        { id: "be-au-5", text: "CPF: AES-256 em repouso; chave em variável de ambiente; NUNCA em logs" },
        { id: "be-au-6", text: "PDF storage: nome do arquivo sempre UUID; excluir do storage após 7 dias (scheduler diário)" },
      ]},
    ],
  },
  {
    id: "be-controllers", label: "Backend — Controllers REST", icon: "🌐",
    color: "#B45309", light: "#FEF3C7", text: "#78350F",
    items: [
      { id: "ctrl-auth", title: "AuthController /auth/**", subs: [
        { id: "ctrl-a-1", text: "POST /auth/register" },
        { id: "ctrl-a-2", text: "POST /auth/login" },
        { id: "ctrl-a-3", text: "POST /auth/refresh" },
        { id: "ctrl-a-4", text: "POST /auth/logout" },
        { id: "ctrl-a-5", text: "GET /auth/verificar-email?token=" },
        { id: "ctrl-a-6", text: "POST /auth/reenviar-verificacao" },
        { id: "ctrl-a-7", text: "POST /auth/esqueci-senha · POST /auth/resetar-senha?token=" },
      ]},
      { id: "ctrl-usuario", title: "UsuarioController /usuarios/me/**", subs: [
        { id: "ctrl-u-1", text: "GET /usuarios/me · PATCH /usuarios/me" },
        { id: "ctrl-u-2", text: "PATCH /usuarios/me/email · PATCH /usuarios/me/senha" },
        { id: "ctrl-u-3", text: "POST /usuarios/me/saldo-inicial" },
        { id: "ctrl-u-4", text: "PATCH /usuarios/me/api-key (retorna plain-text UMA vez)" },
        { id: "ctrl-u-5", text: "POST /usuarios/me/vincular-telegram · POST /usuarios/me/vincular-whatsapp" },
        { id: "ctrl-u-6", text: "POST /usuarios/me/gerar-codigo-vinculacao" },
        { id: "ctrl-u-7", text: "GET /usuarios/me/dados-exportados (LGPD Art. 18)" },
        { id: "ctrl-u-8", text: "DELETE /usuarios/me (soft delete + pipeline LGPD)" },
      ]},
      { id: "ctrl-transacao", title: "TransacaoController /transacoes/**", subs: [
        { id: "ctrl-tr-1", text: "GET /transacoes (filtros: período, tipo, categoria, conta, valor_min, valor_max — paginado)" },
        { id: "ctrl-tr-2", text: "POST /transacoes" },
        { id: "ctrl-tr-3", text: "GET /transacoes/{id}" },
        { id: "ctrl-tr-4", text: "PATCH /transacoes/{id} (body inclui versao para optimistic lock)" },
        { id: "ctrl-tr-5", text: "DELETE /transacoes/{id}" },
        { id: "ctrl-tr-6", text: "POST /transacoes/{id}/estornar" },
        { id: "ctrl-tr-7", text: "PATCH /transacoes/{id}/categoria (body: categoriaId, criarRegra, versao)" },
      ]},
      { id: "ctrl-extrato", title: "ExtratoController e demais", subs: [
        { id: "ctrl-ex-1", text: "POST /api/extratos/upload (multipart — role BOT_UPLOAD)" },
        { id: "ctrl-ex-2", text: "GET /extratos · GET /extratos/{id} (com progresso)" },
        { id: "ctrl-ex-3", text: "GET /extratos/{id}/revisao · POST /extratos/{id}/confirmar-lote · POST /extratos/{id}/reprocessar" },
        { id: "ctrl-ex-4", text: "GET /relatorios/mensal · /anual · /personalizado · POST /relatorios/{tipo}/exportar · GET /jobs/{id}/resultado" },
        { id: "ctrl-ex-5", text: "GET /contas · POST /contas · PATCH/DELETE /contas/{id}" },
        { id: "ctrl-ex-6", text: "GET /categorias · POST /categorias · PATCH/DELETE /categorias/{id}" },
        { id: "ctrl-ex-7", text: "GET /admin/jobs · POST /admin/jobs/{id}/reprocessar · GET /admin/metricas/ia" },
        { id: "ctrl-ex-8", text: "GET /api/usuarios/identificar?telegram_chat_id= (X-Internal-Key para N8N)" },
      ]},
    ],
  },
  {
    id: "fe-setup", label: "Frontend — Setup e Componentes", icon: "🎨",
    color: "#0891B2", light: "#ECFEFF", text: "#164E63",
    items: [
      { id: "fe-config", title: "Configuração do projeto React", subs: [
        { id: "fe-c-1", text: "Vite + React 18 + TypeScript strict. Path aliases: @/components, @/services, @/hooks, @/types, @/utils" },
        { id: "fe-c-2", text: "Tailwind CSS com design tokens customizados: cores da marca, borderRadius, fontFamily, boxShadow" },
        { id: "fe-c-3", text: "React Query v5: staleTime=5min para dados estáticos, retry=2, onError global com Toast" },
        { id: "fe-c-4", text: "Axios: interceptor de refresh automático (fila de requests durante refresh); X-Correlation-ID em todo request; 401 → logout" },
        { id: "fe-c-5", text: "React Router v6: lazy loading por rota. Guards: PrivateRoute (auth) + SaldoGuard (saldo inicial definido)" },
        { id: "fe-c-6", text: "Zustand: authStore (usuário, tokens em sessionStorage — nunca localStorage) + uiStore (toasts, modal)" },
      ]},
      { id: "fe-atoms", title: "Componentes base (atoms)", subs: [
        { id: "fe-a-1",  text: "Button: variantes primary/secondary/danger/ghost; sizes sm/md/lg; props loading, disabled, leftIcon, rightIcon" },
        { id: "fe-a-2",  text: "InputMonetario: máscara R$ 1.234,56 (biblioteca IMask); nunca usar número nativo para moeda" },
        { id: "fe-a-3",  text: "InputCPF: máscara 000.000.000-00" },
        { id: "fe-a-4",  text: "Combobox: input com busca + lista filtrada (para categorias hierárquicas)" },
        { id: "fe-a-5",  text: "Modal: overlay com trap de foco, Escape para fechar, portal (React.createPortal)" },
        { id: "fe-a-6",  text: "Toast: canto inferior direito; variantes sucesso/erro/aviso/info; auto-dismiss 5s; ação inline (ex: 'Desfazer'); máx 3 simultâneos" },
        { id: "fe-a-7",  text: "StatusBadge: mapear StatusExtrato e StatusRevisao para cor + label amigável" },
        { id: "fe-a-8",  text: "Skeleton: SkeletonText, SkeletonCard, SkeletonChart, SkeletonRow — usar em TODOS os estados de carregamento" },
        { id: "fe-a-9",  text: "EmptyState: ícone + título + descrição + CTA — variantes específicas por contexto" },
        { id: "fe-a-10", text: "ConfidenceBar: barra colorida 0-100 (≥80 verde, 50-79 amarelo, <50 vermelho) + label textual" },
        { id: "fe-a-11", text: "DatePicker e DateRangePicker: formato pt-BR, validação de range ≤6 meses para relatório" },
        { id: "fe-a-12", text: "PasswordStrengthMeter: indicador visual de força da senha nos formulários de cadastro" },
      ]},
    ],
  },
  {
    id: "fe-telas", label: "Frontend — Telas e Fluxos", icon: "🖥",
    color: "#6D28D9", light: "#F5F3FF", text: "#3B0764",
    items: [
      { id: "tl-auth", title: "Telas de autenticação e onboarding", subs: [
        { id: "tl-au-1", text: "/login — feedback de tentativas restantes; contador de desbloqueio; redirect para /saldo-inicial se não configurado" },
        { id: "tl-au-2", text: "/cadastro — stepper 3 passos: (1) nome+CPF+e-mail (2) senha+confirmação+PasswordStrengthMeter (3) checkboxes LGPD obrigatórios" },
        { id: "tl-au-3", text: "/verificar-email — tela de espera; botão reenviar com cooldown 60s; deep link ao acessar com ?token=" },
        { id: "tl-au-4", text: "/saldo-inicial — tela obrigatória antes do dashboard; InputMonetario; aceita 0; rejeita negativo; guard que redireciona se não configurado" },
      ]},
      { id: "tl-dashboard", title: "Dashboard e layout principal", subs: [
        { id: "tl-db-1", text: "Sidebar: logo, nav (Dashboard, Transações, Extratos, Relatórios, Configurações), card de saldo consolidado. Colapsa em mobile" },
        { id: "tl-db-2", text: "Banner sticky: extratos pendentes de revisão (X extratos aguardam revisão → link direto)" },
        { id: "tl-db-3", text: "Cards de saldo por conta + consolidado. Gráfico pizza Recharts (clicável → filtra). Top 5 maiores gastos" },
        { id: "tl-db-4", text: "Banner de onboarding para novos usuários: checklist 'Saldo inicial ✓ → Importar extrato → Revisar'" },
        { id: "tl-db-5", text: "Polling React Query a cada 30s para status de extratos em processamento" },
      ]},
      { id: "tl-transacoes", title: "Transações — /transacoes/**", subs: [
        { id: "tl-tr-1", text: "Lista paginada (50/pág) com filtros: período, tipo, categoria (Combobox), conta, valor min/max, busca" },
        { id: "tl-tr-2", text: "Formulário (Drawer): toggle Receita/Gasto; InputMonetario; DatePicker padrão hoje; Combobox categoria; select conta; toggle recorrente" },
        { id: "tl-tr-3", text: "Exclusão: ConfirmModal → soft delete → Toast 'Excluído' com 'Desfazer' em 5s → PATCH para restaurar se ativado" },
        { id: "tl-tr-4", text: "Edição inline: ao clicar na linha abre Drawer preenchido; versao enviado no body" },
        { id: "tl-tr-5", text: "Submit otimista: adiciona à lista imediatamente; reverte se API falhar com Toast de erro" },
      ]},
      { id: "tl-extratos", title: "Extratos — /extratos/**", subs: [
        { id: "tl-ex-1", text: "/extratos — lista com StatusBadge; progresso X/Y revisadas; filtros por banco e status" },
        { id: "tl-ex-2", text: "/extratos/importar — DropZone drag-and-drop; validação frontend (.pdf, <10MB); preview nome" },
        { id: "tl-ex-3", text: "/extratos/:id — progresso granular polling 2s com ícone animado; estado de erro com botão Reprocessar e mensagem humanizada" },
        { id: "tl-ex-4", text: "/extratos/:id/revisar — agrupamento por estabelecimento (Accordion); ConfidenceBar; filtros: apenas pendentes, valor mín, confiança máx" },
        { id: "tl-ex-5", text: "Confirmação em lote: botão 'Confirmar tudo com alta confiança (N transações)'" },
        { id: "tl-ex-6", text: "Preview da regra antes de confirmar: 'Será salva a regra: IFOOD → Alimentação/Delivery'" },
        { id: "tl-ex-7", text: "Toast com 'Desfazer' (30s) após confirmação individual" },
        { id: "tl-ex-8", text: "Progresso de revisão persistido em sessionStorage (resistente a F5)" },
      ]},
      { id: "tl-relatorios", title: "Relatórios — /relatorios", subs: [
        { id: "tl-rl-1", text: "Tabs: Mensal / Anual / Personalizado (DateRangePicker com validação ≤6 meses)" },
        { id: "tl-rl-2", text: "Gráfico de barras empilhadas Recharts (receitas vs gastos por mês)" },
        { id: "tl-rl-3", text: "Gráfico de pizza por categoria — clicável → filtra tabela de transações abaixo" },
        { id: "tl-rl-4", text: "Cards comparativos: 'Alimentação: R$890 (+23% vs mês anterior)' com cor verde/vermelha" },
        { id: "tl-rl-5", text: "Tabela de transações do período com paginação de 50; ordenação por data/valor" },
        { id: "tl-rl-6", text: "Botão 'Exportar PDF': POST → polling do job → quando pronto, download automático do blob" },
      ]},
      { id: "tl-config", title: "Configurações — /configuracoes/**", subs: [
        { id: "tl-cf-1", text: "/perfil — editar nome, e-mail (fluxo com confirmação)" },
        { id: "tl-cf-2", text: "/seguranca — trocar senha (exige atual + PasswordStrengthMeter); gerar API Key (plain-text UMA vez)" },
        { id: "tl-cf-3", text: "/bots — status do vínculo Telegram/WhatsApp; botão vincular com instruções passo-a-passo; desvincular com confirmação" },
        { id: "tl-cf-4", text: "/contas — CRUD de contas financeiras com tipo e banco" },
        { id: "tl-cf-5", text: "/categorias — CRUD de categorias personalizadas (bloqueio se vinculadas)" },
        { id: "tl-cf-6", text: "/privacidade — lista consentimentos; revogar; 'Exportar meus dados'; zona de perigo com exclusão (digitar e-mail)" },
      ]},
    ],
  },
  {
    id: "n8n", label: "Automação N8N & Bots", icon: "🔗",
    color: "#EA580C", light: "#FFF7ED", text: "#7C2D12",
    items: [
      { id: "n8n-telegram", title: "Workflow Telegram — recepção de PDFs", subs: [
        { id: "n8n-tg-1",  text: "Webhook node: receber update do Telegram (configurar HTTPS e token)" },
        { id: "n8n-tg-2",  text: "Switch: message.document != null?" },
        { id: "n8n-tg-3",  text: "HTTP Request: GET api.telegram.org/getFile?file_id={file_id}" },
        { id: "n8n-tg-4",  text: "HTTP Request: baixar arquivo da URL temporária (binary)" },
        { id: "n8n-tg-5",  text: "HTTP Request: GET /api/usuarios/identificar?telegram_chat_id={chatId} com X-Internal-Key" },
        { id: "n8n-tg-6",  text: "Switch: usuário encontrado (200) vs não encontrado (404)?" },
        { id: "n8n-tg-7",  text: "HTTP Request: POST /api/extratos/upload multipart/form-data com X-Api-Key" },
        { id: "n8n-tg-8",  text: "Telegram sendMessage: 'Extrato recebido! ✅ Processando...'" },
        { id: "n8n-tg-9",  text: "Error branch: sendMessage com motivo humanizado" },
        { id: "n8n-tg-10", text: "Branch 404: sendMessage com instrução de vinculação + link do app" },
        { id: "n8n-tg-11", text: "Configurar retenção de execuções ≤ 24h — Settings > Executions (LGPD)" },
      ]},
      { id: "n8n-whatsapp", title: "Workflow WhatsApp — Meta Business API", subs: [
        { id: "n8n-wa-1",  text: "Configurar número no Meta Developer Portal + WhatsApp Business Account" },
        { id: "n8n-wa-2",  text: "Webhook node: verificação inicial GET (hub.challenge + hub.verify_token)" },
        { id: "n8n-wa-3",  text: "Parse: messages[0] do payload entry[0].changes[0].value" },
        { id: "n8n-wa-4",  text: "Switch: message.type = 'document'?" },
        { id: "n8n-wa-5",  text: "HTTP Request: GET graph.facebook.com/v18.0/{media_id}" },
        { id: "n8n-wa-6",  text: "HTTP Request: baixar com Authorization: Bearer {META_TOKEN}" },
        { id: "n8n-wa-7",  text: "HTTP Request: GET /api/usuarios/identificar?whatsapp_id={phone}" },
        { id: "n8n-wa-8",  text: "HTTP Request: POST /api/extratos/upload com X-Api-Key" },
        { id: "n8n-wa-9",  text: "WhatsApp Messages API: enviar resposta" },
        { id: "n8n-wa-10", text: "Retry node: retentar em falha de API (máx 3x, espera 30s)" },
      ]},
      { id: "n8n-vinculacao", title: "Workflow de vinculação de conta", subs: [
        { id: "n8n-vl-1", text: "Detectar comando /vincular no início da mensagem" },
        { id: "n8n-vl-2", text: "Extrair código de 6 dígitos após o comando" },
        { id: "n8n-vl-3", text: "HTTP Request: POST /api/usuarios/vincular-telegram com {chatId, codigo}" },
        { id: "n8n-vl-4", text: "Resposta sucesso: 'Conta vinculada! ✅ Agora envie seus extratos aqui.'" },
        { id: "n8n-vl-5", text: "Resposta erro: 'Código inválido ou expirado. Gere um novo no app.'" },
      ]},
      { id: "n8n-config", title: "Configurações do N8N", subs: [
        { id: "n8n-cfg-1", text: "Self-hosted em Docker (n8nio/n8n) ou N8N Cloud" },
        { id: "n8n-cfg-2", text: "Variáveis: FINTECH_API_URL, TELEGRAM_BOT_TOKEN, WHATSAPP_META_TOKEN, INTERNAL_API_KEY" },
        { id: "n8n-cfg-3", text: "EXECUTIONS_DATA_PRUNE_MAX_COUNT=100 e EXECUTIONS_DATA_MAX_AGE=24 (LGPD)" },
        { id: "n8n-cfg-4", text: "Testar todos os workflows com PDFs reais de cada banco suportado" },
      ]},
    ],
  },
  {
    id: "testes", label: "Testes", icon: "🧪",
    color: "#BE185D", light: "#FDF2F8", text: "#831843",
    items: [
      { id: "test-unit", title: "Testes unitários — JUnit 5 + Mockito", subs: [
        { id: "test-u-1",  text: "UsuarioServiceTest: cadastrarUsuario_cpfDuplicado_retorna409" },
        { id: "test-u-2",  text: "UsuarioServiceTest: autenticar_apos5FalhasLogin_bloqueiaUsuario" },
        { id: "test-u-3",  text: "UsuarioServiceTest: definirSaldoInicial_valorNegativo_lanca400" },
        { id: "test-u-4",  text: "UsuarioServiceTest: definirSaldoInicial_jaDefinido_lanca409" },
        { id: "test-u-5",  text: "TransacaoServiceTest: registrar100Decimais_saldoExatamenteDezReais (BigDecimal HALF_UP)" },
        { id: "test-u-6",  text: "TransacaoServiceTest: editarTransacao_versaoDesatualizada_retorna409 (optimistic lock)" },
        { id: "test-u-7",  text: "TransacaoServiceTest: editarTransacao_origemPdf_bloqueiaValorEData (RN-09)" },
        { id: "test-u-8",  text: "TransacaoServiceTest: editarTransacao_mesFechado_retorna422 (RN-FIN01)" },
        { id: "test-u-9",  text: "ClassificacaoServiceTest: classificar_comRegraUsuario_aplicaComConfianca100" },
        { id: "test-u-10", text: "ClassificacaoServiceTest: classificar_semMatch_chamaDicionario" },
        { id: "test-u-11", text: "ClassificacaoServiceTest: classificar_semDicionario_chamaIA (IA mockada)" },
        { id: "test-u-12", text: "ClassificacaoServiceTest: atualizarScore_incorreto_penaliza010" },
        { id: "test-u-13", text: "ClassificacaoServiceTest: antiLoop_3CorrecoesConsecutivas_suspendeRegra" },
        { id: "test-u-14", text: "ExtratoServiceTest: maquinaEstados_transicoesValidas (upload → extraindo → classificando)" },
        { id: "test-u-15", text: "ExtratoServiceTest: maquinaEstados_transicoesInvalidas (concluido → classificando → lanca Exception)" },
        { id: "test-u-16", text: "SnapshotServiceTest: gerarSnapshot_calculoCorreto_multiplaContas (BigDecimal)" },
        { id: "test-u-17", text: "SnapshotServiceTest: verificarMesFechado_retornaTrueAposFechamento" },
        { id: "test-u-18", text: "LgpdServiceTest: executarAnonimizacao_cpfViraHash_emailNulo_nomeGenerico" },
      ]},
      { id: "test-integ", title: "Testes de integração — Testcontainers", subs: [
        { id: "test-i-1", text: "AuthIntegrationTest: fluxo completo cadastro → verificação e-mail → login → refresh → logout (com blacklist)" },
        { id: "test-i-2", text: "UploadIntegrationTest: upload mesmo PDF duas vezes → segundo retorna 409" },
        { id: "test-i-3", text: "UploadIntegrationTest: upload de arquivo não-PDF → retorna 400 (magic bytes)" },
        { id: "test-i-4", text: "RevisaoIntegrationTest: confirmar em lote com versão desatualizada → retorna 409 (optimistic lock extrato)" },
        { id: "test-i-5", text: "RelatorioIntegrationTest: relatório mensal com múltiplas contas: saldo consolidado correto" },
        { id: "test-i-6", text: "SnapshotIntegrationTest: editar transação em mês fechado → 422 Unprocessable" },
        { id: "test-i-7", text: "Testcontainers: PostgreSQL 16 + Redis 7 instâncias reais nos testes de integração" },
      ]},
      { id: "test-carga", title: "Testes de carga — k6", subs: [
        { id: "test-c-1", text: "carga-normal.js: 30 VUs por 5 minutos — operações mistas (login, listar transações, relatório)" },
        { id: "test-c-2", text: "pico-upload.js: 20 uploads simultâneos de extratos de 50 transações" },
        { id: "test-c-3", text: "Critério P95 de resposta de API < 500ms" },
        { id: "test-c-4", text: "Critério P95 de upload < 2s" },
        { id: "test-c-5", text: "Critério processamento completo de extrato < 60s" },
        { id: "test-c-6", text: "Critério zero erros de timeout" },
        { id: "test-c-7", text: "Monitorar heap JVM — sem vazamento de memória após 10 minutos de carga" },
      ]},
      { id: "test-fe", title: "Testes de frontend — Vitest + Testing Library", subs: [
        { id: "test-fe-1", text: "Login.test.tsx — exibe contador de tentativas; mostra countdown de desbloqueio" },
        { id: "test-fe-2", text: "Upload.test.tsx — drag-and-drop funciona; arquivo não-PDF exibe erro; arquivo > 10MB exibe erro" },
        { id: "test-fe-3", text: "Revisao.test.tsx — confirmação individual cria toast com Desfazer; confirmação em lote seleciona corretas" },
        { id: "test-fe-4", text: "Relatorio.test.tsx — DateRangePicker bloqueia > 6 meses; gráfico renderiza; exportação PDF dispara polling" },
        { id: "test-fe-5", text: "Cobertura mínima de 80% nos services do backend" },
      ]},
    ],
  },
  {
    id: "obs", label: "Observabilidade & Deploy", icon: "📊",
    color: "#374151", light: "#F9FAFB", text: "#111827",
    items: [
      { id: "obs-prometheus", title: "Prometheus — métricas customizadas", subs: [
        { id: "obs-p-1",  text: "spring-boot-starter-actuator + micrometer-registry-prometheus configurados" },
        { id: "obs-p-2",  text: "fintech_extratos_processados_total{status} — counter por status final" },
        { id: "obs-p-3",  text: "fintech_extrato_duracao_segundos{banco} — histogram de tempo por banco" },
        { id: "obs-p-4",  text: "fintech_ia_latencia_ms — histogram P50/P95/P99" },
        { id: "obs-p-5",  text: "fintech_ia_classificacoes_corrigidas_total — taxa de qualidade da IA" },
        { id: "obs-p-6",  text: "fintech_ia_tokens_usados_total — custo de API" },
        { id: "obs-p-7",  text: "fintech_ia_budget_pct — percentual do budget mensal usado" },
        { id: "obs-p-8",  text: "fintech_fila_tamanho{fila} — tamanho de cada fila RabbitMQ" },
        { id: "obs-p-9",  text: "fintech_login_tentativas_total{resultado}" },
        { id: "obs-p-10", text: "fintech_upload_rejeitados_total{motivo}" },
        { id: "obs-p-11", text: "fintech_dlq_tamanho — jobs em dead_letter" },
      ]},
      { id: "obs-grafana", title: "Grafana — dashboards e alertas", subs: [
        { id: "obs-g-1", text: "Dashboard 1: Saúde Geral — uptime, P95 latência, taxa de erros 4xx/5xx, JVM heap" },
        { id: "obs-g-2", text: "Dashboard 2: IA & Classificação — latência, taxa de acerto, custo de tokens, budget%" },
        { id: "obs-g-3", text: "Dashboard 3: Filas & Workers — tamanho das filas, DLQ, jobs por status" },
        { id: "obs-g-4", text: "Alerta: fila de extração parada > 5 minutos → Slack #alertas" },
        { id: "obs-g-5", text: "Alerta: taxa de erro IA > 10% em 5 minutos → Slack #alertas" },
        { id: "obs-g-6", text: "Alerta: DLQ > 5 jobs → Slack #alertas (urgente)" },
        { id: "obs-g-7", text: "Alerta: budget tokens IA > 80% → e-mail do time" },
        { id: "obs-g-8", text: "Alerta: score de parser < 0.7 em 5+ usos recentes → Slack #dev" },
        { id: "obs-g-9", text: "Grafana Loki: logs centralizados com retenção 30 dias; índice por correlation_id" },
      ]},
      { id: "obs-deploy", title: "Deploy e infraestrutura de produção", subs: [
        { id: "obs-d-1",  text: "Railway.app: Spring Boot Web Service (512MB RAM mín), health check /actuator/health" },
        { id: "obs-d-2",  text: "Railway.app: PostgreSQL managed — backup diário, ponto de restauração 7 dias" },
        { id: "obs-d-3",  text: "Railway.app: Redis managed" },
        { id: "obs-d-4",  text: "CloudAMQP: RabbitMQ (free tier)" },
        { id: "obs-d-5",  text: "Vercel: frontend React" },
        { id: "obs-d-6",  text: "Docker multi-stage build: imagem final < 200MB" },
        { id: "obs-d-7",  text: "HTTPS automático via Let's Encrypt; custom domain configurado" },
        { id: "obs-d-8",  text: "Flyway migrations: rodam automaticamente no startup" },
        { id: "obs-d-9",  text: "Seed de produção: apenas categorias e parser versões — NUNCA seed de dev" },
        { id: "obs-d-10", text: "Smoke tests pós-deploy: login, health check, upload de PDF de teste, listagem de relatório" },
        { id: "obs-d-11", text: "TESTAR RESTORE DE BACKUP antes do go-live" },
        { id: "obs-d-12", text: "Rollback documentado: railway rollback {deployment-id} < 2 minutos" },
        { id: "obs-d-13", text: "Ambiente staging idêntico ao de produção" },
        { id: "obs-d-14", text: "Beta fechado: 5-15 usuários por 48h; critério: zero erros críticos e score IA ≥ 70%" },
      ]},
    ],
  },
  {
    id: "lgpd", label: "LGPD, Auditoria & Documentação", icon: "⚖",
    color: "#6D28D9", light: "#F5F3FF", text: "#3B0764",
    items: [
      { id: "lgpd-impl", title: "Implementações obrigatórias de LGPD", subs: [
        { id: "lgpd-1",  text: "CPF criptografado em repouso com AES-256 — NUNCA armazenar em texto plano" },
        { id: "lgpd-2",  text: "cpf_hash (SHA-256) para lookup sem descriptografar" },
        { id: "lgpd-3",  text: "CPF NUNCA aparece em logs — verificar com grep em prod antes de go-live" },
        { id: "lgpd-4",  text: "PDF de extrato excluído do storage após 7 dias (PdfStorageLimpezaScheduler)" },
        { id: "lgpd-5",  text: "N8N: histórico de execuções configurado para ≤ 24h (payload contém o PDF)" },
        { id: "lgpd-6",  text: "Consentimentos registrados por tipo no cadastro (5 tipos obrigatórios)" },
        { id: "lgpd-7",  text: "Endpoint GET /usuarios/me/dados-exportados — ZIP com JSON de tudo (Art. 18 LGPD)" },
        { id: "lgpd-8",  text: "Pipeline de anonimização: 30 dias após deleted_at → CPF=hash, email=NULL, nome='Usuário Removido'" },
        { id: "lgpd-9",  text: "Endpoint DELETE /usuarios/me/consentimentos/{tipo} — revogação individual" },
        { id: "lgpd-10", text: "Política de privacidade exibida e aceite registrado com versão e data" },
        { id: "lgpd-11", text: "Logs de acesso: mascarar último octeto do IP; retenção 90 dias" },
        { id: "lgpd-12", text: "classificacao_logs: mascarar descrições de transação antes de persistir" },
      ]},
      { id: "lgpd-auditoria", title: "Auditoria — trilha de eventos", subs: [
        { id: "lgpd-a-1", text: "Retenção 5 anos: LOGIN, LOGIN_FAIL, LOGOUT, PASSWORD_CHANGE, EMAIL_CHANGE, ACCOUNT_CREATED, ACCOUNT_DELETED, UPLOAD, TRANSACAO_CREATE/UPDATE/DELETE, DADOS_EXPORTADOS" },
        { id: "lgpd-a-2", text: "Retenção 90 dias: consultas de relatório, criação de categoria, alteração de consentimento" },
        { id: "lgpd-a-3", text: "TODOS os eventos incluem correlation_id, ip_origem, user_agent, origem" },
        { id: "lgpd-a-4", text: "AuditoriaService sempre @Async — NUNCA bloqueia o request principal" },
        { id: "lgpd-a-5", text: "auditoria_eventos é imutável: sem UPDATE ou DELETE permitido na aplicação" },
      ]},
      { id: "lgpd-docs", title: "Documentação final", subs: [
        { id: "lgpd-d-1", text: "SpringDoc OpenAPI 3: todos os endpoints documentados com exemplos" },
        { id: "lgpd-d-2", text: "README.md: setup local em 5 comandos, arquitetura em diagrama, decisões técnicas" },
        { id: "lgpd-d-3", text: "ADRs: JWT access+refresh, BigDecimal para moeda, RabbitMQ vs Kafka, parser por banco" },
        { id: "lgpd-d-4", text: "RUNBOOK.md para o sócio: 'O que significa cada painel do Grafana e quando se preocupar'" },
        { id: "lgpd-d-5", text: "RUNBOOK.md: 'Extrato travado em processamento → passo a passo de diagnóstico'" },
        { id: "lgpd-d-6", text: "RUNBOOK.md: 'Como acessar a DLQ e reprocessar um job com erro'" },
        { id: "lgpd-d-7", text: "RUNBOOK.md: 'Como interpretar alertas do Slack e qual a ação esperada'" },
        { id: "lgpd-d-8", text: "Gravar vídeos Loom para cada seção do runbook (acessível para o sócio não-técnico)" },
        { id: "lgpd-d-9", text: "Glossário de termos técnicos para o sócio: DLQ, endpoint, webhook, JWT, polling, etc." },
      ]},
    ],
  },
];

// ─── CONFIGURAÇÃO JSONBIN ─────────────────────────────────────────────────────

const LS_CONFIG_KEY = "fintech_jsonbin_config";
const LS_DATA_KEY   = "fintech_todo_data";

function loadConfig() {
  try { return JSON.parse(localStorage.getItem(LS_CONFIG_KEY) || "null"); }
  catch { return null; }
}
function saveConfig(cfg) {
  localStorage.setItem(LS_CONFIG_KEY, JSON.stringify(cfg));
}
function loadLocal() {
  try { return JSON.parse(localStorage.getItem(LS_DATA_KEY) || "{}"); }
  catch { return {}; }
}
function saveLocal(data) {
  localStorage.setItem(LS_DATA_KEY, JSON.stringify(data));
}

async function fetchFromBin(apiKey, binId) {
  const r = await fetch(`https://api.jsonbin.io/v3/b/${binId}/latest`, {
    headers: { "X-Master-Key": apiKey }
  });
  if (!r.ok) throw new Error(`JSONBin GET ${r.status}`);
  const json = await r.json();
  return json.record || {};
}

async function saveToBin(apiKey, binId, data) {
  const r = await fetch(`https://api.jsonbin.io/v3/b/${binId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", "X-Master-Key": apiKey },
    body: JSON.stringify(data),
  });
  if (!r.ok) throw new Error(`JSONBin PUT ${r.status}`);
}

// ─── TELA DE CONFIGURAÇÃO ─────────────────────────────────────────────────────

function SetupScreen({ onSave }) {
  const [apiKey, setApiKey] = useState("");
  const [binId,  setBinId]  = useState("");
  const [error,  setError]  = useState("");
  const [testing, setTesting] = useState(false);

  const handleSave = async () => {
    if (!apiKey.trim() || !binId.trim()) {
      setError("Preencha os dois campos.");
      return;
    }
    setTesting(true);
    setError("");
    try {
      await fetchFromBin(apiKey.trim(), binId.trim());
      saveConfig({ apiKey: apiKey.trim(), binId: binId.trim() });
      onSave({ apiKey: apiKey.trim(), binId: binId.trim() });
    } catch {
      setError("Credenciais inválidas ou BIN inacessível. Verifique a API Key e o BIN ID.");
    } finally {
      setTesting(false);
    }
  };

  const inp = {
    width: "100%", padding: "10px 12px", fontSize: 13,
    border: "1px solid #D1D5DB", borderRadius: 8,
    background: "#F9FAFB", color: "#111827", outline: "none",
    fontFamily: "monospace", boxSizing: "border-box",
  };

  return (
    <div style={{ minHeight: "100vh", background: "#F9FAFB", display: "flex", alignItems: "center", justifyContent: "center", fontFamily: "system-ui, sans-serif", padding: 20 }}>
      <div style={{ background: "#fff", border: "1px solid #E5E7EB", borderRadius: 16, padding: 36, maxWidth: 480, width: "100%" }}>
        <div style={{ textAlign: "center", marginBottom: 28 }}>
          <div style={{ fontSize: 32, marginBottom: 8 }}>☁</div>
          <h1 style={{ fontSize: 20, fontWeight: 600, color: "#111827", margin: 0 }}>Configurar sincronização</h1>
          <p style={{ fontSize: 13, color: "#6B7280", marginTop: 6, lineHeight: 1.6 }}>
            Configure o JSONBin.io para salvar o progresso na nuvem e acessar de qualquer máquina.
          </p>
        </div>

        <div style={{ background: "#EFF6FF", border: "1px solid #BFDBFE", borderRadius: 10, padding: "12px 14px", marginBottom: 24, fontSize: 12, color: "#1E40AF", lineHeight: 1.7 }}>
          <strong>Como configurar:</strong><br/>
          1. Acesse <a href="https://jsonbin.io" target="_blank" rel="noreferrer" style={{ color: "#2563EB" }}>jsonbin.io</a> e crie uma conta gratuita<br/>
          2. Vá em <strong>API Keys</strong> e copie sua chave<br/>
          3. Clique em <strong>Create Bin</strong>, cole <code style={{ background: "#DBEAFE", padding: "1px 4px", borderRadius: 3 }}>{"{}"}</code> e salve<br/>
          4. Copie o <strong>BIN ID</strong> gerado e cole abaixo
        </div>

        <div style={{ marginBottom: 16 }}>
          <label style={{ fontSize: 12, fontWeight: 500, color: "#374151", display: "block", marginBottom: 6 }}>API Key (Master Key)</label>
          <input style={inp} type="password" placeholder="$2a$10$..." value={apiKey} onChange={e => setApiKey(e.target.value)} />
        </div>

        <div style={{ marginBottom: 20 }}>
          <label style={{ fontSize: 12, fontWeight: 500, color: "#374151", display: "block", marginBottom: 6 }}>BIN ID</label>
          <input style={inp} type="text" placeholder="64a3f8c12..." value={binId} onChange={e => setBinId(e.target.value)} />
        </div>

        {error && (
          <div style={{ background: "#FEF2F2", border: "1px solid #FECACA", borderRadius: 8, padding: "10px 12px", fontSize: 12, color: "#DC2626", marginBottom: 16 }}>
            {error}
          </div>
        )}

        <button
          onClick={handleSave}
          disabled={testing}
          style={{ width: "100%", padding: "11px", background: testing ? "#93C5FD" : "#2563EB", color: "#fff", border: "none", borderRadius: 8, fontSize: 14, fontWeight: 500, cursor: testing ? "not-allowed" : "pointer" }}>
          {testing ? "Testando conexão..." : "Salvar e conectar"}
        </button>
      </div>
    </div>
  );
}

// ─── COMPONENTE PRINCIPAL ─────────────────────────────────────────────────────

export default function TodoFintech() {
  const [config, setConfig]           = useState(() => loadConfig());
  const [checked, setChecked]         = useState({});
  const [syncStatus, setSyncStatus]   = useState("idle"); // idle | saving | saved | error
  const [expanded, setExpanded]       = useState({});
  const [openSections, setOpenSections] = useState(() => {
    const init = {};
    SECTIONS.forEach(s => { init[s.id] = true; });
    return init;
  });
  const [activeSection, setActiveSection] = useState("all");
  const [search, setSearch]           = useState("");
  const [loaded, setLoaded]           = useState(false);

  // ── Carregar dados ao iniciar ──────────────────────────────────────────────
  useEffect(() => {
    if (!config) { setLoaded(true); return; }
    (async () => {
      try {
        const remote = await fetchFromBin(config.apiKey, config.binId);
        setChecked(remote);
        saveLocal(remote); // cache offline
      } catch {
        // offline → usar localStorage
        setChecked(loadLocal());
        setSyncStatus("error");
      } finally {
        setLoaded(true);
      }
    })();
  }, [config]);

  // ── Salvar no JSONBin + localStorage ──────────────────────────────────────
  const persist = useCallback(async (data) => {
    saveLocal(data);
    if (!config) return;
    setSyncStatus("saving");
    try {
      await saveToBin(config.apiKey, config.binId, data);
      setSyncStatus("saved");
      setTimeout(() => setSyncStatus("idle"), 2000);
    } catch {
      setSyncStatus("error");
    }
  }, [config]);

  const toggle = useCallback((id) => {
    setChecked(prev => {
      const next = { ...prev, [id]: !prev[id] };
      persist(next);
      return next;
    });
  }, [persist]);

  const markAllSection = useCallback((section, val) => {
    setChecked(prev => {
      const next = { ...prev };
      section.items.forEach(item => item.subs.forEach(sub => { next[sub.id] = val; }));
      persist(next);
      return next;
    });
  }, [persist]);

  // ── Stats ──────────────────────────────────────────────────────────────────
  const stats = useMemo(() => {
    let total = 0, done = 0;
    SECTIONS.forEach(s => s.items.forEach(item => item.subs.forEach(sub => {
      total++; if (checked[sub.id]) done++;
    })));
    return { total, done, pct: total ? Math.round(done / total * 100) : 0 };
  }, [checked]);

  const sectionStats = useCallback((section) => {
    let total = 0, done = 0;
    section.items.forEach(item => item.subs.forEach(sub => {
      total++; if (checked[sub.id]) done++;
    }));
    return { total, done, pct: total ? Math.round(done / total * 100) : 0 };
  }, [checked]);

  const filteredSections = useMemo(() => {
    const q = search.toLowerCase().trim();
    return SECTIONS
      .filter(s => activeSection === "all" || s.id === activeSection)
      .map(s => ({
        ...s,
        items: s.items.map(item => ({
          ...item,
          subs: q ? item.subs.filter(sub => sub.text.toLowerCase().includes(q) || item.title.toLowerCase().includes(q)) : item.subs,
        })).filter(item => item.subs.length > 0),
      }))
      .filter(s => s.items.length > 0);
  }, [activeSection, search]);

  // ── Tela de setup ──────────────────────────────────────────────────────────
  if (!config) return <SetupScreen onSave={cfg => { setConfig(cfg); setLoaded(false); }} />;

  if (!loaded) return (
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", fontFamily: "system-ui, sans-serif", color: "#6B7280", fontSize: 14 }}>
      Carregando da nuvem...
    </div>
  );

  // ── Indicador de sync ──────────────────────────────────────────────────────
  const syncLabel = { idle: "", saving: "Salvando...", saved: "✓ Salvo", error: "⚠ Erro ao salvar (offline)" };
  const syncColor = { idle: "", saving: "#6B7280", saved: "#059669", error: "#DC2626" };

  const globalPct = stats.pct;
  const barColor  = globalPct < 30 ? "#DC2626" : globalPct < 70 ? "#D97706" : "#059669";

  const css = {
    root:     { fontFamily: "system-ui, -apple-system, sans-serif", background: "#fff", minHeight: "100vh", color: "#18181b" },
    mono:     { fontFamily: "'Courier New', Courier, monospace" },
    sideItem: (active) => ({
      display: "flex", alignItems: "center", gap: 7, width: "100%", padding: "7px 14px",
      background: active ? "#F4F4F5" : "transparent",
      border: "none", cursor: "pointer", textAlign: "left",
      borderLeft: `3px solid ${active ? "#18181b" : "transparent"}`,
    }),
  };

  return (
    <div style={css.root}>
      {/* ── Header ── */}
      <div style={{ padding: "20px 20px 0", borderBottom: "1px solid #E4E4E7" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: 16, flexWrap: "wrap", marginBottom: 14 }}>
          <div>
            <div style={{ ...css.mono, fontSize: 10, color: "#A1A1AA", letterSpacing: "0.1em" }}>SISTEMA FINANCEIRO — TO-DO LIST COMPLETO</div>
            <h1 style={{ fontSize: 18, fontWeight: 600, color: "#18181b", margin: "4px 0 0" }}>Checklist de desenvolvimento v1.0</h1>
          </div>
          <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
            {syncStatus !== "idle" && (
              <span style={{ fontSize: 11, color: syncColor[syncStatus], ...css.mono }}>{syncLabel[syncStatus]}</span>
            )}
            {[["Total", stats.total, "#18181b"], ["Feitos", stats.done, "#059669"], ["Pendentes", stats.total - stats.done, "#DC2626"]]
              .map(([l, n, c]) => (
                <div key={l} style={{ textAlign: "center", background: "#F4F4F5", borderRadius: 8, padding: "6px 12px" }}>
                  <div style={{ fontSize: 18, fontWeight: 600, color: c }}>{n}</div>
                  <div style={{ fontSize: 10, color: "#71717A" }}>{l}</div>
                </div>
              ))}
            <button
              onClick={() => { if (confirm("Redefinir configuração do JSONBin?")) { localStorage.removeItem(LS_CONFIG_KEY); setConfig(null); } }}
              style={{ fontSize: 11, padding: "5px 10px", background: "transparent", border: "1px solid #E4E4E7", borderRadius: 6, cursor: "pointer", color: "#71717A" }}>
              ⚙ Config
            </button>
          </div>
        </div>

        {/* Barra de progresso global */}
        <div style={{ marginBottom: 14 }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 5 }}>
            <span style={{ ...css.mono, fontSize: 11, color: "#A1A1AA" }}>Progresso geral</span>
            <span style={{ ...css.mono, fontSize: 11, color: barColor, fontWeight: 600 }}>{globalPct}%</span>
          </div>
          <div style={{ height: 6, background: "#F4F4F5", borderRadius: 3 }}>
            <div style={{ height: "100%", width: `${globalPct}%`, background: barColor, borderRadius: 3, transition: "width .3s" }} />
          </div>
        </div>

        {/* Busca */}
        <div style={{ paddingBottom: 12 }}>
          <input
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="🔍 Buscar tarefa..."
            style={{ width: "100%", padding: "8px 12px", border: "1px solid #D4D4D8", borderRadius: 8, background: "#F4F4F5", color: "#18181b", fontSize: 13, outline: "none", boxSizing: "border-box" }}
          />
        </div>
      </div>

      {/* ── Body ── */}
      <div style={{ display: "flex" }}>
        {/* Sidebar */}
        <div style={{ width: 210, flexShrink: 0, borderRight: "1px solid #E4E4E7", padding: "10px 0", position: "sticky", top: 0, alignSelf: "flex-start", maxHeight: "calc(100vh - 160px)", overflowY: "auto" }}>
          <button style={css.sideItem(activeSection === "all")} onClick={() => setActiveSection("all")}>
            <span style={{ fontSize: 14 }}>📋</span>
            <span style={{ fontSize: 12, color: activeSection === "all" ? "#18181b" : "#71717A", fontWeight: activeSection === "all" ? 600 : 400 }}>Todas as seções</span>
          </button>
          {SECTIONS.map(s => {
            const st = sectionStats(s);
            const active = activeSection === s.id;
            return (
              <button key={s.id} style={css.sideItem(active)} onClick={() => setActiveSection(s.id)}>
                <span style={{ fontSize: 13 }}>{s.icon}</span>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ fontSize: 11, color: active ? "#18181b" : "#71717A", fontWeight: active ? 600 : 400, lineHeight: 1.3, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{s.label}</div>
                  <div style={{ fontSize: 10, color: st.done === st.total ? "#059669" : "#A1A1AA", marginTop: 1, ...css.mono }}>{st.done}/{st.total}</div>
                </div>
              </button>
            );
          })}
        </div>

        {/* Conteúdo */}
        <div style={{ flex: 1, padding: "16px 20px", overflowY: "auto", minWidth: 0 }}>
          {filteredSections.map(section => {
            const st = sectionStats(section);
            const secOpen = openSections[section.id] !== false;
            const allDone = st.done === st.total;
            return (
              <div key={section.id} style={{ marginBottom: 16 }}>
                {/* Cabeçalho da seção */}
                <div
                  style={{ display: "flex", alignItems: "center", gap: 10, padding: "10px 14px", background: section.light, borderRadius: 10, border: `1px solid ${section.color}33`, marginBottom: secOpen ? 8 : 0, cursor: "pointer" }}
                  onClick={() => setOpenSections(p => ({ ...p, [section.id]: !secOpen }))}>
                  <span style={{ fontSize: 16 }}>{section.icon}</span>
                  <span style={{ fontSize: 14, fontWeight: 600, color: section.text, flex: 1 }}>{section.label}</span>
                  <div style={{ display: "flex", gap: 6, alignItems: "center" }}>
                    <button onClick={e => { e.stopPropagation(); markAllSection(section, true); }}
                      style={{ fontSize: 10, padding: "3px 8px", borderRadius: 6, border: `1px solid ${section.color}55`, background: "transparent", color: section.text, cursor: "pointer" }}>
                      Marcar tudo
                    </button>
                    <button onClick={e => { e.stopPropagation(); markAllSection(section, false); }}
                      style={{ fontSize: 10, padding: "3px 8px", borderRadius: 6, border: "1px solid #E4E4E7", background: "transparent", color: "#71717A", cursor: "pointer" }}>
                      Desmarcar
                    </button>
                    <span style={{ ...css.mono, fontSize: 11, color: allDone ? "#059669" : section.color, fontWeight: 600 }}>{st.done}/{st.total}</span>
                    <div style={{ width: 50, height: 4, background: "#fff", borderRadius: 2, overflow: "hidden" }}>
                      <div style={{ height: "100%", width: `${st.pct}%`, background: allDone ? "#059669" : section.color, borderRadius: 2, transition: "width .3s" }} />
                    </div>
                    <span style={{ fontSize: 11, color: "#A1A1AA" }}>{secOpen ? "▲" : "▼"}</span>
                  </div>
                </div>

                {/* Items */}
                {secOpen && section.items.map(item => {
                  const itemOpen = expanded[item.id] !== false;
                  const itemDone  = item.subs.filter(s => checked[s.id]).length;
                  const itemTotal = item.subs.length;
                  const itemAllDone = itemDone === itemTotal;
                  return (
                    <div key={item.id} style={{ marginBottom: 6, border: "1px solid #E4E4E7", borderRadius: 8, overflow: "hidden" }}>
                      <div
                        style={{ display: "flex", alignItems: "center", gap: 10, padding: "9px 14px", background: "#F9FAFB", cursor: "pointer" }}
                        onClick={() => setExpanded(p => ({ ...p, [item.id]: !itemOpen }))}>
                        <div style={{
                          width: 18, height: 18, borderRadius: "50%", flexShrink: 0,
                          background: itemAllDone ? "#059669" : section.color + "22",
                          border: `1px solid ${itemAllDone ? "#059669" : section.color + "55"}`,
                          display: "flex", alignItems: "center", justifyContent: "center",
                          fontSize: 10, color: itemAllDone ? "#fff" : section.color, fontWeight: 600,
                        }}>{itemAllDone ? "✓" : itemDone > 0 ? itemDone : ""}</div>
                        <span style={{ fontSize: 12, fontWeight: 600, color: "#18181b", flex: 1, lineHeight: 1.4 }}>{item.title}</span>
                        <span style={{ ...css.mono, fontSize: 10, color: "#A1A1AA" }}>{itemDone}/{itemTotal}</span>
                        <span style={{ fontSize: 10, color: "#A1A1AA" }}>{itemOpen ? "▲" : "▼"}</span>
                      </div>
                      {itemOpen && (
                        <div style={{ padding: "6px 0" }}>
                          {item.subs.map(sub => (
                            <label key={sub.id} style={{
                              display: "flex", alignItems: "flex-start", gap: 10, padding: "5px 14px 5px 16px",
                              cursor: "pointer", background: checked[sub.id] ? section.light : "transparent",
                              transition: "background .1s",
                            }}>
                              <input
                                type="checkbox"
                                checked={!!checked[sub.id]}
                                onChange={() => toggle(sub.id)}
                                style={{ marginTop: 3, flexShrink: 0, accentColor: section.color, width: 14, height: 14 }}
                              />
                              <span style={{
                                fontSize: 12, lineHeight: 1.6,
                                color: checked[sub.id] ? "#A1A1AA" : "#18181b",
                                textDecoration: checked[sub.id] ? "line-through" : "none",
                                ...css.mono,
                              }}>
                                {sub.text}
                              </span>
                            </label>
                          ))}
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
