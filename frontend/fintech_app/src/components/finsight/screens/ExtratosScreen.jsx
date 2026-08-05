import { useRef, useState } from "react";
import { UploadCloud, FileText, Inbox } from "lucide-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { useAuth } from "@/context/AuthContext";
import { useContaSelecionada } from "@/context/ContaSelecionadaContext";
import { useExtratos } from "@/hooks/use-extratos";
import { extratoService } from "@/services";
import { formatDataRelativa } from "@/lib/format";
import { Button } from "@/components/ui/button";
import { StatusBadge } from "@/components/ui/status-badge";
import { EmptyState } from "@/components/ui/empty-state";
import { SkeletonRow } from "@/components/ui/skeleton";
import { toast } from "@/hooks/use-toast";

const EXTENSOES_ACEITAS = ["pdf", "csv", "txt", "xls", "xlsx"];
const TAMANHO_MAXIMO_BYTES = 10 * 1024 * 1024;

function extensao(nomeArquivo) {
  return nomeArquivo?.split(".").pop()?.toLowerCase() ?? "";
}

export const ExtratosScreen = () => {
  const [arrastando, setArrastando] = useState(false);
  const inputRef = useRef(null);

  const queryClient = useQueryClient();
  const { user } = useAuth();
  const { contaAtual, loadingContas } = useContaSelecionada();
  const { data: extratos = [], isLoading: loadingExtratos } = useExtratos();

  const { mutate: enviarArquivo, isPending } = useMutation({
    mutationFn: (arquivo) => extratoService.upload(user?.idUsuario, contaAtual?.id, arquivo),
    onSuccess: (extrato) => {
      queryClient.invalidateQueries({ queryKey: ["extratos", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["transacoes", user?.idUsuario] });
      queryClient.invalidateQueries({ queryKey: ["contas", user?.idUsuario] });
      toast.success({
        title: "Extrato importado",
        description: `${extrato.totalLancamentos} lançamento(s) aguardando revisão em Transações.`,
      });
    },
    onError: (error) => {
      toast.error({
        title: "Erro ao importar extrato",
        description: error?.response?.data?.error ?? "Não foi possível processar o arquivo.",
      });
    },
  });

  const validarEEnviar = (arquivo) => {
    if (!arquivo) return;
    if (!EXTENSOES_ACEITAS.includes(extensao(arquivo.name))) {
      toast.warning({
        title: "Formato não suportado",
        description: "Envie um arquivo .pdf, .csv, .txt, .xls ou .xlsx.",
      });
      return;
    }
    if (arquivo.size > TAMANHO_MAXIMO_BYTES) {
      toast.warning({ title: "Arquivo muito grande", description: "O tamanho máximo é 10MB." });
      return;
    }
    if (!contaAtual?.id) {
      toast.warning({
        title: "Selecione uma conta",
        description: "Escolha uma conta na Visão geral antes de importar o extrato.",
      });
      return;
    }
    enviarArquivo(arquivo);
  };

  const onDrop = (e) => {
    e.preventDefault();
    setArrastando(false);
    validarEEnviar(e.dataTransfer.files?.[0]);
  };

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
      <div className="max-w-3xl mx-auto w-full">
        <h1 className="text-[20px] lg:text-[28px] font-extrabold tracking-tight text-foreground">Extratos</h1>
        <p className="text-[11.5px] lg:text-[13px] text-muted-foreground mt-0.5">
          Importe extratos em PDF, CSV, TXT, XLS ou XLSX — os lançamentos entram como pendentes de revisão.
        </p>

        {/* Dropzone */}
        <div
          onDragOver={(e) => { e.preventDefault(); setArrastando(true); }}
          onDragLeave={() => setArrastando(false)}
          onDrop={onDrop}
          onClick={() => !isPending && inputRef.current?.click()}
          role="button"
          tabIndex={0}
          aria-label="Importar extrato — clique ou arraste um arquivo"
          className={`mt-4 flex flex-col items-center justify-center gap-2 rounded-2xl border-2 border-dashed px-6 py-10 text-center transition-colors cursor-pointer ${
            arrastando
              ? "border-primary bg-primary/5"
              : "border-border hover:border-primary/40 hover:bg-secondary/50"
          } ${isPending ? "opacity-60 pointer-events-none" : ""}`}
        >
          <div className="w-12 h-12 rounded-2xl bg-surface-purple text-primary flex items-center justify-center">
            <UploadCloud className="w-6 h-6" strokeWidth={2} />
          </div>
          <p className="text-[13.5px] font-semibold text-foreground">
            {isPending ? "Enviando e processando..." : "Arraste um extrato aqui ou clique para escolher"}
          </p>
          <p className="text-[11.5px] text-muted-foreground">
            PDF, CSV, TXT, XLS ou XLSX · até 10MB
            {contaAtual?.banco && !loadingContas && <> · conta {contaAtual.banco}</>}
          </p>
          <Button size="sm" loading={isPending} onClick={(e) => { e.stopPropagation(); inputRef.current?.click(); }}>
            Escolher arquivo
          </Button>
          <input
            ref={inputRef}
            type="file"
            accept=".pdf,.csv,.txt,.xls,.xlsx"
            className="hidden"
            onChange={(e) => {
              validarEEnviar(e.target.files?.[0]);
              e.target.value = "";
            }}
          />
        </div>

        {/* Lista de extratos já importados */}
        <div className="mt-6">
          <p className="section-label mb-1.5">Importados</p>
          {loadingExtratos ? (
            <div className="card-soft divide-y divide-border">
              {[0, 1, 2].map((i) => <SkeletonRow key={i} />)}
            </div>
          ) : extratos.length === 0 ? (
            <EmptyState
              icon={Inbox}
              title="Nenhum extrato importado ainda"
              description="Arraste um arquivo acima para começar."
            />
          ) : (
            <div className="card-soft divide-y divide-border">
              {extratos.map((e) => (
                <div key={e.id} className="flex items-center gap-3 px-3.5 py-3">
                  <div className="w-9 h-9 rounded-full bg-secondary flex items-center justify-center shrink-0">
                    <FileText className="w-4 h-4 text-muted-foreground" strokeWidth={2} />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-[13px] font-semibold text-foreground truncate">{e.arquivoNome}</p>
                    <p className="text-[11px] text-muted-foreground mt-0.5">
                      {e.totalLancamentos} lançamento(s)
                      {e.lancamentosPendentes > 0 && ` · ${e.lancamentosPendentes} pendente(s)`}
                      {" · "}{formatDataRelativa(e.criadoEm)}
                    </p>
                  </div>
                  <StatusBadge kind="extrato" value={e.status} />
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
