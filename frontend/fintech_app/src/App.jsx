import { BrowserRouter, Route, Routes } from "react-router-dom";

import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { AuthProvider } from "@/context/AuthContext";
import { ProtectedRoute } from "@/components/auth/ProtectedRoute";

import Index             from "./pages/Index.jsx";
import NotFound          from "./pages/NotFound.jsx";
import ComponentsShowcase from "./pages/ComponentsShowcase.jsx";
import LoginPage         from "./pages/auth/LoginPage.jsx";
import CadastroPage      from "./pages/auth/CadastroPage.jsx";
import EsquecerSenhaPage from "./pages/auth/EsquecerSenhaPage.jsx";

const App = () => (
  <TooltipProvider>
    <Toaster />
    <Sonner />

    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Rotas públicas */}
          <Route path="/login"         element={<LoginPage />} />
          <Route path="/cadastro"      element={<CadastroPage />} />
          <Route path="/esqueci-senha" element={<EsquecerSenhaPage />} />

          {/* Rotas protegidas */}
          <Route path="/" element={
            <ProtectedRoute><Index /></ProtectedRoute>
          } />
          <Route path="/components" element={
            <ProtectedRoute><ComponentsShowcase /></ProtectedRoute>
          } />

          {/* Catch-all */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  </TooltipProvider>
);

export default App;
