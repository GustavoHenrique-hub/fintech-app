package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira;

import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraResponseDTO;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.RemoverContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Contas Financeiras",
        description = "Gerenciamento das contas bancárias e carteiras do usuário. " +
                "Cada conta vincula um usuário (`usuarioId`) a um banco previamente cadastrado " +
                "(`bancoId` + `bancoCode`). Um usuário pode ter várias contas em vários bancos, " +
                "e um banco pode ser usado por várias contas de diferentes usuários.")
@RestController
@RequestMapping("/contas")
public class ContaFinanceiraController {

    private final CriarContaFinanceiraUseCase criarUseCase;
    private final ListarContasFinanceirasUseCase listarUseCase;
    private final BuscarContaFinanceiraUseCase buscarUseCase;
    private final RemoverContaFinanceiraUseCase removerUseCase;

    public ContaFinanceiraController(CriarContaFinanceiraUseCase criarUseCase,
                                      ListarContasFinanceirasUseCase listarUseCase,
                                      BuscarContaFinanceiraUseCase buscarUseCase,
                                      RemoverContaFinanceiraUseCase removerUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.removerUseCase = removerUseCase;
    }

    @Operation(summary = "Criar conta financeira",
            description = "Cadastra uma nova conta bancária ou carteira do usuário vinculando-a a um banco. " +
                    "Pré-requisitos: o `usuarioId` deve existir em `/usuarios` e o par `bancoId` + `bancoCode` " +
                    "deve corresponder a um banco existente em `/bancos`. " +
                    "O campo `tipo` deve ser um dos valores de `TipoConta` (ex.: `corrente`, `poupanca`, `investimento`).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso e vinculada ao usuário/banco"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex.: bancoId/bancoCode ausentes ou inconsistentes)")
    })
    @PostMapping
    public ResponseEntity<ContaFinanceiraResponseDTO> criar(@Valid @RequestBody ContaFinanceiraRequestDTO dto) {
        ContaFinanceira conta = new ContaFinanceira(
                dto.usuarioId(),
                TipoConta.valueOf(dto.tipo()),
                dto.bancoId(), dto.bancoCode(),
                dto.saldoInicial(), dto.padrao()
        );
        ContaFinanceiraResponseDTO response = ContaFinanceiraResponseDTO.fromDomain(criarUseCase.executar(conta));
        return ResponseEntity.created(URI.create("/contas/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar contas do usuário",
            description = "Retorna todas as contas financeiras de um usuário, com o respectivo banco vinculado " +
                    "(`bancoId` + `bancoCode`). Útil para montar a tela de visão geral de contas.")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaFinanceiraResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário dono das contas") @PathVariable Long usuarioId) {
        List<ContaFinanceiraResponseDTO> response = listarUseCase.executar(usuarioId)
                .stream().map(ContaFinanceiraResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar conta por ID e código",
            description = "Retorna os dados de uma conta financeira identificada pela chave composta " +
                    "(`id_contas` + `contas_code`), incluindo o banco vinculado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @Operation(summary = "Remover conta financeira (soft delete)",
            description = "Marca a conta como removida (ind_delete='S') e inativa (ativa=false) sem apagar o registro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PatchMapping("/{id_contas}/{contas_code}/remover")
    public ResponseEntity<ContaFinanceiraResponseDTO> remover(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode) {
        return ResponseEntity.ok(ContaFinanceiraResponseDTO.fromDomain(removerUseCase.executar(idContas, contasCode)));
    }

    @GetMapping("/{id_contas}/{contas_code}")
    public ResponseEntity<ContaFinanceiraResponseDTO> buscarPorId(
            @Parameter(description = "ID da conta (id_contas)") @PathVariable("id_contas") Long idContas,
            @Parameter(description = "Código alfanumérico de 6 caracteres (contas_code)") @PathVariable("contas_code") String contasCode) {
        return ResponseEntity.ok(ContaFinanceiraResponseDTO.fromDomain(buscarUseCase.executar(idContas, contasCode)));
    }

}
