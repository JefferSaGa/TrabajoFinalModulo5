package com.tecsup.app.micro.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Order Domain Model (Core Business Entity)
 * Entidad pura que orquesta un pedido de delivery.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    private String orderTrackingNumber;
    private Long customerId;
    private Long restaurantId;
    private String deliveryAddress;

    @Builder.Default
    private String status = "PENDING"; // Estados: PENDING, PAID, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED

    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Reglas de negocio: Valida que la orden esté lista para procesarse
     */
    public boolean isValid() {
        return customerId != null
                && restaurantId != null
                && deliveryAddress != null && !deliveryAddress.trim().isEmpty()
                && items != null && !items.isEmpty()
                && items.stream().allMatch(OrderItem::isValid);
    }

    /**
     * Calcula el monto total a cobrar al cliente sumando los items
     */
    public void calculateTotal() {
        if (this.items != null && !this.items.isEmpty()) {
            this.totalAmount = this.items.stream()
                    .peek(OrderItem::calculateSubtotal)
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
    }

    /**
     * Genera un número de seguimiento único visual para el cliente
     */
    public void generateTrackingNumber() {
        this.orderTrackingNumber = "DELIVERY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Cambia el estado del pedido solo si cumple la lógica de negocio
     */
    public void markAsPaid() {
        if (!"PENDING".equals(this.status)) {
            throw new IllegalStateException("Solo una orden en estado PENDING puede ser pagada");
        }
        this.status = "PAID";
    }
}