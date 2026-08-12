package com.tecsup.app.micro.payment.infrastructure.web.mapper;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.infrastructure.web.dto.PaymentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {

    public PaymentResponseDTO toDto(Payment domain) {
        if (domain == null) return null;
        return PaymentResponseDTO.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .amount(domain.getAmount())
                .status(domain.getStatus() != null ? domain.getStatus().name() : "UNKNOWN")
                .transactionId(domain.getTransactionId())
                .processedAt(domain.getUpdatedAt())
                .build();
    }
}