# Graph Report - fintech-app  (2026-09-29)

## Corpus Check
- 398 files · ~311,925 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 2443 nodes · 5153 edges · 212 communities (126 shown, 86 thin omitted)
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 429 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `aaf35370`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Banco Controller Test & Related
- Categoria Repository Port & Related
- Index & Related
- Bean Config
- Transacao Controller Test & Related
- Mvnw & Related
- Usuario Controller & Related
- Extrato Repository Port & Related
- Cadastro Page & Related
- Transacao Cancelada Repository Port & Related
- Consentimento Lgpd Repository Port & Related
- Motivo Cancelamento Repository Port & Related
- Notificacao Repository Port & Related
- Transacao Repository Port & Related
- Snapshot Financeiro Repository Port & Related
- Conta Financeira Repository Port & Related
- Extrato Controller & Related
- Status Job
- Transacao Controller & Related
- Backend Documentacao & Related
- Conta Financeira Controller & Related
- Package (Todo List)
- Package (Fintech App)
- Tipo Transacao & Related
- Status Extrato
- Components
- Global Exception Handler & Related
- Parser Versao Repository Port
- Acao Auditoria
- Format
- Categorias & Related
- Package (Fintech App) #2
- Tipo Conta
- Conta Financeira Controller Test
- Auditoria Evento Repository Port
- ListarContasFinanceirasUseCase
- Use Toast (Hooks)
- Todo Fintech Cloud
- Categoria Threshold Repository Port
- Conta Financeira Test
- Script
- Toast
- Status Revisao Transacao
- Tipo Job
- SECURITY (Docs)
- Package (Fintech App) #3
- Origem Auditoria
- Modal
- Servlet Initializer
- Estratégia De Branches.pdf
- Confidence Bar
- Password Strength Meter
- Banco Utils
- Cancelado Por
- Origem Transacao
- Application Dev.yaml & Related
- Fintech App Application Tests
- Finapp Guia Dump (Docs)
- Package (Fintech App) #4
- Analytics Screen
- Status Badge
- Fintech App Application
- SECURITY (Docs) #2
- Profile Screen
- Transactions Screen
- Backend Documentacao (Docs)
- Sonner
- Bottom Nav
- Estorno Transacao Screen
- Side Nav
- Button
- Combobox
- Date Range Picker
- Empty State
- Input Cpf
- Categoria Icones
- ContaSelecionadaContext.jsx
- Extrato Request DTO
- Notificacao
- Top Bar
- Nav Link
- Input Monetario
- Tooltip
- HELP
- Transactional
- GlobalExceptionHandler.java
- UsuarioController.java
- Application.yaml
- Backend Documentacao (Docs) #2
- Backend Documentacao (Docs) #3
- Finapp Guia Dump (Docs) #2
- Finapp Guia Dump (Docs) #3
- Finapp Guia Dump (Docs) #4
- Finapp Guia Dump (Docs) #5
- SECURITY (Docs) #3
- SECURITY (Docs) #4
- SECURITY (Docs) #5
- SECURITY (Docs) #6
- SECURITY (Docs) #7
- SECURITY (Docs) #8
- Placeholder.svg
- Robots.txt
- Ideias Regras De Negocio (Organizacao)
- Ideias Regras De Negocio (Organizacao) #2
- Ideias Regras De Negocio (Organizacao) #3
- Ideias Regras De Negocio (Organizacao) #4
- Ideias Regras De Negocio (Organizacao) #5
- Ideias Regras De Negocio (Organizacao) #6
- Favicon.svg
- Icons.svg
- Hero.png
- React.svg
- Vite.svg
- Pom.xml
- StatusRevisaoTransacao
- SessaoToken
- ListarTransacoesUseCaseTest.java
- IA no projeto — como e onde aplicar
- transacoes Table Schema Recommendation
- FinSight — Frontend
- categorias Table Schema (hierarquia pai/filho)
- React + Vite
- CLAUDE.md
- BeanConfig Manual Use Case Registration (PDF export)
- Domain Entities Table (PDF export)
- API REST Endpoints Catalog
- Application Layer (Use Cases)
- ContaFinanceira Domain Entity
- CriarExtratoUseCase (hash anti-duplicate rule)
- Domain Layer (Models, Ports, Exceptions)
- Hexagonal Architecture (Ports & Adapters)
- Environment Profiles Table (dev/hml/prd)
- Use Case Pattern (single executar method)
- CPF AES-256/pgcrypto Encryption Recommendation
- Immutable Audit Trail (auditoria_evento)
- axios Client Config (src/services/api.js)
- Pre-Production Security Checklist
- CSRF Double-Submit Token Mitigation
- LGPD Compliance Measures (masking, retention, DPO)
- Rate Limiting Strategy (edge + Bucket4j)
- Design System / Palette via CSS Variables
- Frontend Project Structure (src/ layout)
- tailwind.config.js Semantic Tokens
- AuditoriaEvento Business Rules (append-only)
- Todo-list Vite+React Boilerplate README
- .detectar
- ContaFinanceiraControllerTest
- ListarTransacoesUseCaseTest.java
- SessaoTokenRepositoryPort
- ExtratosScreen.jsx
- use-extratos.js
- MotivoCancelamentoController.java
- Stack local em Docker — backend + frontend + banco + n8n
- RegistrarMovimentacaoEconomiaUseCaseTest.java
- CategoriaControllerTest
- Categoria
- CategoriaThreshold
- Extratos × N8N — onde cada coisa roda e como o encaminhamento acontece
- StatusRevisaoTransacao
- TipoTransacao
- Transacao
- 2. E hospedar no Heroku?
- .criarNotificacaoUseCase
- TipoTransacao.java
- VincularBancoModal.jsx
- BuscarResumoPeriodoUseCase
- .revisarLote
- CategoriaInvalidaException
- application-hml.yaml Datasource Config
- application-prd.yaml Datasource Config
- GlobalExceptionHandler.java
- NewExtrato.js
- CatalogoCategoriasImportacao
- CategoriaTest
- GetCategoria.js
- GetContaFinanceira.js
- ProcessamentoExtrato.js
- GetNotificacao.js

