# Graph Report - fintech-app  (2026-08-31)

## Corpus Check
- 347 files · ~116,120 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 2010 nodes · 4018 edges · 209 communities (130 shown, 79 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 184 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `afd80636`
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
- CriarTransacaoUseCaseTest.java
- Modal
- Servlet Initializer
- periodo.js
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
- VincularBancoModal.jsx
- Top Bar
- Nav Link
- Input Monetario
- Tooltip
- HELP
- Transactional
- GlobalExceptionHandler.java
- UsuarioController.java
- TransacaoCanceladaController.java
- Mock Mvc
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
- ResumoPeriodo
- SessaoTokenRepositoryPort
- CategoriaTest.java
- SessaoToken
- .detectar
- ExtratosScreen.jsx
- use-extratos.js
- MotivoCancelamentoController.java
- ImportarExtratoUseCase.java
- SessaoToken
- RegistrarMovimentacaoEconomiaUseCaseTest.java
- CategoriaControllerTest
- XlsExtratoParser
- CodeGenerator.java
- Categoria
- CategoriaThreshold
- ResultadoProcessamentoExtrato
- Bean
- BuscarContaFinanceiraUseCase
- StatusRevisaoTransacao
- RemoverContaFinanceiraUseCase
- .importarExtratoUseCase
- TipoTransacao
- .cancelarTransacaoUseCase
- .buscarBancoUseCase
- OverviewScreen.jsx
- .buscarSnapshotFinanceiroUseCase
- .criarNotificacaoUseCase
- TipoTransacao.java
- VincularBancoModal.jsx
- application-hml.yaml Datasource Config
- application-prd.yaml Datasource Config

## God Nodes (most connected - your core abstractions)
1. `ContaFinanceiraRepositoryPort` - 76 edges
2. `apiUnwrap()` - 74 edges
3. `ContaFinanceira` - 60 edges
4. `TransacaoRepositoryPort` - 60 edges
5. `Transacao` - 51 edges
6. `CategoriaRepositoryPort` - 49 edges
7. `ExtratoRepositoryPort` - 47 edges
8. `BeanConfig` - 44 edges
9. `ExtratoInvalidoException` - 40 edges
10. `Extrato` - 40 edges

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

## Communities (209 total, 79 thin omitted)

### Community 0 - "Banco Controller Test & Related"
Cohesion: 0.05
Nodes (38): BancoController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+30 more)

### Community 1 - "Categoria Repository Port & Related"
Cohesion: 0.38
Nodes (3): ConfirmarRevisaoTransacaoUseCaseTest, ExtendWith, Test

### Community 2 - "Index & Related"
Cohesion: 0.05
Nodes (60): AuthContext, AuthProvider(), readUser(), api, apiUnwrap(), login(), buscarPorChave(), listar() (+52 more)

### Community 4 - "Transacao Controller Test & Related"
Cohesion: 0.22
Nodes (7): CriarTransacaoUseCase, EstornarTransacaoUseCase, BeforeEach, ExtendWith, MockMvc, Test, TransacaoControllerTest

### Community 5 - "Mvnw & Related"
Cohesion: 0.18
Nodes (11): AuthController, ApiResponse, Operation, PostMapping, RequestMapping, ResponseEntity, RestController, Tag (+3 more)

### Community 6 - "Usuario Controller & Related"
Cohesion: 0.12
Nodes (8): AtualizarUsuarioUseCase, BuscarUsuarioUseCase, ListarUsuariosUseCase, UsuarioInvalidoException, Getter, Setter, Usuario, UsuarioRepositoryPort

### Community 7 - "Extrato Repository Port & Related"
Cohesion: 0.08
Nodes (13): DetectorFormatoExtrato, AtualizarStatusExtratoUseCase, Transactional, BuscarExtratoUseCase, CriarExtratoUseCase, ListarExtratosUseCase, Transactional, RemoverExtratoUseCase (+5 more)

