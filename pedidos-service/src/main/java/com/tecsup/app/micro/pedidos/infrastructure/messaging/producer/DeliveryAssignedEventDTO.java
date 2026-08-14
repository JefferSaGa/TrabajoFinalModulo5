package com.tecsup.app.micro.pedidos.infrastructure.messaging.producer;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryAssignedEventDTO {
    private Long deliveryId;
    private Long orderId;
    private String driverName;
    private String driverPhone;
    private LocalDateTime estimatedDeliveryTime;
}