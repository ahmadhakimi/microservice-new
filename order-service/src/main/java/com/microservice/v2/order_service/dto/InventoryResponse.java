package com.microservice.v2.order_service.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class InventoryResponse {

    private String skuCode;
    private boolean isInStock;
}
