package com.tecsup.app.micro.payment.domain.repository;

import com.tecsup.app.micro.payment.domain.model.Payment;

import java.util.Optional;

/**
 * Puerto del Repositorio de Pagos (Interface)
 * Pertenece al dominio y será implementado por la infraestructura.
 */
public interface PaymentRepository {

    /**
     * Guarda un nuevo pago o actualiza uno existente
     */
    Payment save(Payment payment);

    /**
     * Busca un pago por su ID interno
     */
    Optional<Payment> findById(Long id);

    /**
     * Busca el pago asociado a una Orden específica
     */
    Optional<Payment> findByOrderId(Long orderId);

    /**
     * Verifica si ya existe un pago exitoso para una orden
     */
    boolean existsSuccessfulPaymentForOrder(Long orderId);
}