package com.tecsup.app.micro.delivery.infrastructure.web.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponseDTO {
    private Long id;
    private Long orderId;
    private String driverName;
    private String driverPhone;
    private String status;
    private LocalDateTime estimatedDeliveryTime;
}