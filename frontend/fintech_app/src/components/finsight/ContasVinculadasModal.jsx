// Modal "Contas vinculadas": lista as contas do usuário e permite invalidar
// (soft delete) cada uma individualmente.
//
// Regra de negócio: remover uma conta não apaga o registro — o backend marca
// indDelete='S' e ativa=false (ContaFinanceiraController#remover). Histórico
// de transações é preservado. Ação é irreversível pelo app (só reativação
// via banco de dados), por isso exige confirmação em duas etapas.
import { useState } from "react";
import { CreditCard, AlertTriangle, Ban } from "lucide-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { useContas } from "@/hooks/use-contas";
import { useAuth } from "@/context/AuthContext";
import { formatBRL } from "@/lib/format";
import { getBancoLogoUrl, getBancoColor } from "@/lib/banco-utils";
import { toast } from "@/hooks/use-toast";
import { contaFinanceiraService } from "@/services";
import { SkeletonRow } from "@/components/ui/skeleton";

function BancoLogo({ nome, size = 36 }) {
  const [erro, setErro] = useState(false);
  const url = getBancoLogoUrl(nome);
  const cor = getBancoColor(nome);
  if (!url || erro) {
    return (
      <div
        className="rounded-xl flex items-center justify-center text-white font-extrabold shrink-0"
        style={{ width: size, height: size, backgroundColor: cor, fontSize: size * 0.38 }}
      >
        {(nome ?? "B")[0].toUpperCase()}
      </div>
    );
  }
  return (
    <img src={url} alt={nome} onError={() => setErro(true)} className="rounded-xl object-contain bg-white p-1 shrink-0"
      style={{ width: size, height: size }} />
  );
}

function ContaRow({ conta, onInvalidar, invalidando }) {
  const [confirmando, setConfirmando] = useState(false);
  const removida = conta.indDelete === "S" || !conta.ativa;

  return (
    <div className="flex items-center gap-3 px-3.5 py-3">
      <BancoLogo nome={conta.banco} size={36} />
      <div className="flex-1 min-w-0">
        <p className="text-[13px] font-semibold text-foreground truncate">{conta.nome}</p>
        <p className="text-[11.5px] text-muted-foreground">{formatBRL(conta.saldoAtual)}</p>
      </div>

      {removida ? (
        <span className="inline-flex items-center gap-1.5 rounded-full border px-2 py-0.5 text-[11px] font-semibold whitespace-nowrap bg-secondary text-muted-foreground border-border">
          <span className="w-1.5 h-1.5 rounded-full bg-muted-foreground" /> Invalidada
        </span>
      ) : confirmando ? (
        <div className="flex items-center gap-1.5 shrink-0">
          <Button size="sm" variant="secondary" onClick={() => setConfirmando(false)} disabled={invalidando}>
            Cancelar
          </Button>
          <Button size="sm" variant="danger" leftIcon={Ban} loading={invalidando} onClick={() => onInvalidar(conta)}>
            Confirmar
          </Button>
        </div>
      ) : (
        <Button size="sm" variant="secondary" onClick={() => setConfirmando(true)} className="shrink-0">
          Invalidar
        </Button>
      )}
    </div>
  );
}

export function ContasVinculadasModal({ open, onOpenChange }) {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const { data: contas = [], isLoading } = useContas();

  const { mutate: invalidar, isPending, variables: contaEmAndamento } = useMutation({
    mutationFn: (conta) => contaFinanceiraService.remover(conta.id, conta.code),
    onSuccess: (_, conta) => {
      queryClient.invalidateQueries({ queryKey: ["contas", user?.idUsuario] });
      toast.success({ title: "Conta invalidada", description: `${conta.nome} foi desvinculada da sua conta.` });
    },
    onError: (err) => {
      toast.error({
        title: "Não foi possível invalidar a conta",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent className="max-w-[480px]">
        <ModalHeader>
          <div className="flex items-start gap-3">
            <div className="w-10 h-10 rounded-full bg-surface-purple text-primary flex items-center justify-center shrink-0">
              <CreditCard className="w-5 h-5" strokeWidth={2.25} />
            </div>
            <div>
              <ModalTitle>Contas vinculadas</ModalTitle>
              <ModalDescription>
                Invalidar uma conta não apaga seu histórico — apenas a marca como inativa.
              </ModalDescription>
            </div>
          </div>
        </ModalHeader>

        <div className="rounded-2xl border border-border divide-y divide-border overflow-hidden max-h-[360px] overflow-y-auto">
          {isLoading ? (
            <>
              <SkeletonRow /> <SkeletonRow /> <SkeletonRow />
            </>
          ) : contas.length === 0 ? (
            <div className="py-8 px-4 text-center">
              <AlertTriangle className="w-6 h-6 text-muted-foreground mx-auto mb-2" strokeWidth={1.75} />
              <p className="text-[13px] text-muted-foreground">Nenhuma conta vinculada.</p>
            </div>
          ) : (
            contas.map((conta) => (
              <ContaRow
                key={`${conta.id}-${conta.code}`}
                conta={conta}
                onInvalidar={invalidar}
                invalidando={isPending && contaEmAndamento?.id === conta.id}
              />
            ))
          )}
        </div>
      </ModalContent>
    </Modal>
  );
}
