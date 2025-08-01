package com.factory.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Factory Management API")
                        .description("API documentation for the Factory Batch, Product, and Downtime tracking system.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Mudit Gupta")
                                .email("mudit.gupta@vonage.com")
                        )
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/mudit201/factory_management_app/tree/main/backend"));
    }
}
