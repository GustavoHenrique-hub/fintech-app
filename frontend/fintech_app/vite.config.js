// Configuração do Vite (bundler/dev server).
// Documentação: https://vitejs.dev/config/
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

export default defineConfig(() => ({
  server: {
    // "::" escuta em todas as interfaces (IPv4 + IPv6).
    host: "::",
    port: 8080,
    hmr: {
      // Desliga o overlay de erros do HMR; erros continuam visíveis no console.
      overlay: false,
    },
  },
  plugins: [react()],
  resolve: {
    alias: {
      // Permite imports como "@/components/..." em vez de caminhos relativos longos.
      "@": path.resolve(__dirname, "./src"),
    },
    // Evita múltiplas cópias do React/React Query quando algum pacote
    // declara essas libs como dependências próprias.
    dedupe: [
      "react",
      "react-dom",
      "react/jsx-runtime",
      "react/jsx-dev-runtime",
      "@tanstack/react-query",
      "@tanstack/query-core",
    ],
  },
}));
