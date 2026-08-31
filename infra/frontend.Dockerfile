# ==============================================================================
#  Frontend — React 18 + Vite, servido em produção por nginx.
#
#  Build em 2 estágios:
#    1) node  → npm ci + vite build  → dist/
#    2) nginx → serve dist/ e faz proxy de /api para o backend
#
#  Por que nginx e não `npm run dev`: em container o dev server do Vite é lento,
#  o HMR não atravessa bem o bridge e o proxy do vite.config.js aponta para
#  localhost:8082 — que dentro do container não existe. O nginx.conf reproduz
#  exatamente o mesmo rewrite (/api/* → backend, sem o prefixo).
#
#  Para desenvolver com hot-reload, continue rodando `npm run dev` na máquina:
#  o proxy do Vite alcança o backend do compose porque a porta 8082 é publicada.
#
#  Contexto de build: frontend/fintech_app
# ==============================================================================

# ── Estágio 1: build ──────────────────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /build

# npm ci (e não npm install) instala exatamente o que está no package-lock.json.
COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# VITE_API_URL fica DELIBERADAMENTE indefinido: src/services/api.js cai no
# fallback `?? "/api"`, que é o caminho que o nginx abaixo intercepta. Assim o
# bundle não carrega nenhuma URL absoluta cravada.
RUN npm run build

# ── Estágio 2: runtime ────────────────────────────────────────────────────────
FROM nginx:1.27-alpine
COPY --from=build /build/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
