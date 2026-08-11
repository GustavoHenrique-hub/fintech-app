# Graph Report - C:/Users/02.2025/Documents/Codar/fintech-app  (2026-07-13)

## Corpus Check
- 32 files · ~82,265 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1593 nodes · 2711 edges · 148 communities (92 shown, 56 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 65 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

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
- Canal Notificacao
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
- Tipo Consentimento Lgpd
- Modal
- Servlet Initializer
- Criar Conta Financeira Use Case Test
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
- Consentimento Lgpd Request DTO
- Extrato Request DTO
- Criar Notificacao Request DTO
- Estornar Transacao Request DTO
- Cancelar Transacao Request DTO
- Nav Link
- Input Monetario
- Tooltip
- HELP
- Transactional
- Bean
- Configuration
- Before Each
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
- README
- README #2

## God Nodes (most connected - your core abstractions)
1. `apiUnwrap()` - 60 edges
2. `Transacao` - 37 edges
3. `BeanConfig` - 35 edges
4. `ContaFinanceiraRepositoryPort` - 31 edges
5. `Extrato` - 31 edges
6. `ContaFinanceira` - 27 edges
7. `api` - 27 edges
8. `Categoria` - 23 edges
9. `TransacaoRepositoryPort` - 21 edges
10. `Banco` - 20 edges

## Surprising Connections (you probably didn't know these)
- `FinTech App Technology Stack` --semantically_similar_to--> `Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway)`  [INFERRED] [semantically similar]
  README.md → docs/resposta_final_cloud.pdf
- `Branch Strategy (main/develop/feature/hotfix)` --semantically_similar_to--> `Full Git Workflow (feature to develop to main)`  [INFERRED] [semantically similar]
  README.md → docs/estratégia_de_branches.pdf
- `JWT Access+Refresh Strategy with jti Redis Blacklist` --semantically_similar_to--> `JWT Access+Refresh Token HttpOnly Cookie Model`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → frontend/fintech_app/docs/SECURITY.md
- `RN-15 to RN-16: Categorias Rules` --semantically_similar_to--> `Categoria Business Rules (padrão read-only)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md
- `RN-05 to RN-09: Transacoes Rules` --semantically_similar_to--> `Transacao Business Rules (imutabilidade, recorrência)`  [INFERRED] [semantically similar]
  docs/resposta_final_cloud.pdf → organizacao/ideias-regras-de-negocio.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Immutable Audit Trail Pattern Across Docs** — docs_finapp_guia_dump_auditoria_eventos_partitioning, organizacao_ideias_regras_de_negocio_auditoriaevento_rules, frontend_fintech_app_docs_security_audit_trail, docs_backend_documentacao_auditoriaevento [INFERRED 0.85]
- **JWT Access+Refresh Authentication Design** — frontend_fintech_app_docs_security_auth_model, docs_resposta_final_cloud_jwt_strategy, readme_tech_stack [INFERRED 0.80]
- **Extrato Hash-Based Deduplication Pattern** — organizacao_ideias_regras_de_negocio_extrato_rules, docs_resposta_final_cloud_rn_pdf_ia, docs_backend_documentacao_criarextratousecase, docs_documenta__o_do_backend_criarextratousecase [INFERRED 0.85]

## Communities (148 total, 56 thin omitted)

### Community 0 - "Banco Controller Test & Related"
Cohesion: 0.05
Nodes (38): BancoController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+30 more)

### Community 1 - "Categoria Repository Port & Related"
Cohesion: 0.05
Nodes (35): CategoriaController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+27 more)

### Community 2 - "Index & Related"
Cohesion: 0.06
Nodes (51): AuthContext, AuthProvider(), readUser(), api, apiUnwrap(), login(), buscarPorChave(), listar() (+43 more)

### Community 3 - "Bean Config"
Cohesion: 0.06
Nodes (45): BeanConfig, BuscarExtratoUseCase, BuscarTransacaoUseCase, ConsentimentoLgpdRepositoryPort, ContaFinanceiraRepositoryPort, CriarTransacaoUseCase, EstornarTransacaoUseCase, ExtratoRepositoryPort (+37 more)

### Community 4 - "Transacao Controller Test & Related"
Cohesion: 0.06
Nodes (36): ContaFinanceira, Getter, Setter, BuscarTransacaoUseCase, CriarTransacaoUseCase, EstornarTransacaoUseCase, ExtendWith, ListarTransacoesUseCase (+28 more)

### Community 5 - "Mvnw & Related"
Cohesion: 0.07
Nodes (30): mvnw script, clean(), die(), exec_maven(), set_java_home(), trim(), verbose(), AuthController (+22 more)

### Community 6 - "Usuario Controller & Related"
Cohesion: 0.08
Nodes (22): UsuarioRequestDTO, UsuarioResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+14 more)

### Community 7 - "Extrato Repository Port & Related"
Cohesion: 0.07
Nodes (19): ExtratoResponseDTO, BuscarExtratoUseCase, ListarExtratosUseCase, Transactional, RemoverExtratoUseCase, ExtratoInvalidoException, Extrato, Getter (+11 more)

### Community 8 - "Cadastro Page & Related"
Cohesion: 0.05
Nodes (29): FinSight Overview Static HTML Mockup, Frontend index.html React Mount Point, Design System / Palette via CSS Variables, Frontend Project Structure (src/ layout), App(), BalanceChart(), buildPoints(), Overview.jsx Screen Component (+21 more)

### Community 9 - "Transacao Cancelada Repository Port & Related"
Cohesion: 0.08
Nodes (26): TransacaoCanceladaResponseDTO, ApiResponse, GetMapping, ListarTransacoesCanceladasUseCase, Operation, PostMapping, RequestMapping, ResponseEntity (+18 more)

### Community 10 - "Consentimento Lgpd Repository Port & Related"
Cohesion: 0.08
Nodes (24): ConsentimentoLgpdController, ApiResponse, ApiResponses, GetMapping, ListarConsentimentosLgpdUseCase, Operation, PostMapping, RequestMapping (+16 more)

### Community 11 - "Motivo Cancelamento Repository Port & Related"
Cohesion: 0.08
Nodes (22): MotivoCancelamentoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+14 more)

### Community 12 - "Notificacao Repository Port & Related"
Cohesion: 0.09
Nodes (23): NotificacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, ListarNotificacoesUseCase, Operation, PostMapping, RequestMapping (+15 more)

### Community 13 - "Transacao Repository Port & Related"
Cohesion: 0.10
Nodes (9): TransacaoResponseDTO, BuscarTransacaoUseCase, CriarTransacaoUseCase, Transactional, EstornarTransacaoUseCase, Transactional, ListarTransacoesUseCase, Transacao (+1 more)

### Community 14 - "Snapshot Financeiro Repository Port & Related"
Cohesion: 0.12
Nodes (16): SnapshotFinanceiroResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+8 more)

### Community 15 - "Conta Financeira Repository Port & Related"
Cohesion: 0.12
Nodes (10): BuscarContaFinanceiraUseCase, CriarContaFinanceiraUseCase, ListarContasFinanceirasUseCase, Transactional, RemoverContaFinanceiraUseCase, ContaFinanceira, ContaFinanceiraRepositoryPort, DeletarContaFinanceiraUseCaseTest (+2 more)

### Community 16 - "Extrato Controller & Related"
Cohesion: 0.17
Nodes (19): ExtratoController, ApiResponse, ApiResponses, BuscarExtratoUseCase, GetMapping, ListarExtratosUseCase, Operation, PatchMapping (+11 more)

### Community 17 - "Status Job"
Cohesion: 0.10
Nodes (20): Getter, Setter, ProcessamentoJob, ProcessamentoJobRepositoryPort, StatusJob, aguardando_ia, cancelado, concluido (+12 more)

### Community 18 - "Transacao Controller & Related"
Cohesion: 0.18
Nodes (18): TransacaoRequestDTO, ApiResponse, ApiResponses, BuscarTransacaoUseCase, CriarTransacaoUseCase, EstornarTransacaoUseCase, GetMapping, ListarTransacoesUseCase (+10 more)

### Community 19 - "Backend Documentacao & Related"
Cohesion: 0.08
Nodes (25): API REST Endpoints Catalog, BeanConfig Manual Use Case Registration, Categoria Domain Entity, ContaFinanceira Domain Entity, CriarExtratoUseCase (hash anti-duplicate rule), Extrato Domain Entity, Hexagonal Architecture (Ports & Adapters), Transacao Domain Entity (+17 more)

### Community 20 - "Conta Financeira Controller & Related"
Cohesion: 0.19
Nodes (15): ContaFinanceiraController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping, PostMapping, RequestMapping (+7 more)

### Community 21 - "Package (Todo List)"
Cohesion: 0.09
Nodes (22): dependencies, react, react-dom, devDependencies, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh (+14 more)

### Community 22 - "Package (Fintech App)"
Cohesion: 0.11
Nodes (19): dependencies, axios, class-variance-authority, clsx, date-fns, imask, lucide-react, next-themes (+11 more)

### Community 23 - "Tipo Transacao & Related"
Cohesion: 0.15
Nodes (9): ContaFinanceiraInvalidaException, Getter, Setter, TipoTransacao, GASTO, RECEITA, BuscarContaFinanceiraUseCaseTest, ExtendWith (+1 more)

### Community 24 - "Status Extrato"
Cohesion: 0.12
Nodes (16): StatusExtrato, aguardando_ia, cancelado, classificando, concluido, erro_classificacao, erro_extracao, erro_formato (+8 more)

### Community 25 - "Components"
Cohesion: 0.12
Nodes (16): aliases, components, hooks, lib, ui, utils, rsc, $schema (+8 more)

### Community 26 - "Global Exception Handler & Related"
Cohesion: 0.21
Nodes (7): GlobalExceptionHandler, ResponseEntity, CredenciaisInvalidasException, TransacaoInvalidaException, TransacaoNaoEncontradaException, ExceptionHandler, RestControllerAdvice

### Community 27 - "Parser Versao Repository Port"
Cohesion: 0.18
Nodes (5): ParserVersaoNaoEncontradaException, Getter, Setter, ParserVersao, ParserVersaoRepositoryPort

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
Cohesion: 0.19
Nodes (9): TipoConta, cartao, corrente, dinheiro, investimento, poupanca, ExtendWith, Test (+1 more)

### Community 33 - "Conta Financeira Controller Test"
Cohesion: 0.29
Nodes (5): ContaFinanceiraControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 34 - "Auditoria Evento Repository Port"
Cohesion: 0.30
Nodes (6): AcaoAuditoria, AuditoriaEvento, Getter, Setter, AuditoriaEventoRepositoryPort, OrigemAuditoria

### Community 35 - "Canal Notificacao"
Cohesion: 0.24
Nodes (8): Bean, Configuration, OpenApiConfig, CanalNotificacao, email, telegram, whatsapp, OpenAPI

### Community 36 - "Use Toast (Hooks)"
Cohesion: 0.31
Nodes (10): actionTypes, dispatch(), genId(), listeners, memoryState, reducer(), scheduleRemoval(), toast() (+2 more)

### Community 37 - "Todo Fintech Cloud"
Cohesion: 0.35
Nodes (9): fetchFromBin(), loadConfig(), loadLocal(), saveConfig(), saveLocal(), saveToBin(), SECTIONS, SetupScreen() (+1 more)

### Community 38 - "Categoria Threshold Repository Port"
Cohesion: 0.33
Nodes (4): CategoriaThreshold, Getter, Setter, CategoriaThresholdRepositoryPort

### Community 40 - "Script"
Cohesion: 0.28
Nodes (5): pathFromPoints(), points, renderIcons(), setupBalanceChart(), setupToggleSaldo()

### Community 41 - "Toast"
Cohesion: 0.22
Nodes (8): Toast, ToastAction, ToastClose, ToastDescription, ToastTitle, toastVariants, ToastViewport, variantIcon

### Community 42 - "Status Revisao Transacao"
Cohesion: 0.25
Nodes (7): StatusRevisaoTransacao, ARQUIVADA, CLASSIFICADA, CONFIRMADA, EXTRAIDA, IGNORADA, PENDENTE_REVISAO

### Community 43 - "Tipo Job"
Cohesion: 0.25
Nodes (7): TipoJob, anonimizacao, classificacao_ia, extracao_pdf, geracao_pdf, notificacao, snapshot

### Community 44 - "SECURITY (Docs)"
Cohesion: 0.25
Nodes (8): JWT Access+Refresh Strategy with jti Redis Blacklist, Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway), JWT Access+Refresh Token HttpOnly Cookie Model, axios Client Config (src/services/api.js), Pre-Production Security Checklist, CSRF Double-Submit Token Mitigation, Rate Limiting Strategy (edge + Bucket4j), FinTech App Technology Stack

### Community 45 - "Package (Fintech App) #3"
Cohesion: 0.25
Nodes (8): scripts, build, build:dev, dev, lint, preview, test, test:watch

### Community 46 - "Origem Auditoria"
Cohesion: 0.29
Nodes (6): OrigemAuditoria, api, bot_telegram, bot_whatsapp, sistema, web

### Community 47 - "Tipo Consentimento Lgpd"
Cohesion: 0.29
Nodes (6): TipoConsentimentoLgpd, armazenamento_extrato, bot_telegram, bot_whatsapp, tratamento_dados_financeiros, uso_ia

### Community 48 - "Modal"
Cohesion: 0.29
Nodes (4): ModalContent, ModalDescription, ModalOverlay, ModalTitle

### Community 49 - "Servlet Initializer"
Cohesion: 0.47
Nodes (4): Override, ServletInitializer, SpringApplicationBuilder, SpringBootServletInitializer

### Community 50 - "Criar Conta Financeira Use Case Test"
Cohesion: 0.53
Nodes (3): CriarContaFinanceiraUseCaseTest, ExtendWith, Test

### Community 51 - "Estratégia De Branches.pdf"
Cohesion: 0.40
Nodes (6): develop Branch (staging integration), feature/* Branches, hotfix/* Branches, main Branch (production, Railway deploy), Full Git Workflow (feature to develop to main), Branch Strategy (main/develop/feature/hotfix)

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
Cohesion: 0.40
Nodes (4): CanceladoPor, admin, sistema, usuario

### Community 57 - "Origem Transacao"
Cohesion: 0.40
Nodes (4): OrigemTransacao, api, manual, pdf

### Community 58 - "Application Dev.yaml & Related"
Cohesion: 0.60
Nodes (5): application.yaml Root Config (active profile, JPA), application-dev.yaml Datasource Config, application-hml.yaml Datasource Config, application-prd.yaml Datasource Config, Environment Profiles Table (dev/hml/prd)

### Community 59 - "Fintech App Application Tests"
Cohesion: 0.60
Nodes (3): FintechAppApplicationTests, Test, SpringBootTest

### Community 60 - "Finapp Guia Dump (Docs)"
Cohesion: 0.40
Nodes (5): CPF AES-256/pgcrypto Encryption Recommendation, Post-Restore Checklist Query: usuarios demo user, RN-01 to RN-04: Usuario e Acesso Rules, usuarios Table Schema Recommendation, Usuario Business Rules (idade mínima, CPF imutável)

### Community 61 - "Package (Fintech App) #4"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 63 - "Status Badge"
Cohesion: 0.40
Nodes (3): STATUS_EXTRATO, STATUS_REVISAO, TONE_CLASSES

### Community 65 - "SECURITY (Docs) #2"
Cohesion: 0.67
Nodes (4): auditoria_eventos Immutable Monthly Partitioning, Immutable Audit Trail (auditoria_evento), LGPD Compliance Measures (masking, retention, DPO), AuditoriaEvento Business Rules (append-only)

### Community 67 - "Transactions Screen"
Cohesion: 0.67
Nodes (3): filtros, normalize(), TransactionsScreen()

### Community 68 - "Backend Documentacao (Docs)"
Cohesion: 1.00
Nodes (3): Adapters Layer (Controllers, Persistence), Application Layer (Use Cases), Domain Layer (Models, Ports, Exceptions)

## Knowledge Gaps
- **254 isolated node(s):** `points`, `com.enterprise.gustadev:fintech_app`, `ConsentimentoLgpdRequestDTO`, `ExtratoRequestDTO`, `CriarNotificacaoRequestDTO` (+249 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **56 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `CriarExtratoUseCase` connect `Extrato Controller & Related` to `Bean Config`, `Extrato Repository Port & Related`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **Why does `CancelarTransacaoUseCase` connect `Transacao Cancelada Repository Port & Related` to `Bean Config`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **What connects `points`, `com.enterprise.gustadev:fintech_app`, `ConsentimentoLgpdRequestDTO` to the rest of the system?**
  _277 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Banco Controller Test & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.05126050420168067 - nodes in this community are weakly interconnected._
- **Should `Categoria Repository Port & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.051425213047311194 - nodes in this community are weakly interconnected._
- **Should `Index & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.057813911472448055 - nodes in this community are weakly interconnected._
- **Should `Bean Config` be split into smaller, more focused modules?**
  _Cohesion score 0.058747160012982795 - nodes in this community are weakly interconnected._