### Community 8 - "Cadastro Page & Related"
Cohesion: 0.05
Nodes (31): FinSight Overview Static HTML Mockup, Frontend index.html React Mount Point, App(), BalanceChart(), buildPoints(), buildPointsDiario(), buildPointsMensal(), DIAS_LABEL (+23 more)

### Community 9 - "Transacao Cancelada Repository Port & Related"
Cohesion: 0.08
Nodes (23): CancelarTransacaoRequestDTO, TransacaoCanceladaResponseDTO, ApiResponse, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+15 more)

### Community 10 - "Consentimento Lgpd Repository Port & Related"
Cohesion: 0.08
Nodes (25): ConsentimentoLgpdController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+17 more)

### Community 11 - "Motivo Cancelamento Repository Port & Related"
Cohesion: 0.08
Nodes (22): MotivoCancelamentoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+14 more)

### Community 12 - "Notificacao Repository Port & Related"
Cohesion: 0.09
Nodes (21): CriarNotificacaoRequestDTO, NotificacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+13 more)

### Community 13 - "Transacao Repository Port & Related"
Cohesion: 0.21
Nodes (14): EconomiaRequestDTO, Schema, Schema, MovimentacaoEconomiaResponseDTO, EconomiaController, ApiResponse, ApiResponses, GetMapping (+6 more)

### Community 14 - "Snapshot Financeiro Repository Port & Related"
Cohesion: 0.12
Nodes (16): SnapshotFinanceiroResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+8 more)

### Community 15 - "Conta Financeira Repository Port & Related"
Cohesion: 0.16
Nodes (8): CatalogoCategoriasImportacao, Categoria, Getter, Setter, TipoCategoria, AMBOS, GASTO, RECEITA

### Community 16 - "Extrato Controller & Related"
Cohesion: 0.14
Nodes (19): AutenticacaoCallbackN8n, Component, Logger, AtualizarStatusExtratoRequestDTO, ExtratoRequestDTO, ExtratoResponseDTO, ExtratoController, ApiResponse (+11 more)

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
Cohesion: 0.17
Nodes (16): ContaFinanceiraController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping, PostMapping, RequestMapping (+8 more)

### Community 21 - "Package (Todo List)"
Cohesion: 0.09
Nodes (22): dependencies, react, react-dom, devDependencies, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh (+14 more)

### Community 22 - "Package (Fintech App)"
Cohesion: 0.11
Nodes (19): dependencies, axios, class-variance-authority, clsx, date-fns, imask, lucide-react, next-themes (+11 more)

### Community 23 - "Tipo Transacao & Related"
Cohesion: 0.15
Nodes (11): Component, Override, Pattern, SessaoTokenFilter, Getter, Setter, SessaoToken, FilterChain (+3 more)

### Community 24 - "Status Extrato"
Cohesion: 0.16
Nodes (10): ImportarExtratoUseCase, Transactional, CategoriaRepositoryPort, SolicitacaoProcessamentoExtrato, ArmazenamentoArquivoPort, ProcessamentoExtratoPort, ImportarExtratoUseCaseTest, BeforeEach (+2 more)

### Community 25 - "Components"
Cohesion: 0.12
Nodes (16): aliases, components, hooks, lib, ui, utils, rsc, $schema (+8 more)

### Community 26 - "Global Exception Handler & Related"
Cohesion: 0.20
Nodes (4): CsvExtratoParserTest, Test, Test, XlsExtratoParserTest

### Community 27 - "Parser Versao Repository Port"
Cohesion: 0.27
Nodes (4): Getter, Setter, ParserVersao, ParserVersaoRepositoryPort

### Community 28 - "Acao Auditoria"
Cohesion: 0.12
Nodes (15): AcaoAuditoria, API_KEY_GEN, CANCEL, CLASSIFY, CONFIRM, CREATE, DELETE, EXPORT (+7 more)

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
Cohesion: 0.11
Nodes (11): ListarMovimentacoesEconomiaUseCase, Transactional, RegistrarMovimentacaoEconomiaUseCase, MovimentacaoEconomiaInvalidaException, Getter, Setter, MovimentacaoEconomia, MovimentacaoEconomiaRepositoryPort (+3 more)

