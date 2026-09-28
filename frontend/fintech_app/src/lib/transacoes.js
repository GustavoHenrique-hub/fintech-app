// Regras de leitura de TransacaoResponseDTO compartilhadas entre as telas.

/**
 * Valor com o sinal da direção: receita positiva, gasto negativo.
 * Na categoria genérica ("Outros", tipo AMBOS) o backend guarda gasto como valor
 * negativo — por isso sempre partimos do valor absoluto e aplicamos `tipo`.
 */
export function valorComSinal(t) {
  const abs = Math.abs(Number(t?.valor) || 0);
  return t?.tipo === "RECEITA" ? abs : -abs;
}

/** Valor absoluto (para somas de receitas ou de gastos). */
export function valorAbsoluto(t) {
  return Math.abs(Number(t?.valor) || 0);
}

/**
 * `true` quando a transação conta nos totais e no saldo: já revisada/confirmada e
 * não estornada. Pendentes de revisão só passam a valer ao serem confirmados —
 * a mesma regra do resumo por período no backend.
 */
export function contaNosTotais(t) {
  return t?.statusRevisao !== "PENDENTE_REVISAO" && !t?.estornadoAt && t?.indEstorno !== "S";
}

/** Categorias compatíveis com o destino escolhido (as do tipo "ambos" servem para os dois). */
export function categoriasCompativeis(categorias, destino) {
  const alvo = (destino ?? "").toLowerCase();
  return categorias
    .filter((c) => [alvo, "ambos"].includes((c.tipo ?? "").toLowerCase()))
    .map((c) => ({ id: c.id, label: c.nome, parentId: c.parentId }));
}

/** Destino inicial de um lançamento na revisão: a direção que veio da extração. */
export const destinoSugerido = (t) => (t?.tipo === "RECEITA" ? "RECEITA" : "GASTO");
