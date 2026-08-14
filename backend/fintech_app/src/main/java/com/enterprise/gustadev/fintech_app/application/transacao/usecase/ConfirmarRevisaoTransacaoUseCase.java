package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CatalogoCategoriasImportacao;
import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.DestinoRevisaoLancamento;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Fecha a revisão manual de um lançamento importado de extrato. O usuário escolhe o
 * destino do lançamento na tela de revisão:
 *
 * <ul>
 *   <li>{@code GASTO}/{@code RECEITA} — a transação é CONFIRMADA e passa a valer na aba
 *       "Transações". Se a direção escolhida for diferente da que veio do extrato, a
 *       categoria (ou o sinal do valor, na categoria genérica) é ajustada e o saldo da
 *       conta é recalculado.</li>
 *   <li>{@code ECONOMIA} — o valor vira uma movimentação do sub-saldo de economias da
 *       conta e a transação sai das listagens (IGNORADA), para não contar duas vezes
 *       em receitas/gastos.</li>
 * </ul>
 *
 * Em qualquer caso os contadores agregados do extrato são atualizados.
 */
public class ConfirmarRevisaoTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepository;
    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CategoriaRepositoryPort categoriaRepository;
    private final MovimentacaoEconomiaRepositoryPort movimentacaoEconomiaRepository;

    public ConfirmarRevisaoTransacaoUseCase(TransacaoRepositoryPort transacaoRepository,
                                             ExtratoRepositoryPort extratoRepository,
                                             ContaFinanceiraRepositoryPort contaRepository,
                                             CategoriaRepositoryPort categoriaRepository,
                                             MovimentacaoEconomiaRepositoryPort movimentacaoEconomiaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimentacaoEconomiaRepository = movimentacaoEconomiaRepository;
    }

    /** Confirma mantendo a classificação que veio do extrato. */
    @Transactional
    public Transacao executar(Long id, String code) {
        return executar(id, code, null, null, null);
    }

    @Transactional
    public Transacao executar(Long id, String code, DestinoRevisaoLancamento destino,
                               Long categoriaId, String categoriaCode) {
        Transacao transacao = transacaoRepository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(
                        "Transação não encontrada: id=" + id + ", code=" + code));

        if (destino == DestinoRevisaoLancamento.ECONOMIA) {
            return converterEmEconomia(transacao);
        }
        return confirmarComoTransacao(transacao, destino, categoriaId, categoriaCode);
    }

    private Transacao confirmarComoTransacao(Transacao transacao, DestinoRevisaoLancamento destino,
                                              Long categoriaId, String categoriaCode) {
        TipoTransacao direcaoAtual = transacao.tipoEfetivo();
        TipoTransacao direcaoEscolhida = destino == null
                ? direcaoAtual
                : TipoTransacao.valueOf(destino.name());

        Categoria categoria = resolverCategoria(transacao, direcaoEscolhida, categoriaId, categoriaCode);
        if (categoria != null) {
            transacao.setCategoriaId(categoria.getId());
            transacao.setCategoriaCode(categoria.getCode());
            transacao.setCategoriaTipo(categoria.getTipo());
        }

        // Só a categoria genérica (AMBOS) carrega a direção no sinal do valor.
        BigDecimal valorAbsoluto = transacao.getValor().abs();
        if (transacao.getCategoriaTipo() == TipoCategoria.AMBOS) {
            transacao.setValor(direcaoEscolhida == TipoTransacao.GASTO ? valorAbsoluto.negate() : valorAbsoluto);
        } else {
            transacao.setValor(valorAbsoluto);
        }
        transacao.validar();

        TipoTransacao direcaoFinal = transacao.tipoEfetivo();
        if (direcaoFinal != direcaoAtual) {
            ContaFinanceira conta = carregarConta(transacao);
            conta.reverterTransacao(direcaoAtual, valorAbsoluto);
            conta.aplicarTransacao(direcaoFinal, valorAbsoluto);
            contaRepository.salvar(conta);
        }

        transacao.confirmarRevisao();
        Transacao salva = transacaoRepository.salvar(transacao);
        atualizarExtrato(transacao, Extrato::confirmarLancamento);
        return salva;
    }

    /**
     * Converte o lançamento em aporte de economias. O dinheiro sai do saldo disponível
     * e entra no sub-saldo de economias da mesma conta:
     *
     * <ul>
     *   <li>lançamento de saída (GASTO): o efeito no saldo é desfeito e reaplicado como
     *       aporte — o saldo disponível termina no mesmo lugar, mas o valor passa a
     *       aparecer como reserva, não como despesa;</li>
     *   <li>lançamento de entrada (RECEITA): o crédito é mantido (o dinheiro entrou
     *       mesmo) e o aporte o transfere para economias.</li>
     * </ul>
     */
    private Transacao converterEmEconomia(Transacao transacao) {
        ContaFinanceira conta = carregarConta(transacao);
        BigDecimal valorAbsoluto = transacao.getValor().abs();

        if (transacao.tipoEfetivo() == TipoTransacao.GASTO) {
            conta.reverterTransacao(TipoTransacao.GASTO, valorAbsoluto);
        }
        conta.aportarEconomia(valorAbsoluto);
        contaRepository.salvar(conta);

        MovimentacaoEconomia movimentacao = new MovimentacaoEconomia(
                conta.getId(), conta.getCode(), TipoMovimentacaoEconomia.APORTE,
                valorAbsoluto, transacao.getDescricao());
        movimentacao.validar();
        movimentacaoEconomiaRepository.salvar(movimentacao);

        transacao.ignorar();
        Transacao salva = transacaoRepository.salvar(transacao);
        atualizarExtrato(transacao, Extrato::ignorarLancamento);
        return salva;
    }

    /**
     * Devolve a categoria a gravar, ou {@code null} quando a atual já serve.
     * Categoria informada precisa ser compatível com a direção escolhida; quando o
     * usuário troca a direção sem escolher categoria, cai na genérica (AMBOS), onde o
     * sinal do valor resolve.
     */
    private Categoria resolverCategoria(Transacao transacao, TipoTransacao direcao,
                                         Long categoriaId, String categoriaCode) {
        if (categoriaId != null) {
            Categoria categoria = (categoriaCode != null && !categoriaCode.isBlank()
                    ? categoriaRepository.buscarPorIdECode(categoriaId, categoriaCode)
                    : categoriaRepository.buscarPorId(categoriaId))
                    .orElseThrow(() -> new CategoriaInvalidaException("Categoria não encontrada: " + categoriaId));
            if (!compativel(categoria.getTipo(), direcao)) {
                throw new TransacaoInvalidaException(
                        "Categoria '" + categoria.getNome() + "' não é compatível com um lançamento de " + direcao);
            }
            return categoria;
        }

        if (compativel(transacao.getCategoriaTipo(), direcao)) {
            return null;
        }
        return CatalogoCategoriasImportacao.carregar(categoriaRepository).fallback();
    }

    private static boolean compativel(TipoCategoria tipoCategoria, TipoTransacao direcao) {
        if (tipoCategoria == null) return false;
        return switch (tipoCategoria) {
            case AMBOS -> true;
            case RECEITA -> direcao == TipoTransacao.RECEITA;
            case GASTO -> direcao == TipoTransacao.GASTO;
        };
    }

    private ContaFinanceira carregarConta(Transacao transacao) {
        Long contaId = transacao.getConta().getId();
        return contaRepository.buscarPorId(contaId)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: " + contaId));
    }

    private void atualizarExtrato(Transacao transacao, Consumer<Extrato> acao) {
        if (transacao.getExtratoId() == null) return;
        Extrato extrato = extratoRepository.buscarPorId(transacao.getExtratoId())
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Extrato não encontrado: " + transacao.getExtratoId()));
        acao.accept(extrato);
        extratoRepository.salvar(extrato);
    }
}
