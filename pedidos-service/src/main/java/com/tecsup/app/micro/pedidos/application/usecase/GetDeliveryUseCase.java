package com.tecsup.app.micro.pedidos.application.usecase;

import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import java.util.Optional;

public interface GetDeliveryUseCase {
    Optional<Delivery> getDeliveryByOrderId(Long orderId);
}