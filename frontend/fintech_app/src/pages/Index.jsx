// Shell responsivo de um web app real:
//
//  • Mobile (< lg): TopBar fixa no topo, conteúdo rolável, BottomNav fixa na base.
//  • Desktop (≥ lg): SideNav fixa à esquerda, conteúdo ocupa toda a largura
//    restante e cresce até max-w-7xl. Sem BottomNav, sem moldura de celular.
//
// A navegação continua em state local — é o mesmo padrão do Linear, Notion
// e bancos digitais, onde a URL não muda a cada troca de aba.
import { useState } from "react";

import { TopBar } from "@/components/finsight/TopBar";
import { BottomNav } from "@/components/finsight/BottomNav";
import { SideNav } from "@/components/finsight/SideNav";
import { OverviewScreen } from "@/components/finsight/screens/OverviewScreen";
import { TransactionsScreen } from "@/components/finsight/screens/TransactionsScreen";
import { AddTransactionScreen } from "@/components/finsight/screens/AddTransactionScreen";
import { AnalyticsScreen } from "@/components/finsight/screens/AnalyticsScreen";
import { ProfileScreen } from "@/components/finsight/screens/ProfileScreen";

const Index = () => {
  const [screen, setScreen] = useState("home");
  const activeForNav = screen === "add" ? "payments" : screen;

  return (
    <div className="min-h-[100dvh] bg-background lg:flex">
      <h1 className="sr-only">FinSight — Personal Finance</h1>

      {/* Sidebar (somente desktop). Em telas menores ela não renderiza. */}
      <SideNav
        active={activeForNav}
        onChange={(t) => setScreen(t)}
        onAdd={() => setScreen("add")}
      />

      {/* Coluna principal: flex column em qualquer tamanho. */}
      <div className="flex flex-col min-h-[100dvh] flex-1 min-w-0">
        <TopBar />

        <main className="flex-1 min-h-0 flex flex-col overflow-hidden">
          {screen === "home" && <OverviewScreen />}
          {screen === "analytics" && <AnalyticsScreen />}
          {screen === "payments" && <TransactionsScreen />}
          {screen === "add" && <AddTransactionScreen />}
          {screen === "profile" && <ProfileScreen />}
        </main>

        {/* BottomNav só em telas menores que lg. */}
        <BottomNav
          active={activeForNav}
          onChange={(t) => setScreen(t)}
          onAdd={() => setScreen("add")}
        />
      </div>
    </div>
  );
};

export default Index;
