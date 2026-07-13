# Ideias de Regras de Negócio — Fintech App

Documento de ideação para regras de negócio do sistema de controle financeiro pessoal. Não trata de integração com dados monetários reais; foco em controle das próprias finanças do usuário.

Base analisada: domínios atuais (`Usuario`, `Banco`, `ContaFinanceira`, `Categoria`/`CategoriaThreshold`, `Transacao`, `TransacaoCancelada`, `Extrato`, `SnapshotFinanceiro`, `Notificacao`, `ConsentimentoLgpd`, `AuditoriaEvento`, `ParserVersao`, `ProcessamentoJob`, `MotivoCancelamento`) e enums correspondentes.

---

## 1. ContaFinanceira

- **Conta padrão única por usuário**: ao marcar uma conta como `padrao=true`, desmarcar automaticamente as demais. Não permitir deletar a conta padrão sem antes promover outra.
- **Não deletar conta com transações ativas**: bloquear `delete` físico em conta que possua transações não arquivadas; oferecer "desativar" (`ativa=false`) como soft delete, preservando histórico.
- **Saldo inicial imutável após primeira transação**: depois que houver qualquer movimentação, `saldoInicial` só pode ser alterado mediante "ajuste" (transação de acerto) — caso contrário corrompe o snapshot.
- **Regras por `TipoConta`**:
  - `cartao` aceita apenas GASTOS e estornos; receitas viram pagamento de fatura (transferência entre contas).
  - `investimento` não entra no cálculo de "dinheiro disponível para gastar" — só no patrimônio.
  - `dinheiro` não pode importar extrato PDF.
- **Limite máximo de contas ativas por usuário** (ex.: 20) para evitar bagunça e custo de snapshot.

## 2. Categoria e CategoriaThreshold

- **Categorias padrão (`padrao=true`) são read-only**: o usuário pode duplicar/clonar, mas não editar nem excluir as do sistema.
- **Transação não tem mais campo `tipo` próprio**: a direção (RECEITA/GASTO) é derivada da categoria vinculada (`Transacao.tipoEfetivo()`). Categoria RECEITA/GASTO implica a mesma direção na transação, com `valor` sempre positivo. Categoria `AMBOS` (ex.: "Outros") é a exceção: o sinal de `valor` desempata (negativo = gasto, positivo = receita).
- **Threshold como gatilho de revisão automática**:
  - Se `confiancaIa >= thresholdAuto` → status vai direto para `CLASSIFICADA`.
  - Se entre `thresholdAlerta` e `thresholdAuto` → `PENDENTE_REVISAO`.
  - Abaixo de `thresholdAlerta` ou `ambiguidadeAlta=true` → não classifica e exige revisão manual.
- **Orçamento mensal por categoria** (extensão natural do threshold): notificar quando o usuário ultrapassar X% do limite, e bloquear/avisar ao estourar 100%.
- **Não excluir categoria com transações vinculadas**: oferecer "mesclar com outra categoria" para reclassificar antes.

## 3. Transacao

- **Imutabilidade pós-confirmação**: transação com `statusRevisao = CONFIRMADA` só pode ser alterada via estorno ou cancelamento — não via PUT. Edição livre apenas em `EXTRAIDA`/`CLASSIFICADA`/`PENDENTE_REVISAO`.
- **Versionamento otimista** (já existe campo `versao`): rejeitar update se versão divergente, evitando perda de edição concorrente entre web e mobile.
- **Estorno como transação contrária**: criar uma nova transação com `indEstorno='S'` referenciando a original, em vez de apagar. Mantém auditoria e snapshot consistente.
- **Data futura permitida apenas para recorrentes/agendadas**: transação manual comum não pode ter `dataTransacao > hoje` (exceto fatura/agendamento). Se permitida, marcar como "prevista" e não contar no saldo realizado.
- **Recorrência**:
  - Se `recorrente=true`, exigir `periodoRecorrencia` e geração automática até X meses à frente como "previstas".
  - Ao confirmar a "prevista" do mês, materializar a transação real e propagar próxima.
  - Excluir recorrência: perguntar se afeta só esta, futuras, ou todas.
- **Detecção de duplicidade na importação**: mesma conta + valor + data + estabelecimento dentro de janela curta (ex.: 2 dias) → marcar como possível duplicata e segurar para revisão.
- **Limites de valor por origem**: transação `manual` aceita qualquer valor; vindas de `pdf` acima de X% do saldo médio entram em `PENDENTE_REVISAO` independente de IA.
- **Bloqueio de mudança de conta**: alterar `contaId` depois de criada não deveria ser permitido — apenas via "transferência" (par de transações).

## 4. TransacaoCancelada

