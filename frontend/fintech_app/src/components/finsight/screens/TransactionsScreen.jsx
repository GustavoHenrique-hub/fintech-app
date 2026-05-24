// TransactionsScreen: lista de transações agrupadas por dia.
// Recursos:
//  - Header sticky (busca + chips de filtro permanecem fixos durante o scroll)
//  - Busca por texto (filtra pelo nome)
//  - Filtro por tipo (All / Income / Expenses / Subscriptions)
//  - Grupos colapsam automaticamente quando ficam vazios após filtrar
import { useState } from "react";
import {
  ShoppingCart, Briefcase, Wifi, Coffee, Car, Zap,
  SlidersHorizontal, Search, ChevronDown,
} from "lucide-react";

const filters = ["All", "Income", "Expenses", "Subscriptions"];

// Dados mockados: cada grupo representa uma janela temporal (Hoje, Ontem, etc.).
const txns = [
  {
    group: "Today · Apr 24",
    items: [
      { icon: Coffee, name: "Blue Bottle Coffee", category: "Dining",    date: "8:42 AM",  amount: -6.5,  bg: "bg-surface-pink",   color: "text-destructive", type: "expense" },
      { icon: ShoppingCart, name: "Green Market", category: "Groceries", date: "11:20 AM", amount: -48.2, bg: "bg-surface-purple", color: "text-primary",     type: "expense" },
    ],
  },
  {
    group: "Yesterday · Apr 23",
    items: [
      { icon: Briefcase, name: "Acme Corp", category: "Salary",    date: "9:00 AM", amount: 3200.0, bg: "bg-surface-green",  color: "text-success",     type: "income" },
      { icon: Car,       name: "Uber",      category: "Transport", date: "6:15 PM", amount: -14.3,  bg: "bg-surface-yellow", color: "text-foreground",  type: "expense" },
    ],
  },
  {
    group: "Earlier this week",
    items: [
      { icon: Wifi, name: "Streamly",  category: "Subscription", date: "Apr 18", amount: -12.99, bg: "bg-surface-purple", color: "text-primary",     type: "subscription" },
      { icon: Zap,  name: "Power Co.", category: "Utilities",    date: "Apr 17", amount: -84.4,  bg: "bg-surface-pink",   color: "text-destructive", type: "expense" },
    ],
  },
];

export const TransactionsScreen = () => {
  const [active, setActive] = useState("All");
  const [query, setQuery] = useState("");

  // Predicado de filtragem aplicado a cada item.
  const filterFn = (t) => {
    // Busca por nome (case-insensitive).
    if (query && !t.name.toLowerCase().includes(query.toLowerCase())) return false;
    if (active === "All") return true;
    if (active === "Income") return t.type === "income";
    if (active === "Expenses") return t.type === "expense";
    if (active === "Subscriptions") return t.type === "subscription";
    return true;
  };

  // Mostra um pontinho no botão "Filter" quando há filtro ativo.
  const activeFilters = active !== "All" || query;

  return (
    <div className="flex-1 overflow-y-auto pb-6 no-scrollbar">
      {/* ── Header sticky ───────────────────────────────────────────── */}
      <div className="sticky top-0 z-10 bg-background/95 backdrop-blur border-b border-border px-4 pt-4 pb-3">
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-[20px] font-extrabold tracking-tight text-foreground">Transactions</h1>
            <p className="text-[11.5px] text-muted-foreground mt-0.5">42 items · Last 30 days</p>
          </div>
          <button className="flex items-center gap-1 px-2.5 py-1.5 rounded-full border border-border text-[11.5px] font-semibold bg-card hover:bg-secondary transition-colors">
            <SlidersHorizontal className="w-3 h-3" strokeWidth={2.5} />
            Filter
            {activeFilters && <span className="w-1.5 h-1.5 bg-primary rounded-full" />}
          </button>
        </div>

        {/* Campo de busca controlado pelo state `query`. */}
        <div className="relative mt-3">
          <Search
            className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2"
            strokeWidth={2.25}
          />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search transactions..."
            className="w-full bg-secondary rounded-full pl-8 pr-3 py-2 text-[12.5px] outline-none placeholder:text-muted-foreground focus:ring-2 focus:ring-primary/30 transition-all"
          />
        </div>

        {/* Chips de filtro. `overflow-x-auto` permite scroll lateral em telas estreitas. */}
        <div className="flex gap-1.5 mt-3 overflow-x-auto no-scrollbar -mx-4 px-4">
          {filters.map((f) => (
            <button
              key={f}
              onClick={() => setActive(f)}
              className={`px-3 py-1 rounded-full text-[11.5px] font-semibold whitespace-nowrap border transition-all active:scale-95 ${
                active === f
                  ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                  : "bg-card border-border text-foreground hover:bg-secondary"
              }`}
            >
              {f}
            </button>
          ))}
        </div>
      </div>

      {/* ── Lista agrupada ──────────────────────────────────────────── */}
      <div className="px-4 mt-3 space-y-4">
        {txns.map(({ group, items }) => {
          const filtered = items.filter(filterFn);
          // Esconde o grupo inteiro quando nada passa pelo filtro.
          if (!filtered.length) return null;
          return (
            <div key={group}>
              <p className="section-label mb-1.5">{group}</p>
              <div className="card-soft divide-y divide-border">
                {filtered.map((t, i) => {
                  const Icon = t.icon;
                  const positive = t.amount > 0;
                  return (
                    <button
                      key={i}
                      className="w-full flex items-center gap-3 px-3.5 py-3 text-left row-press"
                    >
                      <div className={`w-10 h-10 rounded-full ${t.bg} flex items-center justify-center shrink-0`}>
                        <Icon className={`w-[17px] h-[17px] ${t.color}`} strokeWidth={2.25} />
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="font-semibold text-[13.5px] text-foreground truncate leading-tight">
                          {t.name}
                        </p>
                        <p className="text-[11px] text-muted-foreground mt-0.5">
                          {t.category} · {t.date}
                        </p>
                      </div>
                      <div className="text-right">
                        <p
                          className={`text-[13.5px] font-extrabold tabular-nums ${
                            positive ? "text-success" : "text-foreground"
                          }`}
                        >
                          {/* Sinal "+" para crédito, "−" (traço longo) para débito. */}
                          {positive ? "+" : "−"}${Math.abs(t.amount).toFixed(2)}
                        </p>
                        <p className="text-[10px] text-muted-foreground mt-0.5">USD</p>
                      </div>
                    </button>
                  );
                })}
              </div>
            </div>
          );
        })}

        {/* Botão "Carregar mais" — visual apenas (não está paginando ainda). */}
        <button className="w-full py-2.5 mt-2 rounded-xl border border-border bg-card text-[12.5px] font-semibold text-muted-foreground hover:bg-secondary transition-colors flex items-center justify-center gap-1">
          Load more <ChevronDown className="w-3.5 h-3.5" strokeWidth={2.5} />
        </button>
      </div>
    </div>
  );
};
