// Button do design system.
// Variantes: primary | secondary | danger | ghost
// Tamanhos:  sm | md | lg
// Props extras: loading (mostra spinner e desabilita), disabled, leftIcon, rightIcon.
//
// Notas:
//  - Usamos `cva` (class-variance-authority) pra montar as classes
//    deterministicamente a partir das props — evita ternários espalhados.
//  - `forwardRef` é importante: pais (form libs, focus trap) precisam de ref.
//  - `aria-busy` + `aria-disabled` deixam o estado acessível a leitores de tela.
import * as React from "react";
import { cva } from "class-variance-authority";
import { Loader2 } from "lucide-react";

import { cn } from "@/lib/utils";

const buttonVariants = cva(
  // Classes base sempre aplicadas.
  "inline-flex items-center justify-center gap-2 rounded-xl font-semibold transition-all " +
    "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/40 " +
    "disabled:opacity-50 disabled:pointer-events-none active:scale-[0.98]",
  {
    variants: {
      variant: {
        primary:
          "bg-gradient-to-r from-primary to-accent text-primary-foreground shadow-md shadow-primary/25 hover:opacity-95",
        secondary:
          "bg-secondary text-foreground border border-border hover:bg-muted",
        danger:
          "bg-destructive text-destructive-foreground hover:bg-destructive/90 shadow-md shadow-destructive/25",
        ghost:
          "bg-transparent text-foreground hover:bg-secondary",
      },
      size: {
        sm: "h-8 px-3 text-[12px]",
        md: "h-10 px-4 text-[13.5px]",
        lg: "h-12 px-5 text-[15px]",
      },
    },
    defaultVariants: { variant: "primary", size: "md" },
  },
);

export const Button = React.forwardRef(
  (
    {
      className,
      variant,
      size,
      loading = false,
      disabled = false,
      leftIcon: LeftIcon,
      rightIcon: RightIcon,
      children,
      type = "button",
      ...props
    },
    ref,
  ) => {
    // Quando `loading` está ativo o botão precisa ficar desabilitado também,
    // senão dá pra clicar duas vezes e enviar a request 2x.
    const isDisabled = disabled || loading;

    return (
      <button
        ref={ref}
        type={type}
        disabled={isDisabled}
        aria-busy={loading || undefined}
        aria-disabled={isDisabled || undefined}
        className={cn(buttonVariants({ variant, size }), className)}
        {...props}
      >
        {loading ? (
          // Spinner substitui o leftIcon enquanto carrega.
          <Loader2 className="w-4 h-4 animate-spin" aria-hidden="true" />
        ) : (
          LeftIcon && <LeftIcon className="w-4 h-4" aria-hidden="true" />
        )}
        {children}
        {!loading && RightIcon && (
          <RightIcon className="w-4 h-4" aria-hidden="true" />
        )}
      </button>
    );
  },
);
Button.displayName = "Button";
