package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario;

import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioResponseDTO;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.CriarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.DeletarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.ListarUsuariosUseCase;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final DeletarUsuarioUseCase deletarUseCase;

    public UsuarioController(
            CriarUsuarioUseCase criarUseCase,
            ListarUsuariosUseCase listarUseCase,
            BuscarUsuarioUseCase buscarUseCase,
            DeletarUsuarioUseCase deletarUseCase
    ) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
    }

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema com CPF, RG, e-mail e senha.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existente")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(
                dto.usercode(),
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

    @Operation(summary = "Deletar usuário", description = "Remove permanentemente um usuário pelo ID numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID numérico (Long) do usuário") @PathVariable Long id) {
        deletarUseCase.executar(id);
        return ResponseEntity.ok().build();
    }
}
