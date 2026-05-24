// OverviewScreen ("Home"): consome usuario + contaPadrao + snapshot do mês + transações recentes.
//
// Onde plugar com o backend (depois):
//   - usuarioAtual    → GET /usuarios/{id}                      (UsuarioResponseDTO)
//   - contaPadrao     → GET /contas-financeiras/usuario/{id}    (lista, filtrar padrao=true)
//   - snapshotAtual   → GET /snapshots/usuario/{id}/{conta}     (lista, escolher ano+mes corrente)
//   - transacoesRecentes → GET /transacoes/usuario/{id}?limit=3 (TransacaoResponseDTO[])
import { useState, useMemo } from "react";
import {
  ChevronDown, ArrowUpRight, ArrowDownLeft, TrendingUp, TrendingDown,
  PiggyBank, Plus, Send, CreditCard, Eye, EyeOff,
} from "lucide-react";

import { BalanceChart } from "../BalanceChart";
import { usuarioAtual, contaPadrao, snapshotAtual, transacoes, categoriasPorId } from "@/mocks";
import { formatBRL, formatNumeroBR, formatBRLSigned, formatDataRelativa, formatHora, getInitials } from "@/lib/format";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";

const ranges = ["Semana", "Mês", "Ano"];

export const OverviewScreen = () => {
  const [range, setRange] = useState("Semana");
  const [saldoOculto, setSaldoOculto] = useState(false);

  // Sumário derivado do snapshot atual.
  const variacao = useMemo(() => {
    const { totalReceitas, totalGastos } = snapshotAtual;
    const net = totalReceitas - totalGastos;
    const base = totalReceitas || 1;
    return ((net / base) * 100).toFixed(1);
  }, []);

  // Últimas 3 transações pra teaser de "atividade recente".
  const recentes = useMemo(
    () =>
      [...transacoes]
        .sort((a, b) => (a.dataTransacao < b.dataTransacao ? 1 : -1))
        .slice(0, 3),
    [],
  );

  const primeiroNome = usuarioAtual.nome.split(" ")[0];

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 space-y-5 no-scrollbar">
      {/* ── Saudação + data ─────────────────────────────────────────── */}
      <div className="flex items-end justify-between">
        <div>
          <p className="text-[12px] text-muted-foreground font-medium">Olá, {primeiroNome}</p>
          <h1 className="text-[22px] font-extrabold tracking-tight text-foreground leading-tight mt-0.5">
            Visão geral
          </h1>
        </div>
        <button className="text-[11.5px] font-semibold text-primary hover:underline">
          {new Date().toLocaleDateString("pt-BR", { day: "2-digit", month: "short", year: "numeric" })}
        </button>
      </div>

      {/* ── Card hero do saldo ─────────────────────────────────────── */}
      <section className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-primary via-primary to-[hsl(265_70%_55%)] text-primary-foreground p-5 shadow-xl shadow-primary/25">
        {/* Bolhas decorativas (blur) — só visual. */}
        <div className="absolute -top-16 -right-12 w-48 h-48 rounded-full bg-white/10 blur-3xl" />
        <div className="absolute bottom-0 right-0 w-28 h-28 rounded-full bg-accent/40 blur-2xl" />

        <div className="relative flex items-start justify-between">
          <div>
            <div className="flex items-center gap-1.5">
              <p className="text-[10.5px] uppercase tracking-[0.12em] opacity-80 font-semibold">
                Saldo Atual
              </p>
              <button
                onClick={() => setSaldoOculto((v) => !v)}
                className="opacity-70 hover:opacity-100 transition-opacity"
                aria-label={saldoOculto ? "Mostrar saldo" : "Ocultar saldo"}
              >
                {saldoOculto ? <EyeOff className="w-3 h-3" /> : <Eye className="w-3 h-3" />}
              </button>
            </div>
            <p className="text-[32px] font-extrabold tracking-tight mt-1 leading-none tabular-nums">
              {saldoOculto ? "R$ ••••••" : `R$ ${formatNumeroBR(snapshotAtual.saldoFinal)}`}
            </p>
            <p className="text-[11.5px] opacity-80 mt-1.5">
              Disponível ·{" "}
              <span className="font-semibold">
                {saldoOculto ? "•••" : formatBRL(snapshotAtual.saldoFinal - 1200)}
              </span>
            </p>
          </div>
          {/* Seletor de conta (mock — abriria lista no app real). */}
          <button className="flex items-center gap-1 bg-white/15 hover:bg-white/25 backdrop-blur rounded-full px-2.5 py-1 text-[11px] font-semibold transition-colors max-w-[140px]">
            <span className="truncate">{contaPadrao.banco}</span>
            <ChevronDown className="w-3 h-3 shrink-0" strokeWidth={2.5} />
          </button>
        </div>

        {/* Pill de variação no mês. */}
        <div className="relative flex items-center gap-1.5 mt-3">
          <span className="inline-flex items-center gap-0.5 bg-success/90 rounded-full px-1.5 py-0.5 text-[10px] font-bold">
            <TrendingUp className="w-2.5 h-2.5" strokeWidth={3} /> {variacao}%
          </span>
          <span className="text-[10.5px] opacity-80">vs receita do mês</span>
        </div>
      </section>

      {/* ── Atalhos rápidos ────────────────────────────────────────── */}
      <section className="grid grid-cols-4 gap-2">
        {[
          { icon: Send, label: "Transferir", color: "bg-surface-purple text-primary" },
          { icon: Plus, label: "Receber", color: "bg-surface-green text-success" },
          { icon: ArrowDownLeft, label: "Cobrar", color: "bg-surface-yellow text-foreground" },
          { icon: CreditCard, label: "Pagar", color: "bg-surface-pink text-destructive" },
        ].map(({ icon: Icon, label, color }) => (
          <button
            key={label}
            className="flex flex-col items-center gap-1.5 active:scale-95 transition-transform group"
          >
            <div className={`w-12 h-12 rounded-2xl ${color} flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform`}>
              <Icon className="w-[18px] h-[18px]" strokeWidth={2.25} />
            </div>
            <span className="text-[10.5px] font-semibold text-foreground">{label}</span>
          </button>
        ))}
      </section>

      {/* ── Card do gráfico ────────────────────────────────────────── */}
      <section className="card-soft p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Saldo nos últimos dias</p>
            <p className="text-[12.5px] text-muted-foreground mt-0.5">7 dias · vs período anterior</p>
          </div>
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

      {/* ── KPIs do mês (vindos do snapshot) ───────────────────────── */}
      <section className="grid grid-cols-3 gap-2">
        {[
          { label: "Receitas",  value: snapshotAtual.totalReceitas, up: true,  icon: ArrowUpRight,    bg: "bg-surface-green",  color: "text-success" },
          { label: "Gastos",    value: snapshotAtual.totalGastos,   up: false, icon: ArrowDownLeft,   bg: "bg-surface-pink",   color: "text-destructive" },
          { label: "Economia",  value: snapshotAtual.totalReceitas - snapshotAtual.totalGastos, up: true, icon: PiggyBank, bg: "bg-surface-purple", color: "text-primary" },
        ].map((k) => {
          const Icon = k.icon;
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3 hover:shadow-md transition-shadow">
              <div className={`w-7 h-7 rounded-lg ${k.bg} ${k.color} flex items-center justify-center`}>
                <Icon className="w-3.5 h-3.5" strokeWidth={2.5} />
              </div>
              <p className="text-[10.5px] text-muted-foreground font-semibold mt-2">{k.label}</p>
              <p className="text-[13px] font-extrabold text-foreground mt-0.5 tracking-tight tabular-nums">
                {formatBRL(k.value)}
              </p>
              <div className={`flex items-center gap-0.5 mt-1 text-[10px] font-bold ${k.up ? "text-success" : "text-destructive"}`}>
                <Trend className="w-2.5 h-2.5" strokeWidth={3} />
                {k.up ? "+" : "−"}
                {Math.abs(((k.value / (snapshotAtual.totalReceitas || 1)) * 100)).toFixed(1)}%
              </div>
            </div>
          );
        })}
      </section>

      {/* ── Atividade recente ──────────────────────────────────────── */}
      <section>
        <div className="flex items-center justify-between mb-2">
          <p className="section-label">Atividade recente</p>
          <Button variant="ghost" size="sm">Ver tudo</Button>
        </div>
        <div className="card-soft divide-y divide-border">
          {recentes.map((t) => {
            const categoria = categoriasPorId[t.categoriaId];
            const Icone = getIconeCategoria(categoria?.icone);
            const positivo = t.tipo === "RECEITA";
            return (
              <div key={t.id} className="flex items-center gap-3 px-3.5 py-2.5 row-press">
                <div
                  className="w-8 h-8 rounded-full flex items-center justify-center shrink-0"
                  style={{ backgroundColor: `${categoria?.corHex ?? "#94a3b8"}22` }}
                >
                  <Icone
                    className="w-4 h-4"
                    strokeWidth={2.25}
                    style={{ color: categoria?.corHex ?? "var(--foreground)" }}
                  />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-[13px] font-semibold text-foreground truncate">
                    {t.descricaoUsuario ?? t.estabelecimento ?? t.descricaoNormalizada}
                  </p>
                  <p className="text-[11px] text-muted-foreground">
                    {categoria?.nome ?? "Sem categoria"} · {formatDataRelativa(t.dataTransacao)}
                  </p>
                </div>
                <p className={`text-[13px] font-bold tabular-nums ${positivo ? "text-success" : "text-destructive"}`}>
                  {formatBRLSigned(positivo ? t.valor : -t.valor)}
                </p>
              </div>
            );
          })}
        </div>
      </section>
    </div>
  );
};
