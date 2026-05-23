package com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto.CriarNotificacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto.NotificacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.CriarNotificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.ListarNotificacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
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
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final CriarNotificacaoUseCase criarUseCase;
    private final ListarNotificacoesUseCase listarUseCase;

    public NotificacaoController(CriarNotificacaoUseCase criarUseCase,
                                  ListarNotificacoesUseCase listarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO> criar(@Valid @RequestBody CriarNotificacaoRequestDTO dto) {
        Notificacao notificacao = new Notificacao(
                dto.usuarioId(),
                CanalNotificacao.valueOf(dto.canal()),
                dto.tipo(), dto.titulo(), dto.mensagem()
        );
        NotificacaoResponseDTO response = NotificacaoResponseDTO.fromDomain(criarUseCase.executar(notificacao));
        return ResponseEntity.created(URI.create("/notificacoes/" + response.id())).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(NotificacaoResponseDTO::fromDomain).toList());
    }
}