- **Janela de cancelamento**: cancelamento livre nos primeiros N dias; após isso exigir motivo justificado e gerar evento de auditoria.
- **Cancelamento só por origem permitida** (`OrigemPermitidaCancelamento`): transações originárias de extrato PDF podem exigir motivo diferente de uma manual.
- **Cancelamento de transação que faz parte de snapshot fechado**: bloquear ou gerar lançamento de ajuste no mês corrente em vez de alterar o passado.
- **Recálculo automático de snapshot**: ao cancelar uma transação do mês corrente (snapshot aberto), reajustar `totalReceitas/totalGastos/saldoFinal`.

## 5. Extrato

- **Idempotência por `hashArquivo`**: rejeitar reupload do mesmo PDF (mesmo hash + mesma conta) — devolver o extrato existente.
- **Sobreposição de períodos**: avisar quando `periodoInicio/periodoFim` colide com outro extrato já processado da mesma conta; sugerir conciliação.
- **Status com transições válidas** (já existe enum): impedir transições inválidas (ex.: `concluido` → `extraindo`); só `reprocessando` pode voltar.
- **Score mínimo de extração**: abaixo de X (ex.: 0.6), forçar `pendente_revisao` independentemente do que a IA classificou.
- **Reprocessamento usa `parserVersaoId` mais nova**: ao reprocessar, registrar a versão do parser usada para auditoria de qualidade.
- **Conclusão automática**: `concluido` só quando `lancamentosPendentes == 0`. Caso contrário fica `parcialmente_revisado`.

## 6. SnapshotFinanceiro

- **Fechamento mensal**: rodar job no dia 1 que fecha (`fechado=true`) o mês anterior. Snapshot fechado é imutável.
- **`saldoInicial` do mês = `saldoFinal` do mês anterior**: validar consistência na criação; se divergir, gerar alerta de inconsistência.
- **Não permitir transação retroativa em mês fechado**: cair em "ajuste do mês corrente" com referência à data original (para relatório histórico mas snapshot íntegro).
- **Snapshot consolidado por usuário** (além de por conta): visão patrimonial geral, somando ativos e subtraindo cartão.

## 7. Notificacao

- **Retry com backoff**: ao falhar envio, incrementar `tentativas` com backoff exponencial; após N tentativas marcar erro definitivo e logar em auditoria.
- **Tipos disparadores** (catálogo):
  - Estouro de orçamento por categoria.
  - Extrato pendente de revisão há > X dias.
  - Saldo da conta abaixo de limite mínimo configurado.
  - Transação acima de X% da média mensal.
  - Recorrência prestes a vencer.
  - Detecção de cobrança duplicada via IA.
- **Canal preferido por tipo**: notificação crítica (saldo negativo) vai por todos os canais; informativa, só in-app.
- **Janela silenciosa**: não enviar push/WhatsApp entre 22h–7h salvo se classificada como crítica.
- **Dependência de `ConsentimentoLgpd`**: canal externo (WhatsApp/Telegram) só envia se houver consentimento ativo do tipo correspondente.

## 8. ConsentimentoLgpd

- **Versionamento**: ao mudar termos, exigir novo consentimento; revogação não apaga o registro anterior — cria evento novo.
- **Revogação cascateia**: revogar consentimento de notificação por WhatsApp desativa o `whatsappChatID` automaticamente.
- **Exportação/portabilidade**: usuário pode pedir exportação completa (todas transações, extratos, contas) — gerar `ProcessamentoJob` do tipo correspondente.
- **Exclusão de conta (direito ao esquecimento)**: anonimizar usuário (CPF/email/nome → hash) preservando agregados estatísticos sem PII.

## 9. AuditoriaEvento

- Registrar **todo cancelamento, fechamento de snapshot, mudança de senha, revogação de consentimento, alteração de transação confirmada** com IP/origem.
- Eventos de auditoria são **append-only**: nunca podem ser editados ou deletados, mesmo via API administrativa.

## 10. Usuario

- **Idade mínima** (`dtNascimento`): 18+ ou consentimento responsável — relevante para LGPD de menores.
- **Email verificado obrigatório** para enviar notificação por email e para deletar a conta (confirmação por link).
- **CPF imutável** após cadastro; nome editável; mudança de email exige reverificação.
- **Senha**: histórico de últimas N senhas para impedir reuso; expiração opcional; força mínima.

## 11. Regras transversais / agregadas

- **Transferência entre contas**: criar como par atômico (GASTO em uma + RECEITA em outra) com mesmo `code` de transferência. Cancelar uma cancela o par.
- **Pagamento de fatura de cartão**: transferência especial conta corrente → cartão que zera/reduz saldo do cartão e não conta como GASTO no relatório (já contou na compra original).
- **Cálculo de saldo "disponível"** = saldo da conta − soma de transações previstas/agendadas dentro do mês.
- **Conciliação manual**: marcar transação como "reconciliada com extrato" — útil quando extrato e lançamento manual coexistem.
- **Soft delete em tudo que entra em relatório** (`deletedAt`) para que histórico/snapshot não quebre.
- **Limites antifraude/coerência** (mesmo sem dinheiro real): mais de N transações em M segundos → bloquear e marcar para revisão (proteção contra script/bug do cliente).
