// InputMonetario: entrada de valores em R$ com máscara R$ 1.234,56.
//
// Por que usar IMask em vez de <input type="number"> ou regex manual?
//  - type="number" não suporta separador de milhar e usa locale do browser.
//  - Regex manual quebra com colagem, cursor no meio do texto, valores grandes.
//  - IMask faz tudo: separadores localizados, intervalo min/max, casas decimais
//    e expõe o "valor não-mascarado" (number) pra você usar no backend.
//
// API:
//   <InputMonetario value={123.45} onChange={(num) => ...} />
//   - `value`: number | undefined (valor "real" sem máscara)
//   - `onChange(num)`: recebe o número já parseado (não a string da máscara)
//
// Por baixo do capô usamos `IMaskInput` do react-imask, que envolve o
// input nativo e expõe os callbacks `onAccept(value, mask)`.
import * as React from "react";
import { IMaskInput } from "react-imask";

import { cn } from "@/lib/utils";

export const InputMonetario = React.forwardRef(
  (
    {
      value,
      onChange,
      placeholder = "R$ 0,00",
      disabled = false,
      className,
      min = 0,
      max = 9999999999.99,
      // `id` é opcional mas recomendado para acessibilidade (label htmlFor).
      id,
      ...rest
    },
    ref,
  ) => {
    return (
      <IMaskInput
        // Máscara "Number" do IMask aceita as opções de localização abaixo.
        mask={Number}
        thousandsSeparator="."
        radix=","
        scale={2}             // 2 casas decimais
        padFractionalZeros    // sempre mostra "R$ 10,00", não "R$ 10"
        normalizeZeros        // remove zeros à esquerda do inteiro
        signed={false}        // sem sinal negativo
        min={min}
        max={max}
        // O sufixo "R$ " fica fixo à esquerda usando o `lazy={false}`.
        // Mais simples: usar `prepare`/`commit`? Aqui usamos string prefix:
        // - PadFractionalZeros já dá o formato 1.234,56; prefixamos via React
        //   abaixo (input com left padding + span). Manter a máscara pura é
        //   mais robusto pra colagem.
        unmask="typed"        // expõe valor TIPADO (number) em onAccept
        value={value ?? ""}
        onAccept={(typedValue) => {
          // `typedValue` chega como number (ou "" quando o usuário apaga tudo).
          // Normalizamos pra undefined → o consumidor sabe que está vazio.
          onChange?.(typedValue === "" || Number.isNaN(typedValue) ? undefined : typedValue);
        }}
        disabled={disabled}
        placeholder={placeholder}
        inputMode="decimal"
        id={id}
        inputRef={ref}
        className={cn(
          "w-full h-10 px-3 rounded-xl bg-card border border-border " +
            "text-[14px] font-semibold text-foreground tabular-nums outline-none " +
            "placeholder:text-muted-foreground/60 placeholder:font-normal " +
            "focus:ring-2 focus:ring-primary/30 focus:border-primary/40 " +
            "disabled:opacity-50 disabled:cursor-not-allowed transition-all",
          className,
        )}
        {...rest}
      />
    );
  },
);
InputMonetario.displayName = "InputMonetario";
