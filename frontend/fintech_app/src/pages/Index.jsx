// Tela única que orquestra todo o app: mantém qual "screen" está ativa
// e troca o conteúdo renderizado dentro do PhoneFrame.
//
// Em apps reais isso normalmente viraria rotas (react-router), mas como o
// design é mobile-first e o usuário não troca de URL ao navegar entre abas,
// usamos um state local — é mais simples e mantém o histórico do navegador
// limpo.
import { useState } from "react";

import { PhoneFrame } from "@/components/finsight/PhoneFrame";
import { TopBar } from "@/components/finsight/TopBar";
import { BottomNav } from "@/components/finsight/BottomNav";
import { OverviewScreen } from "@/components/finsight/screens/OverviewScreen";
import { TransactionsScreen } from "@/components/finsight/screens/TransactionsScreen";
import { AddTransactionScreen } from "@/components/finsight/screens/AddTransactionScreen";
import { AnalyticsScreen } from "@/components/finsight/screens/AnalyticsScreen";
import { ProfileScreen } from "@/components/finsight/screens/ProfileScreen";

// Lista usada na "barra de demonstração" no topo (fora da moldura do celular)
// que ajuda a alternar telas durante o desenvolvimento.
const tabs = [
  { k: "home", l: "Overview" },
  { k: "analytics", l: "Analytics" },
  { k: "payments", l: "Transactions" },
  { k: "add", l: "Add" },
  { k: "profile", l: "Profile" },
];

const Index = () => {
  // Tela ativa. Strings em vez de enum porque é JS puro — simples e legível.
  const [screen, setScreen] = useState("home");

  return (
    <main className="min-h-screen bg-[#0c0d10]">
      {/* Heading invisível para acessibilidade / SEO. */}
      <h1 className="sr-only">FinSight — Personal Finance Mobile App</h1>

      {/* Barra de demonstração — útil para alternar telas vendo todas no mesmo
          contexto (não faz parte do app "real" do usuário). */}
      <div className="flex justify-center gap-1.5 pt-6 pb-2 px-4 flex-wrap">
        {tabs.map(({ k, l }) => (
          <button
            key={k}
            onClick={() => setScreen(k)}
            className={`px-3 py-1.5 rounded-full text-[11.5px] font-semibold transition-colors ${
              screen === k
                ? "bg-primary text-primary-foreground"
                : "bg-white/10 text-white/70 hover:bg-white/20"
            }`}
          >
            {l}
          </button>
        ))}
      </div>

      <PhoneFrame>
        <TopBar />

        {/* Renderização condicional da tela ativa. */}
        {screen === "home" && <OverviewScreen />}
        {screen === "analytics" && <AnalyticsScreen />}
        {screen === "payments" && <TransactionsScreen />}
        {screen === "add" && <AddTransactionScreen />}
        {screen === "profile" && <ProfileScreen />}

        {/* Quando a tela de "add" está aberta, mantemos a aba "payments"
            destacada (porque "add" não é uma aba na barra inferior). */}
        <BottomNav
          active={screen === "add" ? "payments" : screen}
          onChange={(t) => setScreen(t)}
          onAdd={() => setScreen("add")}
        />
      </PhoneFrame>
    </main>
  );
};

export default Index;
