# Graph Report - fintech-app  (2026-07-13)

## Corpus Check
- 279 files · ~88,552 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1601 nodes · 2840 edges · 179 communities (104 shown, 75 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 97 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `1aa41972`
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
- ListarTransacoesUseCaseTest.java
- ExtratoTest
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
- Branch Strategy (main/develop/feature/hotfix)
- FinTech App Technology Stack

## God Nodes (most connected - your core abstractions)
1. `apiUnwrap()` - 60 edges
2. `ContaFinanceiraRepositoryPort` - 52 edges
3. `ContaFinanceira` - 39 edges
4. `Transacao` - 38 edges
5. `TransacaoRepositoryPort` - 36 edges
6. `BeanConfig` - 35 edges
7. `UsuarioRepositoryPort` - 30 edges
8. `CategoriaRepositoryPort` - 27 edges
9. `api` - 27 edges
10. `Categoria` - 25 edges

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

## Communities (179 total, 75 thin omitted)

### Community 0 - "Banco Controller Test & Related"
Cohesion: 0.05
Nodes (38): BancoController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+30 more)

### Community 1 - "Categoria Repository Port & Related"
Cohesion: 0.06
Nodes (34): CategoriaController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+26 more)

### Community 2 - "Index & Related"
Cohesion: 0.06
Nodes (51): AuthContext, AuthProvider(), readUser(), api, apiUnwrap(), login(), buscarPorChave(), listar() (+43 more)

### Community 4 - "Transacao Controller Test & Related"
Cohesion: 0.17
Nodes (13): RequestMapping, RestController, Tag, TransacaoController, BuscarTransacaoUseCase, CriarTransacaoUseCase, EstornarTransacaoUseCase, ListarTransacoesUseCase (+5 more)

### Community 5 - "Mvnw & Related"
Cohesion: 0.06
Nodes (32): mvnw script, clean(), die(), exec_maven(), set_java_home(), trim(), verbose(), AuthController (+24 more)

### Community 6 - "Usuario Controller & Related"
Cohesion: 0.13
Nodes (7): BuscarUsuarioUseCase, ListarUsuariosUseCase, UsuarioInvalidoException, Getter, Setter, Usuario, UsuarioRepositoryPort

### Community 7 - "Extrato Repository Port & Related"
Cohesion: 0.15
Nodes (6): BuscarExtratoUseCase, ListarExtratosUseCase, Extrato, Getter, Setter, ExtratoRepositoryPort

### Community 8 - "Cadastro Page & Related"
Cohesion: 0.06
Nodes (25): FinSight Overview Static HTML Mockup, Frontend index.html React Mount Point, App(), BalanceChart(), buildPoints(), Overview.jsx Screen Component, ranges, TIPO_LABEL (+17 more)

### Community 9 - "Transacao Cancelada Repository Port & Related"
Cohesion: 0.09
Nodes (22): CancelarTransacaoRequestDTO, TransacaoCanceladaResponseDTO, ApiResponse, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+14 more)

### Community 10 - "Consentimento Lgpd Repository Port & Related"
Cohesion: 0.15
Nodes (7): ListarConsentimentosLgpdUseCase, RegistrarConsentimentoLgpdUseCase, ConsentimentoLgpdInvalidoException, ConsentimentoLgpd, Getter, Setter, ConsentimentoLgpdRepositoryPort

### Community 11 - "Motivo Cancelamento Repository Port & Related"
Cohesion: 0.08
Nodes (22): MotivoCancelamentoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+14 more)

### Community 12 - "Notificacao Repository Port & Related"
Cohesion: 0.09
Nodes (21): CriarNotificacaoRequestDTO, NotificacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+13 more)

### Community 13 - "Transacao Repository Port & Related"
Cohesion: 0.16
Nodes (6): Transactional, ContaFinanceiraInvalidaException, Getter, Setter, Transacao, TransacaoRepositoryPort

### Community 14 - "Snapshot Financeiro Repository Port & Related"
Cohesion: 0.12
Nodes (16): SnapshotFinanceiroResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+8 more)

### Community 15 - "Conta Financeira Repository Port & Related"
Cohesion: 0.10
Nodes (12): ContaFinanceira, Getter, Setter, TipoConta, cartao, corrente, dinheiro, investimento (+4 more)

### Community 16 - "Extrato Controller & Related"
Cohesion: 0.20
Nodes (14): ExtratoRequestDTO, ExtratoResponseDTO, ExtratoController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping (+6 more)

### Community 17 - "Status Job"
Cohesion: 0.13
Nodes (14): StatusJob, aguardando_ia, cancelado, concluido, dead_letter, enfileirado, falha_ia, falha_parser (+6 more)

### Community 18 - "Transacao Controller & Related"
Cohesion: 0.17
Nodes (10): EstornarTransacaoRequestDTO, TransacaoRequestDTO, TransacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping (+2 more)

