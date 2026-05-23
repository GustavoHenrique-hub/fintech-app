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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTransacoesUseCaseTest {

    @Mock
    private TransacaoRepositoryPort repository;

    @InjectMocks
    private ListarTransacoesUseCase useCase;

    private Transacao criarTransacao(UUID usuarioId, UUID contaId) {
        return new Transacao(usuarioId, contaId, TipoTransacao.gasto,
                new BigDecimal("50.00"), LocalDate.now(), OrigemTransacao.manual);
    }

    @Test
    void executarPorUsuario_deveRetornarTransacoesDoUsuario() {
        UUID usuarioId = UUID.randomUUID();
        List<Transacao> lista = List.of(criarTransacao(usuarioId, UUID.randomUUID()));
        when(repository.listarPorUsuario(usuarioId)).thenReturn(lista);

        List<Transacao> resultado = useCase.executarPorUsuario(usuarioId);

        assertThat(resultado).hasSize(1);
        verify(repository).listarPorUsuario(usuarioId);
    }

    @Test
    void executarPorConta_deveRetornarTransacoesDaConta() {
        UUID contaId = UUID.randomUUID();
        List<Transacao> lista = List.of(criarTransacao(UUID.randomUUID(), contaId));
        when(repository.listarPorConta(contaId)).thenReturn(lista);

        List<Transacao> resultado = useCase.executarPorConta(contaId);

        assertThat(resultado).hasSize(1);
        verify(repository).listarPorConta(contaId);
    }

    @Test
    void executarPorExtrato_deveRetornarTransacoesDoExtrato() {
        UUID extratoId = UUID.randomUUID();
        when(repository.listarPorExtrato(extratoId)).thenReturn(List.of());

        List<Transacao> resultado = useCase.executarPorExtrato(extratoId);

        assertThat(resultado).isEmpty();
        verify(repository).listarPorExtrato(extratoId);
    }
}
