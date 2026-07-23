import { useState, useMemo } from "react";
import { SlidersHorizontal, Search, ChevronDown, Inbox, X, RotateCcw } from "lucide-react";
import { format } from "date-fns";

import { useTransacoes } from "@/hooks/use-transacoes";
import { useCategorias } from "@/hooks/use-categorias";
import { useContaSelecionada } from "@/context/ContaSelecionadaContext";
import {
  formatBRL, formatBRLSigned, formatDataRelativa, formatHora,
} from "@/lib/format";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";
import { StatusBadge, STATUS_REVISAO } from "@/components/ui/status-badge";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { EmptyState } from "@/components/ui/empty-state";
import { SkeletonRow } from "@/components/ui/skeleton";
import { DateRangePicker } from "@/components/ui/date-range-picker";
import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription,
  ModalFooter, ModalClose,
} from "@/components/ui/modal";

const FILTROS_PADRAO = {
  tipo: "todas",       // "todas" | "receitas" | "gastos"
  recorrente: false,
  statusRevisao: [],   // chaves de STATUS_REVISAO; vazio = todos
  periodo: null,       // { from, to } | null
  categoriaIds: [],    // vazio = todas
};

function normalize(s) {
  return (s ?? "")
    .toString()
    .toLowerCase()
    .normalize("NFD")
    .replace(/[̀-ͯ]/g, "");
}

function algumFiltroAtivo(filtros) {
  return (
    filtros.tipo !== "todas" ||
    filtros.recorrente ||
    filtros.statusRevisao.length > 0 ||
    !!filtros.periodo?.from ||
    filtros.categoriaIds.length > 0
  );
}

function FiltrosModal({ open, onOpenChange, rascunho, setRascunho, categorias, onAplicar }) {
  const toggleStatus = (key) =>
    setRascunho((f) => ({
      ...f,
      statusRevisao: f.statusRevisao.includes(key)
        ? f.statusRevisao.filter((k) => k !== key)
        : [...f.statusRevisao, key],
    }));

  const toggleCategoria = (id) =>
    setRascunho((f) => ({
      ...f,
      categoriaIds: f.categoriaIds.includes(id)
        ? f.categoriaIds.filter((c) => c !== id)
        : [...f.categoriaIds, id],
    }));

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent className="max-w-lg">
        <ModalHeader>
          <ModalTitle>Filtros</ModalTitle>
          <ModalDescription>Combine período, status, categoria e tipo para refinar a lista.</ModalDescription>
        </ModalHeader>

        <div className="max-h-[55vh] overflow-y-auto space-y-4 pr-1 -mr-1">
          <div>
            <p className="section-label mb-1.5">Tipo</p>
            <div className="flex gap-1.5 flex-wrap">
              {[
                { key: "todas", label: "Todas" },
                { key: "receitas", label: "Receitas" },
                { key: "gastos", label: "Gastos" },
              ].map((t) => (
                <button
                  key={t.key}
                  type="button"
                  onClick={() => setRascunho((f) => ({ ...f, tipo: t.key }))}
                  className={`px-3 py-1 rounded-full text-[11.5px] font-semibold border transition-all active:scale-95 ${
                    rascunho.tipo === t.key
                      ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                      : "bg-card border-border text-foreground hover:bg-secondary"
                  }`}
                >
                  {t.label}
                </button>
              ))}
              <button
                type="button"
                onClick={() => setRascunho((f) => ({ ...f, recorrente: !f.recorrente }))}
                className={`px-3 py-1 rounded-full text-[11.5px] font-semibold border transition-all active:scale-95 ${
                  rascunho.recorrente
                    ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                    : "bg-card border-border text-foreground hover:bg-secondary"
                }`}
              >
                Somente recorrentes
              </button>
            </div>
          </div>

          <div>
            <p className="section-label mb-1.5">Período</p>
            <DateRangePicker
              value={rascunho.periodo}
              onChange={(periodo) => setRascunho((f) => ({ ...f, periodo }))}
            />
          </div>

          <div>
            <p className="section-label mb-1.5">Status de revisão</p>
            <div className="flex gap-1.5 flex-wrap">
              {Object.entries(STATUS_REVISAO).map(([key, { label }]) => {
                const ativo = rascunho.statusRevisao.includes(key);
                return (
                  <button
                    key={key}
                    type="button"
                    onClick={() => toggleStatus(key)}
                    className={`px-3 py-1 rounded-full text-[11.5px] font-semibold border transition-all active:scale-95 ${
                      ativo
                        ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                        : "bg-card border-border text-foreground hover:bg-secondary"
                    }`}
                  >
                    {label}
                  </button>
                );
              })}
            </div>
          </div>

          {categorias.length > 0 && (
            <div>
              <p className="section-label mb-1.5">Categoria</p>
              <div className="flex gap-1.5 flex-wrap">
                {categorias.map((c) => {
                  const ativo = rascunho.categoriaIds.includes(c.id);
                  return (
                    <button
                      key={c.id}
                      type="button"
                      onClick={() => toggleCategoria(c.id)}
                      className={`px-3 py-1 rounded-full text-[11.5px] font-semibold border transition-all active:scale-95 ${
                        ativo
                          ? "bg-primary border-transparent text-primary-foreground shadow-sm shadow-primary/20"
                          : "bg-card border-border text-foreground hover:bg-secondary"
                      }`}
                    >
                      {c.nome}
                    </button>
                  );
                })}
              </div>
            </div>
          )}
        </div>

        <ModalFooter>
          <Button variant="ghost" onClick={() => setRascunho(FILTROS_PADRAO)}>Limpar filtros</Button>
          <ModalClose asChild>
            <Button variant="secondary">Cancelar</Button>
          </ModalClose>
          <Button onClick={onAplicar}>Aplicar</Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}

