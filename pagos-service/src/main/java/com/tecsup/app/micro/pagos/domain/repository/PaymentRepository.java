package com.tecsup.app.micro.pagos.domain.repository;

import com.tecsup.app.micro.pagos.domain.model.Payment;

import java.util.Optional;


public interface PaymentRepository {

    Payment save(Payment payment);


    Optional<Payment> findById(Long id);

    Optional<Payment> findByOrderId(Long orderId);


    boolean existsSuccessfulPaymentForOrder(Long orderId);
}