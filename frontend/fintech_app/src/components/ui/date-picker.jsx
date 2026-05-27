// DatePicker: input de data única em formato pt-BR (dd/MM/yyyy).
//
// Implementação:
//  - Botão "trigger" que mostra a data formatada (ou placeholder).
//  - Painel popover com react-day-picker (calendário).
//  - Locale fixo em pt-BR via date-fns/locale.
//
// API:
//   <DatePicker value={date} onChange={(d) => ...} />
//   - `value`: Date | undefined
//   - `onChange(date)`: chamado quando o usuário escolhe um dia
//
// Para datas vindas do backend como string ISO ("2026-04-24"), converta
// antes: `new Date("2026-04-24")`.
import * as React from "react";
import { DayPicker } from "react-day-picker";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Calendar as CalendarIcon } from "lucide-react";

import { cn } from "@/lib/utils";
import "react-day-picker/style.css";

export function DatePicker({
  value,
  onChange,
  placeholder = "dd/mm/aaaa",
  disabled = false,
  // `disabledDates`: predicado opcional para bloquear dias específicos.
  disabledDates,
  className,
  id,
}) {
  const [open, setOpen] = React.useState(false);
  const rootRef = React.useRef(null);

  // Fecha o popover ao clicar fora.
  React.useEffect(() => {
    if (!open) return;
    const onDocClick = (e) => {
      if (!rootRef.current?.contains(e.target)) setOpen(false);
    };
    document.addEventListener("mousedown", onDocClick);
    return () => document.removeEventListener("mousedown", onDocClick);
  }, [open]);

  const displayValue = value ? format(value, "dd/MM/yyyy", { locale: ptBR }) : "";

  return (
    <div ref={rootRef} className={cn("relative w-full", className)}>
      <button
        type="button"
        id={id}
        disabled={disabled}
        onClick={() => setOpen((o) => !o)}
        className={cn(
          "w-full h-10 px-3 rounded-xl bg-card border border-border text-[13.5px] text-left outline-none " +
            "flex items-center justify-between gap-2 " +
            "focus-visible:ring-2 focus-visible:ring-primary/30 " +
            "disabled:opacity-50 disabled:cursor-not-allowed transition-all",
        )}
      >
        <span className={cn(!value && "text-muted-foreground/70")}>
          {displayValue || placeholder}
        </span>
        <CalendarIcon className="w-4 h-4 text-muted-foreground shrink-0" strokeWidth={2.25} />
      </button>

      {open && (
        <div className="absolute z-50 mt-1 rounded-xl border border-border bg-card shadow-lg p-2">
          <DayPicker
            mode="single"
            selected={value}
            onSelect={(d) => {
              onChange?.(d);
              if (d) setOpen(false);
            }}
            locale={ptBR}
            disabled={disabledDates}
            // weekStartsOn=0 (domingo) — padrão Brasil.
            weekStartsOn={0}
          />
        </div>
      )}
    </div>
  );
}
