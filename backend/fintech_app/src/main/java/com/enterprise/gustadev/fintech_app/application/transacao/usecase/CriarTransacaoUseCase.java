package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class CriarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CategoriaRepositoryPort categoriaRepository;

    public CriarTransacaoUseCase(TransacaoRepositoryPort repository,
                                  ContaFinanceiraRepositoryPort contaRepository,
                                  CategoriaRepositoryPort categoriaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Transacao executar(Transacao transacao) {
        Categoria categoria = categoriaRepository.buscarPorId(transacao.getCategoriaId())
                .orElseThrow(() -> new CategoriaInvalidaException(
                        "Categoria não encontrada: " + transacao.getCategoriaId()));
        transacao.setCategoriaTipo(categoria.getTipo());
        transacao.validar();
        Transacao salva = repository.salvar(transacao);

        ContaFinanceira conta = contaRepository.buscarPorId(transacao.getConta().getId())
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: " + transacao.getConta().getId()));
        conta.aplicarTransacao(transacao.tipoEfetivo(), transacao.getValor().abs());
        contaRepository.salvar(conta);

        return salva;
    }
}
