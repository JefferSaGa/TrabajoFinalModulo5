package com.tecsup.app.micro.ordenes.application.usecase;

import com.tecsup.app.micro.ordenes.domain.model.Order;
import com.tecsup.app.micro.ordenes.domain.model.OrderItem;
import com.tecsup.app.micro.ordenes.domain.publisher.OrderEventPublisher; // 👈 Importamos el puerto
import com.tecsup.app.micro.ordenes.domain.repository.OrderRepository;
import com.tecsup.app.micro.ordenes.infrastructure.client.ProductClient;
import com.tecsup.app.micro.ordenes.infrastructure.client.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductClient productoClient;
    private final OrderEventPublisher kafkaPublisher;

    public Order execute(Order order, String jwtToken) {
        log.info("Creación de pedido de delivery para el cliente: {}", order.getCustomerId());

        if (!order.isValid()) {
            throw new IllegalArgumentException("El pedido no contiene los datos requeridos.");
        }


        for (OrderItem item : order.getItems()) {
            ProductDTO productReal = productoClient.getProductById(item.getProductId(), jwtToken);
            if (productReal == null) {
                throw new IllegalArgumentException("El producto con ID " + item.getProductId() + " no existe.");
            }
            if (productReal.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Stock insuficiente para: " + productReal.getName());
            }
            item.setProductName(productReal.getName());
            item.setUnitPrice(productReal.getPrice());
        }

        // Logica del Dominio
        order.calculateTotal();
        order.generateTrackingNumber();
        order.setStatus("PENDING");

        // Base de datos local del microservicio
        Order savedOrder = orderRepository.save(order);
        log.info("Pedido guardado localmente con tracking: {}", savedOrder.getOrderTrackingNumber());

        //Disparamos el evento ASÍNCRONO
        kafkaPublisher.publishOrderCreatedEvent(savedOrder);

        return savedOrder;
    }
}