### Community 33 - "Conta Financeira Controller Test"
Cohesion: 0.29
Nodes (5): ContaFinanceiraControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 34 - "Auditoria Evento Repository Port"
Cohesion: 0.07
Nodes (29): API REST — Endpoints, Arquitetura Hexagonal (Ports & Adapters), BeanConfig, Camada de Persistência, Categorias `/categorias`, Como Executar Localmente, Como rodar, Configuração (+21 more)

### Community 35 - "ListarContasFinanceirasUseCase"
Cohesion: 0.17
Nodes (8): trim(), CsvExtratoParser, Component, Override, ExtratoParsingUtils, Pattern, LancamentoExtraido, CSVRecord

### Community 36 - "Use Toast (Hooks)"
Cohesion: 0.31
Nodes (10): actionTypes, dispatch(), genId(), listeners, memoryState, reducer(), scheduleRemoval(), toast() (+2 more)

### Community 37 - "Todo Fintech Cloud"
Cohesion: 0.35
Nodes (9): fetchFromBin(), loadConfig(), loadLocal(), saveConfig(), saveLocal(), saveToBin(), SECTIONS, SetupScreen() (+1 more)

### Community 38 - "Categoria Threshold Repository Port"
Cohesion: 0.21
Nodes (6): ListarCategoriasUseCase, CategoriaControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 39 - "Conta Financeira Test"
Cohesion: 0.16
Nodes (5): Transactional, ContaFinanceiraInvalidaException, ContaFinanceira, ContaFinanceiraTest, Test

### Community 40 - "Script"
Cohesion: 0.28
Nodes (5): pathFromPoints(), points, renderIcons(), setupBalanceChart(), setupToggleSaldo()

### Community 41 - "Toast"
Cohesion: 0.22
Nodes (8): Toast, ToastAction, ToastClose, ToastDescription, ToastTitle, toastVariants, ToastViewport, variantIcon

### Community 42 - "Status Revisao Transacao"
Cohesion: 0.23
Nodes (12): CategoriaController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+4 more)

### Community 43 - "Tipo Job"
Cohesion: 0.22
Nodes (7): CallbackExtratoRequestDTO, LancamentoDTO, LancamentoProcessado, ResultadoProcessamentoExtrato, ExtendWith, Test, RegistrarResultadoExtratoUseCaseTest

### Community 44 - "SECURITY (Docs)"
Cohesion: 0.67
Nodes (3): JWT Access+Refresh Strategy with jti Redis Blacklist, Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway), JWT Access+Refresh Token HttpOnly Cookie Model

### Community 45 - "Package (Fintech App) #3"
Cohesion: 0.25
Nodes (8): scripts, build, build:dev, dev, lint, preview, test, test:watch

### Community 46 - "Origem Auditoria"
Cohesion: 0.10
Nodes (19): 1. Pré-requisitos, 2. Criando o banco de dados, 3.1 Ambiente local do zero (desenvolvimento), 3.2 Restaurar backup completo legível, 3.3 Restaurar backup binário comprimido (produção), 3.4 Aplicar apenas o schema (CI/CD / migrations), 3. Cenários de uso, 4. Gerando novos dumps (+11 more)

### Community 47 - "CriarTransacaoUseCaseTest.java"
Cohesion: 0.13
Nodes (9): CriarContaFinanceiraUseCase, ListarContasFinanceirasUseCase, ContaFinanceiraRepositoryPort, CriarContaFinanceiraUseCaseTest, ExtendWith, Test, ExtendWith, Test (+1 more)

### Community 48 - "Modal"
Cohesion: 0.29
Nodes (4): ModalContent, ModalDescription, ModalOverlay, ModalTitle

### Community 49 - "Servlet Initializer"
Cohesion: 0.47
Nodes (4): Override, ServletInitializer, SpringApplicationBuilder, SpringBootServletInitializer

