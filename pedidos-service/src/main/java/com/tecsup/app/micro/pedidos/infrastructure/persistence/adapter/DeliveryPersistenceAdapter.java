package com.tecsup.app.micro.pedidos.infrastructure.persistence.adapter;

import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import com.tecsup.app.micro.pedidos.domain.repository.DeliveryRepository;
import com.tecsup.app.micro.pedidos.infrastructure.persistence.entity.DeliveryEntity;
import com.tecsup.app.micro.pedidos.infrastructure.persistence.mapper.DeliveryMapper;
import com.tecsup.app.micro.pedidos.infrastructure.persistence.repository.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryPersistenceAdapter implements DeliveryRepository {

    private final DeliveryJpaRepository pedidorepository;
    private final DeliveryMapper objMapper;

    @Override
    public Delivery save(Delivery delivery) {
        DeliveryEntity entity = objMapper.toEntity(delivery);
        return objMapper.toDomain(pedidorepository.save(entity));
    }

    @Override
    public Optional<Delivery> findByOrderId(Long orderId) {
        return pedidorepository.findByOrderId(orderId).map(objMapper::toDomain);
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return pedidorepository.existsByOrderId(orderId);
    }
}