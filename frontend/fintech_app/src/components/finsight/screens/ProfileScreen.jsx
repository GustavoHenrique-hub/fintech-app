// ProfileScreen: dados da conta + preferências + segurança.
// Lê o usuario do mock; quando integrar, troque por useQuery(["usuario", id]).
//
// Inclui modal de confirmação de "excluir conta" (Modal do design system).
import { useState } from "react";
import {
  Mail, Phone, CreditCard, Bell, Globe, Languages, Lock, ShieldCheck,
  Crown, LogOut, Trash2, ChevronRight, Check, Pencil, AlertTriangle,
} from "lucide-react";

import { usuarioAtual, contas } from "@/mocks";
import { formatCPF, getInitials, maskEmail, maskCPF } from "@/lib/format";
import { Button } from "@/components/ui/button";
import {
  Modal, ModalTrigger, ModalContent, ModalHeader, ModalTitle,
  ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { toast } from "@/hooks/use-toast";

// ── Componentes auxiliares (só usados aqui) ─────────────────────────
// Linha clicável padrão (ícone + label + valor + chevron).
const Row = ({
  icon: Icon, iconColor = "text-foreground", iconBg = "bg-secondary",
  label, value, trailing, danger,
}) => (
  <button
    className={`w-full flex items-center gap-3 px-3.5 py-3 row-press text-left ${
      danger ? "text-destructive" : ""
    }`}
  >
    <div className={`w-8 h-8 rounded-lg ${iconBg} ${iconColor} flex items-center justify-center shrink-0`}>
      <Icon className="w-4 h-4" strokeWidth={2.25} />
    </div>
    <span className={`flex-1 text-[13px] font-semibold ${danger ? "text-destructive" : "text-foreground"}`}>
      {label}
    </span>
    {value && <span className="text-[12px] text-muted-foreground font-medium truncate max-w-[160px]">{value}</span>}
    {trailing ?? <ChevronRight className="w-4 h-4 text-muted-foreground" />}
  </button>
);

// Switch on/off controlado pelo pai.
const Toggle = ({ on, onChange, label }) => (
  <button
    onClick={(e) => {
      e.stopPropagation();
      onChange(!on);
    }}
    aria-pressed={on}
    aria-label={label}
    className={`w-9 h-5 rounded-full p-0.5 transition-colors ${on ? "bg-primary" : "bg-muted"}`}
  >
    <span
      className={`block w-4 h-4 rounded-full bg-card shadow-sm transition-transform ${on ? "translate-x-4" : ""}`}
    />
  </button>
);

// ── Tela principal ─────────────────────────────────────────────────
export const ProfileScreen = () => {
  const [notif, setNotif] = useState(true);
  const [twoFA, setTwoFA] = useState(true);
  const [showCPF, setShowCPF] = useState(false);
  const [deletando, setDeletando] = useState(false);

  const usuario = usuarioAtual;
  const iniciais = getInitials(usuario.nome);
  const contasAtivas = contas.filter((c) => c.ativa).length;

  const handleDelete = async () => {
    setDeletando(true);
    // Simulação. Na integração: await axios.delete(`/usuarios/${usuario.id}`)
    await new Promise((r) => setTimeout(r, 800));
    setDeletando(false);
    toast.error({
      title: "Conta marcada para exclusão",
      description: "Você receberá um e-mail confirmando em até 24h.",
    });
  };

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
     <div className="max-w-5xl mx-auto w-full space-y-5 lg:space-y-7">
      {/* Header */}
      <div>
        <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight">
          Perfil
        </h1>
        <p className="text-[12px] lg:text-[13px] text-muted-foreground mt-0.5">Conta e preferências</p>
      </div>

      {/* Card do usuário */}
      <section className="card-soft p-4 flex items-center gap-3">
        <div className="relative">
          <div className="w-14 h-14 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-[18px] font-extrabold text-primary-foreground shadow-md shadow-primary/30">
            {iniciais}
          </div>
          <button
            aria-label="Editar foto"
            className="absolute -bottom-0.5 -right-0.5 w-5 h-5 rounded-full bg-card border border-border flex items-center justify-center shadow-sm"
          >
            <Pencil className="w-2.5 h-2.5 text-foreground" strokeWidth={2.5} />
          </button>
        </div>
        <div className="flex-1 min-w-0">
          <p className="text-[15px] font-extrabold text-foreground leading-tight truncate">
            {usuario.nome}
          </p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5 truncate">
            {maskEmail(usuario.email)}
          </p>
          <span className="inline-flex items-center gap-1 mt-1.5 px-2 py-0.5 rounded-full bg-accent text-accent-foreground text-[10px] font-bold">
            <Crown className="w-2.5 h-2.5" strokeWidth={2.75} /> Premium Personal
          </span>
        </div>
      </section>

      {/* Conta + Preferências em 2 colunas no desktop. */}
      <div className="grid lg:grid-cols-2 gap-5 lg:gap-6">
      <section>
        <p className="section-label mb-1.5">Conta</p>
        <div className="card-soft divide-y divide-border">
          <Row
            icon={Mail}
            iconBg="bg-surface-purple"
            iconColor="text-primary"
            label="E-mail"
            value={maskEmail(usuario.email)}
          />
          <Row
            icon={Phone}
            iconBg="bg-surface-green"
            iconColor="text-success"
            label="Telefone"
            value={usuario.telefone}
          />
          <Row
            icon={CreditCard}
            iconBg="bg-surface-yellow"
            iconColor="text-foreground"
            label="CPF"
            value={showCPF ? formatCPF(usuario.cpf) : maskCPF(usuario.cpf)}
            trailing={
              <Toggle on={showCPF} onChange={setShowCPF} label="Mostrar CPF completo" />
            }
          />
          <Row
            icon={CreditCard}
            iconBg="bg-surface-purple"
            iconColor="text-primary"
            label="Contas vinculadas"
            value={`${contasAtivas} ${contasAtivas === 1 ? "ativa" : "ativas"}`}
          />
        </div>
      </section>

      {/* Preferências */}
      <section>
        <p className="section-label mb-1.5">Preferências</p>
        <div className="card-soft divide-y divide-border">
          <Row
            icon={Bell}
            iconBg="bg-surface-pink"
            iconColor="text-destructive"
            label="Notificações"
            trailing={<Toggle on={notif} onChange={setNotif} label="Notificações" />}
          />
          <Row icon={Globe}     iconBg="bg-surface-purple" iconColor="text-primary"    label="Moeda"  value="BRL (R$)" />
          <Row icon={Languages} iconBg="bg-surface-yellow" iconColor="text-foreground" label="Idioma" value="Português (BR)" />
        </div>
      </section>
      </div>

      {/* Segurança + Card upgrade em 2 colunas no desktop. */}
      <div className="grid lg:grid-cols-2 gap-5 lg:gap-6">
      {/* Segurança */}
      <section>
        <p className="section-label mb-1.5">Segurança</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={Lock} iconBg="bg-secondary" label="Alterar senha" />
          <Row
            icon={ShieldCheck}
            iconBg="bg-surface-green"
            iconColor="text-success"
            label="Autenticação em duas etapas"
            trailing={<Toggle on={twoFA} onChange={setTwoFA} label="2FA" />}
          />
        </div>
      </section>

      {/* Card de upgrade */}
      <section className="relative overflow-hidden card-soft p-4 bg-gradient-to-br from-accent/40 to-accent/10 border-accent/40">
        <div className="flex items-center gap-2">
          <Crown className="w-4 h-4 text-foreground" strokeWidth={2.5} />
          <p className="text-[13px] font-extrabold text-foreground">FinSight Premium</p>
        </div>
        <p className="text-[11.5px] text-muted-foreground mt-1">
          Análises avançadas, categorias ilimitadas e suporte prioritário.
        </p>
        <div className="grid grid-cols-2 gap-1.5 mt-3">
          {["Gráficos avançados", "Orçamentos ilimitados", "Insights por IA", "Suporte prioritário"].map((b) => (
            <div key={b} className="flex items-center gap-1 text-[11px] font-medium text-foreground">
              <Check className="w-3 h-3 text-success" strokeWidth={3} /> {b}
            </div>
          ))}
        </div>
        <Button className="w-full mt-3" size="sm">Fazer upgrade para Empresarial</Button>
      </section>
      </div>

      {/* Ações da conta */}
      <section>
        <p className="section-label mb-1.5">Conta</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={LogOut} iconBg="bg-secondary" label="Sair" />

          {/* Excluir conta: usa Modal do design system para confirmar. */}
          <Modal>
            <ModalTrigger asChild>
              <Row
                icon={Trash2}
                iconBg="bg-surface-pink"
                iconColor="text-destructive"
                label="Excluir conta"
                danger
              />
            </ModalTrigger>
            <ModalContent>
              <ModalHeader>
                <div className="flex items-start gap-3">
                  <div className="w-10 h-10 rounded-full bg-surface-pink text-destructive flex items-center justify-center shrink-0">
                    <AlertTriangle className="w-5 h-5" strokeWidth={2.5} />
                  </div>
                  <div>
                    <ModalTitle>Excluir conta permanentemente?</ModalTitle>
                    <ModalDescription>
                      Esta ação é irreversível. Seus extratos, transações e histórico
                      serão removidos após 24 horas.
                    </ModalDescription>
                  </div>
                </div>
              </ModalHeader>
              <ModalFooter>
                <ModalClose asChild>
                  <Button variant="secondary">Cancelar</Button>
                </ModalClose>
                <Button
                  variant="danger"
                  leftIcon={Trash2}
                  loading={deletando}
                  onClick={handleDelete}
                >
                  Confirmar exclusão
                </Button>
              </ModalFooter>
            </ModalContent>
          </Modal>
        </div>
        <p className="text-[10.5px] text-muted-foreground mt-2 px-1">
          FinSight v2.4.0 · Build 2026.04 · Usuário {usuario.usercode}
        </p>
      </section>
     </div>
    </div>
  );
};
