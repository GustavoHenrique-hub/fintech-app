// Confirmação do estorno financeiro de uma transação (PATCH /transacoes/{id}/{code}/estornar).
// Usada pela tela "Estornar transações" e pelo detalhe da transação.
import { useEffect, useState } from "react";
import { RotateCcw, Info } from "lucide-react";
import { useMutation } from "@tanstack/react-query";

import { transacaoService } from "@/services";
import { useInvalidarFinanceiro } from "@/hooks/use-invalidar-financeiro";
import { formatBRL, formatDataRelativa } from "@/lib/format";
import { valorAbsoluto } from "@/lib/transacoes";
import { Button } from "@/components/ui/button";
import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription,
  ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { toast } from "@/hooks/use-toast";

const MIN_MOTIVO = 10;
const MAX_MOTIVO = 240;

const MOTIVOS_RAPIDOS = [
  "Cobrança duplicada",
  "Valor incorreto",
  "Estabelecimento não reconhecido",
  "Compra cancelada",
];

/**
 * @param {object|null} transacao  transação a estornar; `null` fecha o modal
 * @param {() => void}  onClose    chamado ao cancelar ou após estornar
 * @param {() => void}  [onEstornada]
 */
export function EstornarTransacaoModal({ transacao, onClose, onEstornada }) {
  const [motivo, setMotivo] = useState("");
  const [observacao, setObservacao] = useState("");
  const [touched, setTouched] = useState(false);
  const invalidarFinanceiro = useInvalidarFinanceiro();

  // Cada transação aberta começa com o formulário limpo.
  useEffect(() => {
    setMotivo("");
    setObservacao("");
    setTouched(false);
  }, [transacao?.id]);

  const { mutate: estornar, isPending } = useMutation({
    mutationFn: (t) => transacaoService.estornar(t.id, t.code, {
      // O backend (EstornarTransacaoRequestDTO) usa a conta da própria transação.
      // motivo/observacao são apenas UX no cliente — o endpoint não os consome.
      usuarioId: t.usuarioId,
      usuarioCode: t.usuarioCode,
      contaId: t.contaId,
      contaCode: t.contaCode,
    }),
    onSuccess: (_, t) => {
      invalidarFinanceiro();
      toast.success({
        title: "Estorno realizado",
        description: `${formatBRL(valorAbsoluto(t))} foi estornado de "${t.descricao ?? t.estabelecimento ?? "Lançamento"}".`,
      });
      setTimeout(() => {
        toast.info({
          title: "Crédito em até 2 dias úteis",
          description: "O valor será creditado conforme o prazo do emissor.",
        });
      }, 250);
      onEstornada?.();
      onClose();
    },
    onError: (error) => {
      const status = error?.response?.status;
      if (status === 422 || status === 400) {
        toast.warning({
          title: "Estorno não permitido",
          description: error?.response?.data?.message ?? "A janela de estorno para essa transação expirou.",
        });
      } else {
        toast.error({
          title: "Falha de comunicação",
          description: "Não conseguimos contatar o serviço de estornos. Tente novamente em alguns instantes.",
        });
      }
    },
  });

  const confirmar = () => {
    setTouched(true);
    if (!transacao) return;
    if (motivo.trim().length < MIN_MOTIVO) {
      toast.warning({
        title: "Motivo muito curto",
        description: `Descreva o motivo com pelo menos ${MIN_MOTIVO} caracteres.`,
      });
      return;
    }
    estornar(transacao);
  };

  const erroMotivo = touched && motivo.trim().length < MIN_MOTIVO;

  return (
    <Modal open={transacao !== null} onOpenChange={(o) => { if (!o) onClose(); }}>
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

        {transacao && (
          <div className="px-1 space-y-4">
            <div className="card-soft p-3">
              <p className="text-[12px] text-muted-foreground font-semibold">
                {transacao.estabelecimento ?? transacao.descricao}
              </p>
              <p className="text-[15px] font-extrabold text-foreground tracking-tight tabular-nums">
                {formatBRL(valorAbsoluto(transacao))}
              </p>
              <p className="text-[11px] text-muted-foreground mt-0.5">
                {formatDataRelativa(transacao.dataTransacao)} · Código {transacao.code}
              </p>
            </div>

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
            <Button variant="secondary" disabled={isPending}>Cancelar</Button>
          </ModalClose>
          <Button variant="danger" leftIcon={RotateCcw} loading={isPending} onClick={confirmar}>
            Confirmar estorno
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
