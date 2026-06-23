import { useQuery } from "@tanstack/react-query";
import { contaFinanceiraService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useContas() {
  return useQuery({
    queryKey: ["contas", USUARIO_ID],
    queryFn: async () => {
      const result = await contaFinanceiraService.listarPorUsuario(USUARIO_ID);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de contas");
      return result;
    },
  });
}
