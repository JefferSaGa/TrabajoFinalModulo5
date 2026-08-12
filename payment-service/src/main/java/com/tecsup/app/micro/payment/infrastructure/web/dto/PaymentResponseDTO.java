package com.tecsup.app.micro.payment.infrastructure.web.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String transactionId;
    private LocalDateTime processedAt;
}