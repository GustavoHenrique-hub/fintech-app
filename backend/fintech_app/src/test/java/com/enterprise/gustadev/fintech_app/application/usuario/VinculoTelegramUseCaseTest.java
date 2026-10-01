package com.enterprise.gustadev.fintech_app.application.usuario;

import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioPorTelegramUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.GerarCodigoVinculoTelegramUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.VincularTelegramUseCase;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioNaoVinculadoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.VinculoTelegramInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.CodigoVinculoTelegramRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VinculoTelegramUseCaseTest {

    private static final Long CHAT_ID = 8939881994L;

    @Mock private UsuarioRepositoryPort usuarioRepository;
    @Mock private CodigoVinculoTelegramRepositoryPort codigoRepository;
    @Mock private ConsentimentoLgpdRepositoryPort consentimentoRepository;
    @Mock private ContaFinanceiraRepositoryPort contaRepository;

    private static Usuario usuario(Long id, Long chatId) {
        return new Usuario(id, "U0000" + id, "12345678900", "FULANO", "f@x.com", "hash",
                null, chatId, null, true, LocalDate.of(1990, 1, 1));
    }

    private static ConsentimentoLgpd consentimento(boolean consentido, OffsetDateTime criadoEm) {
        return new ConsentimentoLgpd(1L, "C00001", 1L, "U00001", TipoConsentimentoLgpd.bot_telegram,
                "1.0", consentido, null, criadoEm, null, null);
    }

    // ── Gerar código ─────────────────────────────────────────────────────

    @Test
    void gerar_deveExigirConsentimentoBotTelegram() {
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario(1L, null)));
        when(consentimentoRepository.listarPorUsuario(1L)).thenReturn(List.of());
        var useCase = new GerarCodigoVinculoTelegramUseCase(usuarioRepository, codigoRepository, consentimentoRepository);

        assertThatThrownBy(() -> useCase.executar(1L)).isInstanceOf(UsuarioInvalidoException.class);
        verify(codigoRepository, never()).salvar(any());
    }

    @Test
    void gerar_deveCriarCodigoValido_quandoHaConsentimento() {
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario(1L, null)));
        when(consentimentoRepository.listarPorUsuario(1L))
                .thenReturn(List.of(consentimento(true, OffsetDateTime.now())));
        when(codigoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        var useCase = new GerarCodigoVinculoTelegramUseCase(usuarioRepository, codigoRepository, consentimentoRepository);

        CodigoVinculoTelegram codigo = useCase.executar(1L);

        assertThat(codigo.getCodigo()).hasSize(10).matches("[A-Z2-9]+");
        assertThat(codigo.getUsuarioId()).isEqualTo(1L);
        assertThat(codigo.expirou()).isFalse();
    }

    // ── Vincular ─────────────────────────────────────────────────────────

    @Test
    void vincular_deveGravarChatIdEConsumirCodigo() {
        CodigoVinculoTelegram codigo = CodigoVinculoTelegram.novo(1L);
        when(codigoRepository.buscarPorCodigo(codigo.getCodigo())).thenReturn(Optional.of(codigo));
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario(1L, null)));
        when(usuarioRepository.buscarPorTelegramChatId(CHAT_ID)).thenReturn(Optional.empty());
        when(usuarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        var useCase = new VincularTelegramUseCase(usuarioRepository, codigoRepository);

        Usuario vinculado = useCase.executar(codigo.getCodigo().toLowerCase(), CHAT_ID);

        assertThat(vinculado.getTelegramChatID()).isEqualTo(CHAT_ID);
        assertThat(codigo.foiUsado()).isTrue();
    }

    @Test
    void vincular_deveMoverChatDeOutraConta() {
        CodigoVinculoTelegram codigo = CodigoVinculoTelegram.novo(1L);
        Usuario anterior = usuario(2L, CHAT_ID);
        when(codigoRepository.buscarPorCodigo(codigo.getCodigo())).thenReturn(Optional.of(codigo));
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario(1L, null)));
        when(usuarioRepository.buscarPorTelegramChatId(CHAT_ID)).thenReturn(Optional.of(anterior));
        when(usuarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        var useCase = new VincularTelegramUseCase(usuarioRepository, codigoRepository);

        useCase.executar(codigo.getCodigo(), CHAT_ID);

        assertThat(anterior.getTelegramChatID()).isNull();
        verify(usuarioRepository).salvar(anterior);
    }

    @Test
    void vincular_deveRecusarCodigoExpirado() {
        CodigoVinculoTelegram expirado = new CodigoVinculoTelegram(1L, "ABCDEFGHJK", 1L,
                OffsetDateTime.now().minusMinutes(20), OffsetDateTime.now().minusMinutes(10), null);
        when(codigoRepository.buscarPorCodigo("ABCDEFGHJK")).thenReturn(Optional.of(expirado));
        var useCase = new VincularTelegramUseCase(usuarioRepository, codigoRepository);

        assertThatThrownBy(() -> useCase.executar("ABCDEFGHJK", CHAT_ID))
                .isInstanceOf(VinculoTelegramInvalidoException.class);
        verify(usuarioRepository, never()).salvar(any());
    }

    @Test
    void vincular_deveRecusarCodigoJaUsado() {
        CodigoVinculoTelegram usado = CodigoVinculoTelegram.novo(1L);
        usado.marcarUsado();
        when(codigoRepository.buscarPorCodigo(usado.getCodigo())).thenReturn(Optional.of(usado));
        var useCase = new VincularTelegramUseCase(usuarioRepository, codigoRepository);

        assertThatThrownBy(() -> useCase.executar(usado.getCodigo(), CHAT_ID))
                .isInstanceOf(VinculoTelegramInvalidoException.class);
    }

    // ── Resolver usuário pelo chat ───────────────────────────────────────

    @Test
    void buscar_deveTratarConsentimentoRevogadoComoNaoVinculado() {
        when(usuarioRepository.buscarPorTelegramChatId(CHAT_ID)).thenReturn(Optional.of(usuario(1L, CHAT_ID)));
        when(consentimentoRepository.listarPorUsuario(1L)).thenReturn(List.of(
                consentimento(true, OffsetDateTime.now().minusDays(2)),
                consentimento(false, OffsetDateTime.now().minusDays(1))));
        var useCase = new BuscarUsuarioPorTelegramUseCase(usuarioRepository, consentimentoRepository, contaRepository);

        assertThatThrownBy(() -> useCase.executar(CHAT_ID)).isInstanceOf(UsuarioNaoVinculadoException.class);
    }

    @Test
    void buscar_deveRetornarUsuario_quandoVinculadoComConsentimento() {
        when(usuarioRepository.buscarPorTelegramChatId(CHAT_ID)).thenReturn(Optional.of(usuario(1L, CHAT_ID)));
        when(consentimentoRepository.listarPorUsuario(1L))
                .thenReturn(List.of(consentimento(true, OffsetDateTime.now())));
        when(contaRepository.listarPorUsuario(1L)).thenReturn(List.of());
        var useCase = new BuscarUsuarioPorTelegramUseCase(usuarioRepository, consentimentoRepository, contaRepository);

        BuscarUsuarioPorTelegramUseCase.Resultado resultado = useCase.executar(CHAT_ID);

        assertThat(resultado.usuario().getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.contaPadraoId()).isNull();
    }
}
