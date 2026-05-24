// Mock de contas financeiras no formato do ContaFinanceiraResponseDTO.
//
// Origem: adapters/in/web/contafinanceira/dto/ContaFinanceiraResponseDTO.java
//   id, code, usuarioId, nome, tipo (TipoConta), banco,
//   saldoInicial (BigDecimal → number aqui), padrao, ativa, criadoEm (OffsetDateTime)
//
// Enum TipoConta: corrente | poupanca | cartao | dinheiro | investimento
//
// Para integrar: substituir por useQuery(["contas", usuarioId]) e remover este arquivo.
export const contas = [
  {
    id: 101,
    code: "CNT-001",
    usuarioId: 1,
    nome: "Conta Corrente Principal",
    tipo: "corrente",
    banco: "Nubank",
    saldoInicial: 12420.10,
    padrao: true,
    ativa: true,
    criadoEm: "2024-09-12T14:30:00-03:00",
  },
  {
    id: 102,
    code: "CNT-002",
    usuarioId: 1,
    nome: "Poupança",
    tipo: "poupanca",
    banco: "Itaú",
    saldoInicial: 8540.00,
    padrao: false,
    ativa: true,
    criadoEm: "2025-01-04T09:12:00-03:00",
  },
  {
    id: 103,
    code: "CNT-003",
    usuarioId: 1,
    nome: "Cartão Crédito",
    tipo: "cartao",
    banco: "Inter",
    saldoInicial: -812.40,
    padrao: false,
    ativa: true,
    criadoEm: "2025-03-22T18:00:00-03:00",
  },
];

// Helper: conta marcada como padrão (a primeira do array como fallback).
export const contaPadrao = contas.find((c) => c.padrao) ?? contas[0];
