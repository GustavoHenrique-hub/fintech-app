import { useState, useMemo } from "react";
import { ArrowUpRight, ArrowDownLeft, Save } from "lucide-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { useUsuario } from "@/hooks/use-usuario";
import { useContas } from "@/hooks/use-contas";
import { useCategorias } from "@/hooks/use-categorias";
import { transacaoService } from "@/services";
import { USUARIO_ID } from "@/lib/constants";
import { Button } from "@/components/ui/button";
import { InputMonetario } from "@/components/ui/input-monetario";
import { Combobox } from "@/components/ui/combobox";
import { DatePicker } from "@/components/ui/date-picker";
import { toast } from "@/hooks/use-toast";

export const AddTransactionScreen = () => {
  const [tipo, setTipo] = useState("GASTO");
  const [valor, setValor] = useState(undefined);
  const [categoriaId, setCategoriaId] = useState(undefined);
  const [data, setData] = useState(new Date());
  const [descricao, setDescricao] = useState("");
  const [touched, setTouched] = useState(false);

  const queryClient = useQueryClient();
  const { data: usuario } = useUsuario();
  const { data: contas = [] } = useContas();
  const { data: categorias = [] } = useCategorias();

  const contaPadrao = useMemo(
    () => contas.find((c) => c.padrao) ?? contas[0] ?? null,
    [contas],
  );

  const categoriasGasto   = categorias.filter((c) => c.tipo === "gasto"   || c.tipo === "ambos");
  const categoriasReceita = categorias.filter((c) => c.tipo === "receita" || c.tipo === "ambos");

  const itensCategoria = (tipo === "RECEITA" ? categoriasReceita : categoriasGasto).map((c) => ({
    id: c.id,
    label: c.nome,
    parentId: c.parentId,
  }));

  const erroValor    = touched && (!valor || valor <= 0);
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

  const { mutate: criarTransacao, isPending } = useMutation({
    mutationFn: (payload) => transacaoService.criar(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["transacoes", USUARIO_ID] });
      queryClient.invalidateQueries({ queryKey: ["snapshots", USUARIO_ID] });
      toast.success({
        title: "Transação registrada",
        description: `${tipo === "RECEITA" ? "Receita" : "Gasto"} salvo em ${contaPadrao?.nome ?? "conta"}.`,
      });
      resetar();
    },
    onError: () => {
      toast.error({
        title: "Erro ao salvar transação",
        description: "Não foi possível registrar. Tente novamente.",
      });
    },
  });

  const handleSubmit = () => {
    setTouched(true);
    if (!formularioValido) {
      toast.warning({
        title: "Verifique os campos",
        description: "Preencha o valor e selecione uma categoria.",
      });
      return;
    }

    criarTransacao({
      usuarioId: usuario?.id ?? USUARIO_ID,
      contaId: contaPadrao?.id ?? null,
      extratoId: null,
      tipo,
      valor,
      dataTransacao: data.toISOString().slice(0, 10),
      descricaoUsuario: descricao || null,
      categoriaId,
      subcategoria: null,
      estabelecimento: null,
      origem: "manual",
      observacao: null,
    });
  };

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
     <div className="max-w-2xl mx-auto w-full">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-[20px] lg:text-[28px] font-extrabold tracking-tight text-foreground">Nova transação</h1>
          <p className="text-[11.5px] lg:text-[13px] text-muted-foreground mt-0.5">
            Registre uma receita ou gasto manual
          </p>
        </div>
        <Button variant="ghost" size="sm" onClick={resetar}>Cancelar</Button>
      </div>

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
            BRL · {contaPadrao?.banco ?? "—"} ({contaPadrao?.nome ?? "—"})
          </p>
        )}
      </div>

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

      <div className="mt-6 space-y-2">
        <Button
          className="w-full"
          size="lg"
          leftIcon={Save}
          loading={isPending}
          onClick={handleSubmit}
        >
          Salvar transação
        </Button>
        <Button variant="secondary" className="w-full" disabled={isPending}>
          Salvar como rascunho
        </Button>
      </div>
     </div>
    </div>
  );
};
