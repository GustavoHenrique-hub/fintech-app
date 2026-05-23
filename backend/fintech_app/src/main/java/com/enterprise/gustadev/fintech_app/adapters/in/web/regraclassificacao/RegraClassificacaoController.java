package com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto.RegraClassificacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto.RegraClassificacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.CriarRegraClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.ListarRegrasClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CriadaPorRegraClassificacao;
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
import java.util.UUID;

@RestController
@RequestMapping("/regras-classificacao")
public class RegraClassificacaoController {

    private final CriarRegraClassificacaoUseCase criarUseCase;
    private final ListarRegrasClassificacaoUseCase listarUseCase;

    public RegraClassificacaoController(CriarRegraClassificacaoUseCase criarUseCase,
                                         ListarRegrasClassificacaoUseCase listarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @PostMapping
    public ResponseEntity<RegraClassificacaoResponseDTO> criar(@Valid @RequestBody RegraClassificacaoRequestDTO dto) {
        RegraClassificacao regra = new RegraClassificacao(
                dto.usuarioId(), dto.termo(), dto.categoriaId(),
                dto.subcategoria(),
                CriadaPorRegraClassificacao.valueOf(dto.criadaPor())
        );
        RegraClassificacaoResponseDTO response = RegraClassificacaoResponseDTO.fromDomain(criarUseCase.executar(regra));
        return ResponseEntity.created(URI.create("/regras-classificacao/" + response.id())).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RegraClassificacaoResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(RegraClassificacaoResponseDTO::fromDomain).toList());
    }
}
