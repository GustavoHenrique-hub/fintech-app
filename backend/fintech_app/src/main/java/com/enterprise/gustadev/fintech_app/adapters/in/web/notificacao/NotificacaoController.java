package com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto.CriarNotificacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto.NotificacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.CriarNotificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.ListarNotificacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
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

@Tag(name = "Notificações", description = "Envio e listagem de notificações para os usuários (push, e-mail, SMS)")
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final CriarNotificacaoUseCase criarUseCase;
    private final ListarNotificacoesUseCase listarUseCase;

    public NotificacaoController(CriarNotificacaoUseCase criarUseCase,
                                  ListarNotificacoesUseCase listarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Criar notificação", description = "Envia uma notificação para o usuário pelo canal especificado (PUSH, EMAIL, SMS).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO> criar(@Valid @RequestBody CriarNotificacaoRequestDTO dto) {
        Notificacao notificacao = new Notificacao(
                dto.usuarioId(),
                null,
                CanalNotificacao.valueOf(dto.canal()),
                dto.tipo(), dto.titulo(), dto.mensagem()
        );
        NotificacaoResponseDTO response = NotificacaoResponseDTO.fromDomain(criarUseCase.executar(notificacao));
        return ResponseEntity.created(URI.create("/notificacoes/" + response.id())).body(response);
    }

    @Operation(summary = "Listar notificações do usuário", description = "Retorna todas as notificações enviadas para um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(NotificacaoResponseDTO::fromDomain).toList());
    }
}
