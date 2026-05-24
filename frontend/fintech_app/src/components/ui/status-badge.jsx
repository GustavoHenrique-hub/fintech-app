// StatusBadge: mapeia enums do backend (StatusExtrato e StatusRevisaoTransacao)
// para um par (cor, label amigável em pt-BR).
//
// Quando o backend devolve `status: "pendente_revisao"`, este componente
// renderiza um pill cinza/amarelo escrito "Pendente de revisão".
//
// API:
//   <StatusBadge kind="extrato"  value="pendente_revisao" />
//   <StatusBadge kind="revisao" value="CONFIRMADA" />
//
// Categorias de cor reutilizam tokens do design system:
//   neutral  → cinza   (estados intermediários sem indicar sucesso/falha)
//   info     → roxo    (em andamento)
//   warning  → amarelo (atenção necessária)
//   success  → verde   (concluído com sucesso)
//   danger   → vermelho (erro / cancelado)
import { cn } from "@/lib/utils";

// Tabela canônica StatusExtrato → {label, tone}.
// Chaves seguem o enum Java (lowercase).
const STATUS_EXTRATO = {
  upload_recebido:        { label: "Upload recebido",        tone: "neutral" },
  validando:              { label: "Validando",              tone: "info" },
  na_fila:                { label: "Na fila",                tone: "neutral" },
  extraindo:              { label: "Extraindo",              tone: "info" },
  classificando:          { label: "Classificando",          tone: "info" },
  aguardando_ia:          { label: "Aguardando IA",          tone: "info" },
  pendente_revisao:       { label: "Pendente de revisão",    tone: "warning" },
  parcialmente_revisado:  { label: "Parcialmente revisado",  tone: "warning" },
  concluido:              { label: "Concluído",              tone: "success" },
  erro_formato:           { label: "Erro de formato",        tone: "danger" },
  erro_extracao:          { label: "Erro na extração",       tone: "danger" },
  erro_classificacao:     { label: "Erro na classificação",  tone: "danger" },
  erro_timeout:           { label: "Tempo esgotado",         tone: "danger" },
  cancelado:              { label: "Cancelado",              tone: "danger" },
  reprocessando:          { label: "Reprocessando",          tone: "info" },
};

// Tabela canônica StatusRevisaoTransacao → {label, tone}.
// Chaves seguem o enum Java (UPPERCASE).
const STATUS_REVISAO = {
  EXTRAIDA:           { label: "Extraída",            tone: "neutral" },
  CLASSIFICADA:       { label: "Classificada",        tone: "info" },
  PENDENTE_REVISAO:   { label: "Pendente de revisão", tone: "warning" },
  CONFIRMADA:         { label: "Confirmada",          tone: "success" },
  IGNORADA:           { label: "Ignorada",            tone: "neutral" },
  ARQUIVADA:          { label: "Arquivada",           tone: "neutral" },
};

// Classes de Tailwind por tom — fundo claro + texto/contorno acentuados.
const TONE_CLASSES = {
  neutral: "bg-secondary text-foreground border-border",
  info:    "bg-surface-purple text-primary border-primary/20",
  warning: "bg-surface-yellow text-foreground border-accent/40",
  success: "bg-surface-green text-success border-success/30",
  danger:  "bg-surface-pink text-destructive border-destructive/30",
};

export function StatusBadge({ kind, value, className }) {
  // Resolve tabela conforme o tipo do status.
  const table = kind === "extrato" ? STATUS_EXTRATO : STATUS_REVISAO;
  const entry = table[value];

  // Fallback defensivo: se vier um valor desconhecido (ex.: backend novo,
  // front desatualizado), mostramos o valor cru em cinza — sem quebrar a UI.
  const { label, tone } = entry ?? { label: String(value ?? "—"), tone: "neutral" };

  return (
    <span
      className={cn(
        "inline-flex items-center gap-1.5 rounded-full border px-2 py-0.5 text-[11px] font-semibold whitespace-nowrap",
        TONE_CLASSES[tone],
        className,
      )}
      aria-label={`Status: ${label}`}
    >
      {/* Bolinha à esquerda dá um reforço visual da cor. */}
      <span
        className={cn(
          "w-1.5 h-1.5 rounded-full",
          tone === "neutral" && "bg-muted-foreground",
          tone === "info" && "bg-primary",
          tone === "warning" && "bg-accent",
          tone === "success" && "bg-success",
          tone === "danger" && "bg-destructive",
        )}
      />
      {label}
    </span>
  );
}

// Exporta as tabelas pra quem quiser usar em outros lugares (ex.: filtros).
export { STATUS_EXTRATO, STATUS_REVISAO };
