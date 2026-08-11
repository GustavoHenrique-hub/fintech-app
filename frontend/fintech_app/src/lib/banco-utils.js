// Mapa de banco → cor primária da marca
const CORES = {
  "nubank":      "#8A05BE",
  "nu":          "#8A05BE",
  "itaú":        "#F17B22",
  "itau":        "#F17B22",
  "bradesco":    "#CC092F",
  "santander":   "#EC0000",
  "caixa":       "#005CA9",
  "cef":         "#005CA9",
  "bb":          "#F6C000",
  "banco do brasil": "#F6C000",
  "inter":       "#FF7A00",
  "banco inter": "#FF7A00",
  "c6":          "#2B2C2E",
  "c6 bank":     "#2B2C2E",
  "xp":          "#1A1A2E",
  "xp investimentos": "#1A1A2E",
  "btg":         "#0E2245",
  "btg pactual": "#0E2245",
  "sicoob":      "#006E3C",
  "sicredi":     "#007A3D",
  "original":    "#00B900",
  "next":        "#00E676",
  "neon":        "#00C4CC",
  "picpay":      "#21C25E",
  "mercadopago": "#009EE3",
  "iti":         "#FF6B00",
  "pagbank":     "#FF6900",
};

// Mapa de banco → domínio (para buscar logo via Clearbit)
const DOMINIOS = {
  "nubank":      "nubank.com.br",
  "nu":          "nubank.com.br",
  "itaú":        "itau.com.br",
  "itau":        "itau.com.br",
  "bradesco":    "bradesco.com.br",
  "santander":   "santander.com.br",
  "caixa":       "caixa.gov.br",
  "cef":         "caixa.gov.br",
  "bb":          "bb.com.br",
  "banco do brasil": "bb.com.br",
  "inter":       "inter.co",
  "banco inter": "inter.co",
  "c6":          "c6bank.com.br",
  "c6 bank":     "c6bank.com.br",
  "xp":          "xpinvestimentos.com.br",
  "btg":         "btgpactual.com",
  "btg pactual": "btgpactual.com",
  "sicoob":      "sicoob.com.br",
  "sicredi":     "sicredi.com.br",
  "original":    "original.com.br",
  "next":        "next.me",
  "neon":        "neon.com.br",
  "picpay":      "picpay.com",
  "mercadopago": "mercadopago.com.br",
  "pagbank":     "pagbank.com.br",
};

function chave(banco = "") {
  return banco.toLowerCase().trim();
}

/** Retorna a cor hex da marca do banco, ou um fallback */
export function getBancoColor(banco, fallback = "#7C3AED") {
  return CORES[chave(banco)] ?? fallback;
}

/** Retorna a URL do logo via Clearbit Logo API */
export function getBancoLogoUrl(banco) {
  const dominio = DOMINIOS[chave(banco)];
  if (!dominio) return null;
  return `https://logo.clearbit.com/${dominio}`;
}