## God Nodes (most connected - your core abstractions)
1. `ContaFinanceiraRepositoryPort` - 94 edges
2. `apiUnwrap()` - 82 edges
3. `TransacaoRepositoryPort` - 73 edges
4. `ContaFinanceira` - 68 edges
5. `ExtratoRepositoryPort` - 68 edges
6. `Extrato` - 63 edges
7. `UsuarioRepositoryPort` - 57 edges
8. `CategoriaRepositoryPort` - 56 edges
9. `ExtratoInvalidoException` - 54 edges
10. `BeanConfig` - 52 edges

## Surprising Connections (you probably didn't know these)
- `JWT Access+Refresh Strategy with jti Redis Blacklist` --semantically_similar_to--> `JWT Access+Refresh Token HttpOnly Cookie Model`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → frontend/fintech_app/docs/SECURITY.md
- `RN-15 to RN-16: Categorias Rules` --semantically_similar_to--> `Categoria Business Rules (padrão read-only)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md
- `RN-05 to RN-09: Transacoes Rules` --semantically_similar_to--> `Transacao Business Rules (imutabilidade, recorrência)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md
- `RN-10 to RN-14: Processamento de PDF e IA Rules` --semantically_similar_to--> `Extrato Business Rules (hash idempotency, score mínimo)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md
- `RN-01 to RN-04: Usuario e Acesso Rules` --semantically_similar_to--> `Usuario Business Rules (idade mínima, CPF imutável)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md

## Import Cycles
- None detected.

## Communities (212 total, 86 thin omitted)

### Community 0 - "Banco Controller Test & Related"
Cohesion: 0.05
Nodes (38): BancoController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+30 more)

### Community 1 - "Categoria Repository Port & Related"
Cohesion: 0.15
Nodes (15): apiUnwrap(), listarPorUsuario(), remover(), buscarPorChave(), listar(), buscarPorMes(), listarPorUsuario(), buscarPorChave() (+7 more)

