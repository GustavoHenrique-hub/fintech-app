package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.application.classificacao.CalibradorConfiancaIa;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

/**
 * "Estornar revisão": devolve um lançamento CONFIRMADO para PENDENTE_REVISAO.
 *
 * <p>Diferente do estorno financeiro ({@link EstornarTransacaoUseCase}), nada é
 * revertido no banco — o usuário só está dizendo que a classificação estava errada.
 * Por isso o valor sai do saldo da conta (volta a valer quando for confirmado de novo),
 * o extrato reabre o lançamento e a confiança da IA cai, junto com o aprendizado
 * daquela descrição.
 */
public class DesfazerRevisaoTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepository;
    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CalibradorConfiancaIa calibrador;

    public DesfazerRevisaoTransacaoUseCase(TransacaoRepositoryPort transacaoRepository,
                                            ExtratoRepositoryPort extratoRepository,
                                            ContaFinanceiraRepositoryPort contaRepository,
                                            CalibradorConfiancaIa calibrador) {
        this.transacaoRepository = transacaoRepository;
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.calibrador = calibrador;
    }

    @Transactional
    public Transacao executar(Long id, String code) {
        Transacao transacao = transacaoRepository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(
                        "Transação não encontrada: id=" + id + ", code=" + code));

        transacao.desfazerRevisao();

        if (transacao.saldoJaAplicado()) {
            Long contaId = transacao.getConta().getId();
            ContaFinanceira conta = contaRepository.buscarPorId(contaId)
                    .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                            "Conta financeira não encontrada: " + contaId));
            conta.reverterTransacao(transacao.tipoEfetivo(), transacao.getValor().abs());
            contaRepository.salvar(conta);
            transacao.setSaldoAplicado(false);
        }

        calibrador.registrarEstornoRevisao(transacao.getConta().getUsuarioId(), transacao);
        Transacao salva = transacaoRepository.salvar(transacao);

        if (transacao.getExtratoId() != null) {
            Extrato extrato = extratoRepository.buscarPorId(transacao.getExtratoId())
                    .orElseThrow(() -> new ExtratoInvalidoException(
                            "Extrato não encontrado: " + transacao.getExtratoId()));
            extrato.reabrirLancamento();
            extratoRepository.salvar(extrato);
        }
        return salva;
    }
}
