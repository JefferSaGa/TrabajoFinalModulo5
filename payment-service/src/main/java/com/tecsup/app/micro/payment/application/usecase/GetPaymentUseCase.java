package com.tecsup.app.micro.payment.application.usecase;

import com.tecsup.app.micro.payment.domain.model.Payment;
import java.util.Optional;

public interface GetPaymentUseCase {
    /**
     * Busca el estado de pago de una orden específica.
     */
    Optional<Payment> getPaymentByOrderId(Long orderId);
}