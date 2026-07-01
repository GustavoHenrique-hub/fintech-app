package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.EstornarTransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.EstornarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Transações", description = "Registro e consulta de transações financeiras (débito, crédito, transferência)")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarUseCase;
    private final ListarTransacoesUseCase listarUseCase;
    private final BuscarTransacaoUseCase buscarUseCase;
    private final EstornarTransacaoUseCase estornarUseCase;

    public TransacaoController(CriarTransacaoUseCase criarUseCase,
                               ListarTransacoesUseCase listarUseCase,
                               BuscarTransacaoUseCase buscarUseCase,
                               EstornarTransacaoUseCase estornarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.estornarUseCase = estornarUseCase;
    }

    @Operation(summary = "Criar transação", description = "Registra uma nova transação financeira. Os campos tipo e origem devem conter valores válidos dos enums TipoTransacao e OrigemTransacao.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        ContaFinanceira conta = new ContaFinanceira(dto.contaId(), dto.contaCode());
        Transacao transacao = new Transacao(
                conta,
                TipoTransacao.valueOf(dto.tipo()),
                dto.valor(), dto.dataTransacao(),
                dto.categoriaId(),
                OrigemTransacao.valueOf(dto.origem())
        );
        transacao.setDescricao(dto.descricao());
        transacao.setEstabelecimento(dto.estabelecimento());
        transacao.setObservacao(dto.observacao());
        TransacaoResponseDTO response = TransacaoResponseDTO.fromDomain(criarUseCase.executar(transacao));
        return ResponseEntity.created(URI.create("/transacoes/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar transações do usuário", description = "Retorna todas as transações de um usuário, independente da conta.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar transações por conta", description = "Retorna todas as transações vinculadas a uma conta financeira específica.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorConta(
            @Parameter(description = "ID da conta") @PathVariable Long contaId) {
        return ResponseEntity.ok(listarUseCase.executarPorConta(contaId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar transação por ID e código",
            description = "Retorna a transação identificada pela chave composta (id_transacoes + transacoes_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id_transacoes}/{transacoes_code}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode) {
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(buscarUseCase.executar(idTransacoes, transacoesCode)));
    }

    @Operation(summary = "Estornar transação",
            description = "Marca a transação identificada pela chave composta como indEstorno='S'. Operação idempotente: chamar novamente em uma transação já estornada retorna 200 sem nova alteração.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação estornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PatchMapping("/{id_transacoes}/{transacoes_code}/estornar")
    public ResponseEntity<TransacaoResponseDTO> estornar(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode,
            @Valid @RequestBody EstornarTransacaoRequestDTO dto) {
        Transacao estornada = estornarUseCase.executar(
                idTransacoes, transacoesCode,
                dto.contaId(), dto.contaCode());
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(estornada));
    }
}
