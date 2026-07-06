-- ============================================================
--  FINAPP — Carga inicial (seed)
--  Compatível com o schema do log Hibernate: 2026-07-06
--  PostgreSQL 14+
--  ATENÇÃO: Hibernate não gera DEFAULT nas colunas criado_em
--  Todos os timestamps NOT NULL são informados explicitamente.
--
--  Mudanças 2026-07-06:
--  - transacoes: adicionado conta_code (FK composta conta_id + conta_code)
--  - contas_financeiras: adicionado usuario_code (NOT NULL) e saldo_atual
-- ============================================================

DO $$
DECLARE
  v_now         TIMESTAMPTZ := NOW();

  -- Bancos
  v_banco_nub   BIGINT;  v_bnub_code   VARCHAR(6) := 'NUB001';
  v_banco_ita   BIGINT;  v_bita_code   VARCHAR(6) := 'ITA002';
  v_banco_bra   BIGINT;  v_bbra_code   VARCHAR(6) := 'BRA003';

  -- Usuários
  v_usr_id      BIGINT;
  v_usr2_id     BIGINT;

  -- Contas
  v_conta_nub   BIGINT;
  v_conta_ita   BIGINT;
  v_conta_car   BIGINT;
  v_conta_ana   BIGINT;

  -- Categorias
  v_cat_sal     BIGINT;
  v_cat_fre     BIGINT;
  v_cat_ali     BIGINT;
  v_cat_tra     BIGINT;
  v_cat_mor     BIGINT;
  v_cat_sau     BIGINT;
  v_cat_str     BIGINT;
  v_cat_out     BIGINT;
  v_cat_sup     BIGINT;
  v_cat_del     BIGINT;
  v_cat_lan     BIGINT;
  v_cat_ube     BIGINT;
  v_cat_ene     BIGINT;
  v_cat_far     BIGINT;
  v_cat_net     BIGINT;

  -- Parser / Extrato
  v_parser_nub  BIGINT;
  v_extrato_id  BIGINT;

  -- Códigos das contas (para FK composta em transacoes)
  v_conta_nub_code  VARCHAR(6) := 'NUB001';
  v_conta_ita_code  VARCHAR(6) := 'ITA002';
  v_conta_car_code  VARCHAR(6) := 'CAR003';
  v_conta_ana_code  VARCHAR(6) := 'BRA004';

  -- Transações
  v_tx_sup      BIGINT;
  v_tx_del      BIGINT;
  v_tx_ube      BIGINT;
  v_tx_net      BIGINT;
  v_tx_far      BIGINT;
  v_tx_amz      BIGINT;
  v_tx_sal      BIGINT;

  -- Cancelamento
  v_mot_usr     BIGINT;

  v_corr        BIGINT := 2001;

BEGIN

-- ════════════════════════════════════════════════════════════
--  1. BANCO  (nova tabela — catálogo de instituições)
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
INSERT INTO parser_versoes (banco, versao, ativo, score_qualidade, total_usos, total_erros, descricao) VALUES
  ('Nubank',    'v1.0', TRUE,  0.980, 0, 0, 'Parser Nubank — extrato CSV/PDF'),
  ('Itaú',      'v1.0', TRUE,  0.960, 0, 0, 'Parser Itaú — PDF padrão'),
  ('Bradesco',  'v1.0', TRUE,  0.940, 0, 0, 'Parser Bradesco — PDF'),
  ('Santander', 'v1.0', TRUE,  0.920, 0, 0, 'Parser Santander — PDF'),
  ('Caixa',     'v1.0', TRUE,  0.900, 0, 0, 'Parser Caixa — PDF'),
  ('Inter',     'v1.0', TRUE,  0.970, 0, 0, 'Parser Inter — CSV/PDF'),
  ('Genérico',  'v1.0', TRUE,  0.700, 0, 0, 'Parser genérico de fallback');

SELECT id INTO v_parser_nub FROM parser_versoes WHERE banco = 'Nubank' AND versao = 'v1.0';


-- ════════════════════════════════════════════════════════════
--  3. USUARIOS
-- ════════════════════════════════════════════════════════════
INSERT INTO usuarios (cpf, dt_nascimento, email, nome, senha, usuario_code, email_verificado, telefone)
VALUES ('123.456.789-09', '1990-03-15', 'joao.silva@finapp.com.br', 'João Silva',
        '$2b$12$PlaceholderHashBcryptUser01', 'USR001', TRUE, '(11) 98765-4321')
