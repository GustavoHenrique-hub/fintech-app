// Combobox: input com busca + lista filtrada (dropdown).
// Suporta categorias hierárquicas (pai → filho) via prop `parentId` no item.
//
// API:
//   <Combobox
//     items={[
//       { id: "1", label: "Alimentação" },
//       { id: "2", label: "Restaurantes", parentId: "1" },
//       { id: "3", label: "Mercado",      parentId: "1" },
//       { id: "4", label: "Transporte" },
//     ]}
//     value={selectedId}
//     onChange={(id) => ...}
//     placeholder="Selecione uma categoria"
//   />
//
// Decisões:
//  - Implementado em puro React + acessibilidade básica (role=combobox/listbox).
//  - Itens filhos renderizam com indentação (com base em parentId).
//  - Busca é case-insensitive e ignora acentos.
//  - Fecha ao clicar fora (listener no document) e ao pressionar Escape.
//  - Setas ↑ ↓ navegam a lista, Enter seleciona.
import * as React from "react";
import { ChevronDown, Check, Search, X } from "lucide-react";

import { cn } from "@/lib/utils";

// Remove acentos para que "alimentacao" encontre "Alimentação".
function normalize(str) {
  return (str ?? "")
    .toString()
    .toLowerCase()
    .normalize("NFD")
    .replace(/[̀-ͯ]/g, "");
}

export function Combobox({
  items = [],
  value,
  onChange,
  placeholder = "Selecione...",
  emptyMessage = "Nenhum resultado encontrado.",
  disabled = false,
  className,
  id,
}) {
  const [open, setOpen] = React.useState(false);
  const [query, setQuery] = React.useState("");
  // Índice do item "destacado" pelo teclado (não selecionado ainda).
  const [highlight, setHighlight] = React.useState(0);

  const rootRef = React.useRef(null);
  const inputRef = React.useRef(null);

  // Item selecionado (rótulo no botão fechado).
  const selected = items.find((i) => i.id === value) ?? null;

  // Filtra por busca; quando vazio mostra tudo.
  // Mantém a ordem "pais antes dos filhos" para que a indentação faça sentido.
  const filtered = React.useMemo(() => {
    const q = normalize(query);
    if (!q) return items;
    return items.filter((i) => normalize(i.label).includes(q));
  }, [items, query]);

  // Fecha ao clicar fora.
  React.useEffect(() => {
    if (!open) return;
    const onDocClick = (e) => {
      if (!rootRef.current?.contains(e.target)) setOpen(false);
    };
    document.addEventListener("mousedown", onDocClick);
    return () => document.removeEventListener("mousedown", onDocClick);
  }, [open]);

  // Reset destaque quando filtro muda.
  React.useEffect(() => setHighlight(0), [query]);

  const select = (item) => {
    onChange?.(item.id);
    setOpen(false);
    setQuery("");
  };

  const onKeyDown = (e) => {
    if (e.key === "Escape") {
      setOpen(false);
      return;
    }
    if (e.key === "ArrowDown") {
      e.preventDefault();
      setHighlight((h) => Math.min(h + 1, filtered.length - 1));
    } else if (e.key === "ArrowUp") {
      e.preventDefault();
      setHighlight((h) => Math.max(h - 1, 0));
    } else if (e.key === "Enter") {
      e.preventDefault();
      const item = filtered[highlight];
      if (item) select(item);
    }
  };

  return (
    <div ref={rootRef} className={cn("relative w-full", className)}>
      {/* "Trigger": parece um input fechado, mostra o item selecionado. */}
      <button
        type="button"
        id={id}
        disabled={disabled}
        aria-haspopup="listbox"
        aria-expanded={open}
        onClick={() => {
          if (disabled) return;
          setOpen((o) => !o);
          // Foca no input de busca assim que abre.
          requestAnimationFrame(() => inputRef.current?.focus());
        }}
        className={cn(
          "w-full h-10 px-3 rounded-xl bg-card border border-border " +
            "text-[13.5px] text-left text-foreground outline-none " +
            "flex items-center justify-between gap-2 " +
            "focus-visible:ring-2 focus-visible:ring-primary/30 " +
            "disabled:opacity-50 disabled:cursor-not-allowed transition-all",
        )}
      >
        <span className={cn("truncate", !selected && "text-muted-foreground/70 font-normal")}>
          {selected ? selected.label : placeholder}
        </span>
        <ChevronDown
          className={cn("w-4 h-4 text-muted-foreground shrink-0 transition-transform", open && "rotate-180")}
          strokeWidth={2.25}
        />
      </button>

      {/* Painel dropdown. */}
      {open && (
        <div
          className="absolute z-50 mt-1 w-full rounded-xl border border-border bg-card shadow-lg overflow-hidden"
          role="listbox"
        >
          {/* Campo de busca. */}
          <div className="relative border-b border-border">
            <Search
              className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2"
              strokeWidth={2.25}
            />
            <input
              ref={inputRef}
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              onKeyDown={onKeyDown}
              placeholder="Buscar..."
              className="w-full pl-8 pr-8 py-2 text-[12.5px] bg-transparent outline-none placeholder:text-muted-foreground"
            />
            {query && (
              <button
                type="button"
                onClick={() => setQuery("")}
                className="absolute right-2 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                aria-label="Limpar busca"
              >
                <X className="w-3.5 h-3.5" strokeWidth={2.25} />
              </button>
            )}
          </div>

          {/* Lista de opções com scroll. */}
          <ul className="max-h-60 overflow-y-auto py-1">
            {filtered.length === 0 ? (
              <li className="px-3 py-4 text-[12px] text-muted-foreground text-center">
                {emptyMessage}
              </li>
            ) : (
              filtered.map((item, idx) => {
                const isSelected = item.id === value;
                const isHighlighted = idx === highlight;
                // Indentação para itens-filhos (parentId definido).
                const indent = item.parentId ? "pl-7" : "pl-3";
                return (
                  <li
                    key={item.id}
                    role="option"
                    aria-selected={isSelected}
                    onMouseEnter={() => setHighlight(idx)}
                    onMouseDown={(e) => {
                      // mousedown (não click) evita o blur do input antes da seleção.
                      e.preventDefault();
                      select(item);
                    }}
                    className={cn(
                      "flex items-center justify-between pr-3 py-1.5 cursor-pointer text-[13px] transition-colors",
                      indent,
                      isHighlighted ? "bg-secondary" : "bg-transparent",
                      isSelected ? "text-primary font-semibold" : "text-foreground",
                    )}
                  >
                    <span className="truncate">{item.label}</span>
                    {isSelected && <Check className="w-3.5 h-3.5 text-primary shrink-0" strokeWidth={2.75} />}
                  </li>
                );
              })
            )}
          </ul>
        </div>
      )}
    </div>
  );
}
