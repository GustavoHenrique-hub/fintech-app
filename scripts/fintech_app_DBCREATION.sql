-- ============================================================
--  FINAPP — DDL para Oracle SQL Developer Data Modeler
--  Extraído do log Hibernate: 2026-05-27 13:06:45
--  RDBMS alvo: SQL Server 2012
--
--  Como importar:
--    1. File → Data Modeler → Import → DDL File...
--    2. Selecionar este arquivo
--    3. RDBMS: SQL Server 2012
--    4. Clicar OK
-- ============================================================


-- ═══════════════════════════════════════════════════════════════
--  TABELAS
--  Ordem respeita dependências de FK
-- ═══════════════════════════════════════════════════════════════

-- ─────────────────────────────────────────────
--  USUARIOS
-- ─────────────────────────────────────────────
CREATE TABLE usuarios (
    id_usuario       BIGINT        IDENTITY(1,1) NOT NULL,
    cpf              VARCHAR(255)  NOT NULL,
    dt_nascimento    DATE          NOT NULL,
    email            VARCHAR(255)  NOT NULL,
    email_verificado BIT,
    nome             VARCHAR(255)  NOT NULL,
    senha            VARCHAR(255)  NOT NULL,
    telefone         VARCHAR(255),
    telegram_chatid  BIGINT,
    usuario_code     VARCHAR(255)  NOT NULL,
    whatsapp_chatid  BIGINT,
    CONSTRAINT PK_usuarios PRIMARY KEY (id_usuario)
);

-- ─────────────────────────────────────────────
--  BANCO  ← nova tabela
--  Representa as instituições financeiras.
--  Separada de contas_financeiras para normalização:
--  nome e identidade visual do banco não dependem da conta.
-- ─────────────────────────────────────────────
CREATE TABLE banco (
    banco_id    BIGINT        IDENTITY(1,1) NOT NULL,
    banco_code  VARCHAR(6)    NOT NULL,
    cor_hex     VARCHAR(7),
    descricao   VARCHAR(255),
    icone       VARCHAR(50),
    nome        VARCHAR(100)  NOT NULL,
    CONSTRAINT PK_banco PRIMARY KEY (banco_id)
);

