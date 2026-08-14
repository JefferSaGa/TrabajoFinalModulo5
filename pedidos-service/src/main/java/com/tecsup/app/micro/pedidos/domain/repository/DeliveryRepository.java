package com.tecsup.app.micro.pedidos.domain.repository;

import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import java.util.Optional;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    Optional<Delivery> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}