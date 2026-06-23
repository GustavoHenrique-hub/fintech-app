// Configuração do Vite (bundler/dev server).
// Documentação: https://vitejs.dev/config/
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

export default defineConfig(() => ({
  server: {
    host: "::",
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:8082",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
    hmr: {
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