RETURNING usuario_id INTO v_usr_id;

INSERT INTO usuarios (cpf, dt_nascimento, email, nome, senha, usuario_code, email_verificado, telefone)
VALUES ('987.654.321-00', '1985-07-22', 'ana.costa@finapp.com.br', 'Ana Costa',
        '$2b$12$PlaceholderHashBcryptUser02', 'USR002', FALSE, '(21) 91234-5678')
RETURNING usuario_id INTO v_usr2_id;


-- ════════════════════════════════════════════════════════════
--  4. CATEGORIAS  (tipo MAIÚSCULO: RECEITA | GASTO | AMBOS)
-- ════════════════════════════════════════════════════════════
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('SAL001', 'Salário',     'RECEITA', '💰', '#16A34A', TRUE, v_now) RETURNING id INTO v_cat_sal;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('FRE002', 'Freelance',   'RECEITA', '💻', '#0EA5E9', TRUE, v_now) RETURNING id INTO v_cat_fre;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('ALI010', 'Alimentação', 'GASTO',   '🍽️', '#EF4444', TRUE, v_now) RETURNING id INTO v_cat_ali;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('TRA011', 'Transporte',  'GASTO',   '🚗', '#F97316', TRUE, v_now) RETURNING id INTO v_cat_tra;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('MOR012', 'Moradia',     'GASTO',   '🏠', '#FBBF24', TRUE, v_now) RETURNING id INTO v_cat_mor;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('SAU013', 'Saúde',       'GASTO',   '🏥', '#EC4899', TRUE, v_now) RETURNING id INTO v_cat_sau;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('STR019', 'Streaming',   'GASTO',   '🎬', '#DC2626', TRUE, v_now) RETURNING id INTO v_cat_str;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('OUT020', 'Outros',      'AMBOS',   '📦', '#94A3B8', TRUE, v_now) RETURNING id INTO v_cat_out;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('SUP101', 'Supermercado','GASTO',   '🛒', '#EF4444', TRUE, v_now) RETURNING id INTO v_cat_sup;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('DEL103', 'Delivery',    'GASTO',   '🛵', '#EF4444', TRUE, v_now) RETURNING id INTO v_cat_del;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('LAN105', 'Lanchonete',  'GASTO',   '🍔', '#EF4444', TRUE, v_now) RETURNING id INTO v_cat_lan;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('UBE112', 'Uber/99',     'GASTO',   '🚕', '#F97316', TRUE, v_now) RETURNING id INTO v_cat_ube;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('ENE123', 'Energia',     'GASTO',   '💡', '#FBBF24', TRUE, v_now) RETURNING id INTO v_cat_ene;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('FAR132', 'Farmácia',    'GASTO',   '💊', '#EC4899', TRUE, v_now) RETURNING id INTO v_cat_far;
INSERT INTO categorias (categorias_code, nome, tipo, icone, cor_hex, padrao, criado_em)
  VALUES ('NET191', 'Netflix',     'GASTO',   '🎬', '#DC2626', TRUE, v_now) RETURNING id INTO v_cat_net;


-- ════════════════════════════════════════════════════════════
--  5. CATEGORIA_THRESHOLDS
-- ════════════════════════════════════════════════════════════
INSERT INTO categoria_thresholds (categoria_id, threshold_auto, threshold_alerta, ambiguidade_alta) VALUES
  (v_cat_str, 90,  50, FALSE),
  (v_cat_del, 70,  50, FALSE),
  (v_cat_ali, 70,  50, FALSE),
  (v_cat_out, 100, 50, TRUE);


-- ════════════════════════════════════════════════════════════
--  6. CONTAS_FINANCEIRAS
--     Agora referenciam banco via FK composta (banco_code, banco_id)
-- ════════════════════════════════════════════════════════════
INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, contas_code, criado_em)
  VALUES (v_usr_id, 'USR001', v_banco_nub, v_bnub_code, 'corrente', 2500.00, 2500.00, TRUE,  TRUE, 'N', 'NUB001', v_now)
  RETURNING id INTO v_conta_nub;

INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, contas_code, criado_em)
  VALUES (v_usr_id, 'USR001', v_banco_ita, v_bita_code, 'corrente', 10000.00, 10000.00, FALSE, TRUE, 'N', 'ITA002', v_now)
  RETURNING id INTO v_conta_ita;