### Community 50 - "periodo.js"
Cohesion: 0.60
Nodes (3): getIntervaloPeriodo(), getIntervaloPeriodoAnterior(), intervaloDe()

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
Cohesion: 0.47
Nodes (5): chave(), CORES, DOMINIOS, getBancoColor(), getBancoLogoUrl()

### Community 56 - "Cancelado Por"
Cohesion: 0.12
Nodes (16): A) O source-map do Vite/React NÃO é uma falha de segurança, B) Onde NÃO colocar segredos, C) Modelo de autenticação recomendado, Comparativo, D) CSRF — mitigação quando se usa cookie, E) CORS — configuração Spring, F) Rate limiting, G) Sanitização e validação no backend (+8 more)

### Community 57 - "Origem Transacao"
Cohesion: 0.08
Nodes (23): Banco de dados, Desenvolver com hot-reload, Importar os workflows no n8n, Onde vai cada segredo, Os serviços, Pendência conhecida: HMAC do callback, Por que isso resolve o problema do n8n, Problemas comuns (+15 more)

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
Cohesion: 0.29
Nodes (5): AnalyticsScreen(), bucketsDiarios(), DIAS_LABEL, MES_ABREV, ranges

### Community 63 - "Status Badge"
Cohesion: 0.40
Nodes (3): STATUS_EXTRATO, STATUS_REVISAO, TONE_CLASSES

### Community 67 - "Transactions Screen"
Cohesion: 0.47
Nodes (4): algumFiltroAtivo(), FILTROS_PADRAO, normalize(), TransactionsScreen()

### Community 80 - "Extrato Request DTO"
Cohesion: 0.15
Nodes (12): 10. Usuario, 11. Regras transversais / agregadas, 1. ContaFinanceira, 2. Categoria e CategoriaThreshold, 3. Transacao, 4. TransacaoCancelada, 5. Extrato, 6. SnapshotFinanceiro (+4 more)

### Community 82 - "Notificacao"
Cohesion: 0.12
Nodes (7): BuscarTransacaoUseCase, Transactional, TransacaoInvalidaException, Getter, Setter, Transacao, TransacaoRepositoryPort

### Community 86 - "Top Bar"
Cohesion: 0.67
Nodes (3): normalize(), TELAS, TopBar()

### Community 101 - "HELP"
Cohesion: 0.40
Nodes (4): Getting Started, Guides, Maven Parent overrides, Reference Documentation

### Community 102 - "Transactional"
Cohesion: 0.42
Nodes (3): EstornarTransacaoUseCaseTest, ExtendWith, Test

### Community 103 - "GlobalExceptionHandler.java"
Cohesion: 0.34
Nodes (5): GlobalExceptionHandler, ResponseEntity, ExceptionHandler, MaxUploadSizeExceededException, RestControllerAdvice

### Community 104 - "UsuarioController.java"
Cohesion: 0.17
Nodes (16): AlterarSenhaRequestDTO, AtualizarUsuarioRequestDTO, UsuarioRequestDTO, UsuarioResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation (+8 more)

### Community 105 - "TransacaoCanceladaController.java"
Cohesion: 0.18
Nodes (11): ExtratoParser, Component, Override, Pattern, PdfExtratoParser, FormatoExtrato, CSV, PDF (+3 more)

### Community 106 - "Mock Mvc"
Cohesion: 0.48
Nodes (3): CriarExtratoUseCaseTest, ExtendWith, Test

### Community 148 - "ListarTransacoesUseCaseTest.java"
Cohesion: 0.10
Nodes (17): Transactional, StatusExtrato, aguardando_ia, cancelado, classificando, concluido, erro_classificacao, erro_extracao (+9 more)

### Community 149 - "IA no projeto — como e onde aplicar"
Cohesion: 0.11
Nodes (16): A pipeline: cascata, não "manda tudo pra IA", Custo e desempenho — cuidados práticos, IA no projeto — como e onde aplicar, Onde a IA entra hoje, Onde plugar no código, Prompt: o que enviar (e o que nunca enviar), Quando disparar a classificação, Resumo — o que fazer primeiro (+8 more)

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