export const TransactionsScreen = ({ onAbrirEstorno }) => {
  const [query, setQuery] = useState("");
  const [filtros, setFiltros] = useState(FILTROS_PADRAO);
  const [rascunho, setRascunho] = useState(FILTROS_PADRAO);
  const [modalFiltrosAberto, setModalFiltrosAberto] = useState(false);

  const { data: transacoes = [], isLoading: loadingTx } = useTransacoes();
  const { data: categorias = [], isLoading: loadingCat } = useCategorias();
  const { contaAtual, loadingContas } = useContaSelecionada();

  const isLoading = loadingTx || loadingCat || loadingContas;

  const categoriasPorId = useMemo(
    () => Object.fromEntries(categorias.map((c) => [c.id, c])),
    [categorias],
  );

  const transacoesFiltradas = useMemo(() => {
    const q = normalize(query);
    const periodoInicio = filtros.periodo?.from ? format(filtros.periodo.from, "yyyy-MM-dd") : null;
    const periodoFim = filtros.periodo?.to ? format(filtros.periodo.to, "yyyy-MM-dd") : null;

    return transacoes.filter((t) => {
      if (t.contaId !== contaAtual?.id) return false;
      if (filtros.tipo === "receitas" && t.tipo !== "RECEITA") return false;
      if (filtros.tipo === "gastos" && t.tipo !== "GASTO") return false;
      if (filtros.recorrente && !t.recorrente) return false;
      if (filtros.statusRevisao.length && !filtros.statusRevisao.includes(t.statusRevisao)) return false;
      if (filtros.categoriaIds.length && !filtros.categoriaIds.includes(t.categoriaId)) return false;
      if (periodoInicio && t.dataTransacao < periodoInicio) return false;
      if (periodoFim && t.dataTransacao > periodoFim) return false;
      if (!q) return true;
      const haystack = [t.descricaoUsuario, t.descricaoNormalizada, t.estabelecimento]
        .filter(Boolean)
        .map(normalize)
        .join(" ");
      return haystack.includes(q);
    });
  }, [filtros, query, transacoes, contaAtual]);

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
  const filtroAtivo = algumFiltroAtivo(filtros) || query.length > 0;

  const limparFiltros = () => {
    setFiltros(FILTROS_PADRAO);
    setQuery("");
  };

  const abrirFiltros = () => {
    setRascunho(filtros);
    setModalFiltrosAberto(true);
  };

  const aplicarFiltros = () => {
    setFiltros(rascunho);
    setModalFiltrosAberto(false);
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
            <Button variant="secondary" size="sm" leftIcon={SlidersHorizontal} onClick={abrirFiltros}>
              Filtros
              {algumFiltroAtivo(filtros) && <span className="w-1.5 h-1.5 bg-primary rounded-full" />}
            </Button>
          </div>
        </div>

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
       </div>
      </div>

      <div className="px-4 sm:px-5 lg:px-8 mt-3 lg:mt-5">
       <div className="max-w-5xl mx-auto w-full space-y-4 lg:space-y-5">
        {isLoading ? (
          <div className="card-soft divide-y divide-border">
            {[0, 1, 2, 3, 4].map((i) => <SkeletonRow key={i} />)}
          </div>
        ) : semResultados ? (
          <EmptyState
            icon={Inbox}
            title={filtroAtivo ? "Nenhuma transação encontrada" : "Nenhuma transação ainda"}
            description={
              filtroAtivo
                ? "Tente ajustar os filtros ou limpar a busca."
                : "Importe um extrato ou crie uma transação manualmente."
            }
            action={
              filtroAtivo ? (
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

                      <div className="flex items-center gap-2 pl-[52px]">
                        <StatusBadge kind="revisao" value={t.statusRevisao} />
                        {mostrarConfianca && (
                          <div className="flex-1 max-w-[140px]">
                            <ConfidenceBar value={t.confiancaIa} showQualitative={false} />
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

        {!isLoading && !semResultados && (
          <Button variant="secondary" className="w-full" rightIcon={ChevronDown}>
            Carregar mais
          </Button>
        )}
       </div>
      </div>

      <FiltrosModal
        open={modalFiltrosAberto}
        onOpenChange={setModalFiltrosAberto}
        rascunho={rascunho}
        setRascunho={setRascunho}
        categorias={categorias}
        onAplicar={aplicarFiltros}
      />
    </div>
  );
};
