// AnalyticsScreen: visão analítica/estatística do mês.
// Layout:
//   1. Header + seletor de período (Week/Month/Year)
//   2. Cards de KPI (Income / Expenses / Net Savings)
//   3. Gráfico de linha: Income vs Expenses ao longo de 7 períodos
//   4. Gráfico donut: gastos por categoria
//   5. Cards de "Smart Insights" (recomendações automáticas)
import { useState } from "react";
import { TrendingUp, TrendingDown, Sparkles, AlertTriangle, Lightbulb } from "lucide-react";

const ranges = ["Week", "Month", "Year"];

// Dados mockados — em produção viriam de uma API ou React Query.
const incomeData = [3.2, 3.8, 3.4, 4.0, 4.2, 4.6, 4.8];
const expenseData = [2.1, 2.5, 2.8, 2.4, 2.9, 3.1, 2.8];
const labels = ["W1", "W2", "W3", "W4", "W5", "W6", "W7"];

const categories = [
  { name: "Food & Dining", value: 720, pct: 28, color: "hsl(var(--destructive))" },
  { name: "Transport",     value: 410, pct: 16, color: "hsl(var(--primary))" },
  { name: "Shopping",      value: 540, pct: 21, color: "hsl(var(--accent))" },
  { name: "Bills",         value: 620, pct: 24, color: "hsl(var(--info))" },
  { name: "Other",         value: 290, pct: 11, color: "hsl(var(--muted-foreground))" },
];

// Donut: usamos o truque clássico de SVG — um <circle> com stroke-dasharray
// proporcional à porcentagem e stroke-dashoffset acumulando o "início" de
// cada fatia. Rotacionamos 90° para começar do topo.
const Donut = () => {
  const radius = 42;
  const stroke = 14;
  const circumference = 2 * Math.PI * radius;
  let offset = 0;
  return (
    <svg viewBox="0 0 120 120" className="w-32 h-32 -rotate-90">
      {/* Trilha de fundo cinza. */}
      <circle cx="60" cy="60" r={radius} fill="none" stroke="hsl(var(--secondary))" strokeWidth={stroke} />
      {categories.map((c, i) => {
        const length = (c.pct / 100) * circumference;
        // dasharray: "comprimento_pintado comprimento_vazio".
        const dasharray = `${length} ${circumference - length}`;
        // dashoffset negativo "empurra" o início da fatia.
        const dashoffset = -offset;
        offset += length;
        return (
          <circle
            key={i}
            cx="60" cy="60" r={radius}
            fill="none" stroke={c.color} strokeWidth={stroke}
            strokeDasharray={dasharray}
            strokeDashoffset={dashoffset}
            strokeLinecap="butt"
          />
        );
      })}
    </svg>
  );
};

// LineChart: dois caminhos curvados (mesma técnica do BalanceChart).
const LineChart = () => {
  const max = 5;
  const w = 320;
  const h = 130;
  const step = w / (incomeData.length - 1);
  // Helper: converte array de números em uma string de path SVG suave.
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
        {/* Linhas-guia horizontais. */}
        {[30, 70, 110].map((y) => (
          <line key={y} x1="0" y1={y} x2={w} y2={y} stroke="hsl(var(--border))" strokeDasharray="2 4" />
        ))}
        <path d={toPath(incomeData)} fill="none" stroke="hsl(var(--success))" strokeWidth="2.5" strokeLinecap="round" />
        <path d={toPath(expenseData)} fill="none" stroke="hsl(var(--destructive))" strokeWidth="2.5" strokeLinecap="round" />
      </svg>
      {/* Rótulos do eixo X. */}
      <div className="flex justify-between text-[9.5px] text-muted-foreground font-medium mt-1.5 px-0.5">
        {labels.map((l) => <span key={l}>{l}</span>)}
      </div>
    </div>
  );
};

