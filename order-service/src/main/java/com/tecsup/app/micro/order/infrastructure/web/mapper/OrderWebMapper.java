package com.tecsup.app.micro.order.infrastructure.web.mapper;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.infrastructure.web.dto.OrderRequestDTO;
import com.tecsup.app.micro.order.infrastructure.web.dto.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderWebMapper {

    // Request DTO -> Modelo de Dominio
    public Order toDomain(OrderRequestDTO dto) {
        if (dto == null) return null;

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setDeliveryAddress(dto.getDeliveryAddress());

        if (dto.getItems() != null) {
            order.setItems(dto.getItems().stream().map(itemDto -> {
                OrderItem item = new OrderItem();
                item.setProductId(itemDto.getProductId());
                item.setQuantity(itemDto.getQuantity());
                // Inicializamos en cero para pasar la validación inicial (se actualizarán en el UseCase)
                item.setUnitPrice(BigDecimal.ZERO);
                item.setSubtotal(BigDecimal.ZERO);
                return item;
            }).collect(Collectors.toList()));
        }

        return order;
    }

    // Modelo de Dominio -> Response DTO
    public OrderResponseDTO toResponseDTO(Order order) {
        if (order == null) return null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .deliveryAddress(order.getDeliveryAddress())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(item ->
                        OrderResponseDTO.OrderItemResponseDTO.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}