import { useQuery } from "@tanstack/react-query";
import { categoriaService } from "@/services";

export function useCategorias() {
  return useQuery({
    queryKey: ["categorias"],
    queryFn: () => categoriaService.listarPadrao(),
    staleTime: Infinity,
  });
}