### Community 179 - "ResumoPeriodo"
Cohesion: 0.25
Nodes (3): Getter, Setter, TransacaoNaoEncontradaException

### Community 180 - "SessaoTokenRepositoryPort"
Cohesion: 0.15
Nodes (12): 10. Armadilhas específicas deste projeto, 11. Variante: hot-reload no frontend, 1. O que o monorepo tem hoje, 2. Arquivos a criar, 3. Backend — `backend/fintech_app/Dockerfile`, 4. Frontend — `frontend/fintech_app/Dockerfile`, 5. `frontend/fintech_app/nginx.conf` — o substituto do proxy do Vite, 6. `docker-compose.yml` (raiz do monorepo) (+4 more)

### Community 181 - "CategoriaTest.java"
Cohesion: 0.17
Nodes (4): AlterarSenhaUseCase, CriarUsuarioUseCase, CredenciaisInvalidasException, SenhaEncoder

### Community 182 - "SessaoToken"
Cohesion: 0.29
Nodes (4): LoginUseCase, LogoutUseCase, Configuration, SessaoTokenRepositoryPort

### Community 183 - ".detectar"
Cohesion: 0.29
Nodes (4): CriarCategoriaUseCase, CriarCategoriaUseCaseTest, ExtendWith, Test

### Community 184 - "ExtratosScreen.jsx"
Cohesion: 0.60
Nodes (4): extensao(), EXTENSOES_ACEITAS, ExtratosScreen(), STATUS_EM_PROCESSAMENTO

### Community 187 - "MotivoCancelamentoController.java"
Cohesion: 0.67
Nodes (3): CAMPOS, EditarContatoModal(), touched()

### Community 188 - "ImportarExtratoUseCase.java"
Cohesion: 0.46
Nodes (3): CriarTransacaoUseCaseTest, ExtendWith, Test

### Community 189 - "SessaoToken"
Cohesion: 0.29
Nodes (4): ListarTransacoesUseCase, ExtendWith, Test, ListarTransacoesUseCaseTest

### Community 193 - "XlsExtratoParser"
Cohesion: 0.40
Nodes (4): DESTINOS, normTipo(), RevisarExtratoModal(), STATUS_EM_PROCESSAMENTO

### Community 194 - "CodeGenerator.java"
Cohesion: 0.35
Nodes (5): Component, Override, XlsExtratoParser, Cell, Row

### Community 195 - "Categoria"
Cohesion: 0.53
Nodes (4): Bean, Configuration, OpenApiConfig, OpenAPI

### Community 196 - "CategoriaThreshold"
Cohesion: 0.33
Nodes (4): CategoriaThreshold, Getter, Setter, CategoriaThresholdRepositoryPort

### Community 197 - "ResultadoProcessamentoExtrato"
Cohesion: 0.25
Nodes (3): ResumoPeriodoResponseDTO, BuscarResumoPeriodoUseCase, ResumoPeriodo

### Community 199 - "BuscarContaFinanceiraUseCase"
Cohesion: 0.29
Nodes (4): BuscarContaFinanceiraUseCase, BuscarContaFinanceiraUseCaseTest, ExtendWith, Test

### Community 200 - "StatusRevisaoTransacao"
Cohesion: 0.13
Nodes (14): Logger, RegistrarResultadoExtratoUseCase, OrigemTransacao, api, importado, manual, pdf, StatusRevisaoTransacao (+6 more)

### Community 201 - "RemoverContaFinanceiraUseCase"
Cohesion: 0.18
Nodes (9): TipoConta, cartao, corrente, dinheiro, investimento, poupanca, DeletarContaFinanceiraUseCaseTest, ExtendWith (+1 more)

### Community 202 - ".importarExtratoUseCase"
Cohesion: 0.22
Nodes (3): BuscarCategoriaUseCase, Transactional, CategoriaInvalidaException

