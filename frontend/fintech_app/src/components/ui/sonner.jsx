// Toaster alternativo (lib "sonner"): visual mais moderno e API mais simples
// que o radix-toast. Mantemos os dois ativos pra dar liberdade ao restante do
// código de escolher qual usar.
import { useTheme } from "next-themes";
import { Toaster as Sonner, toast } from "sonner";

const Toaster = ({ ...props }) => {
  // Sincroniza o tema dos toasts com o tema atual da aplicação.
  const { theme = "system" } = useTheme();

  return (
    <Sonner
      theme={theme}
      className="toaster group"
      // Classes Tailwind que estilizam cada parte do toast para combinar
      // com o design system (background, border, sombras, etc.).
      toastOptions={{
        classNames: {
          toast:
            "group toast group-[.toaster]:bg-background group-[.toaster]:text-foreground group-[.toaster]:border-border group-[.toaster]:shadow-lg",
          description: "group-[.toast]:text-muted-foreground",
          actionButton:
            "group-[.toast]:bg-primary group-[.toast]:text-primary-foreground",
          cancelButton:
            "group-[.toast]:bg-muted group-[.toast]:text-muted-foreground",
        },
      }}
      {...props}
    />
  );
};

export { Toaster, toast };
