// Família de Skeletons para estados de loading.
//
// Filosofia: cada Skeleton mimica o esqueleto do componente final
// (mesma altura, mesma forma) — assim o layout não "pula" quando o conteúdo
// real chega. Animação shimmer via `animate-pulse` do Tailwind.
import { cn } from "@/lib/utils";

// Bloco genérico — base de todos os outros.
function Skeleton({ className, ...props }) {
  return (
    <div
      aria-hidden="true"
      role="status"
      className={cn(
        "animate-pulse rounded-md bg-muted",
        className,
      )}
      {...props}
    />
  );
}

// Linhas de texto. `lines` controla quantas linhas mostrar (default 3).
// A última fica mais curta pra simular um parágrafo natural.
function SkeletonText({ lines = 3, className }) {
  return (
    <div className={cn("space-y-2", className)} role="status" aria-label="Carregando texto">
      {Array.from({ length: lines }).map((_, i) => (
        <Skeleton
          key={i}
          className={cn("h-3", i === lines - 1 ? "w-2/3" : "w-full")}
        />
      ))}
    </div>
  );
}

// Card: cabeçalho com avatar + título + 2 linhas de texto.
// Aproxima visualmente um card típico (ex.: card-soft com row de transação).
function SkeletonCard({ className }) {
  return (
    <div
      className={cn("card-soft p-4 space-y-3", className)}
      role="status"
      aria-label="Carregando card"
    >
      <div className="flex items-center gap-3">
        <Skeleton className="w-10 h-10 rounded-full" />
        <div className="flex-1 space-y-1.5">
          <Skeleton className="h-3.5 w-1/2" />
          <Skeleton className="h-2.5 w-1/3" />
        </div>
      </div>
      <SkeletonText lines={2} />
    </div>
  );
}

// Chart: barra de "métricas no topo" + área grande do gráfico.
function SkeletonChart({ className }) {
  return (
    <div
      className={cn("card-soft p-4 space-y-3", className)}
      role="status"
      aria-label="Carregando gráfico"
    >
      <div className="flex items-center justify-between">
        <div className="space-y-1.5">
          <Skeleton className="h-2.5 w-20" />
          <Skeleton className="h-3 w-32" />
        </div>
        <Skeleton className="h-6 w-24 rounded-full" />
      </div>
      <Skeleton className="h-[130px] w-full rounded-lg" />
    </div>
  );
}

// Row: linha de lista (ícone redondo + 2 linhas + valor à direita).
// Útil para listas de transações e contas em loading.
function SkeletonRow({ className }) {
  return (
    <div
      className={cn("flex items-center gap-3 px-3.5 py-3", className)}
      role="status"
      aria-label="Carregando item"
    >
      <Skeleton className="w-10 h-10 rounded-full shrink-0" />
      <div className="flex-1 space-y-1.5">
        <Skeleton className="h-3.5 w-1/3" />
        <Skeleton className="h-2.5 w-1/4" />
      </div>
      <Skeleton className="h-3.5 w-16" />
    </div>
  );
}

export { Skeleton, SkeletonText, SkeletonCard, SkeletonChart, SkeletonRow };
