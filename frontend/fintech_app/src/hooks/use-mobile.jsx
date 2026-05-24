// Hook utilitário: devolve `true` quando a viewport está em tamanho mobile
// (< 768px). Usa matchMedia para reagir a mudanças (rotação, resize) sem
// disparar re-renders desnecessários.
import * as React from "react";

const MOBILE_BREAKPOINT = 768;

export function useIsMobile() {
  // Começa como undefined para diferenciar "ainda não medido" de "não é mobile".
  const [isMobile, setIsMobile] = React.useState(undefined);

  React.useEffect(() => {
    const mql = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`);
    const onChange = () => setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);

    // Mede uma vez imediatamente e escuta mudanças futuras.
    mql.addEventListener("change", onChange);
    setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);
    return () => mql.removeEventListener("change", onChange);
  }, []);

  // !! força conversão para booleano (undefined -> false).
  return !!isMobile;
}
