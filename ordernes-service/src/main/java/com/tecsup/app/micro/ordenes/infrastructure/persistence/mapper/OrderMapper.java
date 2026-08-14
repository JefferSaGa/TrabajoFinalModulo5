package com.tecsup.app.micro.ordenes.infrastructure.persistence.mapper;

import com.tecsup.app.micro.ordenes.domain.model.Order;
import com.tecsup.app.micro.ordenes.domain.model.OrderItem;
import com.tecsup.app.micro.ordenes.infrastructure.persistence.entity.OrderEntity;
import com.tecsup.app.micro.ordenes.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {


    public Order toDomain(OrderEntity entity) {
        if (entity == null) return null;

        Order order = Order.builder()
                .id(entity.getId())
                .orderTrackingNumber(entity.getOrderTrackingNumber())
                .customerId(entity.getCustomerId())
                .restaurantId(entity.getRestaurantId())
                .deliveryAddress(entity.getDeliveryAddress())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        if (entity.getItems() != null) {
            order.setItems(entity.getItems().stream()
                    .map(this::toDomainItem)
                    .collect(Collectors.toList()));
        }

        return order;
    }


    public OrderEntity toEntity(Order domain) {
        if (domain == null) return null;

        OrderEntity entity = OrderEntity.builder()
                .id(domain.getId())
                .orderTrackingNumber(domain.getOrderTrackingNumber())
                .customerId(domain.getCustomerId())
                .restaurantId(domain.getRestaurantId())
                .deliveryAddress(domain.getDeliveryAddress())
                .status(domain.getStatus())
                .totalAmount(domain.getTotalAmount())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .items(new java.util.ArrayList<>())
                .build();

        if (domain.getItems() != null) {
            domain.getItems().forEach(item -> {
                entity.addItem(toEntityItem(item)); // Importante: amarramos la relación JPA aquí
            });
        }

        return entity;
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        if (entity == null) return null;
        return OrderItem.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .build();
    }

    private OrderItemEntity toEntityItem(OrderItem domain) {
        if (domain == null) return null;
        return OrderItemEntity.builder()
                .productId(domain.getProductId())
                .productName(domain.getProductName())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .subtotal(domain.getSubtotal())
                .build();
    }
}