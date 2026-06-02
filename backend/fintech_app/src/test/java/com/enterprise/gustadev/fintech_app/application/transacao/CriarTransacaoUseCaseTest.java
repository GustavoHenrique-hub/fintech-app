package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarTransacaoUseCaseTest {

    @Mock
    private TransacaoRepositoryPort repository;

    @InjectMocks
    private CriarTransacaoUseCase useCase;

    @Test
    void executar_deveSalvarTransacao_quandoDadosValidos() {
        Long usuarioId = 1L;
        Long contaId = 1L;
        Transacao transacao = new Transacao(
                new Usuario(usuarioId, "U1"), new ContaFinanceira(contaId, "C1"), TipoTransacao.GASTO,
                new BigDecimal("150.00"), LocalDate.now(), 1L, OrigemTransacao.manual
        );
        Transacao salva = new Transacao(1L, new Usuario(usuarioId, "U1"), new ContaFinanceira(contaId, "C1"), "N",
                TipoTransacao.GASTO, null, new BigDecimal("150.00"),
                LocalDate.now(), 1L, null, OrigemTransacao.manual,
                StatusRevisaoTransacao.EXTRAIDA, null, false, null, null, null, 1, null, null);
        when(repository.salvar(any())).thenReturn(salva);

        Transacao resultado = useCase.executar(transacao);

        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getValor()).isEqualByComparingTo("150.00");
        verify(repository).salvar(transacao);
    }

    @Test
    void executar_naoDeveSalvar_quandoValorInvalido() {
        Transacao transacaoInvalida = new Transacao(
                new Usuario(1L, "U1"), new ContaFinanceira(1L, "C1"), TipoTransacao.GASTO,
                BigDecimal.ZERO, LocalDate.now(), 1L, OrigemTransacao.manual
        );

        assertThatThrownBy(() -> useCase.executar(transacaoInvalida))
                .isInstanceOf(TransacaoInvalidaException.class);

        verify(repository, never()).salvar(any());
    }
}
