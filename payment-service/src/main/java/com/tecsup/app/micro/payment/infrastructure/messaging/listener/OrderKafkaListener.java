package com.tecsup.app.micro.payment.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.infrastructure.messaging.dto.OrderCreatedEventDTO; // Tu DTO local
import com.tecsup.app.micro.payment.infrastructure.messaging.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaListener {

    private final PaymentApplicationService paymentApplicationService;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper objectMapper; // Inyectamos Jackson para mapear el JSON

    @KafkaListener(topics = "order-created-topic", groupId = "payment-service-group-v2")
    public void handleOrderCreated(String rawMessage) {
        log.info("🎯 ¡MENSAJE RECIBIDO EN PAYMENT DESDE KAFKA!: {}", rawMessage);

        try {
            // Mapeamos el JSON crudo a nuestro DTO local del Payment Service
            OrderCreatedEventDTO event = objectMapper.readValue(rawMessage, OrderCreatedEventDTO.class);

            log.info("Procesando pago para la orden ID: {} con monto: {}", event.getOrderId(), event.getTotalAmount());

            // 1. Procesar el pago automáticamente
            Payment payment = paymentApplicationService.processPayment(
                    event.getOrderId(),
                    event.getTotalAmount(),
                    "CREDIT_CARD"
            );

            // 2. Si el pago fue aprobado, emitimos el evento para el Delivery
            if (payment.getStatus() == PaymentStatus.APPROVED) {
                log.info("Pago exitoso para la orden {}. Emitiendo evento [payment-approved]...", event.getOrderId());
                paymentEventProducer.sendPaymentApprovedEvent(payment);
            } else {
                log.warn("El pago para la orden {} fue rechazado.", event.getOrderId());
            }

        } catch (Exception e) {
            log.error("Error deserializando o procesando el evento de Kafka: {}", e.getMessage(), e);
        }
    }
}