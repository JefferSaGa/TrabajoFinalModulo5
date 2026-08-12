package com.tecsup.app.micro.payment.infrastructure.messaging.producer;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentApprovedEventDTO {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String transactionId;
    private LocalDateTime approvedAt;
}