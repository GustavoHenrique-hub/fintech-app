package com.enterprise.gustadev.fintech_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fintech App API")
                        .description("""
                                API RESTful para gerenciamento financeiro pessoal.

                                Permite controle de **bancos**, **contas financeiras** (corrente, poupança, \
                                investimento etc.), **transações**, **categorias**, **extratos**, **snapshots \
                                financeiros**, **notificações** e **consentimentos LGPD**.

                                ### Vínculo Usuário ↔ Conta ↔ Banco
                                Um **usuário** pode possuir várias **contas financeiras**, e cada conta é \
                                obrigatoriamente vinculada a um **banco** já cadastrado. \
                                Um mesmo banco pode estar vinculado a contas de vários usuários (relação N:N \
                                resolvida pela tabela `contas_financeiras`).

                                Fluxo recomendado:
                                1. `POST /bancos` para cadastrar o banco (retorna `banco_id` e `banco_code`).
                                2. `POST /usuarios` para criar o usuário (retorna `usuario_id`).
                                3. `POST /contas` informando `usuarioId`, `bancoId` e `bancoCode` para \
                                vincular o usuário ao banco através da conta.
                                """)
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Gustavo Henrique")
                                .email("gustavosilva.h37@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
