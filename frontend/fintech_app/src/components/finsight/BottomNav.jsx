// BottomNav: barra de navegação inferior + botão flutuante "+" central.
//
// Props:
//  - active    : chave da aba atual ("home" | "analytics" | "payments" | "profile")
//  - onChange  : callback ao trocar de aba
//  - onAdd     : callback do botão central (criar transação)
import { Home, BarChart3, ArrowLeftRight, User, Plus } from "lucide-react";

// Lista declarativa de abas — adicionar uma nova exige só uma entrada aqui.
const items = [
  { key: "home", label: "Home", icon: Home },
  { key: "analytics", label: "Analytics", icon: BarChart3 },
  { key: "payments", label: "Payments", icon: ArrowLeftRight },
  { key: "profile", label: "Profile", icon: User },
];

export const BottomNav = ({ active, onChange, onAdd }) => {
  return (
    <nav className="relative border-t border-border/70 bg-card/95 backdrop-blur-md px-2 pt-2 pb-3">
      {/* Botão "+" central — posicionado por cima da barra usando absolute. */}
      {onAdd && (
        <button
          onClick={onAdd}
          aria-label="Add transaction"
          className="absolute -top-6 left-1/2 -translate-x-1/2 w-12 h-12 rounded-full bg-gradient-to-br from-primary to-accent text-primary-foreground flex items-center justify-center shadow-lg shadow-primary/40 ring-4 ring-card active:scale-95 hover:scale-105 transition-transform z-10"
        >
          <Plus className="w-5 h-5" strokeWidth={2.75} />
        </button>
      )}

      {/* Grid de 4 colunas — os ícones se distribuem igualmente. */}
      <div className="grid grid-cols-4">
        {items.map(({ key, label, icon: Icon }, idx) => {
          const isActive = active === key;
          // Ao redor do botão flutuante adicionamos margem extra para
          // visualmente abrir espaço para o "+".
          const isMiddleSpacer = idx === 2 && onAdd;
          return (
            <button
              key={key}
              onClick={() => onChange(key)}
              className={`flex flex-col items-center gap-0.5 py-1 transition-all active:scale-95 ${
                isMiddleSpacer ? "ml-3" : ""
              }`}
            >
              {/* "Pílula" ao redor do ícone destaca a aba ativa. */}
              <div
                className={`flex items-center justify-center w-9 h-6 rounded-full transition-all ${
                  isActive ? "bg-primary/15" : ""
                }`}
              >
                <Icon
                  className={`w-[19px] h-[19px] transition-colors ${
                    isActive ? "text-primary" : "text-muted-foreground"
                  }`}
                  strokeWidth={isActive ? 2.5 : 2}
                />
              </div>
              <span
                className={`text-[10.5px] font-semibold transition-colors ${
                  isActive ? "text-primary" : "text-muted-foreground"
                }`}
              >
                {label}
              </span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
