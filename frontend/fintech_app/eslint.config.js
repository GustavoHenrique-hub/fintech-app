// Configuração do ESLint (flat config, padrão atual a partir do ESLint 9).
// Sem TypeScript: usamos o preset oficial do JS + plugins de React Hooks /
// React Refresh para garantir boas práticas em hooks e HMR.
import js from "@eslint/js";
import globals from "globals";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";

export default [
  // Não lintar a pasta de build.
  { ignores: ["dist"] },
  {
    ...js.configs.recommended,
    files: ["**/*.{js,jsx}"],
    languageOptions: {
      ecmaVersion: 2020,
      sourceType: "module",
      // Habilita globals do browser (window, document, etc.) sem warnings.
      globals: globals.browser,
      parserOptions: {
        ecmaFeatures: { jsx: true },
      },
    },
    plugins: {
      "react-hooks": reactHooks,
      "react-refresh": reactRefresh,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      // Permite exports adicionais junto de componentes (utilitários, constantes).
      "react-refresh/only-export-components": ["warn", { allowConstantExport: true }],
      // Não queremos falhar build por variável não usada — TS já não está aí.
      "no-unused-vars": "off",
    },
  },
];
