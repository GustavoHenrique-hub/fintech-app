// ContaSelecionadaContext: qual conta financeira está "ativa" no momento.
//
// Antes, cada tela guardava sua própria noção de conta selecionada (na prática,
// só o OverviewScreen tinha um seletor — as demais telas sempre operavam sobre
// todas as contas). Isso significa que Transações, Análises e Nova Transação
// agora compartilham a mesma conta ativa que o usuário escolheu na Overview.
import { createContext, useContext, useMemo, useState } from "react";
import { useContas } from "@/hooks/use-contas";

const ContaSelecionadaContext = createContext(null);

export function ContaSelecionadaProvider({ children }) {
  const { data: contas = [], isLoading: loadingContas } = useContas();
  const [contaSelecionada, setContaSelecionada] = useState(null);

  // Conta padrão: a marcada como padrão no backend, ou a primeira da lista.
  const contaPadrao = useMemo(
    () => contas.find((c) => c.padrao) ?? contas[0] ?? null,
    [contas],
  );

  // A conta ativa é a selecionada manualmente ou, na ausência dela, a padrão.
  const contaAtual = contaSelecionada ?? contaPadrao;

  const value = { contas, contaAtual, setContaSelecionada, loadingContas };

  return (
    <ContaSelecionadaContext.Provider value={value}>
      {children}
    </ContaSelecionadaContext.Provider>
  );
}

export function useContaSelecionada() {
  const ctx = useContext(ContaSelecionadaContext);
  if (!ctx) throw new Error("useContaSelecionada deve ser usado dentro de ContaSelecionadaProvider");
  return ctx;
}
