package com.tecsup.app.micro.payment.infrastructure.persistence.adapter;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.domain.repository.PaymentRepository;
import com.tecsup.app.micro.payment.infrastructure.persistence.entity.PaymentEntity;
import com.tecsup.app.micro.payment.infrastructure.persistence.mapper.PaymentMapper;
import com.tecsup.app.micro.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentRepository {

    private final PaymentJpaRepository repository;
    private final PaymentMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).map(mapper::toDomain);
    }

    @Override
    public boolean existsSuccessfulPaymentForOrder(Long orderId) {
        return repository.existsByOrderIdAndStatus(orderId, PaymentStatus.APPROVED.name());
    }
}