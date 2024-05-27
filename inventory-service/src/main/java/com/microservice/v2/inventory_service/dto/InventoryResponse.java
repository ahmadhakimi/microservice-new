package com.microservice.v2.inventory_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class InventoryResponse {

    private String skuCode;
    private boolean isInStock;
}
