package com.enterprise.gustadev.fintech_app.adapters.in.web.motivocancelamento;

import com.enterprise.gustadev.fintech_app.adapters.in.web.motivocancelamento.dto.MotivoCancelamentoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase.BuscarMotivoCancelamentoUseCase;
import com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase.ListarMotivosCancelamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "Buscar motivo por ID e código",
            description = "Retorna o motivo de cancelamento identificado pela chave composta (id_motivos + motivos_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Motivo encontrado"),
            @ApiResponse(responseCode = "404", description = "Motivo não encontrado")
    })
    @GetMapping("/{id_motivos}/{motivos_code}")
    public ResponseEntity<MotivoCancelamentoResponseDTO> buscar(
            @Parameter(description = "ID do motivo de cancelamento (id_motivos)") @PathVariable("id_motivos") Long idMotivos,
            @Parameter(description = "Código alfanumérico de 6 caracteres (motivos_code)") @PathVariable("motivos_code") String motivosCode) {
        return ResponseEntity.ok(MotivoCancelamentoResponseDTO.fromDomain(buscarUseCase.executar(idMotivos, motivosCode)));
    }
}
