package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.domain.model.Delivery;

public interface AssignDeliveryUseCase {
    Delivery assignDeliveryForOrder(Long orderId);
}