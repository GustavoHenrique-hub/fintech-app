package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarExtratoUseCaseTest {

    @Mock
    private ExtratoRepositoryPort repository;

    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;

    @InjectMocks
    private CriarExtratoUseCase useCase;

    private ContaFinanceira contaValida() {
        ContaFinanceira conta = new ContaFinanceira(1L, "USR001", TipoConta.corrente,
                1L, "BCO001", BigDecimal.ZERO, false);
        conta.setId(1L);
        conta.setCode("CTA001");
        return conta;
    }

    @Test
    void executar_deveSalvarExtrato_quandoHashInexistente() {
        ContaFinanceira conta = contaValida();
        Extrato salvo = new Extrato(1L, "USR001", 1L, "CTA001",
                "nubank.pdf", "uuid-123", "hash-abc");
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(conta));
        when(repository.buscarPorHash("hash-abc")).thenReturn(Optional.empty());
        when(repository.salvar(any())).thenReturn(salvo);

        useCase.executar(1L, 1L, "nubank.pdf", "uuid-123", "hash-abc");

        verify(repository).salvar(any(Extrato.class));
    }

    @Test
    void executar_deveLancarExcecao_quandoHashJaExistente() {
        ContaFinanceira conta = contaValida();
        Extrato existente = new Extrato(1L, "USR001", 1L, "CTA001",
                "nubank.pdf", "uuid-old", "hash-dup");
        existente.setStatus(StatusExtrato.concluido);
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(conta));
        when(repository.buscarPorHash("hash-dup")).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> useCase.executar(1L, 1L, "nubank.pdf", "uuid-123", "hash-dup"))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("duplicado");

        verify(repository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoContaNaoEncontrada() {
        when(contaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(1L, 99L, "f.pdf", "uuid", "hash"))
                .isInstanceOf(ContaFinanceiraInvalidaException.class);

        verify(repository, never()).buscarPorHash(anyString());
        verify(repository, never()).salvar(any());
    }
}
