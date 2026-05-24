// AddTransactionScreen: formulário para registrar uma nova transação.
// Seções (de cima pra baixo):
//   1. Header (título + Cancelar)
//   2. Segmented control Expense ↔ Income (muda a cor do valor)
//   3. Campo de valor com validação simples
//   4. Grid de categorias (grupo de "rádio" visual)
//   5. Data + nota opcional
//   6. CTAs (Salvar / Salvar como rascunho)
import { useState } from "react";
import {
  UtensilsCrossed, Bus, Film, Heart, Calendar, ShoppingBag, Zap,
  MoreHorizontal, ArrowUpRight, ArrowDownLeft, Check,
} from "lucide-react";

// Lista de categorias — usar `key` interno para identificar a selecionada.
const categories = [
  { key: "food",          label: "Food",      icon: UtensilsCrossed, color: "text-destructive",     bg: "bg-surface-pink" },
  { key: "transport",     label: "Transport", icon: Bus,             color: "text-primary",         bg: "bg-surface-purple" },
  { key: "shopping",      label: "Shopping",  icon: ShoppingBag,     color: "text-foreground",      bg: "bg-surface-yellow" },
  { key: "entertainment", label: "Fun",       icon: Film,            color: "text-primary",         bg: "bg-surface-purple" },
  { key: "health",        label: "Health",    icon: Heart,           color: "text-success",         bg: "bg-surface-green" },
  { key: "bills",         label: "Bills",     icon: Zap,             color: "text-destructive",     bg: "bg-surface-pink" },
  { key: "other",         label: "Other",     icon: MoreHorizontal,  color: "text-muted-foreground", bg: "bg-secondary" },
];

export const AddTransactionScreen = () => {
  // Estados do formulário.
  const [type, setType] = useState("expense"); // "expense" | "income"
  const [cat, setCat] = useState("food");
  const [amount, setAmount] = useState("");
  // `touched` evita mostrar erro antes do usuário interagir com o campo.
  const [touched, setTouched] = useState(false);

  // Erro = campo tocado E valor inválido / vazio / <= 0.
  const showError =
    touched && (!amount || isNaN(Number(amount)) || Number(amount) <= 0);

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 no-scrollbar">
      {/* ── Header ──────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-[20px] font-extrabold tracking-tight text-foreground">New Transaction</h1>
          <p className="text-[11.5px] text-muted-foreground mt-0.5">Log income or expense</p>
        </div>
        <button className="text-[12px] font-semibold text-muted-foreground hover:text-foreground transition-colors">
          Cancel
        </button>
      </div>

      {/* ── Tipo (Expense / Income) ────────────────────────────────── */}
      <div className="mt-4 grid grid-cols-2 gap-1 p-1 bg-secondary rounded-2xl">
        <button
          onClick={() => setType("expense")}
          className={`flex items-center justify-center gap-1.5 py-2 rounded-xl text-[12.5px] font-semibold transition-all ${
            type === "expense" ? "bg-card text-destructive shadow-sm" : "text-muted-foreground"
          }`}
        >
          <ArrowUpRight className="w-3.5 h-3.5" strokeWidth={2.5} /> Expense
        </button>
        <button
          onClick={() => setType("income")}
          className={`flex items-center justify-center gap-1.5 py-2 rounded-xl text-[12.5px] font-semibold transition-all ${
            type === "income" ? "bg-card text-success shadow-sm" : "text-muted-foreground"
          }`}
        >
          <ArrowDownLeft className="w-3.5 h-3.5" strokeWidth={2.5} /> Income
        </button>
      </div>

      {/* ── Campo de valor (hero) ───────────────────────────────────── */}
      <div className="card-soft p-5 mt-4 text-center">
        <p className="section-label">Amount</p>
        <div className="flex items-center justify-center mt-2">
          <span
            className={`text-[28px] font-extrabold ${
              type === "expense" ? "text-destructive" : "text-success"
            } mr-1`}
          >
            {type === "expense" ? "−" : "+"}$
          </span>
          {/* Sanitização inline: remove tudo que não for dígito ou ponto. */}
          <input
            value={amount}
            onChange={(e) => setAmount(e.target.value.replace(/[^0-9.]/g, ""))}
            onBlur={() => setTouched(true)}
            placeholder="0.00"
            inputMode="decimal"
            className="w-[160px] bg-transparent text-[34px] font-extrabold tracking-tight text-foreground outline-none placeholder:text-muted-foreground/40 tabular-nums"
          />
        </div>
        {showError ? (
          <p className="text-[11.5px] text-destructive mt-1 font-medium">
            Enter a valid amount greater than 0
          </p>
        ) : (
          <p className="text-[11px] text-muted-foreground mt-1">USD · From Purple Checking</p>
        )}
      </div>

      {/* ── Categorias (grid 4 cols) ────────────────────────────────── */}
      <div className="mt-5">
        <div className="flex items-center justify-between mb-2">
          <p className="section-label">Category</p>
          <span className="text-[11px] text-muted-foreground">
            {categories.find((c) => c.key === cat)?.label}
          </span>
        </div>
        <div className="grid grid-cols-4 gap-2">
          {categories.map(({ key, label, icon: Icon, color, bg }) => {
            const active = cat === key;
            return (
              <button
                key={key}
                onClick={() => setCat(key)}
                className={`relative flex flex-col items-center gap-1 py-2.5 rounded-2xl border transition-all active:scale-95 ${
                  active
                    ? "bg-card border-primary shadow-sm shadow-primary/10 ring-2 ring-primary/20"
                    : "bg-card border-border hover:bg-secondary"
                }`}
              >
                {/* Check no canto superior quando selecionada. */}
                {active && (
                  <span className="absolute top-1 right-1 w-3.5 h-3.5 rounded-full bg-primary text-primary-foreground flex items-center justify-center">
                    <Check className="w-2 h-2" strokeWidth={3} />
                  </span>
                )}
                <div className={`w-7 h-7 rounded-lg ${bg} flex items-center justify-center`}>
                  <Icon className={`w-[15px] h-[15px] ${color}`} strokeWidth={2.25} />
                </div>
                <span className="text-[10.5px] font-semibold text-foreground">{label}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* ── Detalhes (Data + Nota) ──────────────────────────────────── */}
      <div className="mt-5 card-soft p-1 divide-y divide-border">
        <div className="flex items-center px-3.5 py-2.5">
          <Calendar className="w-4 h-4 text-muted-foreground mr-2.5" />
          <span className="text-[12.5px] text-muted-foreground flex-1">Date</span>
          <span className="text-[12.5px] font-semibold text-foreground">Apr 24, 2026</span>
        </div>
        <div className="px-3.5 py-2.5">
          <label className="text-[10.5px] font-semibold text-muted-foreground uppercase tracking-wider">
            Note (optional)
          </label>
          <input
            placeholder="Add a short description"
            className="w-full mt-1 bg-transparent text-[13px] text-foreground outline-none placeholder:text-muted-foreground/60"
          />
        </div>
      </div>

      {/* ── CTAs ────────────────────────────────────────────────────── */}
      <div className="mt-6 space-y-2">
        <button className="w-full py-3 rounded-2xl bg-gradient-to-r from-primary to-accent text-primary-foreground text-[14px] font-bold shadow-lg shadow-primary/25 hover:opacity-95 active:scale-[0.99] transition-all">
          Save Transaction
        </button>
        <button className="w-full py-2.5 rounded-2xl bg-secondary text-[12.5px] font-semibold text-muted-foreground hover:bg-muted transition-colors">
          Save as Draft
        </button>
      </div>
    </div>
  );
};