### Community 19 - "Backend Documentacao & Related"
Cohesion: 0.33
Nodes (6): Extrato Domain Entity, extratos Table (hash_arquivo anti-duplicata), N8N as WhatsApp/Telegram-to-API Middleware, regras_classificacao Table (IA learning rules), RN-10 to RN-14: Processamento de PDF e IA Rules, Extrato Business Rules (hash idempotency, score mínimo)

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
Cohesion: 0.25
Nodes (4): BuscarContaFinanceiraUseCase, BuscarContaFinanceiraUseCaseTest, ExtendWith, Test

### Community 24 - "Status Extrato"
Cohesion: 0.12
Nodes (16): StatusExtrato, aguardando_ia, cancelado, classificando, concluido, erro_classificacao, erro_extracao, erro_formato (+8 more)

### Community 25 - "Components"
Cohesion: 0.12
Nodes (16): aliases, components, hooks, lib, ui, utils, rsc, $schema (+8 more)

### Community 26 - "Global Exception Handler & Related"
Cohesion: 0.20
Nodes (6): GlobalExceptionHandler, ResponseEntity, TransacaoInvalidaException, TransacaoNaoEncontradaException, ExceptionHandler, RestControllerAdvice

### Community 27 - "Parser Versao Repository Port"
Cohesion: 0.20
Nodes (5): ParserVersaoNaoEncontradaException, Getter, Setter, ParserVersao, ParserVersaoRepositoryPort

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
Cohesion: 0.29
Nodes (4): ListarContasFinanceirasUseCase, ExtendWith, Test, ListarContasFinanceirasUseCaseTest

### Community 33 - "Conta Financeira Controller Test"
Cohesion: 0.29
Nodes (5): ContaFinanceiraControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 34 - "Auditoria Evento Repository Port"
Cohesion: 0.07
Nodes (29): API REST — Endpoints, Arquitetura Hexagonal (Ports & Adapters), BeanConfig, Camada de Persistência, Categorias `/categorias`, Como Executar Localmente, Como rodar, Configuração (+21 more)

### Community 35 - "Canal Notificacao"
Cohesion: 0.15
Nodes (17): UsuarioRequestDTO, UsuarioResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+9 more)

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
Cohesion: 0.13
Nodes (11): OrigemTransacao, api, manual, pdf, StatusRevisaoTransacao, ARQUIVADA, CLASSIFICADA, CONFIRMADA (+3 more)

### Community 43 - "Tipo Job"
Cohesion: 0.25
Nodes (7): TipoJob, anonimizacao, classificacao_ia, extracao_pdf, geracao_pdf, notificacao, snapshot

### Community 44 - "SECURITY (Docs)"
Cohesion: 0.67
Nodes (3): JWT Access+Refresh Strategy with jti Redis Blacklist, Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway), JWT Access+Refresh Token HttpOnly Cookie Model

### Community 45 - "Package (Fintech App) #3"
Cohesion: 0.25
Nodes (8): scripts, build, build:dev, dev, lint, preview, test, test:watch

### Community 46 - "Origem Auditoria"
Cohesion: 0.10
Nodes (19): 1. Pré-requisitos, 2. Criando o banco de dados, 3.1 Ambiente local do zero (desenvolvimento), 3.2 Restaurar backup completo legível, 3.3 Restaurar backup binário comprimido (produção), 3.4 Aplicar apenas o schema (CI/CD / migrations), 3. Cenários de uso, 4. Gerando novos dumps (+11 more)

### Community 47 - "Tipo Consentimento Lgpd"
Cohesion: 0.25
Nodes (6): TipoConsentimentoLgpd, armazenamento_extrato, bot_telegram, bot_whatsapp, tratamento_dados_financeiros, uso_ia

### Community 48 - "Modal"
Cohesion: 0.29
Nodes (4): ModalContent, ModalDescription, ModalOverlay, ModalTitle

### Community 49 - "Servlet Initializer"
Cohesion: 0.47
Nodes (4): Override, ServletInitializer, SpringApplicationBuilder, SpringBootServletInitializer

### Community 50 - "Criar Conta Financeira Use Case Test"
Cohesion: 0.35
Nodes (5): CriarContaFinanceiraUseCase, ContaFinanceiraRepositoryPort, CriarContaFinanceiraUseCaseTest, ExtendWith, Test

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
Cohesion: 0.13
Nodes (14): 1. Clonar o repositório, 2. Configurar variáveis de ambiente, 3. Subir a infraestrutura, 4. Rodar o backend, 5. Rodar o frontend, 🌿 Estratégia de branches, 📁 Estrutura do repositório, 💸 FinTech App (+6 more)

### Community 58 - "Application Dev.yaml & Related"
Cohesion: 0.50
Nodes (4): application.yaml Root Config (active profile, JPA), application-dev.yaml Datasource Config, application-hml.yaml Datasource Config, application-prd.yaml Datasource Config

### Community 59 - "Fintech App Application Tests"
Cohesion: 0.60
Nodes (3): FintechAppApplicationTests, Test, SpringBootTest

