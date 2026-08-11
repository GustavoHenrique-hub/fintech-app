// Modal "Revisar extrato": abre ao clicar num arquivo importado na tela de
// Extratos e lista os lançamentos criados a partir dele.
//
// Fluxo de negócio: lançamentos importados nascem com statusRevisao=PENDENTE_REVISAO
// (ImportarExtratoUseCase). Aqui o usuário revisa cada um, confere a conta a que
// pertence (a mesma já vinculada ao extrato — não é possível trocar por aqui) e
// clica em "Revisado", que chama PATCH /transacoes/{id}/{code}/revisar
// (TransacaoController#revisar) e marca o lançamento como CONFIRMADA. O backend
// também atualiza os contadores agregados do extrato (pendentes/confirmados/status).
import { CreditCard, Inbox } from "lucide-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { StatusBadge } from "@/components/ui/status-badge";
import { SkeletonRow } from "@/components/ui/skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { useContas } from "@/hooks/use-contas";
import { useAuth } from "@/context/AuthContext";
import { formatBRLSigned, formatData } from "@/lib/format";
import { transacaoService } from "@/services";
import { toast } from "@/hooks/use-toast";

export function RevisarExtratoModal({ open, onOpenChange, extrato }) {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const { data: contas = [] } = useContas();

  const conta = contas.find((c) => c.id === extrato?.contaId);

  const { data: lancamentos = [], isLoading } = useQuery({
    queryKey: ["transacoes-extrato", extrato?.id],
    queryFn: () => transacaoService.listarPorExtrato(extrato.id),
    enabled: open && !!extrato?.id,
  });

  const { mutate: revisar, isPending, variables: transacaoEmRevisao } = useMutation({
    mutationFn: (t) => transacaoService.revisar(t.id, t.code),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["transacoes-extrato", extrato?.id] });
      queryClient.invalidateQueries({ queryKey: ["extratos", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["transacoes", user?.idUsuario] });
    },
    onError: (err) => {
      toast.error({
        title: "Não foi possível confirmar a revisão",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const pendentes = lancamentos.filter((t) => t.statusRevisao === "PENDENTE_REVISAO").length;

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent className="max-w-lg">
        <ModalHeader>
          <ModalTitle className="truncate">{extrato?.arquivoNome ?? "Extrato"}</ModalTitle>
          <ModalDescription>
            {pendentes > 0
              ? `${pendentes} lançamento(s) aguardando revisão. Confira a conta e clique em "Revisado".`
              : "Todos os lançamentos já foram revisados."}
          </ModalDescription>
        </ModalHeader>

        {conta && (
          <div className="flex items-center gap-2 px-3 py-2 rounded-xl bg-secondary text-[12px] font-semibold text-foreground">
            <CreditCard className="w-3.5 h-3.5 text-muted-foreground shrink-0" strokeWidth={2.25} />
            Conta: {conta.banco ?? conta.nome}
          </div>
        )}

        <div className="rounded-2xl border border-border divide-y divide-border overflow-hidden max-h-[420px] overflow-y-auto">
          {isLoading ? (
            <>
              <SkeletonRow /> <SkeletonRow /> <SkeletonRow />
            </>
          ) : lancamentos.length === 0 ? (
            <div className="py-8 px-4">
              <EmptyState icon={Inbox} title="Nenhum lançamento" description="Este extrato não tem lançamentos." />
            </div>
          ) : (
            lancamentos.map((t) => {
              const pendente = t.statusRevisao === "PENDENTE_REVISAO";
              const positivo = t.tipo === "RECEITA";
              const revisandoEsteItem = isPending && transacaoEmRevisao?.id === t.id;

              return (
                <div key={t.id} className="flex items-center gap-3 px-3.5 py-3">
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
                      {formatBRLSigned(positivo ? t.valor : -t.valor)}
                    </p>
                  </div>
                  {pendente && (
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
