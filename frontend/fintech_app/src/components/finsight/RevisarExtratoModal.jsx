// Modal "Revisar extrato": abre ao clicar num arquivo importado na tela de
// Extratos (e logo após o upload) e lista os lançamentos criados a partir dele.
//
// Fluxo de negócio: lançamentos importados nascem com statusRevisao=PENDENTE_REVISAO
// numa categoria genérica — PDF passa pela automação N8N + IA, os demais formatos
// pelo parser local. Na leitura do resultado da IA o backend calibra a confiança
// de cada lançamento com o que o usuário já decidiu antes; a partir de 95% ele já
// nasce CONFIRMADO. Os demais o usuário revisa aqui, escolhendo Gasto, Receita ou
// Economias e, opcionalmente, a categoria:
//   · "Revisado"          → PATCH /transacoes/{id}/{code}/revisar (um lançamento);
//   · "Revisar tudo"      → POST /transacoes/revisar-lote com a escolha de cada pendente;
//   · "Estornar revisão"  → PATCH /transacoes/{id}/{code}/desfazer-revisao — o
//     confirmado volta a pendente, sai do saldo e a confiança da IA cai.
// GASTO/RECEITA confirmados entram no saldo da conta; ECONOMIA vira aporte no
// sub-saldo de economias e o lançamento sai das listagens.
//
// Enquanto o extrato está em processamento (PDF na fila da automação) a lista é
// recarregada sozinha até os lançamentos chegarem pelo callback.
//
// Ações sobre o processamento (ExtratoController#cancelar / #reenviar):
//   · Cancelar → extrato preso na fila/leitura vai para erro_classificacao;
//   · Reenviar → extrato com erro e sem lançamentos é processado de novo com o
//     arquivo já armazenado — o upload recusa o mesmo arquivo pelo hash.
import { useEffect, useMemo, useState } from "react";
import { CheckCheck, CreditCard, Inbox, Loader2, RotateCw, Undo2, XCircle } from "lucide-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { Combobox } from "@/components/ui/combobox";
import { StatusBadge } from "@/components/ui/status-badge";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { SkeletonRow } from "@/components/ui/skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { useContas } from "@/hooks/use-contas";
import { useCategorias } from "@/hooks/use-categorias";
import { useAuth } from "@/context/AuthContext";
import { useInvalidarFinanceiro } from "@/hooks/use-invalidar-financeiro";
import { formatBRLSigned, formatData } from "@/lib/format";
import { categoriasCompativeis, destinoSugerido } from "@/lib/transacoes";
import { extratoService, transacaoService } from "@/services";
import { toast } from "@/hooks/use-toast";
import { SeletorDestino } from "./SeletorDestino";

// Status de StatusExtrato em que a automação ainda está trabalhando.
const STATUS_EM_PROCESSAMENTO = [
  "upload_recebido", "validando", "na_fila", "extraindo",
  "classificando", "aguardando_ia", "reprocessando",
];

// Status finais de erro — com eles (e sem lançamentos) o extrato pode ser reenviado.
const STATUS_COM_ERRO = [
  "erro_formato", "erro_extracao", "erro_classificacao", "erro_timeout", "cancelado",
];

const FILTROS = [
  { key: "todos", label: "Todos" },
  { key: "pendentes", label: "Pendentes" },
  { key: "confirmados", label: "Confirmados" },
];

const ehPendente = (t) => t.statusRevisao === "PENDENTE_REVISAO";
const ehConfirmado = (t) => t.statusRevisao === "CONFIRMADA" && !t.estornadoAt && t.indEstorno !== "S";

