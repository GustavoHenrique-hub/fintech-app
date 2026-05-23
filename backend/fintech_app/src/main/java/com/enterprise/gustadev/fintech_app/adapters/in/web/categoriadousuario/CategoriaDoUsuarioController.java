package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario;

import com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario.dto.CategoriaDoUsuarioResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario.dto.VincularCategoriaUsuarioRequestDTO;
import com.enterprise.gustadev.fintech_app.application.categoriadousuario.usecase.ListarCategoriasDoUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.categoriadousuario.usecase.VincularCategoriaUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Categorias do Usuário", description = "Vincula categorias a um usuário específico")
@RestController
@RequestMapping("/categorias-do-usuario")
public class CategoriaDoUsuarioController {

    private final VincularCategoriaUsuarioUseCase vincularUseCase;
    private final ListarCategoriasDoUsuarioUseCase listarUseCase;

    public CategoriaDoUsuarioController(VincularCategoriaUsuarioUseCase vincularUseCase,
                                         ListarCategoriasDoUsuarioUseCase listarUseCase) {
        this.vincularUseCase = vincularUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Vincular categoria ao usuário")
    @ApiResponse(responseCode = "201", description = "Vínculo criado com sucesso")
    @PostMapping
    public ResponseEntity<CategoriaDoUsuarioResponseDTO> vincular(
            @Valid @RequestBody VincularCategoriaUsuarioRequestDTO dto) {
        CategoriaDoUsuario dominio = new CategoriaDoUsuario(dto.usuarioId(), dto.categoriaId());
        CategoriaDoUsuarioResponseDTO response = CategoriaDoUsuarioResponseDTO.fromDomain(
                vincularUseCase.executar(dominio));
        return ResponseEntity.created(URI.create("/categorias-do-usuario/" + response.id())).body(response);
    }

    @Operation(summary = "Listar categorias de um usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CategoriaDoUsuarioResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(CategoriaDoUsuarioResponseDTO::fromDomain).toList());
    }
}
