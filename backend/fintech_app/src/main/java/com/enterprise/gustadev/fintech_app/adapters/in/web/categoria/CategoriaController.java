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

@Tag(name = "Categorias", description = "Gerenciamento de categorias de transações")
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

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria. Para vincular ao usuário use /categorias-do-usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CriarCategoriaRequestDTO dto) {
        Categoria categoria = new Categoria(
                dto.nome(),
                TipoCategoria.valueOf(dto.tipo()),
                dto.icone(), dto.corHex()
        );
        CategoriaResponseDTO response = CategoriaResponseDTO.fromDomain(criarUseCase.executar(categoria));
        return ResponseEntity.created(URI.create("/categorias/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar categorias padrão", description = "Retorna todas as categorias padrão do sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/padrao")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPadrao() {
        return ResponseEntity.ok(listarUseCase.executarPadrao().stream()
                .map(CategoriaResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar categoria por ID e código",
            description = "Retorna a categoria correspondente à chave composta (id_categorias + categorias_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id_categorias}/{categorias_code}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(
            @Parameter(description = "UUID da categoria (id_categorias)") @PathVariable("id_categorias") UUID idCategorias,
            @Parameter(description = "Código alfanumérico de 6 caracteres (categorias_code)") @PathVariable("categorias_code") String categoriasCode) {
        return ResponseEntity.ok(CategoriaResponseDTO.fromDomain(buscarUseCase.executar(idCategorias, categoriasCode)));
    }
}
