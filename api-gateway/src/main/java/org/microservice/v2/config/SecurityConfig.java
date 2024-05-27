package org.microservice.v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity //the api gateway spring cloud is a part of the spring.web.flux project
// not spring web mvc security
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity
                .csrf().disable() //we only communication the api using the POSTMAN
                .authorizeExchange(exchange -> exchange //how web-flux should handle the security,
                        .pathMatchers("/eureka/**")  //setting up the static route so that every user must authenticate when call path /eureka/**
                        .permitAll()  //allow all user to go to this path
                        .anyExchange()     //any exchange
                        .authenticated())    //must have been authenticated
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt); //method references that mapping all the jwt token received during auth
        return serverHttpSecurity.build(); //start building the security
    }
}