### Community 60 - "Finapp Guia Dump (Docs)"
Cohesion: 0.50
Nodes (4): Post-Restore Checklist Query: usuarios demo user, RN-01 to RN-04: Usuario e Acesso Rules, usuarios Table Schema Recommendation, Usuario Business Rules (idade mínima, CPF imutável)

### Community 61 - "Package (Fintech App) #4"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 63 - "Status Badge"
Cohesion: 0.40
Nodes (3): STATUS_EXTRATO, STATUS_REVISAO, TONE_CLASSES

### Community 67 - "Transactions Screen"
Cohesion: 0.67
Nodes (3): filtros, normalize(), TransactionsScreen()

### Community 79 - "Consentimento Lgpd Request DTO"
Cohesion: 0.19
Nodes (12): ConsentimentoLgpdController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+4 more)

### Community 80 - "Extrato Request DTO"
Cohesion: 0.15
Nodes (12): 10. Usuario, 11. Regras transversais / agregadas, 1. ContaFinanceira, 2. Categoria e CategoriaThreshold, 3. Transacao, 4. TransacaoCancelada, 5. Extrato, 6. SnapshotFinanceiro (+4 more)

### Community 81 - "Criar Notificacao Request DTO"
Cohesion: 0.29
Nodes (5): Transactional, RemoverContaFinanceiraUseCase, DeletarContaFinanceiraUseCaseTest, ExtendWith, Test

### Community 82 - "Estornar Transacao Request DTO"
Cohesion: 0.33
Nodes (4): Getter, Setter, ProcessamentoJob, ProcessamentoJobRepositoryPort

### Community 83 - "Cancelar Transacao Request DTO"
Cohesion: 0.40
Nodes (3): EstornarTransacaoUseCaseTest, ExtendWith, Test

### Community 101 - "HELP"
Cohesion: 0.40
Nodes (4): Getting Started, Guides, Maven Parent overrides, Reference Documentation

### Community 102 - "Transactional"
Cohesion: 0.12
Nodes (7): Transactional, Transactional, TipoTransacao, GASTO, RECEITA, Test, TransacaoTest

### Community 103 - "Bean"
Cohesion: 0.25
Nodes (3): Transactional, RemoverExtratoUseCase, ExtratoInvalidoException

### Community 104 - "Configuration"
Cohesion: 0.27
Nodes (3): CriarUsuarioUseCase, Configuration, SenhaEncoder

### Community 106 - "Mock Mvc"
Cohesion: 0.44
Nodes (3): CriarExtratoUseCaseTest, ExtendWith, Test

### Community 148 - "ListarTransacoesUseCaseTest.java"
Cohesion: 0.52
Nodes (3): ExtendWith, Test, ListarTransacoesUseCaseTest

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

## Knowledge Gaps
- **352 isolated node(s):** `points`, `com.enterprise.gustadev:fintech_app`, `CREATE`, `UPDATE`, `DELETE` (+347 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **75 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ContaFinanceiraRepositoryPort` connect `Criar Conta Financeira Use Case Test` to `Tipo Conta`, `Categoria Repository Port & Related`, `Bean Config`, `Transacao Controller Test & Related`, `Bean`, `Extrato Repository Port & Related`, `Transacao Cancelada Repository Port & Related`, `Configuration`, `Mock Mvc`, `Transacao Repository Port & Related`, `Conta Financeira Repository Port & Related`, `Extrato Controller & Related`, `Criar Notificacao Request DTO`, `Cancelar Transacao Request DTO`, `Tipo Transacao & Related`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **Why does `ContaFinanceira` connect `Conta Financeira Repository Port & Related` to `Tipo Conta`, `Conta Financeira Controller Test`, `Transactional`, `Status Revisao Transacao`, `Mock Mvc`, `Transacao Repository Port & Related`, `Criar Notificacao Request DTO`, `Criar Conta Financeira Use Case Test`, `Cancelar Transacao Request DTO`, `Conta Financeira Controller & Related`, `Tipo Transacao & Related`?**
  _High betweenness centrality (0.030) - this node is a cross-community bridge._
- **Why does `TransacaoRepositoryPort` connect `Transacao Repository Port & Related` to `Categoria Repository Port & Related`, `Bean Config`, `Transacao Controller Test & Related`, `Configuration`, `Transacao Cancelada Repository Port & Related`, `Conta Financeira Repository Port & Related`, `Cancelar Transacao Request DTO`, `ListarTransacoesUseCaseTest.java`?**
  _High betweenness centrality (0.020) - this node is a cross-community bridge._
- **What connects `points`, `com.enterprise.gustadev:fintech_app`, `CREATE` to the rest of the system?**
  _384 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Banco Controller Test & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.0506155950752394 - nodes in this community are weakly interconnected._
- **Should `Categoria Repository Port & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.055501460564751706 - nodes in this community are weakly interconnected._
- **Should `Index & Related` be split into smaller, more focused modules?**
  _Cohesion score 0.057813911472448055 - nodes in this community are weakly interconnected._