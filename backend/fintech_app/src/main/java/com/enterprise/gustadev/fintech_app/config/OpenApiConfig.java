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
                                Permite controle de contas bancárias, transações, categorias, extratos, \
                                snapshots financeiros, notificações e consentimentos LGPD.
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