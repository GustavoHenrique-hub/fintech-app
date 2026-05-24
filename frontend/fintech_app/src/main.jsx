// Ponto de entrada da aplicação React.
// Aqui o React monta o componente <App /> dentro do elemento #root do index.html.
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import "./index.css";

// createRoot habilita o modo concorrente do React 18.
// O "!" do TypeScript não existe em JS — confiamos que #root está no index.html.
createRoot(document.getElementById("root")).render(<App />);
