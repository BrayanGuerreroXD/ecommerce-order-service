package com.test.ecommerceorderservice.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.url("http://localhost:7500/api/v1");
        localServer.setDescription("Ecommerce Server Url in local environment");

        Contact contact = new Contact();
        contact.setEmail("brayanstewartguor@gmail.com");
        contact.setName("Brayan Guerrero");

        Info info = new Info();
        info.setTitle("API Ecommerce");
        info.version("1.0");
        info.contact(contact);
        info.description(DESCRIPTION_ERRORS_PROJECT);
        return new OpenAPI().info(info).servers(List.of(localServer))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    public static final String DESCRIPTION_ERRORS_PROJECT =
            """
                    <div style="border: 1px solid #ccc; padding: 10px; background-color: #f9f9f9;">
                    <h2 style="text-align: center;">Error Codes ↓</h2>
                    <details>
                    <summary style="font-size: larger; font-weight: bold;">Error Code Details</summary>
                                       
                    <h2 style="text-align: center;">Queries and Data Request</h2>
                       
                    <ul>
                      <li><strong>C01</strong> - Request exception</li>
                      <li><strong>C02</strong> - Query Exception</li>
                      <li><strong>C03</strong> - Validation Exception</li>
                      <li><strong>N01</strong> - NotFound Exception:</li>
                      <li><strong>S01</strong> - Security Exception</li>
                      <li><strong>I01</strong> - Internal Server Exception</li>
                    </ul>

                    <h3 style="text-align: center;">JWT Exceptions</h3>
                    <ul>
                      <li><strong>J01</strong> - Request exception</li>
                      <li><strong>J02</strong> - Invalid token</li>
                    </ul>
                       
                    <h3 style="text-align: center;">Generic Exceptions</h3>
                    <ul>
                      <li><strong>N01GNR01</strong> - Id was not found</li>
                      <li><strong>N01GNR02</strong> - Name was not found</li>
                      <li><strong>N01GNR03</strong> - Code was not found (batchCode o sku code)</li>
                      <li><strong>N01GNR04</strong> - Email was not found</li>
                      <li><strong>N01GNRC01</strong> - Object was not found</li>
                      <li><strong>I01GNRC02</strong> - An error occurred while communicating with the external service</li>
                      <li><strong>C01GNRC01</strong> - Object already exist</li>
                    </ul>

                    <h3 style="text-align: center;">Forbidden Exception Generic</h3>
                    <ul>
                      <li><strong>S01FORB01</strong> - The user does not have permissions to access this resource</li>
                      <li><strong>S01FORB02</strong> - The user does not contain any module permissions</li>
                      <li><strong>S01FORB03</strong> - The user does not contain the module permissions</li>
                    </ul>

                    <h3 style="text-align: center;">Unauthorized Exception Generic</h3>
                    <ul>
                      <li><strong>S01UNAU01</strong> - The user is invalid</li>
                      <li><strong>S01UNAU02</strong> - No session active, log in again</li>
                      <li><strong>S01UNAU03</strong> - The token is not the token of the active session</li>
                      <li><strong>S01UNAU04</strong> - The token is null</li>
                      <li><strong>S01UNAU05</strong> - The token is empty</li>
                      <li><strong>S01UNAU06</strong> - The token is not valid</li>
                    </ul>
                    
                    
                    </details>
                    </div>
                    """;

}
