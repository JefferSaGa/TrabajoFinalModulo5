package com.tecsup.app.micro.pedidos.infrastructure.messaging.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "delivery-assigned";

    public void sendDeliveryAssignedEvent(Delivery delivery) {
        try {
            DeliveryAssignedEventDTO event = DeliveryAssignedEventDTO.builder()
                    .deliveryId(delivery.getId())
                    .orderId(delivery.getOrderId())
                    .driverName(delivery.getDriverName())
                    .driverPhone(delivery.getDriverPhone())
                    .estimatedDeliveryTime(delivery.getEstimatedDeliveryTime())
                    .build();


            String jsonPayload = objectMapper.writeValueAsString(event);

            log.info("Kafka Producer - Enviando evento [delivery-assigned] para la orden ID: {}", delivery.getOrderId());

            // Enviamos el JSON como String
            kafkaTemplate.send(TOPIC, event.getOrderId().toString(), jsonPayload);

        } catch (Exception e) {
            log.error("Error al serializar / enviar el evento [delivery-assigned]: {}", e.getMessage(), e);
        }
    }
}