### Community 2 - "Index & Related"
Cohesion: 0.11
Nodes (13): api, login(), criar(), criar(), registrar(), criar(), remover(), listarPorConta() (+5 more)

### Community 3 - "Bean Config"
Cohesion: 0.14
Nodes (3): BeanConfig, Bean, Configuration

### Community 4 - "Transacao Controller Test & Related"
Cohesion: 0.06
Nodes (14): ContaBancaria, Override, Transacao, Usuario, ContaCorrente, Override, Usuario, ContaInvestimento (+6 more)

### Community 5 - "Mvnw & Related"
Cohesion: 0.21
Nodes (12): ConsentimentoLgpdController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+4 more)

### Community 6 - "Usuario Controller & Related"
Cohesion: 0.15
Nodes (5): CategoriaThreshold, Getter, Setter, CategoriaThresholdRepositoryPort, CodeGenerator

### Community 7 - "Extrato Repository Port & Related"
Cohesion: 0.07
Nodes (12): Calibracao, DetectorFormatoExtrato, Transactional, Transactional, Transactional, Transactional, Categoria, Transactional (+4 more)

### Community 8 - "Cadastro Page & Related"
Cohesion: 0.08
Nodes (19): FinSight Overview Static HTML Mockup, Frontend index.html React Mount Point, App(), Overview.jsx Screen Component, src/index.css Design Tokens (HSL CSS vars), queryClient, CadastroPage(), inputClass() (+11 more)

### Community 9 - "Transacao Cancelada Repository Port & Related"
Cohesion: 0.09
Nodes (21): CancelarTransacaoRequestDTO, TransacaoCanceladaResponseDTO, ApiResponse, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+13 more)

### Community 10 - "Consentimento Lgpd Repository Port & Related"
Cohesion: 0.07
Nodes (18): ListarConsentimentosLgpdUseCase, RegistrarConsentimentoLgpdUseCase, BuscarUsuarioPorTelegramUseCase, Usuario, Resultado, ConsentimentoBotTelegram, ConsentimentoLgpdInvalidoException, ConsentimentoLgpd (+10 more)

### Community 11 - "Motivo Cancelamento Repository Port & Related"
Cohesion: 0.08
Nodes (22): MotivoCancelamentoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+14 more)

### Community 12 - "Notificacao Repository Port & Related"
Cohesion: 0.09
Nodes (21): CriarNotificacaoRequestDTO, NotificacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+13 more)

### Community 13 - "Transacao Repository Port & Related"
Cohesion: 0.06
Nodes (30): EconomiaRequestDTO, Schema, Schema, MovimentacaoEconomiaResponseDTO, EconomiaController, ApiResponse, ApiResponses, GetMapping (+22 more)

### Community 14 - "Snapshot Financeiro Repository Port & Related"
Cohesion: 0.11
Nodes (16): SnapshotFinanceiroResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+8 more)

### Community 15 - "Conta Financeira Repository Port & Related"
Cohesion: 0.20
Nodes (5): ParserVersaoNaoEncontradaException, Getter, Setter, ParserVersao, ParserVersaoRepositoryPort

### Community 16 - "Extrato Controller & Related"
Cohesion: 0.09
Nodes (27): AutenticacaoCallbackN8n, Component, Logger, AtualizarStatusExtratoRequestDTO, ExtratoRequestDTO, ExtratoResponseDTO, ExtratoController, ApiResponse (+19 more)

### Community 17 - "Status Job"
Cohesion: 0.08
Nodes (25): Getter, Setter, ProcessamentoJob, ProcessamentoJobRepositoryPort, StatusJob, aguardando_ia, cancelado, concluido (+17 more)

### Community 18 - "Transacao Controller & Related"
Cohesion: 0.21
Nodes (14): EstornarTransacaoRequestDTO, TransacaoRequestDTO, TransacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping (+6 more)

### Community 19 - "Backend Documentacao & Related"
Cohesion: 0.33
Nodes (6): Extrato Domain Entity, extratos Table (hash_arquivo anti-duplicata), N8N as WhatsApp/Telegram-to-API Middleware, regras_classificacao Table (IA learning rules), RN-10 to RN-14: Processamento de PDF e IA Rules, Extrato Business Rules (hash idempotency, score mínimo)

