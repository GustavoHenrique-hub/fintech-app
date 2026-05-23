package com.enterprise.gustadev.fintech_app.adapters.in.web.gasto;

import com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto.GastoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto.GastoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.CriarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.DeletarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.ListarGastosUseCase;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
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

@Tag(name = "Gastos (Legado)", description = "Módulo legado de controle de gastos — use /transacoes para novas integrações")
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

    @Operation(summary = "Criar gasto (legado)", description = "Registra um novo gasto no módulo legado. Prefira usar /transacoes para novas integrações.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gasto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
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

    @Operation(summary = "Listar gastos do usuário (legado)", description = "Retorna todos os gastos de um usuário pelo ID numérico (Long) do módulo legado.")
    @ApiResponse(responseCode = "200", description = "Lista de gastos retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<GastoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID numérico (Long) do usuário") @PathVariable Long usuarioId) {
        List<GastoResponseDTO> response = listarUseCase.executar(usuarioId)
                .stream()
                .map(GastoResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar gasto (legado)", description = "Remove um gasto pelo ID numérico no módulo legado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gasto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Gasto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID numérico (Long) do gasto") @PathVariable Long id) {
        deletarUseCase.executar(id);
        return ResponseEntity.ok().build();
    }
}
