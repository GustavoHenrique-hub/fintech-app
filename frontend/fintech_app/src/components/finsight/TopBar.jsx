// TopBar: cabeçalho fixo do app, presente em todas as telas.
// Contém o logotipo, busca, botão de upgrade, sino de notificações e avatar.
import { Search, Sparkles, Bell, Wallet } from "lucide-react";

export const TopBar = () => {
  return (
    <header className="px-4 pt-7 pb-3 bg-card/95 backdrop-blur-md border-b border-border/60">
      <div className="flex items-center gap-2">
        {/* Bloco do logo: ícone gradiente + nome da marca. */}
        <div className="flex items-center gap-1.5 shrink-0">
          <div className="w-7 h-7 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center shadow-sm shadow-primary/30">
            <Wallet className="w-[15px] h-[15px] text-primary-foreground" strokeWidth={2.5} />
          </div>
          <span className="font-extrabold text-[15px] tracking-tight text-foreground">FinSight</span>
        </div>

        {/* Campo de busca — `flex-1` faz ele ocupar todo o espaço restante. */}
        <div className="flex-1 mx-1 relative">
          <Search
            className="w-3.5 h-3.5 text-muted-foreground absolute left-2.5 top-1/2 -translate-y-1/2"
            strokeWidth={2.25}
          />
          <input
            placeholder="Search"
            className="w-full bg-secondary/70 rounded-full pl-7 pr-2.5 py-1.5 text-[12px] outline-none placeholder:text-muted-foreground focus:bg-secondary focus:ring-2 focus:ring-primary/30 transition-all"
          />
        </div>

        {/* Botão "Upgrade" só aparece em telas extra pequenas pra cima. */}
        <button className="hidden xs:inline-flex items-center gap-1 px-2.5 py-1.5 rounded-full bg-accent text-accent-foreground text-[11.5px] font-bold shadow-sm hover:opacity-90 active:scale-95 transition-all">
          <Sparkles className="w-3 h-3" strokeWidth={2.5} />
          Upgrade
        </button>

        {/* Sino com badge de notificação não lida (pontinho vermelho). */}
        <button className="relative w-8 h-8 rounded-full bg-secondary flex items-center justify-center hover:bg-muted active:scale-95 transition-all">
          <Bell className="w-[15px] h-[15px] text-foreground" strokeWidth={2.25} />
          <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 bg-destructive rounded-full ring-2 ring-card" />
        </button>

        {/* Avatar do usuário (iniciais). Aqui está mockado como "AM". */}
        <div className="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-[11px] font-bold text-primary-foreground shrink-0 shadow-sm">
          AM
        </div>
      </div>
    </header>
  );
};