### Community 203 - "TipoTransacao"
Cohesion: 0.42
Nodes (3): ExtendWith, Test, RegistrarMovimentacaoEconomiaUseCaseTest

### Community 204 - ".cancelarTransacaoUseCase"
Cohesion: 0.39
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 205 - ".buscarBancoUseCase"
Cohesion: 0.36
Nodes (4): AuditoriaEvento, Getter, Setter, AuditoriaEventoRepositoryPort

### Community 206 - "OverviewScreen.jsx"
Cohesion: 0.29
Nodes (5): RevisarTransacaoRequestDTO, DestinoRevisaoLancamento, ECONOMIA, GASTO, RECEITA

### Community 208 - ".criarNotificacaoUseCase"
Cohesion: 0.29
Nodes (6): OrigemAuditoria, api, bot_telegram, bot_whatsapp, sistema, web

### Community 209 - "TipoTransacao.java"
Cohesion: 0.27
Nodes (5): ConfirmarRevisaoTransacaoUseCase, Transactional, TipoTransacao, GASTO, RECEITA

## Knowledge Gaps
- **402 isolated node(s):** `📁 Estrutura do repositório`, `⚙ Pré-requisitos`, `Opção A — tudo em Docker (recomendado)`, `Opção B — rodar na máquina`, `🔑 Onde ficam as chaves e segredos` (+397 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **79 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ContaFinanceiraRepositoryPort` connect `CriarTransacaoUseCaseTest.java` to `Categoria Repository Port & Related`, `Bean Config`, `Transacao Controller Test & Related`, `Extrato Repository Port & Related`, `Transacao Cancelada Repository Port & Related`, `Conta Financeira Controller & Related`, `Status Extrato`, `Tipo Conta`, `Conta Financeira Test`, `Tipo Job`, `ResumoPeriodo`, `SessaoToken`, `ImportarExtratoUseCase.java`, `BuscarContaFinanceiraUseCase`, `StatusRevisaoTransacao`, `RemoverContaFinanceiraUseCase`, `TipoTransacao`, `TipoTransacao.java`, `Transactional`, `Mock Mvc`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Why does `TransacaoRepositoryPort` connect `Notificacao` to `Tipo Conta`, `Categoria Repository Port & Related`, `Bean Config`, `Transacao Controller Test & Related`, `ResultadoProcessamentoExtrato`, `Transactional`, `Extrato Repository Port & Related`, `StatusRevisaoTransacao`, `Transacao Cancelada Repository Port & Related`, `Tipo Job`, `TipoTransacao.java`, `ResumoPeriodo`, `SessaoToken`, `Status Extrato`, `ImportarExtratoUseCase.java`, `SessaoToken`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **Why does `ContaFinanceira` connect `Conta Financeira Test` to `Tipo Conta`, `Conta Financeira Controller Test`, `Categoria Repository Port & Related`, `Transactional`, `BuscarContaFinanceiraUseCase`, `StatusRevisaoTransacao`, `RemoverContaFinanceiraUseCase`, `Mock Mvc`, `TipoTransacao`, `Tipo Job`, `CriarTransacaoUseCaseTest.java`, `TipoTransacao.java`, `Notificacao`, `ResumoPeriodo`, `Conta Financeira Controller & Related`, `Status Extrato`, `ImportarExtratoUseCase.java`?**
  _High betweenness centrality (0.028) - this node is a cross-community bridge._
- **What connects `📁 Estrutura do repositório`, `⚙ Pré-requisitos`, `Opção A — tudo em Docker (recomendado)` to the rest of the system?**
  _433 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Banco Controller Test & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.05126050420168067 - nodes in this community are weakly interconnected._
- **Should `Index & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.05128779395296752 - nodes in this community are weakly interconnected._
- **Should `Bean Config` be split into smaller, more focused modules?**
  _Cohesion score 0.14245014245014245 - nodes in this community are weakly interconnected._