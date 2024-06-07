package com.microservice.v2.product_service.controller;

import com.microservice.v2.product_service.dto.ProductRequest;
import com.microservice.v2.product_service.dto.ProductResponse;
import com.microservice.v2.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse =  productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct(){
        List<ProductResponse> productResponseList = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse productById = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productById);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct (@PathVariable String id, @RequestBody ProductRequest productRequest) {
        ProductResponse updateProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}