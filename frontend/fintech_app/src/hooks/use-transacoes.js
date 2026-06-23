import { useQuery } from "@tanstack/react-query";
import { transacaoService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useTransacoes() {
  return useQuery({
    queryKey: ["transacoes", USUARIO_ID],
    queryFn: async () => {
      const result = await transacaoService.listarPorUsuario(USUARIO_ID);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de transacoes");
      return result;
    },
  });
}
