package com.hakimi.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer //put the annotation to enable eureka server discovery into the discovery-server module
public class DiscoverServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoverServerApplication.class, args);
    }
}
