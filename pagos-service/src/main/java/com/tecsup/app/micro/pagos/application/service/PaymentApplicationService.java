package com.tecsup.app.micro.pagos.application.service;

import com.tecsup.app.micro.pagos.application.usecase.GetPaymentUseCase;
import com.tecsup.app.micro.pagos.application.usecase.ProcessPaymentUseCase;
import com.tecsup.app.micro.pagos.domain.model.Payment;
import com.tecsup.app.micro.pagos.domain.model.PaymentStatus;
import com.tecsup.app.micro.pagos.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApplicationService implements ProcessPaymentUseCase, GetPaymentUseCase {

    private final PaymentRepository pagoRepository;

    @Override
    public Payment processPayment(Long orderId, BigDecimal amount, String paymentMethod) {
        log.info("Iniciando proceso de pago para la orden: {}", orderId);


        if (pagoRepository.existsSuccessfulPaymentForOrder(orderId)) {
            log.warn("La orden {} ya tiene un pago aprobado previamente. Ignorando doble cobro.", orderId);
            throw new IllegalStateException("La orden ya tiene un pago exitoso.");
        }


        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentMethod(paymentMethod != null ? paymentMethod : "CREDIT_CARD")
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (!payment.isValidForProcessing()) {
            throw new IllegalArgumentException("Datos de pago inválidos (Monto u Orden nulos).");
        }


        boolean isGatewaySuccess = simulateGatewayCall(payment);


        if (isGatewaySuccess) {
            String mockTransactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            payment.approve(mockTransactionId);
            log.info("Pago APROBADO para la orden {}. Transacción: {}", orderId, mockTransactionId);
        } else {
            payment.reject();
            log.error("Pago RECHAZADO para la orden {}", orderId);
        }


        return pagoRepository.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return pagoRepository.findByOrderId(orderId);
    }


    private boolean simulateGatewayCall(Payment payment) {
        log.info("Contactando pasarela de pagos por {} soles...", payment.getAmount());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return Math.random() > 0.1;
    }
}