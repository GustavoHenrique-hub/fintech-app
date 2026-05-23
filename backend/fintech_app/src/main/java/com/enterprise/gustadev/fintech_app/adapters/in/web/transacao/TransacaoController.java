package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.DeletarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Transações", description = "Registro e consulta de transações financeiras (débito, crédito, transferência)")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarUseCase;
    private final ListarTransacoesUseCase listarUseCase;
    private final BuscarTransacaoUseCase buscarUseCase;
    private final DeletarTransacaoUseCase deletarUseCase;

    public TransacaoController(CriarTransacaoUseCase criarUseCase,
                                ListarTransacoesUseCase listarUseCase,
                                BuscarTransacaoUseCase buscarUseCase,
                                DeletarTransacaoUseCase deletarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
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
                OrigemTransacao.valueOf(dto.origem())
        );
        transacao.setExtratoId(dto.extratoId());
        transacao.setDescricaoOriginal(dto.descricaoOriginal());
        transacao.setDescricaoUsuario(dto.descricaoUsuario());
        transacao.setCategoriaId(dto.categoriaId());
        transacao.setSubcategoria(dto.subcategoria());
        transacao.setEstabelecimento(dto.estabelecimento());
        transacao.setObservacao(dto.observacao());
        TransacaoResponseDTO response = TransacaoResponseDTO.fromDomain(criarUseCase.executar(transacao));
        return ResponseEntity.created(URI.create("/transacoes/" + response.id())).body(response);
    }

    @Operation(summary = "Listar transações do usuário", description = "Retorna todas as transações de um usuário, independente da conta.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorUsuario(
            @Parameter(description = "UUID do usuário") @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar transações por conta", description = "Retorna todas as transações vinculadas a uma conta financeira específica.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorConta(
            @Parameter(description = "UUID da conta") @PathVariable UUID contaId) {
        return ResponseEntity.ok(listarUseCase.executarPorConta(contaId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar transações por extrato", description = "Retorna todas as transações importadas de um extrato bancário específico.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/extrato/{extratoId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorExtrato(
            @Parameter(description = "UUID do extrato") @PathVariable UUID extratoId) {
        return ResponseEntity.ok(listarUseCase.executarPorExtrato(extratoId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar transação por ID", description = "Retorna os dados completos de uma transação pelo seu UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(
            @Parameter(description = "UUID da transação") @PathVariable UUID id) {
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }

    @Operation(summary = "Deletar transação", description = "Remove permanentemente uma transação pelo seu UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "UUID da transação") @PathVariable UUID id) {
        deletarUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