-- Carteira usa banco "Carteira" (sem banco real)
INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, contas_code, criado_em)
  VALUES (v_usr_id, 'USR001',
          (SELECT banco_id FROM banco WHERE banco_code = 'CAR007'),
          'CAR007',
          'dinheiro', 300.00, 300.00, FALSE, TRUE, 'N', 'CAR003', v_now)
  RETURNING id INTO v_conta_car;

INSERT INTO contas_financeiras
  (usuario_id, usuario_code, banco_id, banco_code, tipo, saldo_inicial, saldo_atual, padrao, ativa, ind_delete, contas_code, criado_em)
  VALUES (v_usr2_id, 'USR002', v_banco_bra, v_bbra_code, 'corrente', 5000.00, 5000.00, TRUE, TRUE, 'N', 'BRA004', v_now)
  RETURNING id INTO v_conta_ana;


-- ════════════════════════════════════════════════════════════
--  7. CONSENTIMENTOS_LGPD
-- ════════════════════════════════════════════════════════════
INSERT INTO consentimentos_lgpd (usuario_id, tipo, versao_politica, consentido, ip_origem, criado_em) VALUES
  (v_usr_id,  'tratamento_dados_financeiros', '1.0', TRUE,  '192.0.2.1', v_now),
  (v_usr_id,  'uso_ia',                       '1.0', TRUE,  '192.0.2.1', v_now),
  (v_usr_id,  'armazenamento_extrato',        '1.0', TRUE,  '192.0.2.1', v_now),
  (v_usr2_id, 'tratamento_dados_financeiros', '1.0', TRUE,  '192.0.2.2', v_now),
  (v_usr2_id, 'uso_ia',                       '1.0', FALSE, '192.0.2.2', v_now);


-- ════════════════════════════════════════════════════════════
--  8. MOTIVOS_CANCELAMENTO
-- ════════════════════════════════════════════════════════════
INSERT INTO motivos_cancelamento (motivos_cancelamento_code, descricao, origem_permitida, ativo, criado_em) VALUES
  ('MOT001', 'Lançamento duplicado',                   'todos',   TRUE, v_now),
  ('MOT002', 'Erro de valor informado',                'usuario', TRUE, v_now),
  ('MOT003', 'Transação não reconhecida pelo usuário', 'usuario', TRUE, v_now),
  ('MOT004', 'Estorno realizado pelo estabelecimento', 'sistema', TRUE, v_now),
  ('MOT005', 'Cancelado pelo usuário',                 'usuario', TRUE, v_now),
  ('MOT006', 'Cancelado automaticamente pelo sistema', 'sistema', TRUE, v_now),
  ('MOT007', 'Cancelado por revisão administrativa',   'admin',   TRUE, v_now),
  ('MOT008', 'Arquivo de extrato reprocessado',        'sistema', TRUE, v_now);

SELECT id INTO v_mot_usr FROM motivos_cancelamento WHERE motivos_cancelamento_code = 'MOT005';


-- ════════════════════════════════════════════════════════════
--  9. TRANSACOES
--     tipo: 'RECEITA' | 'GASTO'
--     status_revisao: 'EXTRAIDA' | 'PENDENTE_REVISAO' | 'CONFIRMADA' etc.
--     ind_estorno: 'S' = sim, 'N' = não
--     descricao: campo único (unifica descricao_original/usuario)
-- ════════════════════════════════════════════════════════════

