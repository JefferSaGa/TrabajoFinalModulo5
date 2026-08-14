package com.tecsup.app.micro.pedidos.application.usecase;

import com.tecsup.app.micro.pedidos.domain.model.Delivery;

public interface AssignDeliveryUseCase {
    Delivery assignDeliveryForOrder(Long orderId);
}