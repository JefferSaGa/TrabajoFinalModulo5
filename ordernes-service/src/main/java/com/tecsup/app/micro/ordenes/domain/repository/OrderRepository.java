package com.tecsup.app.micro.ordenes.domain.repository;

import com.tecsup.app.micro.ordenes.domain.model.Order;
import java.util.List;
import java.util.Optional;


public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderTrackingNumber(String trackingNumber);

    // Mostrarle al usuario su historial de delivery en la app
    List<Order> findByCustomerId(Long customerId);
}