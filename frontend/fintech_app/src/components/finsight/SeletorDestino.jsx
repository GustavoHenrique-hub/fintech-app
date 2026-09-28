// Seletor Gasto / Receita / Economias usado na revisão de lançamentos importados
// (modal do extrato e detalhe da transação).
import { ArrowUpRight, ArrowDownLeft, PiggyBank } from "lucide-react";

const DESTINOS = [
  { key: "GASTO", label: "Gasto", icon: ArrowUpRight, cor: "text-destructive" },
  { key: "RECEITA", label: "Receita", icon: ArrowDownLeft, cor: "text-success" },
  { key: "ECONOMIA", label: "Economias", icon: PiggyBank, cor: "text-primary" },
];

export function SeletorDestino({ value, onChange, disabled }) {
  return (
    <div className="grid grid-cols-3 gap-1 p-1 bg-secondary rounded-xl">
      {DESTINOS.map(({ key, label, icon: Icone, cor }) => (
        <button
          key={key}
          type="button"
          disabled={disabled}
          onClick={() => onChange(key)}
          aria-pressed={value === key}
          className={`flex items-center justify-center gap-1 py-1.5 rounded-lg text-[11.5px] font-semibold transition-all disabled:opacity-50 ${
            value === key ? `bg-card ${cor} shadow-sm` : "text-muted-foreground"
          }`}
        >
          <Icone className="w-3.5 h-3.5" strokeWidth={2.5} /> {label}
        </button>
      ))}
    </div>
  );
}