### Community 20 - "Conta Financeira Controller & Related"
Cohesion: 0.05
Nodes (42): ContaFinanceiraController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping, PostMapping, RequestMapping (+34 more)

### Community 21 - "Package (Todo List)"
Cohesion: 0.09
Nodes (22): dependencies, react, react-dom, devDependencies, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh (+14 more)

### Community 22 - "Package (Fintech App)"
Cohesion: 0.10
Nodes (21): dependencies, apexcharts, axios, class-variance-authority, clsx, date-fns, imask, lucide-react (+13 more)

### Community 24 - "Status Extrato"
Cohesion: 0.10
Nodes (16): CalibradorConfiancaIa, ListarExtratosUseCase, Logger, Transactional, RegistrarResultadoExtratoUseCase, ConfirmarRevisaoTransacaoUseCase, DesfazerRevisaoTransacaoUseCase, AprendizadoClassificacaoRepositoryPort (+8 more)

### Community 25 - "Components"
Cohesion: 0.12
Nodes (16): aliases, components, hooks, lib, ui, utils, rsc, $schema (+8 more)

### Community 26 - "Global Exception Handler & Related"
Cohesion: 0.17
Nodes (12): 3. O que foi implementado, 4. Pendências conhecidas, Churn do graphify no auto-commit, Contexto, HMAC do callback assinado com segredo vazio, Migrar o Core IA para o backend, n8n sem o Cloud: self-hosted + stack Docker local, O que foi verificado (+4 more)

### Community 28 - "Acao Auditoria"
Cohesion: 0.08
Nodes (25): AuditoriaEvento, Getter, Setter, AuditoriaEventoRepositoryPort, AcaoAuditoria, API_KEY_GEN, CANCEL, CLASSIFY (+17 more)

### Community 29 - "Format"
Cohesion: 0.17
Nodes (8): formatCPF(), formatData(), formatDataCurta(), formatDataRelativa(), formatHora(), maskCPF(), moedaBR, toDate()

### Community 30 - "Categorias & Related"
Cohesion: 0.20
Nodes (9): categorias, categoriasGasto, categoriasPorId, categoriasReceita, contas, extratos, snapshots, transacoes (+1 more)

### Community 31 - "Package (Fintech App) #2"
Cohesion: 0.13
Nodes (15): devDependencies, autoprefixer, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh, globals, jsdom (+7 more)

### Community 32 - "Tipo Conta"
Cohesion: 0.12
Nodes (14): upload(), bancoService, categoriaService, consentimentoService, contaFinanceiraService, economiaService, extratoService, motivoCancelamentoService (+6 more)

### Community 33 - "Conta Financeira Controller Test"
Cohesion: 0.23
Nodes (6): Transacao, ListarTransacoesUseCase, ExtendWith, Test, Transacao, ListarTransacoesUseCaseTest

### Community 34 - "Auditoria Evento Repository Port"
Cohesion: 0.07
Nodes (29): API REST — Endpoints, Arquitetura Hexagonal (Ports & Adapters), BeanConfig, Camada de Persistência, Categorias `/categorias`, Como Executar Localmente, Como rodar, Configuração (+21 more)

### Community 35 - "ListarContasFinanceirasUseCase"
Cohesion: 0.06
Nodes (29): mvnw script, clean(), die(), exec_maven(), set_java_home(), trim(), verbose(), ExtratoParsingUtils (+21 more)

### Community 36 - "Use Toast (Hooks)"
Cohesion: 0.31
Nodes (10): actionTypes, dispatch(), genId(), listeners, memoryState, reducer(), scheduleRemoval(), toast() (+2 more)

### Community 37 - "Todo Fintech Cloud"
Cohesion: 0.35
Nodes (9): fetchFromBin(), loadConfig(), loadLocal(), saveConfig(), saveLocal(), saveToBin(), SECTIONS, SetupScreen() (+1 more)

