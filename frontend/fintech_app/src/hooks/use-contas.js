import { useQuery } from "@tanstack/react-query";
import { contaFinanceiraService } from "@/services";
import { useAuth } from "@/context/AuthContext";

export function useContas() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["contas", id],
    queryFn: async () => {
      const result = await contaFinanceiraService.listarPorUsuario(id);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de contas");
      return result;
    },
    enabled: !!id,
  });
}
