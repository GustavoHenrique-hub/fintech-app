import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { TrendingUp, ArrowLeft, Eye, EyeOff, CheckCircle2, Mail } from "lucide-react";
import { useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { InputCPF } from "@/components/ui/input-cpf";
import { PasswordStrengthMeter } from "@/components/ui/password-strength-meter";
import { usuarioService } from "@/services";

const STEPS = [
  { numero: 1, label: "Dados pessoais" },
  { numero: 2, label: "Segurança" },
];

function StepIndicator({ atual }) {
  return (
    <div className="flex items-center gap-0 mb-8 w-full max-w-[240px]">
      {STEPS.map((s, i) => (
        <div key={s.numero} className="flex items-center flex-1">
          <div className="flex flex-col items-center">
            <div
              className={`w-8 h-8 rounded-full flex items-center justify-center text-[12px] font-extrabold border-2 transition-all ${
                atual > s.numero
                  ? "bg-primary border-primary text-white"
                  : atual === s.numero
                    ? "bg-primary border-primary text-white shadow-md shadow-primary/30"
                    : "bg-card border-border text-muted-foreground"
              }`}
            >
              {atual > s.numero ? <CheckCircle2 className="w-4 h-4" strokeWidth={2.5} /> : s.numero}
            </div>
            <span className={`text-[10px] font-semibold mt-1 whitespace-nowrap ${
              atual >= s.numero ? "text-primary" : "text-muted-foreground"
            }`}>
              {s.label}
            </span>
          </div>
          {i < STEPS.length - 1 && (
            <div className={`flex-1 h-0.5 mb-4 mx-1 transition-all ${
              atual > s.numero ? "bg-primary" : "bg-border"
            }`} />
          )}
        </div>
      ))}
    </div>
  );
}

function InputField({ label, error, children }) {
  return (
    <div>
      {label && <label className="section-label">{label}</label>}
      <div className="mt-1.5">{children}</div>
      {error && <p className="text-[11.5px] text-destructive mt-1.5 font-medium">{error}</p>}
    </div>
  );
}

const inputClass = (erro) =>
  `w-full h-11 px-3.5 rounded-xl bg-card border text-[14px] outline-none
   placeholder:text-muted-foreground/55 transition-all focus:ring-2 focus:ring-primary/25 ${
     erro
       ? "border-destructive/50 focus:border-destructive/40"
       : "border-border focus:border-primary/40"
   }`;

export default function CadastroPage() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  // Campos step 1
  const [nome, setNome]                     = useState("");
  const [cpf, setCpf]                       = useState("");
  const [cpfValido, setCpfValido]           = useState(false);
  const [email, setEmail]                   = useState("");
  const [telefone, setTelefone]             = useState("");
  const [dataNascimento, setDataNascimento] = useState("");

  // Campos step 2
  const [senha, setSenha]               = useState("");
  const [confirmar, setConfirmar]       = useState("");
  const [senhaInfo, setSenhaInfo]       = useState({ score: 0, level: "weak" });
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [mostrarConf, setMostrarConf]   = useState(false);

  const [touched, setTouched] = useState(false);

  // ── Validações step 1 ────────────────────────────────────────────
  const erroNome   = touched && nome.trim().split(" ").filter(Boolean).length < 2;
  const erroCpf    = touched && !cpfValido;
  const erroEmail  = touched && !email.trim().includes("@");
  const erroNasc   = touched && !dataNascimento;
  const step1Valid = !erroNome && !erroCpf && !erroEmail && !erroNasc
                     && nome.trim().length > 0 && cpfValido && email.includes("@") && !!dataNascimento;

  // ── Validações step 2 ────────────────────────────────────────────
  const erroSenhaFraca = touched && senhaInfo.level === "weak";
  const erroConfirmar  = touched && senha !== confirmar;
  const step2Valid     = senhaInfo.level !== "weak" && senha === confirmar && senha.length >= 8;

  const { mutate: criar, isPending, isError, error } = useMutation({
    mutationFn: (dto) => usuarioService.criar(dto),
    onSuccess: () => setStep(3),
  });

  const avancar = () => {
    setTouched(true);
    if (step === 1 && !step1Valid) return;
    setTouched(false);
    setStep(2);
  };

  const submeter = (e) => {
    e.preventDefault();
    setTouched(true);
    if (!step2Valid) return;
    criar({
      nome: nome.trim(),
      cpf: cpf.replace(/\D/g, ""),
      email: email.trim(),
      senha,
      telefone: telefone.trim() || undefined,
      dataNascimento: dataNascimento || undefined,
    });
  };

  const maxNasc = new Date(Date.now() - 18 * 365.25 * 24 * 60 * 60 * 1000)
    .toISOString()
    .slice(0, 10);

  const erroServidor = isError && error?.response?.status !== 401;

  return (
    <div className="min-h-[100dvh] bg-background flex flex-col items-center justify-center
                    px-5 py-10 sm:px-8 overflow-y-auto">

      {/* Logo */}
      <Link to="/login" className="flex items-center gap-2.5 mb-8 self-start sm:self-center">
        <div className="w-9 h-9 rounded-xl bg-primary flex items-center justify-center shadow-md shadow-primary/30">
          <TrendingUp className="w-5 h-5 text-white" strokeWidth={2.25} />
        </div>
        <span className="text-[21px] font-extrabold text-foreground tracking-tight">FinSight</span>
      </Link>

      <div className="w-full max-w-[440px]">

        {/* ── Step 1 — Dados pessoais ─────────────────────────────────── */}
        {step === 1 && (
          <>
            <StepIndicator atual={1} />

            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight leading-tight">
                Criar conta
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1">
                Preencha seus dados para começar
              </p>
            </div>

            <form
              onSubmit={(e) => { e.preventDefault(); avancar(); }}
              noValidate
              className="space-y-4"
            >
              <InputField label="Nome completo" error={erroNome && "Informe nome e sobrenome"}>
                <input
                  type="text"
                  value={nome}
                  onChange={(e) => setNome(e.target.value)}
                  placeholder="Gustavo Henrique Silva"
                  autoComplete="name"
                  className={inputClass(erroNome)}
                />
              </InputField>

              <InputField label="CPF" error={erroCpf && "Informe um CPF válido"}>
                <InputCPF value={cpf} onChange={setCpf} onValid={setCpfValido} />
              </InputField>

              <InputField label="E-mail" error={erroEmail && "Informe um e-mail válido"}>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="voce@email.com"
                  autoComplete="email"
                  className={inputClass(erroEmail)}
                />
              </InputField>

              <div className="grid grid-cols-2 gap-3">
                <InputField label="Data de nascimento" error={erroNasc && "Obrigatório"}>
                  <input
                    type="date"
                    value={dataNascimento}
                    onChange={(e) => setDataNascimento(e.target.value)}
                    max={maxNasc}
                    className={inputClass(erroNasc)}
                  />
                </InputField>

                <InputField label="Telefone (opcional)">
                  <input
                    type="tel"
                    value={telefone}
                    onChange={(e) => setTelefone(e.target.value)}
                    placeholder="+55 11 9 ..."
                    autoComplete="tel"
                    className={inputClass(false)}
                  />
                </InputField>
              </div>

              <Button type="submit" className="w-full !mt-6" size="lg">
                Continuar
              </Button>
            </form>
          </>
        )}

        {/* ── Step 2 — Senha ──────────────────────────────────────────── */}
        {step === 2 && (
          <>
            <StepIndicator atual={2} />

            <button
              type="button"
              onClick={() => { setTouched(false); setStep(1); }}
              className="flex items-center gap-1.5 text-[13px] text-muted-foreground font-semibold
                         mb-5 hover:text-foreground transition-colors"
            >
              <ArrowLeft className="w-4 h-4" /> Voltar
            </button>

            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight leading-tight">
                Crie sua senha
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1">
                Use uma senha forte para proteger sua conta
              </p>
            </div>

            {erroServidor && (
              <div className="mb-4 px-3.5 py-3 rounded-xl bg-destructive/10 border border-destructive/20">
                <p className="text-[13px] text-destructive font-semibold">
                  {error?.response?.data?.message ?? "Não foi possível criar a conta. Tente novamente."}
                </p>
              </div>
            )}

            <form onSubmit={submeter} noValidate className="space-y-4">
              <div>
                <label className="section-label" htmlFor="cad-senha">Senha</label>
                <div className="relative mt-1.5">
                  <input
                    id="cad-senha"
                    type={mostrarSenha ? "text" : "password"}
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    placeholder="••••••••"
                    autoComplete="new-password"
                    className={`${inputClass(erroSenhaFraca)} pr-11`}
                  />
                  <button
                    type="button"
                    onClick={() => setMostrarSenha((v) => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                    aria-label={mostrarSenha ? "Ocultar senha" : "Mostrar senha"}
                  >
                    {mostrarSenha
                      ? <EyeOff className="w-4 h-4" strokeWidth={2} />
                      : <Eye    className="w-4 h-4" strokeWidth={2} />}
                  </button>
                </div>
                {erroSenhaFraca && (
                  <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                    Escolha uma senha mais forte
                  </p>
                )}
                {senha && (
                  <div className="mt-3">
                    <PasswordStrengthMeter value={senha} onChange={setSenhaInfo} />
                  </div>
                )}
              </div>

              <div>
                <label className="section-label" htmlFor="cad-confirmar">Confirmar senha</label>
                <div className="relative mt-1.5">
                  <input
                    id="cad-confirmar"
                    type={mostrarConf ? "text" : "password"}
                    value={confirmar}
                    onChange={(e) => setConfirmar(e.target.value)}
                    placeholder="••••••••"
                    autoComplete="new-password"
                    className={`${inputClass(erroConfirmar)} pr-11`}
                  />
                  <button
                    type="button"
                    onClick={() => setMostrarConf((v) => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                    aria-label="Mostrar confirmação"
                  >
                    {mostrarConf
                      ? <EyeOff className="w-4 h-4" strokeWidth={2} />
                      : <Eye    className="w-4 h-4" strokeWidth={2} />}
                  </button>
                </div>
                {erroConfirmar && (
                  <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                    As senhas não conferem
                  </p>
                )}
              </div>

              <Button type="submit" className="w-full !mt-6" size="lg" loading={isPending}>
                Criar conta
              </Button>
            </form>
          </>
        )}

        {/* ── Step 3 — Sucesso ────────────────────────────────────────── */}
        {step === 3 && (
          <div className="text-center py-4">
            <div className="w-24 h-24 rounded-full bg-success/10 flex items-center justify-center mx-auto mb-6">
              <CheckCircle2 className="w-12 h-12 text-success" strokeWidth={1.75} />
            </div>

            <h2 className="text-[26px] font-extrabold text-foreground tracking-tight">
              Conta criada com sucesso!
            </h2>
            <p className="text-[13.5px] text-muted-foreground mt-2 leading-relaxed">
              Bem-vindo ao FinSight,{" "}
              <span className="font-semibold text-foreground">
                {nome.split(" ")[0]}
              </span>
              .
            </p>

            <div className="mt-6 card-soft p-4 flex items-center gap-3 text-left">
              <div className="w-10 h-10 rounded-xl bg-surface-purple text-primary flex items-center justify-center shrink-0">
                <Mail className="w-5 h-5" strokeWidth={2} />
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-[12.5px] font-bold text-foreground">Verifique seu e-mail</p>
                <p className="text-[11.5px] text-muted-foreground mt-0.5 truncate">
                  Enviamos um link de ativação para <strong>{email}</strong>
                </p>
              </div>
            </div>

            <Link to="/login">
              <Button className="mt-6 w-full" size="lg">Fazer login</Button>
            </Link>
          </div>
        )}

        {step < 3 && (
          <p className="text-center text-[13px] text-muted-foreground mt-6">
            Já tem uma conta?{" "}
            <Link to="/login" className="text-primary font-bold hover:underline">
              Entrar
            </Link>
          </p>
        )}
      </div>
    </div>
  );
}
