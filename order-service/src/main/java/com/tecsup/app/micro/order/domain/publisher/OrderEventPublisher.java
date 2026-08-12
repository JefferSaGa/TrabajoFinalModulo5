package com.tecsup.app.micro.order.domain.publisher;

import com.tecsup.app.micro.order.domain.model.Order;

/**
 * Puerto de salida para eventos de dominio.
 * Aisla al núcleo de negocio de la tecnología de mensajería (Kafka).
 */
public interface OrderEventPublisher {

    void publishOrderCreatedEvent(Order order);

}