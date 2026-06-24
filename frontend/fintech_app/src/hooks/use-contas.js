import { useQuery } from "@tanstack/react-query";
import { contaFinanceiraService, bancoService } from "@/services";
import { useAuth } from "@/context/AuthContext";

// O ContaFinanceiraResponseDTO do backend expõe apenas bancoId/bancoCode — não traz o
// nome do banco nem um campo "nome" da conta. Para a UI exibir o banco vinculado, cruzamos
// cada conta com o catálogo /bancos (BancoResponseDTO: nome, corHex, icone) e anexamos
// `banco` (nome), `corHex`, `icone` e um `nome` amigável de fallback.
export function useContas() {
  const { user } = useAuth();
  const id = user?.idUsuario;

  return useQuery({
    queryKey: ["contas", id],
    queryFn: async () => {
      const [contas, bancos] = await Promise.all([
        contaFinanceiraService.listarPorUsuario(id),
        bancoService.listar(),
      ]);
      if (!Array.isArray(contas)) throw new Error("Resposta inesperada: esperado array de contas");

      const catalogo = Array.isArray(bancos) ? bancos : [];
      const porId   = new Map(catalogo.map((b) => [b.id, b]));
      const porCode = new Map(catalogo.map((b) => [b.code, b]));

      return contas.map((c) => {
        const banco = porId.get(c.bancoId) ?? porCode.get(c.bancoCode) ?? null;
        const nomeBanco = banco?.nome ?? c.bancoCode ?? "Conta";
        return {
          ...c,
          banco:  nomeBanco,
          corHex: c.corHex ?? banco?.corHex ?? null,
          icone:  banco?.icone ?? null,
          nome:   c.nome ?? nomeBanco,
        };
      });
    },
    enabled: !!id,
  });
}
