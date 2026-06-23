import { useState } from "react";
import { Link } from "react-router-dom";
import { Eye, EyeOff, TrendingUp, ShieldCheck, Zap, PieChart } from "lucide-react";
import { Button } from "@/components/ui/button";

export default function LoginPage() {
  const [email, setEmail]               = useState("");
  const [senha, setSenha]               = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [touched, setTouched]           = useState(false);

  const erroEmail = touched && !email.trim().includes("@");
  const erroSenha = touched && senha.length < 1;

  const handleSubmit = (e) => {
    e.preventDefault();
    setTouched(true);
    if (!email.trim().includes("@") || senha.length < 1) return;
    // TODO: integrar com POST /auth/login
    console.log("login", { email, senha });
  };

  return (
    <div className="min-h-[100dvh] bg-background flex">

      {/* ── Coluna esquerda — branding (somente ≥ lg) ─────────────────── */}
      <div className="hidden lg:flex lg:w-[46%] xl:w-[42%] relative overflow-hidden
                      bg-gradient-to-br from-primary via-primary to-[hsl(265_70%_52%)]
                      flex-col items-center justify-center p-14">
        {/* bolhas decorativas */}
        <div className="absolute -top-28 -left-28 w-96 h-96 rounded-full bg-white/10 blur-3xl pointer-events-none" />
        <div className="absolute bottom-0 right-0 w-72 h-72 rounded-full bg-accent/25 blur-3xl pointer-events-none" />
        <div className="absolute top-1/2 -right-20 w-56 h-56 rounded-full bg-white/5 blur-2xl pointer-events-none" />

        <div className="relative z-10 w-full max-w-[340px]">
          {/* Logotipo */}
          <div className="flex items-center gap-3 mb-12">
            <div className="w-11 h-11 rounded-2xl bg-white/15 backdrop-blur flex items-center justify-center shadow-lg">
              <TrendingUp className="w-6 h-6 text-white" strokeWidth={2.25} />
            </div>
            <span className="text-[28px] font-extrabold text-white tracking-tight">FinSight</span>
          </div>

          <h1 className="text-[36px] font-extrabold text-white leading-tight tracking-tight">
            Seu dinheiro,<br />seu controle.
          </h1>
          <p className="text-white/65 text-[14px] mt-4 leading-relaxed">
            Inteligência financeira com IA embutida para você tomar melhores decisões.
          </p>

          {/* Bullets de valor */}
          <div className="mt-10 space-y-4">
            {[
              { icon: Zap,        text: "Categorização automática com IA" },
              { icon: PieChart,   text: "Análise de gastos em tempo real" },
              { icon: ShieldCheck,text: "Criptografia bancária de ponta" },
            ].map(({ icon: Icon, text }) => (
              <div key={text} className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-white/15 flex items-center justify-center shrink-0">
                  <Icon className="w-4 h-4 text-white" strokeWidth={2} />
                </div>
                <span className="text-[13px] text-white/80 font-medium">{text}</span>
              </div>
            ))}
          </div>

          {/* Stat pill */}
          <div className="mt-12 inline-flex items-center gap-2 bg-white/10 backdrop-blur rounded-full px-4 py-2">
            <span className="w-2 h-2 rounded-full bg-success" />
            <span className="text-[12px] text-white/85 font-semibold">+12 000 usuários ativos</span>
          </div>
        </div>
      </div>

      {/* ── Coluna direita — formulário ────────────────────────────────── */}
      <div className="flex-1 flex flex-col items-center justify-center px-5 py-12 sm:px-10 overflow-y-auto">

        {/* Logotipo mobile */}
        <div className="lg:hidden flex items-center gap-2.5 mb-10">
          <div className="w-9 h-9 rounded-xl bg-primary flex items-center justify-center shadow-md shadow-primary/30">
            <TrendingUp className="w-5 h-5 text-white" strokeWidth={2.25} />
          </div>
          <span className="text-[22px] font-extrabold text-foreground tracking-tight">FinSight</span>
        </div>

        <div className="w-full max-w-[380px]">
          <div className="mb-8">
            <h2 className="text-[26px] font-extrabold text-foreground tracking-tight leading-tight">
              Bem-vindo de volta
            </h2>
            <p className="text-[13.5px] text-muted-foreground mt-1.5">
              Acesse sua conta financeira
            </p>
          </div>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">

            {/* E-mail */}
            <div>
              <label className="section-label" htmlFor="login-email">E-mail</label>
              <input
                id="login-email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="voce@email.com"
                autoComplete="email"
                className={`mt-1.5 w-full h-11 px-3.5 rounded-xl bg-card border text-[14px] outline-none
                            placeholder:text-muted-foreground/55 transition-all
                            focus:ring-2 focus:ring-primary/25 ${
                              erroEmail
                                ? "border-destructive/50 focus:border-destructive/40"
                                : "border-border focus:border-primary/40"
                            }`}
              />
              {erroEmail && (
                <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                  Informe um e-mail válido
                </p>
              )}
            </div>

            {/* Senha */}
            <div>
              <div className="flex items-center justify-between">
                <label className="section-label" htmlFor="login-senha">Senha</label>
                <Link
                  to="/esqueci-senha"
                  className="text-[11.5px] text-primary font-semibold hover:underline focus:outline-none"
                >
                  Esqueci minha senha
                </Link>
              </div>
              <div className="relative mt-1.5">
                <input
                  id="login-senha"
                  type={mostrarSenha ? "text" : "password"}
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  placeholder="••••••••"
                  autoComplete="current-password"
                  className={`w-full h-11 pl-3.5 pr-11 rounded-xl bg-card border text-[14px] outline-none
                              placeholder:text-muted-foreground/55 transition-all
                              focus:ring-2 focus:ring-primary/25 ${
                                erroSenha
                                  ? "border-destructive/50 focus:border-destructive/40"
                                  : "border-border focus:border-primary/40"
                              }`}
                />
                <button
                  type="button"
                  onClick={() => setMostrarSenha((v) => !v)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground
                             hover:text-foreground transition-colors"
                  aria-label={mostrarSenha ? "Ocultar senha" : "Mostrar senha"}
                >
                  {mostrarSenha
                    ? <EyeOff className="w-4 h-4" strokeWidth={2} />
                    : <Eye    className="w-4 h-4" strokeWidth={2} />}
                </button>
              </div>
              {erroSenha && (
                <p className="text-[11.5px] text-destructive mt-1.5 font-medium">
                  Informe sua senha
                </p>
              )}
            </div>

            <Button type="submit" className="w-full !mt-6" size="lg">
              Entrar
            </Button>
          </form>

          {/* Separador */}
          <div className="flex items-center gap-3 my-5">
            <div className="flex-1 h-px bg-border" />
            <span className="text-[11.5px] text-muted-foreground font-medium">ou</span>
            <div className="flex-1 h-px bg-border" />
          </div>

          {/* Criar conta */}
          <p className="text-center text-[13.5px] text-muted-foreground">
            Não tem uma conta?{" "}
            <Link to="/cadastro" className="text-primary font-bold hover:underline">
              Criar conta grátis
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
