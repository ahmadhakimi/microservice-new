package com.microservice.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

//add Configuration annotation

@Configuration

public class WebClientConfig {
//    @Bean // bean create webclient object and build from WebClient
//    public WebClient webClient() {
//        return  WebClient.builder().build();
//    }

//    We want load balancing to ensure the distribute request evenly across multiple instances
    @Bean
    @LoadBalanced // add load balancing client-side bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder(); //automatic create WebClient instance load-balancer
        //and calling the inventory services

// order-service able to request to the inventory-service even the inventory-services have multiple instances (port created) 
    }
}
