-- FINTECH - Comandos SQL (Oracle) - RM570791

-- ============================================================
-- CADASTRO
-- ============================================================

-- Cadastrar os dados de um novo usuário
INSERT INTO t_fin_usuarios (usuario_code, cpf, nome, email, senha, telefone, dt_nascimento)
VALUES ('[CÓDIGO DO USUÁRIO]', '[CPF]', '[NOME]', '[E-MAIL]', '[SENHA]', '[TELEFONE]',
        TO_DATE('[DATA DE NASCIMENTO]', 'DD/MM/YYYY'));

-- Cadastrar os dados da conta bancária de um usuário
INSERT INTO t_fin_contas_financeiras (conta_code, usuario_id, usuario_code, banco_id, banco_code,
                                      tipo, saldo_inicial, saldo_atual, padrao)
VALUES ('[CÓDIGO DA CONTA]', [ID DO USUÁRIO], '[CÓDIGO DO USUÁRIO]', [ID DO BANCO], '[CÓDIGO DO BANCO]',
        '[TIPO DA CONTA]', [SALDO INICIAL], [SALDO INICIAL], '[CONTA PADRÃO (S/N)]');

-- Cadastrar os dados de uma nova receita (entrada de dinheiro) para um usuário
INSERT INTO t_fin_transacoes (transacoes_code, conta_id, conta_code, categoria_id, categoria_code,
                              descricao, valor, data_transacao, estabelecimento, origem,
                              status_revisao, recorrente)
VALUES ('[CÓDIGO DA RECEITA]', [ID DA CONTA DO USUÁRIO], '[CÓDIGO DA CONTA DO USUÁRIO]',
        [ID DA CATEGORIA DE RECEITA], '[CÓDIGO DA CATEGORIA DE RECEITA]', '[DESCRIÇÃO DA RECEITA]',
        [VALOR DA RECEITA], TO_DATE('[DATA DA RECEITA]', 'DD/MM/YYYY'), '[FONTE PAGADORA]',
        'manual', 'CONFIRMADA', '[RECORRENTE (S/N)]');

-- Cadastrar os dados de uma nova despesa (gasto) de um usuário
INSERT INTO t_fin_transacoes (transacoes_code, conta_id, conta_code, categoria_id, categoria_code,
                              descricao, valor, data_transacao, estabelecimento, origem,
                              status_revisao, recorrente)
VALUES ('[CÓDIGO DA DESPESA]', [ID DA CONTA DO USUÁRIO], '[CÓDIGO DA CONTA DO USUÁRIO]',
        [ID DA CATEGORIA DE GASTO], '[CÓDIGO DA CATEGORIA DE GASTO]', '[DESCRIÇÃO DA DESPESA]',
        [VALOR DA DESPESA], TO_DATE('[DATA DA DESPESA]', 'DD/MM/YYYY'), '[ESTABELECIMENTO]',
        'manual', 'CONFIRMADA', '[RECORRENTE (S/N)]');

-- Cadastrar os dados de um novo investimento feito por um usuário
INSERT INTO t_fin_investimentos (investimento_code, usuario_id, usuario_code, conta_id, conta_code,
                                 nome, tipo, valor_aplicado, rentabilidade_aa, data_aplicacao,
                                 data_vencimento)
VALUES ('[CÓDIGO DO INVESTIMENTO]', [ID DO USUÁRIO], '[CÓDIGO DO USUÁRIO]', [ID DA CONTA],
        '[CÓDIGO DA CONTA]', '[NOME DO INVESTIMENTO]', '[TIPO DO INVESTIMENTO]', [VALOR APLICADO],
        [RENTABILIDADE AO ANO], TO_DATE('[DATA DA APLICAÇÃO]', 'DD/MM/YYYY'),
        TO_DATE('[DATA DE VENCIMENTO]', 'DD/MM/YYYY'));

-- ============================================================
-- ALTERAÇÃO
-- ============================================================

-- Alterar os dados de um usuário
UPDATE t_fin_usuarios
   SET nome          = '[NOME]',
       email         = '[E-MAIL]',
       telefone      = '[TELEFONE]',
       dt_nascimento = TO_DATE('[DATA DE NASCIMENTO]', 'DD/MM/YYYY')
 WHERE usuario_id = [ID DO USUÁRIO];

