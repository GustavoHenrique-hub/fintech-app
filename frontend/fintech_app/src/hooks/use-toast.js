// Sistema de toasts da aplicação.
//
// Spec atendida:
//  - Posicionamento canto inferior-direito (controlado pelo Viewport).
//  - Variantes: success | error | warning | info (visual + ícone).
//  - Auto-dismiss em 5s (cancelado se o usuário passar o mouse, via Radix).
//  - Máx 3 toasts simultâneos (TOAST_LIMIT).
//  - Suporta ação inline (ex.: "Desfazer") via { action: <Button> }.
//
// API:
//   const { toast } = useToast();
//   toast({ title: "Salvo", description: "...", variant: "success" });
//   toast({ title: "Erro", variant: "error", action: <Button onClick={undo}>Desfazer</Button> });
//
// Padrão de implementação: store singleton em memória + reducer, sem Context.
// Qualquer componente em qualquer ponto da árvore consegue disparar toasts
// chamando `toast(...)` sem precisar de provider customizado.
import * as React from "react";

const TOAST_LIMIT = 3;          // máximo simultâneo
const TOAST_AUTO_DISMISS = 5000;  // ms até começar a desmontar
const TOAST_REMOVE_DELAY = 400;   // ms entre "fechar" e remover do DOM (animação)

const actionTypes = {
  ADD_TOAST: "ADD_TOAST",
  UPDATE_TOAST: "UPDATE_TOAST",
  DISMISS_TOAST: "DISMISS_TOAST",
  REMOVE_TOAST: "REMOVE_TOAST",
};

let count = 0;
function genId() {
  count = (count + 1) % Number.MAX_SAFE_INTEGER;
  return count.toString();
}

// Mapa id → timeout para evitar agendar a mesma remoção 2x.
const toastTimeouts = new Map();

const scheduleRemoval = (toastId) => {
  if (toastTimeouts.has(toastId)) return;
  const timeout = setTimeout(() => {
    toastTimeouts.delete(toastId);
    dispatch({ type: "REMOVE_TOAST", toastId });
  }, TOAST_REMOVE_DELAY);
  toastTimeouts.set(toastId, timeout);
};

export const reducer = (state, action) => {
  switch (action.type) {
    case "ADD_TOAST":
      return {
        ...state,
        toasts: [action.toast, ...state.toasts].slice(0, TOAST_LIMIT),
      };

    case "UPDATE_TOAST":
      return {
        ...state,
        toasts: state.toasts.map((t) =>
          t.id === action.toast.id ? { ...t, ...action.toast } : t,
        ),
      };

    case "DISMISS_TOAST": {
      const { toastId } = action;
      if (toastId) scheduleRemoval(toastId);
      else state.toasts.forEach((t) => scheduleRemoval(t.id));
      return {
        ...state,
        toasts: state.toasts.map((t) =>
          t.id === toastId || toastId === undefined ? { ...t, open: false } : t,
        ),
      };
    }

    case "REMOVE_TOAST":
      if (action.toastId === undefined) return { ...state, toasts: [] };
      return {
        ...state,
        toasts: state.toasts.filter((t) => t.id !== action.toastId),
      };

    default:
      return state;
  }
};

// Listeners de componentes que assinam o estado via useToast().
const listeners = [];
let memoryState = { toasts: [] };

function dispatch(action) {
  memoryState = reducer(memoryState, action);
  listeners.forEach((listener) => listener(memoryState));
}

// API imperativa: `toast({ title, description, variant, action })`.
// Devolve { id, dismiss, update } pra controlar o toast depois.
function toast({ variant = "info", duration = TOAST_AUTO_DISMISS, ...props }) {
  const id = genId();

  const update = (next) =>
    dispatch({ type: "UPDATE_TOAST", toast: { ...next, id } });
  const dismiss = () => dispatch({ type: "DISMISS_TOAST", toastId: id });

  dispatch({
    type: "ADD_TOAST",
    toast: {
      ...props,
      id,
      variant,
      duration,
      open: true,
      // Quando o Radix marca como fechado (X, swipe, escape, autodismiss),
      // disparamos o DISMISS_TOAST para encadear o REMOVE depois da animação.
      onOpenChange: (open) => {
        if (!open) dismiss();
      },
    },
  });

  return { id, dismiss, update };
}

// Atalhos por variante — DX mais agradável que digitar a prop toda hora.
toast.success = (props) => toast({ ...props, variant: "success" });
toast.error = (props) => toast({ ...props, variant: "error" });
toast.warning = (props) => toast({ ...props, variant: "warning" });
toast.info = (props) => toast({ ...props, variant: "info" });

// Hook React que assina o store e devolve { toasts, toast, dismiss }.
function useToast() {
  const [state, setState] = React.useState(memoryState);

  React.useEffect(() => {
    listeners.push(setState);
    return () => {
      const index = listeners.indexOf(setState);
      if (index > -1) listeners.splice(index, 1);
    };
  }, [state]);

  return {
    ...state,
    toast,
    dismiss: (toastId) => dispatch({ type: "DISMISS_TOAST", toastId }),
  };
}

export { useToast, toast };
