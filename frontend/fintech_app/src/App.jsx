// Componente raiz: encaixa os "providers" globais (toasts, tooltips) ao redor das
// páginas. O QueryClientProvider já vive em main.jsx — não duplicar aqui.
import { BrowserRouter, Route, Routes } from "react-router-dom";

import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";

import Index          from "./pages/Index.jsx";
import NotFound       from "./pages/NotFound.jsx";
import ComponentsShowcase from "./pages/ComponentsShowcase.jsx";
import LoginPage      from "./pages/auth/LoginPage.jsx";
import CadastroPage   from "./pages/auth/CadastroPage.jsx";
import EsquecerSenhaPage from "./pages/auth/EsquecerSenhaPage.jsx";

const App = () => (
  <TooltipProvider>
    <Toaster />
    <Sonner />

    <BrowserRouter>
      <Routes>
        {/* App principal */}
        <Route path="/"           element={<Index />} />
        <Route path="/components" element={<ComponentsShowcase />} />

        {/* Autenticação */}
        <Route path="/login"         element={<LoginPage />} />
        <Route path="/cadastro"      element={<CadastroPage />} />
        <Route path="/esqueci-senha" element={<EsquecerSenhaPage />} />

        {/* Catch-all */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  </TooltipProvider>
);

export default App;