-- Alterar os dados de uma receita (código do usuário + código da receita)
UPDATE t_fin_transacoes t
   SET t.descricao       = '[DESCRIÇÃO DA RECEITA]',
       t.valor           = [VALOR DA RECEITA],
       t.data_transacao  = TO_DATE('[DATA DA RECEITA]', 'DD/MM/YYYY'),
       t.estabelecimento = '[FONTE PAGADORA]',
       t.recorrente      = '[RECORRENTE (S/N)]',
       t.versao          = t.versao + 1,
       t.atualizado_em   = SYSTIMESTAMP
 WHERE t.transacoes_id = [ID DA RECEITA]
   AND t.deleted_at IS NULL
   AND EXISTS (SELECT 1
                 FROM t_fin_contas_financeiras cf
                WHERE cf.conta_id   = t.conta_id
                  AND cf.conta_code = t.conta_code
                  AND cf.usuario_id = [ID DO USUÁRIO])
   AND EXISTS (SELECT 1
                 FROM t_fin_categorias c
                WHERE c.categoria_id   = t.categoria_id
                  AND c.categoria_code = t.categoria_code
                  AND (c.tipo = 'RECEITA' OR (c.tipo = 'AMBOS' AND t.valor > 0)));

-- Alterar os dados de uma despesa (código do usuário + código da despesa)
UPDATE t_fin_transacoes t
   SET t.descricao       = '[DESCRIÇÃO DA DESPESA]',
       t.valor           = [VALOR DA DESPESA],
       t.data_transacao  = TO_DATE('[DATA DA DESPESA]', 'DD/MM/YYYY'),
       t.estabelecimento = '[ESTABELECIMENTO]',
       t.recorrente      = '[RECORRENTE (S/N)]',
       t.versao          = t.versao + 1,
       t.atualizado_em   = SYSTIMESTAMP
 WHERE t.transacoes_id = [ID DA DESPESA]
   AND t.deleted_at IS NULL
   AND EXISTS (SELECT 1
                 FROM t_fin_contas_financeiras cf
                WHERE cf.conta_id   = t.conta_id
                  AND cf.conta_code = t.conta_code
                  AND cf.usuario_id = [ID DO USUÁRIO])
   AND EXISTS (SELECT 1
                 FROM t_fin_categorias c
                WHERE c.categoria_id   = t.categoria_id
                  AND c.categoria_code = t.categoria_code
                  AND (c.tipo = 'GASTO' OR (c.tipo = 'AMBOS' AND t.valor < 0)));

-- Alterar os dados de um investimento (código do usuário + código do investimento)
UPDATE t_fin_investimentos
   SET nome             = '[NOME DO INVESTIMENTO]',
       tipo             = '[TIPO DO INVESTIMENTO]',
       valor_aplicado   = [VALOR APLICADO],
       rentabilidade_aa = [RENTABILIDADE AO ANO],
       data_aplicacao   = TO_DATE('[DATA DA APLICAÇÃO]', 'DD/MM/YYYY'),
       data_vencimento  = TO_DATE('[DATA DE VENCIMENTO]', 'DD/MM/YYYY'),
       status           = '[STATUS]',
       atualizado_em    = SYSTIMESTAMP
 WHERE usuario_id      = [ID DO USUÁRIO]
   AND investimento_id = [ID DO INVESTIMENTO];

-- ============================================================
-- CONSULTAS SIMPLES
-- ============================================================

-- Consultar os dados de um usuário específico
SELECT usuario_id, usuario_code, cpf, nome, email, telefone,
       dt_nascimento, email_verificado
  FROM t_fin_usuarios
 WHERE usuario_id = [ID DO USUÁRIO];

-- Consultar os dados de uma despesa específica
SELECT t.transacoes_id, t.transacoes_code, t.descricao, t.valor, t.data_transacao,
       t.estabelecimento, t.recorrente, t.status_revisao, c.nome AS categoria,
       cf.conta_code, cf.tipo AS tipo_conta
  FROM t_fin_transacoes t
  JOIN t_fin_contas_financeiras cf
    ON cf.conta_id = t.conta_id AND cf.conta_code = t.conta_code
  JOIN t_fin_categorias c
    ON c.categoria_id = t.categoria_id AND c.categoria_code = t.categoria_code
 WHERE cf.usuario_id   = [ID DO USUÁRIO]
   AND t.transacoes_id = [ID DA DESPESA]
   AND t.deleted_at IS NULL
   AND (c.tipo = 'GASTO' OR (c.tipo = 'AMBOS' AND t.valor < 0));

