package com.enterprise.gustadev.fintech_app.adapters.in.web.economia;

import com.enterprise.gustadev.fintech_app.adapters.in.web.economia.dto.EconomiaRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.economia.dto.MovimentacaoEconomiaResponseDTO;
import com.enterprise.gustadev.fintech_app.application.economia.usecase.ListarMovimentacoesEconomiaUseCase;
import com.enterprise.gustadev.fintech_app.application.economia.usecase.RegistrarMovimentacaoEconomiaUseCase;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
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

@Tag(name = "Economias",
        description = "Sub-saldo \"Economias\" de uma conta financeira. O usuário reserva parte do " +
                "`saldoAtual` no `saldoEconomias` da MESMA conta (APORTE) e depois resgata de volta " +
                "(RESGATE). NÃO é receita nem gasto, portanto não afeta o resumo do período nem " +
                "aparece em `/transacoes`. Cada operação gera um registro no ledger dedicado " +
                "`movimentacoes_economia`.")
@RestController
@RequestMapping("/contas/{id_contas}/{contas_code}/economias")
public class EconomiaController {

    private final RegistrarMovimentacaoEconomiaUseCase registrarUseCase;
    private final ListarMovimentacoesEconomiaUseCase listarUseCase;

    public EconomiaController(RegistrarMovimentacaoEconomiaUseCase registrarUseCase,
                               ListarMovimentacoesEconomiaUseCase listarUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Aportar em economias",
            description = "Move `valor` de `saldoAtual` para `saldoEconomias` na MESMA conta. " +
                    "Falha com 400 se o saldo disponível for insuficiente ou se o valor não for positivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aporte registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo disponível insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conta financeira não encontrada")
    })
    @PostMapping("/aporte")
    public ResponseEntity<MovimentacaoEconomiaResponseDTO> aportar(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode,
            @Valid @RequestBody EconomiaRequestDTO dto) {
        MovimentacaoEconomia mov = registrarUseCase.executar(
                idContas, contasCode, TipoMovimentacaoEconomia.APORTE, dto.valor(), dto.descricao());
        MovimentacaoEconomiaResponseDTO response = MovimentacaoEconomiaResponseDTO.fromDomain(mov);
        URI location = URI.create("/contas/" + idContas + "/" + contasCode + "/economias/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Resgatar de economias",
            description = "Move `valor` de `saldoEconomias` de volta para `saldoAtual` na MESMA conta. " +
                    "Falha com 400 se o saldo em economias for insuficiente ou se o valor não for positivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resgate registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo em economias insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conta financeira não encontrada")
    })
    @PostMapping("/resgate")
    public ResponseEntity<MovimentacaoEconomiaResponseDTO> resgatar(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode,
            @Valid @RequestBody EconomiaRequestDTO dto) {
        MovimentacaoEconomia mov = registrarUseCase.executar(
                idContas, contasCode, TipoMovimentacaoEconomia.RESGATE, dto.valor(), dto.descricao());
        MovimentacaoEconomiaResponseDTO response = MovimentacaoEconomiaResponseDTO.fromDomain(mov);
        URI location = URI.create("/contas/" + idContas + "/" + contasCode + "/economias/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Listar movimentações de economia da conta",
            description = "Retorna o ledger de aportes/resgates da conta em ordem decrescente de dataMovimentacao.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<MovimentacaoEconomiaResponseDTO>> listar(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode) {
        List<MovimentacaoEconomiaResponseDTO> response = listarUseCase.executar(idContas).stream()
                .map(MovimentacaoEconomiaResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
}
