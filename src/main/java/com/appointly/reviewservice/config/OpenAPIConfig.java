package com.appointly.reviewservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8088}")
    private String serverPort;

    @Bean
    public OpenAPI reviewServiceOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("Appointly Team")
                .email("support@appointly.com")
                .url("https://appointly.com");

        License license = new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Review & Rating Service API")
                .version("1.0.0")
                .contact(contact)
                .description("API for managing customer reviews and ratings for businesses and services")
                .termsOfService("https://appointly.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}