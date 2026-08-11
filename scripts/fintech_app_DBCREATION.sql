-- ============================================================
--  FINAPP — DDL para Oracle SQL Developer Data Modeler
--  Curado manualmente a partir do mapeamento JPA atual (adapters/out/persistence/**)
--  RDBMS alvo: SQL Server 2012
--  Atualizado em: 2026-07-13
--
--  Como importar:
--    1. File → Data Modeler → Import → DDL File...
--    2. Selecionar este arquivo
--    3. RDBMS: SQL Server 2012
--    4. Clicar OK
--
--  Convenção adotada em todas as tabelas:
--    - PK própria:   <entidade>_id   BIGINT IDENTITY(1,1)
--    - Código único: <entidade>_code VARCHAR(6) NOT NULL UNIQUE
--    - Toda referência de FK usa o PAR (xxx_id, xxx_code) — chave composta.
--
--  TABELAS REMOVIDAS NESTA REVISÃO (não correspondem a nenhuma entidade
--  JPA atual — resíduos de iteração anterior do app):
--    - categorias_do_usuario
--    - categorias_pai
--    - gastos
-- ============================================================


-- ═══════════════════════════════════════════════════════════════
--  TABELAS
--  Ordem respeita dependências de FK
-- ═══════════════════════════════════════════════════════════════

-- ─────────────────────────────────────────────
--  USUARIOS  (sem FK)
-- ─────────────────────────────────────────────
CREATE TABLE usuarios (
    usuario_id       BIGINT        IDENTITY(1,1) NOT NULL,
    usuario_code     VARCHAR(6)    NOT NULL,
    cpf              VARCHAR(255)  NOT NULL,
    nome             VARCHAR(255)  NOT NULL,
    email            VARCHAR(255)  NOT NULL,
    senha            VARCHAR(255)  NOT NULL,
    telefone         VARCHAR(255),
    telegram_chatid  BIGINT,
    whatsapp_chatid  BIGINT,
    email_verificado BIT,
    dt_nascimento    DATE          NOT NULL,
    CONSTRAINT PK_usuarios PRIMARY KEY (usuario_id)
);

-- ─────────────────────────────────────────────
--  BANCO  (catálogo de instituições financeiras — sem FK)
-- ─────────────────────────────────────────────
CREATE TABLE banco (
    banco_id    BIGINT        IDENTITY(1,1) NOT NULL,
    banco_code  VARCHAR(6)    NOT NULL,
    nome        VARCHAR(100)  NOT NULL,
    descricao   VARCHAR(255),
    cor_hex     VARCHAR(7),
    icone       VARCHAR(50),
    CONSTRAINT PK_banco PRIMARY KEY (banco_id)
);

-- ─────────────────────────────────────────────
--  CATEGORIAS  (sem FK)
-- ─────────────────────────────────────────────
CREATE TABLE categorias (
    categoria_id   BIGINT        IDENTITY(1,1) NOT NULL,
    categoria_code VARCHAR(6)    NOT NULL,
    nome           VARCHAR(100)  NOT NULL,
    tipo           VARCHAR(10)   NOT NULL
                       CONSTRAINT CK_categorias_tipo
                       CHECK (tipo IN ('RECEITA','GASTO','AMBOS')),
    icone          VARCHAR(50),
    cor_hex        VARCHAR(7),
    padrao         BIT,
    criado_em      DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_categorias PRIMARY KEY (categoria_id)
);

-- ─────────────────────────────────────────────
--  CATEGORIA_THRESHOLDS  → FK composta para categorias
-- ─────────────────────────────────────────────
CREATE TABLE categoria_thresholds (
    categoria_threshold_id   BIGINT     IDENTITY(1,1) NOT NULL,
    categoria_threshold_code VARCHAR(6) NOT NULL,
    categoria_id             BIGINT     NOT NULL,
    categoria_code           VARCHAR(6) NOT NULL,
    threshold_auto           SMALLINT   NOT NULL,
    threshold_alerta         SMALLINT   NOT NULL,
    ambiguidade_alta         BIT        NOT NULL,
    CONSTRAINT PK_categoria_thresholds PRIMARY KEY (categoria_threshold_id)
);

-- ─────────────────────────────────────────────
--  MOTIVOS_CANCELAMENTO  (sem FK)
-- ─────────────────────────────────────────────
CREATE TABLE motivos_cancelamento (
    motivo_id         BIGINT        IDENTITY(1,1) NOT NULL,
    motivo_code       VARCHAR(6)    NOT NULL,
    descricao         VARCHAR(255)  NOT NULL,
    origem_permitida  VARCHAR(20)   NOT NULL
                          CONSTRAINT CK_motivos_origem
                          CHECK (origem_permitida IN ('usuario','sistema','admin','todos')),
    ativo             BIT           NOT NULL,
    criado_em         DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_motivos_cancelamento PRIMARY KEY (motivo_id)
);

-- ─────────────────────────────────────────────
--  CONTAS_FINANCEIRAS  → FK composta para usuarios e banco
-- ─────────────────────────────────────────────
CREATE TABLE contas_financeiras (
    conta_id      BIGINT        IDENTITY(1,1) NOT NULL,
    conta_code    VARCHAR(6)    NOT NULL,
    usuario_id    BIGINT        NOT NULL,
    usuario_code  VARCHAR(6)    NOT NULL,
    banco_id      BIGINT        NOT NULL,
    banco_code    VARCHAR(6)    NOT NULL,
    tipo          VARCHAR(20)   NOT NULL
                      CONSTRAINT CK_contas_tipo
                      CHECK (tipo IN ('corrente','poupanca','cartao','dinheiro','investimento')),
    saldo_inicial NUMERIC(15,2) NOT NULL,
    saldo_atual   NUMERIC(15,2) NOT NULL,
    padrao        BIT,
    ativa         BIT,
    criado_em     DATETIME2(6)  NOT NULL,
    atualizado_em DATETIME2(6),
    ind_delete    VARCHAR(1)    NOT NULL,
    deleted_at    DATETIME2(6),
    CONSTRAINT PK_contas_financeiras PRIMARY KEY (conta_id)
);

-- ─────────────────────────────────────────────
--  TRANSACOES  → FK composta para contas_financeiras e categorias
-- ─────────────────────────────────────────────
CREATE TABLE transacoes (
    transacoes_id           BIGINT        IDENTITY(1,1) NOT NULL,
    transacoes_code         VARCHAR(6)    NOT NULL,
    conta_id                BIGINT        NOT NULL,
    conta_code               VARCHAR(6)    NOT NULL,
    ind_estorno              VARCHAR(1)    NOT NULL,
    descricao                 NVARCHAR(MAX),
    valor                      NUMERIC(15,2) NOT NULL,
    data_transacao              DATE          NOT NULL,
    categoria_id                 BIGINT        NOT NULL,
    categoria_code                VARCHAR(6)    NOT NULL,
    estabelecimento                VARCHAR(255),
    origem                          VARCHAR(20)   NOT NULL
                                       CONSTRAINT CK_transacoes_origem
                                       CHECK (origem IN ('manual','pdf','api','importado')),
    status_revisao                   VARCHAR(30)   NOT NULL
                                       CONSTRAINT CK_transacoes_status
                                       CHECK (status_revisao IN (
                                           'EXTRAIDA','CLASSIFICADA','PENDENTE_REVISAO',
                                           'CONFIRMADA','IGNORADA','ARQUIVADA'
                                       )),
    confianca_ia                       SMALLINT,
    recorrente                          BIT,
    periodo_recorrencia                  DATE,
    observacao                            NVARCHAR(MAX),
    deleted_at                             DATETIME2(6),
    estornado_at                            DATETIME2(6),
    versao                                   INT           NOT NULL,
    criado_em                                 DATETIME2(6)  NOT NULL,
    atualizado_em                              DATETIME2(6),
    transacao_estornada_id                      BIGINT,
    CONSTRAINT PK_transacoes PRIMARY KEY (transacoes_id)
);

-- ─────────────────────────────────────────────
--  TRANSACOES_CANCELADAS  → FK composta para transacoes e motivos_cancelamento
-- ─────────────────────────────────────────────
CREATE TABLE transacoes_canceladas (
    transacao_cancelada_id   BIGINT        IDENTITY(1,1) NOT NULL,
    transacao_cancelada_code VARCHAR(6)    NOT NULL,
    transacao_id             BIGINT        NOT NULL,
    transacao_code           VARCHAR(6)    NOT NULL,
    motivo_id                BIGINT        NOT NULL,
    motivo_code              VARCHAR(6)    NOT NULL,
    cancelado_por             VARCHAR(20)   NOT NULL
                                  CONSTRAINT CK_txcanc_cancelado_por
                                  CHECK (cancelado_por IN ('usuario','sistema','admin')),
    valor_original             NUMERIC(15,2) NOT NULL,
    observacao                  NVARCHAR(MAX),
    ip_origem                    VARCHAR(45),
    cancelado_em                  DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_transacoes_canceladas PRIMARY KEY (transacao_cancelada_id)
);

-- ─────────────────────────────────────────────
--  PARSER_VERSOES  (sem FK)
-- ─────────────────────────────────────────────
CREATE TABLE parser_versoes (
    parser_versao_id    BIGINT        IDENTITY(1,1) NOT NULL,
    parser_versao_code  VARCHAR(6)    NOT NULL,
    banco                VARCHAR(100)  NOT NULL,
    versao                VARCHAR(20)   NOT NULL,
    ativo                  BIT           NOT NULL,
    score_qualidade        NUMERIC(4,3),
    total_usos              INT           NOT NULL,
    total_erros               INT           NOT NULL,
    descricao                  NVARCHAR(MAX),
    depreciado_em                DATETIME2(6),
    CONSTRAINT PK_parser_versoes PRIMARY KEY (parser_versao_id)
);

-- ─────────────────────────────────────────────
--  EXTRATOS  → FK composta para usuarios, contas_financeiras,
--              transacoes (nullable) e parser_versoes (nullable)
-- ─────────────────────────────────────────────
CREATE TABLE extratos (
    extrato_id                BIGINT        IDENTITY(1,1) NOT NULL,
    extrato_code               VARCHAR(6)    NOT NULL,
    usuario_id                  BIGINT        NOT NULL,
    usuario_code                 VARCHAR(6)    NOT NULL,
    conta_id                      BIGINT        NOT NULL,
    conta_code                     VARCHAR(6)    NOT NULL,
    transacao_id                    BIGINT,
    transacao_code                   VARCHAR(6),
    arquivo_nome                      VARCHAR(255),
    arquivo_uuid                       VARCHAR(255)  NOT NULL,
    hash_arquivo                        VARCHAR(64)   NOT NULL,
    banco_detectado                      VARCHAR(100),
    parser_versao_id                      BIGINT,
    parser_versao_code                     VARCHAR(6),
    score_extracao                          NUMERIC(4,3),
    periodo_inicio                           DATE,
    periodo_fim                               DATE,
    status                                     VARCHAR(30)   NOT NULL
                                                   CONSTRAINT CK_extratos_status
                                                   CHECK (status IN (
                                                       'upload_recebido','validando','na_fila','extraindo',
                                                       'classificando','aguardando_ia','pendente_revisao',
                                                       'parcialmente_revisado','concluido','erro_formato',
                                                       'erro_extracao','erro_classificacao','erro_timeout',
                                                       'cancelado','reprocessando'
                                                   )),
    total_lancamentos                            INT           NOT NULL,
    lancamentos_confirmados                       INT           NOT NULL,
    lancamentos_pendentes                          INT           NOT NULL,
    lancamentos_ignorados                           INT           NOT NULL,
    versao                                           INT           NOT NULL,
    criado_em                                         DATETIME2(6)  NOT NULL,
    atualizado_em                                      DATETIME2(6),
    ind_delete                                          VARCHAR(1)    NOT NULL,
    deleted_at                                           DATETIME2(6),
    CONSTRAINT PK_extratos PRIMARY KEY (extrato_id)
);

-- ─────────────────────────────────────────────
--  PROCESSAMENTO_JOBS  → FK composta para extratos
-- ─────────────────────────────────────────────
CREATE TABLE processamento_jobs (
    processamento_job_id    BIGINT        IDENTITY(1,1) NOT NULL,
    processamento_job_code  VARCHAR(6)    NOT NULL,
    extrato_id                BIGINT        NOT NULL,
    extrato_code                VARCHAR(6)    NOT NULL,
    tipo                          VARCHAR(30)   NOT NULL
                                     CONSTRAINT CK_jobs_tipo
                                     CHECK (tipo IN (
                                         'extracao_pdf','classificacao_ia','notificacao',
                                         'geracao_pdf','snapshot','anonimizacao'
                                     )),
    status                          VARCHAR(30)   NOT NULL
                                       CONSTRAINT CK_jobs_status
                                       CHECK (status IN (
                                           'enfileirado','iniciando','processando','aguardando_ia',
                                           'concluido','falha_ia','falha_parser','timeout',
                                           'retry_1','retry_2','retry_3','dead_letter','cancelado'
                                       )),
    tentativas                        SMALLINT      NOT NULL,
    max_tentativas                      SMALLINT      NOT NULL,
    payload                               NVARCHAR(MAX),
    erro_mensagem                          NVARCHAR(MAX),
    worker_id                                VARCHAR(100),
    lock_expires_at                            DATETIME2(6),
    correlation_id                               BIGINT,
    enfileirado_em                                DATETIME2(6)  NOT NULL,
    iniciado_em                                    DATETIME2(6),
    concluido_em                                    DATETIME2(6),
    proximo_retry                                    DATETIME2(6),
    CONSTRAINT PK_processamento_jobs PRIMARY KEY (processamento_job_id)
);

-- ─────────────────────────────────────────────
--  CONSENTIMENTOS_LGPD  → FK composta para usuarios
-- ─────────────────────────────────────────────
CREATE TABLE consentimentos_lgpd (
    consentimento_lgpd_id    BIGINT        IDENTITY(1,1) NOT NULL,
    consentimento_lgpd_code  VARCHAR(6)    NOT NULL,
    usuario_id                 BIGINT        NOT NULL,
    usuario_code                VARCHAR(6)    NOT NULL,
    tipo                          VARCHAR(50)   NOT NULL
                                     CONSTRAINT CK_lgpd_tipo
                                     CHECK (tipo IN (
                                         'tratamento_dados_financeiros','uso_ia',
                                         'armazenamento_extrato','bot_whatsapp','bot_telegram'
                                     )),
    versao_politica                 VARCHAR(20)   NOT NULL,
    consentido                       BIT           NOT NULL,
    ip_origem                         VARCHAR(45),
    criado_em                          DATETIME2(6)  NOT NULL,
    revogado_em                         DATETIME2(6),
    revogado_motivo                      NVARCHAR(MAX),
    CONSTRAINT PK_consentimentos_lgpd PRIMARY KEY (consentimento_lgpd_id)
);

-- ─────────────────────────────────────────────
--  NOTIFICACOES  → FK composta para usuarios
-- ─────────────────────────────────────────────
CREATE TABLE notificacoes (
    notificacao_id    BIGINT        IDENTITY(1,1) NOT NULL,
    notificacao_code  VARCHAR(6)    NOT NULL,
    usuario_id          BIGINT        NOT NULL,
    usuario_code         VARCHAR(6)    NOT NULL,
    canal                  VARCHAR(20)   NOT NULL
                              CONSTRAINT CK_notif_canal
                              CHECK (canal IN ('whatsapp','telegram','email')),
    tipo                    VARCHAR(50)   NOT NULL,
    titulo                    VARCHAR(255),
    mensagem                    NVARCHAR(MAX),
    enviada                       BIT           NOT NULL,
    enviada_em                     DATETIME2(6),
    erro                             NVARCHAR(MAX),
    tentativas                        SMALLINT      NOT NULL,
    criado_em                           DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_notificacoes PRIMARY KEY (notificacao_id)
);

-- ─────────────────────────────────────────────
--  SESSAO_TOKENS  (NOVA — não existia nas revisões anteriores deste
--  script; corresponde a SessaoTokenEntity, usada para sessão/login)
--  → FK composta para usuarios
-- ─────────────────────────────────────────────
CREATE TABLE sessao_tokens (
    sessao_token_id    BIGINT        IDENTITY(1,1) NOT NULL,
    sessao_token_code  VARCHAR(6)    NOT NULL,
    token                 VARCHAR(255)  NOT NULL,
    usuario_id             BIGINT        NOT NULL,
    usuario_code            VARCHAR(6)    NOT NULL,
    criado_em                 DATETIME2(6)  NOT NULL,
    expira_em                   DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_sessao_tokens PRIMARY KEY (sessao_token_id)
);

-- ─────────────────────────────────────────────
--  SNAPSHOTS_FINANCEIROS  → FK composta para usuarios e
--  contas_financeiras (nullable — NULL representa o consolidado do usuário)
-- ─────────────────────────────────────────────
CREATE TABLE snapshots_financeiros (
    snapshot_financeiro_id    BIGINT        IDENTITY(1,1) NOT NULL,
    snapshot_financeiro_code  VARCHAR(6)    NOT NULL,
    usuario_id                  BIGINT        NOT NULL,
    usuario_code                 VARCHAR(6)    NOT NULL,
    conta_id                       BIGINT,
    conta_code                       VARCHAR(6),
    ano                                SMALLINT      NOT NULL,
    mes                                  SMALLINT      NOT NULL,
    saldo_inicial                         NUMERIC(15,2) NOT NULL,
    total_receitas                          NUMERIC(15,2) NOT NULL,
    total_gastos                              NUMERIC(15,2) NOT NULL,
    saldo_final                                 NUMERIC(15,2) NOT NULL,
    fechado                                       BIT           NOT NULL,
    fechado_em                                      DATETIME2(6),
    CONSTRAINT PK_snapshots_financeiros PRIMARY KEY (snapshot_financeiro_id)
);

-- ─────────────────────────────────────────────
--  AUDITORIA_EVENTOS  → FK composta para usuarios (nullable — eventos
--  de sistema sem usuário associado); entidade_id é polimórfico
--  (aponta para PKs de tabelas diferentes conforme "entidade") e por
--  isso não tem FK física.
-- ─────────────────────────────────────────────
CREATE TABLE auditoria_eventos (
    auditoria_evento_id    BIGINT        IDENTITY(1,1) NOT NULL,
    auditoria_evento_code  VARCHAR(6)    NOT NULL,
    correlation_id           BIGINT        NOT NULL,
    usuario_id                 BIGINT,
    usuario_code                VARCHAR(6),
    entidade                      VARCHAR(50)   NOT NULL,
    entidade_id                     BIGINT        NOT NULL,
    acao                               VARCHAR(30)   NOT NULL
                                          CONSTRAINT CK_audit_acao
                                          CHECK (acao IN (
                                              'CREATE','UPDATE','DELETE','LOGIN','LOGOUT','UPLOAD',
                                              'CLASSIFY','CONFIRM','REJECT','EXPORT','REPROCESS',
                                              'API_KEY_GEN','PASSWORD_CHANGE','CANCEL'
                                          )),
    dados_anteriores                     NVARCHAR(MAX),
    dados_novos                            NVARCHAR(MAX),
    ip_origem                                VARCHAR(45),
    user_agent                                 NVARCHAR(MAX),
    origem                                       VARCHAR(20)
                                                     CONSTRAINT CK_audit_origem
                                                     CHECK (origem IN ('web','bot_whatsapp','bot_telegram','api','sistema')),
    criado_em                                       DATETIME2(6)  NOT NULL,
    CONSTRAINT PK_auditoria_eventos PRIMARY KEY (auditoria_evento_id)
);


-- ═══════════════════════════════════════════════════════════════
--  UNIQUE CONSTRAINTS
--  code isolado (unicidade de negócio) + par (id, code) — este
--  último é exigido para servir de alvo de FK composta.
-- ═══════════════════════════════════════════════════════════════

ALTER TABLE usuarios              ADD CONSTRAINT UK_usuarios_cpf UNIQUE (cpf);
ALTER TABLE usuarios              ADD CONSTRAINT UK_usuarios_email UNIQUE (email);
ALTER TABLE usuarios              ADD CONSTRAINT UK_usuarios_code UNIQUE (usuario_code);
ALTER TABLE usuarios              ADD CONSTRAINT UK_usuarios_id_code UNIQUE (usuario_id, usuario_code);

ALTER TABLE banco                 ADD CONSTRAINT UK_banco_code UNIQUE (banco_code);
ALTER TABLE banco                 ADD CONSTRAINT UK_banco_id_code UNIQUE (banco_id, banco_code);

ALTER TABLE categorias            ADD CONSTRAINT UK_categorias_code UNIQUE (categoria_code);
ALTER TABLE categorias            ADD CONSTRAINT UK_categorias_id_code UNIQUE (categoria_id, categoria_code);

ALTER TABLE categoria_thresholds  ADD CONSTRAINT UK_categoria_thresholds_code UNIQUE (categoria_threshold_code);
ALTER TABLE categoria_thresholds  ADD CONSTRAINT UK_categoria_thresholds_categoria UNIQUE (categoria_id);

ALTER TABLE motivos_cancelamento  ADD CONSTRAINT UK_motivos_cancelamento_code UNIQUE (motivo_code);
ALTER TABLE motivos_cancelamento  ADD CONSTRAINT UK_motivos_cancelamento_id_code UNIQUE (motivo_id, motivo_code);

ALTER TABLE contas_financeiras    ADD CONSTRAINT UK_contas_financeiras_code UNIQUE (conta_code);
ALTER TABLE contas_financeiras    ADD CONSTRAINT UK_contas_financeiras_id_code UNIQUE (conta_id, conta_code);

ALTER TABLE transacoes            ADD CONSTRAINT UK_transacoes_code UNIQUE (transacoes_code);
ALTER TABLE transacoes            ADD CONSTRAINT UK_transacoes_id_code UNIQUE (transacoes_id, transacoes_code);

ALTER TABLE transacoes_canceladas ADD CONSTRAINT UK_transacoes_canceladas_code UNIQUE (transacao_cancelada_code);

ALTER TABLE parser_versoes        ADD CONSTRAINT UK_parser_versoes_code UNIQUE (parser_versao_code);
ALTER TABLE parser_versoes        ADD CONSTRAINT UK_parser_versoes_id_code UNIQUE (parser_versao_id, parser_versao_code);

ALTER TABLE extratos              ADD CONSTRAINT UK_extratos_code UNIQUE (extrato_code);
ALTER TABLE extratos              ADD CONSTRAINT UK_extratos_id_code UNIQUE (extrato_id, extrato_code);

ALTER TABLE processamento_jobs    ADD CONSTRAINT UK_processamento_jobs_code UNIQUE (processamento_job_code);

ALTER TABLE consentimentos_lgpd   ADD CONSTRAINT UK_consentimentos_lgpd_code UNIQUE (consentimento_lgpd_code);

ALTER TABLE notificacoes          ADD CONSTRAINT UK_notificacoes_code UNIQUE (notificacao_code);

ALTER TABLE sessao_tokens         ADD CONSTRAINT UK_sessao_tokens_token UNIQUE (token);
ALTER TABLE sessao_tokens         ADD CONSTRAINT UK_sessao_tokens_code UNIQUE (sessao_token_code);

ALTER TABLE snapshots_financeiros ADD CONSTRAINT UK_snapshots_financeiros_code UNIQUE (snapshot_financeiro_code);

ALTER TABLE auditoria_eventos     ADD CONSTRAINT UK_auditoria_eventos_code UNIQUE (auditoria_evento_code);


-- ═══════════════════════════════════════════════════════════════
--  FOREIGN KEYS  (todas compostas: par id + code, exceto onde indicado)
-- ═══════════════════════════════════════════════════════════════

ALTER TABLE categoria_thresholds
    ADD CONSTRAINT fk_categoria_thresholds_categoria
    FOREIGN KEY (categoria_id, categoria_code) REFERENCES categorias (categoria_id, categoria_code);

ALTER TABLE contas_financeiras
    ADD CONSTRAINT fk_contas_financeiras_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE contas_financeiras
    ADD CONSTRAINT fk_contas_financeiras_banco
    FOREIGN KEY (banco_id, banco_code) REFERENCES banco (banco_id, banco_code);

ALTER TABLE transacoes
    ADD CONSTRAINT fk_transacoes_conta
    FOREIGN KEY (conta_id, conta_code) REFERENCES contas_financeiras (conta_id, conta_code);

ALTER TABLE transacoes
    ADD CONSTRAINT fk_transacoes_categoria
    FOREIGN KEY (categoria_id, categoria_code) REFERENCES categorias (categoria_id, categoria_code);

ALTER TABLE transacoes_canceladas
    ADD CONSTRAINT fk_transacoes_canceladas_transacao
    FOREIGN KEY (transacao_id, transacao_code) REFERENCES transacoes (transacoes_id, transacoes_code);

ALTER TABLE transacoes_canceladas
    ADD CONSTRAINT fk_transacoes_canceladas_motivo
    FOREIGN KEY (motivo_id, motivo_code) REFERENCES motivos_cancelamento (motivo_id, motivo_code);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_conta
    FOREIGN KEY (conta_id, conta_code) REFERENCES contas_financeiras (conta_id, conta_code);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_transacao
    FOREIGN KEY (transacao_id, transacao_code) REFERENCES transacoes (transacoes_id, transacoes_code);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_parser_versao
    FOREIGN KEY (parser_versao_id, parser_versao_code) REFERENCES parser_versoes (parser_versao_id, parser_versao_code);

ALTER TABLE processamento_jobs
    ADD CONSTRAINT fk_processamento_jobs_extrato
    FOREIGN KEY (extrato_id, extrato_code) REFERENCES extratos (extrato_id, extrato_code);

ALTER TABLE consentimentos_lgpd
    ADD CONSTRAINT fk_consentimentos_lgpd_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE notificacoes
    ADD CONSTRAINT fk_notificacoes_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE sessao_tokens
    ADD CONSTRAINT fk_sessao_tokens_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE snapshots_financeiros
    ADD CONSTRAINT fk_snapshots_financeiros_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);

ALTER TABLE snapshots_financeiros
    ADD CONSTRAINT fk_snapshots_financeiros_conta
    FOREIGN KEY (conta_id, conta_code) REFERENCES contas_financeiras (conta_id, conta_code);

-- auditoria_eventos.usuario_id/usuario_code: FK opcional (eventos de sistema
-- podem não ter usuário). entidade_id é polimórfico e não tem FK física.
ALTER TABLE auditoria_eventos
    ADD CONSTRAINT fk_auditoria_eventos_usuario
    FOREIGN KEY (usuario_id, usuario_code) REFERENCES usuarios (usuario_id, usuario_code);
