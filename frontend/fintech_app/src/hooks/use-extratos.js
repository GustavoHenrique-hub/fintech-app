import { useQuery } from "@tanstack/react-query";
import { extratoService } from "@/services";
import { useAuth } from "@/context/AuthContext";

export function useExtratos() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["extratos", id],
    queryFn: () => extratoService.listarPorUsuario(id),
    enabled: !!id,
  });
}
