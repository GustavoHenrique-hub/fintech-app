import { useState } from "react";
import {
  Mail, Phone, CreditCard, Bell, Languages, Lock, ShieldCheck,
  LogOut, Trash2, ChevronRight, Pencil, AlertTriangle,
} from "lucide-react";

import { useUsuario } from "@/hooks/use-usuario";
import { useContas } from "@/hooks/use-contas";
import { useAuth } from "@/context/AuthContext";
import { formatCPF, getInitials, maskEmail, maskCPF } from "@/lib/format";
import { Button } from "@/components/ui/button";
import {
  Modal, ModalTrigger, ModalContent, ModalHeader, ModalTitle,
  ModalDescription, ModalFooter, ModalClose,
} from "@/components/ui/modal";
import { toast } from "@/hooks/use-toast";
import { SkeletonCard } from "@/components/ui/skeleton";
import { EditarContatoModal } from "@/components/finsight/EditarContatoModal";
import { AlterarSenhaModal } from "@/components/finsight/AlterarSenhaModal";
import { ContasVinculadasModal } from "@/components/finsight/ContasVinculadasModal";
import { IdiomaModal } from "@/components/finsight/IdiomaModal";
import { IDIOMAS } from "@/lib/idiomas";

const Row = ({
  icon: Icon, iconColor = "text-foreground", iconBg = "bg-secondary",
  label, value, trailing, danger, onClick, disabled,
}) => {
  const inner = (
    <>
      <div className={`w-8 h-8 rounded-lg ${iconBg} ${iconColor} flex items-center justify-center shrink-0`}>
        <Icon className="w-4 h-4" strokeWidth={2.25} />
      </div>
      <span className={`flex-1 text-[13px] font-semibold ${danger ? "text-destructive" : "text-foreground"}`}>
        {label}
      </span>
      {value && <span className="text-[12px] text-muted-foreground font-medium truncate max-w-[160px]">{value}</span>}
      {trailing ?? (disabled ? null : <ChevronRight className="w-4 h-4 text-muted-foreground" />)}
    </>
  );

  if (disabled) {
    return (
      <div
        aria-disabled="true"
        className="w-full flex items-center gap-3 px-3.5 py-3 cursor-default select-none"
      >
        {inner}
      </div>
    );
  }

  return (
    <button
      onClick={onClick}
      className={`w-full flex items-center gap-3 px-3.5 py-3 row-press text-left ${
        danger ? "text-destructive" : ""
      }`}
    >
      {inner}
    </button>
  );
};

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