-- Gastos (vindos de extrato → ficam em PENDENTE_REVISAO aguardando revisão do usuário)
INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES
  (v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 02/05 PAGUE MENOS SP', 'Pague Menos',
   347.89, '2025-05-02', v_now,
   v_cat_sup, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0001', 1),

  (v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 04/05 IFOOD*RESTAURANTE SP', 'iFood',
   58.90, '2025-05-04', v_now,
   v_cat_del, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0002', 1),

  (v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 05/05 UBER *TRIP SP', 'Uber',
   34.70, '2025-05-05', v_now,
   v_cat_ube, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0003', 1),

  (v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 07/05 NETFLIX.COM SP', 'Netflix',
   55.90, '2025-05-07', v_now,
   v_cat_net, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0004', 1),

  (v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 10/05 DROGA RAIA SP', 'Droga Raia',
   89.50, '2025-05-10', v_now,
   v_cat_far, 'pdf', 'PENDENTE_REVISAO', 'N', 'TX0005', 1);

-- Gastos manuais (usuário lançou → já confirmados)
INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES
  (v_conta_ita, v_conta_ita_code, 'GASTO', 'ENEL SP ENERGIA ELETRICA', 'ENEL SP',
   180.00, '2025-05-12', v_now,
   v_cat_ene, 'manual', 'CONFIRMADA', 'N', 'TX0006', 1),

  (v_conta_car, v_conta_car_code, 'GASTO', 'LANCHONETE ESQUINA', 'Lanchonete Esquina',
   22.00, '2025-05-22', v_now,
   v_cat_sup, 'manual', 'CONFIRMADA', 'N', 'TX0007', 1);

-- Transação que será cancelada
INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, confianca_ia, ind_estorno,
  transacoes_code, versao
) VALUES (
  v_conta_nub, v_conta_nub_code, 'GASTO', 'COMPRA 20/05 AMAZON MKTPLC SP', 'Amazon',
  215.00, '2025-05-20', v_now,
  v_cat_out, 'pdf', 'PENDENTE_REVISAO', 45, 'N',
  'TX0008', 1
) RETURNING id INTO v_tx_amz;

-- Receitas do João
INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  recorrente, periodo_recorrencia, transacoes_code, versao
) VALUES (
  v_conta_ita, v_conta_ita_code, 'RECEITA', 'Salário Maio/2025',
  6800.00, '2025-05-05', v_now,
  v_cat_sal, 'manual', 'CONFIRMADA', 'N',
  TRUE, '2025-06-05', 'TX0009', 1
) RETURNING id INTO v_tx_sal;

INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  recorrente, transacoes_code, versao
) VALUES (
  v_conta_nub, v_conta_nub_code, 'RECEITA', 'Projeto Web — Cliente ABC',
  1500.00, '2025-05-15', v_now,
  v_cat_fre, 'manual', 'CONFIRMADA', 'N',
  FALSE, 'TX0010', 1
);

-- Transações da Ana
INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  recorrente, periodo_recorrencia, transacoes_code, versao
) VALUES (
  v_conta_ana, v_conta_ana_code, 'RECEITA', 'Salário Maio/2025',
  4500.00, '2025-05-05', v_now,
  v_cat_sal, 'manual', 'CONFIRMADA', 'N',
  TRUE, '2025-06-05', 'TX0011', 1
);

INSERT INTO transacoes (
  conta_id, conta_code, tipo, descricao, estabelecimento,
  valor, data_transacao, criado_em,
  categoria_id, origem, status_revisao, ind_estorno,
  transacoes_code, versao
) VALUES (
  v_conta_ana, v_conta_ana_code, 'GASTO', 'SUPERMERCADO EXTRA SP', 'Extra',
  290.50, '2025-05-08', v_now,
  v_cat_sup, 'manual', 'CONFIRMADA', 'N',
  'TX0012', 1
);


-- ════════════════════════════════════════════════════════════
--  10. EXTRATOS  (PDF enviado → gera transações pendentes)
-- ════════════════════════════════════════════════════════════
INSERT INTO extratos (
  usuario_id, conta_id, arquivo_nome, arquivo_uuid, hash_arquivo,
  banco_detectado, parser_versao_id, extratos_code,
  score_extracao, periodo_inicio, periodo_fim, status,
  total_lancamentos, lancamentos_confirmados, lancamentos_pendentes, lancamentos_ignorados,
  ind_delete, versao, criado_em
) VALUES (
  v_usr_id, v_conta_nub,
  'nubank_maio_2025.pdf',
  'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
  encode(sha256('nubank_maio_2025'::bytea), 'hex'),
  'Nubank', v_parser_nub, 'EXT001',
  0.980, '2025-05-01', '2025-05-31', 'pendente_revisao',
  6, 0, 6, 0,
  'N', 1, v_now
) RETURNING id INTO v_extrato_id;


-- ════════════════════════════════════════════════════════════
--  11. CANCELAMENTO DA TRANSAÇÃO AMAZON
-- ════════════════════════════════════════════════════════════
UPDATE transacoes
   SET deleted_at     = v_now,
       status_revisao = 'ARQUIVADA',
       atualizado_em  = v_now
 WHERE id = v_tx_amz;

