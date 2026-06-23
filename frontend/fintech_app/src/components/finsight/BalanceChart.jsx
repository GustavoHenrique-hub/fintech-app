import { useState, useMemo } from "react";
import { formatBRL } from "@/lib/format";

function buildPoints(transacoes, saldoFinal) {
  const hoje = new Date();
  const diasStr = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(hoje);
    d.setDate(d.getDate() - (6 - i));
    return d.toISOString().slice(0, 10);
  });

  // Reconstrói saldo diário trabalhando de hoje para trás.
  // saldos[6] = saldoFinal (hoje). Para cada dia anterior, remove o
  // efeito líquido das transações do dia seguinte.
  const saldos = Array(7).fill(0);
  saldos[6] = Number(saldoFinal);
  for (let i = 5; i >= 0; i--) {
    const netDia = (transacoes ?? [])
      .filter((t) => t.dataTransacao === diasStr[i + 1])
      .reduce((acc, t) => acc + (t.tipo === "RECEITA" ? +t.valor : -t.valor), 0);
    saldos[i] = saldos[i + 1] - netDia;
  }

  const saldoMax = Math.max(...saldos);
  const saldoMin = Math.min(...saldos);
  const range    = saldoMax - saldoMin || 1;
  const YMIN = 10;
  const YMAX = 120;
  const W    = 320;

  const pontos = diasStr.map((dateStr, i) => {
    const d = new Date(dateStr + "T12:00:00");
    const labels = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
    return {
      x: Math.round((i / 6) * W),
      y: Math.round(YMAX - ((saldos[i] - saldoMin) / range) * (YMAX - YMIN)),
      label: labels[d.getDay()],
      v: formatBRL(saldos[i]),
    };
  });

  return { pontos, saldoMax };
}

export const BalanceChart = ({ transacoes = [], saldoFinal = 0 }) => {
  const [hover, setHover] = useState(6);

  const { pontos, saldoMax } = useMemo(
    () => buildPoints(transacoes, saldoFinal),
    [transacoes, saldoFinal],
  );

  const path = pontos
    .map((p, i, arr) => {
      if (i === 0) return `M${p.x},${p.y}`;
      const prev = arr[i - 1];
      const cx = (prev.x + p.x) / 2;
      return `Q${cx},${prev.y} ${cx},${(prev.y + p.y) / 2} T${p.x},${p.y}`;
    })
    .join(" ");

  const lastX = pontos[pontos.length - 1]?.x ?? 320;
  const firstX = pontos[0]?.x ?? 0;
  const areaPath = `${path} L${lastX},130 L${firstX},130 Z`;
  const active = hover !== null ? pontos[hover] : pontos[6];

  return (
    <div className="w-full">
      <div className="flex justify-between text-[9px] text-muted-foreground/70 font-medium mb-1 px-0.5">
        <span>{formatBRL(saldoMax)}</span>
        <span className="text-primary font-bold">{active?.v}</span>
      </div>

      <div className="w-full h-[130px] relative">
        <svg
          viewBox="0 0 320 130"
          className="w-full h-full overflow-visible"
          preserveAspectRatio="none"
          onMouseLeave={() => setHover(6)}
        >
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

          {[20, 60, 100].map((y) => (
            <line
              key={y} x1="0" y1={y} x2="320" y2={y}
              stroke="hsl(var(--border))" strokeDasharray="2 4" strokeWidth="1"
            />
          ))}

          {/* Linha de comparação (período anterior) */}
          <path
            d="M0,100 Q40,92 80,96 T160,86 T240,80 T320,72"
            fill="none"
            stroke="hsl(var(--muted-foreground))"
            strokeOpacity="0.3"
            strokeWidth="1.5"
            strokeDasharray="3 3"
            strokeLinecap="round"
          />

          <path d={areaPath} fill="url(#areaGrad)" />
          <path
            d={path}
            fill="none"
            stroke="url(#lineGrad)"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />

          {pontos.map((p, i) => (
            <g key={i}>
              <rect
                x={p.x - 22} y={0} width={44} height={130}
                fill="transparent"
                onMouseEnter={() => setHover(i)}
                style={{ cursor: "pointer" }}
              />
              {hover === i && (
                <>
                  <line
                    x1={p.x} y1={0} x2={p.x} y2={130}
                    stroke="hsl(var(--primary))"
                    strokeOpacity="0.25"
                    strokeWidth="1"
                    strokeDasharray="2 2"
                  />
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

      <div className="flex justify-between text-[9.5px] text-muted-foreground font-medium mt-1.5 px-0.5">
        {pontos.map((p, i) => (
          <span key={i} className={hover === i ? "text-primary font-bold" : ""}>
            {p.label}
          </span>
        ))}
      </div>
    </div>
  );
};
