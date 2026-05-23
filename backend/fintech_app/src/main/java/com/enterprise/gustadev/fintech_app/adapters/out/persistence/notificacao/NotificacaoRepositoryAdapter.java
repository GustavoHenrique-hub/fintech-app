package com.enterprise.gustadev.fintech_app.adapters.out.persistence.notificacao;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.notificacao.port.NotificacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotificacaoRepositoryAdapter implements NotificacaoRepositoryPort {

    private final NotificacaoJpaRepository jpaRepository;

    public NotificacaoRepositoryAdapter(NotificacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        return jpaRepository.save(NotificacaoEntity.fromDomain(notificacao)).toDomain();
    }

    @Override
    public List<Notificacao> listarPorUsuario(UUID usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(NotificacaoEntity::toDomain).toList();
    }

    @Override
    public List<Notificacao> listarNaoEnviadas() {
        return jpaRepository.findByEnviadaFalse().stream()
                .map(NotificacaoEntity::toDomain).toList();
    }

    @Override
    public Optional<Notificacao> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(NotificacaoEntity::toDomain);
    }
}
