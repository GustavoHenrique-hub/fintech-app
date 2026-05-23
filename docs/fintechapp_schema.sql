-- ============================================================
--  FINAPP — Schema completo + carga inicial
--  Compatível com PostgreSQL 14+
--  Gerado por Claude Sonnet 4.6
-- ============================================================

-- ─────────────────────────────────────────────
-- 0. EXTENSÕES
-- ─────────────────────────────────────────────
CREATE EXTENSION IF NOT EXISTS "pgcrypto";   -- gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "citext";     -- e-mail case-insensitive (opcional)

-- ─────────────────────────────────────────────
-- 1. TABELA: parser_versoes
--    (referenciada por extratos, criada primeiro)
-- ─────────────────────────────────────────────
CREATE TABLE parser_versoes (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    banco           VARCHAR(100) NOT NULL,
    versao          VARCHAR(20)  NOT NULL,
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    score_qualidade NUMERIC(4,3),
    total_usos      INT          NOT NULL DEFAULT 0,
    total_erros     INT          NOT NULL DEFAULT 0,
    descricao       TEXT,
    depreciado_em   TIMESTAMPTZ,
    UNIQUE (banco, versao)
);

-- ─────────────────────────────────────────────
-- 2. TABELA: usuarios
-- ─────────────────────────────────────────────
CREATE TABLE usuarios (
    id                       UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo                   VARCHAR(12)  UNIQUE NOT NULL,          -- ex: FIN-A1B2C3
    cpf                      VARCHAR(255) NOT NULL,                 -- AES-256 em repouso
    cpf_hash                 VARCHAR(64)  UNIQUE,                   -- SHA-256 para lookup
    nome_completo            VARCHAR(150) NOT NULL,
    email                    VARCHAR(255) UNIQUE NOT NULL,
    senha_hash               VARCHAR(255) NOT NULL,                 -- bcrypt
    telefone                 VARCHAR(20),
    telegram_chat_id         BIGINT,
    whatsapp_id              VARCHAR(100),
    saldo_inicial_definido   BOOLEAN      NOT NULL DEFAULT FALSE,
    moeda                    CHAR(3)      NOT NULL DEFAULT 'BRL',
    ativo                    BOOLEAN      NOT NULL DEFAULT TRUE,
    email_verificado         BOOLEAN      NOT NULL DEFAULT FALSE,
    token_verificacao        VARCHAR(100),
    token_verificacao_expira TIMESTAMPTZ,
    ultimo_login             TIMESTAMPTZ,
    tentativas_login         SMALLINT     NOT NULL DEFAULT 0,
    bloqueado_ate            TIMESTAMPTZ,
    refresh_token_hash       VARCHAR(255),
    api_key_hash             VARCHAR(255),
    deleted_at               TIMESTAMPTZ,
    versao                   INT          NOT NULL DEFAULT 1,
    criado_em                TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    atualizado_em            TIMESTAMPTZ
);

-- ─────────────────────────────────────────────
-- 3. TABELA: contas_financeiras
-- ─────────────────────────────────────────────
CREATE TABLE contas_financeiras (
    id            UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id    UUID          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    nome          VARCHAR(100)  NOT NULL,
    tipo          VARCHAR(20)   NOT NULL CHECK (tipo IN ('corrente','poupanca','cartao','dinheiro','investimento')),
    banco         VARCHAR(100),
    saldo_inicial NUMERIC(15,2) NOT NULL DEFAULT 0,
    padrao        BOOLEAN       DEFAULT FALSE,
    ativa         BOOLEAN       DEFAULT TRUE,
    criado_em     TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMPTZ
);

