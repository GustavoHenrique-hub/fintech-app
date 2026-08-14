# Extratos × N8N — onde cada coisa roda e como o encaminhamento acontece

Este documento descreve o caminho real do arquivo desde o botão "Importar extrato"
até as transações aparecerem na aba **Transações**, com os endereços que o código
usa hoje.

## Endereços (setup local)

| Peça | Endereço | Onde está definido |
|---|---|---|
| API (Spring Boot) | `http://localhost:8082` | `application.yaml` → `server.port: ${SERVER_PORT:8082}` |
| Frontend (Vite) | `http://localhost:3000` | `vite.config.js` → `server.port` |
| Proxy do front → API | `/api/*` → `http://localhost:8082` | `vite.config.js` → `server.proxy` |
| Base usada pelo axios | `VITE_API_URL` ou `/api` | `src/services/api.js` |
| N8N | `http://localhost:5678` | `n8n.webhook-url` (backend) |
| Webhook de entrada | `POST http://localhost:5678/webhook/extratos/processar` | `automacao/n8n/02-extratos-entrada-app.json` |

Ou seja: **a API local roda na porta 8082** e é para ela que o N8N precisa apontar
ao devolver o resultado (`FINTECH_API_URL`).

## O caminho completo

```
Tela de Extratos (front, :3000)
  └─ POST /extratos/upload            (multipart: usuarioId, contaId, arquivo)
       │
       ▼
  ExtratoController#upload → ImportarExtratoUseCase
       ├─ salva o arquivo, calcula o hash (dedup) e cria o Extrato
       ├─ PDF  → POST http://localhost:5678/webhook/extratos/processar
       │          (arquivo em base64 + X-Internal-Api-Key)   →  status: na_fila
       └─ CSV/TXT/XLS/XLSX → parser local, síncrono          →  status: pendente_revisao
                 (o PDF também cai aqui se o N8N não responder)
       │
       ▼
  N8N: 02-extratos-entrada-app → 01-extratos-core-ia (Claude extrai e classifica)
       ├─ PATCH http://localhost:8082/extratos/{id}/status     (extraindo, classificando...)
       └─ POST  http://localhost:8082/extratos/{id}/callback   (lançamentos + metadados)
       │
       ▼
  RegistrarResultadoExtratoUseCase
       ├─ cria as Transacao com statusRevisao = PENDENTE_REVISAO
       ├─ aplica os valores no saldo da conta
       └─ fecha os contadores do Extrato → status: pendente_revisao
       │
       ▼
  Modal "Revisar extrato" (abre sozinho após o upload e recarrega enquanto processa)
       └─ por lançamento: Gasto | Receita | Economias  (+ categoria opcional)
            PATCH /transacoes/{id}/{code}/revisar { destino, categoriaId, categoriaCode }
              · GASTO/RECEITA → CONFIRMADA → entra na aba Transações
              · ECONOMIA      → aporte em saldoEconomias, lançamento sai das listagens
```

## Configuração

### Backend (`application.yaml`, bloco `n8n`)

| Propriedade | Variável de ambiente | Default |
|---|---|---|
| `n8n.enabled` | `N8N_ENABLED` | `true` |
| `n8n.webhook-url` | `N8N_WEBHOOK_URL` | `http://localhost:5678/webhook/extratos/processar` |
| `n8n.internal-api-key` | `INTERNAL_API_KEY` | vazio |
| `n8n.callback-secret` | `N8N_CALLBACK_SECRET` | vazio |
| `n8n.timeout-ms` | `N8N_TIMEOUT_MS` | `10000` |

Com `INTERNAL_API_KEY`/`N8N_CALLBACK_SECRET` vazios, o backend **aceita** as chamadas
da automação sem verificar (com aviso no log) — conveniente em dev, inaceitável fora dele.

### N8N (variáveis de ambiente da instância)

| Variável | Valor no setup local |
|---|---|
| `FINTECH_API_URL` | `http://localhost:8082` |
| `INTERNAL_API_KEY` | o mesmo valor de `n8n.internal-api-key` |
| `N8N_CALLBACK_SECRET` | o mesmo valor de `n8n.callback-secret` |
| `CLAUDE_MODEL` | `claude-sonnet-5` (default do workflow) |

> Se o N8N rodar em container e o backend na máquina host, `FINTECH_API_URL` precisa
> ser `http://host.docker.internal:8082` — `localhost` dentro do container é o próprio
> container. Se os dois estiverem no mesmo compose, use o nome do serviço
> (`http://backend:8080`, que é o default do nó ⚙️ Config).

## Autenticação das rotas de callback

`/extratos/{id}/status` e `/extratos/{id}/callback` não passam pelo `SessaoTokenFilter`
(não existe usuário logado do outro lado). Elas são autenticadas por:

1. `X-Internal-Api-Key` — chave compartilhada, comparada em tempo constante;
2. `X-N8N-Signature: sha256=<hex>` — HMAC-SHA256 do **corpo bruto** com
   `n8n.callback-secret`. Por isso o controller lê o corpo como `String` e só depois
   desserializa: a assinatura precisa cobrir exatamente os bytes que trafegaram.

O callback é idempotente: se o extrato já saiu do processamento, um reenvio (o N8N
manda `X-Idempotency-Key`) é ignorado sem duplicar lançamentos.

## Testando sem o N8N

- `N8N_ENABLED=false` → todo PDF cai no parser local, o fluxo continua síncrono.
- Ou simule o callback direto na API:

```bash
curl -X POST http://localhost:8082/extratos/42/callback \
  -H "Content-Type: application/json" \
  -H "X-Internal-Api-Key: $INTERNAL_API_KEY" \
  -d '{
    "extratoId": 42, "extratoCode": "A1B2C3", "status": "pendente_revisao",
    "bancoDetectado": "Nubank", "periodoInicio": "2026-08-01", "periodoFim": "2026-08-31",
    "transacoes": [
      { "dataTransacao": "2026-08-05", "descricao": "SUPERMERCADO XYZ",
        "estabelecimento": "Supermercado XYZ", "valor": 150.50, "tipo": "GASTO",
        "categoriaSugerida": "alimentacao", "confiancaIa": 92 }
    ]
  }'
```

(sem `N8N_CALLBACK_SECRET` configurado a assinatura não é exigida — com ele, some o
header `X-N8N-Signature: sha256=<hmac do corpo>`.)
