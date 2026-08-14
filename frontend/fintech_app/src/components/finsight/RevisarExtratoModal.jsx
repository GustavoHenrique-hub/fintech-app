// Modal "Revisar extrato": abre ao clicar num arquivo importado na tela de
// Extratos (e logo após o upload) e lista os lançamentos criados a partir dele.
//
// Fluxo de negócio: lançamentos importados nascem com statusRevisao=PENDENTE_REVISAO
// numa categoria genérica — PDF passa pela automação N8N + IA, os demais formatos
// pelo parser local. Aqui o usuário escolhe, lançamento a lançamento, o que aquilo
// é de fato — Gasto, Receita ou Economias — e, opcionalmente, a categoria. Ao clicar
// em "Revisado" o app chama PATCH /transacoes/{id}/{code}/revisar
// (TransacaoController#revisar) com essa escolha:
//   · GASTO/RECEITA → transação CONFIRMADA, aparece na aba Transações
//     (o backend ajusta categoria/sinal e recalcula o saldo se a direção mudou);
//   · ECONOMIA      → vira aporte no sub-saldo de economias da conta e o
//     lançamento sai das listagens.
// O backend também atualiza os contadores agregados do extrato.
//
// Enquanto o extrato está em processamento (PDF na fila da automação) a lista é
// recarregada sozinha até os lançamentos chegarem pelo callback.
import { useEffect, useState } from "react";
import { ArrowUpRight, ArrowDownLeft, CreditCard, Inbox, Loader2, PiggyBank } from "lucide-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { Combobox } from "@/components/ui/combobox";
import { StatusBadge } from "@/components/ui/status-badge";
import { SkeletonRow } from "@/components/ui/skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { useContas } from "@/hooks/use-contas";
import { useCategorias } from "@/hooks/use-categorias";
import { useAuth } from "@/context/AuthContext";
import { formatBRLSigned, formatData } from "@/lib/format";
import { transacaoService } from "@/services";
import { toast } from "@/hooks/use-toast";

// Status de StatusExtrato em que a automação ainda está trabalhando.
const STATUS_EM_PROCESSAMENTO = [
  "upload_recebido", "validando", "na_fila", "extraindo",
  "classificando", "aguardando_ia", "reprocessando",
];

const DESTINOS = [
  { key: "GASTO", label: "Gasto", icon: ArrowUpRight, cor: "text-destructive" },
  { key: "RECEITA", label: "Receita", icon: ArrowDownLeft, cor: "text-success" },
  { key: "ECONOMIA", label: "Economias", icon: PiggyBank, cor: "text-primary" },
];

const normTipo = (t) => (t ?? "").toLowerCase();

function SeletorDestino({ value, onChange, disabled }) {
  return (
    <div className="grid grid-cols-3 gap-1 p-1 bg-secondary rounded-xl">
      {DESTINOS.map(({ key, label, icon: Icone, cor }) => (
        <button
          key={key}
          type="button"
          disabled={disabled}
          onClick={() => onChange(key)}
          aria-pressed={value === key}
          className={`flex items-center justify-center gap-1 py-1.5 rounded-lg text-[11.5px] font-semibold transition-all disabled:opacity-50 ${
            value === key ? `bg-card ${cor} shadow-sm` : "text-muted-foreground"
          }`}
        >
          <Icone className="w-3.5 h-3.5" strokeWidth={2.5} /> {label}
        </button>
      ))}
    </div>
  );
}

export function RevisarExtratoModal({ open, onOpenChange, extrato }) {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const { data: contas = [] } = useContas();
  const { data: categorias = [] } = useCategorias();

  // Escolha do usuário por lançamento: { [transacaoId]: { destino, categoriaId } }.
  const [escolhas, setEscolhas] = useState({});

  const conta = contas.find((c) => c.id === extrato?.contaId);
  const processando = STATUS_EM_PROCESSAMENTO.includes(extrato?.status);

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
        proximas[t.id] = { destino: t.tipo === "RECEITA" ? "RECEITA" : "GASTO" };
      }
      return proximas;
    });
  }, [lancamentos]);

  const definirEscolha = (id, patch) =>
    setEscolhas((atuais) => ({ ...atuais, [id]: { ...atuais[id], ...patch } }));

  const { mutate: revisar, isPending, variables: transacaoEmRevisao } = useMutation({
    mutationFn: (t) => {
      const escolha = escolhas[t.id] ?? {};
      const categoria = categorias.find((c) => c.id === escolha.categoriaId);
      return transacaoService.revisar(t.id, t.code, {
        destino: escolha.destino,
        categoriaId: categoria?.id ?? null,
        categoriaCode: categoria?.code ?? null,
      });
    },
    onSuccess: (_, t) => {
      queryClient.invalidateQueries({ queryKey: ["transacoes-extrato", extrato?.id] });
      queryClient.invalidateQueries({ queryKey: ["extratos", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["transacoes", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["contas", user?.idUsuario] });
      if (escolhas[t.id]?.destino === "ECONOMIA") {
        queryClient.invalidateQueries({ queryKey: ["economias", extrato?.contaId] });
      }
    },
    onError: (err) => {
      toast.error({
        title: "Não foi possível confirmar a revisão",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const pendentes = lancamentos.filter((t) => t.statusRevisao === "PENDENTE_REVISAO").length;

  const descricaoModal = () => {
    if (processando) return "Estamos lendo o arquivo. Os lançamentos aparecem aqui assim que ficarem prontos.";
    if (pendentes > 0) return `${pendentes} lançamento(s) aguardando revisão. Escolha o tipo de cada um e clique em "Revisado".`;
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
          ) : (
            lancamentos.map((t) => {
              const pendente = t.statusRevisao === "PENDENTE_REVISAO";
              const escolha = escolhas[t.id] ?? {};
              const destino = escolha.destino ?? (t.tipo === "RECEITA" ? "RECEITA" : "GASTO");
              const positivo = destino === "RECEITA";
              const revisandoEsteItem = isPending && transacaoEmRevisao?.id === t.id;

              // A categoria precisa ser compatível com o destino escolhido — as do tipo
              // "ambos" servem para os dois lados. Economias não usa categoria.
              const itensCategoria = categorias
                .filter((c) => [normTipo(destino), "ambos"].includes(normTipo(c.tipo)))
                .map((c) => ({ id: c.id, label: c.nome, parentId: c.parentId }));

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

                  {pendente && (
                    <>
                      <SeletorDestino
                        value={destino}
                        onChange={(novo) => definirEscolha(t.id, { destino: novo, categoriaId: undefined })}
                        disabled={isPending}
                      />

                      <div className="flex items-end gap-2">
                        {destino !== "ECONOMIA" ? (
                          <div className="flex-1 min-w-0">
                            <Combobox
                              items={itensCategoria}
                              value={escolha.categoriaId}
                              onChange={(id) => definirEscolha(t.id, { categoriaId: id })}
                              placeholder="Categoria (opcional)"
                              disabled={isPending}
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
                          disabled={isPending && !revisandoEsteItem}
                          onClick={() => revisar(t)}
                          className="shrink-0"
                        >
                          Revisado
                        </Button>
                      </div>
                    </>
                  )}
                </div>
              );
            })
          )}
        </div>
      </ModalContent>
    </Modal>
  );
}
