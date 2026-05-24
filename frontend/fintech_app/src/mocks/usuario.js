// Mock de usuário no formato do UsuarioResponseDTO do backend.
//
// Origem: adapters/in/web/usuario/dto/UsuarioResponseDTO.java
//   id, usercode, cpf, nome, email, telefone,
//   telegramChatId, whatsappChatId, emailVerificado, dataNascimento (LocalDate)
//
// Para integrar: substituir o `usuarioAtual` por um useQuery(["usuario", id]).
export const usuarioAtual = {
  id: 1,
  usercode: "USR-K9X2A1",
  cpf: "12345678909",            // só dígitos — front aplica a máscara via InputCPF
  nome: "Alex Morgan Silva",
  email: "alex.morgan@finsight.app",
  telefone: "+5511955501142",
  telegramChatId: 5871239871,
  whatsappChatId: null,
  emailVerificado: true,
  dataNascimento: "1995-08-14",  // ISO yyyy-MM-dd (LocalDate)
};
