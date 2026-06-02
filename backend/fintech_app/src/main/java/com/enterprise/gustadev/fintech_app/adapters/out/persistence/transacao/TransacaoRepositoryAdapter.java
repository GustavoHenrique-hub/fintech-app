package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TransacaoRepositoryAdapter implements TransacaoRepositoryPort {

    private final TransacaoJpaRepository jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransacaoRepositoryAdapter(TransacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Transacao salvar(Transacao transacao) {
        return persistir(transacao);
    }

    @Override
    public List<Transacao> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuario_IdUsuarioAndDeletedAtIsNullOrderByDataTransacaoDesc(usuarioId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public List<Transacao> listarPorConta(Long contaId) {
        return jpaRepository.findByConta_IdAndDeletedAtIsNull(contaId)
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
    public Optional<Transacao> buscarTransacao(Long id, String code, Long usuarioId, String usuarioCode, Long contaId, String contaCode){
        return jpaRepository
                .buscarParaEstorno(id, code, usuarioId, usuarioCode, contaId, contaCode)
                .map(TransacaoEntity::toDomain);
    }

    @Override
    public Optional<Transacao> buscarEstornoDe(Long transacaoEstornadaId) {
        return jpaRepository.findByTransacaoEstornadaId(transacaoEstornadaId)
                .map(TransacaoEntity::toDomain);
    }

    /**
     * Persiste a transação ligando usuário e conta por referências gerenciadas
     * (resolvidas via EntityManager a partir da chave do domínio), evitando que o
     * Hibernate tente persistir instâncias transientes das entidades associadas.
     */
    private Transacao persistir(Transacao transacao) {
        TransacaoEntity entity = TransacaoEntity.fromDomain(transacao);
        entity.setUsuario(entityManager.getReference(
                UsuarioEntity.class, transacao.getUsuario().getIdUsuario()));
        entity.setConta(entityManager.getReference(
                ContaFinanceiraEntity.class, transacao.getConta().getId()));
        return jpaRepository.save(entity).toDomain();
    }
}
