package com.microservice.v2.order_service.service;

import com.microservice.v2.order_service.dto.InventoryResponse;
import com.microservice.v2.order_service.dto.OrderLineItemsDTO;
import com.microservice.v2.order_service.dto.OrderRequest;
import com.microservice.v2.order_service.entity.Order;
import com.microservice.v2.order_service.entity.OrderLineItems;
import com.microservice.v2.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;


    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        logger.info("Requesting inventory status for SKUs: {}", skuCodes);

        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build()
                .get() //GET method from Inventory Controller
                .uri("http://inventory-service/api/inventory", //uri using the instance of inventory-service, name of the localhost in application.properties
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
                                .build())
                .retrieve()  //Executes the request and retrieves the response.
                .bodyToMono(InventoryResponse[].class)  //Converts the response body to a Mono of InventoryResponse[].
                .block();//Blocks the call until the response is received, making this a synchronous operation.
// client ready to start making synchronous request to http://inventory-service/api/inventory

        if (inventoryResponsesArray == null) {
            throw new IllegalArgumentException("Inventory service did not respond.");
        }

        logger.info("Received inventory response: {}", Arrays.toString(inventoryResponsesArray));

        logger.info("Received inventory response: {}", Arrays.toString(inventoryResponsesArray));
        for (InventoryResponse response : inventoryResponsesArray) {
            logger.info("SKU: {}, In Stock: {}", response.getSkuCode(), response.isInStock());
        }


        //create stream of array from java8
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);


        if(allProductsInStock){
            orderRepository.save(order);
            logger.info("Order placed successfully with order number: {}", order.getOrderNumber());

        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
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
