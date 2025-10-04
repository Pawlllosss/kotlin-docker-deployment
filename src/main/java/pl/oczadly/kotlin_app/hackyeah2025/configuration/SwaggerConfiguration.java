package pl.oczadly.kotlin_app.hackyeah2025.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(
            new Info()
                .title("Test API of Kotlin API")
                .description("Documentation for Test Kotlin API")
                .version("v1")
        );
    }
}
