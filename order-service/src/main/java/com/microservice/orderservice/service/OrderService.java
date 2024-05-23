package com.microservice.orderservice.service;

import com.microservice.orderservice.dto.InventoryResponse;
import com.microservice.orderservice.dto.OrderLineItemsDTO;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.entity.OrderLineItems;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder; //inject web client builder

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

//        Get the list of skuCodes

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)  //replace lambda with method reference .map(orderLineItems -> orderLineItems.getSkuCode)
                .toList();

        try {
            // Call inventory service and place order if the product is  in stock
            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build()
                    .get() //GET method from Inventory Controller
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder
                            .queryParam("skuCode", skuCodes)  //inventory end-point has @queryParams("skuCode")
                            .build())
                    .retrieve()  //Executes the request and retrieves the response.
                    .bodyToMono(InventoryResponse[].class)  //Converts the response body to a Mono of InventoryResponse[].
                    .block(); //Blocks the call until the response is received, making this a synchronous operation.
            // client ready to start making synchronous request to http://inventory-service/api/inventory

            // Log the response from the inventory service
            if (inventoryResponsesArray != null) {
                logger.info("Inventory response: {}", Arrays.toString(inventoryResponsesArray));
            } else {
                logger.error("Inventory service returned null response");
                throw new RuntimeException("Inventory service returned null response");
            }

//            Create  stream from array inventoryResponsesArray object from InventoryResponse DTO (class)
            boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::isInStock); //replace lambda with reference method (inventoryResponse -> inventoryResponse.isInStock())

            if (allProductsInStock) {
//                Call inventory service and place order if stock available
                orderRepository.save(order);
                logger.info("Order placed successfully with order number: {}", order.getOrderNumber());
            } else {
                logger.error("Product is not in stock for skuCodes: {}", skuCodes);
                throw new IllegalArgumentException("Product is not in stock");
            }
        } catch (Exception e) {
            logger.error("Failed to place order due to: {}", e.getMessage());
            throw new RuntimeException("Failed to place order due to an unexpected error", e);
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
