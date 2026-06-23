import { useQuery } from "@tanstack/react-query";
import { usuarioService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";

export function useUsuario() {
  return useQuery({
    queryKey: ["usuario", USUARIO_ID],
    queryFn: () => usuarioService.buscarPorId(USUARIO_ID),
  });
}