-- ─────────────────────────────────────────────
--  PARSER_VERSOES
-- ─────────────────────────────────────────────
CREATE TABLE parser_versoes (
    id              BIGINT        IDENTITY(1,1) NOT NULL,
    ativo           BIT           NOT NULL,
    banco           VARCHAR(100)  NOT NULL,
    depreciado_em   DATETIME2(6),
    descricao       NVARCHAR(MAX),
    score_qualidade NUMERIC(4,3),
    total_erros     INT           NOT NULL,
    total_usos      INT           NOT NULL,
    versao          VARCHAR(20)   NOT NULL,
    CONSTRAINT PK_parser_versoes PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  CATEGORIAS
-- ─────────────────────────────────────────────
CREATE TABLE categorias (
    id              BIGINT       IDENTITY(1,1) NOT NULL,
    categorias_code VARCHAR(6)   NOT NULL,
    cor_hex         VARCHAR(7),
    criado_em       DATETIME2(6) NOT NULL,
    icone           VARCHAR(50),
    nome            VARCHAR(100) NOT NULL,
    padrao          BIT,
    tipo            VARCHAR(10)  NOT NULL
                        CONSTRAINT CK_categorias_tipo
                        CHECK (tipo IN ('RECEITA','GASTO','AMBOS')),
    CONSTRAINT PK_categorias PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  CATEGORIA_THRESHOLDS
-- ─────────────────────────────────────────────
CREATE TABLE categoria_thresholds (
    id               BIGINT   IDENTITY(1,1) NOT NULL,
    ambiguidade_alta BIT      NOT NULL,
    categoria_id     BIGINT   NOT NULL,
    threshold_alerta SMALLINT NOT NULL,
    threshold_auto   SMALLINT NOT NULL,
    CONSTRAINT PK_categoria_thresholds PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  MOTIVOS_CANCELAMENTO
-- ─────────────────────────────────────────────
CREATE TABLE motivos_cancelamento (
    id                        BIGINT        IDENTITY(1,1) NOT NULL,
    ativo                     BIT           NOT NULL,
    motivos_cancelamento_code VARCHAR(6)    NOT NULL,
    criado_em                 DATETIME2(6)  NOT NULL,
    descricao                 VARCHAR(255)  NOT NULL,
    origem_permitida          VARCHAR(20)   NOT NULL
                                  CONSTRAINT CK_motivos_origem
                                  CHECK (origem_permitida IN ('usuario','sistema','admin','todos')),
    CONSTRAINT PK_motivos_cancelamento PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  CONTAS_FINANCEIRAS
--  ATENÇÃO — banco agora é FK para tabela banco:
--    + banco_id  BIGINT NOT NULL
--    + banco_code VARCHAR(6) NOT NULL
--    - banco VARCHAR(100)  (removido)
--    - nome  VARCHAR(100)  (removido)
-- ─────────────────────────────────────────────
CREATE TABLE contas_financeiras (
    id            BIGINT        IDENTITY(1,1) NOT NULL,
    ativa         BIT,
    atualizado_em DATETIME2(6),
    banco_code    VARCHAR(6)    NOT NULL,
    banco_id      BIGINT        NOT NULL,
    contas_code   VARCHAR(6)    NOT NULL,
    criado_em     DATETIME2(6)  NOT NULL,
    padrao        BIT,
    saldo_inicial NUMERIC(15,2) NOT NULL,
    tipo          VARCHAR(20)   NOT NULL
                      CONSTRAINT CK_contas_tipo
                      CHECK (tipo IN ('corrente','poupanca','cartao','dinheiro','investimento')),
    usuario_id    BIGINT        NOT NULL,
    CONSTRAINT PK_contas_financeiras PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  TRANSACOES
-- ─────────────────────────────────────────────
CREATE TABLE transacoes (
    id                  BIGINT        IDENTITY(1,1) NOT NULL,
    atualizado_em       DATETIME2(6),
    categoria_id        BIGINT        NOT NULL,
    transacoes_code     VARCHAR(6)    NOT NULL,
    confianca_ia        SMALLINT,
    conta_id            BIGINT        NOT NULL,
    criado_em           DATETIME2(6)  NOT NULL,
    data_transacao      DATE          NOT NULL,
    deleted_at          DATETIME2(6),
    descricao           NVARCHAR(MAX),
    estabelecimento     VARCHAR(255),
    ind_estorno         VARCHAR(1)    NOT NULL,
    observacao          NVARCHAR(MAX),
    origem              VARCHAR(20)   NOT NULL
                            CONSTRAINT CK_transacoes_origem
                            CHECK (origem IN ('manual','pdf','api')),
    periodo_recorrencia DATE,
    recorrente          BIT,
    status_revisao      VARCHAR(30)   NOT NULL
                            CONSTRAINT CK_transacoes_status
                            CHECK (status_revisao IN (
                                'EXTRAIDA','CLASSIFICADA','PENDENTE_REVISAO',
                                'CONFIRMADA','IGNORADA','ARQUIVADA'
                            )),
    tipo                VARCHAR(10)   NOT NULL
                            CONSTRAINT CK_transacoes_tipo
                            CHECK (tipo IN ('RECEITA','GASTO')),
    usuario_id          BIGINT        NOT NULL,
    valor               NUMERIC(15,2) NOT NULL,
    versao              INT           NOT NULL,
    CONSTRAINT PK_transacoes PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  EXTRATOS
-- ─────────────────────────────────────────────
CREATE TABLE extratos (
    id                      BIGINT        IDENTITY(1,1) NOT NULL,
    arquivo_nome            VARCHAR(255),
    arquivo_uuid            VARCHAR(255)  NOT NULL,
    atualizado_em           DATETIME2(6),
    banco_detectado         VARCHAR(100),
    extratos_code           VARCHAR(6)    NOT NULL,
    conta_id                BIGINT        NOT NULL,
    criado_em               DATETIME2(6)  NOT NULL,
    hash_arquivo            VARCHAR(64)   NOT NULL,
    lancamentos_confirmados INT           NOT NULL,
    lancamentos_ignorados   INT           NOT NULL,
    lancamentos_pendentes   INT           NOT NULL,
    parser_versao_id        BIGINT,
    periodo_fim             DATE,
    periodo_inicio          DATE,
    score_extracao          NUMERIC(4,3),
    status                  VARCHAR(30)   NOT NULL
                                CONSTRAINT CK_extratos_status
                                CHECK (status IN (
                                    'upload_recebido','validando','na_fila','extraindo',
                                    'classificando','aguardando_ia','pendente_revisao',
                                    'parcialmente_revisado','concluido','erro_formato',
                                    'erro_extracao','erro_classificacao','erro_timeout',
                                    'cancelado','reprocessando'
                                )),
    total_lancamentos       INT           NOT NULL,
    transacao_id            BIGINT,
    usuario_id              BIGINT        NOT NULL,
    versao                  INT           NOT NULL,
    CONSTRAINT PK_extratos PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  TRANSACOES_CANCELADAS
-- ─────────────────────────────────────────────
CREATE TABLE transacoes_canceladas (
    id             BIGINT        IDENTITY(1,1) NOT NULL,
    cancelado_em   DATETIME2(6)  NOT NULL,
    cancelado_por  VARCHAR(20)   NOT NULL
                       CONSTRAINT CK_txcanc_cancelado_por
                       CHECK (cancelado_por IN ('usuario','sistema','admin')),
    ip_origem      VARCHAR(45),
    motivo_id      BIGINT        NOT NULL,
    observacao     NVARCHAR(MAX),
    transacao_id   BIGINT        NOT NULL,
    valor_original NUMERIC(15,2) NOT NULL,
    CONSTRAINT PK_transacoes_canceladas PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  PROCESSAMENTO_JOBS
-- ─────────────────────────────────────────────
CREATE TABLE processamento_jobs (
    id              BIGINT        IDENTITY(1,1) NOT NULL,
    concluido_em    DATETIME2(6),
    correlation_id  BIGINT,
    enfileirado_em  DATETIME2(6)  NOT NULL,
    erro_mensagem   NVARCHAR(MAX),
    extrato_id      BIGINT,
    iniciado_em     DATETIME2(6),
    lock_expires_at DATETIME2(6),
    max_tentativas  SMALLINT      NOT NULL,
    payload         NVARCHAR(MAX),
    proximo_retry   DATETIME2(6),
    status          VARCHAR(30)   NOT NULL
                        CONSTRAINT CK_jobs_status
                        CHECK (status IN (
                            'enfileirado','iniciando','processando','aguardando_ia',
                            'concluido','falha_ia','falha_parser','timeout',
                            'retry_1','retry_2','retry_3','dead_letter','cancelado'
                        )),
    tentativas      SMALLINT      NOT NULL,
    tipo            VARCHAR(30)   NOT NULL
                        CONSTRAINT CK_jobs_tipo
                        CHECK (tipo IN (
                            'extracao_pdf','classificacao_ia','notificacao',
                            'geracao_pdf','snapshot','anonimizacao'
                        )),
    worker_id       VARCHAR(100),
    CONSTRAINT PK_processamento_jobs PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  SNAPSHOTS_FINANCEIROS
-- ─────────────────────────────────────────────
CREATE TABLE snapshots_financeiros (
    id             BIGINT        IDENTITY(1,1) NOT NULL,
    ano            SMALLINT      NOT NULL,
    conta_id       BIGINT,
    fechado        BIT           NOT NULL,
    fechado_em     DATETIME2(6),
    mes            SMALLINT      NOT NULL,
    saldo_final    NUMERIC(15,2) NOT NULL,
    saldo_inicial  NUMERIC(15,2) NOT NULL,
    total_gastos   NUMERIC(15,2) NOT NULL,
    total_receitas NUMERIC(15,2) NOT NULL,
    usuario_id     BIGINT        NOT NULL,
    CONSTRAINT PK_snapshots_financeiros PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  CONSENTIMENTOS_LGPD
-- ─────────────────────────────────────────────
CREATE TABLE consentimentos_lgpd (
    id              BIGINT        IDENTITY(1,1) NOT NULL,
    consentido      BIT           NOT NULL,
    criado_em       DATETIME2(6)  NOT NULL,
    ip_origem       VARCHAR(45),
    revogado_em     DATETIME2(6),
    revogado_motivo NVARCHAR(MAX),
    tipo            VARCHAR(50)   NOT NULL
                        CONSTRAINT CK_lgpd_tipo
                        CHECK (tipo IN (
                            'tratamento_dados_financeiros','uso_ia',
                            'armazenamento_extrato','bot_whatsapp','bot_telegram'
                        )),
    usuario_id      BIGINT        NOT NULL,
    versao_politica VARCHAR(20)   NOT NULL,
    CONSTRAINT PK_consentimentos_lgpd PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  NOTIFICACOES
-- ─────────────────────────────────────────────
CREATE TABLE notificacoes (
    id         BIGINT        IDENTITY(1,1) NOT NULL,
    canal      VARCHAR(20)   NOT NULL
                   CONSTRAINT CK_notif_canal
                   CHECK (canal IN ('whatsapp','telegram','email')),
    criado_em  DATETIME2(6)  NOT NULL,
    enviada    BIT           NOT NULL,
    enviada_em DATETIME2(6),
    erro       NVARCHAR(MAX),
    mensagem   NVARCHAR(MAX),
    tentativas SMALLINT      NOT NULL,
    tipo       VARCHAR(50)   NOT NULL,
    titulo     VARCHAR(255),
    usuario_id BIGINT        NOT NULL,
    CONSTRAINT PK_notificacoes PRIMARY KEY (id)
);

-- ─────────────────────────────────────────────
--  AUDITORIA_EVENTOS
-- ─────────────────────────────────────────────
CREATE TABLE auditoria_eventos (
    id               BIGINT        IDENTITY(1,1) NOT NULL,
    acao             VARCHAR(30)   NOT NULL
                         CONSTRAINT CK_audit_acao
                         CHECK (acao IN (
                             'CREATE','UPDATE','DELETE','LOGIN','LOGOUT','UPLOAD',
                             'CLASSIFY','CONFIRM','REJECT','EXPORT','REPROCESS',
                             'API_KEY_GEN','PASSWORD_CHANGE','CANCEL'
                         )),
    correlation_id   BIGINT        NOT NULL,
    criado_em        DATETIME2(6)  NOT NULL,
    dados_anteriores NVARCHAR(MAX),
    dados_novos      NVARCHAR(MAX),
    entidade         VARCHAR(50)   NOT NULL,
    entidade_id      BIGINT        NOT NULL,
    ip_origem        VARCHAR(45),
    origem           VARCHAR(20)
                         CONSTRAINT CK_audit_origem
                         CHECK (origem IN ('web','bot_whatsapp','bot_telegram','api','sistema')),
    user_agent       NVARCHAR(MAX),
    usuario_id       BIGINT,
    CONSTRAINT PK_auditoria_eventos PRIMARY KEY (id)
);


-- ═══════════════════════════════════════════════════════════════
--  UNIQUE CONSTRAINTS
-- ═══════════════════════════════════════════════════════════════
ALTER TABLE banco                ADD CONSTRAINT UKn0l94yd6ajyneu3i162sf5vsu UNIQUE (banco_code, banco_id);
ALTER TABLE banco                ADD CONSTRAINT UK8ys89xdt6jo2d2x3d8ahhq6x5 UNIQUE (banco_code);
ALTER TABLE categoria_thresholds ADD CONSTRAINT UKhof89v3ea15cfy69fhfvgv6op UNIQUE (categoria_id);
ALTER TABLE categorias           ADD CONSTRAINT UKk73kpeiqdy6n11qojlj39hose UNIQUE (categorias_code);
ALTER TABLE contas_financeiras   ADD CONSTRAINT UKq6yk4e2ao66xqitn12j3hbt19 UNIQUE (contas_code);
ALTER TABLE extratos             ADD CONSTRAINT UKayk58p7exmctcn9m14kdh1xp1 UNIQUE (extratos_code);
ALTER TABLE motivos_cancelamento ADD CONSTRAINT UK77wlysvh4n8d5hhe8we8v0kv1 UNIQUE (motivos_cancelamento_code);
ALTER TABLE transacoes           ADD CONSTRAINT UK67f5gtn438e7ykddm9ydssp99 UNIQUE (transacoes_code);
ALTER TABLE usuarios             ADD CONSTRAINT UK2et2smpfrtsohr7w9fe1v8a5e UNIQUE (cpf);
ALTER TABLE usuarios             ADD CONSTRAINT UKkfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);
ALTER TABLE usuarios             ADD CONSTRAINT UKh8plsiwilrerynftorecsi633 UNIQUE (usuario_code);


-- ═══════════════════════════════════════════════════════════════
--  FOREIGN KEYS  (nomes e referências extraídos do log Hibernate)
-- ═══════════════════════════════════════════════════════════════

ALTER TABLE auditoria_eventos
    ADD CONSTRAINT fk_auditoria_eventos_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE categoria_thresholds
    ADD CONSTRAINT fk_categoria_thresholds_categoria
    FOREIGN KEY (categoria_id) REFERENCES categorias (id);

ALTER TABLE consentimentos_lgpd
    ADD CONSTRAINT fk_consentimentos_lgpd_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

-- FK composta: conta → banco via (banco_code, banco_id)
ALTER TABLE contas_financeiras
    ADD CONSTRAINT fk_contas_financeiras_banco
    FOREIGN KEY (banco_code, banco_id) REFERENCES banco (banco_code, banco_id);

ALTER TABLE contas_financeiras
    ADD CONSTRAINT fk_contas_financeiras_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_conta
    FOREIGN KEY (conta_id) REFERENCES contas_financeiras (id);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_parser_versao
    FOREIGN KEY (parser_versao_id) REFERENCES parser_versoes (id);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_transacao
    FOREIGN KEY (transacao_id) REFERENCES transacoes (id);

ALTER TABLE extratos
    ADD CONSTRAINT fk_extratos_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE notificacoes
    ADD CONSTRAINT fk_notificacoes_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE processamento_jobs
    ADD CONSTRAINT fk_processamento_jobs_extrato
    FOREIGN KEY (extrato_id) REFERENCES extratos (id);

ALTER TABLE snapshots_financeiros
    ADD CONSTRAINT fk_snapshots_financeiros_conta
    FOREIGN KEY (conta_id) REFERENCES contas_financeiras (id);

ALTER TABLE snapshots_financeiros
    ADD CONSTRAINT fk_snapshots_financeiros_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE transacoes
    ADD CONSTRAINT fk_transacoes_categoria
    FOREIGN KEY (categoria_id) REFERENCES categorias (id);

ALTER TABLE transacoes
    ADD CONSTRAINT fk_transacoes_conta
    FOREIGN KEY (conta_id) REFERENCES contas_financeiras (id);

ALTER TABLE transacoes
    ADD CONSTRAINT fk_transacoes_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario);

ALTER TABLE transacoes_canceladas
    ADD CONSTRAINT fk_transacoes_canceladas_motivo
    FOREIGN KEY (motivo_id) REFERENCES motivos_cancelamento (id);

ALTER TABLE transacoes_canceladas
    ADD CONSTRAINT fk_transacoes_canceladas_transacao
    FOREIGN KEY (transacao_id) REFERENCES transacoes (id);