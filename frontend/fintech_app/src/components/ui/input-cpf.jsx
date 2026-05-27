// InputCPF: input com máscara 000.000.000-00.
//
// O IMask faz o trabalho duro: ao digitar "12345678901" o usuário vê
// "123.456.789-01" automaticamente, com cursor inteligente.
//
// API:
//   <InputCPF value={cpf} onChange={(rawDigits) => ...} onValid={(isValid) => ...} />
//   - `value`: string de DÍGITOS (sem máscara). É o formato que normalmente
//     vai pro backend; armazenar com máscara em DB é ruim.
//   - `onChange(rawDigits)`: chama com a string só de números.
//   - `onValid`: opcional, recebe boolean indicando se passou na checagem
//     completa de dígitos verificadores.
//
// Importante:
//  - Validação de CPF aqui é apenas estrutural (dígitos verificadores).
//    Validação de "CPF existe na Receita" não é possível no front.
import * as React from "react";
import { IMaskInput } from "react-imask";

import { cn } from "@/lib/utils";

// Algoritmo padrão de validação dos 2 dígitos verificadores do CPF.
function validarCPF(cpf) {
  if (!cpf || cpf.length !== 11) return false;
  // Rejeita CPFs com todos os dígitos iguais ("111.111.111-11" passa na fórmula
  // mas é inválido por convenção da Receita).
  if (/^(\d)\1+$/.test(cpf)) return false;

  // Calcula o primeiro dígito verificador.
  let soma = 0;
  for (let i = 0; i < 9; i++) soma += Number(cpf[i]) * (10 - i);
  let resto = (soma * 10) % 11;
  if (resto === 10) resto = 0;
  if (resto !== Number(cpf[9])) return false;

  // Segundo dígito verificador.
  soma = 0;
  for (let i = 0; i < 10; i++) soma += Number(cpf[i]) * (11 - i);
  resto = (soma * 10) % 11;
  if (resto === 10) resto = 0;
  return resto === Number(cpf[10]);
}

export const InputCPF = React.forwardRef(
  (
    {
      value = "",
      onChange,
      onValid,
      placeholder = "000.000.000-00",
      disabled = false,
      className,
      id,
      ...rest
    },
    ref,
  ) => {
    return (
      <IMaskInput
        // `mask` literal — o IMask interpreta cada '0' como "qualquer dígito".
        mask="000.000.000-00"
        // unmask=true → o callback recebe SÓ os dígitos (ex.: "12345678901").
        unmask
        value={value}
        onAccept={(rawDigits) => {
          onChange?.(rawDigits);
          // Só reporta válido quando o usuário digitou os 11 dígitos.
          onValid?.(rawDigits.length === 11 && validarCPF(rawDigits));
        }}
        placeholder={placeholder}
        disabled={disabled}
        inputMode="numeric"
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
InputCPF.displayName = "InputCPF";

// Re-exporta a validação caso o consumidor queira validar em outro contexto.
export { validarCPF };
