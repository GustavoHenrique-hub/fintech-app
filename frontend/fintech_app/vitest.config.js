// Configuração do Vitest (runner de testes).
// Roda em ambiente jsdom para que testes possam manipular DOM.
import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react-swc";
import path from "path";

export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    // Permite usar describe/it/expect sem importar (estilo Jest global).
    globals: true,
    setupFiles: ["./src/test/setup.js"],
    // Casa apenas arquivos .test.js / .spec.js (não temos mais TS).
    include: ["src/**/*.{test,spec}.{js,jsx}"],
  },
  resolve: {
    alias: { "@": path.resolve(__dirname, "./src") },
  },
});
