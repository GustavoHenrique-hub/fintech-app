# Graph Report - C:/Users/02.2025/Documents/Codar/fintech-app  (2026-07-08)

## Corpus Check
- 296 files · ~81,656 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1486 nodes · 2695 edges · 148 communities (101 shown, 47 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 93 edges (avg confidence: 0.82)
- Token cost: 350,345 input · 0 output

## Community Hubs (Navigation)
- Community 0
- Community 1
- Community 2
- Community 3
- Community 4
- Community 5
- Community 6
- Community 7
- Community 8
- Community 9
- Community 10
- Community 11
- Community 12
- Community 13
- Community 14
- Community 15
- Community 16
- Community 17
- Community 18
- Community 19
- Community 20
- Community 21
- Community 22
- Community 23
- Community 24
- Community 25
- Community 26
- Community 27
- Community 28
- Community 29
- Community 30
- Community 31
- Community 32
- Community 33
- Community 34
- Community 35
- Community 36
- Community 37
- Community 38
- Community 39
- Community 40
- Community 41
- Community 42
- Community 43
- Community 44
- Community 45
- Community 46
- Community 47
- Community 48
- Community 49
- Community 50
- Community 51
- Community 52
- Community 53
- Community 54
- Community 55
- Community 56
- Community 57
- Community 58
- Community 59
- Community 60
- Community 61
- Community 62
- Community 63
- Community 64
- Community 65
- Community 67
- Community 68
- Community 69
- Community 70
- Community 71
- Community 72
- Community 73
- Community 74
- Community 75
- Community 77
- Community 78
- Community 79
- Community 80
- Community 81
- Community 82
- Community 83
- Community 84
- Community 85
- Community 86
- Community 87
- Community 88
- Community 92
- Community 94
- Community 96
- Community 106
- Community 107
- Community 108
- Community 109
- Community 110
- Community 111
- Community 112
- Community 113
- Community 114
- Community 115
- Community 116
- Community 117
- Community 118
- Community 119
- Community 122
- Community 123
- Community 131
- Community 132
- Community 133
- Community 134
- Community 135
- Community 136
- Community 138
- Community 139
- Community 140
- Community 141
- Community 142
- Community 144
- Community 145
- Community 146

## God Nodes (most connected - your core abstractions)
1. `apiUnwrap()` - 60 edges
2. `ContaFinanceiraRepositoryPort` - 46 edges
3. `ContaFinanceira` - 37 edges
4. `Transacao` - 37 edges
5. `TransacaoRepositoryPort` - 36 edges
6. `BeanConfig` - 35 edges
7. `api` - 27 edges
8. `ExtratoRepositoryPort` - 25 edges
9. `BancoRepositoryPort` - 24 edges
10. `Categoria` - 23 edges

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

## Communities (148 total, 47 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.06
Nodes (51): AuthContext, AuthProvider(), readUser(), api, apiUnwrap(), login(), buscarPorChave(), listar() (+43 more)

### Community 1 - "Community 1"
Cohesion: 0.05
Nodes (38): ContaFinanceiraController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping, PostMapping, RequestMapping (+30 more)

### Community 2 - "Community 2"
Cohesion: 0.07
Nodes (26): CriarNotificacaoRequestDTO, NotificacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+18 more)

### Community 3 - "Community 3"
Cohesion: 0.07
Nodes (25): ConsentimentoLgpdController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+17 more)

### Community 4 - "Community 4"
Cohesion: 0.05
Nodes (29): FinSight Overview Static HTML Mockup, Frontend index.html React Mount Point, Design System / Palette via CSS Variables, Frontend Project Structure (src/ layout), App(), BalanceChart(), buildPoints(), Overview.jsx Screen Component (+21 more)

### Community 5 - "Community 5"
Cohesion: 0.08
Nodes (22): MotivoCancelamentoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+14 more)

### Community 6 - "Community 6"
Cohesion: 0.09
Nodes (21): CancelarTransacaoRequestDTO, TransacaoCanceladaResponseDTO, ApiResponse, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+13 more)

### Community 7 - "Community 7"
Cohesion: 0.10
Nodes (11): Transactional, RemoverContaFinanceiraUseCase, CancelarTransacaoUseCase, ContaFinanceiraInvalidaException, ContaFinanceira, Getter, Setter, ContaFinanceiraRepositoryPort (+3 more)

### Community 8 - "Community 8"
Cohesion: 0.12
Nodes (16): SnapshotFinanceiroResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, RequestMapping, ResponseEntity, RestController (+8 more)

