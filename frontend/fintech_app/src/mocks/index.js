// Barrel: agrupa todos os mocks pra facilitar o import.
//
//   import { transacoes, contaPadrao, categorias } from "@/mocks";
//
// Quando integrar com o backend, cada um destes vira um useQuery — basta
// trocar o consumo nas telas e remover esta pasta.
export { usuarioAtual } from "./usuario";
export { contas, contaPadrao } from "./contas";
export { categorias, categoriasPorId, categoriasGasto, categoriasReceita } from "./categorias";
export { transacoes } from "./transacoes";
export { snapshots, snapshotAtual } from "./snapshots";
export { extratos } from "./extratos";
