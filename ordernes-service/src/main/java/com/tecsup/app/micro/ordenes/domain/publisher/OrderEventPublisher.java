package com.tecsup.app.micro.ordenes.domain.publisher;

import com.tecsup.app.micro.ordenes.domain.model.Order;


public interface OrderEventPublisher {

    void publishOrderCreatedEvent(Order order);

}