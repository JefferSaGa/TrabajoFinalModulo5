package com.tecsup.app.micro.pagos.infrastructure.persistence.mapper;

import com.tecsup.app.micro.pagos.domain.model.Payment;
import com.tecsup.app.micro.pagos.domain.model.PaymentStatus;
import com.tecsup.app.micro.pagos.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment domain) {
        if (domain == null) return null;
        return PaymentEntity.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .amount(domain.getAmount())
                .paymentMethod(domain.getPaymentMethod())
                .transactionId(domain.getTransactionId())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;
        return Payment.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod())
                .transactionId(entity.getTransactionId())
                .status(entity.getStatus() != null ? PaymentStatus.valueOf(entity.getStatus()) : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}