package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira;

import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraResponseDTO;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.DeletarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
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

@Tag(name = "Contas Financeiras", description = "Gerenciamento de contas bancárias e carteiras do usuário (corrente, poupança, investimento etc.)")
@RestController
@RequestMapping("/contas")
public class ContaFinanceiraController {

    private final CriarContaFinanceiraUseCase criarUseCase;
    private final ListarContasFinanceirasUseCase listarUseCase;
    private final BuscarContaFinanceiraUseCase buscarUseCase;
    private final DeletarContaFinanceiraUseCase deletarUseCase;

    public ContaFinanceiraController(CriarContaFinanceiraUseCase criarUseCase,
                                      ListarContasFinanceirasUseCase listarUseCase,
                                      BuscarContaFinanceiraUseCase buscarUseCase,
                                      DeletarContaFinanceiraUseCase deletarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
    }

    @Operation(summary = "Criar conta financeira", description = "Cadastra uma nova conta bancária ou carteira para o usuário. Tipo deve ser um dos valores de TipoConta.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<ContaFinanceiraResponseDTO> criar(@Valid @RequestBody ContaFinanceiraRequestDTO dto) {
        ContaFinanceira conta = new ContaFinanceira(
                dto.usuarioId(), dto.nome(),
                TipoConta.valueOf(dto.tipo()),
                dto.banco(), dto.saldoInicial(), dto.padrao()
        );
        ContaFinanceiraResponseDTO response = ContaFinanceiraResponseDTO.fromDomain(criarUseCase.executar(conta));
        return ResponseEntity.created(URI.create("/contas/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar contas do usuário", description = "Retorna todas as contas financeiras cadastradas para um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaFinanceiraResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        List<ContaFinanceiraResponseDTO> response = listarUseCase.executar(usuarioId)
                .stream().map(ContaFinanceiraResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar conta por ID e código",
            description = "Retorna os dados de uma conta financeira identificada pela chave composta (id_contas + contas_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id_contas}/{contas_code}")
    public ResponseEntity<ContaFinanceiraResponseDTO> buscarPorId(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode) {
        return ResponseEntity.ok(ContaFinanceiraResponseDTO.fromDomain(buscarUseCase.executar(idContas, contasCode)));
    }

    @Operation(summary = "Deletar conta financeira",
            description = "Remove permanentemente a conta identificada pela chave composta (id_contas + contas_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id_contas}/{contas_code}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode) {
        deletarUseCase.executar(idContas, contasCode);
        return ResponseEntity.noContent().build();
    }
}
