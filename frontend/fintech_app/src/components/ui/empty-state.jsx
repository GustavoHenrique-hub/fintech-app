// EmptyState: tela vazia padronizada.
// Estrutura: ícone (com background colorido) + título + descrição + CTA opcional.
//
// Use sempre que uma listagem retornar 0 itens — nada de tela em branco.
//
// API:
//   <EmptyState
//     icon={Inbox}
//     title="Nenhuma transação ainda"
//     description="Importe um extrato ou crie uma transação manual."
//     action={<Button>Importar extrato</Button>}
//   />
//
// Tom:
//   default | info | warning | danger | success
//   Controla a cor do ícone — útil pra estados específicos
//   (ex.: "erro de carregamento" vs "lista vazia").
import { cn } from "@/lib/utils";

const TONE_CLASSES = {
  default: "bg-secondary text-muted-foreground",
  info:    "bg-surface-purple text-primary",
  warning: "bg-surface-yellow text-foreground",
  danger:  "bg-surface-pink text-destructive",
  success: "bg-surface-green text-success",
};

export function EmptyState({
  icon: Icon,
  title,
  description,
  action,
  tone = "default",
  className,
}) {
  return (
    <div
      role="status"
      className={cn(
        "flex flex-col items-center text-center px-6 py-10 gap-3",
        className,
      )}
    >
      {Icon && (
        <div
          className={cn(
            "w-14 h-14 rounded-2xl flex items-center justify-center",
            TONE_CLASSES[tone] ?? TONE_CLASSES.default,
          )}
        >
          <Icon className="w-7 h-7" strokeWidth={2} aria-hidden="true" />
        </div>
      )}
      {title && (
        <p className="text-[15px] font-extrabold text-foreground tracking-tight">
          {title}
        </p>
      )}
      {description && (
        <p className="text-[12.5px] text-muted-foreground max-w-xs leading-relaxed">
          {description}
        </p>
      )}
      {action && <div className="mt-1">{action}</div>}
    </div>
  );
}
