package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.domain.publisher.OrderEventPublisher; // 👈 Importamos el puerto
import com.tecsup.app.micro.order.domain.repository.OrderRepository;
import com.tecsup.app.micro.order.infrastructure.client.ProductClient;
import com.tecsup.app.micro.order.infrastructure.client.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderEventPublisher kafkaPublisher; // 👈 Inyectamos nuestro publicador de eventos

    public Order execute(Order order, String jwtToken) {
        log.info("Iniciando creación de pedido de delivery para el cliente: {}", order.getCustomerId());

        if (!order.isValid()) {
            throw new IllegalArgumentException("El pedido no contiene los datos mínimos requeridos.");
        }

        // 1. Validar productos de forma SÍNCRONA
        for (OrderItem item : order.getItems()) {
            ProductDTO productReal = productClient.getProductById(item.getProductId(), jwtToken);
            if (productReal == null) {
                throw new IllegalArgumentException("El producto con ID " + item.getProductId() + " no existe.");
            }
            if (productReal.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Stock insuficiente para: " + productReal.getName());
            }
            item.setProductName(productReal.getName());
            item.setUnitPrice(productReal.getPrice());
        }

        // 2. Lógica inteligente del Dominio
        order.calculateTotal();
        order.generateTrackingNumber();
        order.setStatus("PENDING");

        // 3. Persistir (Base de datos local del microservicio)
        Order savedOrder = orderRepository.save(order);
        log.info("Pedido guardado localmente con tracking: {}", savedOrder.getOrderTrackingNumber());

        // 4. [Fase Kafka] Disparamos el evento ASÍNCRONO
        kafkaPublisher.publishOrderCreatedEvent(savedOrder);

        return savedOrder;
    }
}