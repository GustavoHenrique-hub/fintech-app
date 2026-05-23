package com.enterprise.gustadev.fintech_app.adapters.in.web.snapshotfinanceiro;

import com.enterprise.gustadev.fintech_app.adapters.in.web.snapshotfinanceiro.dto.SnapshotFinanceiroResponseDTO;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.BuscarSnapshotFinanceiroUseCase;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.ListarSnapshotsFinanceirosUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SnapshotFinanceiroResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(SnapshotFinanceiroResponseDTO::fromDomain).toList());
    }

    @GetMapping("/usuario/{usuarioId}/mes")
    public ResponseEntity<SnapshotFinanceiroResponseDTO> buscarPorMes(
            @PathVariable UUID usuarioId,
            @RequestParam(required = false) UUID contaId,
            @RequestParam short ano,
            @RequestParam short mes) {
        return buscarUseCase.executar(usuarioId, contaId, ano, mes)
                .map(s -> ResponseEntity.ok(SnapshotFinanceiroResponseDTO.fromDomain(s)))
                .orElse(ResponseEntity.notFound().build());
    }
}