INSERT INTO transacoes_canceladas (
  transacao_id, motivo_id, cancelado_por,
  valor_original, observacao, ip_origem, cancelado_em
) VALUES (
  v_tx_amz, v_mot_usr, 'usuario',
  215.00, 'Compra não reconhecida — possível fraude',
  '192.0.2.1', v_now
);


-- ════════════════════════════════════════════════════════════
--  12. SNAPSHOTS_FINANCEIROS
-- ════════════════════════════════════════════════════════════
INSERT INTO snapshots_financeiros
  (usuario_id, conta_id, ano, mes, saldo_inicial, total_receitas, total_gastos, saldo_final, fechado)
VALUES
  -- João — Nubank (Amazon excluída — cancelada)
  (v_usr_id, v_conta_nub, 2025, 5,
   2500.00, 1500.00,
   (347.89 + 58.90 + 34.70 + 55.90 + 89.50),
   2500.00 + 1500.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50),
   FALSE),
  -- João — Itaú
  (v_usr_id, v_conta_ita, 2025, 5,
   10000.00, 6800.00, 180.00, 10000.00 + 6800.00 - 180.00, FALSE),
  -- João — Carteira
  (v_usr_id, v_conta_car, 2025, 5,
   300.00, 0.00, 22.00, 300.00 - 22.00, FALSE),
  -- João — Consolidado
  (v_usr_id, NULL, 2025, 5,
   12800.00, 8300.00,
   (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 22.00),
   12800.00 + 8300.00 - (347.89 + 58.90 + 34.70 + 55.90 + 89.50 + 180.00 + 22.00),
   FALSE),
  -- Ana — Bradesco
  (v_usr2_id, v_conta_ana, 2025, 5,
   5000.00, 4500.00, 290.50, 5000.00 + 4500.00 - 290.50, FALSE),
  -- Ana — Consolidado
  (v_usr2_id, NULL, 2025, 5,
   5000.00, 4500.00, 290.50, 5000.00 + 4500.00 - 290.50, FALSE);


-- ════════════════════════════════════════════════════════════
--  13. PROCESSAMENTO_JOBS
-- ════════════════════════════════════════════════════════════
INSERT INTO processamento_jobs (
  extrato_id, tipo, status, tentativas, max_tentativas,
  payload, correlation_id, enfileirado_em, iniciado_em, concluido_em
) VALUES
  (v_extrato_id, 'extracao_pdf', 'concluido', 1, 3,
   '{"arquivo":"nubank_maio_2025.pdf","banco":"Nubank"}',
   v_corr,
   v_now - INTERVAL '2 hours',
   v_now - INTERVAL '1 hour 59 minutes',
   v_now - INTERVAL '1 hour 55 minutes'),

  (v_extrato_id, 'classificacao_ia', 'concluido', 1, 3,
   '{"transacoes_pendentes":6}',
   v_corr,
   v_now - INTERVAL '1 hour 54 minutes',
   v_now - INTERVAL '1 hour 53 minutes',
   v_now - INTERVAL '1 hour 45 minutes'),

  (NULL, 'snapshot', 'concluido', 1, 3,
   '{"ano":2025,"mes":5}',
   v_corr,
   v_now - INTERVAL '30 minutes',
   v_now - INTERVAL '29 minutes 50 seconds',
   v_now - INTERVAL '29 minutes'),

  (NULL, 'notificacao', 'enfileirado', 0, 3,
   '{"tipo":"verificacao_email","usuario_id":2}',
   NULL, v_now, NULL, NULL);


-- ════════════════════════════════════════════════════════════
--  14. NOTIFICACOES
-- ════════════════════════════════════════════════════════════
INSERT INTO notificacoes (usuario_id, canal, tipo, titulo, mensagem, enviada, enviada_em, tentativas, criado_em) VALUES
  (v_usr_id,  'email', 'boas_vindas',
   'Bem-vindo ao FinApp! 🎉',
   'Olá João, sua conta foi criada com sucesso. Importe seu primeiro extrato!',
   TRUE, v_now - INTERVAL '2 days', 1, v_now - INTERVAL '2 days'),

  (v_usr_id,  'email', 'extrato_processado',
   'Extrato processado ✅',
   '6 transações do seu extrato Nubank estão aguardando sua revisão.',
   TRUE, v_now - INTERVAL '1 hour 44 minutes', 1, v_now - INTERVAL '1 hour 44 minutes'),

  (v_usr_id,  'email', 'cancelamento_transacao',
   'Transação cancelada',
   'A transação AMAZON MKTPLC SP de R$ 215,00 foi cancelada.',
   TRUE, v_now - INTERVAL '10 minutes', 1, v_now - INTERVAL '10 minutes'),

  (v_usr2_id, 'email', 'boas_vindas',
   'Bem-vindo ao FinApp! 🎉',
   'Olá Ana, sua conta foi criada. Verifique seu e-mail para ativar o acesso.',
   TRUE, v_now - INTERVAL '1 day', 1, v_now - INTERVAL '1 day'),

  (v_usr2_id, 'email', 'verificacao_email',
   'Confirme seu e-mail',
   'Clique no link para verificar sua conta no FinApp.',
   FALSE, NULL, 0, v_now);


