package com.tecsup.app.micro.order.domain.repository;

import com.tecsup.app.micro.order.domain.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de los pedidos.
 * El dominio define el contrato, la capa de infraestructura usará JPA para cumplirlo.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderTrackingNumber(String trackingNumber);

    // Mostrarle al usuario su historial de delivery en la app
    List<Order> findByCustomerId(Long customerId);
}