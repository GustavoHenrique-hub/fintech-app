package com.enterprise.gustadev.fintech_app.adapters.in.web.motivocancelamento;

import com.enterprise.gustadev.fintech_app.adapters.in.web.motivocancelamento.dto.MotivoCancelamentoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase.BuscarMotivoCancelamentoUseCase;
import com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase.ListarMotivosCancelamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Motivos de Cancelamento", description = "Catálogo de motivos para cancelamento de transações")
@RestController
@RequestMapping("/motivos-cancelamento")
public class MotivoCancelamentoController {

    private final ListarMotivosCancelamentoUseCase listarUseCase;
    private final BuscarMotivoCancelamentoUseCase buscarUseCase;

    public MotivoCancelamentoController(ListarMotivosCancelamentoUseCase listarUseCase,
                                         BuscarMotivoCancelamentoUseCase buscarUseCase) {
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
    }

    @Operation(summary = "Listar motivos ativos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<MotivoCancelamentoResponseDTO>> listar() {
        return ResponseEntity.ok(listarUseCase.executar().stream()
                .map(MotivoCancelamentoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar motivo por ID")
    @ApiResponse(responseCode = "200", description = "Motivo encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<MotivoCancelamentoResponseDTO> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(MotivoCancelamentoResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }
}
