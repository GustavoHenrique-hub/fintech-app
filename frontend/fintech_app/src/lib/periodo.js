// Helpers de período (Semana/Mês/Ano) usados pela Overview e por Análises.
//
// Datas de transação (`dataTransacao`) vêm do backend como LocalDate serializado
// em "yyyy-MM-dd" — por isso os intervalos aqui também são strings nesse formato,
// tanto para bater com esses campos via comparação lexicográfica (que funciona
// para ISO 8601) quanto para ir direto como query param LocalDate no backend.
import { format, startOfMonth, startOfYear, subDays, subMonths, subYears } from "date-fns";

const ISO = "yyyy-MM-dd";

function intervaloDe(inicio, fim) {
  return { inicio: format(inicio, ISO), fim: format(fim, ISO) };
}

/** Intervalo do período selecionado ("Semana" | "Mês" | "Ano"), terminando em `referencia`. */
export function getIntervaloPeriodo(range, referencia = new Date()) {
  switch (range) {
    case "Semana":
      return intervaloDe(subDays(referencia, 6), referencia);
    case "Ano":
      return intervaloDe(startOfYear(referencia), referencia);
    case "Mês":
    default:
      return intervaloDe(startOfMonth(referencia), referencia);
  }
}

/** Intervalo imediatamente anterior, de mesma duração — usado para calcular tendência. */
export function getIntervaloPeriodoAnterior(range, referencia = new Date()) {
  switch (range) {
    case "Semana":
      return getIntervaloPeriodo(range, subDays(referencia, 7));
    case "Ano":
      return getIntervaloPeriodo(range, subYears(referencia, 1));
    case "Mês":
    default:
      return getIntervaloPeriodo(range, subMonths(referencia, 1));
  }
}

/** Filtra transações cuja `dataTransacao` cai dentro de [inicio, fim] (strings "yyyy-MM-dd"). */
export function filtrarPorIntervalo(transacoes, inicio, fim) {
  return (transacoes ?? []).filter(
    (t) => t.dataTransacao >= inicio && t.dataTransacao <= fim,
  );
}
