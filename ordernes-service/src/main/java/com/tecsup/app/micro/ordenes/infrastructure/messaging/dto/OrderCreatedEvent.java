package com.tecsup.app.micro.ordenes.infrastructure.messaging.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que representa el mensaje JSON que viajará por Kafka.
 */
@Data
@Builder
public class OrderCreatedEvent {
    private Long orderId;
    private String trackingNumber;
    private Long customerId;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;
}