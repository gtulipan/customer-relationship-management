package hu._ig.crm.crm4ig.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("CRM 4iG API")
                        .description("API documentation for the crm-4ig application")
                        .version("1.0.0")
                        .termsOfService("http://example.com/terms")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@example.com")
                                .url("http://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("crm-4ig")
                .pathsToMatch("/**")
                .build();
    }
}
