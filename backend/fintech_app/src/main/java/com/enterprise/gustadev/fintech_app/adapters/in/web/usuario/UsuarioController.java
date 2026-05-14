package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario;

import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioResponseDTO;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.CriarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.DeletarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.ListarUsuariosUseCase;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
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

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(
                dto.usercode(),
                dto.cpf(),
                dto.rg(),
                dto.nome(),
                dto.sobrenome(),
                dto.email(),
                dto.senha(),
                dto.dataNascimento()
        );

        Usuario usuarioCriado = criarUseCase.executar(usuario);
        UsuarioResponseDTO response = UsuarioResponseDTO.fromDomain(usuarioCriado);
        return ResponseEntity.created(URI.create("/usuarios/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> response = listarUseCase.executar()
                .stream()
                .map(UsuarioResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarUseCase.executar(id);
        return ResponseEntity.ok().build();
    }
}
