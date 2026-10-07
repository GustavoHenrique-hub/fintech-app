## graphify

This project has a knowledge graph at about/graphify-out/ with god nodes, community structure, and cross-file relationships.

Rules:
- For codebase questions, first run `graphify query "<question>"` when about/graphify-out/graph.json exists. Use `graphify path "<A>" "<B>"` for relationships and `graphify explain "<concept>"` for focused concepts. These return a scoped subgraph, usually much smaller than GRAPH_REPORT.md or raw grep output.
- If about/graphify-out/wiki/index.md exists, use it for broad navigation instead of raw source browsing.
- Read about/graphify-out/GRAPH_REPORT.md only for broad architecture review or when query/path/explain do not surface enough context.
- After modifying code, run `graphify update .` with `GRAPHIFY_OUT=about/graphify-out` set to keep the graph current (AST-only, no API cost).
