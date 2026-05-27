// DateRangePicker: seleção de intervalo de datas com formato pt-BR.
//
// Spec atendida:
//  - Locale pt-BR (date-fns/locale).
//  - Validação: o intervalo não pode exceder 6 meses (usado em relatórios).
//    Se exceder, exibimos erro vermelho ABAIXO do trigger e NÃO chamamos
//    o onChange — o consumidor recebe apenas ranges válidos.
//
// API:
//   <DateRangePicker
//     value={{ from, to }}
//     onChange={(range) => ...}
//     maxRangeMonths={6}  // default 6
//   />
import * as React from "react";
import { DayPicker } from "react-day-picker";
import { format, differenceInCalendarDays } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Calendar as CalendarIcon, AlertTriangle } from "lucide-react";

import { cn } from "@/lib/utils";
import "react-day-picker/style.css";

// Aprox. 6 meses = 183 dias. Usar diferença em dias evita problemas de
// "30 vs 31 dias do mês".
function exceedsMaxRange(range, maxMonths) {
  if (!range?.from || !range?.to) return false;
  const days = differenceInCalendarDays(range.to, range.from) + 1;
  return days > maxMonths * 30.5;
}

export function DateRangePicker({
  value,
  onChange,
  placeholder = "Selecione um período",
  disabled = false,
  maxRangeMonths = 6,
  className,
  id,
}) {
  const [open, setOpen] = React.useState(false);
  // Range "rascunho" enquanto o usuário ainda não confirmou — permite que
  // selecionemos from→to dentro do calendário sem disparar onChange com range
  // inválido (excedendo 6 meses).
  const [draft, setDraft] = React.useState(value);
  const [error, setError] = React.useState(null);
  const rootRef = React.useRef(null);

  // Sincroniza o rascunho se o pai trocar o valor externamente.
  React.useEffect(() => setDraft(value), [value]);

  React.useEffect(() => {
    if (!open) return;
    const onDocClick = (e) => {
      if (!rootRef.current?.contains(e.target)) setOpen(false);
    };
    document.addEventListener("mousedown", onDocClick);
    return () => document.removeEventListener("mousedown", onDocClick);
  }, [open]);

  // Texto exibido no trigger.
  const displayValue = (() => {
    if (!value?.from) return "";
    const f = format(value.from, "dd/MM/yyyy", { locale: ptBR });
    if (!value.to) return f;
    return `${f} – ${format(value.to, "dd/MM/yyyy", { locale: ptBR })}`;
  })();

  const handleSelect = (range) => {
    setDraft(range);

    // Range completo (from + to): valida.
    if (range?.from && range?.to) {
      if (exceedsMaxRange(range, maxRangeMonths)) {
        setError(`O período não pode exceder ${maxRangeMonths} meses.`);
        return; // não propaga
      }
      setError(null);
      onChange?.(range);
      setOpen(false);
      return;
    }

    // Range parcial (só from): limpa erro e propaga (alguns consumidores
    // querem reagir já no primeiro clique).
    setError(null);
    onChange?.(range);
  };

  return (
    <div ref={rootRef} className={cn("relative w-full", className)}>
      <button
        type="button"
        id={id}
        disabled={disabled}
        onClick={() => setOpen((o) => !o)}
        aria-invalid={!!error || undefined}
        className={cn(
          "w-full h-10 px-3 rounded-xl bg-card border text-[13.5px] text-left outline-none " +
            "flex items-center justify-between gap-2 " +
            "focus-visible:ring-2 focus-visible:ring-primary/30 " +
            "disabled:opacity-50 disabled:cursor-not-allowed transition-all",
          error ? "border-destructive/60" : "border-border",
        )}
      >
        <span className={cn(!value?.from && "text-muted-foreground/70")}>
          {displayValue || placeholder}
        </span>
        <CalendarIcon className="w-4 h-4 text-muted-foreground shrink-0" strokeWidth={2.25} />
      </button>

      {open && (
        <div className="absolute z-50 mt-1 rounded-xl border border-border bg-card shadow-lg p-2">
          <DayPicker
            mode="range"
            selected={draft}
            onSelect={handleSelect}
            locale={ptBR}
            numberOfMonths={1}
            weekStartsOn={0}
          />
          <p className="text-[10.5px] text-muted-foreground px-2 pb-1">
            Período máximo: {maxRangeMonths} meses.
          </p>
        </div>
      )}

      {error && (
        <p className="mt-1.5 flex items-center gap-1 text-[11px] text-destructive font-medium">
          <AlertTriangle className="w-3 h-3" strokeWidth={2.5} /> {error}
        </p>
      )}
    </div>
  );
}
