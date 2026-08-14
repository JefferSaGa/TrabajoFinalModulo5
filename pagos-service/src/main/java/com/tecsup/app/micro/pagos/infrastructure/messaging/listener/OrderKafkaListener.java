package com.tecsup.app.micro.pagos.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.pagos.application.service.PaymentApplicationService;
import com.tecsup.app.micro.pagos.domain.model.Payment;
import com.tecsup.app.micro.pagos.domain.model.PaymentStatus;
import com.tecsup.app.micro.pagos.infrastructure.messaging.dto.OrderCreatedEventDTO; // Tu DTO local
import com.tecsup.app.micro.pagos.infrastructure.messaging.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaListener {

    private final PaymentApplicationService pagosApplicationService;
    private final PaymentEventProducer pagosEventProducer;
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "order-created-topic", groupId = "payment-service-group-v2")
    public void handleOrderCreated(String rawMessage) {
        log.info( "MENSAJE RECIBIDO EN PAGO DESDE KAFKA!: {}", rawMessage);

        try {

            OrderCreatedEventDTO event = objectMapper.readValue(rawMessage, OrderCreatedEventDTO.class);

            log.info("Procesando pago para la orden ID: {} con monto: {}", event.getOrderId(), event.getTotalAmount());


            Payment payment = pagosApplicationService.processPayment(
                    event.getOrderId(),
                    event.getTotalAmount(),
                    "CREDIT_CARD"
            );


            if (payment.getStatus() == PaymentStatus.APPROVED) {
                log.info("Pago exitoso para la orden {}. Emitiendo evento [payment-approved]...", event.getOrderId());
                pagosEventProducer.sendPaymentApprovedEvent(payment);
            } else {
                log.warn("El pago para la orden {} fue rechazado.", event.getOrderId());
            }

        } catch (Exception e) {
            log.error("Error deserializando o procesando el evento de Kafka: {}", e.getMessage(), e);
        }
    }
}