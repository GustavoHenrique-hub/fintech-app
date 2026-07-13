-- ============================================================
--  FINAPP — Carga inicial (seed)
--  Compatível com fintechapp_schema.sql (revisão 2026-07-13)
--  PostgreSQL 14+
--  ATENÇÃO: Hibernate não gera DEFAULT nas colunas criado_em nas
--  entidades que não declaram valor padrão em Java; os timestamps
--  NOT NULL são informados explicitamente abaixo.
--
--  Mudanças 2026-07-13 (refactor id/code em todas as entidades):
--  - categorias:            id/code renomeados p/ categoria_id / categoria_code
--  - motivos_cancelamento:  id/code renomeados p/ motivo_id / motivo_code
--  - extratos:              id/code renomeados p/ extrato_id / extrato_code;
--                            agora carrega transacao_code e parser_versao_code
--  - categoria_thresholds:  ganhou code próprio + categoria_code (FK composta)
--  - transacoes_canceladas: ganhou code próprio + transacao_code/motivo_code
--                            (FK composta); colunas conta_id/usuario_id
--                            REMOVIDAS (não existem na entidade atual)
--  - parser_versoes:        ganhou code próprio
--  - processamento_jobs:    ganhou code próprio + extrato_code (FK composta)
--  - consentimentos_lgpd:   ganhou code próprio + usuario_code (FK composta)
--  - notificacoes:          ganhou code próprio + usuario_code (FK composta)
--  - snapshots_financeiros: ganhou code próprio + usuario_code/conta_code
--  - auditoria_eventos:     ganhou code próprio + usuario_code (FK composta)
--  - sessao_tokens:         tabela nova (SessaoTokenEntity) — sem seed,
--                            tokens são gerados em runtime no login
--  - transacoes:             já tinha transacoes_code; ganhou categoria_code
--                            (FK composta p/ categorias)
--  - contas_financeiras / banco / usuarios: sem mudança nesta revisão
--    (já usavam o padrão id/code correto)
-- ============================================================

DO $$
DECLARE
  v_now         TIMESTAMPTZ := NOW();

  -- Bancos
  v_banco_nub   BIGINT;  v_bnub_code   VARCHAR(6) := 'NUB001';
  v_banco_ita   BIGINT;  v_bita_code   VARCHAR(6) := 'ITA002';
  v_banco_bra   BIGINT;  v_bbra_code   VARCHAR(6) := 'BRA003';

  -- Usuários
  v_usr_id      BIGINT;  v_usr_code    VARCHAR(6) := 'USR001';
  v_usr2_id     BIGINT;  v_usr2_code   VARCHAR(6) := 'USR002';

  -- Contas
  v_conta_nub   BIGINT;
  v_conta_ita   BIGINT;
  v_conta_car   BIGINT;
  v_conta_ana   BIGINT;

  -- Códigos das contas (para FK composta em transacoes/extratos/snapshots)
  v_conta_nub_code  VARCHAR(6) := 'NUB001';
  v_conta_ita_code  VARCHAR(6) := 'ITA002';
  v_conta_car_code  VARCHAR(6) := 'CAR003';
  v_conta_ana_code  VARCHAR(6) := 'BRA004';

  -- Categorias
  v_cat_sal     BIGINT;  v_cat_sal_code VARCHAR(6) := 'SAL001';
  v_cat_fre     BIGINT;  v_cat_fre_code VARCHAR(6) := 'FRE002';
  v_cat_ali     BIGINT;  v_cat_ali_code VARCHAR(6) := 'ALI010';
  v_cat_tra     BIGINT;  v_cat_tra_code VARCHAR(6) := 'TRA011';
  v_cat_mor     BIGINT;  v_cat_mor_code VARCHAR(6) := 'MOR012';
  v_cat_sau     BIGINT;  v_cat_sau_code VARCHAR(6) := 'SAU013';
  v_cat_str     BIGINT;  v_cat_str_code VARCHAR(6) := 'STR019';
  v_cat_out     BIGINT;  v_cat_out_code VARCHAR(6) := 'OUT020';
  v_cat_sup     BIGINT;  v_cat_sup_code VARCHAR(6) := 'SUP101';
  v_cat_del     BIGINT;  v_cat_del_code VARCHAR(6) := 'DEL103';
  v_cat_lan     BIGINT;  v_cat_lan_code VARCHAR(6) := 'LAN105';
  v_cat_ube     BIGINT;  v_cat_ube_code VARCHAR(6) := 'UBE112';
  v_cat_ene     BIGINT;  v_cat_ene_code VARCHAR(6) := 'ENE123';
  v_cat_far     BIGINT;  v_cat_far_code VARCHAR(6) := 'FAR132';
  v_cat_net     BIGINT;  v_cat_net_code VARCHAR(6) := 'NET191';

  -- Parser / Extrato
  v_parser_nub       BIGINT;  v_parser_nub_code VARCHAR(6) := 'PRV001';
  v_extrato_id       BIGINT;  v_extrato_code    VARCHAR(6) := 'EXT001';

  -- Transações
  v_tx_sup      BIGINT;
  v_tx_del      BIGINT;
  v_tx_ube      BIGINT;
  v_tx_net      BIGINT;
  v_tx_far      BIGINT;
  v_tx_amz      BIGINT;  v_tx_amz_code VARCHAR(6) := 'TX0008';
  v_tx_sal      BIGINT;  v_tx_sal_code VARCHAR(6) := 'TX0009';

  -- Cancelamento
  v_mot_usr     BIGINT;  v_mot_usr_code VARCHAR(6) := 'MOT005';

  v_corr        BIGINT := 2001;

