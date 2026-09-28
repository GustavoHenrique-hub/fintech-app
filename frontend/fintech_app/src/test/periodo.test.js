import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { format } from "date-fns";
import {
  descreverPeriodo, getDataReferencia, getIntervaloPeriodo, getIntervaloPeriodoAnterior,
} from "@/lib/periodo";

const iso = (d) => format(d, "yyyy-MM-dd");
const tx = (dataTransacao, extra = {}) => ({ dataTransacao, statusRevisao: "CONFIRMADA", indEstorno: "N", ...extra });

describe("periodo", () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(new Date(2026, 8, 28, 12)); // 28/09/2026
  });
  afterEach(() => vi.useRealTimers());

  it("ancora no lançamento confirmado mais recente quando é de mês anterior", () => {
    const ref = getDataReferencia([
      tx("2026-08-06"),
      tx("2026-08-28"),
      tx("2026-09-10", { statusRevisao: "PENDENTE_REVISAO" }),
      tx("2026-09-12", { estornadoAt: "2026-09-13T10:00:00Z" }),
    ]);
    expect(iso(ref)).toBe("2026-08-28");
    expect(getIntervaloPeriodo("Mês", ref)).toEqual({ inicio: "2026-08-01", fim: "2026-08-31" });
    expect(getIntervaloPeriodoAnterior("Mês", ref)).toEqual({ inicio: "2026-07-01", fim: "2026-07-31" });
    expect(descreverPeriodo("Mês", getIntervaloPeriodo("Mês", ref))).toBe("agosto de 2026");
  });

  it("usa hoje sem lançamentos confirmados e nunca passa de hoje", () => {
    expect(iso(getDataReferencia([]))).toBe("2026-09-28");
    expect(iso(getDataReferencia([tx("2026-12-01")]))).toBe("2026-09-28");
    expect(getIntervaloPeriodo("Mês", getDataReferencia([]))).toEqual({ inicio: "2026-09-01", fim: "2026-09-28" });
  });
});
