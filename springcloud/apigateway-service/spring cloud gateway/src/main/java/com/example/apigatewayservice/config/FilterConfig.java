package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration // 스프링 컨테이너에 빈을 등록하기 위해 필요한 어노테이션
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/first-service/**") // first-service/**의 요청이 오면
                        .filters(f -> f.addRequestHeader("first-request","first-request-value")
                                .addResponseHeader("first-response","first-response-value")) // request,response헤더를 추가하고
                        .uri("http://localhost:8081")) // localhost:8081로 보내라
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request","second-request-value")
                                .addResponseHeader("second-response","second-response-value"))
                        .uri("http://localhost:8082"))

                .build();
    }
}
