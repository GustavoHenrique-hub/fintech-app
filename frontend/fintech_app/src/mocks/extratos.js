// Mock de Extrato no formato do ExtratoResponseDTO.
//
// Origem: adapters/in/web/extrato/dto/ExtratoResponseDTO.java
//   id, code, usuarioId, contaId, arquivoNome, bancoDetectado,
//   periodoInicio (LocalDate), periodoFim (LocalDate),
//   status (StatusExtrato: upload_recebido | validando | na_fila | extraindo |
//           classificando | aguardando_ia | pendente_revisao |
//           parcialmente_revisado | concluido | erro_formato | erro_extracao |
//           erro_classificacao | erro_timeout | cancelado | reprocessando),
//   totalLancamentos, lancamentosConfirmados, lancamentosPendentes,
//   criadoEm (OffsetDateTime)
export const extratos = [
  {
    id: 9001, code: "EXT-001",
    usuarioId: 1, contaId: 101,
    arquivoNome: "nubank_extrato_abril_2026.pdf",
    bancoDetectado: "Nubank",
    periodoInicio: "2026-04-01",
    periodoFim:    "2026-04-30",
    status: "parcialmente_revisado",
    totalLancamentos: 42,
    lancamentosConfirmados: 35,
    lancamentosPendentes: 7,
    criadoEm: "2026-04-30T22:15:00-03:00",
  },
  {
    id: 9000, code: "EXT-002",
    usuarioId: 1, contaId: 101,
    arquivoNome: "nubank_extrato_marco_2026.pdf",
    bancoDetectado: "Nubank",
    periodoInicio: "2026-03-01",
    periodoFim:    "2026-03-31",
    status: "concluido",
    totalLancamentos: 38,
    lancamentosConfirmados: 38,
    lancamentosPendentes: 0,
    criadoEm: "2026-03-31T22:00:00-03:00",
  },
  {
    id: 8999, code: "EXT-003",
    usuarioId: 1, contaId: 102,
    arquivoNome: "itau_poupanca_2026q1.pdf",
    bancoDetectado: "Itaú",
    periodoInicio: "2026-01-01",
    periodoFim:    "2026-03-31",
    status: "aguardando_ia",
    totalLancamentos: 9,
    lancamentosConfirmados: 0,
    lancamentosPendentes: 9,
    criadoEm: "2026-04-22T10:30:00-03:00",
  },
];