BEGIN

-- ════════════════════════════════════════════════════════════
--  1. BANCO  (catálogo de instituições)
-- ════════════════════════════════════════════════════════════
INSERT INTO banco (banco_code, nome, cor_hex, icone, descricao) VALUES
  ('NUB001', 'Nubank',    '#820AD1', '💜', 'Banco digital Nubank')
  RETURNING banco_id INTO v_banco_nub;

INSERT INTO banco (banco_code, nome, cor_hex, icone, descricao) VALUES
  ('ITA002', 'Itaú',      '#EC7000', '🧡', 'Itaú Unibanco')
  RETURNING banco_id INTO v_banco_ita;

INSERT INTO banco (banco_code, nome, cor_hex, icone, descricao) VALUES
  ('BRA003', 'Bradesco',  '#CC0000', '❤️',  'Banco Bradesco')
  RETURNING banco_id INTO v_banco_bra;

INSERT INTO banco (banco_code, nome, cor_hex, icone, descricao) VALUES
  ('INT004', 'Inter',     '#FF6600', '🟠', 'Banco Inter'),
  ('CAI005', 'Caixa',     '#0070AF', '🔵', 'Caixa Econômica Federal'),
  ('SAN006', 'Santander', '#EC0000', '🔴', 'Banco Santander'),
  ('CAR007', 'Carteira',  '#64748B', '👛', 'Dinheiro físico / carteira');


-- ════════════════════════════════════════════════════════════
--  2. PARSER_VERSOES
-- ════════════════════════════════════════════════════════════
INSERT INTO parser_versoes (parser_versao_code, banco, versao, ativo, score_qualidade, total_usos, total_erros, descricao) VALUES
  ('PRV001', 'Nubank',    'v1.0', TRUE, 0.980, 0, 0, 'Parser Nubank — extrato CSV/PDF'),
  ('PRV002', 'Itaú',      'v1.0', TRUE, 0.960, 0, 0, 'Parser Itaú — PDF padrão'),
  ('PRV003', 'Bradesco',  'v1.0', TRUE, 0.940, 0, 0, 'Parser Bradesco — PDF'),
  ('PRV004', 'Santander', 'v1.0', TRUE, 0.920, 0, 0, 'Parser Santander — PDF'),
  ('PRV005', 'Caixa',     'v1.0', TRUE, 0.900, 0, 0, 'Parser Caixa — PDF'),
  ('PRV006', 'Inter',     'v1.0', TRUE, 0.970, 0, 0, 'Parser Inter — CSV/PDF'),
  ('PRV007', 'Genérico',  'v1.0', TRUE, 0.700, 0, 0, 'Parser genérico de fallback');

SELECT parser_versao_id INTO v_parser_nub FROM parser_versoes WHERE parser_versao_code = v_parser_nub_code;


