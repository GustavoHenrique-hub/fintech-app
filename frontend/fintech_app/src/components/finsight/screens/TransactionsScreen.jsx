// TransactionsScreen: lista de transações vindas do backend, agrupadas por dia.
//
// Onde plugar com o backend:
//   - GET /transacoes/usuario/{id}  → TransacaoResponseDTO[]
//   - GET /categorias               → CategoriaResponseDTO[]  (para o mapa de ícones/nomes)
//
// Features na tela:
//   - Busca por descrição/estabelecimento (case+acento insensitive)
//   - Filtros por tipo (Todas, Receitas, Gastos, Recorrentes)
//   - Status de revisão visível com StatusBadge
//   - Confiança da IA com ConfidenceBar quando pendente de revisão
//   - EmptyState quando filtros não casam
import { useState, useMemo } from "react";
import { SlidersHorizontal, Search, ChevronDown, Inbox, X, RotateCcw } from "lucide-react";

import { transacoes, categoriasPorId } from "@/mocks";
import {
  formatBRL, formatBRLSigned, formatDataRelativa, formatHora,
} from "@/lib/format";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";
import { StatusBadge } from "@/components/ui/status-badge";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { EmptyState } from "@/components/ui/empty-state";

// Os 4 filtros disponíveis. `tipo` mapeia para o atributo do DTO.
const filtros = [
  { key: "todas",       label: "Todas" },
  { key: "receitas",    label: "Receitas" },
  { key: "gastos",      label: "Gastos" },
  { key: "recorrentes", label: "Recorrentes" },
];

// Remove acentos pra busca tolerante.
function normalize(s) {
  return (s ?? "")
    .toString()
    .toLowerCase()
    .normalize("NFD")
    .replace(/[̀-ͯ]/g, "");
}

