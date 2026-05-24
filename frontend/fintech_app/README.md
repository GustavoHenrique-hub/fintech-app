# FinSight — Frontend

Aplicação web mobile-first de finanças pessoais (React + Vite + Tailwind, 100% JavaScript).

## Scripts

```powershell
npm install     # instala dependências
npm run dev     # sobe o dev server em http://localhost:8080
npm run build   # gera o build de produção em dist/
npm run preview # serve o build de produção localmente
npm run lint    # roda o ESLint
npm test        # roda os testes (Vitest)
```

## Estrutura

```
src/
├─ main.jsx              # bootstrap do React
├─ App.jsx               # providers globais + roteamento
├─ index.css             # tokens (CSS vars) + estilos base do Tailwind
├─ pages/
│  ├─ Index.jsx          # tela única que orquestra as abas do app
│  └─ NotFound.jsx       # 404
├─ components/
│  ├─ finsight/          # componentes específicos do app
│  │  ├─ PhoneFrame.jsx  # moldura responsiva (full-screen no mobile, moldura no desktop)
│  │  ├─ TopBar.jsx
│  │  ├─ BottomNav.jsx
│  │  ├─ BalanceChart.jsx
│  │  └─ screens/        # 5 telas: Overview, Analytics, Transactions, Profile, AddTransaction
│  └─ ui/                # primitivas de UI (toast, tooltip, sonner)
├─ hooks/                # use-toast, use-mobile
└─ lib/utils.js          # helper `cn()` para classes do Tailwind
```

## Paleta / Design system

A paleta vive em variáveis CSS HSL em [`src/index.css`](src/index.css) e é exposta ao Tailwind como tokens semânticos (`primary`, `success`, `surface-purple`, etc.) via [`tailwind.config.js`](tailwind.config.js). Trocar o tema é só editar essas variáveis.
