// Setup global do Vitest: roda antes de qualquer arquivo de teste.
// 1) Importa matchers customizados (toBeInTheDocument, toHaveClass, ...).
// 2) Faz polyfill de matchMedia (jsdom não implementa) — código que usa
//    window.matchMedia (como useIsMobile) quebraria sem isso.
import "@testing-library/jest-dom";

Object.defineProperty(window, "matchMedia", {
  writable: true,
  value: (query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: () => {},
    removeListener: () => {},
    addEventListener: () => {},
    removeEventListener: () => {},
    dispatchEvent: () => {},
  }),
});
