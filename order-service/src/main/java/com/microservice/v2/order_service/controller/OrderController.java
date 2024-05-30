package com.microservice.v2.order_service.controller;

import com.microservice.v2.order_service.dto.OrderRequest;
import com.microservice.v2.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    // Fallback method
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, Throwable throwable) {
        return CompletableFuture.supplyAsync(() -> "Somethings went wrong!!, please wait for some time.");
    }
}