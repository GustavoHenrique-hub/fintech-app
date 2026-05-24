// ProfileScreen: configurações e dados da conta do usuário.
// Para reduzir repetição visual, definimos dois componentes locais:
//   - <Row>: linha clicável padrão (ícone + label + valor opcional + chevron)
//   - <Toggle>: switch on/off controlado
import { useState } from "react";
import {
  Mail, Phone, CreditCard, Bell, Globe, Languages, Lock, ShieldCheck,
  Crown, LogOut, Trash2, ChevronRight, Check, Pencil,
} from "lucide-react";

// Linha reutilizada em todas as listas da tela.
// `trailing` permite injetar qualquer elemento à direita (ex.: um Toggle).
const Row = ({
  icon: Icon,
  iconColor = "text-foreground",
  iconBg = "bg-secondary",
  label,
  value,
  trailing,
  danger,
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
    {value && <span className="text-[12px] text-muted-foreground font-medium">{value}</span>}
    {/* `trailing ?? <Chevron>` mostra o chevron por padrão e o substitui se o caller passou outro elemento. */}
    {trailing ?? <ChevronRight className="w-4 h-4 text-muted-foreground" />}
  </button>
);

// Switch genérico controlado pelo pai via { on, onChange }.
const Toggle = ({ on, onChange }) => (
  <button
    // stopPropagation evita disparar o onClick da <Row> ao redor.
    onClick={(e) => {
      e.stopPropagation();
      onChange(!on);
    }}
    className={`w-9 h-5 rounded-full p-0.5 transition-colors ${on ? "bg-primary" : "bg-muted"}`}
  >
    <span
      className={`block w-4 h-4 rounded-full bg-card shadow-sm transition-transform ${
        on ? "translate-x-4" : ""
      }`}
    />
  </button>
);

export const ProfileScreen = () => {
  // Estados locais dos switches (ligado/desligado).
  const [notif, setNotif] = useState(true);
  const [twoFA, setTwoFA] = useState(true);

  return (
    <div className="flex-1 overflow-y-auto px-4 pt-4 pb-6 space-y-5 no-scrollbar">
      {/* ── Header ──────────────────────────────────────────────────── */}
      <div>
        <h1 className="text-[22px] font-extrabold tracking-tight text-foreground leading-tight">
          Profile
        </h1>
        <p className="text-[12px] text-muted-foreground mt-0.5">Account & preferences</p>
      </div>

      {/* ── Card do usuário ─────────────────────────────────────────── */}
      <section className="card-soft p-4 flex items-center gap-3">
        <div className="relative">
          {/* Avatar gradiente. */}
          <div className="w-14 h-14 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-[18px] font-extrabold text-primary-foreground shadow-md shadow-primary/30">
            AM
          </div>
          {/* Botão "lápis" para editar foto. */}
          <button className="absolute -bottom-0.5 -right-0.5 w-5 h-5 rounded-full bg-card border border-border flex items-center justify-center shadow-sm">
            <Pencil className="w-2.5 h-2.5 text-foreground" strokeWidth={2.5} />
          </button>
        </div>
        <div className="flex-1 min-w-0">
          <p className="text-[15px] font-extrabold text-foreground leading-tight truncate">
            Alex Morgan
          </p>
          <p className="text-[11.5px] text-muted-foreground mt-0.5 truncate">
            alex.morgan@finsight.app
          </p>
          {/* Badge do plano. */}
          <span className="inline-flex items-center gap-1 mt-1.5 px-2 py-0.5 rounded-full bg-accent text-accent-foreground text-[10px] font-bold">
            <Crown className="w-2.5 h-2.5" strokeWidth={2.75} /> Premium Personal
          </span>
        </div>
      </section>

      {/* ── Account ─────────────────────────────────────────────────── */}
      <section>
        <p className="section-label mb-1.5">Account</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={Mail}       iconBg="bg-surface-purple" iconColor="text-primary"    label="Email"               value="alex.morgan@..." />
          <Row icon={Phone}      iconBg="bg-surface-green"  iconColor="text-success"    label="Phone"               value="+1 555 0142" />
          <Row icon={CreditCard} iconBg="bg-surface-yellow" iconColor="text-foreground" label="Connected accounts"  value="3 linked" />
        </div>
      </section>

      {/* ── Preferences (inclui Toggle de notificações) ─────────────── */}
      <section>
        <p className="section-label mb-1.5">Preferences</p>
        <div className="card-soft divide-y divide-border">
          <Row
            icon={Bell}
            iconBg="bg-surface-pink"
            iconColor="text-destructive"
            label="Notifications"
            trailing={<Toggle on={notif} onChange={setNotif} />}
          />
          <Row icon={Globe}     iconBg="bg-surface-purple" iconColor="text-primary"    label="Currency" value="USD" />
          <Row icon={Languages} iconBg="bg-surface-yellow" iconColor="text-foreground" label="Language" value="English" />
        </div>
      </section>

      {/* ── Security (inclui 2FA) ───────────────────────────────────── */}
      <section>
        <p className="section-label mb-1.5">Security</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={Lock} iconBg="bg-secondary" label="Change password" />
          <Row
            icon={ShieldCheck}
            iconBg="bg-surface-green"
            iconColor="text-success"
            label="Two-factor authentication"
            trailing={<Toggle on={twoFA} onChange={setTwoFA} />}
          />
        </div>
      </section>

      {/* ── Card de upgrade (cross-sell) ────────────────────────────── */}
      <section className="relative overflow-hidden card-soft p-4 bg-gradient-to-br from-accent/40 to-accent/10 border-accent/40">
        <div className="flex items-center gap-2">
          <Crown className="w-4 h-4 text-foreground" strokeWidth={2.5} />
          <p className="text-[13px] font-extrabold text-foreground">Premium Personal</p>
        </div>
        <p className="text-[11.5px] text-muted-foreground mt-1">
          Unlock advanced analytics, unlimited categories, and priority support.
        </p>
        {/* Lista de benefícios em grid 2 colunas. */}
        <div className="grid grid-cols-2 gap-1.5 mt-3">
          {["Advanced charts", "Unlimited budgets", "AI insights", "Priority support"].map((b) => (
            <div key={b} className="flex items-center gap-1 text-[11px] font-medium text-foreground">
              <Check className="w-3 h-3 text-success" strokeWidth={3} /> {b}
            </div>
          ))}
        </div>
        <button className="w-full mt-3 py-2 rounded-xl bg-foreground text-background text-[12.5px] font-bold hover:opacity-90 active:scale-[0.99] transition-all">
          Upgrade to Business
        </button>
      </section>

      {/* ── Ações da conta (sair / deletar) ─────────────────────────── */}
      <section>
        <p className="section-label mb-1.5">Account actions</p>
        <div className="card-soft divide-y divide-border">
          <Row icon={LogOut} iconBg="bg-secondary" label="Log out" />
          <Row icon={Trash2} iconBg="bg-surface-pink" iconColor="text-destructive" label="Delete account" danger />
        </div>
        <p className="text-[10.5px] text-muted-foreground mt-2 px-1">FinSight v2.4.0 · Build 2026.04</p>
      </section>
    </div>
  );
};