export const TransactionsScreen = ({ onAbrirEstorno }) => {
  const [filtroAtivo, setFiltroAtivo] = useState("todas");
  const [query, setQuery] = useState("");

  // Aplica filtros + busca. useMemo para evitar recalcular toda render.
  const transacoesFiltradas = useMemo(() => {
    const q = normalize(query);
    return transacoes.filter((t) => {
      // Filtro de tipo
      if (filtroAtivo === "receitas" && t.tipo !== "RECEITA") return false;
      if (filtroAtivo === "gastos" && t.tipo !== "GASTO") return false;
      if (filtroAtivo === "recorrentes" && !t.recorrente) return false;

      // Busca textual
      if (!q) return true;
      const haystack = [t.descricaoUsuario, t.descricaoNormalizada, t.estabelecimento]
        .filter(Boolean)
        .map(normalize)
        .join(" ");
      return haystack.includes(q);
    });
  }, [filtroAtivo, query]);

  // Agrupa por dataTransacao (LocalDate). Map preserva ordem de inserção.
  const grupos = useMemo(() => {
    const ordenadas = [...transacoesFiltradas].sort((a, b) =>
      a.dataTransacao < b.dataTransacao ? 1 : -1,
    );
    const map = new Map();
    for (const t of ordenadas) {
      const key = t.dataTransacao;
      if (!map.has(key)) map.set(key, []);
      map.get(key).push(t);
    }
    return Array.from(map.entries());
  }, [transacoesFiltradas]);

  const semResultados = grupos.length === 0;
  const algumFiltroAtivo = filtroAtivo !== "todas" || query.length > 0;

  const limparFiltros = () => {
    setFiltroAtivo("todas");
    setQuery("");
  };

  return (
    <div className="flex-1 min-h-0 overflow-y-auto pb-6 lg:pb-10 no-scrollbar">
      {/* Header sticky */}
      <div className="sticky top-0 z-10 bg-background/95 backdrop-blur border-b border-border px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-3 lg:pb-5">
       <div className="max-w-5xl mx-auto w-full">
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-[20px] lg:text-[28px] font-extrabold tracking-tight text-foreground">Transações</h1>
            <p className="text-[11.5px] lg:text-[13px] text-muted-foreground mt-0.5">
              {transacoesFiltradas.length} {transacoesFiltradas.length === 1 ? "item" : "itens"} · Últimos 30 dias
            </p>
          </div>
          <div className="flex items-center gap-2">
            {onAbrirEstorno && (
              <Button
                variant="ghost"
                size="sm"
                leftIcon={RotateCcw}
                onClick={onAbrirEstorno}
                aria-label="Abrir tela de estornos"
              >
                Estornar
              </Button>
            )}
            <Button variant="secondary" size="sm" leftIcon={SlidersHorizontal}>
              Filtros
              {algumFiltroAtivo && <span className="w-1.5 h-1.5 bg-primary rounded-full" />}
            </Button>
          </div>
        </div>

        {/* Campo de busca */}
        <div className="relative mt-3">
          <Search
            className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2"
            strokeWidth={2.25}
          />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Buscar transações..."
            className="w-full bg-secondary rounded-full pl-8 pr-8 py-2 text-[12.5px] outline-none placeholder:text-muted-foreground focus:ring-2 focus:ring-primary/30 transition-all"
          />
          {query && (
            <button
              onClick={() => setQuery("")}
              className="absolute right-2 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
              aria-label="Limpar busca"
            >
              <X className="w-3.5 h-3.5" strokeWidth={2.25} />
            </button>
          )}
        </div>

        {/* Chips de filtro */}
        <div className="flex gap-1.5 mt-3 overflow-x-auto no-scrollbar -mx-4 px-4">
          {filtros.map((f) => (
            <button
              key={f.key}
              onClick={() => setFiltroAtivo(f.key)}
              className={`px-3 py-1 rounded-full text-[11.5px] font-semibold whitespace-nowrap border transition-all active:scale-95 ${
                filtroAtivo === f.key
                  ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                  : "bg-card border-border text-foreground hover:bg-secondary"
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>
       </div>
      </div>

      {/* Lista agrupada (ou EmptyState quando vazio) */}
      <div className="px-4 sm:px-5 lg:px-8 mt-3 lg:mt-5">
       <div className="max-w-5xl mx-auto w-full space-y-4 lg:space-y-5">
        {semResultados ? (
          <EmptyState
            icon={Inbox}
            title={algumFiltroAtivo ? "Nenhuma transação encontrada" : "Nenhuma transação ainda"}
            description={
              algumFiltroAtivo
                ? "Tente ajustar os filtros ou limpar a busca."
                : "Importe um extrato ou crie uma transação manualmente."
            }
            action={
              algumFiltroAtivo ? (
                <Button variant="secondary" onClick={limparFiltros}>Limpar filtros</Button>
              ) : (
                <Button>Nova transação</Button>
              )
            }
          />
        ) : (
          grupos.map(([data, items]) => (
            <div key={data}>
              <p className="section-label mb-1.5">{formatDataRelativa(data)}</p>
              <div className="card-soft divide-y divide-border">
                {items.map((t) => {
                  const categoria = categoriasPorId[t.categoriaId];
                  const Icone = getIconeCategoria(categoria?.icone);
                  const positivo = t.tipo === "RECEITA";
                  // Mostra ConfidenceBar quando a IA classificou mas a transação
                  // ainda não foi revisada manualmente.
                  const mostrarConfianca =
                    t.statusRevisao === "PENDENTE_REVISAO" ||
                    t.statusRevisao === "CLASSIFICADA" ||
                    t.statusRevisao === "EXTRAIDA";

                  return (
                    <button
                      key={t.id}
                      className="w-full flex flex-col gap-2 px-3.5 py-3 text-left row-press"
                    >
                      <div className="flex items-center gap-3">
                        <div
                          className="w-10 h-10 rounded-full flex items-center justify-center shrink-0"
                          style={{ backgroundColor: `${categoria?.corHex ?? "#94a3b8"}22` }}
                        >
                          <Icone
                            className="w-[17px] h-[17px]"
                            strokeWidth={2.25}
                            style={{ color: categoria?.corHex ?? "var(--foreground)" }}
                          />
                        </div>
                        <div className="flex-1 min-w-0">
                          <p className="font-semibold text-[13.5px] text-foreground truncate leading-tight">
                            {t.descricaoUsuario ?? t.estabelecimento ?? t.descricaoNormalizada}
                          </p>
                          <p className="text-[11px] text-muted-foreground mt-0.5 truncate">
                            {categoria?.nome ?? "Sem categoria"} · {formatHora(t.criadoEm)}
                            {t.recorrente && " · Recorrente"}
                          </p>
                        </div>
                        <div className="text-right shrink-0">
                          <p className={`text-[13.5px] font-extrabold tabular-nums ${positivo ? "text-success" : "text-foreground"}`}>
                            {formatBRLSigned(positivo ? t.valor : -t.valor)}
                          </p>
                          <p className="text-[10px] text-muted-foreground mt-0.5">BRL</p>
                        </div>
                      </div>

                      {/* Linha inferior: status + confiança quando relevante. */}
                      <div className="flex items-center gap-2 pl-[52px]">
                        <StatusBadge kind="revisao" value={t.statusRevisao} />
                        {mostrarConfianca && (
                          <div className="flex-1 max-w-[140px]">
                            <ConfidenceBar
                              value={t.confiancaIa}
                              showQualitative={false}
                            />
                          </div>
                        )}
                      </div>
                    </button>
                  );
                })}
              </div>
            </div>
          ))
        )}

        {!semResultados && (
          <Button variant="secondary" className="w-full" rightIcon={ChevronDown}>
            Carregar mais
          </Button>
        )}
       </div>
      </div>
    </div>
  );
};
