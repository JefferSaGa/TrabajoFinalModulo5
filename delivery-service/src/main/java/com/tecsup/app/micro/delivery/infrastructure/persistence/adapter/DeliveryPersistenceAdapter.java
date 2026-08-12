package com.tecsup.app.micro.delivery.infrastructure.persistence.adapter;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.repository.DeliveryRepository;
import com.tecsup.app.micro.delivery.infrastructure.persistence.entity.DeliveryEntity;
import com.tecsup.app.micro.delivery.infrastructure.persistence.mapper.DeliveryMapper;
import com.tecsup.app.micro.delivery.infrastructure.persistence.repository.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryPersistenceAdapter implements DeliveryRepository {

    private final DeliveryJpaRepository repository;
    private final DeliveryMapper mapper;

    @Override
    public Delivery save(Delivery delivery) {
        DeliveryEntity entity = mapper.toEntity(delivery);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Delivery> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return repository.existsByOrderId(orderId);
    }
}