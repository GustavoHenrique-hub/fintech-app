package com.enterprise.gustadev.fintech_app.config;

import com.enterprise.gustadev.fintech_app.application.gasto.usecase.CriarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.DeletarGastoUseCase;
import com.enterprise.gustadev.fintech_app.application.gasto.usecase.ListarGastosUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.CriarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.DeletarUsuarioUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.ListarUsuariosUseCase;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

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
}
