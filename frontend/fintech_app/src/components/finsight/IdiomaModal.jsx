// Modal de seleção de idioma. Além do Português, traz os idiomas mais usuais
// no Brasil (inglês e espanhol são os mais comuns em apps e no dia a dia).
//
// Preferência é só de UI por enquanto — não há campo de idioma persistido no
// backend (Usuario não tem esse atributo), então guardamos localmente,
// seguindo o mesmo nível de fidelidade de "Notificações" nesta tela.
import { Check } from "lucide-react";

import { Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription } from "@/components/ui/modal";
import { cn } from "@/lib/utils";
import { IDIOMAS } from "@/lib/idiomas";

export function IdiomaModal({ open, onOpenChange, value, onChange }) {
  return (
    <Modal open={open} onOpenChange={onOpenChange}>
      <ModalContent className="max-w-sm">
        <ModalHeader>
          <ModalTitle>Idioma</ModalTitle>
          <ModalDescription>Escolha o idioma de exibição do app.</ModalDescription>
        </ModalHeader>

        <div className="rounded-2xl border border-border divide-y divide-border overflow-hidden">
          {IDIOMAS.map((idioma) => {
            const ativo = idioma.value === value;
            return (
              <button
                key={idioma.value}
                onClick={() => { onChange(idioma.value); onOpenChange(false); }}
                className={cn(
                  "w-full flex items-center justify-between px-3.5 py-3 text-left text-[13.5px] font-medium transition-colors",
                  ativo ? "bg-primary/8 text-primary font-semibold" : "text-foreground hover:bg-secondary",
                )}
              >
                {idioma.label}
                {ativo && <Check className="w-4 h-4 text-primary" strokeWidth={2.75} />}
              </button>
            );
          })}
        </div>
      </ModalContent>
    </Modal>
  );
}
