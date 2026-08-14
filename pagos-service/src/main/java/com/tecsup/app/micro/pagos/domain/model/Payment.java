package com.tecsup.app.micro.pagos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void approve(String gatewayTransactionId) {
        this.status = PaymentStatus.APPROVED;
        this.transactionId = gatewayTransactionId;
        this.updatedAt = LocalDateTime.now();
    }


    public void reject() {
        this.status = PaymentStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }


    public boolean isValidForProcessing() {
        return orderId != null && amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}