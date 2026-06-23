import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  TrendingUp, ArrowLeft, Eye, EyeOff, CheckCircle2,
  ChevronRight, Search, Plus, Building2,
} from "lucide-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { InputCPF } from "@/components/ui/input-cpf";
import { PasswordStrengthMeter } from "@/components/ui/password-strength-meter";
import { InputMonetario } from "@/components/ui/input-monetario";
import { usuarioService, contaFinanceiraService, bancoService, authService } from "@/services";
import { useAuth } from "@/context/AuthContext";
import { getBancoLogoUrl, getBancoColor } from "@/lib/banco-utils";

// ── Constantes ──────────────────────────────────────────────────────────────
const STEPS = [
  { numero: 1, label: "Dados pessoais" },
  { numero: 2, label: "Segurança" },
  { numero: 3, label: "Seu banco" },
];

const TIPOS_CONTA = [
  { value: "corrente",     label: "Conta corrente" },
  { value: "poupanca",     label: "Poupança" },
  { value: "cartao",       label: "Cartão de crédito" },
  { value: "investimento", label: "Investimento" },
  { value: "dinheiro",     label: "Dinheiro" },
];

// ── Componentes internos ────────────────────────────────────────────────────
function StepIndicator({ atual }) {
  return (
    <div className="flex items-center mb-8 w-full">
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
            <span className={`text-[9.5px] font-semibold mt-1 whitespace-nowrap ${
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

// Logo do banco com fallback
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

// ── Componente principal ───────────────────────────────────────────────────
export default function CadastroPage() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();
  const { saveSession, user } = useAuth();

  // ── Step 1: dados pessoais
  const [nome, setNome]                     = useState("");
  const [cpf, setCpf]                       = useState("");
  const [cpfValido, setCpfValido]           = useState(false);
  const [email, setEmail]                   = useState("");
  const [telefone, setTelefone]             = useState("");
  const [dataNascimento, setDataNascimento] = useState("");

  // ── Step 2: senha
  const [senha, setSenha]               = useState("");
  const [confirmar, setConfirmar]       = useState("");
  const [senhaInfo, setSenhaInfo]       = useState({ score: 0, level: "weak" });
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [mostrarConf, setMostrarConf]   = useState(false);

  // ── Step 3: banco
  const [buscaBanco, setBuscaBanco]         = useState("");
  const [bancoSelecionado, setBancoSelecionado] = useState(null);
  const [tipoConta, setTipoConta]           = useState("corrente");
  const [nomeConta, setNomeConta]           = useState("");
  const [saldoInicial, setSaldoInicial]     = useState(undefined);

  const [touched, setTouched]               = useState(false);
  const [userId, setUserId]                 = useState(null);

  // Validações step 1
  const erroNome   = touched && nome.trim().split(" ").filter(Boolean).length < 2;
  const erroCpf    = touched && !cpfValido;
  const erroEmail  = touched && !email.trim().includes("@");
  const erroNasc   = touched && !dataNascimento;
  const step1Valid = nome.trim().split(" ").filter(Boolean).length >= 2 && cpfValido && email.includes("@") && !!dataNascimento;

  // Validações step 2
  const erroSenhaFraca = touched && senhaInfo.level === "weak";
  const erroConfirmar  = touched && senha !== confirmar;
  const step2Valid     = senhaInfo.level !== "weak" && senha === confirmar && senha.length >= 8;

  // Bancos da API (só busca quando autenticado — step 3)
  const { data: bancos = [], isLoading: loadingBancos } = useQuery({
    queryKey: ["bancos"],
    queryFn: () => bancoService.listar(),
    enabled: step === 3,
    staleTime: Infinity,
  });

  const bancosFiltrados = bancos.filter((b) =>
    !buscaBanco || b.nome?.toLowerCase().includes(buscaBanco.toLowerCase()),
  );

  // POST /usuarios
  const { mutate: criarUsuario, isPending: criandoUsuario, isError: erroCriacao, error: erroCriacaoObj } = useMutation({
    mutationFn: (dto) => usuarioService.criar(dto),
    onSuccess: async (data) => {
      const id = data.id ?? data.idUsuario;
      setUserId(id);
      // Auto-login para obter o token antes do step 3
      try {
        const session = await authService.login({ email: email.trim(), senha });
        saveSession(session);
      } catch {
        // login falhou, mas cadastro foi feito — vai pra step 3 sem token
        // a tela de bancos não vai carregar mas o usuário já está cadastrado
      }
      setTouched(false);
      setStep(3);
    },
  });

  // POST /contas
  const { mutate: vincularConta, isPending: vinculando } = useMutation({
    mutationFn: (dto) => contaFinanceiraService.criar(dto),
    onSuccess: () => setStep(4),
  });

  const maxNasc = new Date(Date.now() - 18 * 365.25 * 24 * 60 * 60 * 1000)
    .toISOString().slice(0, 10);

  const avancar = () => {
    setTouched(true);
    if (!step1Valid) return;
    setTouched(false);
    setStep(2);
  };

  const submeterCadastro = (e) => {
    e.preventDefault();
    setTouched(true);
    if (!step2Valid) return;
    criarUsuario({
      nome: nome.trim(),
      cpf: cpf.replace(/\D/g, ""),
      email: email.trim(),
      senha,
      telefone: telefone.trim() || undefined,
      dataNascimento: dataNascimento || undefined,
    });
  };

  const submeterBanco = () => {
    if (!bancoSelecionado) return;
    const uid = userId ?? user?.idUsuario;
    vincularConta({
      usuarioId: uid,
      bancoId: bancoSelecionado.id,
      nome: nomeConta.trim() || `${bancoSelecionado.nome} – ${TIPOS_CONTA.find(t => t.value === tipoConta)?.label}`,
      tipo: tipoConta,
      saldoInicial: saldoInicial ?? 0,
      padrao: true,
      ativa: true,
    });
  };

  const pularVinculacao = () => setStep(4);

  const erroServidorCadastro = erroCriacao && erroCriacaoObj?.response?.status !== 401;

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

      <div className="w-full max-w-[460px]">

        {/* ── Step 1 — Dados pessoais ─────────────────────────────────── */}
        {step === 1 && (
          <>
            <StepIndicator atual={1} />
            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight leading-tight">Criar conta</h2>
              <p className="text-[13px] text-muted-foreground mt-1">Preencha seus dados para começar</p>
            </div>

            <form onSubmit={(e) => { e.preventDefault(); avancar(); }} noValidate className="space-y-4">
              <InputField label="Nome completo" error={erroNome && "Informe nome e sobrenome"}>
                <input type="text" value={nome} onChange={(e) => setNome(e.target.value)}
                  placeholder="Gustavo Henrique Silva" autoComplete="name" className={inputClass(erroNome)} />
              </InputField>

              <InputField label="CPF" error={erroCpf && "Informe um CPF válido"}>
                <InputCPF value={cpf} onChange={setCpf} onValid={setCpfValido} />
              </InputField>

              <InputField label="E-mail" error={erroEmail && "Informe um e-mail válido"}>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)}
                  placeholder="voce@email.com" autoComplete="email" className={inputClass(erroEmail)} />
              </InputField>

              <div className="grid grid-cols-2 gap-3">
                <InputField label="Data de nascimento" error={erroNasc && "Obrigatório"}>
                  <input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)}
                    max={maxNasc} className={inputClass(erroNasc)} />
                </InputField>
                <InputField label="Telefone (opcional)">
                  <input type="tel" value={telefone} onChange={(e) => setTelefone(e.target.value)}
                    placeholder="+55 11 9 ..." autoComplete="tel" className={inputClass(false)} />
                </InputField>
              </div>

              <Button type="submit" className="w-full !mt-6" size="lg">Continuar</Button>
            </form>
          </>
        )}

        {/* ── Step 2 — Senha ──────────────────────────────────────────── */}
        {step === 2 && (
          <>
            <StepIndicator atual={2} />
            <button type="button" onClick={() => { setTouched(false); setStep(1); }}
              className="flex items-center gap-1.5 text-[13px] text-muted-foreground font-semibold mb-5 hover:text-foreground transition-colors">
              <ArrowLeft className="w-4 h-4" /> Voltar
            </button>

            <div className="mb-6">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight leading-tight">Crie sua senha</h2>
              <p className="text-[13px] text-muted-foreground mt-1">Use uma senha forte para proteger sua conta</p>
            </div>

            {erroServidorCadastro && (
              <div className="mb-4 px-3.5 py-3 rounded-xl bg-destructive/10 border border-destructive/20">
                <p className="text-[13px] text-destructive font-semibold">
                  {erroCriacaoObj?.response?.data?.message ?? "Não foi possível criar a conta. Tente novamente."}
                </p>
              </div>
            )}

            <form onSubmit={submeterCadastro} noValidate className="space-y-4">
              <div>
                <label className="section-label" htmlFor="cad-senha">Senha</label>
                <div className="relative mt-1.5">
                  <input id="cad-senha" type={mostrarSenha ? "text" : "password"} value={senha}
                    onChange={(e) => setSenha(e.target.value)} placeholder="••••••••" autoComplete="new-password"
                    className={`${inputClass(erroSenhaFraca)} pr-11`} />
                  <button type="button" onClick={() => setMostrarSenha((v) => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                    aria-label={mostrarSenha ? "Ocultar senha" : "Mostrar senha"}>
                    {mostrarSenha ? <EyeOff className="w-4 h-4" strokeWidth={2} /> : <Eye className="w-4 h-4" strokeWidth={2} />}
                  </button>
                </div>
                {erroSenhaFraca && <p className="text-[11.5px] text-destructive mt-1.5 font-medium">Escolha uma senha mais forte</p>}
                {senha && <div className="mt-3"><PasswordStrengthMeter value={senha} onChange={setSenhaInfo} /></div>}
              </div>

              <div>
                <label className="section-label" htmlFor="cad-confirmar">Confirmar senha</label>
                <div className="relative mt-1.5">
                  <input id="cad-confirmar" type={mostrarConf ? "text" : "password"} value={confirmar}
                    onChange={(e) => setConfirmar(e.target.value)} placeholder="••••••••" autoComplete="new-password"
                    className={`${inputClass(erroConfirmar)} pr-11`} />
                  <button type="button" onClick={() => setMostrarConf((v) => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                    aria-label="Mostrar confirmação">
                    {mostrarConf ? <EyeOff className="w-4 h-4" strokeWidth={2} /> : <Eye className="w-4 h-4" strokeWidth={2} />}
                  </button>
                </div>
                {erroConfirmar && <p className="text-[11.5px] text-destructive mt-1.5 font-medium">As senhas não conferem</p>}
              </div>

              <Button type="submit" className="w-full !mt-6" size="lg" loading={criandoUsuario}>
                Criar conta
              </Button>
            </form>
          </>
        )}

        {/* ── Step 3 — Vincular banco ─────────────────────────────────── */}
        {step === 3 && (
          <>
            <StepIndicator atual={3} />
            <div className="mb-5">
              <h2 className="text-[24px] font-extrabold text-foreground tracking-tight leading-tight">
                Vincule seu banco
              </h2>
              <p className="text-[13px] text-muted-foreground mt-1">
                Selecione onde sua conta principal está
              </p>
            </div>

            {/* Busca de banco */}
            <div className="relative mb-3">
              <Search className="w-3.5 h-3.5 text-muted-foreground absolute left-3 top-1/2 -translate-y-1/2" strokeWidth={2.25} />
              <input
                value={buscaBanco}
                onChange={(e) => setBuscaBanco(e.target.value)}
                placeholder="Buscar banco..."
                className="w-full h-10 pl-8 pr-3.5 rounded-xl bg-secondary border border-transparent text-[13.5px] outline-none placeholder:text-muted-foreground/60 focus:ring-2 focus:ring-primary/25 focus:border-primary/30 transition-all"
              />
            </div>

            {/* Grid de bancos */}
            <div className="max-h-[260px] overflow-y-auto rounded-2xl border border-border bg-card divide-y divide-border no-scrollbar">
              {loadingBancos ? (
                <div className="py-8 text-center text-[13px] text-muted-foreground">Carregando bancos...</div>
              ) : bancosFiltrados.length === 0 ? (
                <div className="py-8 text-center text-[13px] text-muted-foreground">Nenhum banco encontrado</div>
              ) : bancosFiltrados.map((b) => {
                const ativo = bancoSelecionado?.id === b.id;
                return (
                  <button
                    key={b.id}
                    onClick={() => setBancoSelecionado(b)}
                    className={`w-full flex items-center gap-3 px-3.5 py-3 text-left transition-colors ${
                      ativo ? "bg-primary/8 border-l-2 border-primary" : "hover:bg-secondary"
                    }`}
                  >
                    <BancoLogo nome={b.nome} size={36} />
                    <div className="flex-1 min-w-0">
                      <p className="text-[13.5px] font-semibold text-foreground">{b.nome}</p>
                      {b.ispb && <p className="text-[10.5px] text-muted-foreground">ISPB {b.ispb}</p>}
                    </div>
                    {ativo && <CheckCircle2 className="w-5 h-5 text-primary shrink-0" strokeWidth={2.25} />}
                  </button>
                );
              })}
            </div>

            {/* Tipo de conta */}
            {bancoSelecionado && (
              <div className="mt-4 space-y-4 animate-in fade-in slide-in-from-bottom-2 duration-200">
                <div>
                  <label className="section-label">Tipo de conta</label>
                  <div className="mt-2 grid grid-cols-2 gap-2">
                    {TIPOS_CONTA.map((t) => (
                      <button
                        key={t.value}
                        onClick={() => setTipoConta(t.value)}
                        className={`h-10 px-3 rounded-xl text-[12.5px] font-semibold border transition-all ${
                          tipoConta === t.value
                            ? "bg-primary text-primary-foreground border-primary shadow-sm"
                            : "bg-card border-border text-foreground hover:bg-secondary"
                        }`}
                      >
                        {t.label}
                      </button>
                    ))}
                  </div>
                </div>

                <div>
                  <label className="section-label">Nome da conta (opcional)</label>
                  <input
                    value={nomeConta}
                    onChange={(e) => setNomeConta(e.target.value)}
                    placeholder={`${bancoSelecionado.nome} – ${TIPOS_CONTA.find(t => t.value === tipoConta)?.label}`}
                    className="mt-1.5 w-full h-11 px-3.5 rounded-xl bg-card border border-border text-[14px] outline-none placeholder:text-muted-foreground/55 transition-all focus:ring-2 focus:ring-primary/25 focus:border-primary/40"
                  />
                </div>

                <div>
                  <label className="section-label">Saldo inicial (opcional)</label>
                  <div className="mt-1.5">
                    <InputMonetario value={saldoInicial} onChange={setSaldoInicial} placeholder="R$ 0,00" />
                  </div>
                </div>
              </div>
            )}

            <div className="mt-5 space-y-2">
              <Button
                className="w-full"
                size="lg"
                disabled={!bancoSelecionado}
                loading={vinculando}
                onClick={submeterBanco}
              >
                {bancoSelecionado
                  ? `Vincular ${bancoSelecionado.nome}`
                  : "Selecione um banco acima"}
              </Button>
              <button
                onClick={pularVinculacao}
                className="w-full py-2.5 text-[13px] text-muted-foreground font-semibold hover:text-foreground transition-colors"
              >
                Pular por enquanto
              </button>
            </div>
          </>
        )}

        {/* ── Step 4 — Sucesso ────────────────────────────────────────── */}
        {step === 4 && (
          <div className="text-center py-4">
            <div className="w-24 h-24 rounded-full bg-success/10 flex items-center justify-center mx-auto mb-6">
              <CheckCircle2 className="w-12 h-12 text-success" strokeWidth={1.75} />
            </div>

            <h2 className="text-[26px] font-extrabold text-foreground tracking-tight">
              Conta criada com sucesso!
            </h2>
            <p className="text-[13.5px] text-muted-foreground mt-2 leading-relaxed">
              Bem-vindo ao FinSight,{" "}
              <span className="font-semibold text-foreground">{nome.split(" ")[0]}</span>.
              {bancoSelecionado && (
                <> Sua conta no <strong>{bancoSelecionado.nome}</strong> foi vinculada.</>
              )}
            </p>

            <div className="mt-6 card-soft p-4 flex items-center gap-3 text-left">
              {bancoSelecionado ? (
                <>
                  <BancoLogo nome={bancoSelecionado.nome} size={40} />
                  <div>
                    <p className="text-[12.5px] font-bold text-foreground">
                      {nomeConta || `${bancoSelecionado.nome} – ${TIPOS_CONTA.find(t => t.value === tipoConta)?.label}`}
                    </p>
                    <p className="text-[11.5px] text-muted-foreground mt-0.5">
                      Conta padrão vinculada com sucesso
                    </p>
                  </div>
                </>
              ) : (
                <>
                  <div className="w-10 h-10 rounded-xl bg-surface-purple text-primary flex items-center justify-center shrink-0">
                    <Building2 className="w-5 h-5" strokeWidth={2} />
                  </div>
                  <div>
                    <p className="text-[12.5px] font-bold text-foreground">Vincule um banco depois</p>
                    <p className="text-[11.5px] text-muted-foreground mt-0.5">
                      Você pode adicionar contas a qualquer momento em Perfil
                    </p>
                  </div>
                </>
              )}
            </div>

            <Button className="mt-6 w-full" size="lg" onClick={() => navigate("/", { replace: true })}>
              Acessar minha conta
            </Button>
          </div>
        )}

        {step < 3 && (
          <p className="text-center text-[13px] text-muted-foreground mt-6">
            Já tem uma conta?{" "}
            <Link to="/login" className="text-primary font-bold hover:underline">Entrar</Link>
          </p>
        )}
      </div>
    </div>
  );
}
