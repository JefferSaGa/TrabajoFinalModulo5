package com.tecsup.app.micro.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    private Long id;
    private Long orderId;
    private String driverName;
    private String driverPhone;
    private DeliveryStatus status;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void assignDriver(String driverName, String driverPhone) {
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.status = DeliveryStatus.ASSIGNED;
        this.estimatedDeliveryTime = LocalDateTime.now().plusMinutes(45); // Entrega en 45 min
        this.updatedAt = LocalDateTime.now();
    }
}