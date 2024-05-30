package com.microservice.v2.inventory_service.service;

import com.microservice.v2.inventory_service.dto.InventoryResponse;
import com.microservice.v2.inventory_service.entity.Inventory;
import com.microservice.v2.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCodes) {

        log.info("Wait started"); //send to log "wait started"
        Thread.sleep(5000); // thread sleep 10 seconds
        log.info("Wait ended"); //send to log "wait ended"
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        Map<String, Boolean> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getSkuCode, inventory -> inventory.getQuantity() > 0));

        return skuCodes.stream()
                .map(skuCode -> new InventoryResponse(skuCode, inventoryMap.getOrDefault(skuCode, false)))
                .collect(Collectors.toList());
    }
}


