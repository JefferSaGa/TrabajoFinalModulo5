package com.tecsup.app.micro.delivery.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.delivery.application.service.DeliveryApplicationService;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.infrastructure.messaging.dto.PaymentApprovedEventDTO;
import com.tecsup.app.micro.delivery.infrastructure.messaging.producer.DeliveryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaListener {

    private final DeliveryApplicationService deliveryApplicationService;
    private final DeliveryEventProducer deliveryEventProducer;
    private final ObjectMapper objectMapper; //

    @KafkaListener(topics = "payment-approved", groupId = "delivery-service-group")
    public void handlePaymentApproved(String rawMessage) {
        log.info("🎯 ¡MENSAJE CRUDA [payment-approved] RECIBIDO EN DELIVERY!: {}", rawMessage);

        try {
            // Convertimos el String JSON plano a nuestro DTO local
            PaymentApprovedEventDTO event = objectMapper.readValue(rawMessage, PaymentApprovedEventDTO.class);

            log.info("Kafka Listener - Procesando orden ID: {}", event.getOrderId());

            // 1. Asignamos el motorizado
            Delivery delivery = deliveryApplicationService.assignDeliveryForOrder(event.getOrderId());

            // 2. Producimos el evento para que la app móvil o el order-service se enteren
            deliveryEventProducer.sendDeliveryAssignedEvent(delivery);

        } catch (Exception e) {
            log.error("Error procesando asignación de delivery para orden desde Kafka: {}", e.getMessage(), e);
        }
    }
}