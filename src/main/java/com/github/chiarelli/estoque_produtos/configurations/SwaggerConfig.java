package com.github.chiarelli.estoque_produtos.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
  @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("🛒 Estoque de Produtos API")
                .description("API REST para cadastro e gerenciamento de produtos de supermercado.")
                .version("v1.0")
                .contact(new Contact()
                    .name("Raphael Chiarelli")
                    .url("https://github.com/chiarelli")
                    .email("chiarelli.rm@gmail.com")
                )
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
                )
            );
    }
}
