package com.tecsup.app.micro.ordenes.infrastructure.persistence.repository;

import com.tecsup.app.micro.ordenes.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderTrackingNumber(String trackingNumber);

    List<OrderEntity> findByCustomerId(Long customerId);
}