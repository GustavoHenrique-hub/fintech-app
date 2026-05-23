package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai;

import com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto.CategoriaPaiResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto.CriarCategoriaPaiRequestDTO;
import com.enterprise.gustadev.fintech_app.application.categoriapai.usecase.CriarCategoriaPaiUseCase;
import com.enterprise.gustadev.fintech_app.application.categoriapai.usecase.ListarCategoriasPaiUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;
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

@Tag(name = "Hierarquia de Categorias", description = "Define relações pai/filho entre categorias")
@RestController
@RequestMapping("/categorias-pai")
public class CategoriaPaiController {

    private final CriarCategoriaPaiUseCase criarUseCase;
    private final ListarCategoriasPaiUseCase listarUseCase;

    public CategoriaPaiController(CriarCategoriaPaiUseCase criarUseCase,
                                   ListarCategoriasPaiUseCase listarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Definir categoria pai", description = "Associa uma categoria como filha de outra.")
    @ApiResponse(responseCode = "201", description = "Hierarquia criada com sucesso")
    @PostMapping
    public ResponseEntity<CategoriaPaiResponseDTO> criar(@Valid @RequestBody CriarCategoriaPaiRequestDTO dto) {
        CategoriaPai dominio = new CategoriaPai(dto.categoriaId(), dto.paiId());
        CategoriaPaiResponseDTO response = CategoriaPaiResponseDTO.fromDomain(criarUseCase.executar(dominio));
        return ResponseEntity.created(URI.create("/categorias-pai/" + response.id())).body(response);
    }

    @Operation(summary = "Listar subcategorias de uma categoria pai")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/pai/{paiId}")
    public ResponseEntity<List<CategoriaPaiResponseDTO>> listarPorPai(@PathVariable Long paiId) {
        return ResponseEntity.ok(listarUseCase.executar(paiId).stream()
                .map(CategoriaPaiResponseDTO::fromDomain).toList());
    }
}
