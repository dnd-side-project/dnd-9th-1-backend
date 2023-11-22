package com.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

         Server testServer = new Server();

        return new OpenAPI()
                .addServersItem(new Server().url("https://milestone-staging.site"))
                .addServersItem(new Server().url("https://dev.milestone-staging.stie"))
                .info(
                        new Info()
                                .title("DND-9th-1 'Milestone' API Document")
                                .description("DND 9기 1팀 'Milestone' 프로젝트의 API 명세서입니다.")
                                .version("v0.0.1")
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                 .addServersItem(testServer)
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "Authorization",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
