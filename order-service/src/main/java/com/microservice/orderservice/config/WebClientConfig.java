package com.microservice.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

//add Configuration annotation

@Configuration
public class WebClientConfig {
    @Bean // bean create webclient object and build from WebClient
    public WebClient webClient() {
        return  WebClient.builder().build();
    }
}
