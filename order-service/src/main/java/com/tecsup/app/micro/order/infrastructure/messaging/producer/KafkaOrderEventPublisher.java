package com.tecsup.app.micro.order.infrastructure.messaging.producer;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.publisher.OrderEventPublisher;
import com.tecsup.app.micro.order.infrastructure.messaging.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    // Herramienta nativa de Spring Boot para enviar mensajes a Kafka
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // El nombre del tópico (buzón) donde los otros microservicios estarán escuchando
    private static final String ORDER_CREATED_TOPIC = "order-created-topic";

    @Override
    public void publishOrderCreatedEvent(Order order) {
        // 1. Mapear de Dominio al Evento que viajará por la red
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .trackingNumber(order.getOrderTrackingNumber())
                .customerId(order.getCustomerId())
                .totalAmount(order.getTotalAmount())
                .timestamp(LocalDateTime.now())
                .build();

        // 2. Enviar el mensaje a Kafka y evaluar su confirmación real
        try {
            var future = kafkaTemplate.send(ORDER_CREATED_TOPIC, event.getTrackingNumber(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("¡CONFIRMADO POR KAFKA! Evento publicado [Topic: {}] - Partition: {}, Offset: {}, Tracking: {}",
                            ORDER_CREATED_TOPIC,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            event.getTrackingNumber());
                } else {
                    log.error("❌ ERROR REAL DE KAFKA: El broker rechazó el mensaje para el tracking {}: {}",
                            event.getTrackingNumber(), ex.getMessage(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Fallo crítico al invocar KafkaTemplate para la orden {}: {}", event.getTrackingNumber(), e.getMessage());
        }
    }
    }