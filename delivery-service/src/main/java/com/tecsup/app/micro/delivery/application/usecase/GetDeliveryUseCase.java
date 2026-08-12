package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import java.util.Optional;

public interface GetDeliveryUseCase {
    Optional<Delivery> getDeliveryByOrderId(Long orderId);
}