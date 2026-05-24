// Mock de SnapshotFinanceiro no formato do SnapshotFinanceiroResponseDTO.
//
// Origem: adapters/in/web/snapshotfinanceiro/dto/SnapshotFinanceiroResponseDTO.java
//   id, usuarioId, contaId, ano (short), mes (short),
//   saldoInicial (BigDecimal), totalReceitas (BigDecimal),
//   totalGastos (BigDecimal), saldoFinal (BigDecimal), fechado
//
// Convenção: 1 snapshot por (usuario, conta, ano, mês). Aqui só geramos para
// a conta padrão (101). Para integrar, useQuery(["snapshots", { usuarioId, contaId }]).
export const snapshots = [
  // Mês corrente (Abril/2026) ainda aberto.
  {
    id: 7012, usuarioId: 1, contaId: 101,
    ano: 2026, mes: 4,
    saldoInicial: 13800.00, totalReceitas: 8420.00,
    totalGastos: 2870.40,    saldoFinal: 19349.60,
    fechado: false,
  },
  // Histórico (últimos 6 meses fechados).
  { id: 7011, usuarioId: 1, contaId: 101, ano: 2026, mes: 3, saldoInicial: 11540.00, totalReceitas: 7950.00, totalGastos: 5690.00, saldoFinal: 13800.00, fechado: true },
  { id: 7010, usuarioId: 1, contaId: 101, ano: 2026, mes: 2, saldoInicial: 10120.00, totalReceitas: 7820.00, totalGastos: 6400.00, saldoFinal: 11540.00, fechado: true },
  { id: 7009, usuarioId: 1, contaId: 101, ano: 2026, mes: 1, saldoInicial:  9550.00, totalReceitas: 7820.00, totalGastos: 7250.00, saldoFinal: 10120.00, fechado: true },
  { id: 7008, usuarioId: 1, contaId: 101, ano: 2025, mes: 12, saldoInicial: 8900.00, totalReceitas: 9200.00, totalGastos: 8550.00, saldoFinal:  9550.00, fechado: true },
  { id: 7007, usuarioId: 1, contaId: 101, ano: 2025, mes: 11, saldoInicial: 8200.00, totalReceitas: 7820.00, totalGastos: 7120.00, saldoFinal:  8900.00, fechado: true },
  { id: 7006, usuarioId: 1, contaId: 101, ano: 2025, mes: 10, saldoInicial: 7100.00, totalReceitas: 7820.00, totalGastos: 6720.00, saldoFinal:  8200.00, fechado: true },
];

// Helper: snapshot do mês corrente (não fechado).
export const snapshotAtual = snapshots.find((s) => !s.fechado) ?? snapshots[0];
