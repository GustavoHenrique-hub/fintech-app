package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria;

import com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto.CategoriaResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto.CriarCategoriaRequestDTO;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.BuscarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.CriarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.ListarCategoriasUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
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

    @GetMapping("/padrao")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPadrao() {
        return ResponseEntity.ok(listarUseCase.executarPadrao().stream()
                .map(CategoriaResponseDTO::fromDomain).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(CategoriaResponseDTO::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(CategoriaResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }
}
