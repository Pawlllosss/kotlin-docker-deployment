package pl.oczadly.kotlin_app.hackyeah2025

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(servers = [Server(url = "/", description = "Default Server URL")])
@SpringBootApplication
class Hackyeah2025Application

fun main(args: Array<String>) {
	runApplication<Hackyeah2025Application>(*args)
}
