import { useQuery } from "@tanstack/react-query";
import { snapshotFinanceiroService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useSnapshots() {
  return useQuery({
    queryKey: ["snapshots", USUARIO_ID],
    queryFn: () => snapshotFinanceiroService.listarPorUsuario(USUARIO_ID),
  });
}
