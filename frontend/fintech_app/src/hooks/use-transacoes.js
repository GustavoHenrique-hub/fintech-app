import { useQuery } from "@tanstack/react-query";
import { transacaoService } from "@/services";
import { useAuth } from "@/context/AuthContext";

export function useTransacoes() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["transacoes", id],
    queryFn: async () => {
      const result = await transacaoService.listarPorUsuario(id);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de transacoes");
      return result;
    },
    enabled: !!id,
  });
}
