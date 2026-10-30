# Skills do projeto

Skills do Claude Code carregadas em toda sessão aberta neste repositório. Foram revisadas e adaptadas à stack
do projeto (Java/Spring, React/Vitest, n8n e fluxo de branches por Pull Request).

| Skill | Para quê | Origem | Licença |
|---|---|---|---|
| `n8n-skills` | Criar e depurar os workflows de `automacao/n8n/` | [haunchen/n8n-skills](https://github.com/haunchen/n8n-skills) v2.22.0 | MIT |
| `test-driven-development` | Escrever o teste antes da regra de negócio | [obra/superpowers](https://github.com/obra/superpowers) `2ce8aa1` | MIT |
| `systematic-debugging` | Achar a causa raiz antes de corrigir (inclui `root-cause-tracing`) | [obra/superpowers](https://github.com/obra/superpowers) `2ce8aa1` | MIT |
| `finishing-a-development-branch` | Encerrar uma branch com testes verdes e Pull Request | [obra/superpowers](https://github.com/obra/superpowers) `2ce8aa1` | MIT |
| `test-fixing` | Corrigir testes quebrados em grupos | [mhattingpete/claude-skills-marketplace](https://github.com/mhattingpete/claude-skills-marketplace) `b5b34bc` | Apache-2.0 |

## O que mudou em relação ao original

- **n8n-skills:** sem `resources/templates/`, porque os workflows de terceiros chamam APIs externas. Os guias e o
  `INDEX.md` ainda citam essa pasta: essas referências não valem mais.
- **finishing-a-development-branch:** sem a opção de merge local, porque este repositório só integra por Pull Request.
- **test-fixing:** comandos e exemplos trocados de `make`/`pytest` para Maven e Vitest.
- **systematic-debugging:** sem os arquivos de teste e histórico da própria skill.
- Todas, menos `test-fixing`, ganharam uma seção "Neste projeto" no fim do `SKILL.md`.

## Ao atualizar uma skill

Ela é uma cópia, então não recebe atualizações sozinha. Revise o conteúdo novo antes de copiar e reaplique as mudanças
acima: uma skill vira instrução para o agente e pode mandá-lo rodar comandos.
