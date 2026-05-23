package com.enterprise.gustadev.fintech_app.config;

import com.enterprise.gustadev.fintech_app.application.categoria.usecase.BuscarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.CriarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.ListarCategoriasUseCase;
import com.enterprise.gustadev.fintech_app.application.classificacaolog.usecase.ListarClassificacaoLogsUseCase;
import com.enterprise.gustadev.fintech_app.application.classificacaolog.usecase.RegistrarClassificacaoLogUseCase;
import com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase.ListarConsentimentosLgpdUseCase;
import com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase.RegistrarConsentimentoLgpdUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.DeletarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.BuscarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ListarExtratosUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.CriarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.DeletarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.ListarGastosUseCase;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.CriarNotificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.notificacao.usecase.ListarNotificacoesUseCase;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.CriarRegraClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase.ListarRegrasClassificacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.BuscarSnapshotFinanceiroUseCase;
import com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase.ListarSnapshotsFinanceirosUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.DeletarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.CriarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.DeletarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.ListarUsuariosUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.classificacaolog.port.ClassificacaoLogRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.notificacao.port.NotificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.port.RegraClassificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port.SnapshotFinanceiroRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // ── Gasto (legado) ──────────────────────────────────────────────────
    @Bean
    public CriarGastoUseCase criarGastoUseCase(GastoRepositoryPort repository) {
        return new CriarGastoUseCase(repository);
    }

    @Bean
    public ListarGastosUseCase listarGastosUseCase(GastoRepositoryPort repository) {
        return new ListarGastosUseCase(repository);
    }

    @Bean
    public DeletarGastoUseCase deletarGastoUseCase(GastoRepositoryPort repository) {
        return new DeletarGastoUseCase(repository);
    }

    // ── Usuario ─────────────────────────────────────────────────────────
    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioRepositoryPort repository) {
        return new CriarUsuarioUseCase(repository);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepositoryPort repository) {
        return new ListarUsuariosUseCase(repository);
    }

    @Bean
    public BuscarUsuarioUseCase buscarUsuarioUseCase(UsuarioRepositoryPort repository) {
        return new BuscarUsuarioUseCase(repository);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioRepositoryPort repository) {
        return new DeletarUsuarioUseCase(repository);
    }

    // ── ContaFinanceira ──────────────────────────────────────────────────
    @Bean
    public CriarContaFinanceiraUseCase criarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        return new CriarContaFinanceiraUseCase(repository);
    }

    @Bean
    public ListarContasFinanceirasUseCase listarContasFinanceirasUseCase(ContaFinanceiraRepositoryPort repository) {
        return new ListarContasFinanceirasUseCase(repository);
    }

    @Bean
    public BuscarContaFinanceiraUseCase buscarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        return new BuscarContaFinanceiraUseCase(repository);
    }

    @Bean
    public DeletarContaFinanceiraUseCase deletarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        return new DeletarContaFinanceiraUseCase(repository);
    }

    // ── Categoria ────────────────────────────────────────────────────────
    @Bean
    public CriarCategoriaUseCase criarCategoriaUseCase(CategoriaRepositoryPort repository) {
        return new CriarCategoriaUseCase(repository);
    }

    @Bean
    public ListarCategoriasUseCase listarCategoriasUseCase(CategoriaRepositoryPort repository) {
        return new ListarCategoriasUseCase(repository);
    }

    @Bean
    public BuscarCategoriaUseCase buscarCategoriaUseCase(CategoriaRepositoryPort repository) {
        return new BuscarCategoriaUseCase(repository);
    }

    // ── Extrato ──────────────────────────────────────────────────────────
    @Bean
    public CriarExtratoUseCase criarExtratoUseCase(ExtratoRepositoryPort repository) {
        return new CriarExtratoUseCase(repository);
    }

    @Bean
    public ListarExtratosUseCase listarExtratosUseCase(ExtratoRepositoryPort repository) {
        return new ListarExtratosUseCase(repository);
    }

    @Bean
    public BuscarExtratoUseCase buscarExtratoUseCase(ExtratoRepositoryPort repository) {
        return new BuscarExtratoUseCase(repository);
    }

    // ── Transacao ────────────────────────────────────────────────────────
    @Bean
    public CriarTransacaoUseCase criarTransacaoUseCase(TransacaoRepositoryPort repository) {
        return new CriarTransacaoUseCase(repository);
    }

    @Bean
    public ListarTransacoesUseCase listarTransacoesUseCase(TransacaoRepositoryPort repository) {
        return new ListarTransacoesUseCase(repository);
    }

    @Bean
    public BuscarTransacaoUseCase buscarTransacaoUseCase(TransacaoRepositoryPort repository) {
        return new BuscarTransacaoUseCase(repository);
    }

    @Bean
    public DeletarTransacaoUseCase deletarTransacaoUseCase(TransacaoRepositoryPort repository) {
        return new DeletarTransacaoUseCase(repository);
    }

    // ── RegraClassificacao ───────────────────────────────────────────────
    @Bean
    public CriarRegraClassificacaoUseCase criarRegraClassificacaoUseCase(RegraClassificacaoRepositoryPort repository) {
        return new CriarRegraClassificacaoUseCase(repository);
    }

    @Bean
    public ListarRegrasClassificacaoUseCase listarRegrasClassificacaoUseCase(RegraClassificacaoRepositoryPort repository) {
        return new ListarRegrasClassificacaoUseCase(repository);
    }

    // ── ClassificacaoLog ─────────────────────────────────────────────────
    @Bean
    public RegistrarClassificacaoLogUseCase registrarClassificacaoLogUseCase(ClassificacaoLogRepositoryPort repository) {
        return new RegistrarClassificacaoLogUseCase(repository);
    }

    @Bean
    public ListarClassificacaoLogsUseCase listarClassificacaoLogsUseCase(ClassificacaoLogRepositoryPort repository) {
        return new ListarClassificacaoLogsUseCase(repository);
    }

    // ── SnapshotFinanceiro ───────────────────────────────────────────────
    @Bean
    public ListarSnapshotsFinanceirosUseCase listarSnapshotsFinanceirosUseCase(SnapshotFinanceiroRepositoryPort repository) {
        return new ListarSnapshotsFinanceirosUseCase(repository);
    }

    @Bean
    public BuscarSnapshotFinanceiroUseCase buscarSnapshotFinanceiroUseCase(SnapshotFinanceiroRepositoryPort repository) {
        return new BuscarSnapshotFinanceiroUseCase(repository);
    }

    // ── Notificacao ──────────────────────────────────────────────────────
    @Bean
    public CriarNotificacaoUseCase criarNotificacaoUseCase(NotificacaoRepositoryPort repository) {
        return new CriarNotificacaoUseCase(repository);
    }

    @Bean
    public ListarNotificacoesUseCase listarNotificacoesUseCase(NotificacaoRepositoryPort repository) {
        return new ListarNotificacoesUseCase(repository);
    }

    // ── ConsentimentoLgpd ────────────────────────────────────────────────
    @Bean
    public RegistrarConsentimentoLgpdUseCase registrarConsentimentoLgpdUseCase(ConsentimentoLgpdRepositoryPort repository) {
        return new RegistrarConsentimentoLgpdUseCase(repository);
    }

    @Bean
    public ListarConsentimentosLgpdUseCase listarConsentimentosLgpdUseCase(ConsentimentoLgpdRepositoryPort repository) {
        return new ListarConsentimentosLgpdUseCase(repository);
    }
}
