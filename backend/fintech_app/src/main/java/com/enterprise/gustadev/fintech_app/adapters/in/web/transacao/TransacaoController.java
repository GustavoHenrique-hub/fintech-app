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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorConta(@PathVariable UUID contaId) {
        return ResponseEntity.ok(listarUseCase.executarPorConta(contaId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @GetMapping("/extrato/{extratoId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorExtrato(@PathVariable UUID extratoId) {
        return ResponseEntity.ok(listarUseCase.executarPorExtrato(extratoId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
