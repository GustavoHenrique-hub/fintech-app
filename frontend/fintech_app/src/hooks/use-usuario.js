import { useQuery } from "@tanstack/react-query";
import { usuarioService } from "@/services";
import { useAuth } from "@/context/AuthContext";

export function useUsuario() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["usuario", id],
    queryFn: () => usuarioService.buscarPorId(id),
    enabled: !!id,
  });
}
