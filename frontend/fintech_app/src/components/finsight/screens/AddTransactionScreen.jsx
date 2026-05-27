// AddTransactionScreen: formulário de criação de transação.
//
// Payload final (montado abaixo) já está no formato esperado pelo
// TransacaoRequestDTO do backend — basta enviar:
//
//   POST /transacoes
//   {
//     usuarioId, contaId, extratoId, tipo, valor, dataTransacao,
//     descricaoUsuario, categoriaId, subcategoria, estabelecimento,
//     origem, observacao
//   }
//
// Onde plugar com o backend:
//   - Substituir o handleSubmit pelo `useMutation(criarTransacao)`.
//   - Disparar toast.success em onSuccess / toast.error em onError.
import { useState } from "react";
import { ArrowUpRight, ArrowDownLeft, Save } from "lucide-react";

import { usuarioAtual, contaPadrao, categoriasGasto, categoriasReceita } from "@/mocks";
import { Button } from "@/components/ui/button";
import { InputMonetario } from "@/components/ui/input-monetario";
import { Combobox } from "@/components/ui/combobox";
import { DatePicker } from "@/components/ui/date-picker";
import { toast } from "@/hooks/use-toast";

export const AddTransactionScreen = () => {
  // Estado do formulário — espelha campos do TransacaoRequestDTO.
  const [tipo, setTipo] = useState("GASTO");
  const [valor, setValor] = useState(undefined);
  const [categoriaId, setCategoriaId] = useState(undefined);
  const [data, setData] = useState(new Date());
  const [descricao, setDescricao] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [touched, setTouched] = useState(false);

  // A lista de categorias do Combobox depende do tipo selecionado.
  const itensCategoria = (tipo === "RECEITA" ? categoriasReceita : categoriasGasto).map((c) => ({
    id: c.id,
    label: c.nome,
    parentId: c.parentId,
  }));

  // Validações simples.
  const erroValor = touched && (!valor || valor <= 0);
  const erroCategoria = touched && !categoriaId;
  const formularioValido = !!valor && valor > 0 && !!categoriaId;

  const resetar = () => {
    setTipo("GASTO");
    setValor(undefined);
    setCategoriaId(undefined);
    setData(new Date());
    setDescricao("");
    setTouched(false);
  };

  const handleSubmit = async () => {
    setTouched(true);
    if (!formularioValido) {
      toast.warning({
        title: "Verifique os campos",
        description: "Preencha o valor e selecione uma categoria.",
      });
      return;
    }

    // Payload pronto pra enviar ao backend (TransacaoRequestDTO).
    const payload = {
      usuarioId: usuarioAtual.id,
      contaId: contaPadrao.id,
      extratoId: null,                     // transação manual → sem extrato
      tipo,                                 // RECEITA | GASTO
      valor,                                // BigDecimal (number)
      dataTransacao: data.toISOString().slice(0, 10), // LocalDate "yyyy-MM-dd"
      descricaoUsuario: descricao || null,
      categoriaId,
      subcategoria: null,
      estabelecimento: null,
      origem: "manual",                     // enum OrigemTransacao
      observacao: null,
    };

    setSubmitting(true);
    // Simulação de chamada — quando integrar, troque por:
    //   await axios.post("/transacoes", payload)
    console.log("POST /transacoes", payload);
    await new Promise((r) => setTimeout(r, 600));
    setSubmitting(false);

    toast.success({
      title: "Transação registrada",
      description: `${tipo === "RECEITA" ? "Receita" : "Gasto"} salvo em ${contaPadrao.nome}.`,
    });
    resetar();
  };

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
     <div className="max-w-2xl mx-auto w-full">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-[20px] lg:text-[28px] font-extrabold tracking-tight text-foreground">Nova transação</h1>
          <p className="text-[11.5px] lg:text-[13px] text-muted-foreground mt-0.5">
            Registre uma receita ou gasto manual
          </p>
        </div>
        <Button variant="ghost" size="sm" onClick={resetar}>Cancelar</Button>
      </div>

      {/* Tipo (segmented) */}
      <div className="mt-4 grid grid-cols-2 gap-1 p-1 bg-secondary rounded-2xl">
        <button
          onClick={() => setTipo("GASTO")}
          className={`flex items-center justify-center gap-1.5 py-2 rounded-xl text-[12.5px] font-semibold transition-all ${
            tipo === "GASTO" ? "bg-card text-destructive shadow-sm" : "text-muted-foreground"
          }`}
        >
          <ArrowUpRight className="w-3.5 h-3.5" strokeWidth={2.5} /> Gasto
        </button>
        <button
          onClick={() => setTipo("RECEITA")}
          className={`flex items-center justify-center gap-1.5 py-2 rounded-xl text-[12.5px] font-semibold transition-all ${
            tipo === "RECEITA" ? "bg-card text-success shadow-sm" : "text-muted-foreground"
          }`}
        >
          <ArrowDownLeft className="w-3.5 h-3.5" strokeWidth={2.5} /> Receita
        </button>
      </div>

      {/* Valor (hero) */}
      <div className="card-soft p-5 mt-4">
        <p className="section-label text-center">Valor</p>
        <div className="mt-3 flex items-center justify-center gap-2">
          <span
            className={`text-[22px] font-extrabold ${
              tipo === "GASTO" ? "text-destructive" : "text-success"
            }`}
          >
            {tipo === "GASTO" ? "−" : "+"}
          </span>
          <div className="w-[200px]">
            <InputMonetario
              value={valor}
              onChange={setValor}
              className="h-12 text-[20px] text-center"
            />
          </div>
        </div>
        {erroValor ? (
          <p className="text-[11.5px] text-destructive mt-2 font-medium text-center">
            Informe um valor maior que zero
          </p>
        ) : (
          <p className="text-[11px] text-muted-foreground mt-2 text-center">
            BRL · {contaPadrao.banco} ({contaPadrao.nome})
          </p>
        )}
      </div>

      {/* Categoria via Combobox (suporta hierarquia) */}
      <div className="mt-5">
        <label className="section-label">Categoria</label>
        <div className="mt-1.5">
          <Combobox
            items={itensCategoria}
            value={categoriaId}
            onChange={setCategoriaId}
            placeholder="Selecione uma categoria"
          />
        </div>
        {erroCategoria && (
          <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
            Selecione uma categoria
          </p>
        )}
      </div>

      {/* Detalhes (Data + Descrição) — em sm+ ficam lado a lado. */}
      <div className="mt-5 grid sm:grid-cols-2 gap-3">
        <div>
          <label className="section-label">Data</label>
          <div className="mt-1.5">
            <DatePicker value={data} onChange={setData} />
          </div>
        </div>
        <div>
          <label className="section-label">Descrição (opcional)</label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            placeholder="Ex.: Almoço com cliente"
            maxLength={120}
            className="mt-1.5 w-full h-10 px-3 rounded-xl bg-card border border-border text-[13.5px] outline-none placeholder:text-muted-foreground/60 focus:ring-2 focus:ring-primary/30 focus:border-primary/40 transition-all"
          />
        </div>
      </div>

      {/* CTAs */}
      <div className="mt-6 space-y-2">
        <Button
          className="w-full"
          size="lg"
          leftIcon={Save}
          loading={submitting}
          onClick={handleSubmit}
        >
          Salvar transação
        </Button>
        <Button variant="secondary" className="w-full" disabled={submitting}>
          Salvar como rascunho
        </Button>
      </div>
     </div>
    </div>
  );
};
