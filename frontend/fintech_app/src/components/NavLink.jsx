// Wrapper em volta do NavLink do react-router-dom que aceita classes
// separadas para os estados "ativo" e "pendente" — assim a chamada fica
// declarativa em vez de exigir uma função para className em cada uso.
import { forwardRef } from "react";
import { NavLink as RouterNavLink } from "react-router-dom";
import { cn } from "@/lib/utils";

const NavLink = forwardRef(
  ({ className, activeClassName, pendingClassName, to, ...props }, ref) => {
    return (
      <RouterNavLink
        ref={ref}
        to={to}
        // O NavLink original aceita uma função que recebe { isActive, isPending }.
        // Combinamos as classes condicionais com `cn` (tailwind-merge).
        className={({ isActive, isPending }) =>
          cn(className, isActive && activeClassName, isPending && pendingClassName)
        }
        {...props}
      />
    );
  },
);

NavLink.displayName = "NavLink";

export { NavLink };
