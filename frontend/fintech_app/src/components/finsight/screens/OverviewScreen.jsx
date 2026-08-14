import { useState, useMemo, useRef, useEffect } from "react";
import {
  ChevronDown, ArrowUpRight, ArrowDownLeft, TrendingUp, TrendingDown,
  PiggyBank, Plus, Eye, EyeOff, Check,
} from "lucide-react";

import { BalanceChart } from "../BalanceChart";
import { VincularBancoModal } from "../VincularBancoModal";
import { useUsuario } from "@/hooks/use-usuario";
import { useContaSelecionada } from "@/context/ContaSelecionadaContext";
import { useTransacoes } from "@/hooks/use-transacoes";
import { useCategorias } from "@/hooks/use-categorias";
import { useResumoPeriodo } from "@/hooks/use-resumo-periodo";
import { getIntervaloPeriodo } from "@/lib/periodo";
import { formatBRL, formatNumeroBR, formatBRLSigned, formatDataRelativa } from "@/lib/format";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { getBancoColor, getBancoLogoUrl } from "@/lib/banco-utils";
import { Button } from "@/components/ui/button";
import { Skeleton, SkeletonChart } from "@/components/ui/skeleton";

const ranges = ["Semana", "Mês", "Ano"];
const PERIODO_LABEL = { Semana: "7 dias", "Mês": "30 dias", Ano: "12 meses" };

// Tipo de conta → label legível
const TIPO_LABEL = {
  corrente:     "Conta corrente",
  poupanca:     "Poupança",
  cartao:       "Cartão",
  dinheiro:     "Dinheiro",
  investimento: "Investimento",
};

// Logo do banco com fallback para iniciais
function BancoLogo({ banco, size = 28 }) {
  const [erro, setErro] = useState(false);
  const url = getBancoLogoUrl(banco);
  const cor = getBancoColor(banco);

  if (!url || erro) {
    return (
      <div
        className="rounded-full flex items-center justify-center text-white font-extrabold shrink-0"
        style={{ width: size, height: size, backgroundColor: cor, fontSize: size * 0.38 }}
      >
        {(banco ?? "?")[0].toUpperCase()}
      </div>
    );
  }

  return (
    <img
      src={url}
      alt={banco}
      onError={() => setErro(true)}
      className="rounded-full object-contain bg-white shrink-0"
      style={{ width: size, height: size }}
    />
  );
}

// Dropdown de seleção de conta
function ContaSelector({ contas, contaSelecionada, onChange, onVincular }) {
  const [aberto, setAberto] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    const handler = (e) => { if (ref.current && !ref.current.contains(e.target)) setAberto(false); };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  // Sem contas: mostra botão de adicionar
  if (contas.length === 0) {
    return (
      <button onClick={onVincular} className="flex items-center gap-1.5 bg-white/15 hover:bg-white/25 backdrop-blur rounded-full px-2.5 py-1.5 text-[11.5px] font-semibold transition-colors">
        <Plus className="w-3.5 h-3.5" strokeWidth={2.5} />
        <span>Vincular banco</span>
      </button>
    );
  }

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setAberto((v) => !v)}
        className="flex items-center gap-1.5 bg-white/15 hover:bg-white/25 backdrop-blur rounded-full px-2.5 py-1.5 text-[11.5px] font-semibold transition-colors max-w-[160px]"
      >
        {contaSelecionada?.banco
          ? <BancoLogo banco={contaSelecionada.banco} size={18} />
          : <div className="w-[18px] h-[18px] rounded-full bg-white/30 shrink-0" />}
        <span className="truncate">{contaSelecionada?.banco ?? "Selecionar"}</span>
        <ChevronDown
          className={`w-3 h-3 shrink-0 transition-transform ${aberto ? "rotate-180" : ""}`}
          strokeWidth={2.5}
        />
      </button>

      {aberto && (
        <div className="absolute right-0 top-full mt-2 w-64 bg-card rounded-2xl shadow-xl border border-border z-[9999] overflow-y-auto max-h-[280px] py-1">
          {contas.map((c) => {
            const ativa = c.id === contaSelecionada?.id;
            return (
              <button
                key={c.id}
                onClick={() => { onChange(c); setAberto(false); }}
                className="w-full flex items-center gap-3 px-3.5 py-2.5 hover:bg-secondary transition-colors text-left"
              >
                <BancoLogo banco={c.banco} size={28} />
                <div className="flex-1 min-w-0">
                  <p className="text-[13px] font-semibold text-foreground truncate">{c.banco}</p>
                  <p className="text-[11px] text-muted-foreground truncate">
                    {TIPO_LABEL[c.tipo] ?? c.tipo}
                  </p>
                </div>
                {ativa && <Check className="w-4 h-4 text-primary shrink-0" strokeWidth={2.5} />}
                {c.padrao && !ativa && (
                  <span className="text-[9px] font-bold text-muted-foreground bg-secondary px-1.5 py-0.5 rounded-full">
                    padrão
                  </span>
                )}
              </button>
            );
          })}
          <button
            onClick={() => { setAberto(false); onVincular(); }}
            className="w-full flex items-center gap-3 px-3.5 py-2.5 hover:bg-secondary transition-colors text-left border-t border-border"
          >
            <div className="w-7 h-7 rounded-full bg-secondary flex items-center justify-center shrink-0">
              <Plus className="w-3.5 h-3.5 text-foreground" strokeWidth={2.5} />
            </div>
            <p className="text-[13px] font-semibold text-foreground">Adicionar banco</p>
          </button>
        </div>
      )}
    </div>
  );
}

