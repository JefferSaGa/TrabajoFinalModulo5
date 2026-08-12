package com.tecsup.app.micro.payment.domain.model;

/**
 * Estados posibles de un pago en nuestro sistema de delivery.
 */
public enum PaymentStatus {
    PENDING,    // El pago se está procesando
    APPROVED,   // El pago fue exitoso (Motorizado puede ir a recoger)
    REJECTED    // Tarjeta denegada, sin fondos, etc.
}