// Helper usado em quase todos os componentes para juntar classes do Tailwind:
// - clsx: monta uma string de classes a partir de objetos/arrays condicionais
// - twMerge: deduplica classes Tailwind conflitantes ("p-2 p-4" vira "p-4")
import { clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs) {
  return twMerge(clsx(inputs));
}
