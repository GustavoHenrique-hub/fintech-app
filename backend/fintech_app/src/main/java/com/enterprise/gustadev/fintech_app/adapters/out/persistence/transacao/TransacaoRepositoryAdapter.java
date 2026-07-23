package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria.CategoriaEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.ResumoPeriodo;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        return jpaRepository.findByConta_UsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(usuarioId)
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
    public Optional<Transacao> buscarTransacao(Long id, String code, Long contaId, String contaCode) {
        return jpaRepository
                .buscarParaEstorno(id, code, contaId, contaCode)
                .map(TransacaoEntity::toDomain);
    }

    @Override
    public Optional<Transacao> buscarEstornoDe(Long transacaoEstornadaId) {
        return jpaRepository.findByTransacaoEstornadaId(transacaoEstornadaId)
                .map(TransacaoEntity::toDomain);
    }

    @Override
    public ResumoPeriodo somarPorUsuarioContaNoPeriodo(Long usuarioId, String usuarioCode,
                                                        Long contaId, String contaCode,
                                                        LocalDate inicio, LocalDate fim) {
        // A query agrega sem GROUP BY, então sempre retorna exatamente uma linha —
        // mas o Spring Data JPA só projeta corretamente múltiplas colunas escalares
        // quando o método é declarado como List<Object[]> (Object[] direto expõe o
        // array da linha aninhado, e o cast para BigDecimal falha em runtime).
        List<Object[]> resultado = jpaRepository.somarPorUsuarioContaNoPeriodo(
                usuarioId, usuarioCode, contaId, contaCode, inicio, fim,
                TipoCategoria.RECEITA, TipoCategoria.GASTO, TipoCategoria.AMBOS);
        Object[] linha = resultado.get(0);
        return new ResumoPeriodo(usuarioId, contaId, inicio, fim,
                (BigDecimal) linha[0], (BigDecimal) linha[1]);
    }

    /**
     * Persiste a transação populando explicitamente os escalares {@code contaId} e
     * {@code contaCode} (a associação {@code conta} agora é apenas leitura, com
     * {@code insertable=false, updatable=false}). Também liga a instância gerenciada
     * de {@link ContaFinanceiraEntity} para que {@code toDomain()} funcione no retorno
     * do save sem disparar carregamento adicional. O mesmo é feito para
     * {@link CategoriaEntity}, já que {@code toDomain()} agora lê {@code categoria.getTipo()}
     * para resolver a direção (RECEITA/GASTO) da transação.
     */
    private Transacao persistir(Transacao transacao) {
        TransacaoEntity entity = TransacaoEntity.fromDomain(transacao);
        entity.setContaId(transacao.getConta().getId());
        entity.setContaCode(transacao.getConta().getCode());
        entity.setConta(entityManager.getReference(
                ContaFinanceiraEntity.class, transacao.getConta().getId()));
        entity.setCategoria(entityManager.getReference(
                CategoriaEntity.class, transacao.getCategoriaId()));
        return jpaRepository.save(entity).toDomain();
    }
}