### Community 40 - "Script"
Cohesion: 0.28
Nodes (5): pathFromPoints(), points, renderIcons(), setupBalanceChart(), setupToggleSaldo()

### Community 41 - "Toast"
Cohesion: 0.22
Nodes (8): Toast, ToastAction, ToastClose, ToastDescription, ToastTitle, toastVariants, ToastViewport, variantIcon

### Community 42 - "Status Revisao Transacao"
Cohesion: 0.08
Nodes (28): CategoriaController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+20 more)

### Community 44 - "SECURITY (Docs)"
Cohesion: 0.67
Nodes (3): JWT Access+Refresh Strategy with jti Redis Blacklist, Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway), JWT Access+Refresh Token HttpOnly Cookie Model

### Community 45 - "Package (Fintech App) #3"
Cohesion: 0.25
Nodes (8): scripts, build, build:dev, dev, lint, preview, test, test:watch

### Community 46 - "Origem Auditoria"
Cohesion: 0.10
Nodes (19): 1. Pré-requisitos, 2. Criando o banco de dados, 3.1 Ambiente local do zero (desenvolvimento), 3.2 Restaurar backup completo legível, 3.3 Restaurar backup binário comprimido (produção), 3.4 Aplicar apenas o schema (CI/CD / migrations), 3. Cenários de uso, 4. Gerando novos dumps (+11 more)

### Community 48 - "Modal"
Cohesion: 0.29
Nodes (4): ModalContent, ModalDescription, ModalOverlay, ModalTitle

### Community 49 - "Servlet Initializer"
Cohesion: 0.47
Nodes (4): Override, ServletInitializer, SpringApplicationBuilder, SpringBootServletInitializer

