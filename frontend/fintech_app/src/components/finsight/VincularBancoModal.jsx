import { useState } from "react";
import { Search, CheckCircle2, X } from "lucide-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  Modal, ModalContent, ModalHeader, ModalTitle, ModalDescription,
  ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { Button } from "@/components/ui/button";
import { InputMonetario } from "@/components/ui/input-monetario";
import { bancoService, contaFinanceiraService } from "@/services";
import { useAuth } from "@/context/AuthContext";
import { getBancoLogoUrl, getBancoColor } from "@/lib/banco-utils";
import { toast } from "@/hooks/use-toast";

const TIPOS_CONTA = [
  { value: "corrente",     label: "Corrente" },
  { value: "poupanca",     label: "Poupança" },
  { value: "cartao",       label: "Cartão" },
  { value: "investimento", label: "Investimento" },
  { value: "dinheiro",     label: "Dinheiro" },
];

function BancoLogo({ nome, size = 32 }) {
  const [erro, setErro] = useState(false);
  const url = getBancoLogoUrl(nome);
  const cor = getBancoColor(nome);

  if (!url || erro) {
    return (
      <div
        className="rounded-xl flex items-center justify-center text-white font-extrabold shrink-0"
        style={{ width: size, height: size, backgroundColor: cor, fontSize: size * 0.38 }}
      >
        {(nome ?? "B")[0].toUpperCase()}
      </div>
    );
  }
  return (
    <img
      src={url} alt={nome} onError={() => setErro(true)}
      className="rounded-xl object-contain bg-white p-1 shrink-0"
      style={{ width: size, height: size }}
    />
  );
}

