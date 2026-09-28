// Saldo dia a dia (Semana/Mês) ou mês a mês (Ano) da conta ativa, em área.
// A série é reconstruída de hoje para trás: saldos[last] = saldo atual e cada
// ponto anterior remove o efeito líquido das transações do período seguinte.
// Só entram transações que contam no saldo (confirmadas e não estornadas).
import { useMemo } from "react";
import Chart from "react-apexcharts";

import { formatBRL } from "@/lib/format";
import { contaNosTotais, valorComSinal } from "@/lib/transacoes";
import { CORES, opcoesBase } from "./charts/chart-theme";

const DIAS_LABEL = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
const MESES_LABEL = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];

const isoLocal = (d) =>
  `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;

function serieDiaria(transacoes, saldoFinal, n, labelFn) {
  const hoje = new Date();
  const dias = Array.from({ length: n }, (_, i) => {
    const d = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate() - (n - 1 - i));
    return { iso: isoLocal(d), data: d };
  });

  const saldos = Array(n).fill(0);
  saldos[n - 1] = saldoFinal;
  for (let i = n - 2; i >= 0; i--) {
    const netDia = transacoes
      .filter((t) => t.dataTransacao === dias[i + 1].iso)
      .reduce((acc, t) => acc + valorComSinal(t), 0);
    saldos[i] = saldos[i + 1] - netDia;
  }
  return { saldos, labels: dias.map(({ data }, i) => labelFn(data, i)) };
}

function serieMensal(transacoes, saldoFinal, n = 12) {
  const hoje = new Date();
  const meses = Array.from({ length: n }, (_, i) => {
    const d = new Date(hoje.getFullYear(), hoje.getMonth() - (n - 1 - i), 1);
    return { ano: d.getFullYear(), mes: d.getMonth() + 1 };
  });

  const saldos = Array(n).fill(0);
  saldos[n - 1] = saldoFinal;
  for (let i = n - 2; i >= 0; i--) {
    const { ano, mes } = meses[i + 1];
    const prefixo = `${ano}-${String(mes).padStart(2, "0")}`;
    const netMes = transacoes
      .filter((t) => t.dataTransacao?.startsWith(prefixo))
      .reduce((acc, t) => acc + valorComSinal(t), 0);
    saldos[i] = saldos[i + 1] - netMes;
  }
  return { saldos, labels: meses.map(({ ano, mes }) => `${MESES_LABEL[mes - 1]}/${String(ano).slice(2)}`) };
}

function montarSerie(transacoes, saldoFinal, range) {
  const validas = transacoes.filter(contaNosTotais);
  if (range === "Ano") return serieMensal(validas, saldoFinal, 12);
  if (range === "Mês") {
    return serieDiaria(validas, saldoFinal, 30, (d) => `${String(d.getDate()).padStart(2, "0")}/${String(d.getMonth() + 1).padStart(2, "0")}`);
  }
  return serieDiaria(validas, saldoFinal, 7, (d) => DIAS_LABEL[d.getDay()]);
}

export const BalanceChart = ({ transacoes = [], saldoFinal = 0, range = "Semana" }) => {
  const { saldos, labels } = useMemo(
    () => montarSerie(transacoes, Number(saldoFinal) || 0, range),
    [transacoes, saldoFinal, range],
  );

  const minimo = Math.min(...saldos);
  const atual = saldos[saldos.length - 1];
  const negativo = atual < 0;
  const base = opcoesBase();

  const options = {
    ...base,
    chart: { ...base.chart, id: "saldo", type: "area" },
    colors: [CORES.primaria],
    stroke: { curve: "monotoneCubic", width: 2, lineCap: "round" },
    fill: {
      type: "gradient",
      gradient: { shadeIntensity: 0, opacityFrom: 0.18, opacityTo: 0.02, stops: [0, 100] },
    },
    markers: {
      size: 0,
      strokeColors: CORES.superficie,
      strokeWidth: 2,
      hover: { size: 5 },
      // Ponto de hoje sempre visível, com o valor atual.
      discrete: [{
        seriesIndex: 0,
        dataPointIndex: saldos.length - 1,
        fillColor: negativo ? CORES.gasto : CORES.primaria,
        strokeColor: CORES.superficie,
        size: 5,
      }],
    },
    xaxis: {
      ...base.xaxis,
      categories: labels,
      tickAmount: range === "Mês" ? 6 : undefined,
      labels: { ...base.xaxis.labels, rotate: 0 },
    },
    // Quando o saldo cruza o zero, a linha de referência deixa claro onde começa o negativo.
    annotations: minimo < 0 ? {
      yaxis: [{
        y: 0,
        borderColor: CORES.gasto,
        strokeDashArray: 0,
        label: {
          text: "R$ 0",
          position: "left",
          textAnchor: "start",
          borderWidth: 0,
          style: { background: "transparent", color: CORES.gasto, fontSize: "10px", fontWeight: 600 },
        },
      }],
    } : {},
    tooltip: { ...base.tooltip, x: { show: true } },
  };

  return (
    <div className="w-full">
      <div className="flex items-baseline justify-between">
        <span className="text-[11px] text-muted-foreground font-medium">Saldo disponível hoje</span>
        <span className={`text-[13px] font-extrabold tabular-nums ${negativo ? "text-destructive" : "text-foreground"}`}>
          {formatBRL(atual)}
        </span>
      </div>
      <div className="-mx-1 mt-1">
        <Chart
          type="area"
          height={210}
          options={options}
          series={[{ name: "Saldo", data: saldos.map((v) => Number(v.toFixed(2))) }]}
        />
      </div>
    </div>
  );
};
