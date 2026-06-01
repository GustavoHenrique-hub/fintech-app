package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TransacaoRepositoryAdapter implements TransacaoRepositoryPort {

    private final TransacaoJpaRepository jpaRepository;

    public TransacaoRepositoryAdapter(TransacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        return jpaRepository.save(TransacaoEntity.fromDomain(transacao)).toDomain();
    }

    @Override
    public List<Transacao> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(usuarioId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public List<Transacao> listarPorConta(Long contaId) {
        return jpaRepository.findByContaIdAndDeletedAtIsNull(contaId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public List<Transacao> listarPorUsuarioNoPeriodoComCategoriaEConta(
            Long usuarioId, LocalDate inicio, LocalDate fim) {
        return jpaRepository
                .buscarPorUsuarioNoPeriodoComCategoriaEConta(usuarioId, inicio, fim)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(TransacaoEntity::toDomain);
    }

    @Override
    public Optional<Transacao> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(TransacaoEntity::toDomain);
    }

    @Override
    public void deletarPorId(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Transacao estornaTransacao(Transacao transacao) {
        return jpaRepository.save(TransacaoEntity.fromDomain(transacao)).toDomain();
    }
}
