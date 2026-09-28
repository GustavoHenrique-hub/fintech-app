// Detalhe de uma transação, aberto ao clicar num item da tela de Transações.
//
// Ações conforme o status de revisão:
//   · PENDENTE_REVISAO → revisar ali mesmo (Gasto/Receita/Economias + categoria),
//     a mesma regra do modal de revisão do extrato;
//   · CONFIRMADA       → "Estornar revisão" (volta a pendente, sai do saldo e a
//     confiança da IA cai) ou "Estornar" (estorno financeiro, via onEstornar).
import { useEffect, useState } from "react";
import { CheckCheck, RotateCcw, Undo2 } from "lucide-react";
import { useMutation } from "@tanstack/react-query";

import { transacaoService } from "@/services";
import { useInvalidarFinanceiro } from "@/hooks/use-invalidar-financeiro";
import { formatBRLSigned, formatData, formatHora } from "@/lib/format";
import { categoriasCompativeis, destinoSugerido, valorComSinal } from "@/lib/transacoes";
import { getIconeCategoria } from "@/lib/categoria-icones";
import { Button } from "@/components/ui/button";
import { Combobox } from "@/components/ui/combobox";
import { StatusBadge } from "@/components/ui/status-badge";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { toast } from "@/hooks/use-toast";
import { SeletorDestino } from "./SeletorDestino";

const ORIGEM_LABEL = { manual: "Manual", importado: "Extrato importado" };

function Linha({ label, children }) {
  return (
    <div className="flex items-start justify-between gap-4 py-2">
      <span className="text-[11.5px] text-muted-foreground font-medium shrink-0">{label}</span>
      <span className="text-[12.5px] text-foreground font-semibold text-right break-words min-w-0">{children}</span>
    </div>
  );
}

/**
 * @param {object|null} transacao    `null` fecha o modal
 * @param {object[]}    categorias
 * @param {() => void}  onClose
 * @param {(t: object) => void} onEstornar  abre a confirmação do estorno financeiro
 */
