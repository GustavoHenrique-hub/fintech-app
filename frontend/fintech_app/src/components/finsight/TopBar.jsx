// TopBar: cabeçalho fixo do app, presente em todas as telas.
// Usa o usuário "logado" (mock por enquanto) para mostrar iniciais no avatar.
import { useEffect, useRef, useState } from "react";
import { Search, Sparkles, Bell, Wallet } from "lucide-react";

import { usuarioAtual } from "@/mocks";
import { getInitials } from "@/lib/format";

// Telas navegáveis pela busca — mesmas chaves usadas pelo state `screen` do Index.
const TELAS = [
  { key: "home", label: "Visão geral" },
  { key: "analytics", label: "Análises" },
  { key: "payments", label: "Transações" },
  { key: "estorno", label: "Estornos" },
  { key: "add", label: "Nova transação" },
  { key: "profile", label: "Perfil" },
];

function normalize(s) {
  return (s ?? "")
    .toString()
    .toLowerCase()
    .normalize("NFD")
    .replace(/[̀-ͯ]/g, "");
}

export const TopBar = ({ onNavigate }) => {
  // Quando o backend estiver plugado, trocar para useQuery(["usuario", id]).
  const usuario = usuarioAtual;
  const iniciais = getInitials(usuario.nome);

  const [query, setQuery] = useState("");
  const [aberto, setAberto] = useState(false);
  const buscaRef = useRef(null);

  useEffect(() => {
    const handler = (e) => {
      if (buscaRef.current && !buscaRef.current.contains(e.target)) setAberto(false);
    };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  const sugestoes = query.trim()
    ? TELAS.filter((t) => normalize(t.label).includes(normalize(query)))
    : TELAS;

  const navegar = (key) => {
    onNavigate?.(key);
    setQuery("");
    setAberto(false);
  };

  const onKeyDown = (e) => {
    if (e.key === "Escape") setAberto(false);
    if (e.key === "Enter" && sugestoes[0]) navegar(sugestoes[0].key);
  };

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
        <div ref={buscaRef} className="flex flex-col mx-1 relative max-w-100% w-full">
          <Search
            className="w-3.5 h-3.5 lg:w-4 lg:h-4 text-muted-foreground absolute left-2.5 lg:left-3 top-1/2 -translate-y-1/2"
            strokeWidth={2.25}
          />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onFocus={() => setAberto(true)}
            onKeyDown={onKeyDown}
            placeholder="Buscar"
            aria-label="Buscar"
            className="w-full bg-secondary/70 rounded-full pl-7 lg:pl-9 pr-2.5 py-1.5 sm:py-2 lg:py-2.5 text-[12px] sm:text-[13px] lg:text-[14px] outline-none placeholder:text-muted-foreground focus:bg-secondary focus:ring-2 focus:ring-primary/30 transition-all"
          />

          {aberto && sugestoes.length > 0 && (
            <div className="absolute left-0 right-0 top-full mt-1.5 rounded-2xl border border-border bg-card shadow-xl z-[9999] overflow-hidden py-1">
              {sugestoes.map((t) => (
                <button
                  key={t.key}
                  type="button"
                  onMouseDown={(e) => { e.preventDefault(); navegar(t.key); }}
                  className="w-full flex items-center gap-2.5 px-3.5 py-2 text-left text-[13px] font-medium text-foreground hover:bg-secondary transition-colors"
                >
                  <Search className="w-3.5 h-3.5 text-muted-foreground shrink-0" strokeWidth={2.25} />
                  {t.label}
                </button>
              ))}
            </div>
          )}
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
