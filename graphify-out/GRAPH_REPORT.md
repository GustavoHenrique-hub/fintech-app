# Graph Report - fintech-app  (2026-10-07)

## Corpus Check
- 398 files · ~312,489 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 122 nodes · 165 edges · 11 communities (5 shown, 6 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 12 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `970293a8`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ContaBancaria
- Transacao
- Usuario
- ContaInvestimento
- Categoria
- 💸 FinTech App
- ContaPoupanca
- script.js
- ContaCorrente
- SaldoInsuficienteException
- CLAUDE.md

## God Nodes (most connected - your core abstractions)
1. `Usuario` - 19 edges
2. `ContaBancaria` - 18 edges
3. `Transacao` - 14 edges
4. `ContaInvestimento` - 11 edges
5. `💸 FinTech App` - 10 edges
6. `ContaPoupanca` - 9 edges
7. `TipoTransacao` - 9 edges
8. `Categoria` - 8 edges
9. `ContaCorrente` - 8 edges
10. `TipoCategoria` - 7 edges

## Surprising Connections (you probably didn't know these)
- `ContaBancaria` --references--> `Transacao`  [EXTRACTED]
  about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaBancaria.java → about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/Transacao.java
- `ContaBancaria` --references--> `Usuario`  [EXTRACTED]
  about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaBancaria.java → about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/Usuario.java
- `ContaCorrente` --inherits--> `ContaBancaria`  [EXTRACTED]
  about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaCorrente.java → about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaBancaria.java
- `ContaInvestimento` --inherits--> `ContaBancaria`  [EXTRACTED]
  about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaInvestimento.java → about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaBancaria.java
- `ContaPoupanca` --inherits--> `ContaBancaria`  [EXTRACTED]
  about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaPoupanca.java → about/Entregas FIAP/Atividade Heranca e Polimorfismo - RM570791 - 2026-09-08/FintechApp/src/com/fintech/ContaBancaria.java

## Import Cycles
- None detected.

## Communities (11 total, 6 thin omitted)

### Community 0 - "ContaBancaria"
Cohesion: 0.14
Nodes (4): ContaBancaria, Override, Main, Override

### Community 1 - "Transacao"
Cohesion: 0.14
Nodes (7): TipoTransacao, DEPOSITO, RENDIMENTO, SAQUE, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECEBIDA, Transacao

### Community 4 - "Categoria"
Cohesion: 0.22
Nodes (5): Categoria, Override, TipoCategoria, GASTO, RECEITA

### Community 5 - "💸 FinTech App"
Cohesion: 0.15
Nodes (12): 🌿 Estratégia de branches, 📁 Estrutura do repositório, 💸 FinTech App, 🔑 Onde ficam as chaves e segredos, Opção A — tudo em Docker (recomendado), Opção B — rodar na máquina, ⚙ Pré-requisitos, 🧪 Rodar os testes (+4 more)

### Community 7 - "script.js"
Cohesion: 0.28
Nodes (5): pathFromPoints(), points, renderIcons(), setupBalanceChart(), setupToggleSaldo()

## Knowledge Gaps
- **19 isolated node(s):** `graphify`, `📁 Estrutura do repositório`, `⚙ Pré-requisitos`, `Opção A — tudo em Docker (recomendado)`, `Opção B — rodar na máquina` (+14 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **6 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ContaBancaria` connect `ContaBancaria` to `Transacao`, `Usuario`, `ContaInvestimento`, `ContaPoupanca`, `ContaCorrente`?**
  _High betweenness centrality (0.296) - this node is a cross-community bridge._
- **Why does `Transacao` connect `Transacao` to `ContaBancaria`?**
  _High betweenness centrality (0.158) - this node is a cross-community bridge._
- **Why does `Usuario` connect `Usuario` to `ContaBancaria`, `ContaCorrente`, `ContaInvestimento`, `ContaPoupanca`?**
  _High betweenness centrality (0.152) - this node is a cross-community bridge._
- **What connects `graphify`, `📁 Estrutura do repositório`, `⚙ Pré-requisitos` to the rest of the system?**
  _19 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ContaBancaria` be split into smaller, more focused modules?**
  _Cohesion score 0.13725490196078433 - nodes in this community are weakly interconnected._
- **Should `Transacao` be split into smaller, more focused modules?**
  _Cohesion score 0.14166666666666666 - nodes in this community are weakly interconnected._