export function RevisarExtratoModal({ open, onOpenChange, extrato }) {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const invalidarFinanceiro = useInvalidarFinanceiro();
  const { data: contas = [] } = useContas();
  const { data: categorias = [] } = useCategorias();

  // Escolha do usuário por lançamento: { [transacaoId]: { destino, categoriaId } }.
  const [escolhas, setEscolhas] = useState({});
  const [filtro, setFiltro] = useState("todos");

  const conta = contas.find((c) => c.id === extrato?.contaId);
  const processando = STATUS_EM_PROCESSAMENTO.includes(extrato?.status);
  const podeReenviar = STATUS_COM_ERRO.includes(extrato?.status) && (extrato?.totalLancamentos ?? 0) === 0;

  const { data: lancamentos = [], isLoading } = useQuery({
    queryKey: ["transacoes-extrato", extrato?.id],
    queryFn: () => transacaoService.listarPorExtrato(extrato.id),
    enabled: open && !!extrato?.id,
    // Enquanto a automação processa, as transações ainda não existem: pergunta de novo.
    refetchInterval: open && processando ? 4000 : false,
  });

  // Cada lançamento começa com a direção sugerida pela extração; o usuário troca se quiser.
  // Devolver o mesmo objeto quando não há lançamento novo evita re-render em loop —
  // `lancamentos` é uma referência nova a cada render enquanto a query não resolveu.
  useEffect(() => {
    setEscolhas((atuais) => {
      const novos = lancamentos.filter((t) => !atuais[t.id]);
      if (novos.length === 0) return atuais;
      const proximas = { ...atuais };
      for (const t of novos) {
        proximas[t.id] = { destino: destinoSugerido(t) };
      }
      return proximas;
    });
  }, [lancamentos]);

  const definirEscolha = (id, patch) =>
    setEscolhas((atuais) => ({ ...atuais, [id]: { ...atuais[id], ...patch } }));

  // Corpo do revisar com a escolha atual do lançamento (usado no individual e no lote).
  const payloadRevisao = (t) => {
    const escolha = escolhas[t.id] ?? {};
    const categoria = categorias.find((c) => c.id === escolha.categoriaId);
    return {
      destino: escolha.destino ?? destinoSugerido(t),
      categoriaId: categoria?.id ?? null,
      categoriaCode: categoria?.code ?? null,
    };
  };

  const erroApi = (titulo) => (err) =>
    toast.error({ title: titulo, description: err?.response?.data?.error ?? "Tente novamente." });

  const { mutate: revisar, isPending: revisandoUm, variables: transacaoEmRevisao } = useMutation({
    mutationFn: (t) => transacaoService.revisar(t.id, t.code, payloadRevisao(t)),
    onSuccess: invalidarFinanceiro,
    onError: erroApi("Não foi possível confirmar a revisão"),
  });

  const { mutate: desfazerRevisao, isPending: desfazendo, variables: transacaoDesfazendo } = useMutation({
    mutationFn: (t) => transacaoService.desfazerRevisao(t.id, t.code),
    onSuccess: (_, t) => {
      // A escolha volta para a direção atual do lançamento, pronta para revisar de novo.
      definirEscolha(t.id, { destino: destinoSugerido(t), categoriaId: undefined });
      invalidarFinanceiro();
      toast.success({
        title: "Revisão estornada",
        description: "O lançamento voltou para revisão e a confiança da IA foi reduzida.",
      });
    },
    onError: erroApi("Não foi possível estornar a revisão"),
  });

  const pendentesLista = useMemo(() => lancamentos.filter(ehPendente), [lancamentos]);
  const confirmadosLista = useMemo(() => lancamentos.filter(ehConfirmado), [lancamentos]);

  const { mutate: revisarTudo, isPending: revisandoTudo } = useMutation({
    mutationFn: () => transacaoService.revisarLote(
      pendentesLista.map((t) => ({ id: t.id, code: t.code, ...payloadRevisao(t) })),
    ),
    onSuccess: ({ revisadas = [], falhas = [] }) => {
      invalidarFinanceiro();
      if (falhas.length === 0) {
        toast.success({
          title: "Extrato revisado",
          description: `${revisadas.length} lançamento(s) confirmado(s) com a classificação escolhida.`,
        });
      } else {
        toast.warning({
          title: `${revisadas.length} revisado(s), ${falhas.length} com erro`,
          description: falhas[0]?.erro ?? "Revise os lançamentos restantes individualmente.",
        });
      }
    },
    onError: erroApi("Não foi possível revisar o extrato"),
  });

  // Cancelar/reenviar devolvem o extrato atualizado; a lista de extratos (de onde o
  // modal lê o extrato aberto) é recarregada e volta a se atualizar sozinha.
  const { mutate: executarAcao, isPending: acaoPendente, variables: acaoEmCurso } = useMutation({
    mutationFn: (acao) => extratoService[acao](extrato.id, extrato.code),
    onSuccess: (_, acao) => {
      queryClient.invalidateQueries({ queryKey: ["extratos", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["transacoes-extrato", extrato?.id] });
      if (acao === "reenviar") queryClient.invalidateQueries({ queryKey: ["transacoes", user?.idUsuario] });
      toast.success({
        title: acao === "reenviar" ? "Extrato reenviado" : "Processamento cancelado",
        description: acao === "reenviar"
          ? "O arquivo voltou para a leitura. Os lançamentos aparecem aqui quando ficarem prontos."
          : "O extrato foi marcado com erro na classificação. Você pode reenviá-lo quando quiser.",
      });
    },
    onError: (err, acao) => {
      toast.error({
        title: acao === "reenviar" ? "Não foi possível reenviar o extrato" : "Não foi possível cancelar o processamento",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const ocupado = revisandoUm || revisandoTudo || desfazendo;
  const pendentes = pendentesLista.length;
  const contagem = { todos: lancamentos.length, pendentes, confirmados: confirmadosLista.length };
  const visiveis = filtro === "pendentes" ? pendentesLista
    : filtro === "confirmados" ? confirmadosLista
    : lancamentos;

  const descricaoModal = () => {
    if (processando) return "Estamos lendo o arquivo. Os lançamentos aparecem aqui assim que ficarem prontos.";
    if (podeReenviar) return "Não foi possível ler este extrato. Clique em Reenviar para tentar de novo com o mesmo arquivo.";
    if (pendentes > 0) return `${pendentes} lançamento(s) aguardando revisão. Escolha o tipo de cada um e clique em "Revisado" ou em "Revisar tudo".`;
    return "Todos os lançamentos já foram revisados.";
  };

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent className="max-w-xl">
        <ModalHeader>
          <ModalTitle className="truncate">{extrato?.arquivoNome ?? "Extrato"}</ModalTitle>
          <ModalDescription>{descricaoModal()}</ModalDescription>
        </ModalHeader>

        {conta && (
          <div className="flex items-center gap-2 px-3 py-2 rounded-xl bg-secondary text-[12px] font-semibold text-foreground">
            <CreditCard className="w-3.5 h-3.5 text-muted-foreground shrink-0" strokeWidth={2.25} />
            Conta: {conta.banco ?? conta.nome}
            {extrato?.status && <StatusBadge kind="extrato" value={extrato.status} className="ml-auto" />}
          </div>
        )}

        {lancamentos.length > 0 && (
          <div className="flex flex-wrap items-center justify-between gap-2">
            <div className="inline-flex bg-secondary rounded-full p-0.5" role="tablist" aria-label="Filtrar lançamentos">
              {FILTROS.map((f) => (
                <button
                  key={f.key}
                  type="button"
                  role="tab"
                  aria-selected={filtro === f.key}
                  onClick={() => setFiltro(f.key)}
                  className={`px-3 py-1 text-[11.5px] font-semibold rounded-full transition-all ${
                    filtro === f.key ? "bg-card text-foreground shadow-sm" : "text-muted-foreground"
                  }`}
                >
                  {f.label} <span className="tabular-nums opacity-60">{contagem[f.key]}</span>
                </button>
              ))}
            </div>
            <Button
              size="sm"
              leftIcon={CheckCheck}
              loading={revisandoTudo}
              disabled={pendentes === 0 || ocupado}
              onClick={() => revisarTudo()}
            >
              Revisar tudo{pendentes > 0 ? ` (${pendentes})` : ""}
            </Button>
          </div>
        )}

        <div className="rounded-2xl border border-border divide-y divide-border overflow-hidden max-h-[440px] overflow-y-auto">
          {isLoading ? (
            <>
              <SkeletonRow /> <SkeletonRow /> <SkeletonRow />
            </>
          ) : lancamentos.length === 0 ? (
            <div className="py-8 px-4">
              {processando ? (
                <div className="flex flex-col items-center gap-2 text-center">
                  <Loader2 className="w-5 h-5 text-primary animate-spin" strokeWidth={2.25} />
                  <p className="text-[13px] font-semibold text-foreground">Processando o extrato...</p>
                  <p className="text-[11.5px] text-muted-foreground">
                    A leitura por IA leva alguns segundos. Pode deixar esta tela aberta.
                  </p>
                </div>
              ) : (
                <EmptyState icon={Inbox} title="Nenhum lançamento" description="Este extrato não tem lançamentos." />
              )}
            </div>
          ) : visiveis.length === 0 ? (
            <div className="py-8 px-4">
              <EmptyState
                icon={Inbox}
                title={filtro === "pendentes" ? "Nada pendente" : "Nenhum lançamento confirmado"}
                description={filtro === "pendentes" ? "Todos os lançamentos deste extrato já foram revisados." : "Confirme lançamentos para vê-los aqui."}
              />
            </div>
          ) : (
            visiveis.map((t) => {
              const pendente = ehPendente(t);
              const confirmado = ehConfirmado(t);
              const escolha = escolhas[t.id] ?? {};
              const destino = pendente ? (escolha.destino ?? destinoSugerido(t)) : destinoSugerido(t);
              const positivo = destino === "RECEITA";
              const revisandoEsteItem = revisandoUm && transacaoEmRevisao?.id === t.id;
              const desfazendoEsteItem = desfazendo && transacaoDesfazendo?.id === t.id;

              return (
                <div key={t.id} className="px-3.5 py-3 space-y-2">
                  <div className="flex items-center gap-3">
                    <div className="flex-1 min-w-0">
                      <p className="text-[13px] font-semibold text-foreground truncate">
                        {t.descricao ?? t.estabelecimento ?? "Lançamento"}
                      </p>
                      <div className="flex items-center gap-2 mt-1">
                        <StatusBadge kind="revisao" value={t.statusRevisao} />
                        <span className="text-[11px] text-muted-foreground">{formatData(t.dataTransacao)}</span>
                      </div>
                    </div>
                    <div className="text-right shrink-0">
                      <p className={`text-[13px] font-extrabold tabular-nums ${positivo ? "text-success" : "text-foreground"}`}>
                        {formatBRLSigned(positivo ? Math.abs(t.valor) : -Math.abs(t.valor))}
                      </p>
                    </div>
                  </div>

                  {t.confiancaIa != null && (
                    <div className="max-w-[220px]">
                      <ConfidenceBar value={t.confiancaIa} label="Confiança da IA" />
                    </div>
                  )}

                  {pendente && (
                    <>
                      <SeletorDestino
                        value={destino}
                        onChange={(novo) => definirEscolha(t.id, { destino: novo, categoriaId: undefined })}
                        disabled={ocupado}
                      />

                      <div className="flex items-end gap-2">
                        {destino !== "ECONOMIA" ? (
                          <div className="flex-1 min-w-0">
                            <Combobox
                              items={categoriasCompativeis(categorias, destino)}
                              value={escolha.categoriaId}
                              onChange={(id) => definirEscolha(t.id, { categoriaId: id })}
                              placeholder="Categoria (opcional)"
                              disabled={ocupado}
                            />
                          </div>
                        ) : (
                          <p className="flex-1 text-[11px] text-muted-foreground">
                            Vai para o cofrinho da conta, sem entrar em gastos nem receitas.
                          </p>
                        )}
                        <Button
                          size="sm"
                          variant="secondary"
                          loading={revisandoEsteItem}
                          disabled={ocupado && !revisandoEsteItem}
                          onClick={() => revisar(t)}
                          className="shrink-0"
                        >
                          Revisado
                        </Button>
                      </div>
                    </>
                  )}

                  {confirmado && (
                    <div className="flex justify-end">
                      <Button
                        size="sm"
                        variant="ghost"
                        leftIcon={Undo2}
                        loading={desfazendoEsteItem}
                        disabled={ocupado && !desfazendoEsteItem}
                        onClick={() => desfazerRevisao(t)}
                      >
                        Estornar revisão
                      </Button>
                    </div>
                  )}
                </div>
              );
            })
          )}
        </div>

        {(processando || podeReenviar) && (
          <div className="flex justify-end gap-2">
            {processando && (
              <Button
                size="sm"
                variant="secondary"
                loading={acaoPendente && acaoEmCurso === "cancelar"}
                disabled={acaoPendente}
                onClick={() => executarAcao("cancelar")}
              >
                <XCircle className="w-3.5 h-3.5" strokeWidth={2.25} /> Cancelar
              </Button>
            )}
            {podeReenviar && (
              <Button
                size="sm"
                loading={acaoPendente && acaoEmCurso === "reenviar"}
                disabled={acaoPendente}
                onClick={() => executarAcao("reenviar")}
              >
                <RotateCw className="w-3.5 h-3.5" strokeWidth={2.25} /> Reenviar
              </Button>
            )}
          </div>
        )}
      </ModalContent>
    </Modal>
  );
}