export const OverviewScreen = ({ onNavigate }) => {
  const [range, setRange] = useState("Semana");
  const [saldoOculto, setSaldoOculto] = useState(false);
  const [modalBancoAberto, setModalBancoAberto] = useState(false);

  const { data: usuario, isLoading: loadingUsuario } = useUsuario();
  const { contas, contaAtual, setContaSelecionada, loadingContas } = useContaSelecionada();
  const { data: transacoes = [], isLoading: loadingTransacoes } = useTransacoes();
  const { data: categorias = [], isLoading: loadingCategorias } = useCategorias();

  // Resumo do mês corrente, escopado à conta ativa — alimenta o hero card e os KPIs.
  const intervaloMes = useMemo(() => getIntervaloPeriodo("Mês"), []);
  const { data: resumoMes, isLoading: loadingResumo } = useResumoPeriodo({
    conta: contaAtual, ...intervaloMes,
  });

  const isLoading =
    loadingUsuario || loadingContas || loadingTransacoes || loadingCategorias || loadingResumo;

  const categoriasPorId = useMemo(
    () => Object.fromEntries(categorias.map((c) => [c.id, c])),
    [categorias],
  );

  // Transações da conta ativa — base para atividade recente e para o gráfico.
  const transacoesDaConta = useMemo(
    () => transacoes.filter((t) => t.contaId === contaAtual?.id),
    [transacoes, contaAtual],
  );

  const totalReceitas = Number(resumoMes?.totalReceitas ?? 0);
  const totalGastos = Number(resumoMes?.totalGastos ?? 0);
  const economia = Number(contaAtual?.saldoEconomias ?? 0);

  const variacao = useMemo(() => {
    const base = totalReceitas || 1;
    return ((economia / base) * 100).toFixed(1);
  }, [totalReceitas, economia]);

  const recentes = useMemo(
    () => [...transacoesDaConta].sort((a, b) => (a.dataTransacao < b.dataTransacao ? 1 : -1)).slice(0, 3),
    [transacoesDaConta],
  );

  const primeiroNome = usuario?.nome?.split(" ")[0] ?? "";

  // Cor do card baseada na corHex da conta ou na cor da marca do banco
  const cardColor = useMemo(() => {
    if (contaAtual?.corHex) return contaAtual.corHex;
    return getBancoColor(contaAtual?.banco);
  }, [contaAtual]);

  // Cor secundária para o gradiente (versão mais escura/saturada)
  const cardColorEnd = useMemo(() => {
    return cardColor + "CC"; // adiciona 80% opacity para criar variação
  }, [cardColor]);

  if (isLoading) {
    return (
      <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-6 pb-6 lg:pb-8 no-scrollbar">
        <div className="max-w-6xl mx-auto w-full space-y-5">
          <Skeleton className="h-16 w-full rounded-3xl" />
          <Skeleton className="h-36 w-full rounded-3xl" />
          <div className="grid grid-cols-3 gap-2">
            {[0, 1, 2].map((i) => <Skeleton key={i} className="h-24 rounded-2xl" />)}
          </div>
          <SkeletonChart />
          <div className="card-soft divide-y divide-border">
            {[0, 1, 2].map((i) => <Skeleton key={i} className="h-14 rounded-none" />)}
          </div>
        </div>
      </div>
    );
  }

  return (
    <>
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-6 pb-6 lg:pb-8 no-scrollbar">
     <div className="max-w-6xl mx-auto w-full space-y-5 lg:space-y-4">

      {/* ── Saudação + data ─────────────────────────────────────────── */}
      <div className="flex items-end justify-between">
        <div>
          <p className="text-[12px] lg:text-[13px] text-muted-foreground font-medium">Olá, {primeiroNome}</p>
          <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight mt-0.5">
            Visão geral
          </h1>
        </div>
        <button className="text-[11.5px] lg:text-[13px] font-semibold text-primary hover:underline">
          {new Date().toLocaleDateString("pt-BR", { day: "2-digit", month: "short", year: "numeric" })}
        </button>
      </div>

      {/* ── Card hero do saldo ──────────────────────────────────────── */}
      <section
        className="relative rounded-3xl text-white p-5 lg:p-6 shadow-xl"
        style={{
          background: `linear-gradient(135deg, ${cardColor} 0%, ${cardColorEnd} 100%)`,
          boxShadow: `0 20px 40px -12px ${cardColor}55`,
        }}
      >
        {/* Blobs decorativos isolados para não bloquear o dropdown */}
        <div className="absolute inset-0 rounded-3xl overflow-hidden pointer-events-none">
          <div className="absolute -top-16 -right-12 w-48 h-48 rounded-full bg-white/10 blur-3xl" />
          <div className="absolute bottom-0 right-0 w-28 h-28 rounded-full bg-black/20 blur-2xl" />
        </div>

        {/* Logo do banco — canto superior direito, atrás do conteúdo */}
        {getBancoLogoUrl(contaAtual?.banco) && (
          <img
            src={getBancoLogoUrl(contaAtual.banco)}
            alt={contaAtual.banco}
            className="absolute top-4 right-[155px] w-10 h-10 rounded-xl object-contain bg-white/20 p-1.5 opacity-40 pointer-events-none"
            onError={(e) => { e.currentTarget.style.display = "none"; }}
          />
        )}

        <div className="relative flex items-start justify-between">
          <div>
            <div className="flex items-center gap-1.5">
              <p className="text-[10.5px] uppercase tracking-[0.12em] opacity-80 font-semibold">
                Saldo Total
              </p>
              <button
                onClick={() => setSaldoOculto((v) => !v)}
                className="opacity-70 hover:opacity-100 transition-opacity"
                aria-label={saldoOculto ? "Mostrar saldo" : "Ocultar saldo"}
              >
                {saldoOculto ? <EyeOff className="w-3 h-3" /> : <Eye className="w-3 h-3" />}
              </button>
            </div>
            <p className="text-[32px] lg:text-[40px] font-extrabold tracking-tight mt-1 leading-none tabular-nums">
              {saldoOculto
                ? "R$ ••••••"
                : `R$ ${formatNumeroBR((contaAtual?.saldoAtual ?? 0) + (contaAtual?.saldoEconomias ?? 0))}`}
            </p>
            <div className="flex flex-wrap items-center gap-x-2.5 gap-y-0 mt-1.5">
              <p className="text-[11.5px] opacity-80">
                Disponível ·{" "}
                <span className="font-semibold">
                  {saldoOculto ? "•••" : formatBRL(contaAtual?.saldoAtual ?? 0)}
                </span>
              </p>
              <span className="opacity-40 text-[11.5px]">·</span>
              <p className="text-[11.5px] opacity-80 flex items-center gap-1">
                <PiggyBank className="w-3 h-3" strokeWidth={2} />
                Guardado ·{" "}
                <span className="font-semibold">
                  {saldoOculto ? "•••" : formatBRL(contaAtual?.saldoEconomias ?? 0)}
                </span>
              </p>
            </div>
          </div>

          {/* Seletor de conta */}
          <ContaSelector
            contas={contas}
            contaSelecionada={contaAtual}
            onChange={setContaSelecionada}
            onVincular={() => setModalBancoAberto(true)}
          />
        </div>

        <div className="relative flex items-center gap-1.5 mt-3">
          <span className="inline-flex items-center gap-0.5 bg-white/20 rounded-full px-1.5 py-0.5 text-[10px] font-bold">
            <TrendingUp className="w-2.5 h-2.5" strokeWidth={3} /> {variacao}%
          </span>
          <span className="text-[10.5px] opacity-80">vs receita do mês</span>
        </div>
      </section>

      {/* KPIs horizontais */}
      <section className="grid grid-cols-3 gap-2 lg:gap-4">
        {[
          { label: "Receitas", value: totalReceitas, up: true,  icon: ArrowUpRight,  bg: "bg-surface-green",  color: "text-success" },
          { label: "Gastos",   value: totalGastos,    up: false, icon: ArrowDownLeft, bg: "bg-surface-pink",   color: "text-destructive" },
          { label: "Economia", value: economia,       up: true, icon: PiggyBank, bg: "bg-surface-purple", color: "text-primary" },
        ].map((k) => {
          const Icon = k.icon;
          const Trend = k.up ? TrendingUp : TrendingDown;
          return (
            <div key={k.label} className="card-soft p-3 lg:p-4 hover:shadow-md transition-shadow">
              <div className={`w-7 h-7 lg:w-9 lg:h-9 rounded-lg lg:rounded-xl ${k.bg} ${k.color} flex items-center justify-center`}>
                <Icon className="w-3.5 h-3.5 lg:w-4 lg:h-4" strokeWidth={2.5} />
              </div>
              <p className="text-[10.5px] lg:text-[12px] text-muted-foreground font-semibold mt-2">{k.label}</p>
              <p className="text-[13px] lg:text-[20px] font-extrabold text-foreground mt-0.5 tracking-tight tabular-nums">
                {formatBRL(k.value)}
              </p>
              <div className={`flex items-center gap-0.5 mt-1 text-[10px] lg:text-[11.5px] font-bold ${k.up ? "text-success" : "text-destructive"}`}>
                <Trend className="w-2.5 h-2.5" strokeWidth={3} />
                {k.up ? "+" : "−"}
                {Math.abs(((k.value / (totalReceitas || 1)) * 100)).toFixed(1)}%
              </div>
            </div>
          );
        })}
      </section>

      {/* ── Card do gráfico ────────────────────────────────────────── */}
      <section className="card-soft p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="section-label">Saldo nos últimos dias</p>
            <p className="text-[12.5px] text-muted-foreground mt-0.5">{PERIODO_LABEL[range]} · vs período anterior</p>
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
          <BalanceChart
            transacoes={transacoesDaConta}
            saldoFinal={Number(contaAtual?.saldoAtual ?? 0)}
            range={range}
          />
        </div>
      </section>

      {/* ── Atividade recente ──────────────────────────────────────── */}
      <section>
        <div className="flex items-center justify-between mb-2">
          <p className="section-label">Atividade recente</p>
          <Button variant="ghost" size="sm" onClick={() => onNavigate?.("payments")}>Ver tudo</Button>
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
                    {t.descricao ?? t.estabelecimento ?? "Lançamento"}
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
    </div>

    <VincularBancoModal
      open={modalBancoAberto}
      onOpenChange={setModalBancoAberto}
    />
    </>
  );
};
