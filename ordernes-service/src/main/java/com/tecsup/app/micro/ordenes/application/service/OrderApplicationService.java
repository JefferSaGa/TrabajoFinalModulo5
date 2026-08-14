package com.tecsup.app.micro.ordenes.application.service;

import com.tecsup.app.micro.ordenes.application.usecase.CreateOrderUseCase;
import com.tecsup.app.micro.ordenes.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

    private final CreateOrderUseCase crearOrderUseCase;

    public Order createOrder(Order order, String jwtToken) {
        log.debug("OrderApplicationService: Delegando la creación del pedido al Use Case");
        return crearOrderUseCase.execute(order, jwtToken);
    }
}