export function VincularBancoModal({ open, onOpenChange }) {
  const { user } = useAuth();
  const queryClient = useQueryClient();

  const [busca, setBusca]                       = useState("");
  const [bancoSelecionado, setBancoSelecionado] = useState(null);
  const [tipo, setTipo]                         = useState("corrente");
  const [nome, setNome]                         = useState("");
  const [saldoInicial, setSaldoInicial]         = useState(undefined);

  const reset = () => {
    setBusca("");
    setBancoSelecionado(null);
    setTipo("corrente");
    setNome("");
    setSaldoInicial(undefined);
  };

  const { data: bancos = [], isLoading } = useQuery({
    queryKey: ["bancos"],
    queryFn: () => bancoService.listar(),
    enabled: open,
    staleTime: Infinity,
  });

  const bancosFiltrados = bancos.filter((b) =>
    !busca.trim() || b.nome?.toLowerCase().includes(busca.trim().toLowerCase()),
  );

  const { mutate: criar, isPending } = useMutation({
    mutationFn: (dto) => contaFinanceiraService.criar(dto),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["contas", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["snapshots", user?.idUsuario] });
      toast.success({
        title: "Banco vinculado",
        description: `${bancoSelecionado?.nome} foi adicionado com sucesso.`,
      });
      reset();
      onOpenChange(false);
    },
    onError: (err) => {
      toast.error({
        title: "Erro ao vincular banco",
        description: err?.response?.data?.message ?? "Tente novamente.",
      });
    },
  });

  const handleSubmit = () => {
    if (!bancoSelecionado) return;
    criar({
      usuarioId:    user?.idUsuario,
      bancoId:      bancoSelecionado.id,
      nome:         nome.trim() || `${bancoSelecionado.nome} – ${TIPOS_CONTA.find(t => t.value === tipo)?.label}`,
      tipo,
      saldoInicial: saldoInicial ?? 0,
      padrao:       false,
      ativa:        true,
    });
  };

  const tipoLabel = TIPOS_CONTA.find((t) => t.value === tipo)?.label ?? tipo;

  return (
    <Modal open={open} onOpenChange={(v) => { if (!v) reset(); onOpenChange(v); }}>
      <ModalContent className="max-w-[480px]">
        <ModalHeader>
          <ModalTitle>Vincular banco</ModalTitle>
          <ModalDescription>
            Selecione sua instituição financeira e o tipo de conta.
          </ModalDescription>
        </ModalHeader>

        <div className="px-1 space-y-4">

          {/* Busca */}
          <div className="relative">
            <Search className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2" strokeWidth={2.25} />
            <input
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
              placeholder="Buscar banco..."
              className="w-full h-10 pl-8 pr-8 rounded-xl bg-secondary border border-transparent text-[13.5px] outline-none placeholder:text-muted-foreground/60 focus:ring-2 focus:ring-primary/25 focus:border-primary/30 transition-all"
            />
            {busca && (
              <button onClick={() => setBusca("")} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground">
                <X className="w-3.5 h-3.5" strokeWidth={2.25} />
              </button>
            )}
          </div>

          {/* Lista de bancos */}
          <div className="max-h-[220px] overflow-y-auto rounded-2xl border border-border divide-y divide-border bg-card">
            {isLoading ? (
              <div className="py-8 text-center text-[13px] text-muted-foreground">
                Carregando bancos...
              </div>
            ) : bancosFiltrados.length === 0 ? (
              <div className="py-8 text-center text-[13px] text-muted-foreground">
                Nenhum banco encontrado
              </div>
            ) : bancosFiltrados.map((b) => {
              const ativo = bancoSelecionado?.id === b.id;
              return (
                <button
                  key={b.id}
                  onClick={() => setBancoSelecionado(ativo ? null : b)}
                  className={`w-full flex items-center gap-3 px-3.5 py-3 text-left transition-colors ${
                    ativo
                      ? "bg-primary/8 border-l-[3px] border-primary"
                      : "hover:bg-secondary border-l-[3px] border-transparent"
                  }`}
                >
                  <BancoLogo nome={b.nome} size={36} />
                  <div className="flex-1 min-w-0">
                    <p className="text-[13.5px] font-semibold text-foreground leading-tight">{b.nome}</p>
                    {b.ispb && (
                      <p className="text-[10.5px] text-muted-foreground">ISPB {b.ispb}</p>
                    )}
                  </div>
                  {ativo && <CheckCircle2 className="w-5 h-5 text-primary shrink-0" strokeWidth={2.25} />}
                </button>
              );
            })}
          </div>

          {/* Detalhes da conta — aparece após selecionar banco */}
          {bancoSelecionado && (
            <div className="space-y-3 pt-1 animate-in fade-in slide-in-from-bottom-2 duration-200">
              {/* Tipo de conta */}
              <div>
                <p className="section-label mb-2">Tipo de conta</p>
                <div className="grid grid-cols-3 gap-1.5 sm:grid-cols-5">
                  {TIPOS_CONTA.map((t) => (
                    <button
                      key={t.value}
                      onClick={() => setTipo(t.value)}
                      className={`h-9 px-2 rounded-xl text-[11.5px] font-semibold border transition-all ${
                        tipo === t.value
                          ? "bg-primary text-primary-foreground border-primary"
                          : "bg-card border-border text-foreground hover:bg-secondary"
                      }`}
                    >
                      {t.label}
                    </button>
                  ))}
                </div>
              </div>

              {/* Nome personalizado */}
              <div>
                <p className="section-label">Nome da conta (opcional)</p>
                <input
                  value={nome}
                  onChange={(e) => setNome(e.target.value)}
                  placeholder={`${bancoSelecionado.nome} – ${tipoLabel}`}
                  className="mt-1.5 w-full h-10 px-3.5 rounded-xl bg-card border border-border text-[13.5px] outline-none placeholder:text-muted-foreground/55 focus:ring-2 focus:ring-primary/25 focus:border-primary/40 transition-all"
                />
              </div>

              {/* Saldo inicial */}
              <div>
                <p className="section-label">Saldo inicial (opcional)</p>
                <div className="mt-1.5">
                  <InputMonetario value={saldoInicial} onChange={setSaldoInicial} placeholder="R$ 0,00" />
                </div>
              </div>
            </div>
          )}
        </div>

        <ModalFooter>
          <ModalClose asChild>
            <Button variant="secondary" disabled={isPending}>Cancelar</Button>
          </ModalClose>
          <Button
            disabled={!bancoSelecionado}
            loading={isPending}
            onClick={handleSubmit}
          >
            {bancoSelecionado ? `Vincular ${bancoSelecionado.nome}` : "Selecione um banco"}
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
