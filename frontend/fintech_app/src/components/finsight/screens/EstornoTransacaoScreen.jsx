// EstornoTransacaoScreen: estornar transações já CONFIRMADAS.
//
// Backend (futuro):
//   - GET  /transacoes/usuario/{id}?status=CONFIRMADA   → elegíveis
//   - POST /transacoes/{id}/estorno  { motivo, observacao }
//   - O estorno cria uma transação inversa e marca a original como
//     statusRevisao = "ARQUIVADA" + flag `estornada`.
//
// Esta tela mostra todas as transações elegíveis, abre um modal para confirmar
// (com motivo obrigatório) e dispara um toast conforme o resultado.
//
// O bloco "Testar alertas" no topo emite os 4 tipos de toast (success / error
// / warning / info) sem chamar nenhuma lógica de domínio — útil para QA.
import { useMemo, useState } from "react";
import {
  RotateCcw, Inbox, AlertTriangle, CheckCircle2, XCircle, Info,
  Search, X,
} from "lucide-react";

import { transacoes, categoriasPorId } from "@/mocks";
import {
  formatBRL, formatBRLSigned, formatDataRelativa, formatHora,
} from "@/lib/format";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";
import { StatusBadge } from "@/components/ui/status-badge";
import { EmptyState } from "@/components/ui/empty-state";
import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription,
  ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { toast } from "@/hooks/use-toast";

// Min/max do textarea de motivo. Usado tanto para validação quanto para o
// hint visual mostrado ao usuário.
const MIN_MOTIVO = 10;
const MAX_MOTIVO = 240;

// Catálogo de motivos pré-definidos. Em produção viria de /enums/motivos-estorno.
const MOTIVOS_RAPIDOS = [
  "Cobrança duplicada",
  "Valor incorreto",
  "Estabelecimento não reconhecido",
  "Compra cancelada",
];

// Conjunto de IDs estornados nesta sessão — simula o "lado servidor" sem
// precisar mutar o mock global de transações. Quando o backend estiver
// conectado, isso volta a ser estado vindo de queries.
function useSetEstornados() {
  return useState(() => new Set());
}

