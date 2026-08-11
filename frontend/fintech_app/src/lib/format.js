// Helpers de formatação centralizados. Usar SEMPRE estes — evita inconsistência
// de R$ entre telas e mantém locale pt-BR num lugar só.
import { format, parseISO } from "date-fns";
import { ptBR } from "date-fns/locale";

// ── Moeda ────────────────────────────────────────────────────────────
// Formatter cacheado: criar Intl.NumberFormat a cada chamada é caro.
const moedaBR = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
  minimumFractionDigits: 2,
});

/** Formata número como R$. Aceita number ou string numérica. */
export function formatBRL(valor) {
  if (valor === null || valor === undefined || valor === "") return "—";
  const n = typeof valor === "string" ? Number(valor) : valor;
  if (Number.isNaN(n)) return "—";
  return moedaBR.format(n);
}

/** Versão "+R$ 123,45" / "-R$ 123,45" — sinal sempre presente. */
export function formatBRLSigned(valor) {
  if (valor === null || valor === undefined) return "—";
  const n = Number(valor);
  if (Number.isNaN(n)) return "—";
  const sinal = n > 0 ? "+" : n < 0 ? "−" : "";
  return `${sinal}${moedaBR.format(Math.abs(n))}`;
}

/** Devolve só o número (sem o "R$"). Útil para hero cards. */
export function formatNumeroBR(valor) {
  if (valor === null || valor === undefined) return "—";
  const n = Number(valor);
  if (Number.isNaN(n)) return "—";
  return n.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// ── Datas ────────────────────────────────────────────────────────────
// Convertemos strings ISO (vindas do backend) em Date antes de formatar.
function toDate(dataIso) {
  if (!dataIso) return null;
  if (dataIso instanceof Date) return dataIso;
  return parseISO(dataIso);
}

/** "24/04/2026" — formato de LocalDate do backend. */
export function formatData(dataIso) {
  const d = toDate(dataIso);
  if (!d) return "—";
  return format(d, "dd/MM/yyyy", { locale: ptBR });
}

/** "24 de abr." — versão curta usada em listas. */
export function formatDataCurta(dataIso) {
  const d = toDate(dataIso);
  if (!d) return "—";
  return format(d, "dd 'de' MMM.", { locale: ptBR });
}

/** "08:42" — extrai o horário de um OffsetDateTime. */
export function formatHora(dataIso) {
  const d = toDate(dataIso);
  if (!d) return "—";
  return format(d, "HH:mm");
}

/** "Abril de 2026" — mês + ano por extenso. */
export function formatMesAno(ano, mes) {
  // mes vem 1..12 do backend (Java MonthValue), Date espera 0..11.
  const d = new Date(ano, mes - 1, 1);
  return format(d, "MMMM 'de' yyyy", { locale: ptBR });
}

/** "Hoje", "Ontem" ou data curta — útil para agrupar lista de transações. */
export function formatDataRelativa(dataIso) {
  const d = toDate(dataIso);
  if (!d) return "—";
  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);
  const alvo = new Date(d);
  alvo.setHours(0, 0, 0, 0);
  const diffDias = Math.round((hoje - alvo) / (1000 * 60 * 60 * 24));
  if (diffDias === 0) return "Hoje";
  if (diffDias === 1) return "Ontem";
  return format(d, "dd/MM/yyyy", { locale: ptBR });
}

// ── Documentos ───────────────────────────────────────────────────────
/** "12345678909" → "123.456.789-09". Recebe só dígitos. */
export function formatCPF(digits) {
  if (!digits) return "";
  const d = String(digits);
  return `${d.slice(0, 3)}.${d.slice(3, 6)}.${d.slice(6, 9)}-${d.slice(9)}`;
}

/** Mascara CPF para exibição: "•••.456.789-••". */
export function maskCPF(digits) {
  if (!digits) return "";
  const d = String(digits).slice(-11);
  if (d.length < 11) return formatCPF(d);
  return `•••.${d.slice(3, 6)}.${d.slice(6, 9)}-••`;
}

/** Mascara email: "ale••@finsight.app". */
export function maskEmail(email) {
  if (!email || !email.includes("@")) return email ?? "";
  const [user, domain] = email.split("@");
  if (user.length <= 3) return `${user[0]}••@${domain}`;
  return `${user.slice(0, 3)}••@${domain}`;
}

// ── Strings ──────────────────────────────────────────────────────────
/** Pega as iniciais (até 2 letras) do nome para usar em avatares. */
export function getInitials(nome) {
  if (!nome) return "?";
  const partes = nome.trim().split(/\s+/);
  if (partes.length === 1) return partes[0].slice(0, 2).toUpperCase();
  return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
}

/** Primeira letra maiúscula, resto minúsculo. */
export function capitalize(s) {
  if (!s) return "";
  return s[0].toUpperCase() + s.slice(1).toLowerCase();
}
