// Re-exporta o hook/função de toast do diretório hooks/ para manter
// compatibilidade com o padrão shadcn (onde o import vem de "@/components/ui/use-toast").
import { useToast, toast } from "@/hooks/use-toast";

export { useToast, toast };
