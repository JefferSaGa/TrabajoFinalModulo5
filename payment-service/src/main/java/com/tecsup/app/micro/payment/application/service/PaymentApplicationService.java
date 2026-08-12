package com.tecsup.app.micro.payment.application.service;

import com.tecsup.app.micro.payment.application.usecase.GetPaymentUseCase;
import com.tecsup.app.micro.payment.application.usecase.ProcessPaymentUseCase;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.domain.repository.PaymentRepository;
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

    private final PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Long orderId, BigDecimal amount, String paymentMethod) {
        log.info("Iniciando proceso de pago para la orden: {}", orderId);

        // 1. VALIDACIÓN DE IDEMPOTENCIA --> Ya le cobramos esta orden antes?
        if (paymentRepository.existsSuccessfulPaymentForOrder(orderId)) {
            log.warn("La orden {} ya tiene un pago aprobado previamente. Ignorando doble cobro.", orderId);
            throw new IllegalStateException("La orden ya tiene un pago exitoso.");
        }

        // 2. Crear la entidad de dominio en estado PENDING
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

        // 3. Simular llamada a la pasarela de pagos (Stripe, Niubiz, MercadoPago)
        boolean isGatewaySuccess = simulateGatewayCall(payment);

        // 4. Aplicar lógica de negocio del dominio según la respuesta
        if (isGatewaySuccess) {
            String mockTransactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            payment.approve(mockTransactionId);
            log.info("Pago APROBADO para la orden {}. Transacción: {}", orderId, mockTransactionId);
        } else {
            payment.reject();
            log.error("Pago RECHAZADO para la orden {}", orderId);
        }

        // 5. Guardar en BD (vía el puerto del repositorio)
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    /**
     * Metodo simulado. En la vida real, podriamos llamar a un cliente HTTP/REST
     * hacia la API de Visa, Mastercard, etc.
     */
    private boolean simulateGatewayCall(Payment payment) {
        log.info("Contactando pasarela de pagos por {} soles...", payment.getAmount());
        try {
            Thread.sleep(1000); // Simulamos la latencia de red
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Para pruebas locales, vamos a decir que el 90% de los pagos pasan.
        return Math.random() > 0.1;
    }
}