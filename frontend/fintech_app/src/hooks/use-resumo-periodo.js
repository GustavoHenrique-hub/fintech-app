import { useQuery } from "@tanstack/react-query";
import { transacaoService } from "@/services";
import { useAuth } from "@/context/AuthContext";

/**
 * Resumo (receitas/gastos/saldo) de uma conta num intervalo de datas.
 *
 * @param {object} params
 * @param {object} params.conta   conta atual (precisa de `id` e `code`)
 * @param {string} params.inicio  "yyyy-MM-dd"
 * @param {string} params.fim     "yyyy-MM-dd"
 */
export function useResumoPeriodo({ conta, inicio, fim }) {
  const { user } = useAuth();
  const usuarioId = user?.idUsuario;
  const contaId = conta?.id;

  return useQuery({
    queryKey: ["resumo-periodo", usuarioId, contaId, inicio, fim],
    queryFn: () => transacaoService.buscarResumoPeriodo(
      usuarioId, user?.usuarioCode, contaId, conta?.code, inicio, fim,
    ),
    enabled: !!usuarioId && !!contaId && !!inicio && !!fim,
  });
}