export const EstornoTransacaoScreen = () => {
  const [query, setQuery] = useState("");
  const [estornados, setEstornados] = useSetEstornados();
  const [selecionada, setSelecionada] = useState(null);
  const [motivo, setMotivo] = useState("");
  const [observacao, setObservacao] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [touched, setTouched] = useState(false);

  // Só transações CONFIRMADAS são elegíveis ao estorno. Outros estados
  // (EXTRAIDA, PENDENTE_REVISAO, IGNORADA, ARQUIVADA) ficam de fora.
  const elegiveis = useMemo(() => {
    const q = query.trim().toLowerCase();
    return transacoes
      .filter((t) => t.statusRevisao === "CONFIRMADA")
      .filter((t) => {
        if (!q) return true;
        const h = [t.descricaoUsuario, t.descricaoNormalizada, t.estabelecimento]
          .filter(Boolean).join(" ").toLowerCase();
        return h.includes(q);
      })
      .sort((a, b) => (a.dataTransacao < b.dataTransacao ? 1 : -1));
  }, [query]);

  // Fechamento do modal: limpa o formulário ao mesmo tempo.
  const fechar = () => {
    setSelecionada(null);
    setMotivo("");
    setObservacao("");
    setTouched(false);
    setSubmitting(false);
  };

  // ── Submissão do estorno ───────────────────────────────────────────────
  // Simula 3 cenários aleatórios para exercitar todas as variantes de toast:
  //   - 70% sucesso
  //   - 15% warning (transação não pode ser estornada — janela expirada)
  //   - 15% erro de rede
  const confirmarEstorno = async () => {
    setTouched(true);

    if (!selecionada) return;
    if (motivo.trim().length < MIN_MOTIVO) {
      toast.warning({
        title: "Motivo muito curto",
        description: `Descreva o motivo com pelo menos ${MIN_MOTIVO} caracteres.`,
      });
      return;
    }
    if (estornados.has(selecionada.id)) {
      toast.warning({
        title: "Estorno já registrado",
        description: "Esta transação já foi estornada nesta sessão.",
      });
      return;
    }

    setSubmitting(true);

    // Latência simulada.
    await new Promise((r) => setTimeout(r, 700));

    const dado = Math.random();
    setSubmitting(false);

    if (dado < 0.15) {
      // Erro de rede: usuário pode tentar novamente.
      toast.error({
        title: "Falha de comunicação",
        description: "Não conseguimos contatar o serviço de estornos. Tente novamente em alguns instantes.",
      });
      return;
    }
    if (dado < 0.30) {
      // Warning: regra de negócio bloqueou (ex.: prazo expirou).
      toast.warning({
        title: "Estorno não permitido",
        description: "A janela de 7 dias para estornar essa transação já expirou.",
      });
      return;
    }

    // Sucesso. Marca como estornada localmente para esconder da lista
    // (próximo render filtra) e mostra toast de confirmação com info adicional.
    setEstornados((prev) => new Set(prev).add(selecionada.id));
    toast.success({
      title: "Estorno realizado",
      description: `${formatBRL(selecionada.valor)} foi estornado de "${
        selecionada.descricaoUsuario ?? selecionada.estabelecimento ?? selecionada.descricaoNormalizada
      }".`,
    });

    // Toast informativo extra explicando o tempo do crédito.
    setTimeout(() => {
      toast.info({
        title: "Crédito em até 2 dias úteis",
        description: "O valor será creditado conforme o prazo do emissor.",
      });
    }, 250);

    fechar();
  };

  const erroMotivo = touched && motivo.trim().length < MIN_MOTIVO;

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-6 pb-6 lg:pb-8 no-scrollbar">
     <div className="max-w-5xl mx-auto w-full space-y-5 lg:space-y-6">

      {/* ── Header ────────────────────────────────────────────────────── */}
      <div className="flex items-start justify-between gap-4">
        <div>
          <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight">
            Estornar transações
          </h1>
          <p className="text-[12px] lg:text-[13px] text-muted-foreground mt-0.5">
            Reverta cobranças confirmadas em até 7 dias após a transação.
          </p>
        </div>
        <div className="hidden sm:flex w-10 h-10 lg:w-11 lg:h-11 rounded-2xl bg-surface-purple text-primary items-center justify-center shrink-0">
          <RotateCcw className="w-5 h-5" strokeWidth={2.25} />
        </div>
      </div>

      {/* ── Painel de testes de alerta ────────────────────────────────── */}
      <section className="card-soft p-3 lg:p-4">
        <div className="flex items-center justify-between gap-2">
          <div>
            <p className="section-label">Testar alertas</p>
            <p className="text-[11.5px] text-muted-foreground mt-0.5">
              Dispara as 4 variantes sem afetar dados reais.
            </p>
          </div>
        </div>
        <div className="mt-3 grid grid-cols-2 sm:grid-cols-4 gap-2">
          <Button
            variant="primary"
            size="sm"
            leftIcon={CheckCircle2}
            onClick={() =>
              toast.success({
                title: "Operação concluída",
                description: "Tudo certo — sem pendências.",
              })
            }
          >
            Sucesso
          </Button>
          <Button
            variant="secondary"
            size="sm"
            leftIcon={AlertTriangle}
            onClick={() =>
              toast.warning({
                title: "Atenção",
                description: "Alguns campos precisam ser revisados antes de continuar.",
              })
            }
          >
            Aviso
          </Button>
          <Button
            variant="danger"
            size="sm"
            leftIcon={XCircle}
            onClick={() =>
              toast.error({
                title: "Ocorreu um erro",
                description: "Não foi possível concluir a operação. Tente novamente.",
              })
            }
          >
            Erro
          </Button>
          <Button
            variant="secondary"
            size="sm"
            leftIcon={Info}
            onClick={() =>
              toast.info({
                title: "Informação",
                description: "O estorno pode levar até 2 dias úteis para refletir.",
              })
            }
          >
            Info
          </Button>
        </div>
      </section>

      {/* ── Busca ─────────────────────────────────────────────────────── */}
      <div className="relative">
        <Search
          className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2"
          strokeWidth={2.25}
        />
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

      {/* ── Lista de elegíveis ────────────────────────────────────────── */}
      <div>
        <p className="section-label mb-2">
          {elegiveis.length} transação(ões) elegível(eis) ao estorno
        </p>

        {elegiveis.length === 0 ? (
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
              const positivo = t.tipo === "RECEITA";
              const jaEstornada = estornados.has(t.id);

              return (
                <div
                  key={t.id}
                  className={`flex items-center gap-3 px-3.5 py-3 row-press ${jaEstornada ? "opacity-50" : ""}`}
                >
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
                      {categoria?.nome ?? "Sem categoria"} · {formatDataRelativa(t.dataTransacao)} · {formatHora(t.criadoEm)}
                    </p>
                    <div className="mt-1.5">
                      <StatusBadge
                        kind="revisao"
                        value={jaEstornada ? "ARQUIVADA" : "CONFIRMADA"}
                      />
                    </div>
                  </div>
                  <div className="text-right shrink-0 flex flex-col items-end gap-1.5">
                    <p className={`text-[13.5px] font-extrabold tabular-nums ${positivo ? "text-success" : "text-foreground"}`}>
                      {formatBRLSigned(positivo ? t.valor : -t.valor)}
                    </p>
                    <Button
                      variant="secondary"
                      size="sm"
                      leftIcon={RotateCcw}
                      disabled={jaEstornada}
                      onClick={() => {
                        setSelecionada(t);
                        setMotivo("");
                        setObservacao("");
                        setTouched(false);
                      }}
                    >
                      {jaEstornada ? "Estornada" : "Estornar"}
                    </Button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

     </div>

      {/* ── Modal de confirmação ──────────────────────────────────────── */}
      <Modal
        open={selecionada !== null}
        onOpenChange={(o) => {
          if (!o) fechar();
        }}
      >
        <ModalContent>
          <ModalHeader>
            <div className="flex items-start gap-3">
              <div className="w-10 h-10 rounded-full bg-surface-purple text-primary flex items-center justify-center shrink-0">
                <RotateCcw className="w-5 h-5" strokeWidth={2.5} />
              </div>
              <div>
                <ModalTitle>Estornar transação</ModalTitle>
                <ModalDescription>
                  Esta ação cria uma transação inversa e arquiva a original.
                  Após o estorno, a operação não pode ser desfeita.
                </ModalDescription>
              </div>
            </div>
          </ModalHeader>

          {selecionada && (
            <div className="px-1 space-y-4">
              {/* Resumo da transação */}
              <div className="card-soft p-3 flex items-center gap-3">
                <div className="flex-1 min-w-0">
                  <p className="text-[12px] text-muted-foreground font-semibold">
                    {selecionada.estabelecimento ?? selecionada.descricaoNormalizada}
                  </p>
                  <p className="text-[15px] font-extrabold text-foreground tracking-tight tabular-nums">
                    {formatBRL(selecionada.valor)}
                  </p>
                  <p className="text-[11px] text-muted-foreground mt-0.5">
                    {formatDataRelativa(selecionada.dataTransacao)} · Código {selecionada.code}
                  </p>
                </div>
              </div>

              {/* Motivos rápidos */}
              <div>
                <label className="section-label">Motivo (obrigatório)</label>
                <div className="mt-1.5 flex flex-wrap gap-1.5">
                  {MOTIVOS_RAPIDOS.map((m) => (
                    <button
                      key={m}
                      type="button"
                      onClick={() => setMotivo(m)}
                      className={`px-2.5 py-1 rounded-full text-[11.5px] font-semibold border transition-all active:scale-95 ${
                        motivo === m
                          ? "bg-primary border-transparent text-primary-foreground"
                          : "bg-card border-border text-foreground hover:bg-secondary"
                      }`}
                    >
                      {m}
                    </button>
                  ))}
                </div>
                <textarea
                  value={motivo}
                  onChange={(e) => setMotivo(e.target.value.slice(0, MAX_MOTIVO))}
                  placeholder={`Descreva o motivo com pelo menos ${MIN_MOTIVO} caracteres.`}
                  rows={3}
                  className={`mt-2 w-full rounded-xl px-3 py-2 text-[13px] outline-none bg-card border transition-all focus:ring-2 focus:ring-primary/30 ${
                    erroMotivo ? "border-destructive/50" : "border-border focus:border-primary/40"
                  }`}
                />
                <div className="flex items-center justify-between mt-1">
                  <p className={`text-[11px] font-medium ${erroMotivo ? "text-destructive" : "text-muted-foreground"}`}>
                    {erroMotivo ? `Faltam ${MIN_MOTIVO - motivo.trim().length} caracteres.` : "Mín. 10, máx. 240 caracteres."}
                  </p>
                  <p className="text-[11px] text-muted-foreground tabular-nums">
                    {motivo.length}/{MAX_MOTIVO}
                  </p>
                </div>
              </div>

              {/* Observação opcional */}
              <div>
                <label className="section-label">Observação (opcional)</label>
                <input
                  value={observacao}
                  onChange={(e) => setObservacao(e.target.value)}
                  placeholder="Ex.: protocolo do atendimento, número de contato..."
                  maxLength={120}
                  className="mt-1.5 w-full h-10 px-3 rounded-xl bg-card border border-border text-[13px] outline-none placeholder:text-muted-foreground/60 focus:ring-2 focus:ring-primary/30 focus:border-primary/40 transition-all"
                />
              </div>

              {/* Aviso informativo */}
              <div className="flex items-start gap-2 p-3 rounded-xl bg-surface-purple/60 border border-primary/15">
                <Info className="w-4 h-4 text-primary shrink-0 mt-0.5" strokeWidth={2.5} />
                <p className="text-[11.5px] text-foreground leading-snug">
                  O valor estornado será creditado conforme o prazo do emissor (até 2 dias úteis).
                </p>
              </div>
            </div>
          )}

          <ModalFooter>
            <ModalClose asChild>
              <Button variant="secondary" disabled={submitting}>Cancelar</Button>
            </ModalClose>
            <Button
              variant="danger"
              leftIcon={RotateCcw}
              loading={submitting}
              onClick={confirmarEstorno}
            >
              Confirmar estorno
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </div>
  );
};
