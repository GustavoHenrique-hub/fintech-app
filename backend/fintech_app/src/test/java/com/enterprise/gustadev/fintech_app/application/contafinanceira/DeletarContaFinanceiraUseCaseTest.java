package com.enterprise.gustadev.fintech_app.application.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.RemoverContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletarContaFinanceiraUseCaseTest {

    @Mock
    private ContaFinanceiraRepositoryPort repository;

    @InjectMocks
    private RemoverContaFinanceiraUseCase useCase;

    @Test
    void executar_deveDelegarRemoverAoRepository() {
        Long id = 1L;
        String code = "ABC123";
        ContaFinanceira conta = new ContaFinanceira(id, 1L,
                TipoConta.corrente, 10L, "BNK001", BigDecimal.TEN, false, true, null, null);
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.of(conta));

        useCase.executar(id, code);

        verify(repository).deletarPorId(id);
    }
}
