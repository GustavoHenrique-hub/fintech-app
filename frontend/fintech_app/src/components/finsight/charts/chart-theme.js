// Base comum dos gráficos (ApexCharts — MIT, configurado por objetos JSON de
// `options` + `series`). Cores em hex espelham os tokens de src/index.css, porque
// o ApexCharts precisa de hex para gerar opacidades e gradientes.
//
// Receita/Gasto (#2ECC71 / #E74C3C) passam no validador de paleta (CVD ΔE 10,7);
// o verde tem contraste baixo com a superfície, então os valores sempre aparecem
// em legenda + tooltip, nunca só pela cor.
import { formatBRL } from "@/lib/format";

export const CORES = {
  primaria: "#7159E7",   // --primary  250 75% 63%
  receita: "#2ECC71",    // --success  145 63% 49%
  gasto: "#E74C3C",      // --destructive 6 78% 57%
  texto: "#18181B",      // --foreground
  textoSuave: "#67676F", // --muted-foreground
  grade: "#E9E9EC",      // --border
  superficie: "#FFFFFF", // --card
  semCategoria: "#94A3B8",
};

const FONTE = "inherit";

/** "R$ 1,2 mil" / "R$ 3,4 mi" — rótulos de eixo curtos. */
export function formatBRLCompacto(valor) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency", currency: "BRL", notation: "compact", maximumFractionDigits: 1,
  }).format(valor);
}

/** Opções compartilhadas: sem toolbar, grade hairline sólida, eixos recessivos, tooltip em R$. */
export function opcoesBase() {
  return {
    chart: {
      fontFamily: FONTE,
      foreColor: CORES.textoSuave,
      toolbar: { show: false },
      zoom: { enabled: false },
      animations: { enabled: true, speed: 350 },
      parentHeightOffset: 0,
    },
    dataLabels: { enabled: false },
    grid: {
      borderColor: CORES.grade,
      strokeDashArray: 0,
      xaxis: { lines: { show: false } },
      padding: { left: 4, right: 8, top: 0, bottom: 0 },
    },
    legend: { show: false },
    xaxis: {
      axisBorder: { show: false },
      axisTicks: { show: false },
      tooltip: { enabled: false },
      labels: { style: { fontSize: "11px" }, hideOverlappingLabels: true },
    },
    yaxis: {
      labels: {
        style: { fontSize: "11px" },
        formatter: (v) => formatBRLCompacto(v),
      },
    },
    tooltip: {
      theme: "light",
      style: { fontSize: "12px", fontFamily: FONTE },
      y: { formatter: (v) => formatBRL(v) },
    },
  };
}
