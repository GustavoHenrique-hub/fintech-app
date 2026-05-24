// OverviewScreen ("Home"): tela inicial do app.
// Estrutura, de cima pra baixo:
//   1. Saudação + data
//   2. Card hero do saldo total (gradiente roxo)
//   3. Atalhos rápidos (Transfer / Add Funds / Request / Pay Bill)
//   4. Gráfico do saldo (BalanceChart) com seletor de período
//   5. Mini KPIs de Income / Expenses / Savings
//   6. Lista resumida de atividade recente
import { useState } from "react";
import {
  ChevronDown,
  ArrowUpRight,
  ArrowDownLeft,
  TrendingUp,
  TrendingDown,
  PiggyBank,
  Plus,
  Send,
  CreditCard,
  Eye,
} from "lucide-react";

import { BalanceChart } from "../BalanceChart";

const ranges = ["Week", "Month", "Year"];

export const OverviewScreen = () => {
  // Período mostrado no gráfico de saldo.
  const [range, setRange] = useState("Week");

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 space-y-5 no-scrollbar">
      {/* ── 1. Saudação + data ─────────────────────────────────────────── */}
      <div className="flex items-end justify-between">
        <div>
          <p className="text-[12px] text-muted-foreground font-medium">Good morning, Alex</p>
          <h1 className="text-[22px] font-extrabold tracking-tight text-foreground leading-tight mt-0.5">
            Overview
          </h1>
        </div>
        <button className="text-[11.5px] font-semibold text-primary hover:underline">
          Apr 24, 2026
        </button>
      </div>

      {/* ── 2. Card hero do saldo ─────────────────────────────────────── */}
      <section className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-primary via-primary to-[hsl(265_70%_55%)] text-primary-foreground p-5 shadow-xl shadow-primary/25">
        {/* Bolhas de luz decorativas (blur) que dão profundidade ao gradiente. */}
        <div className="absolute -top-16 -right-12 w-48 h-48 rounded-full bg-white/10 blur-3xl" />
        <div className="absolute bottom-0 right-0 w-28 h-28 rounded-full bg-accent/40 blur-2xl" />

        <div className="relative flex items-start justify-between">
          <div>
            <div className="flex items-center gap-1.5">
              <p className="text-[10.5px] uppercase tracking-[0.12em] opacity-80 font-semibold">
                Total Balance
              </p>
              {/* Ícone de "olho" sugere que dá para ocultar/exibir o saldo. */}
              <Eye className="w-3 h-3 opacity-70" />
            </div>
            <p className="text-[32px] font-extrabold tracking-tight mt-1 leading-none tabular-nums">
              $14,872.50
            </p>
            <p className="text-[11.5px] opacity-80 mt-1.5">
              Available · <span className="font-semibold">$13,420.10</span>
            </p>
          </div>
          {/* Seletor de conta — visual mockado, ainda sem dropdown funcional. */}
          <button className="flex items-center gap-1 bg-white/15 hover:bg-white/25 backdrop-blur rounded-full px-2.5 py-1 text-[11px] font-semibold transition-colors">
            Purple Checking
            <ChevronDown className="w-3 h-3" strokeWidth={2.5} />
          </button>
        </div>

        {/* Pill de variação semanal. */}
        <div className="relative flex items-center gap-1.5 mt-3">
          <span className="inline-flex items-center gap-0.5 bg-success/90 rounded-full px-1.5 py-0.5 text-[10px] font-bold">
            <TrendingUp className="w-2.5 h-2.5" strokeWidth={3} /> 4.2%
          </span>
          <span className="text-[10.5px] opacity-80">vs last week</span>
        </div>
      </section>

      {/* ── 3. Atalhos rápidos ────────────────────────────────────────── */}
      {/* Os 4 cards são renderizados por map() — adicionar uma ação é só
          incluir mais um objeto na lista. */}
      <section className="grid grid-cols-4 gap-2">
        {[
          { icon: Send, label: "Transfer", color: "bg-surface-purple text-primary" },
          { icon: Plus, label: "Add Funds", color: "bg-surface-green text-success" },
          { icon: ArrowDownLeft, label: "Request", color: "bg-surface-yellow text-foreground" },
          { icon: CreditCard, label: "Pay Bill", color: "bg-surface-pink text-destructive" },
        ].map(({ icon: Icon, label, color }) => (
          <button
            key={label}
            className="flex flex-col items-center gap-1.5 active:scale-95 transition-transform group"
          >
            <div
              className={`w-12 h-12 rounded-2xl ${color} flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform`}
            >
              <Icon className="w-[18px] h-[18px]" strokeWidth={2.25} />
            </div>
            <span className="text-[10.5px] font-semibold text-foreground">{label}</span>
          </button>
        ))}
      </section>

      {/* ── 4. Card do gráfico ────────────────────────────────────────── */}
      <section className="card-soft p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Balance Trend</p>
            <p className="text-[12.5px] text-muted-foreground mt-0.5">Last 7 days · vs previous</p>
          </div>
          {/* Segmented control de período (Week / Month / Year). */}
          <div className="inline-flex bg-secondary rounded-full p-0.5">
            {ranges.map((r) => (
              <button
                key={r}
                onClick={() => setRange(r)}
                className={`px-3 py-1 text-[11.5px] font-semibold rounded-full transition-all ${
                  range === r ? "bg-card text-foreground shadow-sm" : "text-muted-foreground"
                }`}
              >
                {r}
              </button>
            ))}
          </div>
        </div>

        <div className="mt-3">
          <BalanceChart />
        </div>
      </section>

      {/* ── 5. Mini KPIs ──────────────────────────────────────────────── */}
      <section className="grid grid-cols-3 gap-2">
        {[
          {
            label: "Income", value: "$4,120", delta: "+8.4%", up: true,
            icon: ArrowUpRight, bg: "bg-surface-green", color: "text-success",
          },
          {
            label: "Expenses", value: "$2,780", delta: "-3.1%", up: false,
            icon: ArrowDownLeft, bg: "bg-surface-pink", color: "text-destructive",
          },
          {
            label: "Savings", value: "$1,340", delta: "+12%", up: true,
            icon: PiggyBank, bg: "bg-surface-purple", color: "text-primary",
          },
        ].map((k) => {
          // Renomeia para usar como componente JSX (precisa começar com maiúscula).
          const Icon = k.icon;
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3 hover:shadow-md transition-shadow">
              <div className={`w-7 h-7 rounded-lg ${k.bg} ${k.color} flex items-center justify-center`}>
                <Icon className="w-3.5 h-3.5" strokeWidth={2.5} />
              </div>
              <p className="text-[10.5px] text-muted-foreground font-semibold mt-2">{k.label}</p>
              <p className="text-[14.5px] font-extrabold text-foreground mt-0.5 tracking-tight tabular-nums">
                {k.value}
              </p>
              <div
                className={`flex items-center gap-0.5 mt-1 text-[10px] font-bold ${
                  k.up ? "text-success" : "text-destructive"
                }`}
              >
                <Trend className="w-2.5 h-2.5" strokeWidth={3} />
                {k.delta}
              </div>
            </div>
          );
        })}
      </section>

      {/* ── 6. Atividade recente ──────────────────────────────────────── */}
      <section>
        <div className="flex items-center justify-between mb-2">
          <p className="section-label">Recent Activity</p>
          <button className="text-[11.5px] font-semibold text-primary hover:underline">
            See all
          </button>
        </div>
        <div className="card-soft divide-y divide-border">
          {[
            { name: "Green Market", meta: "Groceries · Today", amt: "-$48.20", neg: true },
            { name: "Acme Corp", meta: "Salary · Apr 20", amt: "+$3,200.00", neg: false },
            { name: "Streamly", meta: "Subscription · Apr 18", amt: "-$12.99", neg: true },
          ].map((t, i) => (
            <div key={i} className="flex items-center gap-3 px-3.5 py-2.5 row-press">
              {/* Avatar simples com a primeira letra do nome do estabelecimento. */}
              <div className="w-8 h-8 rounded-full bg-secondary flex items-center justify-center text-[11px] font-bold text-foreground">
                {t.name[0]}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-[13px] font-semibold text-foreground truncate">{t.name}</p>
                <p className="text-[11px] text-muted-foreground">{t.meta}</p>
              </div>
              <p
                className={`text-[13px] font-bold tabular-nums ${
                  t.neg ? "text-destructive" : "text-success"
                }`}
              >
                {t.amt}
              </p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};
