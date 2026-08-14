package com.tecsup.app.micro.pagos.application.usecase;

import com.tecsup.app.micro.pagos.domain.model.Payment;
import java.math.BigDecimal;

public interface ProcessPaymentUseCase {
    /**
     * Procesa el cobro de una orden.
     */
    Payment processPayment(Long orderId, BigDecimal amount, String paymentMethod);
}