export function TransacaoDetalheModal({ transacao: t, categorias, onClose, onEstornar }) {
  const [destino, setDestino] = useState("GASTO");
  const [categoriaId, setCategoriaId] = useState(undefined);
  const invalidarFinanceiro = useInvalidarFinanceiro();

  useEffect(() => {
    setDestino(destinoSugerido(t));
    setCategoriaId(undefined);
  }, [t?.id]); // eslint-disable-line react-hooks/exhaustive-deps

  const aoErrar = (titulo) => (err) =>
    toast.error({ title: titulo, description: err?.response?.data?.error ?? "Tente novamente." });

  const { mutate: revisar, isPending: revisando } = useMutation({
    mutationFn: () => {
      const categoria = categorias.find((c) => c.id === categoriaId);
      return transacaoService.revisar(t.id, t.code, {
        destino,
        categoriaId: categoria?.id ?? null,
        categoriaCode: categoria?.code ?? null,
      });
    },
    onSuccess: () => {
      invalidarFinanceiro();
      toast.success({
        title: "Revisão confirmada",
        description: destino === "ECONOMIA"
          ? "O valor foi para as economias da conta."
          : "O lançamento entrou no saldo da conta.",
      });
      onClose();
    },
    onError: aoErrar("Não foi possível confirmar a revisão"),
  });

  const { mutate: desfazerRevisao, isPending: desfazendo } = useMutation({
    mutationFn: () => transacaoService.desfazerRevisao(t.id, t.code),
    onSuccess: () => {
      invalidarFinanceiro();
      toast.success({
        title: "Revisão estornada",
        description: "O lançamento voltou para revisão, saiu do saldo e a confiança da IA foi reduzida.",
      });
      onClose();
    },
    onError: aoErrar("Não foi possível estornar a revisão"),
  });

  const categoria = categorias.find((c) => c.id === t?.categoriaId);
  const Icone = getIconeCategoria(categoria?.icone);
  const valor = t ? valorComSinal(t) : 0;
  const pendente = t?.statusRevisao === "PENDENTE_REVISAO";
  const estornada = !!t?.estornadoAt || t?.indEstorno === "S";
  const confirmada = t?.statusRevisao === "CONFIRMADA" && !estornada;
  const ocupado = revisando || desfazendo;

  return (
    <Modal open={t !== null} onOpenChange={(o) => { if (!o) onClose(); }}>
      <ModalContent className="max-w-lg">
        {t && (
          <>
            <ModalHeader>
              <div className="flex items-start gap-3">
                <div
                  className="w-11 h-11 rounded-full flex items-center justify-center shrink-0"
                  style={{ backgroundColor: `${categoria?.corHex ?? "#94a3b8"}22` }}
                >
                  <Icone className="w-5 h-5" strokeWidth={2.25} style={{ color: categoria?.corHex ?? "var(--foreground)" }} />
                </div>
                <div className="min-w-0">
                  <ModalTitle className="leading-snug break-words">
                    {t.descricao ?? t.estabelecimento ?? "Lançamento"}
                  </ModalTitle>
                  <ModalDescription>
                    {categoria?.nome ?? "Sem categoria"} · {formatData(t.dataTransacao)}
                  </ModalDescription>
                </div>
              </div>
            </ModalHeader>

            <div className="flex items-end justify-between gap-3 px-3.5 py-3 rounded-2xl bg-secondary">
              <div>
                <p className="text-[10.5px] uppercase tracking-wider text-muted-foreground font-bold">
                  {t.tipo === "RECEITA" ? "Receita" : "Gasto"}
                </p>
                <p className={`text-[24px] font-extrabold tracking-tight tabular-nums ${valor > 0 ? "text-success" : "text-foreground"}`}>
                  {formatBRLSigned(valor)}
                </p>
              </div>
              <div className="flex flex-col items-end gap-1">
                <StatusBadge kind="revisao" value={t.statusRevisao} />
                {estornada && <span className="text-[11px] font-semibold text-destructive">Estornada</span>}
              </div>
            </div>

            <div className="divide-y divide-border">
              {t.estabelecimento && t.estabelecimento !== t.descricao && (
                <Linha label="Estabelecimento">{t.estabelecimento}</Linha>
              )}
              <Linha label="Origem">{ORIGEM_LABEL[t.origem] ?? t.origem}</Linha>
              <Linha label="Registrada em">{formatData(t.criadoEm)} · {formatHora(t.criadoEm)}</Linha>
              {t.recorrente && <Linha label="Recorrência">Recorrente</Linha>}
              {t.observacao && <Linha label="Observação">{t.observacao}</Linha>}
              <Linha label="Saldo da conta">{t.saldoAplicado ? "Já considerado" : "Entra ao confirmar"}</Linha>
              <Linha label="Código">{t.code}</Linha>
              {t.confiancaIa != null && (
                <div className="py-2.5">
                  <ConfidenceBar value={t.confiancaIa} label="Confiança da IA" />
                </div>
              )}
            </div>

            {pendente && (
              <div className="space-y-2 pt-1">
                <p className="section-label">Revisar lançamento</p>
                <SeletorDestino
                  value={destino}
                  onChange={(novo) => { setDestino(novo); setCategoriaId(undefined); }}
                  disabled={ocupado}
                />
                {destino !== "ECONOMIA" ? (
                  <Combobox
                    items={categoriasCompativeis(categorias, destino)}
                    value={categoriaId}
                    onChange={setCategoriaId}
                    placeholder="Categoria (opcional)"
                    disabled={ocupado}
                  />
                ) : (
                  <p className="text-[11px] text-muted-foreground">
                    Vai para o cofrinho da conta, sem entrar em gastos nem receitas.
                  </p>
                )}
              </div>
            )}

            <div className="flex flex-wrap justify-end gap-2 pt-1">
              <Button variant="ghost" onClick={onClose} disabled={ocupado}>Fechar</Button>
              {pendente && (
                <Button leftIcon={CheckCheck} loading={revisando} disabled={ocupado} onClick={() => revisar()}>
                  Revisar
                </Button>
              )}
              {confirmada && (
                <>
                  <Button
                    variant="secondary"
                    leftIcon={Undo2}
                    loading={desfazendo}
                    disabled={ocupado}
                    onClick={() => desfazerRevisao()}
                  >
                    Estornar revisão
                  </Button>
                  <Button variant="danger" leftIcon={RotateCcw} disabled={ocupado} onClick={() => onEstornar(t)}>
                    Estornar
                  </Button>
                </>
              )}
            </div>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
