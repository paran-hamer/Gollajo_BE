package com.gollajo.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    private final String devUrl;
    private final String prodUrl;

    public OpenAPIConfig(@Value("${gollajo.openapi.dev-url}") final String devUrl,
                         @Value("${gollajo.openapi.prod-url}")final String prodUrl){
        this.devUrl = devUrl;
        this.prodUrl = prodUrl;
    }
    @Bean
    public OpenAPI openAPI(){
        final Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.description("개발 환경 서버 URL");

        final Server prodServer = new Server();
        devServer.setUrl(prodUrl);
        devServer.description("개발 환경 서버 URL");

        final Info info = new Info()
                .title("Gollajo API")
                .version("v1.0.0")
                .description("골라줘 API입니다.");
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
