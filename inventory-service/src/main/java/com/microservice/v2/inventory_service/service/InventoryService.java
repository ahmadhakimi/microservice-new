package com.microservice.v2.inventory_service.service;

import com.microservice.v2.inventory_service.dto.InventoryResponse;
import com.microservice.v2.inventory_service.entity.Inventory;
import com.microservice.v2.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        Map<String, Boolean> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getSkuCode, inventory -> inventory.getQuantity() > 0));

        return skuCodes.stream()
                .map(skuCode -> new InventoryResponse(skuCode, inventoryMap.getOrDefault(skuCode, false)))
                .collect(Collectors.toList());
    }
}



//public List<InventoryResponse> isInStock(List<String> skuCode) {
//    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
//            .map(inventory ->
//                    InventoryResponse.builder()
//                            .skuCode(inventory.getSkuCode())
//                            .isInStock(inventory.getQuantity() > 0)
//                            .build()
//            ).toList();
//}