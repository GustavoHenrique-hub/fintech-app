package com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto.RegraClassificacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto.RegraClassificacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.CriarRegraClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.ListarRegrasClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CriadaPorRegraClassificacao;
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
import java.util.UUID;

@Tag(name = "Regras de Classificação", description = "Regras automáticas para categorizar transações com base em termos de descrição")
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

    @Operation(summary = "Criar regra de classificação", description = "Cria uma regra que classifica automaticamente transações cujo termo apareça na descrição. CriadaPor deve ser USUARIO ou SISTEMA.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Regra criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
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

    @Operation(summary = "Listar regras do usuário", description = "Retorna todas as regras de classificação criadas por um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de regras retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RegraClassificacaoResponseDTO>> listarPorUsuario(
            @Parameter(description = "UUID do usuário") @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(RegraClassificacaoResponseDTO::fromDomain).toList());
    }
}
