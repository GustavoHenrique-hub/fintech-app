import { useState, useMemo } from "react";
import { formatBRL } from "@/lib/format";

const W = 320;
const YMIN = 10;
const YMAX = 120;
const DIAS_LABEL = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
const MESES_LABEL = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];

// Monta pontos (x, y, label, v) a partir de uma série de saldos já calculada —
// escala os valores para o viewBox fixo do SVG (0..W, YMIN..YMAX).
function montarPontos(saldos, labels) {
  const saldoMax = Math.max(...saldos);
  const saldoMin = Math.min(...saldos);
  const range = saldoMax - saldoMin || 1;
  const n = saldos.length;

  const pontos = saldos.map((saldo, i) => ({
    x: Math.round((i / (n - 1 || 1)) * W),
    y: Math.round(YMAX - ((saldo - saldoMin) / range) * (YMAX - YMIN)),
    label: labels[i],
    v: formatBRL(saldo),
  }));

  return { pontos, saldoMax };
}

// Reconstrói o saldo dia a dia trabalhando de hoje para trás: saldos[last] =
// saldoFinal (hoje). Para cada dia anterior, remove o efeito líquido das
// transações do dia seguinte.
function buildPointsDiario(transacoes, saldoFinal, n, labelFn) {
  const hoje = new Date();
  const diasStr = Array.from({ length: n }, (_, i) => {
    const d = new Date(hoje);
    d.setDate(d.getDate() - (n - 1 - i));
    return d.toISOString().slice(0, 10);
  });

  const saldos = Array(n).fill(0);
  saldos[n - 1] = Number(saldoFinal);
  for (let i = n - 2; i >= 0; i--) {
    const netDia = (transacoes ?? [])
      .filter((t) => t.dataTransacao === diasStr[i + 1])
      .reduce((acc, t) => acc + (t.tipo === "RECEITA" ? +t.valor : -t.valor), 0);
    saldos[i] = saldos[i + 1] - netDia;
  }

  const labels = diasStr.map((dateStr, i) => labelFn(new Date(dateStr + "T12:00:00"), i, n));
  return montarPontos(saldos, labels);
}

// Mesma reconstrução "de trás pra frente", mas por mês — usado no range "Ano".
function buildPointsMensal(transacoes, saldoFinal, n = 12) {
  const hoje = new Date();
  const meses = Array.from({ length: n }, (_, i) => {
    const d = new Date(hoje.getFullYear(), hoje.getMonth() - (n - 1 - i), 1);
    return { ano: d.getFullYear(), mes: d.getMonth() + 1 };
  });

  const saldos = Array(n).fill(0);
  saldos[n - 1] = Number(saldoFinal);
  for (let i = n - 2; i >= 0; i--) {
    const { ano, mes } = meses[i + 1];
    const prefixo = `${ano}-${String(mes).padStart(2, "0")}`;
    const netMes = (transacoes ?? [])
      .filter((t) => t.dataTransacao.startsWith(prefixo))
      .reduce((acc, t) => acc + (t.tipo === "RECEITA" ? +t.valor : -t.valor), 0);
    saldos[i] = saldos[i + 1] - netMes;
  }

  const labels = meses.map(({ mes }) => MESES_LABEL[mes - 1]);
  return montarPontos(saldos, labels);
}

function buildPoints(transacoes, saldoFinal, range) {
  if (range === "Ano") return buildPointsMensal(transacoes, saldoFinal, 12);
  if (range === "Mês") {
    // 30 dias: rotular só a cada ~5 dias para não lotar o eixo.
    return buildPointsDiario(transacoes, saldoFinal, 30, (d, i, n) =>
      i % 5 === 0 || i === n - 1 ? String(d.getDate()).padStart(2, "0") : "",
    );
  }
  // "Semana" (default): 7 dias, rótulo por dia da semana — comportamento original.
  return buildPointsDiario(transacoes, saldoFinal, 7, (d) => DIAS_LABEL[d.getDay()]);
}

export const BalanceChart = ({ transacoes = [], saldoFinal = 0, range = "Semana" }) => {
  const { pontos, saldoMax } = useMemo(
    () => buildPoints(transacoes, saldoFinal, range),
    [transacoes, saldoFinal, range],
  );
  const [hover, setHover] = useState(pontos.length - 1);

  // Se o range mudar e o índice de hover ficar fora da nova série, volta pro último ponto.
  const hoverAtivo = hover < pontos.length ? hover : pontos.length - 1;

  const path = pontos
    .map((p, i, arr) => {
      if (i === 0) return `M${p.x},${p.y}`;
      const prev = arr[i - 1];
      const cx = (prev.x + p.x) / 2;
      return `Q${cx},${prev.y} ${cx},${(prev.y + p.y) / 2} T${p.x},${p.y}`;
    })
    .join(" ");

  const lastX = pontos[pontos.length - 1]?.x ?? W;
  const firstX = pontos[0]?.x ?? 0;
  const areaPath = `${path} L${lastX},130 L${firstX},130 Z`;
  const active = pontos[hoverAtivo] ?? pontos[pontos.length - 1];
  const hitWidth = Math.max(W / pontos.length, 8);

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
          onMouseLeave={() => setHover(pontos.length - 1)}
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
                x={p.x - hitWidth / 2} y={0} width={hitWidth} height={130}
                fill="transparent"
                onMouseEnter={() => setHover(i)}
                style={{ cursor: "pointer" }}
              />
              {hoverAtivo === i && (
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
          <span key={i} className={hoverAtivo === i ? "text-primary font-bold" : ""}>
            {p.label}
          </span>
        ))}
      </div>
    </div>
  );
};