-- ════════════════════════════════════════════════════════════
--  3. USUARIOS
-- ════════════════════════════════════════════════════════════
INSERT INTO usuarios (cpf, dt_nascimento, email, nome, senha, usuario_code, email_verificado, telefone)
VALUES ('123.456.789-09', '1990-03-15', 'joao.silva@finapp.com.br', 'João Silva',
        '$2b$12$PlaceholderHashBcryptUser01', v_usr_code, TRUE, '(11) 98765-4321')
RETURNING usuario_id INTO v_usr_id;

INSERT INTO usuarios (cpf, dt_nascimento, email, nome, senha, usuario_code, email_verificado, telefone)
VALUES ('987.654.321-00', '1985-07-22', 'ana.costa@finapp.com.br', 'Ana Costa',
        '$2b$12$PlaceholderHashBcryptUser02', v_usr2_code, FALSE, '(21) 91234-5678')
RETURNING usuario_id INTO v_usr2_id;


-- ════════════════════════════════════════════════════════════
--  4. CATEGORIAS  (tipo MAIÚSCULO: RECEITA | GASTO | AMBOS)
-- ════════════════════════════════════════════════════════════
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_sal_code, 'Salário',     'RECEITA', '💰', '#16A34A', TRUE, v_now) RETURNING categoria_id INTO v_cat_sal;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_fre_code, 'Freelance',   'RECEITA', '💻', '#0EA5E9', TRUE, v_now) RETURNING categoria_id INTO v_cat_fre;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_ali_code, 'Alimentação', 'GASTO',   '🍽️', '#EF4444', TRUE, v_now) RETURNING categoria_id INTO v_cat_ali;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_tra_code, 'Transporte',  'GASTO',   '🚗', '#F97316', TRUE, v_now) RETURNING categoria_id INTO v_cat_tra;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_mor_code, 'Moradia',     'GASTO',   '🏠', '#FBBF24', TRUE, v_now) RETURNING categoria_id INTO v_cat_mor;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_sau_code, 'Saúde',       'GASTO',   '🏥', '#EC4899', TRUE, v_now) RETURNING categoria_id INTO v_cat_sau;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_str_code, 'Streaming',   'GASTO',   '🎬', '#DC2626', TRUE, v_now) RETURNING categoria_id INTO v_cat_str;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_out_code, 'Outros',      'AMBOS',   '📦', '#94A3B8', TRUE, v_now) RETURNING categoria_id INTO v_cat_out;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_sup_code, 'Supermercado','GASTO',   '🛒', '#EF4444', TRUE, v_now) RETURNING categoria_id INTO v_cat_sup;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_del_code, 'Delivery',    'GASTO',   '🛵', '#EF4444', TRUE, v_now) RETURNING categoria_id INTO v_cat_del;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_lan_code, 'Lanchonete',  'GASTO',   '🍔', '#EF4444', TRUE, v_now) RETURNING categoria_id INTO v_cat_lan;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_ube_code, 'Uber/99',     'GASTO',   '🚕', '#F97316', TRUE, v_now) RETURNING categoria_id INTO v_cat_ube;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_ene_code, 'Energia',     'GASTO',   '💡', '#FBBF24', TRUE, v_now) RETURNING categoria_id INTO v_cat_ene;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_far_code, 'Farmácia',    'GASTO',   '💊', '#EC4899', TRUE, v_now) RETURNING categoria_id INTO v_cat_far;
INSERT INTO categorias (categoria_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES (v_cat_net_code, 'Netflix',     'GASTO',   '🎬', '#DC2626', TRUE, v_now) RETURNING categoria_id INTO v_cat_net;


-- ════════════════════════════════════════════════════════════
--  5. CATEGORIA_THRESHOLDS  → FK composta (categoria_id, categoria_code)
-- ════════════════════════════════════════════════════════════
INSERT INTO categoria_thresholds (categoria_threshold_code, categoria_id, categoria_code, threshold_auto, threshold_alerta, ambiguidade_alta) VALUES
  ('THR001', v_cat_str, v_cat_str_code, 90,  50, FALSE),
  ('THR002', v_cat_del, v_cat_del_code, 70,  50, FALSE),
  ('THR003', v_cat_ali, v_cat_ali_code, 70,  50, FALSE),
  ('THR004', v_cat_out, v_cat_out_code, 100, 50, TRUE);


-- ════════════════════════════════════════════════════════════
--  6. CONTAS_FINANCEIRAS  → FK composta para usuarios e banco
-- ════════════════════════════════════════════════════════════
INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, conta_code, criado_em)
  VALUES (v_usr_id, v_usr_code, v_banco_nub, v_bnub_code, 'corrente', 2500.00, 2500.00, TRUE,  TRUE, 'N', v_conta_nub_code, v_now)
  RETURNING conta_id INTO v_conta_nub;

INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, conta_code, criado_em)
  VALUES (v_usr_id, v_usr_code, v_banco_ita, v_bita_code, 'corrente', 10000.00, 10000.00, FALSE, TRUE, 'N', v_conta_ita_code, v_now)
  RETURNING conta_id INTO v_conta_ita;

-- Carteira usa banco "Carteira" (sem banco real)
INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, conta_code, criado_em)
  VALUES (v_usr_id, v_usr_code,
          (SELECT banco_id FROM banco WHERE banco_code = 'CAR007'),
          'CAR007',
          'dinheiro', 300.00, 300.00, FALSE, TRUE, 'N', v_conta_car_code, v_now)
  RETURNING conta_id INTO v_conta_car;

INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, conta_code, criado_em)
  VALUES (v_usr2_id, v_usr2_code, v_banco_bra, v_bbra_code, 'corrente', 5000.00, 5000.00, TRUE, TRUE, 'N', v_conta_ana_code, v_now)
  RETURNING conta_id INTO v_conta_ana;


-- ════════════════════════════════════════════════════════════
--  7. CONSENTIMENTOS_LGPD  → FK composta (usuario_id, usuario_code)
-- ════════════════════════════════════════════════════════════
INSERT INTO consentimentos_lgpd (consentimento_lgpd_code, usuario_id, usuario_code, tipo, versao_politica, consentido, ip_origem, criado_em) VALUES
  ('LGP001', v_usr_id,  v_usr_code,  'tratamento_dados_financeiros', '1.0', TRUE,  '192.0.2.1', v_now),
  ('LGP002', v_usr_id,  v_usr_code,  'uso_ia',                       '1.0', TRUE,  '192.0.2.1', v_now),
  ('LGP003', v_usr_id,  v_usr_code,  'armazenamento_extrato',        '1.0', TRUE,  '192.0.2.1', v_now),
  ('LGP004', v_usr2_id, v_usr2_code, 'tratamento_dados_financeiros', '1.0', TRUE,  '192.0.2.2', v_now),
  ('LGP005', v_usr2_id, v_usr2_code, 'uso_ia',                       '1.0', FALSE, '192.0.2.2', v_now);


-- ════════════════════════════════════════════════════════════
--  8. MOTIVOS_CANCELAMENTO
-- ════════════════════════════════════════════════════════════
INSERT INTO motivos_cancelamento (motivo_code, descricao, origem_permitida, ativo, criado_em) VALUES
  ('MOT001', 'Lançamento duplicado',                   'todos',   TRUE, v_now),
  ('MOT002', 'Erro de valor informado',                'usuario', TRUE, v_now),
  ('MOT003', 'Transação não reconhecida pelo usuário', 'usuario', TRUE, v_now),
  ('MOT004', 'Estorno realizado pelo estabelecimento', 'sistema', TRUE, v_now),
  ('MOT005', 'Cancelado pelo usuário',                 'usuario', TRUE, v_now),
  ('MOT006', 'Cancelado automaticamente pelo sistema', 'sistema', TRUE, v_now),
  ('MOT007', 'Cancelado por revisão administrativa',   'admin',   TRUE, v_now),
  ('MOT008', 'Arquivo de extrato reprocessado',        'sistema', TRUE, v_now);

SELECT motivo_id INTO v_mot_usr FROM motivos_cancelamento WHERE motivo_code = v_mot_usr_code;


-- ════════════════════════════════════════════════════════════
--  9. TRANSACOES  → FK composta para contas_financeiras e categorias
--     Direção (RECEITA/GASTO) não é mais coluna própria: é derivada da
--     categoria (v_cat_*). Para categoria AMBOS (ex.: v_cat_out/"Outros"),
--     o sinal de valor desempata (negativo = gasto, positivo = receita).
--     status_revisao: 'EXTRAIDA' | 'PENDENTE_REVISAO' | 'CONFIRMADA' etc.
--     ind_estorno: 'S' = sim, 'N' = não
-- ════════════════════════════════════════════════════════════

