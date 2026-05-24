// Renderiza a lista de toasts ativos.
// Lê do store via useToast e materializa cada item nas primitivas Radix.
import { useToast } from "@/hooks/use-toast";
import {
  Toast,
  ToastClose,
  ToastDescription,
  ToastProvider,
  ToastTitle,
  ToastViewport,
} from "@/components/ui/toast";

export function Toaster() {
  const { toasts } = useToast();

  return (
    // `duration` global: 5s = autodismiss da spec. Pode ser sobrescrito por toast.
    <ToastProvider duration={5000} swipeDirection="right">
      {toasts.map(({ id, title, description, action, variant, duration, ...props }) => (
        <Toast key={id} variant={variant} duration={duration} {...props}>
          {title && <ToastTitle>{title}</ToastTitle>}
          {description && <ToastDescription>{description}</ToastDescription>}
          {/* Ação inline opcional (ex.: botão "Desfazer"). Aparece à direita. */}
          {action && <div className="mt-2">{action}</div>}
          <ToastClose />
        </Toast>
      ))}
      <ToastViewport />
    </ToastProvider>
  );
}
