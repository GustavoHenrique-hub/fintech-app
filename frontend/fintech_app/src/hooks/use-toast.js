// Sistema de toasts (notificações temporárias) inspirado no react-hot-toast.
// Mantém um "store" simples em memória usando padrão de reducer + observers,
// sem precisar do Context API: qualquer componente chama `toast(...)` ou
// `useToast()` para acessar o mesmo estado global.
import * as React from "react";

const TOAST_LIMIT = 1; // Quantos toasts podem aparecer simultaneamente.
const TOAST_REMOVE_DELAY = 1000000; // Atraso (ms) entre fechar e desmontar.

// Nomes de ações usadas pelo reducer abaixo.
const actionTypes = {
  ADD_TOAST: "ADD_TOAST",
  UPDATE_TOAST: "UPDATE_TOAST",
  DISMISS_TOAST: "DISMISS_TOAST",
  REMOVE_TOAST: "REMOVE_TOAST",
};

let count = 0;

// Gera ids sequenciais únicos para cada toast.
function genId() {
  count = (count + 1) % Number.MAX_SAFE_INTEGER;
  return count.toString();
}

// Mapa id -> timeoutId para evitar agendar a remoção do mesmo toast 2x.
const toastTimeouts = new Map();

// Agenda a remoção definitiva (depois de fechar) de um toast.
const addToRemoveQueue = (toastId) => {
  if (toastTimeouts.has(toastId)) return;

  const timeout = setTimeout(() => {
    toastTimeouts.delete(toastId);
    dispatch({ type: "REMOVE_TOAST", toastId });
  }, TOAST_REMOVE_DELAY);

  toastTimeouts.set(toastId, timeout);
};

// Reducer puro: recebe estado anterior + ação, devolve novo estado.
export const reducer = (state, action) => {
  switch (action.type) {
    case "ADD_TOAST":
      return {
        ...state,
        // Adiciona o novo no topo e respeita o limite máximo.
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

      // Efeito colateral: agenda a remoção do(s) toast(s) afetado(s).
      if (toastId) {
        addToRemoveQueue(toastId);
      } else {
        state.toasts.forEach((toast) => addToRemoveQueue(toast.id));
      }

      // Apenas marca como fechado (open=false) — quem remove é o REMOVE_TOAST.
      return {
        ...state,
        toasts: state.toasts.map((t) =>
          t.id === toastId || toastId === undefined ? { ...t, open: false } : t,
        ),
      };
    }

    case "REMOVE_TOAST":
      // Sem id remove todos; com id remove apenas o correspondente.
      if (action.toastId === undefined) return { ...state, toasts: [] };
      return {
        ...state,
        toasts: state.toasts.filter((t) => t.id !== action.toastId),
      };

    default:
      return state;
  }
};

// Lista de callbacks que querem reagir a mudanças do estado (cada hook useToast
// se registra/desregistra aqui).
const listeners = [];

// Estado vive como variável de módulo — singleton.
let memoryState = { toasts: [] };

function dispatch(action) {
  memoryState = reducer(memoryState, action);
  listeners.forEach((listener) => listener(memoryState));
}

// API imperativa: `toast({ title, description })` dispara uma notificação.
function toast({ ...props }) {
  const id = genId();

  const update = (props) =>
    dispatch({ type: "UPDATE_TOAST", toast: { ...props, id } });
  const dismiss = () => dispatch({ type: "DISMISS_TOAST", toastId: id });

  dispatch({
    type: "ADD_TOAST",
    toast: {
      ...props,
      id,
      open: true,
      // Quando o Radix fecha o toast (clique no X, swipe...), também damos dismiss.
      onOpenChange: (open) => {
        if (!open) dismiss();
      },
    },
  });

  return { id, dismiss, update };
}

// Hook React: assina o store e devolve { toasts, toast, dismiss }.
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
