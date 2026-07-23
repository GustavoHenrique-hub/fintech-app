// Barrel export. Permite imports curtos:
//   import { transacaoService } from "@/services";
//   await transacaoService.listarPorUsuario(usuarioId);
//
// Cada "service" reúne todas as funções de uma controller backend.
// Se preferir, importe direto dos arquivos específicos:
//   import { criar } from "@/services/transacao/NewTransacao";
export { api, apiUnwrap } from "./api";

import * as AuthService from "./auth/AuthService";
export const authService = { ...AuthService };

import * as GetBanco                   from "./banco/GetBanco";
import * as NewBanco                   from "./banco/NewBanco";
import * as GetCategoria               from "./categoria/GetCategoria";
import * as NewCategoria               from "./categoria/NewCategoria";
import * as GetConsentimento           from "./consentimento/GetConsentimento";
import * as NewConsentimento           from "./consentimento/NewConsentimento";
import * as GetContaFinanceira         from "./contaFinanceira/GetContaFinanceira";
import * as NewContaFinanceira         from "./contaFinanceira/NewContaFinanceira";
import * as RemoverContaFinanceira     from "./contaFinanceira/RemoverContaFinanceira";
import * as GetExtrato                 from "./extrato/GetExtrato";
import * as NewExtrato                 from "./extrato/NewExtrato";
import * as RemoverExtrato             from "./extrato/RemoverExtrato";
import * as GetMotivoCancelamento      from "./motivoCancelamento/GetMotivoCancelamento";
import * as GetNotificacao             from "./notificacao/GetNotificacao";
import * as NewNotificacao             from "./notificacao/NewNotificacao";
import * as GetSnapshotFinanceiro      from "./snapshotFinanceiro/GetSnapshotFinanceiro";
import * as GetTransacao               from "./transacao/GetTransacao";
import * as NewTransacao               from "./transacao/NewTransacao";
import * as EstornarTransacao          from "./transacao/EstornarTransacao";
import * as GetResumoPeriodo           from "./transacao/GetResumoPeriodo";
import * as GetTransacaoCancelada      from "./transacaoCancelada/GetTransacaoCancelada";
import * as NewTransacaoCancelada      from "./transacaoCancelada/NewTransacaoCancelada";
import * as GetUsuario                 from "./usuario/GetUsuario";
import * as NewUsuario                 from "./usuario/NewUsuario";
import * as UpdateUsuario              from "./usuario/UpdateUsuario";

export const bancoService               = { ...GetBanco, ...NewBanco };
export const categoriaService           = { ...GetCategoria, ...NewCategoria };
export const consentimentoService       = { ...GetConsentimento, ...NewConsentimento };
export const contaFinanceiraService     = { ...GetContaFinanceira, ...NewContaFinanceira, ...RemoverContaFinanceira };
export const extratoService             = { ...GetExtrato, ...NewExtrato, ...RemoverExtrato };
export const motivoCancelamentoService  = { ...GetMotivoCancelamento };
export const notificacaoService         = { ...GetNotificacao, ...NewNotificacao };
export const snapshotFinanceiroService  = { ...GetSnapshotFinanceiro };
export const transacaoService           = { ...GetTransacao, ...NewTransacao, ...EstornarTransacao, ...GetResumoPeriodo };
export const transacaoCanceladaService  = { ...GetTransacaoCancelada, ...NewTransacaoCancelada };
export const usuarioService             = { ...GetUsuario, ...NewUsuario, ...UpdateUsuario };
