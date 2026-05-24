// BalanceChart: gráfico de linha desenhado em SVG puro mostrando o saldo
// nos últimos 7 dias. Sem bibliotecas externas — controle total do visual e
// bundle leve.
import { useState } from "react";

// Cada ponto: x/y em coordenadas internas do SVG (viewBox 320x130), o rótulo
// do dia e o valor formatado para exibir no tooltip.
const points = [
  { x: 0, y: 95, label: "Mon", v: "$12.4k" },
  { x: 53, y: 78, label: "Tue", v: "$13.1k" },
  { x: 106, y: 88, label: "Wed", v: "$12.8k" },
  { x: 160, y: 55, label: "Thu", v: "$14.0k" },
  { x: 213, y: 42, label: "Fri", v: "$14.5k" },
  { x: 266, y: 28, label: "Sat", v: "$14.7k" },
  { x: 320, y: 18, label: "Sun", v: "$14.87k" },
];

export const BalanceChart = () => {
  // Índice do ponto atualmente "hovered" — 6 (Sun) é o padrão (mais recente).
  const [hover, setHover] = useState(6);

  // Monta o "d" do <path> conectando os pontos com curvas suaves (quadráticas).
  // Para cada ponto a partir do segundo, calcula um ponto de controle no meio
  // entre o ponto anterior e o atual para criar a sensação de curva natural.
  const path = points
    .map((p, i, arr) => {
      if (i === 0) return `M${p.x},${p.y}`;
      const prev = arr[i - 1];
      const cx = (prev.x + p.x) / 2;
      return `Q${cx},${prev.y} ${cx},${(prev.y + p.y) / 2} T${p.x},${p.y}`;
    })
    .join(" ");

  // Área sob a curva: mesmo caminho da linha + dois pontos no eixo X para fechar.
  const areaPath = `${path} L320,130 L0,130 Z`;
  const active = hover !== null ? points[hover] : null;

  return (
    <div className="w-full">
      {/* Topo: limite máximo do eixo Y + valor do ponto ativo. */}
      <div className="flex justify-between text-[9px] text-muted-foreground/70 font-medium mb-1 px-0.5">
        <span>$15k</span>
        <span className="text-primary font-bold">{active?.v ?? "$14.87k"}</span>
      </div>

      <div className="w-full h-[130px] relative">
        <svg
          viewBox="0 0 320 130"
          className="w-full h-full overflow-visible"
          preserveAspectRatio="none"
          onMouseLeave={() => setHover(6)}
        >
          {/* Gradientes reutilizáveis: área de preenchimento + linha. */}
          <defs>
            <linearGradient id="areaGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor="hsl(var(--primary))" stopOpacity="0.3" />
              <stop offset="100%" stopColor="hsl(var(--primary))" stopOpacity="0" />
            </linearGradient>
            <linearGradient id="lineGrad" x1="0" y1="0" x2="1" y2="0">
              <stop offset="0%" stopColor="hsl(var(--primary))" />
              <stop offset="100%" stopColor="hsl(var(--accent))" />
            </linearGradient>
          </defs>

          {/* Linhas-guia horizontais (eixo Y). */}
          {[20, 60, 100].map((y) => (
            <line
              key={y}
              x1="0" y1={y} x2="320" y2={y}
              stroke="hsl(var(--border))"
              strokeDasharray="2 4"
              strokeWidth="1"
            />
          ))}

          {/* Linha tracejada de comparação (período anterior). */}
          <path
            d="M0,100 Q40,92 80,96 T160,86 T240,80 T320,72"
            fill="none"
            stroke="hsl(var(--muted-foreground))"
            strokeOpacity="0.3"
            strokeWidth="1.5"
            strokeDasharray="3 3"
            strokeLinecap="round"
          />

          {/* Área preenchida + linha principal. */}
          <path d={areaPath} fill="url(#areaGrad)" />
          <path
            d={path}
            fill="none"
            stroke="url(#lineGrad)"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />

          {/* "Hitboxes" invisíveis em cima de cada ponto: capturam o hover do mouse
              em uma área maior que o pontinho visível, melhorando a usabilidade. */}
          {points.map((p, i) => (
            <g key={i}>
              <rect
                x={p.x - 22} y={0} width={44} height={130}
                fill="transparent"
                onMouseEnter={() => setHover(i)}
                style={{ cursor: "pointer" }}
              />
              {hover === i && (
                <>
                  {/* Linha vertical tracejada no ponto ativo. */}
                  <line
                    x1={p.x} y1={0} x2={p.x} y2={130}
                    stroke="hsl(var(--primary))"
                    strokeOpacity="0.25"
                    strokeWidth="1"
                    strokeDasharray="2 2"
                  />
                  {/* Halo + bolinha do ponto ativo. */}
                  <circle cx={p.x} cy={p.y} r="7" fill="hsl(var(--primary))" fillOpacity="0.15" />
                  <circle
                    cx={p.x} cy={p.y} r="3.75"
                    fill="hsl(var(--card))"
                    stroke="hsl(var(--primary))"
                    strokeWidth="2.25"
                  />
                </>
              )}
            </g>
          ))}
        </svg>
      </div>

      {/* Rótulos dos dias da semana — o ativo recebe destaque. */}
      <div className="flex justify-between text-[9.5px] text-muted-foreground font-medium mt-1.5 px-0.5">
        {points.map((p, i) => (
          <span key={i} className={hover === i ? "text-primary font-bold" : ""}>
            {p.label}
          </span>
        ))}
      </div>
    </div>
  );
};
