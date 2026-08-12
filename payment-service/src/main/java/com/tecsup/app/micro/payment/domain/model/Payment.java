package com.tecsup.app.micro.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment Domain Model (Core Business Entity)
 * Representa la transacción de pago de una orden.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;
    private Long orderId;          // Vinculamos el pago a la orden creada
    private BigDecimal amount;     // Monto a cobrar
    private String paymentMethod;  // Ej: CREDIT_CARD, YAPE, PLIN
    private String transactionId;  // ID de la pasarela de pagos (Stripe, Niubiz, etc.)
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Lógica de Negocio: Aprobar el pago
     */
    public void approve(String gatewayTransactionId) {
        this.status = PaymentStatus.APPROVED;
        this.transactionId = gatewayTransactionId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lógica de Negocio: Rechazar el pago
     */
    public void reject() {
        this.status = PaymentStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Validación inicial al crear un pago
     */
    public boolean isValidForProcessing() {
        return orderId != null && amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}