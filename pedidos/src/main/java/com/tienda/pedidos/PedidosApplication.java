package com.tienda.pedidos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestion de Pedidos",
                version = "1.0.0",
                description = "Laboratorio Unidad 11 - Programacion Web: Refactorizacion, Clean Architecture, Patrones de Diseno y QA.",
                contact = @Contact(
                        name = "Equipo de Desarrollo Web",
                        email = "dev@tienda.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor de Desarrollo Local")
        }
)
public class PedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidosApplication.class, args);
    }
}