-- Gastos (vindos de extrato → ficam em PENDENTE_REVISAO aguardando revisão do usuário)
INSERT INTO transacoes (
  conta_id, conta_code, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES
  (v_conta_nub, v_conta_nub_code, 'COMPRA 02/05 PAGUE MENOS SP', 'Pague Menos',
   347.89, '2025-05-02', v_now,
   v_cat_sup, v_cat_sup_code, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0001', 1),

  (v_conta_nub, v_conta_nub_code, 'COMPRA 04/05 IFOOD*RESTAURANTE SP', 'iFood',
   58.90, '2025-05-04', v_now,
   v_cat_del, v_cat_del_code, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0002', 1),

  (v_conta_nub, v_conta_nub_code, 'COMPRA 05/05 UBER *TRIP SP', 'Uber',
   34.70, '2025-05-05', v_now,
   v_cat_ube, v_cat_ube_code, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0003', 1),

  (v_conta_nub, v_conta_nub_code, 'COMPRA 07/05 NETFLIX.COM SP', 'Netflix',
   55.90, '2025-05-07', v_now,
   v_cat_net, v_cat_net_code, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0004', 1),

  (v_conta_nub, v_conta_nub_code, 'COMPRA 10/05 DROGA RAIA SP', 'Droga Raia',
   89.50, '2025-05-10', v_now,
   v_cat_far, v_cat_far_code, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0005', 1);

-- Gastos manuais (usuário lançou → já confirmados)
INSERT INTO transacoes (
  conta_id, conta_code, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES
  (v_conta_ita, v_conta_ita_code, 'ENEL SP ENERGIA ELETRICA', 'ENEL SP',
   180.00, '2025-05-12', v_now,
   v_cat_ene, v_cat_ene_code, 'manual', 'CONFIRMADA', 'N', 'TX0006', 1),

  (v_conta_car, v_conta_car_code, 'LANCHONETE ESQUINA', 'Lanchonete Esquina',
   22.00, '2025-05-22', v_now,
   v_cat_sup, v_cat_sup_code, 'manual', 'CONFIRMADA', 'N', 'TX0007', 1);

-- Transação que será cancelada (categoria "Outros" = AMBOS; valor negativo = gasto)
INSERT INTO transacoes (
  conta_id, conta_code, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, confianca_ia, ind_estorno,
  transacoes_code, versao
) VALUES (
  v_conta_nub, v_conta_nub_code, 'COMPRA 20/05 AMAZON MKTPLC SP', 'Amazon',
  -215.00, '2025-05-20', v_now,
  v_cat_out, v_cat_out_code, 'pdf', 'PENDENTE_REVISAO', 45, 'N',
  v_tx_amz_code, 1
) RETURNING transacoes_id INTO v_tx_amz;

-- Receitas do João
INSERT INTO transacoes (
  conta_id, conta_code, descricao,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  recorrente, periodo_recorrencia, transacoes_code, versao
) VALUES (
  v_conta_ita, v_conta_ita_code, 'Salário Maio/2025',
  6800.00, '2025-05-05', v_now,
  v_cat_sal, v_cat_sal_code, 'manual', 'CONFIRMADA', 'N',
  TRUE, '2025-06-05', v_tx_sal_code, 1
) RETURNING transacoes_id INTO v_tx_sal;

INSERT INTO transacoes (
  conta_id, conta_code, descricao,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  recorrente, transacoes_code, versao
) VALUES (
  v_conta_nub, v_conta_nub_code, 'Projeto Web — Cliente ABC',
  1500.00, '2025-05-15', v_now,
  v_cat_fre, v_cat_fre_code, 'manual', 'CONFIRMADA', 'N',
  FALSE, 'TX0010', 1
);

-- Transações da Ana
INSERT INTO transacoes (
  conta_id, conta_code, descricao,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  recorrente, periodo_recorrencia, transacoes_code, versao
) VALUES (
  v_conta_ana, v_conta_ana_code, 'Salário Maio/2025',
  4500.00, '2025-05-05', v_now,
  v_cat_sal, v_cat_sal_code, 'manual', 'CONFIRMADA', 'N',
  TRUE, '2025-06-05', 'TX0011', 1
);

INSERT INTO transacoes (
  conta_id, conta_code, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, categoria_code, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES (
  v_conta_ana, v_conta_ana_code, 'SUPERMERCADO EXTRA SP', 'Extra',
  290.50, '2025-05-08', v_now,
  v_cat_sup, v_cat_sup_code, 'manual', 'CONFIRMADA', 'N',
  'TX0012', 1
);


-- ════════════════════════════════════════════════════════════
--  10. EXTRATOS  (PDF enviado → gera transações pendentes)
--      → FK composta para usuarios, contas_financeiras e parser_versoes
--      transacao_id/transacao_code ficam NULL: o extrato não está
--      vinculado a uma única transação (relação 1 extrato : N transações)
-- ════════════════════════════════════════════════════════════
INSERT INTO extratos (
  usuario_id, usuario_code, conta_id, conta_code,
  arquivo_nome, arquivo_uuid, hash_arquivo,
  banco_detectado, parser_versao_id, parser_versao_code, extrato_code,
  score_extracao, periodo_inicio, periodo_fim, status,
  total_lancamentos, lancamentos_confirmados, lancamentos_pendentes, lancamentos_ignorados,
  ind_delete, versao, criado_em
) VALUES (
  v_usr_id, v_usr_code, v_conta_nub, v_conta_nub_code,
  'nubank_maio_2025.pdf',
  'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
  encode(sha256('nubank_maio_2025'::bytea), 'hex'),
  'Nubank', v_parser_nub, v_parser_nub_code, v_extrato_code,
  0.980, '2025-05-01', '2025-05-31', 'pendente_revisao',
  6, 0, 6, 0,
  'N', 1, v_now
) RETURNING extrato_id INTO v_extrato_id;


-- ════════════════════════════════════════════════════════════
--  11. CANCELAMENTO DA TRANSAÇÃO AMAZON
--      transacoes_canceladas → FK composta para transacoes e motivos_cancelamento
-- ════════════════════════════════════════════════════════════
UPDATE transacoes
   SET deleted_at     = v_now,
       status_revisao = 'ARQUIVADA',
       atualizado_em  = v_now
 WHERE transacoes_id = v_tx_amz;

INSERT INTO transacoes_canceladas (
  transacao_cancelada_code, transacao_id, transacao_code, motivo_id, motivo_code, cancelado_por,
  valor_original, observacao, ip_origem, cancelado_em
) VALUES (
  'TXC001', v_tx_amz, v_tx_amz_code, v_mot_usr, v_mot_usr_code, 'usuario',
  215.00, 'Compra não reconhecida — possível fraude',
  '192.0.2.1', v_now
);


-- ════════════════════════════════════════════════════════════
--  12. SNAPSHOTS_FINANCEIROS  → FK composta para usuarios e
--      contas_financeiras (conta_id/conta_code NULL = consolidado do usuário)
-- ════════════════════════════════════════════════════════════
INSERT INTO snapshots_financeiros
  (snapshot_financeiro_code, usuario_id, usuario_code, conta_id, conta_code, ano, mes, saldo_inicial, total_receitas, total_gastos, saldo_final, fechado)
VALUES
  -- João — Nubank (Amazon excluída — cancelada)
  ('SNP001', v_usr_id, v_usr_code, v_conta_nub, v_conta_nub_code, 2025, 5,
   2500.00, 1500.00,
   (347.89 + 58.90 + 34.70 + 55.90 + 89.50),
   2500.00 + 1500.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50),
   FALSE),
  -- João — Itaú
  ('SNP002', v_usr_id, v_usr_code, v_conta_ita, v_conta_ita_code, 2025, 5,
   10000.00, 6800.00, 180.00, 10000.00 + 6800.00 - 180.00, FALSE),
  -- João — Carteira
  ('SNP003', v_usr_id, v_usr_code, v_conta_car, v_conta_car_code, 2025, 5,
   300.00, 0.00, 22.00, 300.00 - 22.00, FALSE),
  -- João — Consolidado (sem conta específica)
  ('SNP004', v_usr_id, v_usr_code, NULL, NULL, 2025, 5,
   12800.00, 8300.00,
   (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 22.00),
   12800.00 + 8300.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 22.00),
   FALSE),
  -- Ana — Bradesco
  ('SNP005', v_usr2_id, v_usr2_code, v_conta_ana, v_conta_ana_code, 2025, 5,
   5000.00, 4500.00, 290.50, 5000.00 + 4500.00 - 290.50, FALSE),
  -- Ana — Consolidado
  ('SNP006', v_usr2_id, v_usr2_code, NULL, NULL, 2025, 5,
   5000.00, 4500.00, 290.50, 5000.00 + 4500.00 - 290.50, FALSE);


-- ════════════════════════════════════════════════════════════
--  13. PROCESSAMENTO_JOBS  → FK composta para extratos
-- ════════════════════════════════════════════════════════════
INSERT INTO processamento_jobs (
  processamento_job_code, extrato_id, extrato_code, tipo, status, tentativas, max_tentativas,
  payload, correlation_id, enfileirado_em, iniciado_em, concluido_em
) VALUES
  ('JOB001', v_extrato_id, v_extrato_code, 'extracao_pdf', 'concluido', 1, 3,
   '{"arquivo":"nubank_maio_2025.pdf","banco":"Nubank"}',
   v_corr,
   v_now - INTERVAL '2 hours',
   v_now - INTERVAL '1 hour 59 minutes',
   v_now - INTERVAL '1 hour 55 minutes'),

  ('JOB002', v_extrato_id, v_extrato_code, 'classificacao_ia', 'concluido', 1, 3,
   '{"transacoes_pendentes":6}',
   v_corr,
   v_now - INTERVAL '1 hour 54 minutes',
   v_now - INTERVAL '1 hour 53 minutes',
   v_now - INTERVAL '1 hour 45 minutes');

-- Jobs sem extrato associado (snapshot mensal / notificação avulsa) —
-- extrato_id é NOT NULL na entidade atual, então reaproveitam o extrato
-- de exemplo acima em vez de NULL (ajuste os dados se seu fluxo real
-- gerar extrato próprio para jobs de snapshot/notificação).
INSERT INTO processamento_jobs (
  processamento_job_code, extrato_id, extrato_code, tipo, status, tentativas, max_tentativas,
  payload, correlation_id, enfileirado_em, iniciado_em, concluido_em
) VALUES
  ('JOB003', v_extrato_id, v_extrato_code, 'snapshot', 'concluido', 1, 3,
   '{"ano":2025,"mes":5}',
   v_corr,
   v_now - INTERVAL '30 minutes',
   v_now - INTERVAL '29 minutes 50 seconds',
   v_now - INTERVAL '29 minutes'),

  ('JOB004', v_extrato_id, v_extrato_code, 'notificacao', 'enfileirado', 0, 3,
   '{"tipo":"verificacao_email","usuario_id":2}',
   NULL, v_now, NULL, NULL);


-- ════════════════════════════════════════════════════════════
--  14. NOTIFICACOES  → FK composta para usuarios
-- ════════════════════════════════════════════════════════════
INSERT INTO notificacoes (notificacao_code, usuario_id, usuario_code, canal, tipo, titulo, mensagem, enviada, enviada_em, tentativas, criado_em) VALUES
  ('NOT001', v_usr_id,  v_usr_code,  'email', 'boas_vindas',
   'Bem-vindo ao FinApp! 🎉',
   'Olá João, sua conta foi criada com sucesso. Importe seu primeiro extrato!',
   TRUE, v_now - INTERVAL '2 days', 1, v_now - INTERVAL '2 days'),

  ('NOT002', v_usr_id,  v_usr_code,  'email', 'extrato_processado',
   'Extrato processado ✅',
   '6 transações do seu extrato Nubank estão aguardando sua revisão.',
   TRUE, v_now - INTERVAL '1 hour 44 minutes', 1, v_now - INTERVAL '1 hour 44 minutes'),

  ('NOT003', v_usr_id,  v_usr_code,  'email', 'cancelamento_transacao',
   'Transação cancelada',
   'A transação AMAZON MKTPLC SP de R$ 215,00 foi cancelada.',
   TRUE, v_now - INTERVAL '10 minutes', 1, v_now - INTERVAL '10 minutes'),

  ('NOT004', v_usr2_id, v_usr2_code, 'email', 'boas_vindas',
   'Bem-vindo ao FinApp! 🎉',
   'Olá Ana, sua conta foi criada. Verifique seu e-mail para ativar o acesso.',
   TRUE, v_now - INTERVAL '1 day', 1, v_now - INTERVAL '1 day'),

  ('NOT005', v_usr2_id, v_usr2_code, 'email', 'verificacao_email',
   'Confirme seu e-mail',
   'Clique no link para verificar sua conta no FinApp.',
   FALSE, NULL, 0, v_now);


-- ════════════════════════════════════════════════════════════
--  15. AUDITORIA_EVENTOS  → FK composta para usuarios (nullable)
-- ════════════════════════════════════════════════════════════
INSERT INTO auditoria_eventos (
  auditoria_evento_code, correlation_id, usuario_id, usuario_code, entidade, entidade_id,
  acao, dados_novos, ip_origem, origem, criado_em
) VALUES
  ('AUD001', v_corr,     v_usr_id,  v_usr_code,  'usuario',  v_usr_id,
   'CREATE', '{"nome":"João Silva","email":"joao.silva@finapp.com.br"}',
   '192.0.2.1', 'web', v_now),

  ('AUD002', v_corr + 1, v_usr2_id, v_usr2_code, 'usuario',  v_usr2_id,
   'CREATE', '{"nome":"Ana Costa","email":"ana.costa@finapp.com.br"}',
   '192.0.2.2', 'web', v_now),

  ('AUD003', v_corr + 2, v_usr_id,  v_usr_code,  'extrato',  v_extrato_id,
   'UPLOAD', '{"arquivo":"nubank_maio_2025.pdf","banco":"Nubank","lancamentos":6}',
   '192.0.2.1', 'web', v_now),

  ('AUD004', v_corr + 3, v_usr_id,  v_usr_code,  'usuario',  v_usr_id,
   'LOGIN', '{"canal":"web"}',
   '192.0.2.1', 'web', v_now),

  ('AUD005', v_corr + 4, v_usr_id,  v_usr_code,  'transacao', v_tx_amz,
   'CANCEL',
   '{"status_anterior":"PENDENTE_REVISAO","status_novo":"ARQUIVADA","valor":215.00}',
   '192.0.2.1', 'web', v_now),

  ('AUD006', v_corr + 5, v_usr_id,  v_usr_code,  'transacao', v_tx_sal,
   'CONFIRM', '{"status_novo":"CONFIRMADA","valor":6800.00}',
   '192.0.2.1', 'web', v_now);

-- ════════════════════════════════════════════════════════════
--  16. SESSAO_TOKENS
--      Intencionalmente sem seed: tokens de sessão são gerados em
--      runtime no fluxo de login (ver AuthController / SessaoToken).
-- ════════════════════════════════════════════════════════════

END $$;


-- ════════════════════════════════════════════════════════════
--  VERIFICAÇÃO
-- ════════════════════════════════════════════════════════════
/*
SELECT tabela, total FROM (
  SELECT 'banco'                   AS tabela, COUNT(*) AS total FROM banco
  UNION ALL SELECT 'usuarios',                COUNT(*) FROM usuarios
  UNION ALL SELECT 'parser_versoes',          COUNT(*) FROM parser_versoes
  UNION ALL SELECT 'categorias',              COUNT(*) FROM categorias
  UNION ALL SELECT 'categoria_thresholds',    COUNT(*) FROM categoria_thresholds
  UNION ALL SELECT 'contas_financeiras',      COUNT(*) FROM contas_financeiras
  UNION ALL SELECT 'consentimentos_lgpd',     COUNT(*) FROM consentimentos_lgpd
  UNION ALL SELECT 'motivos_cancelamento',    COUNT(*) FROM motivos_cancelamento
  UNION ALL SELECT 'transacoes (total)',      COUNT(*) FROM transacoes
  UNION ALL SELECT 'transacoes (ativas)',     COUNT(*) FROM transacoes WHERE deleted_at IS NULL
  UNION ALL SELECT 'transacoes_canceladas',   COUNT(*) FROM transacoes_canceladas
  UNION ALL SELECT 'extratos',                COUNT(*) FROM extratos
  UNION ALL SELECT 'processamento_jobs',      COUNT(*) FROM processamento_jobs
  UNION ALL SELECT 'snapshots_financeiros',   COUNT(*) FROM snapshots_financeiros
  UNION ALL SELECT 'notificacoes',            COUNT(*) FROM notificacoes
  UNION ALL SELECT 'sessao_tokens',           COUNT(*) FROM sessao_tokens
  UNION ALL SELECT 'auditoria_eventos',       COUNT(*) FROM auditoria_eventos
) t ORDER BY tabela;
*/