-- ─────────────────────────────────────────────
-- 4. TABELA: categorias
-- ─────────────────────────────────────────────
CREATE TABLE categorias (
    id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    nome             VARCHAR(100) NOT NULL,
    categoria_pai_id UUID         REFERENCES categorias(id),
    tipo             VARCHAR(10)  NOT NULL CHECK (tipo IN ('RECEITA','GASTO','AMBOS')),
    icone            VARCHAR(50),
    cor_hex          CHAR(7),
    padrao           BOOLEAN      DEFAULT FALSE,  -- TRUE = global imutável
    usuario_id       UUID         REFERENCES usuarios(id),
    criado_em        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- ─────────────────────────────────────────────
-- 5. TABELA: categoria_thresholds
-- ─────────────────────────────────────────────
CREATE TABLE categoria_thresholds (
    id               UUID     PRIMARY KEY DEFAULT gen_random_uuid(),
    categoria_id     UUID     UNIQUE NOT NULL REFERENCES categorias(id),
    threshold_auto   SMALLINT NOT NULL DEFAULT 70  CHECK (threshold_auto   BETWEEN 0 AND 100),
    threshold_alerta SMALLINT NOT NULL DEFAULT 50  CHECK (threshold_alerta BETWEEN 0 AND 100),
    ambiguidade_alta BOOLEAN  NOT NULL DEFAULT FALSE
);

-- ─────────────────────────────────────────────
-- 6. TABELA: extratos
-- ─────────────────────────────────────────────
CREATE TABLE extratos (
    id                    UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id            UUID          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    conta_id              UUID          NOT NULL REFERENCES contas_financeiras(id),
    arquivo_nome          VARCHAR(255),
    arquivo_uuid          VARCHAR(255)  NOT NULL,
    hash_arquivo          VARCHAR(64)   NOT NULL,
    banco_detectado       VARCHAR(100),
    parser_versao_id      UUID          REFERENCES parser_versoes(id),
    score_extracao        NUMERIC(4,3),
    periodo_inicio        DATE,
    periodo_fim           DATE,
    status                VARCHAR(30)   NOT NULL DEFAULT 'upload_recebido'
                            CHECK (status IN (
                                'upload_recebido','validando','na_fila','extraindo',
                                'classificando','aguardando_ia','pendente_revisao',
                                'parcialmente_revisado','concluido','erro_formato',
                                'erro_extracao','erro_classificacao','erro_timeout',
                                'cancelado','reprocessando'
                            )),
    total_lancamentos     INT           NOT NULL DEFAULT 0,
    lancamentos_confirmados INT         NOT NULL DEFAULT 0,
    lancamentos_pendentes INT           NOT NULL DEFAULT 0,
    lancamentos_ignorados INT           NOT NULL DEFAULT 0,
    versao                INT           NOT NULL DEFAULT 1,
    criado_em             TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    atualizado_em         TIMESTAMPTZ
);

-- ─────────────────────────────────────────────
-- 7. TABELA: transacoes
-- ─────────────────────────────────────────────
CREATE TABLE transacoes (
    id                   UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id           UUID          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    conta_id             UUID          NOT NULL REFERENCES contas_financeiras(id),
    extrato_id           UUID          REFERENCES extratos(id),
    transacao_estorno_id UUID          REFERENCES transacoes(id),        -- auto-referência
    tipo                 VARCHAR(10)   NOT NULL CHECK (tipo IN ('RECEITA','GASTO')),
    descricao_original   TEXT,
    descricao_usuario    VARCHAR(255),
    descricao_normalizada VARCHAR(255),
    valor                NUMERIC(15,2) NOT NULL CHECK (valor > 0),
    data_transacao       DATE          NOT NULL,
    data_lancamento      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    categoria_id         UUID          REFERENCES categorias(id),
    subcategoria         VARCHAR(100),
    estabelecimento      VARCHAR(255),
    origem               VARCHAR(20)   NOT NULL DEFAULT 'manual'
                            CHECK (origem IN ('manual','pdf','api')),
    status_revisao       VARCHAR(30)   NOT NULL DEFAULT 'extraida'
                            CHECK (status_revisao IN (
                                'extraida','classificada','pendente_revisao',
                                'confirmada','ignorada','arquivada'
                            )),
    confianca_ia         SMALLINT      CHECK (confianca_ia BETWEEN 0 AND 100),
    recorrente           BOOLEAN       DEFAULT FALSE,
    periodo_recorrencia  VARCHAR(20),
    observacao           TEXT,
    deleted_at           TIMESTAMPTZ,
    versao               INT           NOT NULL DEFAULT 1,
    criado_em            TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    atualizado_em        TIMESTAMPTZ
);

-- ─────────────────────────────────────────────
-- 8. TABELA: regras_classificacao
-- ─────────────────────────────────────────────
CREATE TABLE regras_classificacao (
    id                    UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id            UUID          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    termo                 VARCHAR(255)  NOT NULL,
    categoria_id          UUID          NOT NULL REFERENCES categorias(id),
    subcategoria          VARCHAR(100),
    score_confianca       NUMERIC(4,3)  NOT NULL DEFAULT 1.000,
    vezes_aplicada        INT           NOT NULL DEFAULT 0,
    vezes_corretas        INT           NOT NULL DEFAULT 0,
    vezes_incorretas      INT           NOT NULL DEFAULT 0,
    correcoes_consecutivas SMALLINT     NOT NULL DEFAULT 0,
    congelada             BOOLEAN       NOT NULL DEFAULT FALSE,
    congelada_motivo      TEXT,
    congelada_ate         DATE,
    criada_por            VARCHAR(20)   NOT NULL DEFAULT 'usuario'
                            CHECK (criada_por IN ('usuario','ia_auto','admin')),
    ultima_aplicacao      TIMESTAMPTZ,
    ultima_correcao       TIMESTAMPTZ,
    criado_em             TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- ─────────────────────────────────────────────
-- 9. TABELA: classificacao_logs
-- ─────────────────────────────────────────────
CREATE TABLE classificacao_logs (
    id                  UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    transacao_id        UUID        NOT NULL REFERENCES transacoes(id),
    usuario_id          UUID        NOT NULL REFERENCES usuarios(id),
    estrategia          VARCHAR(20) NOT NULL CHECK (estrategia IN ('regra_usuario','dicionario','ia_api','fallback')),
    modelo_ia_versao    VARCHAR(50),
    prompt_versao       VARCHAR(20),
    prompt_hash         VARCHAR(64),
    resposta_raw        TEXT,
    categoria_sugerida  UUID        REFERENCES categorias(id),
    confianca           SMALLINT    NOT NULL CHECK (confianca BETWEEN 0 AND 100),
    tokens_usados       INT,
    latencia_ms         INT,
    categoria_final     UUID        REFERENCES categorias(id),
    corrigida           BOOLEAN     NOT NULL DEFAULT FALSE,
    corrigida_em        TIMESTAMPTZ,
    motivo_correcao     TEXT,
    criado_em           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ─────────────────────────────────────────────
-- 10. TABELA: processamento_jobs
-- ─────────────────────────────────────────────
CREATE TABLE processamento_jobs (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    extrato_id      UUID        REFERENCES extratos(id),
    tipo            VARCHAR(30) NOT NULL CHECK (tipo IN (
                        'extracao_pdf','classificacao_ia','notificacao',
                        'geracao_pdf','snapshot','anonimizacao'
                    )),
    status          VARCHAR(30) NOT NULL DEFAULT 'enfileirado' CHECK (status IN (
                        'enfileirado','iniciando','processando','aguardando_ia',
                        'concluido','falha_ia','falha_parser','timeout',
                        'retry_1','retry_2','retry_3','dead_letter','cancelado'
                    )),
    tentativas      SMALLINT    NOT NULL DEFAULT 0,
    max_tentativas  SMALLINT    NOT NULL DEFAULT 3,
    payload         JSONB,
    erro_mensagem   TEXT,
    worker_id       VARCHAR(100),
    lock_expires_at TIMESTAMPTZ,
    correlation_id  UUID,
    enfileirado_em  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    iniciado_em     TIMESTAMPTZ,
    concluido_em    TIMESTAMPTZ,
    proximo_retry   TIMESTAMPTZ
);

-- ─────────────────────────────────────────────
-- 11. TABELA: snapshots_financeiros
-- ─────────────────────────────────────────────
CREATE TABLE snapshots_financeiros (
    id             UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id     UUID          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    conta_id       UUID          REFERENCES contas_financeiras(id),   -- NULL = consolidado
    ano            SMALLINT      NOT NULL,
    mes            SMALLINT      NOT NULL CHECK (mes BETWEEN 1 AND 12),
    saldo_inicial  NUMERIC(15,2) NOT NULL,
    total_receitas NUMERIC(15,2) NOT NULL DEFAULT 0,
    total_gastos   NUMERIC(15,2) NOT NULL DEFAULT 0,
    saldo_final    NUMERIC(15,2) NOT NULL,
    fechado        BOOLEAN       NOT NULL DEFAULT FALSE,
    fechado_em     TIMESTAMPTZ,
    UNIQUE (usuario_id, conta_id, ano, mes)
);

-- ─────────────────────────────────────────────
-- 12. TABELA: auditoria_eventos (particionada por mês)
-- ─────────────────────────────────────────────
CREATE TABLE auditoria_eventos (
    id               UUID        NOT NULL DEFAULT gen_random_uuid(),
    correlation_id   UUID        NOT NULL,
    usuario_id       UUID        REFERENCES usuarios(id),
    entidade         VARCHAR(50) NOT NULL,
    entidade_id      UUID        NOT NULL,
    acao             VARCHAR(30) NOT NULL CHECK (acao IN (
                         'CREATE','UPDATE','DELETE','LOGIN','LOGOUT','UPLOAD',
                         'CLASSIFY','CONFIRM','REJECT','EXPORT','REPROCESS',
                         'API_KEY_GEN','PASSWORD_CHANGE'
                     )),
    dados_anteriores JSONB,       -- NUNCA incluir CPF, senha, tokens
    dados_novos      JSONB,
    ip_origem        INET,
    user_agent       TEXT,
    origem           VARCHAR(20)  CHECK (origem IN ('web','bot_whatsapp','bot_telegram','api','sistema')),
    criado_em        TIMESTAMPTZ  NOT NULL DEFAULT NOW()   -- IMUTÁVEL
) PARTITION BY RANGE (criado_em);

-- Partições do ano corrente (adicione conforme necessidade)
CREATE TABLE auditoria_eventos_2025_01 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
CREATE TABLE auditoria_eventos_2025_02 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');
CREATE TABLE auditoria_eventos_2025_03 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-03-01') TO ('2025-04-01');
CREATE TABLE auditoria_eventos_2025_04 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-04-01') TO ('2025-05-01');
CREATE TABLE auditoria_eventos_2025_05 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-05-01') TO ('2025-06-01');
CREATE TABLE auditoria_eventos_2025_06 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-06-01') TO ('2025-07-01');
CREATE TABLE auditoria_eventos_2025_07 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-07-01') TO ('2025-08-01');
CREATE TABLE auditoria_eventos_2025_08 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-08-01') TO ('2025-09-01');
CREATE TABLE auditoria_eventos_2025_09 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-09-01') TO ('2025-10-01');
CREATE TABLE auditoria_eventos_2025_10 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');
CREATE TABLE auditoria_eventos_2025_11 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-11-01') TO ('2025-12-01');
CREATE TABLE auditoria_eventos_2025_12 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2025-12-01') TO ('2026-01-01');
CREATE TABLE auditoria_eventos_2026_01 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');
CREATE TABLE auditoria_eventos_2026_02 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');
CREATE TABLE auditoria_eventos_2026_03 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');
CREATE TABLE auditoria_eventos_2026_04 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-04-01') TO ('2026-05-01');
CREATE TABLE auditoria_eventos_2026_05 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');
CREATE TABLE auditoria_eventos_2026_06 PARTITION OF auditoria_eventos
    FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');

-- ─────────────────────────────────────────────
-- 13. TABELA: consentimentos_lgpd
-- ─────────────────────────────────────────────
CREATE TABLE consentimentos_lgpd (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id      UUID        NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    tipo            VARCHAR(50) NOT NULL CHECK (tipo IN (
                        'tratamento_dados_financeiros','uso_ia',
                        'armazenamento_extrato','bot_whatsapp','bot_telegram'
                    )),
    versao_politica VARCHAR(20) NOT NULL,
    consentido      BOOLEAN     NOT NULL,
    ip_origem       INET,
    criado_em       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    revogado_em     TIMESTAMPTZ,
    revogado_motivo TEXT
);

-- ─────────────────────────────────────────────
-- 14. TABELA: notificacoes
-- ─────────────────────────────────────────────
CREATE TABLE notificacoes (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID        NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    canal      VARCHAR(20) NOT NULL CHECK (canal IN ('whatsapp','telegram','email')),
    tipo       VARCHAR(50) NOT NULL,
    titulo     VARCHAR(255),
    mensagem   TEXT,
    enviada    BOOLEAN     NOT NULL DEFAULT FALSE,
    enviada_em TIMESTAMPTZ,
    erro       TEXT,
    tentativas SMALLINT    NOT NULL DEFAULT 0,
    criado_em  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ═══════════════════════════════════════════════════════════════
--  ÍNDICES ESSENCIAIS
-- ═══════════════════════════════════════════════════════════════
-- usuarios
CREATE INDEX idx_usuarios_email         ON usuarios(email)     WHERE deleted_at IS NULL;
CREATE INDEX idx_usuarios_cpf_hash      ON usuarios(cpf_hash)  WHERE deleted_at IS NULL;
CREATE INDEX idx_usuarios_codigo        ON usuarios(codigo);

-- contas_financeiras
CREATE INDEX idx_contas_usuario         ON contas_financeiras(usuario_id);

-- categorias
CREATE INDEX idx_categorias_tipo        ON categorias(tipo);
CREATE INDEX idx_categorias_padrao      ON categorias(padrao)  WHERE padrao = TRUE;
CREATE INDEX idx_categorias_usuario     ON categorias(usuario_id) WHERE usuario_id IS NOT NULL;

-- transacoes
CREATE INDEX idx_transacoes_usuario     ON transacoes(usuario_id, data_transacao DESC) WHERE deleted_at IS NULL;
CREATE INDEX idx_transacoes_conta       ON transacoes(conta_id);
CREATE INDEX idx_transacoes_categoria   ON transacoes(categoria_id);
CREATE INDEX idx_transacoes_status      ON transacoes(status_revisao);
CREATE INDEX idx_transacoes_extrato     ON transacoes(extrato_id) WHERE extrato_id IS NOT NULL;

-- extratos
CREATE INDEX idx_extratos_usuario       ON extratos(usuario_id, criado_em DESC);
CREATE INDEX idx_extratos_hash          ON extratos(hash_arquivo);   -- deduplicação

-- regras_classificacao
CREATE INDEX idx_regras_usuario_termo   ON regras_classificacao(usuario_id, termo);

-- classificacao_logs
CREATE INDEX idx_classlog_transacao     ON classificacao_logs(transacao_id);

-- snapshots
CREATE INDEX idx_snapshots_usuario_mes  ON snapshots_financeiros(usuario_id, ano DESC, mes DESC);

-- auditoria_eventos (na tabela mãe; herdada pelas partições)
CREATE INDEX idx_audit_usuario          ON auditoria_eventos(usuario_id, criado_em DESC);
CREATE INDEX idx_audit_correlation      ON auditoria_eventos(correlation_id);
CREATE INDEX idx_audit_entidade         ON auditoria_eventos(entidade, entidade_id);


-- ═══════════════════════════════════════════════════════════════
--  SEED — PARSER_VERSOES
-- ═══════════════════════════════════════════════════════════════
INSERT INTO parser_versoes (banco, versao, ativo, score_qualidade, descricao) VALUES
  ('Nubank',   'v1.0', TRUE,  0.980, 'Parser Nubank — extrato CSV/PDF'),
  ('Itaú',     'v1.0', TRUE,  0.960, 'Parser Itaú  — PDF padrão'),
  ('Bradesco',  'v1.0', TRUE,  0.940, 'Parser Bradesco — PDF'),
  ('Santander', 'v1.0', TRUE,  0.920, 'Parser Santander — PDF'),
  ('Caixa',    'v1.0', TRUE,  0.900, 'Parser Caixa — PDF'),
  ('Inter',    'v1.0', TRUE,  0.970, 'Parser Inter — CSV/PDF'),
  ('XP',       'v1.0', TRUE,  0.930, 'Parser XP Investimentos — PDF'),
  ('Genérico', 'v1.0', TRUE,  0.700, 'Parser genérico de fallback');


-- ═══════════════════════════════════════════════════════════════
--  SEED — CATEGORIAS GLOBAIS (padrao = TRUE, usuario_id = NULL)
--  UUIDs fixos para referência confiável em toda a aplicação
-- ═══════════════════════════════════════════════════════════════

-- ── CATEGORIAS RAIZ ──────────────────────────────────────────
INSERT INTO categorias (id, nome, tipo, icone, cor_hex, padrao) VALUES
  -- Receitas
  ('00000000-0000-0000-0000-000000000001', 'Salário',           'RECEITA', '💰', '#16A34A', TRUE),
  ('00000000-0000-0000-0000-000000000002', 'Freelance',         'RECEITA', '💻', '#0EA5E9', TRUE),
  ('00000000-0000-0000-0000-000000000003', 'Investimentos',     'RECEITA', '📈', '#7C3AED', TRUE),
  ('00000000-0000-0000-0000-000000000004', 'Outras Receitas',   'RECEITA', '➕', '#64748B', TRUE),

  -- Gastos — nível raiz
  ('00000000-0000-0000-0000-000000000010', 'Alimentação',       'GASTO',   '🍽️', '#EF4444', TRUE),
  ('00000000-0000-0000-0000-000000000011', 'Transporte',        'GASTO',   '🚗', '#F97316', TRUE),
  ('00000000-0000-0000-0000-000000000012', 'Moradia',           'GASTO',   '🏠', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000013', 'Saúde',             'GASTO',   '🏥', '#EC4899', TRUE),
  ('00000000-0000-0000-0000-000000000014', 'Educação',          'GASTO',   '📚', '#8B5CF6', TRUE),
  ('00000000-0000-0000-0000-000000000015', 'Lazer',             'GASTO',   '🎮', '#06B6D4', TRUE),
  ('00000000-0000-0000-0000-000000000016', 'Vestuário',         'GASTO',   '👗', '#F43F5E', TRUE),
  ('00000000-0000-0000-0000-000000000017', 'Assinaturas',       'GASTO',   '📱', '#6366F1', TRUE),
  ('00000000-0000-0000-0000-000000000018', 'Financeiro',        'GASTO',   '🏦', '#059669', TRUE),
  ('00000000-0000-0000-0000-000000000019', 'Streaming',         'GASTO',   '🎬', '#DC2626', TRUE),
  ('00000000-0000-0000-0000-000000000020', 'Outros',            'AMBOS',   '📦', '#94A3B8', TRUE);

-- ── SUBCATEGORIAS — Alimentação ───────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000101', 'Supermercado',  '00000000-0000-0000-0000-000000000010', 'GASTO', '🛒', '#EF4444', TRUE),
  ('00000000-0000-0000-0000-000000000102', 'Restaurante',   '00000000-0000-0000-0000-000000000010', 'GASTO', '🍴', '#EF4444', TRUE),
  ('00000000-0000-0000-0000-000000000103', 'Delivery',      '00000000-0000-0000-0000-000000000010', 'GASTO', '🛵', '#EF4444', TRUE),
  ('00000000-0000-0000-0000-000000000104', 'Padaria/Café',  '00000000-0000-0000-0000-000000000010', 'GASTO', '☕', '#EF4444', TRUE),
  ('00000000-0000-0000-0000-000000000105', 'Lanchonete',    '00000000-0000-0000-0000-000000000010', 'GASTO', '🍔', '#EF4444', TRUE);

-- ── SUBCATEGORIAS — Transporte ────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000111', 'Combustível',   '00000000-0000-0000-0000-000000000011', 'GASTO', '⛽', '#F97316', TRUE),
  ('00000000-0000-0000-0000-000000000112', 'Uber/99',       '00000000-0000-0000-0000-000000000011', 'GASTO', '🚕', '#F97316', TRUE),
  ('00000000-0000-0000-0000-000000000113', 'Transporte Público', '00000000-0000-0000-0000-000000000011', 'GASTO', '🚌', '#F97316', TRUE),
  ('00000000-0000-0000-0000-000000000114', 'Estacionamento','00000000-0000-0000-0000-000000000011', 'GASTO', '🅿️', '#F97316', TRUE),
  ('00000000-0000-0000-0000-000000000115', 'Manutenção',    '00000000-0000-0000-0000-000000000011', 'GASTO', '🔧', '#F97316', TRUE);

-- ── SUBCATEGORIAS — Moradia ───────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000121', 'Aluguel',       '00000000-0000-0000-0000-000000000012', 'GASTO', '🔑', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000122', 'Condomínio',    '00000000-0000-0000-0000-000000000012', 'GASTO', '🏢', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000123', 'Energia',       '00000000-0000-0000-0000-000000000012', 'GASTO', '💡', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000124', 'Água',          '00000000-0000-0000-0000-000000000012', 'GASTO', '💧', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000125', 'Internet',      '00000000-0000-0000-0000-000000000012', 'GASTO', '📶', '#FBBF24', TRUE),
  ('00000000-0000-0000-0000-000000000126', 'Gás',           '00000000-0000-0000-0000-000000000012', 'GASTO', '🔥', '#FBBF24', TRUE);

-- ── SUBCATEGORIAS — Saúde ─────────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000131', 'Plano de Saúde','00000000-0000-0000-0000-000000000013', 'GASTO', '🏥', '#EC4899', TRUE),
  ('00000000-0000-0000-0000-000000000132', 'Farmácia',      '00000000-0000-0000-0000-000000000013', 'GASTO', '💊', '#EC4899', TRUE),
  ('00000000-0000-0000-0000-000000000133', 'Consultas',     '00000000-0000-0000-0000-000000000013', 'GASTO', '👨‍⚕️', '#EC4899', TRUE),
  ('00000000-0000-0000-0000-000000000134', 'Academia',      '00000000-0000-0000-0000-000000000013', 'GASTO', '🏋️', '#EC4899', TRUE);

-- ── SUBCATEGORIAS — Educação ──────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000141', 'Mensalidade',   '00000000-0000-0000-0000-000000000014', 'GASTO', '🎓', '#8B5CF6', TRUE),
  ('00000000-0000-0000-0000-000000000142', 'Cursos Online', '00000000-0000-0000-0000-000000000014', 'GASTO', '💡', '#8B5CF6', TRUE),
  ('00000000-0000-0000-0000-000000000143', 'Livros',        '00000000-0000-0000-0000-000000000014', 'GASTO', '📖', '#8B5CF6', TRUE);

-- ── SUBCATEGORIAS — Lazer ─────────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000151', 'Cinema/Teatro', '00000000-0000-0000-0000-000000000015', 'GASTO', '🎭', '#06B6D4', TRUE),
  ('00000000-0000-0000-0000-000000000152', 'Jogos',         '00000000-0000-0000-0000-000000000015', 'GASTO', '🕹️', '#06B6D4', TRUE),
  ('00000000-0000-0000-0000-000000000153', 'Viagem',        '00000000-0000-0000-0000-000000000015', 'GASTO', '✈️', '#06B6D4', TRUE),
  ('00000000-0000-0000-0000-000000000154', 'Bares/Baladas', '00000000-0000-0000-0000-000000000015', 'GASTO', '🍻', '#06B6D4', TRUE);

-- ── SUBCATEGORIAS — Assinaturas ───────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000171', 'Celular',       '00000000-0000-0000-0000-000000000017', 'GASTO', '📲', '#6366F1', TRUE),
  ('00000000-0000-0000-0000-000000000172', 'Softwares',     '00000000-0000-0000-0000-000000000017', 'GASTO', '🖥️', '#6366F1', TRUE),
  ('00000000-0000-0000-0000-000000000173', 'Clubes/Serviços','00000000-0000-0000-0000-000000000017','GASTO', '🔄', '#6366F1', TRUE);

-- ── SUBCATEGORIAS — Streaming ─────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000191', 'Netflix',       '00000000-0000-0000-0000-000000000019', 'GASTO', '🎬', '#DC2626', TRUE),
  ('00000000-0000-0000-0000-000000000192', 'Spotify',       '00000000-0000-0000-0000-000000000019', 'GASTO', '🎵', '#DC2626', TRUE),
  ('00000000-0000-0000-0000-000000000193', 'YouTube Premium','00000000-0000-0000-0000-000000000019','GASTO', '▶️', '#DC2626', TRUE),
  ('00000000-0000-0000-0000-000000000194', 'Disney+',       '00000000-0000-0000-0000-000000000019', 'GASTO', '🏰', '#DC2626', TRUE),
  ('00000000-0000-0000-0000-000000000195', 'Amazon Prime',  '00000000-0000-0000-0000-000000000019', 'GASTO', '📦', '#DC2626', TRUE);

-- ── SUBCATEGORIAS — Financeiro ────────────────────────────────
INSERT INTO categorias (id, nome, categoria_pai_id, tipo, icone, cor_hex, padrao) VALUES
  ('00000000-0000-0000-0000-000000000181', 'Empréstimo',    '00000000-0000-0000-0000-000000000018', 'GASTO', '💳', '#059669', TRUE),
  ('00000000-0000-0000-0000-000000000182', 'Cartão Crédito','00000000-0000-0000-0000-000000000018', 'GASTO', '💳', '#059669', TRUE),
  ('00000000-0000-0000-0000-000000000183', 'Tarifas Bancárias','00000000-0000-0000-0000-000000000018','GASTO','🏦','#059669', TRUE),
  ('00000000-0000-0000-0000-000000000184', 'Seguro',        '00000000-0000-0000-0000-000000000018', 'GASTO', '🛡️', '#059669', TRUE);


-- ── SEED: categoria_thresholds ────────────────────────────────
--  Streaming = 90, Alimentação/Delivery = 70, Outros = 100
INSERT INTO categoria_thresholds (categoria_id, threshold_auto, threshold_alerta, ambiguidade_alta) VALUES
  ('00000000-0000-0000-0000-000000000019', 90, 50, FALSE),  -- Streaming
  ('00000000-0000-0000-0000-000000000103', 70, 50, FALSE),  -- Delivery
  ('00000000-0000-0000-0000-000000000010', 70, 50, FALSE),  -- Alimentação
  ('00000000-0000-0000-0000-000000000020', 100, 50, TRUE);  -- Outros (nunca auto)


-- ═══════════════════════════════════════════════════════════════
--  SEED — USUÁRIO DE DEMONSTRAÇÃO
-- ═══════════════════════════════════════════════════════════════
-- ATENÇÃO: senha_hash abaixo é bcrypt de 'Senha@2025'
-- Em produção NUNCA insira senhas reais via SQL; use a API.
-- CPF abaixo é fictício e está em placeholder (sem criptografia real).

DO $$
DECLARE
  v_usuario_id        UUID := '11111111-1111-1111-1111-111111111111';
  v_conta_nubank      UUID := '22222222-2222-2222-2222-222222222201';
  v_conta_itau        UUID := '22222222-2222-2222-2222-222222222202';
  v_conta_carteira    UUID := '22222222-2222-2222-2222-222222222203';
  v_extrato_id        UUID := '33333333-3333-3333-3333-333333333301';
  v_parser_nubank_id  UUID;
  v_corr              UUID := gen_random_uuid();

BEGIN

  -- ── Busca o parser Nubank ──────────────────────────────────
  SELECT id INTO v_parser_nubank_id FROM parser_versoes WHERE banco = 'Nubank' AND versao = 'v1.0';

  -- ── Usuário ───────────────────────────────────────────────
  INSERT INTO usuarios (
    id, codigo, cpf, cpf_hash, nome_completo, email, senha_hash,
    telefone, saldo_inicial_definido, moeda, ativo, email_verificado,
    ultimo_login, versao
  ) VALUES (
    v_usuario_id,
    'FIN-DEMO01',
    'ENCRYPTED_CPF_PLACEHOLDER',          -- substituir pelo CPF real criptografado
    encode(sha256('12345678909'::bytea), 'hex'),  -- SHA-256 do CPF fictício
    'João Silva Demonstração',
    'joao.demo@finapp.com.br',
    '$2b$12$K2hXdW7eL9mNqRvP3sBuCeXgTlAaDz5yQfJcMkwE1nHoVpYrI8uGa',  -- bcrypt placeholder
    '(11) 99999-0000',
    TRUE,
    'BRL',
    TRUE,
    TRUE,
    NOW(),
    1
  );

  -- ── Consentimentos LGPD ───────────────────────────────────
  INSERT INTO consentimentos_lgpd (usuario_id, tipo, versao_politica, consentido, ip_origem) VALUES
    (v_usuario_id, 'tratamento_dados_financeiros', '1.0', TRUE, '192.0.2.1'),
    (v_usuario_id, 'uso_ia',                       '1.0', TRUE, '192.0.2.1'),
    (v_usuario_id, 'armazenamento_extrato',        '1.0', TRUE, '192.0.2.1');

  -- ── Contas financeiras ────────────────────────────────────
  INSERT INTO contas_financeiras (id, usuario_id, nome, tipo, banco, saldo_inicial, padrao, ativa) VALUES
    (v_conta_nubank,   v_usuario_id, 'Nubank Conta',    'corrente', 'Nubank',  2500.00, TRUE,  TRUE),
    (v_conta_itau,     v_usuario_id, 'Itaú Corrente',   'corrente', 'Itaú',   10000.00, FALSE, TRUE),
    (v_conta_carteira, v_usuario_id, 'Carteira/Dinheiro','dinheiro', NULL,       300.00, FALSE, TRUE);

  -- ── Extrato de exemplo (Nubank — Maio 2025) ───────────────
  INSERT INTO extratos (
    id, usuario_id, conta_id, arquivo_nome, arquivo_uuid, hash_arquivo,
    banco_detectado, parser_versao_id, score_extracao,
    periodo_inicio, periodo_fim, status,
    total_lancamentos, lancamentos_confirmados, lancamentos_pendentes
  ) VALUES (
    v_extrato_id,
    v_usuario_id,
    v_conta_nubank,
    'nubank_maio_2025.pdf',
    gen_random_uuid()::text,
    encode(sha256('nubank_maio_2025_content_hash'::bytea), 'hex'),
    'Nubank',
    v_parser_nubank_id,
    0.980,
    '2025-05-01', '2025-05-31',
    'concluido',
    8, 6, 2
  );

  -- ── TRANSAÇÕES — Gastos ───────────────────────────────────
  INSERT INTO transacoes (
    usuario_id, conta_id, extrato_id, tipo, descricao_original,
    descricao_normalizada, valor, data_transacao,
    categoria_id, subcategoria, estabelecimento, origem, status_revisao, confianca_ia
  ) VALUES

    -- Supermercado
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 02/05 PAGUE MENOS SP',
     'COMPRA PAGUE MENOS SP',
     347.89, '2025-05-02',
     '00000000-0000-0000-0000-000000000101', 'Supermercado', 'Pague Menos',
     'pdf', 'confirmada', 92),

    -- Delivery
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 04/05 IFOOD*RESTAURANTE SP',
     'IFOOD RESTAURANTE SP',
     58.90, '2025-05-04',
     '00000000-0000-0000-0000-000000000103', 'Delivery', 'iFood',
     'pdf', 'confirmada', 95),

    -- Uber
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 05/05 UBER *TRIP SP',
     'UBER TRIP SP',
     34.70, '2025-05-05',
     '00000000-0000-0000-0000-000000000112', 'Uber/99', 'Uber',
     'pdf', 'confirmada', 98),

    -- Streaming Netflix
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 07/05 NETFLIX.COM SP',
     'NETFLIX COM SP',
     55.90, '2025-05-07',
     '00000000-0000-0000-0000-000000000191', 'Streaming', 'Netflix',
     'pdf', 'confirmada', 99),

    -- Farmácia
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 10/05 DROGA RAIA SP',
     'DROGA RAIA SP',
     89.50, '2025-05-10',
     '00000000-0000-0000-0000-000000000132', 'Farmácia', 'Droga Raia',
     'pdf', 'confirmada', 91),

    -- Conta de luz (manual)
    (v_usuario_id, v_conta_itau, NULL, 'GASTO',
     NULL,
     'ENEL SP ENERGIA ELETRICA',
     180.00, '2025-05-12',
     '00000000-0000-0000-0000-000000000123', 'Energia', 'ENEL SP',
     'manual', 'confirmada', NULL),

    -- Pendente de revisão
    (v_usuario_id, v_conta_nubank, v_extrato_id, 'GASTO',
     'COMPRA 20/05 AMAZON MKTPLC SP',
     'AMAZON MKTPLC SP',
     215.00, '2025-05-20',
     '00000000-0000-0000-0000-000000000020', NULL, 'Amazon',
     'pdf', 'pendente_revisao', 45),

    -- Carteira — lanche
    (v_usuario_id, v_conta_carteira, NULL, 'GASTO',
     NULL,
     'LANCHONETE ESQUINA',
     22.00, '2025-05-22',
     '00000000-0000-0000-0000-000000000105', 'Lanchonete', 'Lanchonete Esquina',
     'manual', 'confirmada', NULL);

  -- ── TRANSAÇÕES — Receitas ─────────────────────────────────
  INSERT INTO transacoes (
    usuario_id, conta_id, tipo, descricao_usuario,
    descricao_normalizada, valor, data_transacao,
    categoria_id, subcategoria, origem, status_revisao,
    recorrente, periodo_recorrencia
  ) VALUES

    -- Salário
    (v_usuario_id, v_conta_itau, 'RECEITA',
     'Salário Maio/2025',
     'SALARIO MAIO 2025',
     6800.00, '2025-05-05',
     '00000000-0000-0000-0000-000000000001', NULL, 'manual', 'confirmada',
     TRUE, 'mensal'),

    -- Freelance
    (v_usuario_id, v_conta_nubank, 'RECEITA',
     'Projeto Web — Cliente ABC',
     'PROJETO WEB CLIENTE ABC',
     1500.00, '2025-05-15',
     '00000000-0000-0000-0000-000000000002', 'Desenvolvimento', 'manual', 'confirmada',
     FALSE, NULL);

  -- ── Regras de classificação do usuário ───────────────────
  INSERT INTO regras_classificacao (usuario_id, termo, categoria_id, subcategoria, criada_por) VALUES
    (v_usuario_id, 'IFOOD',    '00000000-0000-0000-0000-000000000103', 'Delivery',       'ia_auto'),
    (v_usuario_id, 'NETFLIX',  '00000000-0000-0000-0000-000000000191', 'Streaming',      'ia_auto'),
    (v_usuario_id, 'UBER',     '00000000-0000-0000-0000-000000000112', 'Transporte',     'ia_auto'),
    (v_usuario_id, 'DROGA RAIA','00000000-0000-0000-0000-000000000132','Farmácia',       'usuario'),
    (v_usuario_id, 'ENEL',     '00000000-0000-0000-0000-000000000123', 'Energia',        'usuario');

  -- ── Snapshot financeiro Maio 2025 ─────────────────────────
  -- Conta Nubank
  INSERT INTO snapshots_financeiros (
    usuario_id, conta_id, ano, mes,
    saldo_inicial, total_receitas, total_gastos, saldo_final
  ) VALUES
    (v_usuario_id, v_conta_nubank, 2025, 5,
     2500.00,
     1500.00,
     (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 215.00),
     2500.00 + 1500.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 215.00)),

  -- Conta Itaú
    (v_usuario_id, v_conta_itau, 2025, 5,
     10000.00,
     6800.00,
     180.00,
     10000.00 + 6800.00 - 180.00),

  -- Consolidado (conta_id = NULL)
    (v_usuario_id, NULL, 2025, 5,
     12800.00,
     (6800.00 + 1500.00),
     (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 215.00 + 22.00),
     12800.00 + 8300.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 215.00 + 22.00));

  -- ── Auditoria — evento de criação do usuário ──────────────
  INSERT INTO auditoria_eventos (
    correlation_id, usuario_id, entidade, entidade_id,
    acao, dados_novos, ip_origem, origem
  ) VALUES (
    v_corr,
    v_usuario_id,
    'usuario',
    v_usuario_id,
    'CREATE',
    '{"nome_completo":"João Silva Demonstração","email":"joao.demo@finapp.com.br","moeda":"BRL"}'::jsonb,
    '192.0.2.1',
    'web'
  );

  -- ── Notificação de boas-vindas ────────────────────────────
  INSERT INTO notificacoes (usuario_id, canal, tipo, titulo, mensagem, enviada, enviada_em) VALUES
    (v_usuario_id, 'email', 'boas_vindas',
     'Bem-vindo ao FinApp! 🎉',
     'Olá João, sua conta foi criada com sucesso. Comece importando seu primeiro extrato!',
     TRUE, NOW());

END $$;


-- ═══════════════════════════════════════════════════════════════
--  VERIFICAÇÃO RÁPIDA (execute separadamente para checar)
-- ═══════════════════════════════════════════════════════════════
/*
SELECT 'usuarios'            AS tabela, COUNT(*) FROM usuarios
UNION ALL
SELECT 'contas_financeiras', COUNT(*) FROM contas_financeiras
UNION ALL
SELECT 'categorias',         COUNT(*) FROM categorias
UNION ALL
SELECT 'categoria_thresholds',COUNT(*) FROM categoria_thresholds
UNION ALL
SELECT 'extratos',           COUNT(*) FROM extratos
UNION ALL
SELECT 'transacoes',         COUNT(*) FROM transacoes
UNION ALL
SELECT 'regras_classificacao',COUNT(*) FROM regras_classificacao
UNION ALL
SELECT 'snapshots_financeiros',COUNT(*) FROM snapshots_financeiros
UNION ALL
SELECT 'auditoria_eventos',  COUNT(*) FROM auditoria_eventos
UNION ALL
SELECT 'consentimentos_lgpd',COUNT(*) FROM consentimentos_lgpd
UNION ALL
SELECT 'notificacoes',       COUNT(*) FROM notificacoes;
*/
