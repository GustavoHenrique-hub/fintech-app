// ConfidenceBar: indicador visual de confiança (0–100) com 3 faixas de cor.
//
// Regras da spec:
//   >= 80  → verde   (alta confiança)
//   50–79  → amarelo (média)
//   <  50  → vermelho (baixa)
//
// Layout: label opcional à esquerda, barra colorida no meio, valor à direita.
// Acessível: aria-valuenow/min/max + role="progressbar".
//
// API:
//   <ConfidenceBar value={87} />
//   <ConfidenceBar value={72} label="Confiança IA" showValue />
import { cn } from "@/lib/utils";

function getTone(value) {
  if (value >= 80) return "success";
  if (value >= 50) return "warning";
  return "danger";
}

const TONE_LABELS = {
  success: "Alta",
  warning: "Média",
  danger:  "Baixa",
};

const TONE_BAR_CLASSES = {
  success: "bg-success",
  warning: "bg-accent",
  danger:  "bg-destructive",
};

const TONE_TEXT_CLASSES = {
  success: "text-success",
  warning: "text-foreground",
  danger:  "text-destructive",
};

export function ConfidenceBar({
  value,
  label,
  showValue = true,
  showQualitative = true,
  className,
}) {
  // Garante 0..100 mesmo se vier algo fora do range do backend.
  const safe = Math.max(0, Math.min(100, Number(value) || 0));
  const tone = getTone(safe);

  return (
    <div className={cn("w-full", className)}>
      {(label || showValue) && (
        <div className="flex items-center justify-between mb-1.5">
          {label && (
            <span className="text-[11px] font-semibold text-muted-foreground">
              {label}
            </span>
          )}
          {showValue && (
            <span className={cn("text-[11.5px] font-bold tabular-nums", TONE_TEXT_CLASSES[tone])}>
              {safe.toFixed(0)}%
              {showQualitative && <span className="ml-1 opacity-70 font-medium">· {TONE_LABELS[tone]}</span>}
            </span>
          )}
        </div>
      )}

      {/* Trilha cinza + barra colorida sobreposta. */}
      <div
        className="w-full h-2 rounded-full bg-secondary overflow-hidden"
        role="progressbar"
        aria-valuenow={safe}
        aria-valuemin={0}
        aria-valuemax={100}
        aria-label={label ?? "Confiança"}
      >
        <div
          className={cn("h-full rounded-full transition-[width] duration-500", TONE_BAR_CLASSES[tone])}
          style={{ width: `${safe}%` }}
        />
      </div>
    </div>
  );
}
