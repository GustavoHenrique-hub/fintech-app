import { useMemo, useState } from "react";
import { RotateCcw, Inbox, Search, X } from "lucide-react";

import { useTransacoes } from "@/hooks/use-transacoes";
import { useCategorias } from "@/hooks/use-categorias";
import { formatBRLSigned, formatDataRelativa, formatHora } from "@/lib/format";
import { valorComSinal } from "@/lib/transacoes";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";
import { StatusBadge } from "@/components/ui/status-badge";
import { EmptyState } from "@/components/ui/empty-state";
import { SkeletonRow } from "@/components/ui/skeleton";
import { EstornarTransacaoModal } from "../EstornarTransacaoModal";

/**
 * @param {() => void} [onCancelar]  sai da tela sem estornar nada (volta para Transações)
 */
export const EstornoTransacaoScreen = ({ onCancelar }) => {
  const [query, setQuery] = useState("");
  const [selecionada, setSelecionada] = useState(null);

  const { data: transacoes = [], isLoading: loadingTx } = useTransacoes();
  const { data: categorias = [], isLoading: loadingCat } = useCategorias();

  const isLoading = loadingTx || loadingCat;

  const categoriasPorId = useMemo(
    () => Object.fromEntries(categorias.map((c) => [c.id, c])),
    [categorias],
  );

  const elegiveis = useMemo(() => {
    const q = query.trim().toLowerCase();
    return transacoes
      .filter((t) => t.statusRevisao === "CONFIRMADA" && !t.estornadoAt && t.indEstorno !== "S")
      .filter((t) => {
        if (!q) return true;
        const h = [t.descricao, t.estabelecimento]
          .filter(Boolean).join(" ").toLowerCase();
        return h.includes(q);
      })
      .sort((a, b) => (a.dataTransacao < b.dataTransacao ? 1 : -1));
  }, [query, transacoes]);

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-6 pb-6 lg:pb-8 no-scrollbar">
     <div className="max-w-5xl mx-auto w-full space-y-5 lg:space-y-6">

      <div className="flex items-start justify-between gap-4">
        <div>
          <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight">
            Estornar transações
          </h1>
          <p className="text-[12px] lg:text-[13px] text-muted-foreground mt-0.5">
            Reverta cobranças confirmadas em até 7 dias após a transação.
          </p>
        </div>
        <div className="flex items-center gap-2 shrink-0">
          {onCancelar && (
            <Button variant="secondary" size="sm" leftIcon={X} onClick={onCancelar}>
              Cancelar
            </Button>
          )}
          <div className="hidden sm:flex w-10 h-10 lg:w-11 lg:h-11 rounded-2xl bg-surface-purple text-primary items-center justify-center">
            <RotateCcw className="w-5 h-5" strokeWidth={2.25} />
          </div>
        </div>
      </div>

      {/* Busca */}
      <div className="relative">
        <Search className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2" strokeWidth={2.25} />
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Buscar transações elegíveis..."
          aria-label="Buscar transações elegíveis"
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

      {/* Lista de elegíveis */}
      <div>
        <p className="section-label mb-2">
          {isLoading ? "Carregando..." : `${elegiveis.length} transação(ões) elegível(eis) ao estorno`}
        </p>

        {isLoading ? (
          <div className="card-soft divide-y divide-border">
            {[0, 1, 2].map((i) => <SkeletonRow key={i} />)}
          </div>
        ) : elegiveis.length === 0 ? (
          <EmptyState
            icon={Inbox}
            title="Nenhuma transação elegível"
            description="Apenas transações confirmadas dentro do prazo aparecem aqui."
            action={query ? <Button variant="secondary" onClick={() => setQuery("")}>Limpar busca</Button> : null}
          />
        ) : (
          <div className="card-soft divide-y divide-border">
            {elegiveis.map((t) => {
              const categoria = categoriasPorId[t.categoriaId];
              const Icone = getIconeCategoria(categoria?.icone);
              const valor = valorComSinal(t);

              return (
                <div key={t.id} className="flex items-center gap-3 px-3.5 py-3 row-press">
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
                      {t.descricao ?? t.estabelecimento ?? "Lançamento"}
                    </p>
                    <p className="text-[11px] text-muted-foreground mt-0.5 truncate">
                      {categoria?.nome ?? "Sem categoria"} · {formatDataRelativa(t.dataTransacao)} · {formatHora(t.criadoEm)}
                    </p>
                    <div className="mt-1.5">
                      <StatusBadge kind="revisao" value="CONFIRMADA" />
                    </div>
                  </div>
                  <div className="text-right shrink-0 flex flex-col items-end gap-1.5">
                    <p className={`text-[13.5px] font-extrabold tabular-nums ${valor > 0 ? "text-success" : "text-foreground"}`}>
                      {formatBRLSigned(valor)}
                    </p>
                    <Button variant="secondary" size="sm" leftIcon={RotateCcw} onClick={() => setSelecionada(t)}>
                      Estornar
                    </Button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

     </div>

      <EstornarTransacaoModal transacao={selecionada} onClose={() => setSelecionada(null)} />
    </div>
  );
};
