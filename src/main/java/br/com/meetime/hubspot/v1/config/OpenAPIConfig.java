package br.com.meetime.hubspot.v1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HubSpot API - Meetime")
                        .version("v1")
                        .description("API for HubSpot integration Meetime")
                        .contact(new Contact()
                                .url("http://www.linkedin.com/in/matheus-de-paulo/oliveira")
                                .email("matheusoliveira1991@hotmail.com")
                                .name("Matheus Oliveira"))
                );
    }
}
