package com.microservice.productservice.service;

import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.entity.Product;
import com.microservice.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {

//        Create new product
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

//        Save into repo
        Product savedProduct = productRepository.save(product);

//        Display response of new product created
        ProductResponse productResponse = ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .build();

        return productResponse;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList   = productRepository.findAll();
        return productList.stream().map(product -> {
            return mapAllProduct(product);
        }).toList();

//              return productList.stream().map(product -> mapAllProduct(product)).toList();
    }

    private ProductResponse mapAllProduct(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
