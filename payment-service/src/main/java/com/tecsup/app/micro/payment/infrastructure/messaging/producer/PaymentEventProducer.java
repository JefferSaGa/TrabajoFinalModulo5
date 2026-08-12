package com.tecsup.app.micro.payment.infrastructure.messaging.producer;

import com.tecsup.app.micro.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "payment-approved";

    public void sendPaymentApprovedEvent(Payment payment) {
        PaymentApprovedEventDTO event = PaymentApprovedEventDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .approvedAt(payment.getUpdatedAt())
                .build();

        log.info("Kafka Producer - Enviando evento [payment-approved] para la orden ID: {}", payment.getOrderId());
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }
}