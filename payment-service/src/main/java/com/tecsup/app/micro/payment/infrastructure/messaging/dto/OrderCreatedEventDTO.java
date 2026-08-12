package com.tecsup.app.micro.payment.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEventDTO {
    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;
    private String status;
}