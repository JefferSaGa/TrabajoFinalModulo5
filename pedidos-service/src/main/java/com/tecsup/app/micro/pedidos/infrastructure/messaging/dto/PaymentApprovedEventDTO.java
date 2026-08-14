package com.tecsup.app.micro.pedidos.infrastructure.messaging.dto;

import lombok.Data;

@Data
public class PaymentApprovedEventDTO {
    private Long paymentId;
    private Long orderId;
}