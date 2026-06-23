import { useQuery } from "@tanstack/react-query";
import { snapshotFinanceiroService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useSnapshots() {
  return useQuery({
    queryKey: ["snapshots", USUARIO_ID],
    queryFn: async () => {
      const result = await snapshotFinanceiroService.listarPorUsuario(USUARIO_ID);
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de snapshots");
      return result;
    },
  });
}
