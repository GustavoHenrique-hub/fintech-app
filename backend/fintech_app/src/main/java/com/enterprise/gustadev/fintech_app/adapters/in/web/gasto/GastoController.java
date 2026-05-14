package com.enterprise.gustadev.fintech_app.adapters.in.web.gasto;

import com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto.GastoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto.GastoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.CriarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.DeletarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.ListarGastosUseCase;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
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
@RequestMapping("/gastos")
public class GastoController {

    private final CriarGastoUseCase criarUseCase;
    private final ListarGastosUseCase listarUseCase;
    private final DeletarGastoUseCase deletarUseCase;

    public GastoController(
            CriarGastoUseCase criarUseCase,
            ListarGastosUseCase listarUseCase,
            DeletarGastoUseCase deletarUseCase
    ) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.deletarUseCase = deletarUseCase;
    }

    @PostMapping
    public ResponseEntity<GastoResponseDTO> criar(@Valid @RequestBody GastoRequestDTO dto) {
        Gasto gasto = new Gasto(
                dto.usuarioId(),
                dto.valor(),
                dto.descricao(),
                Categoria.valueOf(dto.categoria().toUpperCase()),
                dto.data()
        );

        Gasto gastoCriado = criarUseCase.executar(gasto);
        GastoResponseDTO response = GastoResponseDTO.fromDomain(gastoCriado);
        return ResponseEntity.created(URI.create("/gastos/" + response.id())).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<GastoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<GastoResponseDTO> response = listarUseCase.executar(usuarioId)
                .stream()
                .map(GastoResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarUseCase.executar(id);
        return ResponseEntity.ok().build();
    }
}