-- ════════════════════════════════════════════════════════════
--  15. AUDITORIA_EVENTOS
-- ════════════════════════════════════════════════════════════
INSERT INTO auditoria_eventos (
  correlation_id, usuario_id, entidade, entidade_id,
  acao, dados_novos, ip_origem, origem, criado_em
) VALUES
  (v_corr,     v_usr_id,  'usuario',  v_usr_id,
   'CREATE', '{"nome":"João Silva","email":"joao.silva@finapp.com.br"}',
   '192.0.2.1', 'web', v_now),

  (v_corr + 1, v_usr2_id, 'usuario',  v_usr2_id,
   'CREATE', '{"nome":"Ana Costa","email":"ana.costa@finapp.com.br"}',
   '192.0.2.2', 'web', v_now),

  (v_corr + 2, v_usr_id,  'extrato',  v_extrato_id,
   'UPLOAD', '{"arquivo":"nubank_maio_2025.pdf","banco":"Nubank","lancamentos":6}',
   '192.0.2.1', 'web', v_now),

  (v_corr + 3, v_usr_id,  'usuario',  v_usr_id,
   'LOGIN', '{"canal":"web"}',
   '192.0.2.1', 'web', v_now),

  (v_corr + 4, v_usr_id,  'transacao', v_tx_amz,
   'CANCEL',
   '{"status_anterior":"PENDENTE_REVISAO","status_novo":"ARQUIVADA","valor":215.00}',
   '192.0.2.1', 'web', v_now),

  (v_corr + 5, v_usr_id,  'transacao', v_tx_sal,
   'CONFIRM', '{"status_novo":"CONFIRMADA","valor":6800.00}',
   '192.0.2.1', 'web', v_now);

END $$;


-- ════════════════════════════════════════════════════════════
--  VERIFICAÇÃO
-- ════════════════════════════════════════════════════════════
/*
SELECT tabela, total FROM (
  SELECT 'banco'                AS tabela, COUNT(*) AS total FROM banco
  UNION ALL SELECT 'usuarios',             COUNT(*) FROM usuarios
  UNION ALL SELECT 'parser_versoes',       COUNT(*) FROM parser_versoes
  UNION ALL SELECT 'categorias',           COUNT(*) FROM categorias
  UNION ALL SELECT 'categoria_thresholds', COUNT(*) FROM categoria_thresholds
  UNION ALL SELECT 'contas_financeiras',   COUNT(*) FROM contas_financeiras
  UNION ALL SELECT 'consentimentos_lgpd',  COUNT(*) FROM consentimentos_lgpd
  UNION ALL SELECT 'motivos_cancelamento', COUNT(*) FROM motivos_cancelamento
  UNION ALL SELECT 'transacoes (total)',    COUNT(*) FROM transacoes
  UNION ALL SELECT 'transacoes (ativas)',   COUNT(*) FROM transacoes WHERE deleted_at IS NULL
  UNION ALL SELECT 'transacoes_canceladas',COUNT(*) FROM transacoes_canceladas
  UNION ALL SELECT 'extratos',             COUNT(*) FROM extratos
  UNION ALL SELECT 'snapshots_financeiros',COUNT(*) FROM snapshots_financeiros
  UNION ALL SELECT 'processamento_jobs',   COUNT(*) FROM processamento_jobs
  UNION ALL SELECT 'notificacoes',         COUNT(*) FROM notificacoes
  UNION ALL SELECT 'auditoria_eventos',    COUNT(*) FROM auditoria_eventos
) t ORDER BY tabela;
*/