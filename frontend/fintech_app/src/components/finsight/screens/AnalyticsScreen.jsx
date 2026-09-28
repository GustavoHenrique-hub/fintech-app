import { useState, useMemo } from "react";
import { TrendingUp, TrendingDown, Sparkles, AlertTriangle, Lightbulb } from "lucide-react";
import Chart from "react-apexcharts";

import { useTransacoes } from "@/hooks/use-transacoes";
import { useCategorias } from "@/hooks/use-categorias";
import { useContaSelecionada } from "@/context/ContaSelecionadaContext";
import { useResumoPeriodo } from "@/hooks/use-resumo-periodo";
import {
  descreverPeriodo, filtrarPorIntervalo, getDataReferencia, getIntervaloPeriodo, getIntervaloPeriodoAnterior,
} from "@/lib/periodo";
import { formatBRL, formatNumeroBR } from "@/lib/format";
import { contaNosTotais, valorAbsoluto } from "@/lib/transacoes";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { Skeleton } from "@/components/ui/skeleton";
import { CORES, opcoesBase } from "../charts/chart-theme";

const ranges = ["Semana", "Mês", "Ano"];
const DIAS_LABEL = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
const MES_ABREV = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];
const MAX_FATIAS = 6;

const isoLocal = (d) =>
  `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;

// Soma receitas/gastos por bucket. `chaveDe` leva a data da transação ao bucket
// ("yyyy-MM-dd" por dia, "yyyy-MM" por mês).
function somarPorBucket(transacoes, buckets, chaveDe) {
  const receitas = new Map(buckets.map((b) => [b.chave, 0]));
  const gastos = new Map(buckets.map((b) => [b.chave, 0]));
  for (const t of transacoes) {
    const chave = chaveDe(t.dataTransacao);
    const alvo = t.tipo === "RECEITA" ? receitas : gastos;
    if (alvo.has(chave)) alvo.set(chave, alvo.get(chave) + valorAbsoluto(t));
  }
  return {
    receitasSerie: buckets.map((b) => Number(receitas.get(b.chave).toFixed(2))),
    gastosSerie: buckets.map((b) => Number(gastos.get(b.chave).toFixed(2))),
    labels: buckets.map((b) => b.label),
  };
}

function bucketsDiarios(inicioStr, fimStr, range) {
  const buckets = [];
  const fim = new Date(`${fimStr}T12:00:00`);
  for (let d = new Date(`${inicioStr}T12:00:00`); d <= fim; d = new Date(d.getFullYear(), d.getMonth(), d.getDate() + 1, 12)) {
    buckets.push({
      chave: isoLocal(d),
      label: range === "Semana" ? DIAS_LABEL[d.getDay()] : String(d.getDate()).padStart(2, "0"),
    });
  }
  return buckets;
}

function bucketsMensais(inicioStr, fimStr) {
  const buckets = [];
  const [anoIni, mesIni] = inicioStr.split("-").map(Number);
  const [anoFim, mesFim] = fimStr.split("-").map(Number);
  for (let a = anoIni, m = mesIni; a < anoFim || (a === anoFim && m <= mesFim); m === 12 ? (a++, m = 1) : m++) {
    buckets.push({ chave: `${a}-${String(m).padStart(2, "0")}`, label: MES_ABREV[m - 1] });
  }
  return buckets;
}

function GraficoReceitasGastos({ receitas, gastos, labels, range }) {
  const base = opcoesBase();
  const options = {
    ...base,
    chart: { ...base.chart, id: "receitas-gastos", type: "bar" },
    colors: [CORES.receita, CORES.gasto],
    plotOptions: {
      bar: {
        columnWidth: range === "Mês" ? "70%" : "55%",
        borderRadius: 4,
        borderRadiusApplication: "end",
      },
    },
    // Espaço de superfície entre barras vizinhas em vez de borda.
    stroke: { show: true, width: 2, colors: ["transparent"] },
    xaxis: {
      ...base.xaxis,
      categories: labels,
      tickAmount: range === "Mês" ? 8 : undefined,
      labels: { ...base.xaxis.labels, rotate: 0 },
    },
    tooltip: { ...base.tooltip, shared: true, intersect: false },
  };

  return (
    <Chart
      type="bar"
      height={240}
      options={options}
      series={[
        { name: "Receitas", data: receitas },
        { name: "Gastos", data: gastos },
      ]}
    />
  );
}

function GraficoCategorias({ fatias, total }) {
  const options = {
    ...opcoesBase(),
    chart: { ...opcoesBase().chart, id: "gastos-categoria", type: "donut" },
    labels: fatias.map((f) => f.nome),
    colors: fatias.map((f) => f.cor),
    // Anel de superfície separando as fatias.
    stroke: { width: 2, colors: [CORES.superficie] },
    plotOptions: {
      pie: {
        expandOnClick: false,
        donut: {
          size: "72%",
          labels: {
            show: true,
            name: { show: true, fontSize: "11px", color: CORES.textoSuave, offsetY: -4 },
            value: {
              show: true, fontSize: "15px", fontWeight: 800, color: CORES.texto, offsetY: 4,
              formatter: (v) => formatBRL(Number(v)),
            },
            total: {
              show: true, showAlways: true, label: "Total", fontSize: "11px", color: CORES.textoSuave,
              formatter: () => formatBRL(total),
            },
          },
        },
      },
    },
    tooltip: { ...opcoesBase().tooltip, fillSeriesColor: false },
  };

  return <Chart type="donut" height={200} width={200} options={options} series={fatias.map((f) => f.total)} />;
}

export const AnalyticsScreen = () => {
  const [range, setRange] = useState("Mês");

  const { contaAtual, loadingContas } = useContaSelecionada();
  const { data: transacoes = [], isLoading: loadingTx } = useTransacoes();
  const { data: categorias = [], isLoading: loadingCat } = useCategorias();

  // Transações da conta ativa. Os gráficos usam só as que contam nos totais
  // (confirmadas e não estornadas) — a mesma regra dos KPIs do backend.
  const transacoesDaConta = useMemo(
    () => transacoes.filter((t) => t.contaId === contaAtual?.id),
    [transacoes, contaAtual],
  );

  // Mesma referência da Visão geral: o período que contém o lançamento confirmado
  // mais recente (extratos importados costumam ser de meses anteriores).
  const referencia = useMemo(() => getDataReferencia(transacoesDaConta), [transacoesDaConta]);
  const intervaloAtual = useMemo(() => getIntervaloPeriodo(range, referencia), [range, referencia]);
  const intervaloAnterior = useMemo(
    () => getIntervaloPeriodoAnterior(range, referencia),
    [range, referencia],
  );

  const { data: resumoAtual, isLoading: loadingResumoAtual } = useResumoPeriodo({
    conta: contaAtual, ...intervaloAtual,
  });
  const { data: resumoAnterior, isLoading: loadingResumoAnterior } = useResumoPeriodo({
    conta: contaAtual, inicio: intervaloAnterior.inicio, fim: intervaloAnterior.fim,
  });

  const isLoading =
    loadingContas || loadingTx || loadingCat || loadingResumoAtual || loadingResumoAnterior;

  const categoriasPorId = useMemo(
    () => Object.fromEntries(categorias.map((c) => [c.id, c])),
    [categorias],
  );

  const efetivasNoPeriodo = useMemo(
    () => filtrarPorIntervalo(transacoesDaConta.filter(contaNosTotais), intervaloAtual.inicio, intervaloAtual.fim),
    [transacoesDaConta, intervaloAtual],
  );

  const { receitasSerie, gastosSerie, labels } = useMemo(() => {
    if (range === "Ano") {
      return somarPorBucket(efetivasNoPeriodo, bucketsMensais(intervaloAtual.inicio, intervaloAtual.fim),
        (data) => data.slice(0, 7));
    }
    return somarPorBucket(efetivasNoPeriodo, bucketsDiarios(intervaloAtual.inicio, intervaloAtual.fim, range),
      (data) => data);
  }, [range, efetivasNoPeriodo, intervaloAtual]);

  const totalReceitas = Number(resumoAtual?.totalReceitas ?? 0);
  const totalGastos   = Number(resumoAtual?.totalGastos ?? 0);
  const economia      = totalReceitas - totalGastos;
  const economiaPct   = ((economia / (totalReceitas || 1)) * 100).toFixed(0);

  const receitasAnterior = Number(resumoAnterior?.totalReceitas ?? 0);
  const gastosAnterior   = Number(resumoAnterior?.totalGastos ?? 0);

  const receitasTrend = resumoAnterior
    ? (((totalReceitas - receitasAnterior) / (receitasAnterior || 1)) * 100).toFixed(0)
    : null;
  const gastosTrend = resumoAnterior
    ? (((totalGastos - gastosAnterior) / (gastosAnterior || 1)) * 100).toFixed(0)
    : null;

  // Gastos por categoria-mãe, em valor absoluto (na categoria genérica o gasto vem
  // negativo). Acima de MAX_FATIAS o resto vira uma fatia "Outras categorias".
  const fatias = useMemo(() => {
    const porCat = new Map();
    for (const t of efetivasNoPeriodo.filter((x) => x.tipo === "GASTO")) {
      const cat = categoriasPorId[t.categoriaId];
      const catRef = categoriasPorId[cat?.parentId] ?? cat;
      const chave = catRef?.id ?? "sem-categoria";
      const atual = porCat.get(chave) ?? {
        id: chave,
        nome: catRef?.nome ?? "Sem categoria",
        cor: catRef?.corHex ?? CORES.semCategoria,
        total: 0,
      };
      atual.total += valorAbsoluto(t);
      porCat.set(chave, atual);
    }

    const ordenadas = Array.from(porCat.values()).sort((a, b) => b.total - a.total);
    const principais = ordenadas.slice(0, MAX_FATIAS);
    const resto = ordenadas.slice(MAX_FATIAS).reduce((acc, c) => acc + c.total, 0);
    if (resto > 0) {
      principais.push({ id: "outras", nome: "Outras categorias", cor: CORES.semCategoria, total: resto });
    }
    const total = principais.reduce((acc, c) => acc + c.total, 0) || 1;
    return principais.map((c) => ({ ...c, total: Number(c.total.toFixed(2)), pct: (c.total / total) * 100 }));
  }, [efetivasNoPeriodo, categoriasPorId]);

  const totalGastosCat = fatias.reduce((acc, f) => acc + f.total, 0);

  // Média só entre lançamentos que a IA classificou (os manuais não têm confiança).
  const comConfianca = transacoesDaConta.filter((t) => t.confiancaIa != null);
  const confiancaMedia =
    comConfianca.reduce((acc, t) => acc + t.confiancaIa, 0) / (comConfianca.length || 1);
  const autoClassificadas = comConfianca.filter((t) => t.confiancaIa >= 95 && t.statusRevisao === "CONFIRMADA").length;

  if (isLoading) {
    return (
      <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
        <div className="max-w-6xl mx-auto w-full space-y-5">
          <Skeleton className="h-12 w-48 rounded-xl" />
          <div className="grid grid-cols-3 gap-2">
            {[0, 1, 2].map((i) => <Skeleton key={i} className="h-24 rounded-2xl" />)}
          </div>
          <div className="grid lg:grid-cols-2 gap-5">
            <Skeleton className="h-52 rounded-2xl" />
            <Skeleton className="h-52 rounded-2xl" />
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
     <div className="max-w-6xl mx-auto w-full space-y-5 lg:space-y-7">
      <div>
        <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight">
          Análises
        </h1>
        <p className="text-[12px] lg:text-[13px] text-muted-foreground mt-0.5">
          Acompanhe seu comportamento financeiro
        </p>
      </div>

      <div className="inline-flex bg-secondary rounded-full p-0.5">
        {ranges.map((r) => (
          <button
            key={r}
            onClick={() => setRange(r)}
            className={`px-4 py-1 text-[12px] font-semibold rounded-full transition-all ${
              range === r ? "bg-card text-foreground shadow-sm" : "text-muted-foreground"
            }`}
          >
            {r}
          </button>
        ))}
      </div>
      <p className="text-[12px] text-muted-foreground -mt-3 lg:-mt-5 first-letter:uppercase">
        {descreverPeriodo(range, intervaloAtual)}
      </p>

      <section className="grid grid-cols-3 gap-2">
        {[
          {
            label: "Receitas",
            value: totalReceitas,
            trend: receitasTrend !== null ? `${receitasTrend > 0 ? "+" : ""}${receitasTrend}%` : "—",
            up: receitasTrend === null || Number(receitasTrend) >= 0,
            color: "text-success",
          },
          {
            label: "Gastos",
            value: totalGastos,
            trend: gastosTrend !== null ? `${gastosTrend > 0 ? "+" : ""}${gastosTrend}%` : "—",
            up: gastosTrend === null || Number(gastosTrend) <= 0,
            color: "text-destructive",
          },
          {
            label: "Economia",
            value: economia,
            trend: `${economia >= 0 ? "+" : ""}${economiaPct}%`,
            up: economia >= 0,
            color: economia >= 0 ? "text-primary" : "text-destructive",
          },
        ].map((k) => {
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3">
              <p className="text-[10px] uppercase tracking-wider text-muted-foreground font-bold">
                {k.label}
              </p>
              <p className={`text-[13.5px] font-extrabold mt-1 tracking-tight tabular-nums ${k.color}`}>
                {k.value < 0 ? "−" : ""}R$ {formatNumeroBR(Math.abs(k.value))}
              </p>
              <div className={`flex items-center gap-0.5 mt-1 text-[10px] font-bold ${k.up ? "text-success" : "text-destructive"}`}>
                <Trend className="w-2.5 h-2.5" strokeWidth={3} />
                {k.trend}
              </div>
            </div>
          );
        })}
      </section>

      <div className="grid lg:grid-cols-2 gap-5 lg:gap-6">
      <section className="card-soft p-4 lg:p-6">
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="section-label">Receitas vs Gastos</p>
            <p className="text-[12px] text-muted-foreground mt-0.5">
              {range === "Ano" ? "Por mês, no ano" : range === "Mês" ? "Por dia, no mês" : "Por dia, últimos 7 dias"}
            </p>
          </div>
          <div className="flex items-center gap-3 text-[11px] shrink-0">
            <span className="flex items-center gap-1.5 text-muted-foreground font-medium">
              <span className="w-2.5 h-2.5 rounded-sm" style={{ backgroundColor: CORES.receita }} /> Receitas
            </span>
            <span className="flex items-center gap-1.5 text-muted-foreground font-medium">
              <span className="w-2.5 h-2.5 rounded-sm" style={{ backgroundColor: CORES.gasto }} /> Gastos
            </span>
          </div>
        </div>
        <div className="mt-2 -mx-1">
          <GraficoReceitasGastos receitas={receitasSerie} gastos={gastosSerie} labels={labels} range={range} />
        </div>
      </section>

      <section className="card-soft p-4 lg:p-6">
        <p className="section-label">Gastos por categoria</p>
        {fatias.length === 0 ? (
          <p className="text-[12px] text-muted-foreground mt-6 text-center">
            Nenhum gasto confirmado no período.
          </p>
        ) : (
          <div className="flex flex-col sm:flex-row items-center gap-4 mt-3">
            <div className="shrink-0">
              <GraficoCategorias fatias={fatias} total={totalGastosCat} />
            </div>
            <div className="flex-1 w-full space-y-2">
              {fatias.map((c) => (
                <div key={c.id} className="flex items-center gap-2">
                  <span className="w-2.5 h-2.5 rounded-full shrink-0" style={{ backgroundColor: c.cor }} />
                  <span className="text-[12px] text-foreground font-medium flex-1 truncate">{c.nome}</span>
                  <span className="text-[11.5px] text-muted-foreground tabular-nums">{formatBRL(c.total)}</span>
                  <span className="text-[11.5px] text-foreground font-semibold tabular-nums w-10 text-right">
                    {c.pct.toFixed(0)}%
                  </span>
                </div>
              ))}
            </div>
          </div>
        )}
      </section>
      </div>

      <section className="card-soft p-4 space-y-3">
        <div>
          <p className="section-label">Acuracidade da IA</p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5">
            Média da confiança da IA nos lançamentos importados. A partir de 95% o lançamento é
            confirmado automaticamente{autoClassificadas > 0 ? ` — ${autoClassificadas} até agora` : ""}.
          </p>
        </div>
        <ConfidenceBar value={confiancaMedia} label="Confiança média" />
      </section>

      <section>
        <p className="section-label mb-2">Recomendações</p>
        <div className="grid lg:grid-cols-3 gap-2 lg:gap-3">
          {[
            {
              icon: Lightbulb,
              color: "text-primary",
              bg: "bg-surface-purple",
              title: "Reduza delivery em 15%",
              desc: "Você economizaria ~R$ 108 este mês com esse ajuste.",
            },
            {
              icon: AlertTriangle,
              color: "text-destructive",
              bg: "bg-surface-pink",
              title: "Assinaturas subiram 22%",
              desc: "3 novas assinaturas detectadas nos últimos 30 dias.",
            },
            {
              icon: Sparkles,
              color: "text-success",
              bg: "bg-surface-green",
              title: "Meta de economia no rumo",
              desc: `${formatBRL(economia)} economizados · ${economiaPct}% da meta mensal.`,
            },
          ].map((s, i) => {
            const Icon = s.icon;
            return (
              <div key={i} className="card-soft p-3 flex gap-3">
                <div className={`w-9 h-9 rounded-xl ${s.bg} ${s.color} flex items-center justify-center shrink-0`}>
                  <Icon className="w-4 h-4" strokeWidth={2.25} />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-[13px] font-bold text-foreground leading-tight">{s.title}</p>
                  <p className="text-[11.5px] text-muted-foreground mt-0.5 leading-snug">{s.desc}</p>
                </div>
              </div>
            );
          })}
        </div>
      </section>
     </div>
    </div>
  );
};
