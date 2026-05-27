/* =============================================================================
 * script.js — FinSight (Home / Visão Geral)
 *
 * Responsabilidades:
 *   1. Renderizar os ícones do Lucide (substitui <i data-lucide="..."> por <svg>).
 *   2. Preencher a data atual na pílula do canto superior direito.
 *   3. Alternar visibilidade do saldo (ícone de olho).
 *   4. Trocar o período ativo do segmented control (Semana / Mês / Ano).
 *   5. Renderizar o BalanceChart em SVG puro com hover interativo nos 7 pontos.
 *   6. Trocar a aba ativa da SideNav (desktop) e da BottomNav (mobile).
 * ========================================================================== */

// ── Bootstrap dos ícones ────────────────────────────────────────────────────
// O Lucide expõe lucide.createIcons() que varre o DOM por elementos com
// `data-lucide="<nome>"` e substitui por um <svg> equivalente.
function renderIcons() {
  if (window.lucide && typeof window.lucide.createIcons === "function") {
    window.lucide.createIcons();
  }
}

// ── Data atual (pt-BR) na pílula do header ──────────────────────────────────
function setDataAtual() {
  const el = document.getElementById("data-atual");
  if (!el) return;
  el.textContent = new Date().toLocaleDateString("pt-BR", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

// ── Toggle de visibilidade do saldo ────────────────────────────────────────
function setupToggleSaldo() {
  const btn = document.getElementById("toggle-saldo");
  const valor = document.getElementById("saldo-valor");
  const disponivel = document.getElementById("saldo-disponivel");
  if (!btn || !valor || !disponivel) return;

  // Guarda os valores reais para conseguir restaurar depois do "ocultar".
  const VALOR_REAL = valor.textContent;
  const DISP_REAL = disponivel.textContent;
  let oculto = false;

  btn.addEventListener("click", () => {
    oculto = !oculto;
    valor.textContent = oculto ? "R$ ••••••" : VALOR_REAL;
    disponivel.textContent = oculto ? "•••" : DISP_REAL;

    // Troca o ícone (eye <-> eye-off). Para isso recriamos o <i> e
    // pedimos ao Lucide para reprocessar.
    const ico = document.getElementById("ico-saldo");
    if (ico) {
      const novo = document.createElement("i");
      novo.id = "ico-saldo";
      novo.setAttribute("data-lucide", oculto ? "eye-off" : "eye");
      novo.className = "w-3 h-3";
      ico.replaceWith(novo);
      renderIcons();
    }

    btn.setAttribute("aria-label", oculto ? "Mostrar saldo" : "Ocultar saldo");
  });
}

// ── Segmented control de período (Semana / Mês / Ano) ──────────────────────
function setupRangeButtons() {
  const grupo = document.querySelector("[data-range-group]");
  if (!grupo) return;
  const botoes = grupo.querySelectorAll(".range-btn");

  botoes.forEach((b) => {
    b.addEventListener("click", () => {
      botoes.forEach((x) => x.classList.remove("is-active"));
      b.classList.add("is-active");
      // Hook para integração futura: rerender do gráfico com o novo intervalo.
      // document.dispatchEvent(new CustomEvent("range:change", { detail: b.dataset.range }));
    });
  });
}

// ── BalanceChart: gráfico de saldo em SVG com 7 pontos e hover ─────────────
// Coordenadas internas do viewBox (320 x 130). Cada ponto guarda x/y, label
// do dia da semana e o valor formatado mostrado no canto superior direito.
const points = [
  { x: 0,   y: 95, label: "Seg", v: "R$12,4k" },
  { x: 53,  y: 78, label: "Ter", v: "R$13,1k" },
  { x: 106, y: 88, label: "Qua", v: "R$12,8k" },
  { x: 160, y: 55, label: "Qui", v: "R$14,0k" },
  { x: 213, y: 42, label: "Sex", v: "R$14,5k" },
  { x: 266, y: 28, label: "Sáb", v: "R$14,7k" },
  { x: 320, y: 18, label: "Dom", v: "R$14,87k" },
];

function pathFromPoints(arr) {
  return arr
    .map((p, i, a) => {
      if (i === 0) return `M${p.x},${p.y}`;
      const prev = a[i - 1];
      const cx = (prev.x + p.x) / 2;
      return `Q${cx},${prev.y} ${cx},${(prev.y + p.y) / 2} T${p.x},${p.y}`;
    })
    .join(" ");
}

function setupBalanceChart() {
  const svg = document.getElementById("balance-chart");
  const labelsEl = document.getElementById("chart-labels");
  const activeValueEl = document.getElementById("chart-active-value");
  if (!svg || !labelsEl) return;

  // Estado: índice do ponto ativo (último por padrão).
  let hover = points.length - 1;

  const SVG_NS = "http://www.w3.org/2000/svg";
  const linePath = pathFromPoints(points);
  const areaPath = `${linePath} L320,130 L0,130 Z`;

  function el(name, attrs = {}) {
    const node = document.createElementNS(SVG_NS, name);
    for (const k in attrs) node.setAttribute(k, attrs[k]);
    return node;
  }

  function render() {
    // Mantém os <defs> (gradientes) e remove o resto.
    [...svg.querySelectorAll(":scope > :not(defs)")].forEach((n) => n.remove());

    // Linhas-guia horizontais.
    [20, 60, 100].forEach((y) => {
      svg.appendChild(el("line", {
        x1: 0, y1: y, x2: 320, y2: y,
        stroke: "hsl(var(--border))",
        "stroke-dasharray": "2 4",
        "stroke-width": 1,
      }));
    });

    // Curva tracejada (período anterior).
    svg.appendChild(el("path", {
      d: "M0,100 Q40,92 80,96 T160,86 T240,80 T320,72",
      fill: "none",
      stroke: "hsl(var(--muted-foreground))",
      "stroke-opacity": 0.3,
      "stroke-width": 1.5,
      "stroke-dasharray": "3 3",
      "stroke-linecap": "round",
    }));

    // Área preenchida e linha principal.
    svg.appendChild(el("path", { d: areaPath, fill: "url(#areaGrad)" }));
    svg.appendChild(el("path", {
      d: linePath,
      fill: "none",
      stroke: "url(#lineGrad)",
      "stroke-width": 2.5,
      "stroke-linecap": "round",
      "stroke-linejoin": "round",
    }));

    // "Hitboxes" invisíveis + ponto ativo destacado.
    points.forEach((p, i) => {
      const g = el("g");

      const hit = el("rect", {
        x: p.x - 22, y: 0, width: 44, height: 130,
        fill: "transparent", style: "cursor: pointer",
      });
      hit.addEventListener("mouseenter", () => setHover(i));
      g.appendChild(hit);

      if (hover === i) {
        g.appendChild(el("line", {
          x1: p.x, y1: 0, x2: p.x, y2: 130,
          stroke: "hsl(var(--primary))",
          "stroke-opacity": 0.25,
          "stroke-width": 1,
          "stroke-dasharray": "2 2",
        }));
        g.appendChild(el("circle", {
          cx: p.x, cy: p.y, r: 7,
          fill: "hsl(var(--primary))",
          "fill-opacity": 0.15,
        }));
        g.appendChild(el("circle", {
          cx: p.x, cy: p.y, r: 3.75,
          fill: "hsl(var(--card))",
          stroke: "hsl(var(--primary))",
          "stroke-width": 2.25,
        }));
      }

      svg.appendChild(g);
    });

    // Atualiza rótulo numérico (canto superior).
    if (activeValueEl) {
      activeValueEl.textContent = points[hover].v;
    }
    // Atualiza rótulos dos dias (linha inferior) e destaque do ativo.
    labelsEl.innerHTML = "";
    points.forEach((p, i) => {
      const span = document.createElement("span");
      span.textContent = p.label;
      if (i === hover) span.className = "text-primary font-bold";
      labelsEl.appendChild(span);
    });
  }

  function setHover(i) {
    if (i === hover) return;
    hover = i;
    render();
  }

  // Mouse leave do SVG inteiro restaura o último ponto (mais recente).
  svg.addEventListener("mouseleave", () => setHover(points.length - 1));

  render();
}

// ── Navegação (SideNav + BottomNav) ────────────────────────────────────────
// Mantém qual aba está ativa em ambas as barras. Como esta página é só a
// "home", as outras abas apenas mostram um console.log para fins didáticos.
function setupNavigation() {
  const buttons = document.querySelectorAll("[data-nav]");
  buttons.forEach((b) => {
    b.addEventListener("click", () => {
      const target = b.dataset.nav;
      // Atualiza visualmente todos os botões com a mesma data-nav.
      buttons.forEach((x) => {
        x.classList.toggle("is-active", x.dataset.nav === target);
      });
      // Hook de integração futura (rotear para outra tela).
      // location.hash = target;
      console.log("[nav] alterado para:", target);
    });
  });

  // Botão "+" da BottomNav e o botão "Nova transação" da SideNav.
  document.querySelectorAll('[data-action="add"]').forEach((b) => {
    b.addEventListener("click", () => {
      console.log("[action] nova transação");
    });
  });
}

// ── Boot ────────────────────────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
  renderIcons();
  setDataAtual();
  setupToggleSaldo();
  setupRangeButtons();
  setupBalanceChart();
  setupNavigation();
});
