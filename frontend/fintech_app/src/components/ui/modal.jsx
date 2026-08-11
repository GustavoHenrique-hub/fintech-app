// Modal: dialog acessível com:
//   - Renderização em portal (fora da árvore do componente pai)
//   - Trap de foco automático enquanto está aberto
//   - Tecla Escape fecha
//   - Clique no overlay fecha (configurável)
//
// Usamos @radix-ui/react-dialog como base — focus trap manual seria muito
// código e fácil de quebrar. O Radix já faz portal, ARIA, lock de scroll, etc.
//
// API:
//   <Modal open={open} onOpenChange={setOpen} title="..." description="...">
//     conteúdo
//   </Modal>
//
//  - Para um modal não controlado (abre pelo trigger), use também `trigger`:
//   <Modal trigger={<Button>Abrir</Button>} title="...">...</Modal>
import * as React from "react";
import * as DialogPrimitive from "@radix-ui/react-dialog";
import { X } from "lucide-react";

import { cn } from "@/lib/utils";

const Modal = DialogPrimitive.Root;
const ModalTrigger = DialogPrimitive.Trigger;

// Overlay escuro semi-transparente atrás do modal.
const ModalOverlay = React.forwardRef(({ className, ...props }, ref) => (
  <DialogPrimitive.Overlay
    ref={ref}
    className={cn(
      "fixed inset-0 z-50 bg-black/50 backdrop-blur-sm " +
        "data-[state=open]:animate-in data-[state=closed]:animate-out " +
        "data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0",
      className,
    )}
    {...props}
  />
));
ModalOverlay.displayName = "ModalOverlay";

// Conteúdo do modal centralizado.
// `onPointerDownOutside` é o hook do Radix para clique no overlay. Por padrão
// bloqueamos o dismiss por clique fora — o usuário só fecha pelo "X" ou por um
// botão explícito (ex.: Cancelar) dentro da modal. Esc continua funcionando
// (mantém a modal acessível via teclado). Para reabilitar o clique fora em um
// caso específico, passe `onPointerDownOutside={() => {}}`.
const ModalContent = React.forwardRef(
  ({ className, children, hideClose = false, onPointerDownOutside, ...props }, ref) => (
    <DialogPrimitive.Portal>
      <ModalOverlay />
      <DialogPrimitive.Content
        ref={ref}
        onPointerDownOutside={onPointerDownOutside ?? ((e) => e.preventDefault())}
        className={cn(
          "fixed left-1/2 top-1/2 z-50 grid w-[92vw] max-w-md -translate-x-1/2 -translate-y-1/2 " +
            "gap-4 rounded-2xl border border-border bg-card p-5 shadow-2xl " +
            "data-[state=open]:animate-in data-[state=closed]:animate-out " +
            "data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 " +
            "data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95",
          className,
        )}
        {...props}
      >
        {children}
        {!hideClose && (
          <DialogPrimitive.Close
            aria-label="Fechar"
            className="absolute right-3 top-3 rounded-md p-1 text-muted-foreground hover:text-foreground hover:bg-secondary transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/40"
          >
            <X className="w-4 h-4" strokeWidth={2.25} />
          </DialogPrimitive.Close>
        )}
      </DialogPrimitive.Content>
    </DialogPrimitive.Portal>
  ),
);
ModalContent.displayName = "ModalContent";

// Helpers de header/footer/título/descrição — semânticos e estilizados.
const ModalHeader = ({ className, ...props }) => (
  <div className={cn("flex flex-col gap-1 text-left", className)} {...props} />
);

const ModalFooter = ({ className, ...props }) => (
  <div
    className={cn("flex flex-col-reverse sm:flex-row sm:justify-end gap-2 mt-2", className)}
    {...props}
  />
);

const ModalTitle = React.forwardRef(({ className, ...props }, ref) => (
  <DialogPrimitive.Title
    ref={ref}
    className={cn("text-[16px] font-extrabold tracking-tight text-foreground", className)}
    {...props}
  />
));
ModalTitle.displayName = "ModalTitle";

const ModalDescription = React.forwardRef(({ className, ...props }, ref) => (
  <DialogPrimitive.Description
    ref={ref}
    className={cn("text-[12.5px] text-muted-foreground leading-snug", className)}
    {...props}
  />
));
ModalDescription.displayName = "ModalDescription";

const ModalClose = DialogPrimitive.Close;

export {
  Modal,
  ModalTrigger,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalTitle,
  ModalDescription,
  ModalClose,
};
