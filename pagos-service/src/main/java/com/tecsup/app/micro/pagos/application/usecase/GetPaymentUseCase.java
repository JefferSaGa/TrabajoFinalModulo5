package com.tecsup.app.micro.pagos.application.usecase;

import com.tecsup.app.micro.pagos.domain.model.Payment;
import java.util.Optional;

public interface GetPaymentUseCase {
    /**
     * Busca el estado de pago de una orden específica.
     */
    Optional<Payment> getPaymentByOrderId(Long orderId);
}