package com.tecsup.app.micro.payment.infrastructure.web.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount;
    private String method; // Opcional, por defecto CREDIT_CARD
}