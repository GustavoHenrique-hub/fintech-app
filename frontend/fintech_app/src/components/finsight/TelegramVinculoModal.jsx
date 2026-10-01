// Vínculo do Telegram (Perfil → Integrações).
//
// Fluxo: registra o consentimento LGPD `bot_telegram` → backend gera código de uso
// único → usuário abre t.me/<bot>?start=<codigo> → o bot (workflow n8n 03) chama o
// backend com código + chat_id. Enquanto o modal está aberto, o usuário é
// recarregado a cada 3s para detectar o vínculo e fechar sozinho.
import { useEffect, useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Copy, ExternalLink, Send, Unlink } from "lucide-react";

import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { consentimentoService, usuarioService } from "@/services";
import { toast } from "@/hooks/use-toast";

const VERSAO_POLITICA = "1.0";
const INTERVALO_VERIFICACAO_MS = 3000;

export function TelegramVinculoModal({ open, onOpenChange, usuarioId, telegramChatId }) {
  const queryClient = useQueryClient();
  const [aceite, setAceite] = useState(false);
  const [vinculo, setVinculo] = useState(null); // { codigo, link, expiraEm }

  const vinculado = !!telegramChatId;
  const recarregarUsuario = () => queryClient.invalidateQueries({ queryKey: ["usuario", usuarioId] });

  useEffect(() => {
    if (!open) {
      setAceite(false);
      setVinculo(null);
    }
  }, [open]);

  // Aguardando o /start no Telegram: consulta o usuário até o chat aparecer.
  useEffect(() => {
    if (!open || !vinculo || vinculado) return undefined;
    const timer = setInterval(recarregarUsuario, INTERVALO_VERIFICACAO_MS);
    return () => clearInterval(timer);
  }, [open, vinculo, vinculado]); // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    if (open && vinculo && vinculado) {
      toast.success({ title: "Telegram vinculado", description: "Agora você pode enviar extratos pelo bot." });
      onOpenChange(false);
    }
  }, [open, vinculo, vinculado]); // eslint-disable-line react-hooks/exhaustive-deps

  const { mutate: gerar, isPending: gerando } = useMutation({
    mutationFn: async () => {
      await consentimentoService.registrar({
        usuarioId,
        tipo: "bot_telegram",
        versaoPolitica: VERSAO_POLITICA,
        consentido: true,
      });
      return usuarioService.gerarCodigoVinculoTelegram();
    },
    onSuccess: (dados) => {
      setVinculo(dados);
      if (dados.link) window.open(dados.link, "_blank", "noopener,noreferrer");
    },
    onError: (err) => {
      toast.error({
        title: "Não foi possível gerar o link",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const { mutate: desvincular, isPending: desvinculando } = useMutation({
    mutationFn: () => usuarioService.desvincularTelegram(),
    onSuccess: () => {
      recarregarUsuario();
      toast.success({ title: "Telegram desvinculado" });
      onOpenChange(false);
    },
    onError: (err) => {
      toast.error({
        title: "Não foi possível desvincular",
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const comando = vinculo ? `/vincular ${vinculo.codigo}` : "";
  const copiarComando = async () => {
    try {
      await navigator.clipboard.writeText(comando);
      toast.success({ title: "Comando copiado" });
    } catch {
      toast.error({ title: "Não foi possível copiar", description: comando });
    }
  };

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent>
        <ModalHeader>
          <ModalTitle>Telegram</ModalTitle>
          <ModalDescription>
            {vinculado
              ? "Este app está vinculado a um chat do Telegram. Envie seus extratos em PDF ou foto para o bot."
              : "Vincule seu Telegram para enviar extratos direto pelo chat com o bot."}
          </ModalDescription>
        </ModalHeader>

        {vinculado && (
          <p className="text-[12.5px] text-muted-foreground">
            Chat vinculado: <span className="font-mono text-foreground">{telegramChatId}</span>
          </p>
        )}

        {!vinculado && !vinculo && (
          <label className="flex items-start gap-2.5 text-[12.5px] text-foreground cursor-pointer select-none">
            <input
              type="checkbox"
              checked={aceite}
              onChange={(e) => setAceite(e.target.checked)}
              className="mt-0.5 w-4 h-4 accent-primary"
            />
            <span>
              Autorizo o FinSight a receber meus extratos e a me enviar mensagens pelo Telegram.
              Posso revogar a qualquer momento desvinculando por aqui.
            </span>
          </label>
        )}

        {!vinculado && vinculo && (
          <div className="space-y-3 text-[12.5px]">
            <p className="text-foreground">
              {vinculo.link
                ? "Abrimos o Telegram em outra aba. Toque em Iniciar na conversa com o bot."
                : "Envie o comando abaixo para o bot no Telegram."}
            </p>
            <div className="flex items-center gap-2">
              <code className="flex-1 px-3 py-2 rounded-lg bg-secondary font-mono text-[13px] text-foreground truncate">
                {comando}
              </code>
              <Button variant="secondary" size="sm" leftIcon={Copy} onClick={copiarComando}>Copiar</Button>
            </div>
            <p className="text-muted-foreground">Aguardando o vínculo… o código vale 10 minutos.</p>
          </div>
        )}

        <ModalFooter>
          <ModalClose asChild>
            <Button variant="secondary" disabled={gerando || desvinculando}>Fechar</Button>
          </ModalClose>
          {vinculado && (
            <Button variant="danger" leftIcon={Unlink} loading={desvinculando} onClick={() => desvincular()}>
              Desvincular
            </Button>
          )}
          {!vinculado && !vinculo && (
            <Button leftIcon={Send} loading={gerando} disabled={!aceite} onClick={() => gerar()}>
              Vincular Telegram
            </Button>
          )}
          {!vinculado && vinculo?.link && (
            <Button leftIcon={ExternalLink} onClick={() => window.open(vinculo.link, "_blank", "noopener,noreferrer")}>
              Abrir no Telegram
            </Button>
          )}
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
