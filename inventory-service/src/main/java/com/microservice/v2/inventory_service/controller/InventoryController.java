package com.microservice.v2.inventory_service.controller;

import com.microservice.v2.inventory_service.dto.InventoryResponse;
import com.microservice.v2.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode)   {
        return inventoryService.isInStock(skuCode);
    }

}