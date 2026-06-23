import { useQuery } from "@tanstack/react-query";
import { contaFinanceiraService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useContas() {
  return useQuery({
    queryKey: ["contas", USUARIO_ID],
    queryFn: () => contaFinanceiraService.listarPorUsuario(USUARIO_ID),
  });
}
