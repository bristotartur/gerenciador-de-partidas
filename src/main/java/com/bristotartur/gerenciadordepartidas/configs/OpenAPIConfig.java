package com.bristotartur.gerenciadordepartidas.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Gerenciador de Gincans API",
                description = "API para o gerenciamento de gincanas do colégio CEDUP Abílio Paulo.",
                version = "0.1.0-ALPHA",
                contact = @Contact(
                        name = "Artur Bristot",
                        email = "arturdarosabristot@gmail.com"
                )
        ),
        servers = @Server(description = "Ambiente de desenvolvimento", url = "http://localhost:8080")
)
public class OpenAPIConfig {

}
