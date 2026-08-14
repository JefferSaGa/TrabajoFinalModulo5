package com.tecsup.app.micro.pedidos.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.pedidos.application.service.DeliveryApplicationService;
import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import com.tecsup.app.micro.pedidos.infrastructure.messaging.dto.PaymentApprovedEventDTO;
import com.tecsup.app.micro.pedidos.infrastructure.messaging.producer.DeliveryEventProducer;
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
        log.info("MENSAJE [payment-approved] RECIBIDO EN DELIVERY!: {}", rawMessage);

        try {

            PaymentApprovedEventDTO event = objectMapper.readValue(rawMessage, PaymentApprovedEventDTO.class);

            log.info("Kafka Listener - Procesando orden ID: {}", event.getOrderId());


            Delivery delivery = deliveryApplicationService.assignDeliveryForOrder(event.getOrderId());


            deliveryEventProducer.sendDeliveryAssignedEvent(delivery);

        } catch (Exception e) {
            log.error("Error procesando asignación de delivery para orden: {}", e.getMessage(), e);
        }
    }
}