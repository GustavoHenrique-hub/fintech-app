import { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { TrendingUp, ArrowLeft, Eye, EyeOff, CheckCircle2, RotateCcw } from "lucide-react";
import { Button } from "@/components/ui/button";
import { PasswordStrengthMeter } from "@/components/ui/password-strength-meter";

// ── OTP — 6 caixas numéricas com navegação automática ──────────────
function OtpInput({ value, onChange }) {
  const refs = Array.from({ length: 6 }, () => useRef(null));

  const handleChange = (i, raw) => {
    const digit = raw.replace(/\D/g, "").slice(-1);
    const next  = [...value];
    next[i]     = digit;
    onChange(next);
    if (digit && i < 5) refs[i + 1].current?.focus();
  };

  const handleKeyDown = (i, e) => {
    if (e.key === "Backspace" && !value[i] && i > 0) {
      refs[i - 1].current?.focus();
    }
    if (e.key === "ArrowLeft"  && i > 0) refs[i - 1].current?.focus();
    if (e.key === "ArrowRight" && i < 5) refs[i + 1].current?.focus();
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const digits = e.clipboardData.getData("text").replace(/\D/g, "").slice(0, 6).split("");
    const next   = Array(6).fill("");
    digits.forEach((d, i) => { next[i] = d; });
    onChange(next);
    const focusIdx = Math.min(digits.length, 5);
    refs[focusIdx].current?.focus();
  };

  return (
    <div className="flex gap-2.5" onPaste={handlePaste}>
      {value.map((d, i) => (
        <input
          key={i}
          ref={refs[i]}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={d}
          onChange={(e) => handleChange(i, e.target.value)}
          onKeyDown={(e) => handleKeyDown(i, e)}
          className={`w-full aspect-[1/1.1] max-w-[52px] text-center text-[20px] font-extrabold
                      rounded-xl bg-card border-2 outline-none transition-all
                      focus:ring-2 focus:ring-primary/25 focus:border-primary/50 ${
                        d ? "border-primary/40 text-foreground" : "border-border text-foreground"
                      }`}
        />
      ))}
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

export default function EsquecerSenhaPage() {
  // 1 = inserir e-mail | 2 = código OTP | 3 = nova senha | 4 = sucesso
  const [step, setStep] = useState(1);

  const [email, setEmail]                 = useState("");
  const [otp, setOtp]                     = useState(Array(6).fill(""));
  const [novaSenha, setNovaSenha]         = useState("");
  const [confirmar, setConfirmar]         = useState("");
  const [senhaInfo, setSenhaInfo]         = useState({ score: 0, level: "weak" });
  const [mostrarSenha, setMostrarSenha]   = useState(false);
  const [mostrarConf, setMostrarConf]     = useState(false);
  const [touched, setTouched]             = useState(false);
  const [reenviando, setReenviando]       = useState(false);

  // validações
  const erroEmail    = touched && !email.trim().includes("@");
  const otpCompleto  = otp.every(Boolean);
  const erroOtp      = touched && !otpCompleto;
  const erroSenha    = touched && senhaInfo.level === "weak";
  const erroConfirmar = touched && novaSenha !== confirmar;
  const step3Valid   = senhaInfo.level !== "weak" && novaSenha === confirmar && novaSenha.length >= 8;

  const simularReenvio = () => {
    setReenviando(true);
    setTimeout(() => setReenviando(false), 2000);
  };

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

      <div className="w-full max-w-[380px]">

        {/* ── Step 1 — Inserir e-mail ─────────────────────────────────── */}
        {step === 1 && (
          <>
            <Link
              to="/login"
              className="flex items-center gap-1.5 text-[13px] text-muted-foreground font-semibold
                         mb-5 hover:text-foreground transition-colors"
            >
              <ArrowLeft className="w-4 h-4" /> Voltar ao login
            </Link>

            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight">
                Esqueceu sua senha?
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1 leading-relaxed">
                Informe o e-mail da sua conta e enviaremos um código de verificação.
              </p>
            </div>

            <div>
              <label className="section-label" htmlFor="rec-email">E-mail</label>
              <input
                id="rec-email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="voce@email.com"
                autoComplete="email"
                className={`mt-1.5 ${inputClass(erroEmail)}`}
                onKeyDown={(e) => e.key === "Enter" && e.currentTarget.form?.requestSubmit()}
              />
              {erroEmail && (
                <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                  Informe um e-mail válido
                </p>
              )}
            </div>

            <Button
              className="w-full mt-6"
              size="lg"
              onClick={() => {
                setTouched(true);
                if (!email.trim().includes("@")) return;
                setTouched(false);
                setStep(2);
              }}
            >
              Enviar código
            </Button>
          </>
        )}

        {/* ── Step 2 — Verificação OTP ────────────────────────────────── */}
        {step === 2 && (
          <>
            <button
              type="button"
              onClick={() => { setTouched(false); setStep(1); }}
              className="flex items-center gap-1.5 text-[13px] text-muted-foreground font-semibold
                         mb-5 hover:text-foreground transition-colors"
            >
              <ArrowLeft className="w-4 h-4" /> Voltar
            </button>

            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight">
                Verifique seu e-mail
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1 leading-relaxed">
                Enviamos um código de 6 dígitos para{" "}
                <span className="font-semibold text-foreground">{email}</span>.
              </p>
            </div>

            <OtpInput value={otp} onChange={setOtp} />

            {erroOtp && (
              <p className="text-[11.5px] text-destructive mt-2 font-medium">
                Preencha os 6 dígitos do código
              </p>
            )}

            <Button
              className="w-full mt-6"
              size="lg"
              onClick={() => {
                setTouched(true);
                if (!otpCompleto) return;
                setTouched(false);
                setStep(3);
              }}
            >
              Confirmar código
            </Button>

            <button
              type="button"
              onClick={simularReenvio}
              disabled={reenviando}
              className="w-full mt-3 flex items-center justify-center gap-1.5
                         text-[13px] text-muted-foreground hover:text-primary font-semibold
                         transition-colors disabled:opacity-50"
            >
              <RotateCcw className={`w-3.5 h-3.5 ${reenviando ? "animate-spin" : ""}`} strokeWidth={2.5} />
              {reenviando ? "Reenviando..." : "Reenviar código"}
            </button>
          </>
        )}

        {/* ── Step 3 — Nova senha ─────────────────────────────────────── */}
        {step === 3 && (
          <>
            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight">
                Nova senha
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1">
                Crie uma senha forte para proteger sua conta.
              </p>
            </div>

            <div className="space-y-4">
              {/* Nova senha */}
              <div>
                <label className="section-label" htmlFor="nova-senha">Nova senha</label>
                <div className="relative mt-1.5">
                  <input
                    id="nova-senha"
                    type={mostrarSenha ? "text" : "password"}
                    value={novaSenha}
                    onChange={(e) => setNovaSenha(e.target.value)}
                    placeholder="••••••••"
                    autoComplete="new-password"
                    className={`${inputClass(erroSenha)} pr-11`}
                  />
                  <button
                    type="button"
                    onClick={() => setMostrarSenha((v) => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                    aria-label="Mostrar senha"
                  >
                    {mostrarSenha
                      ? <EyeOff className="w-4 h-4" strokeWidth={2} />
                      : <Eye    className="w-4 h-4" strokeWidth={2} />}
                  </button>
                </div>
                {erroSenha && (
                  <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                    Escolha uma senha mais forte
                  </p>
                )}
                {novaSenha && (
                  <div className="mt-3">
                    <PasswordStrengthMeter value={novaSenha} onChange={setSenhaInfo} />
                  </div>
                )}
              </div>

              {/* Confirmar */}
              <div>
                <label className="section-label" htmlFor="conf-senha">Confirmar nova senha</label>
                <div className="relative mt-1.5">
                  <input
                    id="conf-senha"
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
            </div>

            <Button
              className="w-full mt-7"
              size="lg"
              onClick={() => {
                setTouched(true);
                if (!step3Valid) return;
                // TODO: integrar com POST /auth/reset-senha
                setStep(4);
              }}
            >
              Salvar nova senha
            </Button>
          </>
        )}

        {/* ── Step 4 — Sucesso ────────────────────────────────────────── */}
        {step === 4 && (
          <div className="text-center py-4">
            <div className="w-24 h-24 rounded-full bg-success/10 flex items-center justify-center mx-auto mb-6">
              <CheckCircle2 className="w-12 h-12 text-success" strokeWidth={1.75} />
            </div>

            <h2 className="text-[26px] font-extrabold text-foreground tracking-tight">
              Senha redefinida!
            </h2>
            <p className="text-[13.5px] text-muted-foreground mt-2 leading-relaxed">
              Sua senha foi atualizada com sucesso.<br />
              Acesse sua conta normalmente.
            </p>

            <Link to="/login">
              <Button className="mt-8 w-full" size="lg">Fazer login</Button>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}
