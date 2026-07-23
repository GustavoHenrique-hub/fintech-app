import { useState, useMemo } from "react";
import { TrendingUp, TrendingDown, Sparkles, AlertTriangle, Lightbulb } from "lucide-react";

import { useSnapshots } from "@/hooks/use-snapshots";
import { useTransacoes } from "@/hooks/use-transacoes";
import { useCategorias } from "@/hooks/use-categorias";
import { useContaSelecionada } from "@/context/ContaSelecionadaContext";
import { useResumoPeriodo } from "@/hooks/use-resumo-periodo";
import { getIntervaloPeriodo, getIntervaloPeriodoAnterior, filtrarPorIntervalo } from "@/lib/periodo";
import { formatBRL, formatNumeroBR } from "@/lib/format";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { Skeleton } from "@/components/ui/skeleton";

const ranges = ["Semana", "Mês", "Ano"];
const DIAS_LABEL = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
const MES_ABREV = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];

// Soma receitas/gastos por dia entre `inicioStr` e `fimStr` (strings "yyyy-MM-dd").
// `labelFn` decide o rótulo de cada ponto (dia da semana, ou dia do mês esparso).
function bucketsDiarios(transacoes, inicioStr, fimStr, labelFn) {
  const dias = [];
  let cursor = new Date(`${inicioStr}T12:00:00`);
  const fimDate = new Date(`${fimStr}T12:00:00`);
  while (cursor <= fimDate) {
    dias.push(cursor.toISOString().slice(0, 10));
    cursor = new Date(cursor.getFullYear(), cursor.getMonth(), cursor.getDate() + 1);
  }

  const receitasSerie = dias.map((d) =>
    transacoes
      .filter((t) => t.dataTransacao === d && t.tipo === "RECEITA")
      .reduce((acc, t) => acc + Number(t.valor), 0),
  );
  const gastosSerie = dias.map((d) =>
    transacoes
      .filter((t) => t.dataTransacao === d && t.tipo === "GASTO")
      .reduce((acc, t) => acc + Number(t.valor), 0),
  );
  const n = dias.length;
  const labels = dias.map((d, i) => labelFn(new Date(`${d}T12:00:00`), i, n));

  return { receitasSerie, gastosSerie, labels };
}

function Donut({ fatias }) {
  const radius = 42;
  const stroke = 14;
  const circumference = 2 * Math.PI * radius;
  let offset = 0;
  return (
    <svg viewBox="0 0 120 120" className="w-32 h-32 -rotate-90">
      <circle cx="60" cy="60" r={radius} fill="none" stroke="hsl(var(--secondary))" strokeWidth={stroke} />
      {fatias.map((c, i) => {
        const length = (c.pct / 100) * circumference;
        const dasharray = `${length} ${circumference - length}`;
        const dashoffset = -offset;
        offset += length;
        return (
          <circle
            key={i}
            cx="60" cy="60" r={radius}
            fill="none" stroke={c.cor} strokeWidth={stroke}
            strokeDasharray={dasharray}
            strokeDashoffset={dashoffset}
            strokeLinecap="butt"
          />
        );
      })}
    </svg>
  );
}

function LineChart({ receitas, gastos, labels }) {
  const max = Math.max(...receitas, ...gastos) * 1.1 || 1;
  const w = 320;
  const h = 130;
  const step = w / (receitas.length - 1 || 1);

  const toPath = (data) =>
    data
      .map((v, i) => {
        const x = i * step;
        const y = h - (v / max) * (h - 20) - 10;
        if (i === 0) return `M${x},${y}`;
        const px = (i - 1) * step;
        const py = h - (data[i - 1] / max) * (h - 20) - 10;
        const cx = (px + x) / 2;
        return `Q${cx},${py} ${cx},${(py + y) / 2} T${x},${y}`;
      })
      .join(" ");

  return (
    <div>
      <svg viewBox={`0 0 ${w} ${h}`} className="w-full h-[130px]" preserveAspectRatio="none">
        {[30, 70, 110].map((y) => (
          <line key={y} x1="0" y1={y} x2={w} y2={y} stroke="hsl(var(--border))" strokeDasharray="2 4" />
        ))}
        <path d={toPath(receitas)} fill="none" stroke="hsl(var(--success))" strokeWidth="2.5" strokeLinecap="round" />
        <path d={toPath(gastos)} fill="none" stroke="hsl(var(--destructive))" strokeWidth="2.5" strokeLinecap="round" />
      </svg>
      <div className="flex justify-between text-[9.5px] text-muted-foreground font-medium mt-1.5 px-0.5">
        {labels.map((l, i) => <span key={i}>{l}</span>)}
      </div>
    </div>
  );
}

