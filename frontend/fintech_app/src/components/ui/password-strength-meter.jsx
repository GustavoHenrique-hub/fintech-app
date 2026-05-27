// PasswordStrengthMeter: avalia a força de uma senha em 4 níveis e mostra:
//   - Barra colorida segmentada (4 pedaços)
//   - Label textual ("Fraca", "Razoável", "Boa", "Forte")
//   - Checklist de regras que faltam (minúscula, maiúscula, número, símbolo, 8+)
//
// Cálculo: cada regra atendida soma 1 ponto (0..5). Mapeamos pra 4 níveis:
//   0–1 ponto  → Fraca   (vermelho)
//   2 pontos   → Razoável (laranja/amarelo)
//   3–4 pontos → Boa     (azul)
//   5 pontos   → Forte   (verde)
//
// API:
//   <PasswordStrengthMeter value={password} onChange={(strength) => ...} />
//   - `value`: string da senha (controlada pelo input pai)
//   - `onChange(level)`: opcional — recebe { score:0-5, level: "weak"|"fair"|"good"|"strong" }
//     útil quando o submit do form depende de ter pelo menos "good".
import * as React from "react";
import { Check, X } from "lucide-react";

import { cn } from "@/lib/utils";

// Regras avaliadas. Adicionar/remover regras aqui já reflete no checklist.
const RULES = [
  { key: "length", label: "Pelo menos 8 caracteres", test: (s) => s.length >= 8 },
  { key: "lower",  label: "Uma letra minúscula",    test: (s) => /[a-z]/.test(s) },
  { key: "upper",  label: "Uma letra maiúscula",    test: (s) => /[A-Z]/.test(s) },
  { key: "digit",  label: "Um número",              test: (s) => /[0-9]/.test(s) },
  { key: "symbol", label: "Um símbolo (!@#…)",       test: (s) => /[^A-Za-z0-9]/.test(s) },
];

// Score → nível qualitativo.
function getLevel(score) {
  if (score <= 1) return { level: "weak",   label: "Fraca",    tone: "danger",  segments: 1 };
  if (score === 2) return { level: "fair",   label: "Razoável", tone: "warning", segments: 2 };
  if (score <= 4) return { level: "good",   label: "Boa",      tone: "info",    segments: 3 };
  return { level: "strong", label: "Forte",    tone: "success", segments: 4 };
}

const TONE_BAR = {
  danger:  "bg-destructive",
  warning: "bg-accent",
  info:    "bg-primary",
  success: "bg-success",
};

const TONE_TEXT = {
  danger:  "text-destructive",
  warning: "text-foreground",
  info:    "text-primary",
  success: "text-success",
};

export function PasswordStrengthMeter({ value = "", showChecklist = true, onChange, className }) {
  // Calcula score e regras atendidas a cada render.
  const ruleResults = RULES.map((r) => ({ ...r, ok: r.test(value) }));
  const score = ruleResults.filter((r) => r.ok).length;
  const info = getLevel(score);

  // Notifica o pai quando o nível mudar — bom para habilitar/desabilitar submit.
  React.useEffect(() => {
    onChange?.({ score, level: info.level });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [score, info.level]);

  return (
    <div className={cn("w-full space-y-2", className)} aria-live="polite">
      {/* Barra segmentada (4 pedaços). Pedaços além do segmento atual ficam cinza. */}
      <div className="flex gap-1">
        {[0, 1, 2, 3].map((i) => (
          <div
            key={i}
            className={cn(
              "h-1.5 flex-1 rounded-full transition-colors",
              i < info.segments && value ? TONE_BAR[info.tone] : "bg-secondary",
            )}
          />
        ))}
      </div>

      {/* Label textual. Só mostra depois que o usuário digitou algo. */}
      {value && (
        <p className={cn("text-[11.5px] font-bold", TONE_TEXT[info.tone])}>
          Força da senha: {info.label}
        </p>
      )}

      {/* Checklist de regras (opcional). */}
      {showChecklist && (
        <ul className="grid grid-cols-1 sm:grid-cols-2 gap-x-3 gap-y-1 pt-1">
          {ruleResults.map((r) => (
            <li
              key={r.key}
              className={cn(
                "flex items-center gap-1.5 text-[11px]",
                r.ok ? "text-success" : "text-muted-foreground",
              )}
            >
              {r.ok ? (
                <Check className="w-3 h-3 shrink-0" strokeWidth={3} />
              ) : (
                <X className="w-3 h-3 shrink-0" strokeWidth={3} />
              )}
              <span>{r.label}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
