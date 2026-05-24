// Componente raiz: encaixa os "providers" globais (toasts, tooltips, cache de
// dados e roteamento) ao redor das páginas. Tudo que precisa estar disponível
// em qualquer tela é registrado aqui uma única vez.
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";

import Index from "./pages/Index.jsx";
import NotFound from "./pages/NotFound.jsx";

// Uma única instância do QueryClient é criada fora do componente
// para que ela não seja recriada a cada render — caso contrário o cache do
// React Query seria descartado a cada atualização de estado.
const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      {/* Dois sistemas de toast convivem aqui: o radix-toast (Toaster) e o sonner. */}
      <Toaster />
      <Sonner />

      <BrowserRouter>
        <Routes>
          {/* Rota principal: renderiza a tela mobile da aplicação. */}
          <Route path="/" element={<Index />} />
          {/* IMPORTANTE: rotas customizadas devem ficar ACIMA do catch-all "*". */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
