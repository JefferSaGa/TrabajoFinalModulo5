package com.tecsup.app.micro.ordenes.infrastructure.messaging.producer;

import com.tecsup.app.micro.ordenes.domain.model.Order;
import com.tecsup.app.micro.ordenes.domain.publisher.OrderEventPublisher;
import com.tecsup.app.micro.ordenes.infrastructure.messaging.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderEventPublisher implements OrderEventPublisher {


    private final KafkaTemplate<String, Object> kafkaTemplate;


    private static final String ORDER_CREATED_TOPIC = "order-created-topic";

    @Override
    public void publishOrderCreatedEvent(Order order) {

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .trackingNumber(order.getOrderTrackingNumber())
                .customerId(order.getCustomerId())
                .totalAmount(order.getTotalAmount())
                .timestamp(LocalDateTime.now())
                .build();


        try {
            var future = kafkaTemplate.send(ORDER_CREATED_TOPIC, event.getTrackingNumber(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("¡CONFIRMADO! Evento publicado [Topic: {}]: {}, Offset: {}, Tracking: {}",
                            ORDER_CREATED_TOPIC,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            event.getTrackingNumber());
                } else {
                    log.error("ERROR: El broker rechazó el mensaje {}: {}",
                            event.getTrackingNumber(), ex.getMessage(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Fallo al invocar KafkaTemplate {}: {}", event.getTrackingNumber(), e.getMessage());
        }
    }
    }