export const AnalyticsScreen = () => {
  const [range, setRange] = useState("Month");

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 space-y-5 no-scrollbar">
      {/* ── Header ──────────────────────────────────────────────────── */}
      <div>
        <h1 className="text-[22px] font-extrabold tracking-tight text-foreground leading-tight">
          Analytics
        </h1>
        <p className="text-[12px] text-muted-foreground mt-0.5">Track your financial behavior</p>
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

      {/* ── KPIs ────────────────────────────────────────────────────── */}
      <section className="grid grid-cols-3 gap-2">
        {[
          { label: "Income",      value: "$8,420", trend: "+12%", up: true,  color: "text-success" },
          { label: "Expenses",    value: "$2,580", trend: "-4%",  up: false, color: "text-destructive" },
          { label: "Net Savings", value: "$5,840", trend: "+18%", up: true,  color: "text-primary" },
        ].map((k) => {
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3">
              <p className="text-[10px] uppercase tracking-wider text-muted-foreground font-bold">
                {k.label}
              </p>
              <p className={`text-[15px] font-extrabold mt-1 tracking-tight tabular-nums ${k.color}`}>
                {k.value}
              </p>
              <div
                className={`flex items-center gap-0.5 mt-1 text-[10px] font-bold ${
                  k.up ? "text-success" : "text-destructive"
                }`}
              >
                <Trend className="w-2.5 h-2.5" strokeWidth={3} />
                {k.trend}
              </div>
            </div>
          );
        })}
      </section>

      {/* ── Gráfico Income vs Expenses ──────────────────────────────── */}
      <section className="card-soft p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Income vs Expenses</p>
            <p className="text-[12px] text-muted-foreground mt-0.5">7-period comparison</p>
          </div>
          {/* Legenda do gráfico. */}
          <div className="flex items-center gap-3 text-[10.5px]">
            <span className="flex items-center gap-1 text-muted-foreground">
              <span className="w-2 h-2 rounded-full bg-success" />
              Income
            </span>
            <span className="flex items-center gap-1 text-muted-foreground">
              <span className="w-2 h-2 rounded-full bg-destructive" />
              Expenses
            </span>
          </div>
        </div>
        <div className="mt-3">
          <LineChart />
        </div>
      </section>

      {/* ── Donut: gastos por categoria ─────────────────────────────── */}
      <section className="card-soft p-4">
        <p className="section-label">Spending by Category</p>
        <div className="flex items-center gap-4 mt-3">
          {/* Donut + total absoluto centralizado dentro do círculo. */}
          <div className="relative shrink-0">
            <Donut />
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <p className="text-[10px] text-muted-foreground font-semibold">Total</p>
              <p className="text-[14px] font-extrabold text-foreground tracking-tight tabular-nums">
                $2,580
              </p>
            </div>
          </div>
          {/* Legenda categorizada — bolinha colorida + nome + %. */}
          <div className="flex-1 space-y-1.5">
            {categories.map((c) => (
              <div key={c.name} className="flex items-center gap-2">
                <span
                  className="w-2 h-2 rounded-full shrink-0"
                  style={{ backgroundColor: c.color }}
                />
                <span className="text-[11.5px] text-foreground font-medium flex-1 truncate">
                  {c.name}
                </span>
                <span className="text-[11px] text-muted-foreground font-semibold tabular-nums">
                  {c.pct}%
                </span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Smart Insights ──────────────────────────────────────────── */}
      <section>
        <p className="section-label mb-2">Smart Insights</p>
        <div className="space-y-2">
          {[
            {
              icon: Lightbulb,
              color: "text-primary",
              bg: "bg-surface-purple",
              title: "Reduce dining out by 15%",
              desc: "You'd save ~$108 this month based on current spend.",
            },
            {
              icon: AlertTriangle,
              color: "text-destructive",
              bg: "bg-surface-pink",
              title: "Subscriptions up 22%",
              desc: "3 new subscriptions added in the last 30 days.",
            },
            {
              icon: Sparkles,
              color: "text-success",
              bg: "bg-surface-green",
              title: "On track for savings goal",
              desc: "$5,840 saved · 78% of monthly target.",
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
