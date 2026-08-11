package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario;

import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.AlterarSenhaRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.AtualizarUsuarioRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioResponseDTO;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.AlterarSenhaUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.AtualizarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.CriarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.ListarUsuariosUseCase;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Usuários (Legado)", description = "Gerenciamento de usuários do sistema — módulo legado com IDs numéricos (Long)")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUseCase;
    private final ListarUsuariosUseCase listarUseCase;
    private final BuscarUsuarioUseCase buscarUseCase;
    private final AtualizarUsuarioUseCase atualizarUseCase;
    private final AlterarSenhaUseCase alterarSenhaUseCase;

    public UsuarioController(
            CriarUsuarioUseCase criarUseCase,
            ListarUsuariosUseCase listarUseCase,
            BuscarUsuarioUseCase buscarUseCase,
            AtualizarUsuarioUseCase atualizarUseCase,
            AlterarSenhaUseCase alterarSenhaUseCase
    ) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.atualizarUseCase = atualizarUseCase;
        this.alterarSenhaUseCase = alterarSenhaUseCase;
    }

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema com CPF, e-mail e senha.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existente")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(
                dto.cpf(),
                dto.nome(),
                dto.email(),
                dto.senha(),
                dto.telefone(),
                dto.telegramChatId(),
                dto.whatsappChatId(),
                dto.emailVerificado(),
                dto.dataNascimento()
        );

        Usuario usuarioCriado = criarUseCase.executar(usuario);
        UsuarioResponseDTO response = UsuarioResponseDTO.fromDomain(usuarioCriado);
        return ResponseEntity.created(URI.create("/usuarios/" + response.id())).body(response);
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista completa de usuários cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> response = listarUseCase.executar()
                .stream()
                .map(UsuarioResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico pelo ID numérico (Long).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID numérico (Long) do usuário") @PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }

    @Operation(summary = "Atualizar dados de contato",
            description = "Atualiza e-mail e/ou telefone do usuário. CPF é imutável por este endpoint — " +
                    "alteração de CPF é dado sensível e exige ofício com execução direta no banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já em uso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @Parameter(description = "ID numérico (Long) do usuário") @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioRequestDTO dto) {
        Usuario usuario = atualizarUseCase.executar(id, dto.email(), dto.telefone());
        return ResponseEntity.ok(UsuarioResponseDTO.fromDomain(usuario));
    }

    @Operation(summary = "Alterar senha",
            description = "Altera a senha do usuário mediante confirmação da senha atual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nova senha inválida"),
            @ApiResponse(responseCode = "401", description = "Senha atual incorreta"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @Parameter(description = "ID numérico (Long) do usuário") @PathVariable Long id,
            @Valid @RequestBody AlterarSenhaRequestDTO dto) {
        alterarSenhaUseCase.executar(id, dto.senhaAtual(), dto.novaSenha());
        return ResponseEntity.noContent().build();
    }

}
