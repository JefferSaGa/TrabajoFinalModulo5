package com.tecsup.app.micro.delivery.infrastructure.messaging.dto;

import lombok.Data;

@Data
public class PaymentApprovedEventDTO {
    private Long paymentId;
    private Long orderId;
    // Solo necesitamos el orderId para asignar el delivery
}