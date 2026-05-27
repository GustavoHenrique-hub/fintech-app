// Mock de categorias no formato do CategoriaResponseDTO.
//
// Origem: adapters/in/web/categoria/dto/CategoriaResponseDTO.java
//   id, code, nome, tipo (TipoCategoria), icone, corHex, padrao
//
// Enum TipoCategoria: receita | gasto | ambos
//
// Hierarquia (categorias_pai): no schema v2 é separada via tabela `categorias_pai`.
// Aqui usamos `parentId` no front pra renderizar a árvore no Combobox; ao integrar,
// chame o endpoint /categorias-pai para hidratar o `parentId` real.
//
// `icone` é uma string que mapeamos para um ícone do lucide-react via
// src/lib/categoria-icones.js
export const categorias = [
  // ── Gastos (pais) ──────────────────────────────────────────
  { id: 1,  code: "CAT-AL", nome: "Alimentação",  tipo: "gasto",   icone: "utensils",      corHex: "#E74C3C", padrao: true },
  { id: 11, code: "CAT-AL-MERC", nome: "Mercado",      tipo: "gasto", icone: "shopping_cart", corHex: "#E67E22", padrao: false, parentId: 1 },
  { id: 12, code: "CAT-AL-REST", nome: "Restaurantes", tipo: "gasto", icone: "utensils",      corHex: "#E74C3C", padrao: false, parentId: 1 },
  { id: 13, code: "CAT-AL-DEL",  nome: "Delivery",     tipo: "gasto", icone: "pizza",         corHex: "#D35400", padrao: false, parentId: 1 },
  { id: 14, code: "CAT-AL-CAFE", nome: "Café/Padaria", tipo: "gasto", icone: "coffee",        corHex: "#B7691E", padrao: false, parentId: 1 },

  { id: 2,  code: "CAT-TR", nome: "Transporte",   tipo: "gasto",   icone: "car",           corHex: "#3498DB", padrao: true },
  { id: 21, code: "CAT-TR-APP",  nome: "Apps mobilidade", tipo: "gasto", icone: "car",  corHex: "#2980B9", padrao: false, parentId: 2 },
  { id: 22, code: "CAT-TR-COMB", nome: "Combustível",     tipo: "gasto", icone: "fuel", corHex: "#1F618D", padrao: false, parentId: 2 },
  { id: 23, code: "CAT-TR-PUB",  nome: "Transporte público", tipo: "gasto", icone: "bus", corHex: "#5DADE2", padrao: false, parentId: 2 },

  { id: 3,  code: "CAT-MO", nome: "Moradia",      tipo: "gasto",   icone: "home",          corHex: "#8E44AD", padrao: true },
  { id: 31, code: "CAT-MO-ALU",  nome: "Aluguel",   tipo: "gasto", icone: "home",  corHex: "#8E44AD", padrao: false, parentId: 3 },
  { id: 32, code: "CAT-MO-CON",  nome: "Condomínio", tipo: "gasto", icone: "home", corHex: "#7D3C98", padrao: false, parentId: 3 },
  { id: 33, code: "CAT-MO-UTIL", nome: "Contas (luz/água/net)", tipo: "gasto", icone: "zap", corHex: "#6C3483", padrao: false, parentId: 3 },

  { id: 4,  code: "CAT-SA", nome: "Saúde",        tipo: "gasto",   icone: "heart",         corHex: "#27AE60", padrao: true },
  { id: 41, code: "CAT-SA-FARM", nome: "Farmácia",    tipo: "gasto", icone: "pill", corHex: "#229954", padrao: false, parentId: 4 },
  { id: 42, code: "CAT-SA-CONS", nome: "Consultas",   tipo: "gasto", icone: "heart", corHex: "#1E8449", padrao: false, parentId: 4 },

  { id: 5,  code: "CAT-LA", nome: "Lazer",        tipo: "gasto",   icone: "film",          corHex: "#F39C12", padrao: true },
  { id: 51, code: "CAT-LA-SUB",  nome: "Assinaturas", tipo: "gasto", icone: "wifi", corHex: "#D68910", padrao: false, parentId: 5 },
  { id: 52, code: "CAT-LA-VIA",  nome: "Viagens",     tipo: "gasto", icone: "plane", corHex: "#B9770E", padrao: false, parentId: 5 },

  { id: 6,  code: "CAT-CO", nome: "Compras",      tipo: "gasto",   icone: "shopping_bag",  corHex: "#16A085", padrao: true },
  { id: 7,  code: "CAT-OU", nome: "Outros",       tipo: "ambos",   icone: "more",          corHex: "#7F8C8D", padrao: true },

  // ── Receitas ──────────────────────────────────────────────
  { id: 100, code: "CAT-SAL", nome: "Salário",   tipo: "receita", icone: "briefcase", corHex: "#2ECC71", padrao: true },
  { id: 101, code: "CAT-INV", nome: "Investimentos", tipo: "receita", icone: "trending_up", corHex: "#1ABC9C", padrao: true },
  { id: 102, code: "CAT-FRE", nome: "Freelance", tipo: "receita", icone: "briefcase", corHex: "#16A085", padrao: true },
];

// Helpers úteis pra UI.
export const categoriasPorId = Object.fromEntries(categorias.map((c) => [c.id, c]));

export const categoriasGasto = categorias.filter((c) => c.tipo === "gasto" || c.tipo === "ambos");
export const categoriasReceita = categorias.filter((c) => c.tipo === "receita" || c.tipo === "ambos");
