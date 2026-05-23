package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria;

import com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto.CategoriaResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto.CriarCategoriaRequestDTO;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.BuscarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.CriarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.ListarCategoriasUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "Categorias", description = "Gerenciamento de categorias de transações (padrão do sistema e personalizadas por usuário)")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CriarCategoriaUseCase criarUseCase;
    private final ListarCategoriasUseCase listarUseCase;
    private final BuscarCategoriaUseCase buscarUseCase;

    public CategoriaController(CriarCategoriaUseCase criarUseCase,
                                ListarCategoriasUseCase listarUseCase,
                                BuscarCategoriaUseCase buscarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
    }

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria personalizada para um usuário. Tipo deve ser um dos valores do enum TipoCategoria.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CriarCategoriaRequestDTO dto) {
        Categoria categoria = new Categoria(
                dto.usuarioId(), dto.nome(),
                TipoCategoria.valueOf(dto.tipo()),
                dto.icone(), dto.corHex()
        );
        categoria.setCategoriaPaiId(dto.categoriaPaiId());
        CategoriaResponseDTO response = CategoriaResponseDTO.fromDomain(criarUseCase.executar(categoria));
        return ResponseEntity.created(URI.create("/categorias/" + response.id())).body(response);
    }

    @Operation(summary = "Listar categorias padrão", description = "Retorna todas as categorias padrão do sistema, disponíveis para todos os usuários.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias padrão retornada com sucesso")
    @GetMapping("/padrao")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPadrao() {
        return ResponseEntity.ok(listarUseCase.executarPadrao().stream()
                .map(CategoriaResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar categorias do usuário", description = "Retorna todas as categorias personalizadas criadas por um usuário específico.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias do usuário retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPorUsuario(
            @Parameter(description = "UUID do usuário") @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(CategoriaResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar categoria por ID", description = "Retorna os dados de uma categoria específica pelo seu UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(
            @Parameter(description = "UUID da categoria") @PathVariable UUID id) {
        return ResponseEntity.ok(CategoriaResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }
}