export const ProfileScreen = () => {
  const [notif, setNotif] = useState(true);
  const [twoFA, setTwoFA] = useState(true);
  const [showCPF, setShowCPF] = useState(false);
  const [deletando, setDeletando] = useState(false);
  const [idioma, setIdioma] = useState("pt-BR");

  // Modais de edição — um por campo, controlados via estado local.
  const [campoContatoAberto, setCampoContatoAberto] = useState(null); // "email" | "telefone" | null
  const [senhaModalAberto, setSenhaModalAberto] = useState(false);
  const [contasModalAberto, setContasModalAberto] = useState(false);
  const [idiomaModalAberto, setIdiomaModalAberto] = useState(false);

  const { logout } = useAuth();
  const { data: usuario, isLoading: loadingUsuario } = useUsuario();
  const { data: contas = [], isLoading: loadingContas } = useContas();

  const isLoading = loadingUsuario || loadingContas;

  const iniciais = getInitials(usuario?.nome ?? "");
  const contasAtivas = contas.filter((c) => c.ativa).length;
  const idiomaLabel = IDIOMAS.find((i) => i.value === idioma)?.label ?? "Português (Brasil)";

  const handleDelete = async () => {
    setDeletando(true);
    await new Promise((r) => setTimeout(r, 800));
    setDeletando(false);
    toast.error({
      title: "Conta marcada para exclusão",
      description: "Você receberá um e-mail confirmando em até 24h.",
    });
  };

  if (isLoading) {
    return (
      <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
        <div className="max-w-5xl mx-auto w-full space-y-5">
          <SkeletonCard />
          <SkeletonCard />
          <SkeletonCard />
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 min-h-0 overflow-y-auto px-4 sm:px-5 lg:px-8 pt-4 lg:pt-8 pb-6 lg:pb-10 no-scrollbar">
     <div className="max-w-5xl mx-auto w-full space-y-5 lg:space-y-7">
      <div>
        <h1 className="text-[22px] lg:text-[28px] font-extrabold tracking-tight text-foreground leading-tight">
          Perfil
        </h1>
        <p className="text-[12px] lg:text-[13px] text-muted-foreground mt-0.5">Conta e preferências</p>
      </div>

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
            {usuario?.nome ?? "—"}
          </p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5 truncate">
            {maskEmail(usuario?.email ?? "")}
          </p>
        </div>
      </section>

      <div className="grid lg:grid-cols-2 gap-5 lg:gap-6">
      <section>
        <p className="section-label mb-1.5">Conta</p>
        <div className="card-soft divide-y divide-border">
          <Row
            icon={Mail}
            iconBg="bg-surface-purple"
            iconColor="text-primary"
            label="E-mail"
            value={maskEmail(usuario?.email ?? "")}
            onClick={() => setCampoContatoAberto("email")}
          />
          <Row
            icon={Phone}
            iconBg="bg-surface-green"
            iconColor="text-success"
            label="Telefone"
            value={usuario?.telefone ?? "—"}
            onClick={() => setCampoContatoAberto("telefone")}
          />
          <Row
            icon={CreditCard}
            iconBg="bg-surface-yellow"
            iconColor="text-foreground"
            label="CPF"
            value={showCPF ? formatCPF(usuario?.cpf ?? "") : maskCPF(usuario?.cpf ?? "")}
            disabled
            trailing={
              <div className="flex items-center gap-2">
                <Lock className="w-3 h-3 text-muted-foreground" strokeWidth={2.25} aria-hidden="true" />
                <Toggle on={showCPF} onChange={setShowCPF} label="Mostrar CPF completo" />
              </div>
            }
          />
          <Row
            icon={CreditCard}
            iconBg="bg-surface-purple"
            iconColor="text-primary"
            label="Contas vinculadas"
            value={`${contasAtivas} ${contasAtivas === 1 ? "ativa" : "ativas"}`}
            onClick={() => setContasModalAberto(true)}
          />
        </div>
        <p className="text-[10.5px] text-muted-foreground mt-1.5 px-1">
          CPF é dado sensível: alteração só via ofício, diretamente no banco de dados.
        </p>
      </section>

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
          <Row
            icon={Languages}
            iconBg="bg-surface-yellow"
            iconColor="text-foreground"
            label="Idioma"
            value={idiomaLabel}
            onClick={() => setIdiomaModalAberto(true)}
          />
        </div>
      </section>
      </div>

      <section>
        <p className="section-label mb-1.5">Segurança</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={Lock} iconBg="bg-secondary" label="Alterar senha" onClick={() => setSenhaModalAberto(true)} />
          <Row
            icon={ShieldCheck}
            iconBg="bg-surface-green"
            iconColor="text-success"
            label="Autenticação em duas etapas"
            trailing={<Toggle on={twoFA} onChange={setTwoFA} label="2FA" />}
          />
        </div>
      </section>

      <section>
        <p className="section-label mb-1.5">Conta</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={LogOut} iconBg="bg-secondary" label="Sair" onClick={logout} />

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
          FinSight v2.4.0 · Build 2026.04 · Usuário {usuario?.usercode ?? "—"}
        </p>
      </section>
     </div>

      <EditarContatoModal
        open={!!campoContatoAberto}
        onOpenChange={(v) => setCampoContatoAberto(v ? campoContatoAberto : null)}
        campo={campoContatoAberto ?? "email"}
        usuarioId={usuario?.id}
        valorAtual={campoContatoAberto === "telefone" ? usuario?.telefone : usuario?.email}
      />
      <AlterarSenhaModal
        open={senhaModalAberto}
        onOpenChange={setSenhaModalAberto}
        usuarioId={usuario?.id}
      />
      <ContasVinculadasModal
        open={contasModalAberto}
        onOpenChange={setContasModalAberto}
      />
      <IdiomaModal
        open={idiomaModalAberto}
        onOpenChange={setIdiomaModalAberto}
        value={idioma}
        onChange={setIdioma}
      />
    </div>
  );
};
