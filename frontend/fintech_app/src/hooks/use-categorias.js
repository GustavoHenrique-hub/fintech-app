import { useQuery } from "@tanstack/react-query";
import { categoriaService } from "@/services";

export function useCategorias() {
  return useQuery({
    queryKey: ["categorias"],
    queryFn: async () => {
      const result = await categoriaService.listarPadrao();
      if (!Array.isArray(result)) throw new Error("Resposta inesperada: esperado array de categorias");
      return result;
    },
    staleTime: Infinity,
  });
}