### Community 9 - "Community 9"
Cohesion: 0.08
Nodes (25): Getter, Setter, ProcessamentoJob, ProcessamentoJobRepositoryPort, StatusJob, aguardando_ia, cancelado, concluido (+17 more)

### Community 10 - "Community 10"
Cohesion: 0.08
Nodes (25): AuditoriaEvento, Getter, Setter, AuditoriaEventoRepositoryPort, AcaoAuditoria, API_KEY_GEN, CANCEL, CLASSIFY (+17 more)

### Community 11 - "Community 11"
Cohesion: 0.13
Nodes (6): BuscarTransacaoUseCase, EstornarTransacaoUseCase, Transactional, TransacaoInvalidaException, Transacao, TransacaoRepositoryPort

### Community 12 - "Community 12"
Cohesion: 0.14
Nodes (6): BuscarExtratoUseCase, ListarExtratosUseCase, Transactional, RemoverExtratoUseCase, Extrato, ExtratoRepositoryPort

### Community 13 - "Community 13"
Cohesion: 0.08
Nodes (25): API REST Endpoints Catalog, BeanConfig Manual Use Case Registration, Categoria Domain Entity, ContaFinanceira Domain Entity, CriarExtratoUseCase (hash anti-duplicate rule), Extrato Domain Entity, Hexagonal Architecture (Ports & Adapters), Transacao Domain Entity (+17 more)

### Community 14 - "Community 14"
Cohesion: 0.14
Nodes (14): mvnw script, clean(), die(), exec_maven(), set_java_home(), trim(), verbose(), Override (+6 more)

### Community 15 - "Community 15"
Cohesion: 0.11
Nodes (8): Categoria, Getter, Setter, TipoCategoria, AMBOS, GASTO, RECEITA, CodeGenerator

### Community 16 - "Community 16"
Cohesion: 0.20
Nodes (14): EstornarTransacaoRequestDTO, TransacaoRequestDTO, TransacaoResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping (+6 more)

### Community 17 - "Community 17"
Cohesion: 0.09
Nodes (22): dependencies, react, react-dom, devDependencies, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh (+14 more)

### Community 18 - "Community 18"
Cohesion: 0.19
Nodes (14): BancoController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+6 more)

### Community 19 - "Community 19"
Cohesion: 0.19
Nodes (7): BuscarUsuarioUseCase, CriarUsuarioUseCase, ListarUsuariosUseCase, BeanConfig, Bean, Configuration, UsuarioRepositoryPort

### Community 20 - "Community 20"
Cohesion: 0.22
Nodes (13): ExtratoRequestDTO, ExtratoResponseDTO, ExtratoController, ApiResponse, ApiResponses, GetMapping, Operation, PatchMapping (+5 more)

### Community 21 - "Community 21"
Cohesion: 0.23
Nodes (12): UsuarioRequestDTO, UsuarioResponseDTO, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping (+4 more)

### Community 22 - "Community 22"
Cohesion: 0.19
Nodes (4): CriarBancoUseCase, ListarBancosUseCase, Banco, BancoRepositoryPort

### Community 23 - "Community 23"
Cohesion: 0.11
Nodes (19): dependencies, axios, class-variance-authority, clsx, date-fns, imask, lucide-react, next-themes (+11 more)

### Community 24 - "Community 24"
Cohesion: 0.18
Nodes (11): AuthController, ApiResponse, Operation, PostMapping, RequestMapping, ResponseEntity, RestController, Tag (+3 more)

### Community 25 - "Community 25"
Cohesion: 0.25
Nodes (12): CategoriaController, ApiResponse, ApiResponses, GetMapping, Operation, PostMapping, RequestMapping, ResponseEntity (+4 more)

### Community 26 - "Community 26"
Cohesion: 0.19
Nodes (6): BuscarCategoriaUseCase, CriarCategoriaUseCase, CategoriaRepositoryPort, CriarCategoriaUseCaseTest, ExtendWith, Test

### Community 27 - "Community 27"
Cohesion: 0.12
Nodes (16): StatusExtrato, aguardando_ia, cancelado, classificando, concluido, erro_classificacao, erro_extracao, erro_formato (+8 more)

### Community 28 - "Community 28"
Cohesion: 0.12
Nodes (16): aliases, components, hooks, lib, ui, utils, rsc, $schema (+8 more)

### Community 29 - "Community 29"
Cohesion: 0.22
Nodes (6): BuscarBancoUseCase, BancoControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 30 - "Community 30"
Cohesion: 0.21
Nodes (6): ListarCategoriasUseCase, CategoriaControllerTest, BeforeEach, ExtendWith, MockMvc, Test

