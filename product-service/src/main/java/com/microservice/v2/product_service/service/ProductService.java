package com.microservice.v2.product_service.service;

import com.microservice.v2.product_service.dto.ProductRequest;
import com.microservice.v2.product_service.dto.ProductResponse;
import com.microservice.v2.product_service.entity.Product;
import com.microservice.v2.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ProductResponse getProductById(String id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        return existingProduct.map(product -> {
            return mapAllProduct(product);
        }).orElse(null);
    }

    public ProductResponse updateProduct (String id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No product id: " + id));
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);
        return mapAllProduct(updatedProduct);

    }

    public void deleteProduct(String id){
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
             productRepository.delete(existingProductOptional.get());
        } else {
            throw new NoSuchElementException("No product with id: " + id);
        }
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

