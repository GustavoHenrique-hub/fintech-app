// Mapeia a string `icone` da Categoria (backend) para um componente do lucide-react.
// O backend guarda só o nome do ícone (string) — quem traduz pra componente é o front.
//
// Como adicionar um ícone novo: importe acima e adicione no objeto ICONES.
import {
  Utensils,
  ShoppingCart,
  ShoppingBag,
  Pizza,
  Coffee,
  Car,
  Fuel,
  Bus,
  Plane,
  Home,
  Zap,
  Heart,
  Pill,
  Film,
  Wifi,
  Briefcase,
  TrendingUp,
  MoreHorizontal,
  Tag, // fallback
} from "lucide-react";

const ICONES = {
  utensils:       Utensils,
  shopping_cart:  ShoppingCart,
  shopping_bag:   ShoppingBag,
  pizza:          Pizza,
  coffee:         Coffee,
  car:            Car,
  fuel:           Fuel,
  bus:            Bus,
  plane:          Plane,
  home:           Home,
  zap:            Zap,
  heart:          Heart,
  pill:           Pill,
  film:           Film,
  wifi:           Wifi,
  briefcase:      Briefcase,
  trending_up:    TrendingUp,
  more:           MoreHorizontal,
};

/** Devolve o componente lucide correspondente, com fallback em `Tag`. */
export function getIconeCategoria(nomeIcone) {
  if (!nomeIcone) return Tag;
  return ICONES[nomeIcone] ?? Tag;
}
