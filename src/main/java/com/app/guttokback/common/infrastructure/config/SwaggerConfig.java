package com.app.guttokback.common.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();   // API 서버 설정
        server.setUrl("/");

        Server prodServer = new Server();  // 운영서버에 따로 띄우기 위해 서버를 추가할 수 있다.
        server.setUrl("http://localhost:8080");

        Info info = new Info()
                .title("구똑-구독을 똑똑하게 API")       // API 문서 제목
                .version("v1.0.0")          // API 문서 버전
                .description("구똑-구독을 똑똑하게 API"); // API 문서 설명

        return new OpenAPI()
                .info(info)
                .servers(List.of(server, prodServer));
    }

}
