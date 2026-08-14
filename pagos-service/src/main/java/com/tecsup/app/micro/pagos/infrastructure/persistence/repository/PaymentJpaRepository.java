package com.tecsup.app.micro.pagos.infrastructure.persistence.repository;

import com.tecsup.app.micro.pagos.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByOrderId(Long orderId);
    boolean existsByOrderIdAndStatus(Long orderId, String status);
}