import { useCallback } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { useAuth } from "@/context/AuthContext";

/**
 * Recarrega tudo que depende do saldo/classificação das transações: listas,
 * contas (saldo do card da Visão geral), resumos de período (KPIs), snapshots
 * e extratos. Chamar depois de confirmar/estornar revisão ou estornar transação.
 */
export function useInvalidarFinanceiro() {
  const queryClient = useQueryClient();
  const { user } = useAuth();
  const usuarioId = user?.idUsuario;

  return useCallback(() => {
    for (const key of [
      ["transacoes", usuarioId],
      ["contas", usuarioId],
      ["extratos", usuarioId],
      ["snapshots", usuarioId],
      ["resumo-periodo"],
      ["transacoes-extrato"],
      ["economias"],
    ]) {
      queryClient.invalidateQueries({ queryKey: key });
    }
  }, [queryClient, usuarioId]);
}
