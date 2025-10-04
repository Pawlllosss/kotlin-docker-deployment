package pl.oczadly.kotlin_app.hackyeah2025;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = @Server(url = "/", description = "Default Server URL"))
@SpringBootApplication
public class Hackyeah2025Application {

    public static void main(String[] args) {
        SpringApplication.run(Hackyeah2025Application.class, args);
    }
}
