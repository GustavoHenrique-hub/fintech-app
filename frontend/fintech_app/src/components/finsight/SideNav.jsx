// SideNav: barra lateral fixa exibida apenas em telas largas (lg+ = 1024px).
// Substitui a BottomNav em desktop, oferecendo um layout de "web app" real
// (estilo Linear/Notion/banco digital) com nav vertical e o botão "+" no topo.
import { Home, BarChart3, ArrowLeftRight, User, Plus, Wallet, RotateCcw } from "lucide-react";

import { usuarioAtual } from "@/mocks";
import { getInitials } from "@/lib/format";

const items = [
  { key: "home",      label: "Visão geral", icon: Home },
  { key: "analytics", label: "Análises",    icon: BarChart3 },
  { key: "payments",  label: "Transações",  icon: ArrowLeftRight },
  { key: "estorno",   label: "Estornos",    icon: RotateCcw },
  { key: "profile",   label: "Perfil",      icon: User },
];

export const SideNav = ({ active, onChange, onAdd }) => {
  const usuario = usuarioAtual;
  const iniciais = getInitials(usuario.nome);

  return (
    <aside className="hidden lg:flex sticky top-0 h-[100dvh] w-64 shrink-0 flex-col border-r border-border bg-card/60 backdrop-blur px-4 py-6">
      {/* Marca */}
      <div className="flex items-center gap-2 px-2">
        <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center shadow-sm shadow-primary/30">
          <Wallet className="w-[18px] h-[18px] text-primary-foreground" strokeWidth={2.5} />
        </div>
        <span className="font-extrabold text-[17px] tracking-tight text-foreground">FinSight</span>
      </div>

      {/* Botão de criar transação */}
      <button
        onClick={onAdd}
        className="mt-6 inline-flex items-center justify-center gap-2 h-10 rounded-xl bg-gradient-to-br from-primary to-accent text-primary-foreground text-[13px] font-bold shadow-md shadow-primary/30 hover:opacity-95 active:scale-[0.98] transition-all"
      >
        <Plus className="w-4 h-4" strokeWidth={2.75} />
        Nova transação
      </button>

      {/* Itens de navegação */}
      <nav className="mt-6 flex flex-col gap-1">
        {items.map(({ key, label, icon: Icon }) => {
          const isActive = active === key;
          return (
            <button
              key={key}
              onClick={() => onChange(key)}
              aria-current={isActive ? "page" : undefined}
              className={`flex items-center gap-3 px-3 h-10 rounded-xl text-[13.5px] font-semibold transition-colors ${
                isActive
                  ? "bg-primary/12 text-primary"
                  : "text-muted-foreground hover:bg-secondary hover:text-foreground"
              }`}
            >
              <Icon className="w-[18px] h-[18px]" strokeWidth={isActive ? 2.5 : 2} />
              {label}
            </button>
          );
        })}
      </nav>

      {/* Cartão do usuário fixo na base */}
      <div className="mt-auto flex items-center gap-3 p-3 rounded-2xl bg-secondary/60">
        <div className="w-9 h-9 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-[12px] font-bold text-primary-foreground shrink-0">
          {iniciais}
        </div>
        <div className="min-w-0">
          <p className="text-[12.5px] font-semibold text-foreground truncate">{usuario.nome}</p>
          <p className="text-[10.5px] text-muted-foreground truncate">{usuario.email}</p>
        </div>
      </div>
    </aside>
  );
};
