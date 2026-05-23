package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTransacoesUseCaseTest {

    @Mock
    private TransacaoRepositoryPort repository;

    @InjectMocks
    private ListarTransacoesUseCase useCase;

    private Transacao criarTransacao(Long usuarioId, Long contaId) {
        return new Transacao(usuarioId, contaId, TipoTransacao.GASTO,
                new BigDecimal("50.00"), LocalDate.now(), OrigemTransacao.manual);
    }

    @Test
    void executarPorUsuario_deveRetornarTransacoesDoUsuario() {
        Long usuarioId = 1L;
        List<Transacao> lista = List.of(criarTransacao(usuarioId, 1L));
        when(repository.listarPorUsuario(usuarioId)).thenReturn(lista);

        List<Transacao> resultado = useCase.executarPorUsuario(usuarioId);

        assertThat(resultado).hasSize(1);
        verify(repository).listarPorUsuario(usuarioId);
    }

    @Test
    void executarPorConta_deveRetornarTransacoesDaConta() {
        Long contaId = 1L;
        List<Transacao> lista = List.of(criarTransacao(1L, contaId));
        when(repository.listarPorConta(contaId)).thenReturn(lista);

        List<Transacao> resultado = useCase.executarPorConta(contaId);

        assertThat(resultado).hasSize(1);
        verify(repository).listarPorConta(contaId);
    }

    @Test
    void executarPorExtrato_deveRetornarTransacoesDoExtrato() {
        Long extratoId = 1L;
        when(repository.listarPorExtrato(extratoId)).thenReturn(List.of());

        List<Transacao> resultado = useCase.executarPorExtrato(extratoId);

        assertThat(resultado).isEmpty();
        verify(repository).listarPorExtrato(extratoId);
    }
}
