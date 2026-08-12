package com.tecsup.app.micro.order.application.service;

import com.tecsup.app.micro.order.application.usecase.CreateOrderUseCase;
import com.tecsup.app.micro.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

    private final CreateOrderUseCase createOrderUseCase;

    // Agregamos el parámetro jwtToken
    public Order createOrder(Order order, String jwtToken) {
        log.debug("OrderApplicationService: Delegando la creación del pedido al Use Case");
        return createOrderUseCase.execute(order, jwtToken); // Lo pasamos al Caso de Uso
    }
}