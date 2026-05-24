// Configuração do Tailwind CSS.
// O objeto exportado descreve onde procurar classes (`content`), quais cores e
// variáveis fazem parte do design system, e quais plugins habilitar.
// Toda a paleta vem de variáveis CSS (definidas em src/index.css) para que
// trocar o tema seja trivial no futuro.

/** @type {import('tailwindcss').Config} */
export default {
  // "class" => dark mode ativado quando a classe "dark" estiver no <html>.
  darkMode: ["class"],
  // Glob das fontes de onde o Tailwind extrai as classes utilizadas.
  // Apenas .js/.jsx agora — sem TS.
  content: [
    "./pages/**/*.{js,jsx}",
    "./components/**/*.{js,jsx}",
    "./app/**/*.{js,jsx}",
    "./src/**/*.{js,jsx}",
    "./index.html",
  ],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: { "2xl": "1400px" },
    },
    extend: {
      // Mapeia tokens semânticos (primary, success, surface-purple, ...) para
      // as variáveis CSS — as cores HSL reais ficam no src/index.css.
      colors: {
        border: "hsl(var(--border))",
        input: "hsl(var(--input))",
        ring: "hsl(var(--ring))",
        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",
        primary: {
          DEFAULT: "hsl(var(--primary))",
          foreground: "hsl(var(--primary-foreground))",
        },
        secondary: {
          DEFAULT: "hsl(var(--secondary))",
          foreground: "hsl(var(--secondary-foreground))",
        },
        destructive: {
          DEFAULT: "hsl(var(--destructive))",
          foreground: "hsl(var(--destructive-foreground))",
        },
        muted: {
          DEFAULT: "hsl(var(--muted))",
          foreground: "hsl(var(--muted-foreground))",
        },
        accent: {
          DEFAULT: "hsl(var(--accent))",
          foreground: "hsl(var(--accent-foreground))",
        },
        success: {
          DEFAULT: "hsl(var(--success))",
          foreground: "hsl(var(--success-foreground))",
        },
        info: "hsl(var(--info))",
        // Tons pastéis usados como fundo de chips/ícones por categoria.
        surface: {
          yellow: "hsl(var(--surface-yellow))",
          purple: "hsl(var(--surface-purple))",
          pink: "hsl(var(--surface-pink))",
          green: "hsl(var(--surface-green))",
        },
        popover: {
          DEFAULT: "hsl(var(--popover))",
          foreground: "hsl(var(--popover-foreground))",
        },
        card: {
          DEFAULT: "hsl(var(--card))",
          foreground: "hsl(var(--card-foreground))",
        },
      },
      borderRadius: {
        lg: "var(--radius)",
        md: "calc(var(--radius) - 2px)",
        sm: "calc(var(--radius) - 4px)",
      },
      // Keyframes/animações usadas pelos componentes Radix (accordion, etc.).
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
};
