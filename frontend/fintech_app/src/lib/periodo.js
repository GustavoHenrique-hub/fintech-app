// Helpers de período (Semana/Mês/Ano) usados pela Overview e por Análises.
//
// Datas de transação (`dataTransacao`) vêm do backend como LocalDate serializado
// em "yyyy-MM-dd" — por isso os intervalos aqui também são strings nesse formato,
// tanto para bater com esses campos via comparação lexicográfica (que funciona
// para ISO 8601) quanto para ir direto como query param LocalDate no backend.
import {
  endOfMonth, endOfYear, format, min, parseISO, startOfMonth, startOfYear,
  subDays, subMonths, subYears,
} from "date-fns";
import { ptBR } from "date-fns/locale";
import { contaNosTotais } from "./transacoes";

const ISO = "yyyy-MM-dd";

// Nunca avança além de hoje: o mês/ano corrente termina no dia atual.
function intervaloDe(inicio, fim) {
  return { inicio: format(inicio, ISO), fim: format(min([fim, new Date()]), ISO) };
}

/**
 * Data de referência dos KPIs (Overview e Análises usam a mesma): o dia do lançamento
 * mais recente que já conta nos totais (confirmado e não estornado), limitado a hoje.
 * Extratos importados costumam ser de meses anteriores — ancorar no mês corrente
 * deixaria Receitas/Gastos zerados mesmo depois da revisão. Sem lançamentos, hoje.
 */
export function getDataReferencia(transacoes) {
  const hoje = format(new Date(), ISO);
  const ultima = (transacoes ?? [])
    .filter(contaNosTotais)
    .reduce((maior, t) => (t.dataTransacao > maior ? t.dataTransacao : maior), "");
  return parseISO(ultima && ultima < hoje ? ultima : hoje);
}

/** Intervalo do período selecionado ("Semana" | "Mês" | "Ano") que contém `referencia`. */
export function getIntervaloPeriodo(range, referencia = new Date()) {
  switch (range) {
    case "Semana":
      return intervaloDe(subDays(referencia, 6), referencia);
    case "Ano":
      return intervaloDe(startOfYear(referencia), endOfYear(referencia));
    case "Mês":
    default:
      return intervaloDe(startOfMonth(referencia), endOfMonth(referencia));
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

/** Rótulo legível do intervalo: "agosto de 2026", "2026" ou "22/08 a 28/08/2026". */
export function descreverPeriodo(range, { inicio, fim }) {
  const i = parseISO(inicio);
  const f = parseISO(fim);
  switch (range) {
    case "Semana":
      return `${format(i, "dd/MM")} a ${format(f, "dd/MM/yyyy")}`;
    case "Ano":
      return format(i, "yyyy");
    case "Mês":
    default:
      return format(i, "MMMM 'de' yyyy", { locale: ptBR });
  }
}

/** Filtra transações cuja `dataTransacao` cai dentro de [inicio, fim] (strings "yyyy-MM-dd"). */
export function filtrarPorIntervalo(transacoes, inicio, fim) {
  return (transacoes ?? []).filter(
    (t) => t.dataTransacao >= inicio && t.dataTransacao <= fim,
  );
}
