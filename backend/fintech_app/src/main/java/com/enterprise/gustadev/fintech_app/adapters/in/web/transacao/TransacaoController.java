package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.*;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Tag(name = "Transações", description = "Registro e consulta de transações financeiras (débito, crédito, transferência)")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarUseCase;
    private final ListarTransacoesUseCase listarUseCase;
    private final BuscarTransacaoUseCase buscarUseCase;
    private final DeletarTransacaoUseCase deletarUseCase;
    private final EstornarTransacaoUseCase estornarUseCase;

    public TransacaoController(CriarTransacaoUseCase criarUseCase,
                               ListarTransacoesUseCase listarUseCase,
                               BuscarTransacaoUseCase buscarUseCase,
                               DeletarTransacaoUseCase deletarUseCase,
                               EstornarTransacaoUseCase estornarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
        this.estornarUseCase = estornarUseCase;
    }

    @Operation(summary = "Criar transação", description = "Registra uma nova transação financeira. Os campos tipo e origem devem conter valores válidos dos enums TipoTransacao e OrigemTransacao.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        Transacao transacao = new Transacao(
                dto.usuarioId(), dto.contaId(),
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

    @Operation(summary = "Deletar transação",
            description = "Remove permanentemente a transação identificada pela chave composta (id_transacoes + transacoes_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @DeleteMapping("/{id_transacoes}/{transacoes_code}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode) {
        deletarUseCase.executar(idTransacoes, transacoesCode);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Estornar transação", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PatchMapping("/estornar")
    public ResponseEntity<Map<String, String>> estornar(
            @Parameter(description = "ID da transação (id_transacao)") @RequestBody Long transacaoId,
            @RequestBody String transacoeCode,
            @RequestBody Long usuarioId,
            @RequestBody String usuarioCode,
            @RequestBody Long contaId,
            @RequestBody String contaCode) {
        return estornarUseCase.executar(transacaoId, transacoeCode, usuarioId, usuarioCode, contaId, contaCode);
    }
}
