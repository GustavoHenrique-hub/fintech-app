import { useQuery } from "@tanstack/react-query";
import { transacaoService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useTransacoes() {
  return useQuery({
    queryKey: ["transacoes", USUARIO_ID],
    queryFn: () => transacaoService.listarPorUsuario(USUARIO_ID),
  });
}
