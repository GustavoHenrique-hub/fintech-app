import { useQuery } from "@tanstack/react-query";
import { extratoService } from "@/services";
import { useAuth } from "@/context/AuthContext";

// Status de StatusExtrato em que a automação (N8N + IA) ainda está trabalhando:
// enquanto algum extrato estiver assim, a lista se atualiza sozinha para o usuário
// ver quando os lançamentos ficam prontos para revisão.
const STATUS_EM_PROCESSAMENTO = [
  "upload_recebido", "validando", "na_fila", "extraindo",
  "classificando", "aguardando_ia", "reprocessando",
];

export function useExtratos() {
  const { user } = useAuth();
  const id = user?.idUsuario;
  return useQuery({
    queryKey: ["extratos", id],
    queryFn: () => extratoService.listarPorUsuario(id),
    enabled: !!id,
    refetchInterval: (query) =>
      (query.state.data ?? []).some((e) => STATUS_EM_PROCESSAMENTO.includes(e.status)) ? 4000 : false,
  });
}
