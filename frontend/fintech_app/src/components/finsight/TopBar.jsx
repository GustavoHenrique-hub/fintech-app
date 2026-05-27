// TopBar: cabeçalho fixo do app, presente em todas as telas.
// Usa o usuário "logado" (mock por enquanto) para mostrar iniciais no avatar.
import { Search, Sparkles, Bell, Wallet } from "lucide-react";

import { usuarioAtual } from "@/mocks";
import { getInitials } from "@/lib/format";

export const TopBar = () => {
  // Quando o backend estiver plugado, trocar para useQuery(["usuario", id]).
  const usuario = usuarioAtual;
  const iniciais = getInitials(usuario.nome);

  return (
    <header
      className="sticky top-0 z-30 px-4 sm:px-5 pb-3 bg-card/95 backdrop-blur-md border-b border-border/60"
      style={{ paddingTop: "calc(env(safe-area-inset-top, 0px) + 0.75rem)" }}
    >
      <div className="flex items-center gap-2 sm:gap-3 lg:gap-4 max-w-7xl mx-auto w-full">
        {/* Marca: visível em mobile/tablet. Em lg+ está na SideNav. */}
        <div className="flex items-center gap-1.5 shrink-0 lg:hidden">
          <div className="w-7 h-7 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center shadow-sm shadow-primary/30">
            <Wallet className="w-[15px] h-[15px] text-primary-foreground" strokeWidth={2.5} />
          </div>
          <span className="font-extrabold text-[15px] tracking-tight text-foreground">FinSight</span>
        </div>

        {/* Busca: ocupa o espaço restante. Mais larga em desktop. */}
        <div className="flex-1 mx-1 relative max-w-xl">
          <Search
            className="w-3.5 h-3.5 lg:w-4 lg:h-4 text-muted-foreground absolute left-2.5 lg:left-3 top-1/2 -translate-y-1/2"
            strokeWidth={2.25}
          />
          <input
            placeholder="Buscar"
            aria-label="Buscar"
            className="w-full bg-secondary/70 rounded-full pl-7 lg:pl-9 pr-2.5 py-1.5 sm:py-2 lg:py-2.5 text-[12px] sm:text-[13px] lg:text-[14px] outline-none placeholder:text-muted-foreground focus:bg-secondary focus:ring-2 focus:ring-primary/30 transition-all"
          />
        </div>

        {/* CTA de upgrade — escondido em telas muito estreitas. */}
        <button className="hidden xs:inline-flex items-center gap-1 px-2.5 lg:px-3 py-1.5 lg:py-2 rounded-full bg-accent text-accent-foreground text-[11.5px] lg:text-[12.5px] font-bold shadow-sm hover:opacity-90 active:scale-95 transition-all">
          <Sparkles className="w-3 h-3 lg:w-3.5 lg:h-3.5" strokeWidth={2.5} />
          Premium
        </button>

        {/* Sino de notificações com pontinho indicando não-lidas. */}
        <button
          className="relative w-8 h-8 lg:w-9 lg:h-9 rounded-full bg-secondary flex items-center justify-center hover:bg-muted active:scale-95 transition-all"
          aria-label="Notificações"
        >
          <Bell className="w-[15px] h-[15px] lg:w-[17px] lg:h-[17px] text-foreground" strokeWidth={2.25} />
          <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 bg-destructive rounded-full ring-2 ring-card" />
        </button>

        {/* Avatar com iniciais — escondido em lg+ (já está na SideNav). */}
        <div
          className="lg:hidden w-8 h-8 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-[11px] font-bold text-primary-foreground shrink-0 shadow-sm"
          aria-label={`Avatar de ${usuario.nome}`}
        >
          {iniciais}
        </div>
      </div>
    </header>
  );
};
