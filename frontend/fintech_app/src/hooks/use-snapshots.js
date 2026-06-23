import { useQuery } from "@tanstack/react-query";
import { snapshotFinanceiroService } from "@/services";
import { useAuth } from "@/context/AuthContext";

export function useSnapshots() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["snapshots", id],
    queryFn: async () => {
      const result = await snapshotFinanceiroService.listarPorUsuario(id);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de snapshots");
      return result;
    },
    enabled: !!id,
  });
}
