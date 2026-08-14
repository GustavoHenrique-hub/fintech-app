package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.ResultadoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Aplica o resultado que a automação (N8N + IA) devolve para um extrato: cria as
 * transações extraídas com {@code statusRevisao=PENDENTE_REVISAO}, ajusta o saldo
 * da conta e fecha os contadores do extrato.
 *
 * <p>É idempotente: o N8N reenvia o callback em caso de timeout (ele manda
 * {@code X-Idempotency-Key}), então um extrato que já saiu do processamento é
 * devolvido sem alteração — nada de lançamento duplicado.
 */
public class RegistrarResultadoExtratoUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarResultadoExtratoUseCase.class);

    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CategoriaRepositoryPort categoriaRepository;
    private final TransacaoRepositoryPort transacaoRepository;

    public RegistrarResultadoExtratoUseCase(ExtratoRepositoryPort extratoRepository,
                                             ContaFinanceiraRepositoryPort contaRepository,
                                             CategoriaRepositoryPort categoriaRepository,
                                             TransacaoRepositoryPort transacaoRepository) {
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public Extrato executar(Long extratoId, ResultadoProcessamentoExtrato resultado) {
        Extrato extrato = extratoRepository.buscarPorId(extratoId)
                .orElseThrow(() -> new ExtratoInvalidoException("Extrato não encontrado: " + extratoId));

        if (!extrato.emProcessamento()) {
            log.info("Callback ignorado: extrato {} já está em {} (reenvio do N8N)", extratoId, extrato.getStatus());
            return extrato;
        }

        StatusExtrato status = statusReportado(resultado.status());
        if (status != null && status.name().startsWith("erro")) {
            extrato.registrarErroProcessamento(status);
            return extratoRepository.salvar(extrato);
        }

        ContaFinanceira conta = contaRepository.buscarPorId(extrato.getContaId())
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira do extrato não encontrada: " + extrato.getContaId()));

        CatalogoCategoriasImportacao catalogo = CatalogoCategoriasImportacao.carregar(categoriaRepository);
        Categoria fallback = catalogo.fallback();

        List<ResultadoProcessamentoExtrato.LancamentoProcessado> lancamentos =
                resultado.lancamentos() != null ? resultado.lancamentos() : List.of();

        int criadas = 0;
        for (ResultadoProcessamentoExtrato.LancamentoProcessado lancamento : lancamentos) {
            if (lancamento.valor() == null || lancamento.valor().signum() == 0
                    || lancamento.dataTransacao() == null || lancamento.tipo() == null) {
                continue;
            }

            Categoria categoria = catalogo
                    .porNomeSugerido(lancamento.categoriaSugerida(), lancamento.tipo())
                    .orElse(fallback);

            BigDecimal valorAbsoluto = lancamento.valor().abs();
            // Na categoria genérica (AMBOS) é o sinal do valor que carrega a direção;
            // numa categoria de tipo fixo o valor é sempre positivo.
            BigDecimal valor = categoria.getTipo() == TipoCategoria.AMBOS
                    && lancamento.tipo() == TipoTransacao.GASTO
                    ? valorAbsoluto.negate()
                    : valorAbsoluto;

            Transacao transacao = new Transacao(conta, valor, lancamento.dataTransacao(),
                    categoria.getId(), categoria.getCode(), OrigemTransacao.importado);
            transacao.setDescricao(lancamento.descricao());
            transacao.setEstabelecimento(lancamento.estabelecimento());
            transacao.setObservacao(lancamento.observacao());
            transacao.setCategoriaTipo(categoria.getTipo());
            transacao.setConfiancaIa(lancamento.confiancaIa());
            transacao.setStatusRevisao(StatusRevisaoTransacao.PENDENTE_REVISAO);
            transacao.setExtratoId(extrato.getId());
            transacao.setExtratoCode(extrato.getCode());
            transacao.validar();

            transacaoRepository.salvar(transacao);
            conta.aplicarTransacao(transacao.tipoEfetivo(), valorAbsoluto);
            criadas++;
        }
        contaRepository.salvar(conta);

        extrato.registrarResultadoProcessamento(resultado.bancoDetectado(),
                resultado.periodoInicio(), resultado.periodoFim(), criadas);
        log.info("Extrato {} processado pela automação: {} lançamento(s) pendentes de revisão", extratoId, criadas);
        return extratoRepository.salvar(extrato);
    }

    /** Aceita o status cru do callback; desconhecido vira {@code null} (segue o fluxo de sucesso). */
    private StatusExtrato statusReportado(String status) {
        if (status == null || status.isBlank()) return null;
        try {
            return StatusExtrato.valueOf(status.trim().toLowerCase());
        } catch (IllegalArgumentException e) {
            log.warn("Status desconhecido no callback do extrato: {}", status);
            return null;
        }
    }
}