### Community 51 - "Estratégia De Branches.pdf"
Cohesion: 0.50
Nodes (5): develop Branch (staging integration), feature/* Branches, hotfix/* Branches, main Branch (production, Railway deploy), Full Git Workflow (feature to develop to main)

### Community 52 - "Confidence Bar"
Cohesion: 0.40
Nodes (5): ConfidenceBar(), getTone(), TONE_BAR_CLASSES, TONE_LABELS, TONE_TEXT_CLASSES

### Community 53 - "Password Strength Meter"
Cohesion: 0.40
Nodes (5): getLevel(), PasswordStrengthMeter(), RULES, TONE_BAR, TONE_TEXT

### Community 55 - "Banco Utils"
Cohesion: 0.06
Nodes (35): BalanceChart(), DIAS_LABEL, isoLocal(), MESES_LABEL, montarSerie(), serieDiaria(), serieMensal(), CORES (+27 more)

### Community 56 - "Cancelado Por"
Cohesion: 0.12
Nodes (16): A) O source-map do Vite/React NÃO é uma falha de segurança, B) Onde NÃO colocar segredos, C) Modelo de autenticação recomendado, Comparativo, D) CSRF — mitigação quando se usa cookie, E) CORS — configuração Spring, F) Rate limiting, G) Sanitização e validação no backend (+8 more)

### Community 57 - "Origem Transacao"
Cohesion: 0.17
Nodes (12): 🌿 Estratégia de branches, 📁 Estrutura do repositório, 💸 FinTech App, 🔑 Onde ficam as chaves e segredos, Opção A — tudo em Docker (recomendado), Opção B — rodar na máquina, ⚙ Pré-requisitos, 🧪 Rodar os testes (+4 more)

### Community 59 - "Fintech App Application Tests"
Cohesion: 0.60
Nodes (3): FintechAppApplicationTests, Test, SpringBootTest

### Community 60 - "Finapp Guia Dump (Docs)"
Cohesion: 0.50
Nodes (4): Post-Restore Checklist Query: usuarios demo user, RN-01 to RN-04: Usuario e Acesso Rules, usuarios Table Schema Recommendation, Usuario Business Rules (idade mínima, CPF imutável)

### Community 61 - "Package (Fintech App) #4"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 62 - "Analytics Screen"
Cohesion: 0.06
Nodes (38): AuthController, ApiResponse, DeleteMapping, Operation, PostMapping, RequestMapping, ResponseEntity, RestController (+30 more)

### Community 63 - "Status Badge"
Cohesion: 0.40
Nodes (3): STATUS_EXTRATO, STATUS_REVISAO, TONE_CLASSES

### Community 67 - "Transactions Screen"
Cohesion: 0.11
Nodes (16): EstornarTransacaoModal(), MOTIVOS_RAPIDOS, ehConfirmado(), ehPendente(), FILTROS, RevisarExtratoModal(), STATUS_COM_ERRO, STATUS_EM_PROCESSAMENTO (+8 more)

### Community 71 - "Estorno Transacao Screen"
Cohesion: 0.40
Nodes (4): AuthContext, AuthProvider(), readUser(), authService

### Community 80 - "Extrato Request DTO"
Cohesion: 0.15
Nodes (12): 10. Usuario, 11. Regras transversais / agregadas, 1. ContaFinanceira, 2. Categoria e CategoriaThreshold, 3. Transacao, 4. TransacaoCancelada, 5. Extrato, 6. SnapshotFinanceiro (+4 more)

### Community 82 - "Notificacao"
Cohesion: 0.16
Nodes (8): CriarTransacaoUseCase, Transacao, TransacaoRepositoryPort, CriarTransacaoUseCaseTest, Categoria, ExtendWith, Test, TipoCategoria

### Community 86 - "Top Bar"
Cohesion: 0.67
Nodes (3): normalize(), TELAS, TopBar()

### Community 101 - "HELP"
Cohesion: 0.40
Nodes (4): Getting Started, Guides, Maven Parent overrides, Reference Documentation

### Community 102 - "Transactional"
Cohesion: 0.08
Nodes (16): AtualizarUsuarioUseCase, Usuario, BuscarUsuarioUseCase, Usuario, DesvincularTelegramUseCase, GerarCodigoVinculoTelegramUseCase, Usuario, ListarUsuariosUseCase (+8 more)

### Community 103 - "GlobalExceptionHandler.java"
Cohesion: 0.11
Nodes (12): CategoriaInvalidaException, Categoria, Getter, Setter, TipoCategoria, TipoCategoria, AMBOS, GASTO (+4 more)

### Community 104 - "UsuarioController.java"
Cohesion: 0.08
Nodes (23): AlterarSenhaRequestDTO, AtualizarUsuarioRequestDTO, UsuarioRequestDTO, Usuario, UsuarioResponseDTO, ApiResponse, ApiResponses, GetMapping (+15 more)

### Community 145 - "StatusRevisaoTransacao"
Cohesion: 0.10
Nodes (9): Transactional, CriarExtratoUseCase, EstornarTransacaoUseCase, CancelarTransacaoUseCase, ContaFinanceiraInvalidaException, ContaFinanceira, Getter, Setter (+1 more)

### Community 146 - "SessaoToken"
Cohesion: 0.50
Nodes (4): 1. Existe alternativa gratuita?, A opção "matar o n8n" (adiada, não descartada), Alternativas avaliadas e descartadas, O self-hosted é melhor para estes workflows, não só mais barato

### Community 148 - "ListarTransacoesUseCaseTest.java"
Cohesion: 0.11
Nodes (20): StatusExtrato, aguardando_ia, cancelado, classificando, concluido, erro_classificacao, erro_extracao, erro_formato (+12 more)

### Community 149 - "IA no projeto — como e onde aplicar"
Cohesion: 0.25
Nodes (8): A pipeline: cascata, não "manda tudo pra IA", Custo e desempenho — cuidados práticos, IA no projeto — como e onde aplicar, Onde a IA entra hoje, Onde plugar no código, Prompt: o que enviar (e o que nunca enviar), Quando disparar a classificação, Resumo — o que fazer primeiro

### Community 150 - "transacoes Table Schema Recommendation"
Cohesion: 0.40
Nodes (5): Transacao Domain Entity, Post-Restore Checklist Query: transacoes, RN-05 to RN-09: Transacoes Rules, transacoes Table Schema Recommendation, Transacao Business Rules (imutabilidade, recorrência)

### Community 151 - "FinSight — Frontend"
Cohesion: 0.40
Nodes (4): Estrutura, FinSight — Frontend, Paleta / Design system, Scripts

### Community 152 - "categorias Table Schema (hierarquia pai/filho)"
Cohesion: 0.50
Nodes (4): Categoria Domain Entity, categorias Table Schema (hierarquia pai/filho), RN-15 to RN-16: Categorias Rules, Categoria Business Rules (padrão read-only)

### Community 153 - "React + Vite"
Cohesion: 0.50
Nodes (3): Expanding the ESLint configuration, React Compiler, React + Vite

### Community 177 - ".detectar"
Cohesion: 0.14
Nodes (9): CsvExtratoParser, Component, Override, SolicitacaoProcessamentoExtrato, BeforeEach, ExtendWith, Test, ReenviarExtratoUseCaseTest (+1 more)

### Community 179 - "ListarTransacoesUseCaseTest.java"
Cohesion: 0.20
Nodes (8): BuscarTransacaoUseCase, Transacao, BeforeEach, ExtendWith, MockMvc, Test, Transacao, TransacaoControllerTest

### Community 180 - "SessaoTokenRepositoryPort"
Cohesion: 0.15
Nodes (12): 10. Armadilhas específicas deste projeto, 11. Variante: hot-reload no frontend, 1. O que o monorepo tem hoje, 2. Arquivos a criar, 3. Backend — `backend/fintech_app/Dockerfile`, 4. Frontend — `frontend/fintech_app/Dockerfile`, 5. `frontend/fintech_app/nginx.conf` — o substituto do proxy do Vite, 6. `docker-compose.yml` (raiz do monorepo) (+4 more)

### Community 184 - "ExtratosScreen.jsx"
Cohesion: 0.60
Nodes (4): extensao(), EXTENSOES_ACEITAS, ExtratosScreen(), STATUS_EM_PROCESSAMENTO

### Community 187 - "MotivoCancelamentoController.java"
Cohesion: 0.67
Nodes (3): CAMPOS, EditarContatoModal(), touched()

### Community 188 - "Stack local em Docker — backend + frontend + banco + n8n"
Cohesion: 0.17
Nodes (12): Banco de dados, Desenvolver com hot-reload, Importar os workflows no n8n, n8n Assistant: sandbox de código e web search, Onde vai cada segredo, Os serviços, Pendência conhecida: HMAC do callback, Por que isso resolve o problema do n8n (+4 more)

### Community 195 - "Categoria"
Cohesion: 0.31
Nodes (3): Categoria, Override, TipoCategoria

### Community 196 - "CategoriaThreshold"
Cohesion: 0.14
Nodes (6): AlterarSenhaUseCase, Usuario, CriarUsuarioUseCase, Usuario, CredenciaisInvalidasException, SenhaEncoder

### Community 197 - "Extratos × N8N — onde cada coisa roda e como o encaminhamento acontece"
Cohesion: 0.25
Nodes (8): Autenticação das rotas de callback, Backend (`application.yaml`, bloco `n8n`), Configuração, Endereços (setup local), Extratos × N8N — onde cada coisa roda e como o encaminhamento acontece, N8N (variáveis de ambiente da instância), O caminho completo, Testando sem o N8N

### Community 200 - "StatusRevisaoTransacao"
Cohesion: 0.09
Nodes (17): OrigemTransacao, api, importado, manual, pdf, StatusRevisaoTransacao, ARQUIVADA, CLASSIFICADA (+9 more)

### Community 203 - "TipoTransacao"
Cohesion: 0.29
Nodes (6): TipoTransacao, DEPOSITO, RENDIMENTO, SAQUE, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECEBIDA

### Community 204 - "Transacao"
Cohesion: 0.09
Nodes (16): CallbackExtratoRequestDTO, TipoTransacao, LancamentoDTO, TipoTransacao, Transacao, AprendizadoClassificacao, Getter, Setter (+8 more)

### Community 206 - "2. E hospedar no Heroku?"
Cohesion: 0.40
Nodes (5): 2. E hospedar no Heroku?, Não é grátis, O bloqueio real, Outras armadilhas, Se o objetivo for URL pública gratuita

### Community 208 - ".criarNotificacaoUseCase"
Cohesion: 0.50
Nodes (3): TipoCategoria, GASTO, RECEITA

### Community 209 - "TipoTransacao.java"
Cohesion: 0.07
Nodes (23): Transacao, Transacao, Transacao, Transactional, Transacao, Transactional, Transacao, Transactional (+15 more)

### Community 214 - "BuscarResumoPeriodoUseCase"
Cohesion: 0.23
Nodes (3): ResumoPeriodoResponseDTO, BuscarResumoPeriodoUseCase, ResumoPeriodo

### Community 217 - ".revisarLote"
Cohesion: 0.15
Nodes (12): Item, RevisarLoteRequestDTO, RevisarLoteResponseDTO, RevisarTransacaoRequestDTO, FalhaRevisao, ItemRevisao, Transactional, ResultadoRevisaoLote (+4 more)

### Community 218 - "CategoriaInvalidaException"
Cohesion: 0.20
Nodes (9): ExtratoParser, EncaminhamentoExtrato, ImportarExtratoUseCase, ReenviarExtratoUseCase, CategoriaRepositoryPort, Categoria, ArmazenamentoArquivoPort, ProcessamentoExtratoPort (+1 more)

### Community 226 - "GlobalExceptionHandler.java"
Cohesion: 0.06
Nodes (25): GlobalExceptionHandler, ResponseEntity, CodigoVinculoTelegramResponseDTO, Usuario, Usuario, TipoCategoria, VinculoTelegramInvalidoException, CodigoVinculoTelegram (+17 more)

### Community 231 - "CatalogoCategoriasImportacao"
Cohesion: 0.26
Nodes (7): CatalogoCategoriasImportacao, Categoria, TipoCategoria, TipoTransacao, Categoria, TipoCategoria, TipoTransacao

## Knowledge Gaps
- **430 isolated node(s):** `points`, `RECEITA`, `GASTO`, `DEPOSITO`, `SAQUE` (+425 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **86 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ContaFinanceiraRepositoryPort` connect `StatusRevisaoTransacao` to `GlobalExceptionHandler.java`, `Bean Config`, `Transactional`, `GlobalExceptionHandler.java`, `Consentimento Lgpd Repository Port & Related`, `Transacao`, `Transacao Repository Port & Related`, `.detectar`, `Notificacao`, `TipoTransacao.java`, `Conta Financeira Controller & Related`, `Status Extrato`, `CategoriaInvalidaException`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Why does `TransacaoRepositoryPort` connect `Notificacao` to `Conta Financeira Controller Test`, `GlobalExceptionHandler.java`, `Bean Config`, `GlobalExceptionHandler.java`, `StatusRevisaoTransacao`, `Transacao`, `StatusRevisaoTransacao`, `TipoTransacao.java`, `ListarTransacoesUseCaseTest.java`, `.detectar`, `BuscarResumoPeriodoUseCase`, `Status Extrato`, `CategoriaInvalidaException`?**
  _High betweenness centrality (0.037) - this node is a cross-community bridge._
- **Why does `CategoriaRepositoryPort` connect `CategoriaInvalidaException` to `GlobalExceptionHandler.java`, `Bean Config`, `CatalogoCategoriasImportacao`, `Extrato Repository Port & Related`, `GlobalExceptionHandler.java`, `Status Revisao Transacao`, `Transacao`, `.detectar`, `Notificacao`, `TipoTransacao.java`, `Status Extrato`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `points`, `RECEITA`, `GASTO` to the rest of the system?**
  _461 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Banco Controller Test & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.05134825014343087 - nodes in this community are weakly interconnected._
- **Should `Index & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.10826210826210826 - nodes in this community are weakly interconnected._
- **Should `Bean Config` be split into smaller, more focused modules?**
  _Cohesion score 0.14461538461538462 - nodes in this community are weakly interconnected._