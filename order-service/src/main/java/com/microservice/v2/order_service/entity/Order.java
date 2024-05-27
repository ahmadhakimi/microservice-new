package com.microservice.v2.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderLineItems> orderLineItemsList;

    // Add convenience method to add order line items
    public void addOrderLineItem(OrderLineItems orderLineItem) {
        orderLineItemsList.add(orderLineItem);
        orderLineItem.setOrder(this);
    }
}
