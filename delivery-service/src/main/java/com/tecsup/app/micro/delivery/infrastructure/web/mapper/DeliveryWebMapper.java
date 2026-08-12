package com.tecsup.app.micro.delivery.infrastructure.web.mapper;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.infrastructure.web.dto.DeliveryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class DeliveryWebMapper {

    public DeliveryResponseDTO toDto(Delivery domain) {
        if (domain == null) return null;
        return DeliveryResponseDTO.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .driverName(domain.getDriverName())
                .driverPhone(domain.getDriverPhone())
                .status(domain.getStatus() != null ? domain.getStatus().name() : "UNKNOWN")
                .estimatedDeliveryTime(domain.getEstimatedDeliveryTime())
                .build();
    }
}