### Community 31 - "Community 31"
Cohesion: 0.15
Nodes (4): UsuarioInvalidoException, Getter, Setter, Usuario

### Community 32 - "Community 32"
Cohesion: 0.18
Nodes (5): ParserVersaoNaoEncontradaException, Getter, Setter, ParserVersao, ParserVersaoRepositoryPort

### Community 33 - "Community 33"
Cohesion: 0.12
Nodes (13): OrigemTransacao, api, manual, pdf, StatusRevisaoTransacao, ARQUIVADA, CLASSIFICADA, CONFIRMADA (+5 more)

### Community 34 - "Community 34"
Cohesion: 0.17
Nodes (8): formatCPF(), formatData(), formatDataCurta(), formatDataRelativa(), formatHora(), maskCPF(), moedaBR, toDate()

### Community 35 - "Community 35"
Cohesion: 0.20
Nodes (9): categorias, categoriasGasto, categoriasPorId, categoriasReceita, contas, extratos, snapshots, transacoes (+1 more)

### Community 36 - "Community 36"
Cohesion: 0.13
Nodes (15): devDependencies, autoprefixer, eslint, @eslint/js, eslint-plugin-react-hooks, eslint-plugin-react-refresh, globals, jsdom (+7 more)

### Community 37 - "Community 37"
Cohesion: 0.20
Nodes (5): ExtratoInvalidoException, Getter, Setter, ExtratoTest, Test

### Community 38 - "Community 38"
Cohesion: 0.14
Nodes (6): Transactional, Transactional, TipoTransacao, GASTO, RECEITA, TransacaoNaoEncontradaException

### Community 39 - "Community 39"
Cohesion: 0.28
Nodes (4): LoginUseCase, LogoutUseCase, SenhaEncoder, SessaoTokenRepositoryPort

### Community 40 - "Community 40"
Cohesion: 0.26
Nodes (4): ListarTransacoesUseCase, ExtendWith, Test, ListarTransacoesUseCaseTest

### Community 41 - "Community 41"
Cohesion: 0.22
Nodes (5): BancoInvalidoException, Getter, Setter, BancoTest, Test

### Community 42 - "Community 42"
Cohesion: 0.32
Nodes (5): BeforeEach, ExtendWith, MockMvc, Test, TransacaoControllerTest

### Community 43 - "Community 43"
Cohesion: 0.23
Nodes (3): CategoriaInvalidaException, CategoriaTest, Test

### Community 44 - "Community 44"
Cohesion: 0.29
Nodes (4): CriarExtratoUseCase, CriarExtratoUseCaseTest, ExtendWith, Test

### Community 45 - "Community 45"
Cohesion: 0.33
Nodes (5): GlobalExceptionHandler, ResponseEntity, CredenciaisInvalidasException, ExceptionHandler, RestControllerAdvice

### Community 46 - "Community 46"
Cohesion: 0.31
Nodes (10): actionTypes, dispatch(), genId(), listeners, memoryState, reducer(), scheduleRemoval(), toast() (+2 more)

### Community 47 - "Community 47"
Cohesion: 0.35
Nodes (9): fetchFromBin(), loadConfig(), loadLocal(), saveConfig(), saveLocal(), saveToBin(), SECTIONS, SetupScreen() (+1 more)

### Community 48 - "Community 48"
Cohesion: 0.24
Nodes (3): Getter, Setter, SessaoToken

### Community 49 - "Community 49"
Cohesion: 0.31
Nodes (4): CriarTransacaoUseCase, CriarTransacaoUseCaseTest, ExtendWith, Test

### Community 51 - "Community 51"
Cohesion: 0.40
Nodes (3): EstornarTransacaoUseCaseTest, ExtendWith, Test

### Community 52 - "Community 52"
Cohesion: 0.33
Nodes (4): CategoriaThreshold, Getter, Setter, CategoriaThresholdRepositoryPort

### Community 53 - "Community 53"
Cohesion: 0.28
Nodes (5): pathFromPoints(), points, renderIcons(), setupBalanceChart(), setupToggleSaldo()

### Community 54 - "Community 54"
Cohesion: 0.22
Nodes (8): Toast, ToastAction, ToastClose, ToastDescription, ToastTitle, toastVariants, ToastViewport, variantIcon

