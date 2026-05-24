// AnalyticsScreen: gráficos de comportamento financeiro mês a mês.
//
// Onde plugar com o backend:
//   - snapshots          → GET /snapshots/usuario/{id}/{conta} (últimos 12 meses)
//   - transacoes         → GET /transacoes/usuario/{id}        (para o donut por categoria)
//   - categoriasPorId    → mapa categoria.id → CategoriaResponseDTO
import { useState, useMemo } from "react";
import { TrendingUp, TrendingDown, Sparkles, AlertTriangle, Lightbulb } from "lucide-react";

import { snapshots, transacoes, categoriasPorId } from "@/mocks";
import { formatBRL, formatNumeroBR } from "@/lib/format";
import { ConfidenceBar } from "@/components/ui/confidence-bar";

const ranges = ["Semana", "Mês", "Ano"];

// Donut: SVG puro — `strokeDasharray` proporcional à fatia + offset acumulado.
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

// Gráfico de linha: receitas vs gastos por mês.
function LineChart({ receitas, gastos, labels }) {
  // Escala dinâmica baseada no maior valor (com 10% de folga).
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
        {labels.map((l) => <span key={l}>{l}</span>)}
      </div>
    </div>
  );
}

export const AnalyticsScreen = () => {
  const [range, setRange] = useState("Mês");

  // ── Série de receitas/gastos a partir dos snapshots ─────────────
  // Pegamos os snapshots em ordem cronológica crescente (mais antigos→atuais).
  const { receitasSerie, gastosSerie, labels } = useMemo(() => {
    const ord = [...snapshots].sort((a, b) =>
      a.ano !== b.ano ? a.ano - b.ano : a.mes - b.mes,
    );
    const ultimos = ord.slice(-7);
    const mesAbrev = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];
    return {
      receitasSerie: ultimos.map((s) => Number(s.totalReceitas)),
      gastosSerie:   ultimos.map((s) => Number(s.totalGastos)),
      labels:        ultimos.map((s) => mesAbrev[s.mes - 1]),
    };
  }, []);

  // ── KPIs agregados do último snapshot ──────────────────────────
  const ultimoSnap = snapshots[0]; // primeiro do array = mais recente (não-fechado)
  const totalReceitas = ultimoSnap.totalReceitas;
  const totalGastos = ultimoSnap.totalGastos;
  const economia = totalReceitas - totalGastos;
  const economiaPct = ((economia / (totalReceitas || 1)) * 100).toFixed(0);

  // ── Gastos por categoria (transações do mês corrente) ──────────
  const fatias = useMemo(() => {
    // Agrupa GASTOS do mês corrente por categoriaId.
    const gastosMes = transacoes.filter(
      (t) =>
        t.tipo === "GASTO" &&
        t.dataTransacao.startsWith(`${ultimoSnap.ano}-${String(ultimoSnap.mes).padStart(2, "0")}`),
    );
    const totalMes = gastosMes.reduce((acc, t) => acc + Number(t.valor), 0) || 1;

    // Soma por categoria (uma pílula por categoria pai — agrupa filhos no pai).
    const porCat = new Map();
    for (const t of gastosMes) {
      const cat = categoriasPorId[t.categoriaId];
      // Se a categoria tem `parentId`, agrupa no pai pra não pulverizar o donut.
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
        pct: (c.total / totalMes) * 100,
      }))
      .sort((a, b) => b.total - a.total);
  }, [ultimoSnap]);

  const totalGastosCat = fatias.reduce((acc, f) => acc + f.total, 0);

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 space-y-5 no-scrollbar">
      {/* Header */}
      <div>
        <h1 className="text-[22px] font-extrabold tracking-tight text-foreground leading-tight">
          Análises
        </h1>
        <p className="text-[12px] text-muted-foreground mt-0.5">
          Acompanhe seu comportamento financeiro
        </p>
      </div>

      {/* Segmented control de período. */}
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

      {/* KPIs do mês corrente */}
      <section className="grid grid-cols-3 gap-2">
        {[
          { label: "Receitas",  value: totalReceitas, trend: "+12%", up: true,  color: "text-success" },
          { label: "Gastos",    value: totalGastos,   trend: "-4%",  up: false, color: "text-destructive" },
          { label: "Economia",  value: economia,      trend: `+${economiaPct}%`, up: true, color: "text-primary" },
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

      {/* Gráfico Income vs Expenses (mensal). */}
      <section className="card-soft p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Receitas vs Gastos</p>
            <p className="text-[12px] text-muted-foreground mt-0.5">Últimos 7 períodos</p>
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

      {/* Donut: gastos por categoria. */}
      <section className="card-soft p-4">
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
                <span className="text-[11.5px] text-foreground font-medium flex-1 truncate">
                  {c.nome}
                </span>
                <span className="text-[11px] text-muted-foreground font-semibold tabular-nums">
                  {c.pct.toFixed(0)}%
                </span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Acuracidade da classificação — usa ConfidenceBar */}
      <section className="card-soft p-4 space-y-3">
        <div>
          <p className="section-label">Acuracidade da IA</p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5">
            Média da confiança da IA na classificação das transações deste mês.
          </p>
        </div>
        <ConfidenceBar
          value={
            transacoes.reduce((acc, t) => acc + (t.confiancaIa ?? 0), 0) /
            (transacoes.length || 1)
          }
          label="Confiança média"
        />
      </section>

      {/* Smart insights */}
      <section>
        <p className="section-label mb-2">Recomendações</p>
        <div className="space-y-2">
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
  );
};
