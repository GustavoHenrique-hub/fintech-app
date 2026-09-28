package com.enterprise.gustadev.fintech_app.application.classificacao;

import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;
import com.enterprise.gustadev.fintech_app.domain.classificacao.port.AprendizadoClassificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.util.Optional;

/**
 * Liga a confiança da IA ao comportamento do usuário:
 *
 * <ul>
 *   <li><b>Leitura do extrato</b> ({@link #calibrar}) — a confiança crua que a automação
 *       devolve é somada ao ajuste aprendido para a mesma descrição. A partir de
 *       {@link #LIMIAR_CLASSIFICACAO_AUTOMATICA}% o lançamento é confirmado sem revisão.</li>
 *   <li><b>Revisão confirmada</b> ({@link #registrarConfirmacao}) — a confiança do lançamento
 *       sobe e o aprendizado ganha um acerto.</li>
 *   <li><b>Revisão estornada</b> ({@link #registrarEstornoRevisao}) — a confiança do lançamento
 *       cai e o aprendizado ganha um erro, o que afasta aquela descrição da confirmação automática.</li>
 * </ul>
 *
 * Confirmações automáticas não alimentam o aprendizado: só decisões do usuário contam,
 * senão a IA reforçaria as próprias escolhas.
 */
public class CalibradorConfiancaIa {

    public static final int LIMIAR_CLASSIFICACAO_AUTOMATICA = 95;
    public static final int PASSO_CONFIRMACAO = 5;
    public static final int PASSO_ESTORNO_REVISAO = 15;

    private final AprendizadoClassificacaoRepositoryPort repository;

    public CalibradorConfiancaIa(AprendizadoClassificacaoRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Resultado da calibração. {@code categoriaId}/{@code categoriaCode} vêm preenchidos
     * quando o usuário já confirmou essa descrição, na mesma direção, com uma categoria.
     */
    public record Calibracao(Short confianca, Long categoriaId, String categoriaCode) {
        public boolean classificaAutomaticamente() {
            return confianca != null && confianca >= LIMIAR_CLASSIFICACAO_AUTOMATICA;
        }
    }

    public Calibracao calibrar(Long usuarioId, String descricao, String estabelecimento,
                               TipoTransacao tipoSugerido, Short confiancaIa) {
        if (confiancaIa == null) {
            return new Calibracao(null, null, null);
        }
        Optional<AprendizadoClassificacao> aprendizado = buscar(usuarioId, chaveDe(descricao, estabelecimento));
        if (aprendizado.isEmpty()) {
            return new Calibracao(limitar(confiancaIa), null, null);
        }

        AprendizadoClassificacao a = aprendizado.get();
        if (a.getTipo() != tipoSugerido) {
            return new Calibracao(limitar(confiancaIa - AprendizadoClassificacao.PENALIDADE_DIVERGENCIA), null, null);
        }
        return new Calibracao(limitar(confiancaIa + a.ajuste()), a.getCategoriaId(), a.getCategoriaCode());
    }

    /** A revisão foi confirmada pelo usuário: a confiança sobe e a escolha vira aprendizado. */
    public void registrarConfirmacao(Long usuarioId, Transacao transacao) {
        transacao.ajustarConfianca(PASSO_CONFIRMACAO);

        String chave = chaveDe(transacao.getDescricao(), transacao.getEstabelecimento());
        if (usuarioId == null || chave == null) return;
        TipoTransacao tipo = transacao.tipoEfetivo();
        AprendizadoClassificacao aprendizado = repository.buscarPorUsuarioEChave(usuarioId, chave)
                .orElseGet(() -> new AprendizadoClassificacao(usuarioId, chave, tipo));
        aprendizado.registrarAcerto(tipo, transacao.getCategoriaId(), transacao.getCategoriaCode());
        repository.salvar(aprendizado);
    }

    /** A revisão confirmada foi estornada: a confiança cai e o aprendizado registra o erro. */
    public void registrarEstornoRevisao(Long usuarioId, Transacao transacao) {
        transacao.ajustarConfianca(-PASSO_ESTORNO_REVISAO);

        buscar(usuarioId, chaveDe(transacao.getDescricao(), transacao.getEstabelecimento()))
                .ifPresent(aprendizado -> {
                    aprendizado.registrarErro();
                    repository.salvar(aprendizado);
                });
    }

    private Optional<AprendizadoClassificacao> buscar(Long usuarioId, String chave) {
        if (usuarioId == null || chave == null) return Optional.empty();
        return repository.buscarPorUsuarioEChave(usuarioId, chave);
    }

    /** A descrição do extrato já traz o estabelecimento; ele só é usado quando ela falta. */
    public static String chaveDe(String descricao, String estabelecimento) {
        String chave = AprendizadoClassificacao.normalizarChave(descricao);
        return chave != null ? chave : AprendizadoClassificacao.normalizarChave(estabelecimento);
    }

    private static Short limitar(int valor) {
        return (short) Math.max(0, Math.min(100, valor));
    }
}
