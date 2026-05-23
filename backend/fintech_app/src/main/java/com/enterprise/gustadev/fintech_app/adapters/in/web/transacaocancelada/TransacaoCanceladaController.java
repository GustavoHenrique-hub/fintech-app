package com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada;

import com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada.dto.CancelarTransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada.dto.TransacaoCanceladaResponseDTO;
import com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase.CancelarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase.ListarTransacoesCanceladasUseCase;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanceladoPor;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
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

@Tag(name = "Transações Canceladas", description = "Registro de cancelamentos de transações")
@RestController
@RequestMapping("/transacoes-canceladas")
public class TransacaoCanceladaController {

    private final CancelarTransacaoUseCase cancelarUseCase;
    private final ListarTransacoesCanceladasUseCase listarUseCase;

    public TransacaoCanceladaController(CancelarTransacaoUseCase cancelarUseCase,
                                         ListarTransacoesCanceladasUseCase listarUseCase) {
        this.cancelarUseCase = cancelarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Cancelar transação", description = "Registra o cancelamento de uma transação com rastreabilidade completa.")
    @ApiResponse(responseCode = "201", description = "Cancelamento registrado com sucesso")
    @PostMapping
    public ResponseEntity<TransacaoCanceladaResponseDTO> cancelar(
            @Valid @RequestBody CancelarTransacaoRequestDTO dto) {
        TransacaoCancelada dominio = new TransacaoCancelada(
                dto.transacaoId(), dto.usuarioId(), dto.contaId(), dto.motivoId(),
                CanceladoPor.valueOf(dto.canceladoPor()),
                dto.valorOriginal(), dto.observacao(), dto.ipOrigem()
        );
        TransacaoCanceladaResponseDTO response = TransacaoCanceladaResponseDTO.fromDomain(
                cancelarUseCase.executar(dominio));
        return ResponseEntity.created(URI.create("/transacoes-canceladas/" + response.id())).body(response);
    }

    @Operation(summary = "Listar cancelamentos de um usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoCanceladaResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(TransacaoCanceladaResponseDTO::fromDomain).toList());
    }
}
