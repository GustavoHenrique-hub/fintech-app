package com.enterprise.gustadev.fintech_app.application.economia.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Registra um APORTE (saldoAtual -&gt; saldoEconomias) ou RESGATE
 * (saldoEconomias -&gt; saldoAtual) dentro da MESMA conta financeira.
 * Não gera receita nem gasto: a auditoria fica no ledger
 * {@link MovimentacaoEconomia}, separado de {@code Transacao}.
 */
public class RegistrarMovimentacaoEconomiaUseCase {

    private final MovimentacaoEconomiaRepositoryPort movimentacaoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;

    public RegistrarMovimentacaoEconomiaUseCase(MovimentacaoEconomiaRepositoryPort movimentacaoRepository,
                                                 ContaFinanceiraRepositoryPort contaRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public MovimentacaoEconomia executar(Long contaId, String contaCode,
                                          TipoMovimentacaoEconomia tipo,
                                          BigDecimal valor, String descricao) {
        ContaFinanceira conta = contaRepository.buscarPorIdECode(contaId, contaCode)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: " + contaId));

        BigDecimal valorAbs = valor != null ? valor.abs() : null;
        if (tipo == TipoMovimentacaoEconomia.APORTE) {
            conta.aportarEconomia(valorAbs);
        } else {
            conta.resgatarEconomia(valorAbs);
        }
        contaRepository.salvar(conta);

        MovimentacaoEconomia mov = new MovimentacaoEconomia(
                conta.getId(), conta.getCode(), tipo, valorAbs, descricao);
        mov.validar();
        return movimentacaoRepository.salvar(mov);
    }
}