-- Consultar os dados de um investimento específico
SELECT investimento_id, investimento_code, conta_id, conta_code, nome, tipo,
       valor_aplicado, rentabilidade_aa, data_aplicacao, data_vencimento, status
  FROM t_fin_investimentos
 WHERE usuario_id      = [ID DO USUÁRIO]
   AND investimento_id = [ID DO INVESTIMENTO];

-- ============================================================
-- CONSULTAS ORDENADAS
-- ============================================================

-- Consultar todas as despesas de um usuário, da mais recente à mais antiga
SELECT t.transacoes_id, t.transacoes_code, t.descricao, t.valor, t.data_transacao,
       t.estabelecimento, c.nome AS categoria, cf.conta_code
  FROM t_fin_transacoes t
  JOIN t_fin_contas_financeiras cf
    ON cf.conta_id = t.conta_id AND cf.conta_code = t.conta_code
  JOIN t_fin_categorias c
    ON c.categoria_id = t.categoria_id AND c.categoria_code = t.categoria_code
 WHERE cf.usuario_id = [ID DO USUÁRIO]
   AND t.deleted_at IS NULL
   AND (c.tipo = 'GASTO' OR (c.tipo = 'AMBOS' AND t.valor < 0))
 ORDER BY t.data_transacao DESC, t.transacoes_id DESC;

-- Consultar todos os investimentos de um usuário, do mais recente ao mais antigo
SELECT investimento_id, investimento_code, nome, tipo, valor_aplicado,
       rentabilidade_aa, data_aplicacao, data_vencimento, status
  FROM t_fin_investimentos
 WHERE usuario_id = [ID DO USUÁRIO]
 ORDER BY data_aplicacao DESC, investimento_id DESC;

-- ============================================================
-- CONSULTA PARA O DASHBOARD
-- ============================================================

-- Dados principais do usuário + última despesa + último investimento
SELECT u.usuario_id,
       u.usuario_code,
       u.nome,
       u.email,
       u.telefone,
       (SELECT SUM(cf.saldo_atual)
          FROM t_fin_contas_financeiras cf
         WHERE cf.usuario_id = u.usuario_id
           AND cf.ind_delete = 'N')  AS saldo_total,
       d.transacoes_code             AS despesa_code,
       d.descricao                   AS despesa_descricao,
       d.valor                       AS despesa_valor,
       d.data_transacao              AS despesa_data,
       d.categoria                   AS despesa_categoria,
       i.investimento_code,
       i.nome                        AS investimento_nome,
       i.tipo                        AS investimento_tipo,
       i.valor_aplicado              AS investimento_valor,
       i.data_aplicacao              AS investimento_data
  FROM t_fin_usuarios u
  LEFT JOIN (SELECT cf.usuario_id, t.transacoes_code, t.descricao, t.valor,
                    t.data_transacao, c.nome AS categoria,
                    ROW_NUMBER() OVER (PARTITION BY cf.usuario_id
                                       ORDER BY t.data_transacao DESC, t.transacoes_id DESC) AS rn
               FROM t_fin_transacoes t
               JOIN t_fin_contas_financeiras cf
                 ON cf.conta_id = t.conta_id AND cf.conta_code = t.conta_code
               JOIN t_fin_categorias c
                 ON c.categoria_id = t.categoria_id AND c.categoria_code = t.categoria_code
              WHERE t.deleted_at IS NULL
                AND (c.tipo = 'GASTO' OR (c.tipo = 'AMBOS' AND t.valor < 0))) d
         ON d.usuario_id = u.usuario_id AND d.rn = 1
  LEFT JOIN (SELECT inv.usuario_id, inv.investimento_code, inv.nome, inv.tipo,
                    inv.valor_aplicado, inv.data_aplicacao,
                    ROW_NUMBER() OVER (PARTITION BY inv.usuario_id
                                       ORDER BY inv.data_aplicacao DESC, inv.investimento_id DESC) AS rn
               FROM t_fin_investimentos inv) i
         ON i.usuario_id = u.usuario_id AND i.rn = 1
 WHERE u.usuario_id = [ID DO USUÁRIO];
