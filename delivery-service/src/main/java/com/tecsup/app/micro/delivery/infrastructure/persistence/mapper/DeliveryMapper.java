package com.tecsup.app.micro.delivery.infrastructure.persistence.mapper;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.model.DeliveryStatus;
import com.tecsup.app.micro.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public DeliveryEntity toEntity(Delivery domain) {
        if (domain == null) return null;
        return DeliveryEntity.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .driverName(domain.getDriverName())
                .driverPhone(domain.getDriverPhone())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .estimatedDeliveryTime(domain.getEstimatedDeliveryTime())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public Delivery toDomain(DeliveryEntity entity) {
        if (entity == null) return null;
        return Delivery.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .driverName(entity.getDriverName())
                .driverPhone(entity.getDriverPhone())
                .status(entity.getStatus() != null ? DeliveryStatus.valueOf(entity.getStatus()) : null)
                .estimatedDeliveryTime(entity.getEstimatedDeliveryTime())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}