### Community 56 - "Community 56"
Cohesion: 0.25
Nodes (8): JWT Access+Refresh Strategy with jti Redis Blacklist, Complementary Technology Recommendations (Redis, RabbitMQ, PDFBox, Testcontainers, Flyway), JWT Access+Refresh Token HttpOnly Cookie Model, axios Client Config (src/services/api.js), Pre-Production Security Checklist, CSRF Double-Submit Token Mitigation, Rate Limiting Strategy (edge + Bucket4j), FinTech App Technology Stack

### Community 57 - "Community 57"
Cohesion: 0.25
Nodes (8): scripts, build, build:dev, dev, lint, preview, test, test:watch

### Community 58 - "Community 58"
Cohesion: 0.29
Nodes (4): ModalContent, ModalDescription, ModalOverlay, ModalTitle

### Community 59 - "Community 59"
Cohesion: 0.47
Nodes (4): Override, ServletInitializer, SpringApplicationBuilder, SpringBootServletInitializer

### Community 60 - "Community 60"
Cohesion: 0.53
Nodes (3): BuscarBancoUseCaseTest, ExtendWith, Test

### Community 61 - "Community 61"
Cohesion: 0.53
Nodes (3): CriarBancoUseCaseTest, ExtendWith, Test

### Community 62 - "Community 62"
Cohesion: 0.53
Nodes (3): ExtendWith, Test, ListarBancosUseCaseTest

### Community 63 - "Community 63"
Cohesion: 0.40
Nodes (6): develop Branch (staging integration), feature/* Branches, hotfix/* Branches, main Branch (production, Railway deploy), Full Git Workflow (feature to develop to main), Branch Strategy (main/develop/feature/hotfix)

### Community 64 - "Community 64"
Cohesion: 0.40
Nodes (5): ConfidenceBar(), getTone(), TONE_BAR_CLASSES, TONE_LABELS, TONE_TEXT_CLASSES

### Community 65 - "Community 65"
Cohesion: 0.40
Nodes (5): getLevel(), PasswordStrengthMeter(), RULES, TONE_BAR, TONE_TEXT

### Community 67 - "Community 67"
Cohesion: 0.47
Nodes (5): chave(), CORES, DOMINIOS, getBancoColor(), getBancoLogoUrl()

### Community 68 - "Community 68"
Cohesion: 0.60
Nodes (5): application.yaml Root Config (active profile, JPA), application-dev.yaml Datasource Config, application-hml.yaml Datasource Config, application-prd.yaml Datasource Config, Environment Profiles Table (dev/hml/prd)

### Community 69 - "Community 69"
Cohesion: 0.60
Nodes (3): FintechAppApplicationTests, Test, SpringBootTest

### Community 70 - "Community 70"
Cohesion: 0.40
Nodes (5): CPF AES-256/pgcrypto Encryption Recommendation, Post-Restore Checklist Query: usuarios demo user, RN-01 to RN-04: Usuario e Acesso Rules, usuarios Table Schema Recommendation, Usuario Business Rules (idade mínima, CPF imutável)

### Community 71 - "Community 71"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 73 - "Community 73"
Cohesion: 0.40
Nodes (3): STATUS_EXTRATO, STATUS_REVISAO, TONE_CLASSES

### Community 75 - "Community 75"
Cohesion: 0.67
Nodes (4): auditoria_eventos Immutable Monthly Partitioning, Immutable Audit Trail (auditoria_evento), LGPD Compliance Measures (masking, retention, DPO), AuditoriaEvento Business Rules (append-only)

### Community 77 - "Community 77"
Cohesion: 0.67
Nodes (3): filtros, normalize(), TransactionsScreen()

### Community 78 - "Community 78"
Cohesion: 1.00
Nodes (3): Adapters Layer (Controllers, Persistence), Application Layer (Use Cases), Domain Layer (Models, Ports, Exceptions)

## Knowledge Gaps
- **249 isolated node(s):** `points`, `com.enterprise.gustadev:fintech_app`, `CREATE`, `UPDATE`, `DELETE` (+244 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **47 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ContaFinanceiraRepositoryPort` connect `Community 7` to `Community 1`, `Community 11`, `Community 49`, `Community 19`, `Community 51`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Why does `TransacaoRepositoryPort` connect `Community 11` to `Community 7`, `Community 40`, `Community 49`, `Community 19`, `Community 51`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Why does `ExtratoRepositoryPort` connect `Community 12` to `Community 19`, `Community 44`, `Community 37`?**
  _High betweenness centrality (0.020) - this node is a cross-community bridge._
- **What connects `points`, `com.enterprise.gustadev:fintech_app`, `CREATE` to the rest of the system?**
  _272 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.057813911472448055 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.05251141552511415 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.07450980392156863 - nodes in this community are weakly interconnected._