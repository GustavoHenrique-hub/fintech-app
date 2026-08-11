// Modal genérico de edição de campo simples (VARCHAR) do usuário: e-mail ou telefone.
//
// Backend: UsuarioController#atualizar (PATCH /usuarios/{id}). CPF nunca passa por
// aqui — é imutável pelo app, só é alterado via ofício direto no banco.
import { useEffect, useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { usuarioService } from "@/services";
import { toast } from "@/hooks/use-toast";

const CAMPOS = {
  email: {
    titulo: "Editar e-mail",
    descricao: "Usado para login e comunicações importantes da sua conta.",
    label: "E-mail",
    type: "email",
    autoComplete: "email",
    placeholder: "voce@email.com",
  },
  telefone: {
    titulo: "Editar telefone",
    descricao: "Usado para notificações e recuperação de conta.",
    label: "Telefone",
    type: "tel",
    autoComplete: "tel",
    placeholder: "+55 11 9 8765-4321",
  },
};

export function EditarContatoModal({ open, onOpenChange, campo, usuarioId, valorAtual }) {
  const config = CAMPOS[campo];
  const queryClient = useQueryClient();
  const [valor, setValor] = useState(valorAtual ?? "");

  useEffect(() => {
    if (open) setValor(valorAtual ?? "");
  }, [open, valorAtual]);

  const erroEmail = campo === "email" && touched(valor) && !valor.trim().includes("@");

  const { mutate: salvar, isPending } = useMutation({
    mutationFn: () => usuarioService.atualizar(usuarioId, { [campo]: valor.trim() }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["usuario", usuarioId] });
      toast.success({ title: `${config.label} atualizado`, description: "Suas informações foram salvas." });
      onOpenChange(false);
    },
    onError: (err) => {
      toast.error({
        title: `Não foi possível atualizar o ${config.label.toLowerCase()}`,
        description: err?.response?.data?.error ?? "Tente novamente.",
      });
    },
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (campo === "email" && !valor.trim().includes("@")) return;
    if (valor.trim() === (valorAtual ?? "")) { onOpenChange(false); return; }
    salvar();
  };

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent>
        <ModalHeader>
          <ModalTitle>{config.titulo}</ModalTitle>
          <ModalDescription>{config.descricao}</ModalDescription>
        </ModalHeader>

        <form onSubmit={handleSubmit} noValidate>
          <label className="section-label" htmlFor={`campo-${campo}`}>{config.label}</label>
          <input
            id={`campo-${campo}`}
            type={config.type}
            value={valor}
            onChange={(e) => setValor(e.target.value)}
            autoComplete={config.autoComplete}
            placeholder={config.placeholder}
            className={`mt-1.5 w-full h-11 px-3.5 rounded-xl bg-card border text-[14px] outline-none
              placeholder:text-muted-foreground/55 transition-all focus:ring-2 focus:ring-primary/25 ${
                erroEmail ? "border-destructive/50 focus:border-destructive/40" : "border-border focus:border-primary/40"
              }`}
          />
          {erroEmail && <p className="text-[11.5px] text-destructive mt-1.5 font-medium">Informe um e-mail válido</p>}

          <ModalFooter>
            <ModalClose asChild>
              <Button type="button" variant="secondary" disabled={isPending}>Cancelar</Button>
            </ModalClose>
            <Button type="submit" loading={isPending}>Salvar</Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
}

function touched(v) {
  return v !== "" && v !== undefined && v !== null;
}
