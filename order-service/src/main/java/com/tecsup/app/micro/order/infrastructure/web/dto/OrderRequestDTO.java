package com.tecsup.app.micro.order.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long customerId;

    @NotNull(message = "El ID del restaurante es obligatorio")
    private Long restaurantId;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String deliveryAddress;

    @NotEmpty(message = "El pedido debe tener al menos un plato/producto")
    private List<OrderItemRequestDTO> items;

    @Data
    public static class OrderItemRequestDTO {
        @NotNull(message = "El ID del producto es obligatorio")
        private Long productId;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer quantity;
    }
}