// Modal de alteração de senha — exige confirmação da senha atual.
// Backend: UsuarioController#alterarSenha (PATCH /usuarios/{id}/senha).
import { useEffect, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { Eye, EyeOff } from "lucide-react";

import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { PasswordStrengthMeter } from "@/components/ui/password-strength-meter";
import { usuarioService } from "@/services";
import { toast } from "@/hooks/use-toast";

const inputClass = (erro) =>
  `w-full h-11 px-3.5 pr-11 rounded-xl bg-card border text-[14px] outline-none
   placeholder:text-muted-foreground/55 transition-all focus:ring-2 focus:ring-primary/25 ${
     erro ? "border-destructive/50 focus:border-destructive/40" : "border-border focus:border-primary/40"
   }`;

export function AlterarSenhaModal({ open, onOpenChange, usuarioId }) {
  const [senhaAtual, setSenhaAtual] = useState("");
  const [novaSenha, setNovaSenha] = useState("");
  const [confirmar, setConfirmar] = useState("");
  const [senhaInfo, setSenhaInfo] = useState({ score: 0, level: "weak" });
  const [mostrar, setMostrar] = useState({ atual: false, nova: false, conf: false });
  const [touched, setTouched] = useState(false);

  const reset = () => {
    setSenhaAtual(""); setNovaSenha(""); setConfirmar("");
    setSenhaInfo({ score: 0, level: "weak" });
    setTouched(false);
  };

  useEffect(() => { if (!open) reset(); }, [open]);

  const erroConfirmar = touched && novaSenha !== confirmar;
  const erroFraca = touched && senhaInfo.level === "weak";
  const valido = senhaAtual.length > 0 && novaSenha === confirmar && senhaInfo.level !== "weak";

  const { mutate: salvar, isPending } = useMutation({
    mutationFn: () => usuarioService.alterarSenha(usuarioId, { senhaAtual, novaSenha }),
    onSuccess: () => {
      toast.success({ title: "Senha alterada", description: "Sua senha foi atualizada com sucesso." });
      onOpenChange(false);
    },
    onError: (err) => {
      const status = err?.response?.status;
      toast.error({
        title: status === 401 ? "Senha atual incorreta" : "Não foi possível alterar a senha",
        description: status === 401
          ? "Confira a senha atual e tente novamente."
          : (err?.response?.data?.error ?? "Tente novamente."),
      });
    },
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    setTouched(true);
    if (!valido) return;
    salvar();
  };

  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent>
        <ModalHeader>
          <ModalTitle>Alterar senha</ModalTitle>
          <ModalDescription>Confirme sua senha atual para definir uma nova.</ModalDescription>
        </ModalHeader>

        <form onSubmit={handleSubmit} noValidate className="space-y-4">
          <div>
            <label className="section-label" htmlFor="senha-atual">Senha atual</label>
            <div className="relative mt-1.5">
              <input
                id="senha-atual"
                type={mostrar.atual ? "text" : "password"}
                value={senhaAtual}
                onChange={(e) => setSenhaAtual(e.target.value)}
                autoComplete="current-password"
                placeholder="••••••••"
                className={inputClass(false)}
              />
              <button type="button" onClick={() => setMostrar((m) => ({ ...m, atual: !m.atual }))}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                aria-label={mostrar.atual ? "Ocultar senha" : "Mostrar senha"}>
                {mostrar.atual ? <EyeOff className="w-4 h-4" strokeWidth={2} /> : <Eye className="w-4 h-4" strokeWidth={2} />}
              </button>
            </div>
          </div>

          <div>
            <label className="section-label" htmlFor="senha-nova">Nova senha</label>
            <div className="relative mt-1.5">
              <input
                id="senha-nova"
                type={mostrar.nova ? "text" : "password"}
                value={novaSenha}
                onChange={(e) => setNovaSenha(e.target.value)}
                autoComplete="new-password"
                placeholder="••••••••"
                className={inputClass(erroFraca)}
              />
              <button type="button" onClick={() => setMostrar((m) => ({ ...m, nova: !m.nova }))}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                aria-label={mostrar.nova ? "Ocultar senha" : "Mostrar senha"}>
                {mostrar.nova ? <EyeOff className="w-4 h-4" strokeWidth={2} /> : <Eye className="w-4 h-4" strokeWidth={2} />}
              </button>
            </div>
            {novaSenha && <div className="mt-3"><PasswordStrengthMeter value={novaSenha} onChange={setSenhaInfo} /></div>}
          </div>

          <div>
            <label className="section-label" htmlFor="senha-conf">Confirmar nova senha</label>
            <div className="relative mt-1.5">
              <input
                id="senha-conf"
                type={mostrar.conf ? "text" : "password"}
                value={confirmar}
                onChange={(e) => setConfirmar(e.target.value)}
                autoComplete="new-password"
                placeholder="••••••••"
                className={inputClass(erroConfirmar)}
              />
              <button type="button" onClick={() => setMostrar((m) => ({ ...m, conf: !m.conf }))}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                aria-label="Mostrar confirmação">
                {mostrar.conf ? <EyeOff className="w-4 h-4" strokeWidth={2} /> : <Eye className="w-4 h-4" strokeWidth={2} />}
              </button>
            </div>
            {erroConfirmar && <p className="text-[11.5px] text-destructive mt-1.5 font-medium">As senhas não conferem</p>}
          </div>

          <ModalFooter>
            <ModalClose asChild>
              <Button type="button" variant="secondary" disabled={isPending}>Cancelar</Button>
            </ModalClose>
            <Button type="submit" loading={isPending}>Salvar nova senha</Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
}
