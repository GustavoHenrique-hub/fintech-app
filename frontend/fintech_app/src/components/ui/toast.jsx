// Primitivas visuais do Toast com 4 variantes (success/error/warning/info).
// Posicionamento canto inferior-direito é definido pelo ToastViewport.
//
// Auto-dismiss em 5s vem do `duration` que setamos no use-toast.js — o Radix
// dispara o `onOpenChange(false)` automaticamente quando o tempo expira.
import * as React from "react";
import * as ToastPrimitives from "@radix-ui/react-toast";
import { cva } from "class-variance-authority";
import { CheckCircle2, AlertCircle, AlertTriangle, Info, X } from "lucide-react";

import { cn } from "@/lib/utils";

const ToastProvider = ToastPrimitives.Provider;

// Viewport: canto inferior-direito no desktop, full-width no mobile.
const ToastViewport = React.forwardRef(({ className, ...props }, ref) => (
  <ToastPrimitives.Viewport
    ref={ref}
    className={cn(
      "fixed z-[100] flex max-h-screen w-full flex-col p-4 gap-2 " +
        // Mobile: empilha em cima.
        "top-0 sm:top-auto " +
        // Desktop: canto inferior-direito.
        "sm:bottom-0 sm:right-0 sm:max-w-[400px]",
      className,
    )}
    {...props}
  />
));
ToastViewport.displayName = ToastPrimitives.Viewport.displayName;

// Variantes visuais — cada uma tem cor de borda + acento próprios.
const toastVariants = cva(
  "group pointer-events-auto relative flex w-full items-start gap-3 rounded-2xl border p-4 pr-10 shadow-lg transition-all " +
    "data-[swipe=cancel]:translate-x-0 data-[swipe=end]:translate-x-[var(--radix-toast-swipe-end-x)] " +
    "data-[swipe=move]:translate-x-[var(--radix-toast-swipe-move-x)] data-[swipe=move]:transition-none " +
    "data-[state=open]:animate-in data-[state=closed]:animate-out " +
    "data-[state=closed]:fade-out-80 data-[state=closed]:slide-out-to-right-full " +
    "data-[state=open]:slide-in-from-bottom-full sm:data-[state=open]:slide-in-from-bottom-full",
  {
    variants: {
      variant: {
        success: "bg-card border-success/40",
        error: "bg-card border-destructive/40",
        warning: "bg-card border-accent/60",
        info: "bg-card border-border",
      },
    },
    defaultVariants: { variant: "info" },
  },
);

// Ícone+cor por variante.
const variantIcon = {
  success: { Icon: CheckCircle2, color: "text-success" },
  error: { Icon: AlertCircle, color: "text-destructive" },
  warning: { Icon: AlertTriangle, color: "text-accent-foreground" },
  info: { Icon: Info, color: "text-primary" },
};

const Toast = React.forwardRef(({ className, variant = "info", children, ...props }, ref) => {
  const { Icon, color } = variantIcon[variant] ?? variantIcon.info;
  return (
    <ToastPrimitives.Root
      ref={ref}
      className={cn(toastVariants({ variant }), className)}
      {...props}
    >
      <Icon className={cn("w-5 h-5 mt-0.5 shrink-0", color)} strokeWidth={2.25} aria-hidden="true" />
      <div className="flex-1 min-w-0">{children}</div>
    </ToastPrimitives.Root>
  );
});
Toast.displayName = ToastPrimitives.Root.displayName;

const ToastAction = React.forwardRef(({ className, ...props }, ref) => (
  <ToastPrimitives.Action
    ref={ref}
    className={cn(
      "inline-flex h-7 shrink-0 items-center justify-center rounded-md border border-border bg-transparent px-2.5 text-[11.5px] font-semibold " +
        "hover:bg-secondary transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/40",
      className,
    )}
    {...props}
  />
));
ToastAction.displayName = ToastPrimitives.Action.displayName;

const ToastClose = React.forwardRef(({ className, ...props }, ref) => (
  <ToastPrimitives.Close
    ref={ref}
    className={cn(
      "absolute right-2 top-2 rounded-md p-1 text-muted-foreground hover:text-foreground hover:bg-secondary transition-colors " +
        "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/40",
      className,
    )}
    toast-close=""
    {...props}
  >
    <X className="w-3.5 h-3.5" strokeWidth={2.25} />
  </ToastPrimitives.Close>
));
ToastClose.displayName = ToastPrimitives.Close.displayName;

const ToastTitle = React.forwardRef(({ className, ...props }, ref) => (
  <ToastPrimitives.Title
    ref={ref}
    className={cn("text-[13.5px] font-bold text-foreground leading-tight", className)}
    {...props}
  />
));
ToastTitle.displayName = ToastPrimitives.Title.displayName;

const ToastDescription = React.forwardRef(({ className, ...props }, ref) => (
  <ToastPrimitives.Description
    ref={ref}
    className={cn("text-[12px] text-muted-foreground mt-0.5 leading-snug", className)}
    {...props}
  />
));
ToastDescription.displayName = ToastPrimitives.Description.displayName;

export {
  ToastProvider,
  ToastViewport,
  Toast,
  ToastTitle,
  ToastDescription,
  ToastClose,
  ToastAction,
};
