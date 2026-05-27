// Página de catálogo dos componentes do design system.
// Acesse em /components para ver e interagir com cada um.
//
// Não é uma "tela" do app — é só uma vitrine pra desenvolvimento e revisão
// (estilo Storybook, mas minimalista).
import { useState } from "react";
import {
  Save, Trash2, Inbox, ArrowLeft, AlertCircle, Plus,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { InputMonetario } from "@/components/ui/input-monetario";
import { InputCPF } from "@/components/ui/input-cpf";
import { Combobox } from "@/components/ui/combobox";
import {
  Modal, ModalTrigger, ModalContent, ModalHeader, ModalTitle, ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { StatusBadge } from "@/components/ui/status-badge";
import {
  Skeleton, SkeletonText, SkeletonCard, SkeletonChart, SkeletonRow,
} from "@/components/ui/skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { ConfidenceBar } from "@/components/ui/confidence-bar";
import { DatePicker } from "@/components/ui/date-picker";
import { DateRangePicker } from "@/components/ui/date-range-picker";
import { PasswordStrengthMeter } from "@/components/ui/password-strength-meter";
import { toast } from "@/hooks/use-toast";

// Pequeno helper visual pra separar cada seção da página.
const Section = ({ title, children }) => (
  <section className="space-y-3">
    <h2 className="text-[13px] uppercase tracking-[0.08em] font-bold text-muted-foreground">
      {title}
    </h2>
    <div className="card-soft p-4 space-y-4">{children}</div>
  </section>
);

const Row = ({ label, children }) => (
  <div className="grid grid-cols-1 sm:grid-cols-[140px_1fr] gap-2 items-start">
    <span className="text-[12px] font-semibold text-muted-foreground pt-2">{label}</span>
    <div className="min-w-0">{children}</div>
  </div>
);

// Categorias mockadas com hierarquia (pai/filho) — testam o Combobox.
const categoriasMock = [
  { id: "alim",     label: "Alimentação" },
  { id: "alim_rest", label: "Restaurantes", parentId: "alim" },
  { id: "alim_merc", label: "Mercado",      parentId: "alim" },
  { id: "alim_del",  label: "Delivery",     parentId: "alim" },
  { id: "trans",    label: "Transporte" },
  { id: "trans_app", label: "Apps de mobilidade", parentId: "trans" },
  { id: "trans_comb", label: "Combustível",       parentId: "trans" },
  { id: "lazer",    label: "Lazer" },
  { id: "saude",    label: "Saúde" },
  { id: "moradia",  label: "Moradia" },
];

export default function ComponentsShowcase() {
  const [valor, setValor] = useState(undefined);
  const [cpf, setCpf] = useState("");
  const [cpfValido, setCpfValido] = useState(false);
  const [categoria, setCategoria] = useState(undefined);
  const [data, setData] = useState(undefined);
  const [periodo, setPeriodo] = useState(undefined);
  const [senha, setSenha] = useState("");

  return (
    <main className="min-h-screen bg-background">
      <div className="max-w-3xl mx-auto px-4 py-8 space-y-8">
        {/* Header da página. */}
        <header className="flex items-center justify-between">
          <div>
            <p className="text-[12px] text-muted-foreground font-medium">FinSight Design System</p>
            <h1 className="text-[24px] font-extrabold tracking-tight text-foreground">
              Componentes
            </h1>
          </div>
          <Button variant="ghost" size="sm" leftIcon={ArrowLeft} onClick={() => (window.location.href = "/")}>
            Voltar
          </Button>
        </header>

        {/* ── Button ───────────────────────────────────────────────── */}
        <Section title="Button">
          <Row label="Variantes">
            <div className="flex flex-wrap gap-2">
              <Button variant="primary">Primary</Button>
              <Button variant="secondary">Secondary</Button>
              <Button variant="danger">Danger</Button>
              <Button variant="ghost">Ghost</Button>
            </div>
          </Row>
          <Row label="Tamanhos">
            <div className="flex flex-wrap items-center gap-2">
              <Button size="sm">Small</Button>
              <Button size="md">Medium</Button>
              <Button size="lg">Large</Button>
            </div>
          </Row>
          <Row label="Com ícones">
            <div className="flex flex-wrap gap-2">
              <Button leftIcon={Save}>Salvar</Button>
              <Button variant="danger" leftIcon={Trash2}>Excluir</Button>
              <Button variant="secondary" rightIcon={Plus}>Adicionar</Button>
            </div>
          </Row>
          <Row label="Estados">
            <div className="flex flex-wrap gap-2">
              <Button loading>Salvando</Button>
              <Button disabled>Desabilitado</Button>
            </div>
          </Row>
        </Section>

        {/* ── Inputs com máscara ───────────────────────────────────── */}
        <Section title="InputMonetario (R$)">
          <Row label="Valor controlado">
            <InputMonetario value={valor} onChange={setValor} />
          </Row>
          <Row label="Valor capturado">
            <code className="text-[12px] text-muted-foreground">
              {valor === undefined ? "undefined" : String(valor)}
            </code>
          </Row>
        </Section>

        <Section title="InputCPF">
          <Row label="Digite seu CPF">
            <InputCPF value={cpf} onChange={setCpf} onValid={setCpfValido} />
          </Row>
          <Row label="Status">
            <code className="text-[12px] text-muted-foreground">
              dígitos: "{cpf}" · válido: {String(cpfValido)}
            </code>
          </Row>
        </Section>

        {/* ── Combobox ─────────────────────────────────────────────── */}
        <Section title="Combobox (categorias hierárquicas)">
          <Row label="Categoria">
            <Combobox
              items={categoriasMock}
              value={categoria}
              onChange={setCategoria}
              placeholder="Selecione uma categoria"
            />
          </Row>
          <Row label="Selecionado">
            <code className="text-[12px] text-muted-foreground">{categoria ?? "—"}</code>
          </Row>
        </Section>

        {/* ── Modal ────────────────────────────────────────────────── */}
        <Section title="Modal">
          <Row label="Trigger">
            <Modal>
              <ModalTrigger asChild>
                <Button variant="secondary">Abrir modal</Button>
              </ModalTrigger>
              <ModalContent>
                <ModalHeader>
                  <ModalTitle>Confirmar exclusão</ModalTitle>
                  <ModalDescription>
                    Esta ação não pode ser desfeita. A transação será removida permanentemente.
                  </ModalDescription>
                </ModalHeader>
                <ModalFooter>
                  <ModalClose asChild>
                    <Button variant="secondary">Cancelar</Button>
                  </ModalClose>
                  <Button variant="danger" leftIcon={Trash2}>Excluir</Button>
                </ModalFooter>
              </ModalContent>
            </Modal>
          </Row>
        </Section>

        {/* ── Toast ────────────────────────────────────────────────── */}
        <Section title="Toast (canto inferior-direito, 5s autodismiss)">
          <Row label="Variantes">
            <div className="flex flex-wrap gap-2">
              <Button size="sm" variant="secondary" onClick={() => toast.success({ title: "Transação salva", description: "Os dados foram registrados com sucesso." })}>
                Success
              </Button>
              <Button size="sm" variant="secondary" onClick={() => toast.error({ title: "Falha ao salvar", description: "Verifique sua conexão e tente novamente." })}>
                Error
              </Button>
              <Button size="sm" variant="secondary" onClick={() => toast.warning({ title: "Atenção", description: "Categoria não definida." })}>
                Warning
              </Button>
              <Button size="sm" variant="secondary" onClick={() => toast.info({ title: "Sincronizando…", description: "Estamos atualizando seus extratos." })}>
                Info
              </Button>
            </div>
          </Row>
          <Row label="Com ação inline">
            <Button
              size="sm"
              variant="secondary"
              onClick={() =>
                toast.success({
                  title: "Transação removida",
                  action: (
                    <Button size="sm" variant="ghost" onClick={() => toast.info({ title: "Restaurada!" })}>
                      Desfazer
                    </Button>
                  ),
                })
              }
            >
              Toast com "Desfazer"
            </Button>
          </Row>
        </Section>

        {/* ── StatusBadge ──────────────────────────────────────────── */}
        <Section title="StatusBadge (StatusExtrato / StatusRevisaoTransacao)">
          <Row label="Extrato">
            <div className="flex flex-wrap gap-2">
              {["upload_recebido", "validando", "pendente_revisao", "concluido", "erro_formato", "cancelado"].map((v) => (
                <StatusBadge key={v} kind="extrato" value={v} />
              ))}
            </div>
          </Row>
          <Row label="Revisão">
            <div className="flex flex-wrap gap-2">
              {["EXTRAIDA", "CLASSIFICADA", "PENDENTE_REVISAO", "CONFIRMADA", "IGNORADA", "ARQUIVADA"].map((v) => (
                <StatusBadge key={v} kind="revisao" value={v} />
              ))}
            </div>
          </Row>
        </Section>

        {/* ── Skeleton ─────────────────────────────────────────────── */}
        <Section title="Skeleton (loading states)">
          <Row label="Skeleton base"><Skeleton className="h-4 w-48" /></Row>
          <Row label="SkeletonText"><SkeletonText lines={3} /></Row>
          <Row label="SkeletonCard"><SkeletonCard /></Row>
          <Row label="SkeletonChart"><SkeletonChart /></Row>
          <Row label="SkeletonRow">
            <div className="card-soft divide-y divide-border">
              <SkeletonRow />
              <SkeletonRow />
            </div>
          </Row>
        </Section>

        {/* ── EmptyState ───────────────────────────────────────────── */}
        <Section title="EmptyState">
          <Row label="Default">
            <EmptyState
              icon={Inbox}
              title="Nenhuma transação ainda"
              description="Importe um extrato ou crie uma transação manualmente."
              action={<Button leftIcon={Plus}>Importar extrato</Button>}
            />
          </Row>
          <Row label="Danger (erro)">
            <EmptyState
              icon={AlertCircle}
              tone="danger"
              title="Falha ao carregar"
              description="Não conseguimos buscar as transações. Tente novamente."
              action={<Button variant="secondary">Tentar novamente</Button>}
            />
          </Row>
        </Section>

        {/* ── ConfidenceBar ────────────────────────────────────────── */}
        <Section title="ConfidenceBar">
          <Row label="Alta (87%)"><ConfidenceBar value={87} label="Confiança IA" /></Row>
          <Row label="Média (62%)"><ConfidenceBar value={62} label="Confiança IA" /></Row>
          <Row label="Baixa (34%)"><ConfidenceBar value={34} label="Confiança IA" /></Row>
        </Section>

        {/* ── Date pickers ─────────────────────────────────────────── */}
        <Section title="DatePicker (pt-BR)">
          <Row label="Data única">
            <DatePicker value={data} onChange={setData} />
          </Row>
        </Section>

        <Section title="DateRangePicker (máx 6 meses)">
          <Row label="Período">
            <DateRangePicker value={periodo} onChange={setPeriodo} />
          </Row>
          <Row label="Selecionado">
            <code className="text-[12px] text-muted-foreground">
              {periodo?.from ? periodo.from.toLocaleDateString("pt-BR") : "—"} →{" "}
              {periodo?.to ? periodo.to.toLocaleDateString("pt-BR") : "—"}
            </code>
          </Row>
        </Section>

        {/* ── PasswordStrengthMeter ────────────────────────────────── */}
        <Section title="PasswordStrengthMeter">
          <Row label="Senha">
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              placeholder="Digite uma senha"
              className="w-full h-10 px-3 rounded-xl bg-card border border-border text-[14px] outline-none focus:ring-2 focus:ring-primary/30"
            />
          </Row>
          <Row label="Força">
            <PasswordStrengthMeter value={senha} />
          </Row>
        </Section>
      </div>
    </main>
  );
}
