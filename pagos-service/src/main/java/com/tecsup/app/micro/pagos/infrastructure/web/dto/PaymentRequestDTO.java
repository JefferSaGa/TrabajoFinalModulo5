package com.tecsup.app.micro.pagos.infrastructure.web.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount;
    private String method;
}