export const AnalyticsScreen = () => {
  const [range, setRange] = useState("Mês");

  const { contaAtual, loadingContas } = useContaSelecionada();
  const { data: snapshots = [], isLoading: loadingSnap } = useSnapshots();
  const { data: transacoes = [], isLoading: loadingTx } = useTransacoes();
  const { data: categorias = [], isLoading: loadingCat } = useCategorias();

  const intervaloAtual = useMemo(() => getIntervaloPeriodo(range), [range]);
  const intervaloAnterior = useMemo(() => getIntervaloPeriodoAnterior(range), [range]);

  const { data: resumoAtual, isLoading: loadingResumoAtual } = useResumoPeriodo({
    conta: contaAtual, ...intervaloAtual,
  });
  const { data: resumoAnterior, isLoading: loadingResumoAnterior } = useResumoPeriodo({
    conta: contaAtual, inicio: intervaloAnterior.inicio, fim: intervaloAnterior.fim,
  });

  const isLoading =
    loadingContas || loadingSnap || loadingTx || loadingCat || loadingResumoAtual || loadingResumoAnterior;

  const categoriasPorId = useMemo(
    () => Object.fromEntries(categorias.map((c) => [c.id, c])),
    [categorias],
  );

  // Transações da conta ativa — base para o gráfico (Semana/Mês) e para o donut.
  const transacoesDaConta = useMemo(
    () => transacoes.filter((t) => t.contaId === contaAtual?.id),
    [transacoes, contaAtual],
  );

  const { receitasSerie, gastosSerie, labels } = useMemo(() => {
    if (range === "Ano") {
      // Mantém a granularidade mensal (snapshots), agora escopada à conta ativa.
      const snapsDaConta = snapshots.filter((s) => s.contaId === contaAtual?.id);
      const ord = [...snapsDaConta].sort((a, b) =>
        a.ano !== b.ano ? a.ano - b.ano : a.mes - b.mes,
      );
      const ultimos = ord.slice(-7);
      return {
        receitasSerie: ultimos.map((s) => Number(s.totalReceitas)),
        gastosSerie: ultimos.map((s) => Number(s.totalGastos)),
        labels: ultimos.map((s) => MES_ABREV[s.mes - 1]),
      };
    }

    const labelFn = range === "Semana"
      ? (d) => DIAS_LABEL[d.getDay()]
      : (d, i, n) => (i % Math.max(1, Math.ceil(n / 6)) === 0 || i === n - 1
          ? String(d.getDate()).padStart(2, "0")
          : "");
    return bucketsDiarios(transacoesDaConta, intervaloAtual.inicio, intervaloAtual.fim, labelFn);
  }, [range, snapshots, transacoesDaConta, contaAtual, intervaloAtual]);

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

  const fatias = useMemo(() => {
    const gastosPeriodo = filtrarPorIntervalo(transacoesDaConta, intervaloAtual.inicio, intervaloAtual.fim)
      .filter((t) => t.tipo === "GASTO");
    const totalPeriodo = gastosPeriodo.reduce((acc, t) => acc + Number(t.valor), 0) || 1;

    const porCat = new Map();
    for (const t of gastosPeriodo) {
      const cat = categoriasPorId[t.categoriaId];
      const chave = cat?.parentId ?? cat?.id;
      const catRef = categoriasPorId[chave] ?? cat;
      if (!catRef) continue;
      const atual = porCat.get(catRef.id) ?? { ...catRef, total: 0 };
      atual.total += Number(t.valor);
      porCat.set(catRef.id, atual);
    }

    return Array.from(porCat.values())
      .map((c) => ({
        id: c.id,
        nome: c.nome,
        cor: c.corHex,
        total: c.total,
        pct: (c.total / totalPeriodo) * 100,
      }))
      .sort((a, b) => b.total - a.total);
  }, [transacoesDaConta, intervaloAtual, categoriasPorId]);

  const totalGastosCat = fatias.reduce((acc, f) => acc + f.total, 0);

  const confiancaMedia =
    transacoesDaConta.reduce((acc, t) => acc + (t.confiancaIa ?? 0), 0) / (transacoesDaConta.length || 1);

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
            trend: `+${economiaPct}%`,
            up: true,
            color: "text-primary",
          },
        ].map((k) => {
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3">
              <p className="text-[10px] uppercase tracking-wider text-muted-foreground font-bold">
                {k.label}
              </p>
              <p className={`text-[13.5px] font-extrabold mt-1 tracking-tight tabular-nums ${k.color}`}>
                R$ {formatNumeroBR(k.value)}
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
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Receitas vs Gastos</p>
            <p className="text-[12px] text-muted-foreground mt-0.5">
              {range === "Ano" ? "Últimos 7 períodos" : `Período: ${range}`}
            </p>
          </div>
          <div className="flex items-center gap-3 text-[10.5px]">
            <span className="flex items-center gap-1 text-muted-foreground">
              <span className="w-2 h-2 rounded-full bg-success" /> Receitas
            </span>
            <span className="flex items-center gap-1 text-muted-foreground">
              <span className="w-2 h-2 rounded-full bg-destructive" /> Gastos
            </span>
          </div>
        </div>
        <div className="mt-3">
          <LineChart receitas={receitasSerie} gastos={gastosSerie} labels={labels} />
        </div>
      </section>

      <section className="card-soft p-4 lg:p-6">
        <p className="section-label">Gastos por categoria</p>
        <div className="flex items-center gap-4 mt-3">
          <div className="relative shrink-0">
            <Donut fatias={fatias} />
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <p className="text-[10px] text-muted-foreground font-semibold">Total</p>
              <p className="text-[13px] font-extrabold text-foreground tracking-tight tabular-nums">
                {formatBRL(totalGastosCat)}
              </p>
            </div>
          </div>
          <div className="flex-1 space-y-1.5">
            {fatias.slice(0, 5).map((c) => (
              <div key={c.id} className="flex items-center gap-2">
                <span className="w-2 h-2 rounded-full shrink-0" style={{ backgroundColor: c.cor }} />
                <span className="text-[11.5px] text-foreground font-medium flex-1 truncate">{c.nome}</span>
                <span className="text-[11px] text-muted-foreground font-semibold tabular-nums">
                  {c.pct.toFixed(0)}%
                </span>
              </div>
            ))}
          </div>
        </div>
      </section>
      </div>

      <section className="card-soft p-4 space-y-3">
        <div>
          <p className="section-label">Acuracidade da IA</p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5">
            Média da confiança da IA na classificação das transações deste mês.
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
