package com.enterprise.gustadev.fintech_app.adapters.in.web.snapshotfinanceiro;

import com.enterprise.gustadev.fintech_app.adapters.in.web.snapshotfinanceiro.dto.SnapshotFinanceiroResponseDTO;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.BuscarSnapshotFinanceiroUseCase;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.ListarSnapshotsFinanceirosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Snapshots Financeiros", description = "Consolidação mensal de saldo e movimentação financeira por conta ou usuário")
@RestController
@RequestMapping("/snapshots")
public class SnapshotFinanceiroController {

    private final ListarSnapshotsFinanceirosUseCase listarUseCase;
    private final BuscarSnapshotFinanceiroUseCase buscarUseCase;

    public SnapshotFinanceiroController(ListarSnapshotsFinanceirosUseCase listarUseCase,
                                         BuscarSnapshotFinanceiroUseCase buscarUseCase) {
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
    }

    @Operation(summary = "Listar snapshots do usuário", description = "Retorna todos os snapshots financeiros mensais de um usuário em todas as contas.")
    @ApiResponse(responseCode = "200", description = "Lista de snapshots retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SnapshotFinanceiroResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(SnapshotFinanceiroResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar snapshot por mês", description = "Retorna o snapshot financeiro de um usuário para um ano e mês específicos. Filtragem por conta é opcional.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snapshot encontrado"),
            @ApiResponse(responseCode = "404", description = "Snapshot não encontrado para o período informado")
    })
    @GetMapping("/usuario/{usuarioId}/mes")
    public ResponseEntity<SnapshotFinanceiroResponseDTO> buscarPorMes(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId,
            @Parameter(description = "ID da conta (opcional, filtra por conta específica)") @RequestParam(required = false) Long contaId,
            @Parameter(description = "Ano do snapshot (ex: 2025)") @RequestParam short ano,
            @Parameter(description = "Mês do snapshot (1-12)") @RequestParam short mes) {
        return buscarUseCase.executar(usuarioId, contaId, ano, mes)
                .map(s -> ResponseEntity.ok(SnapshotFinanceiroResponseDTO.fromDomain(s)))
                .orElse(ResponseEntity.notFound().build());
    }
}
