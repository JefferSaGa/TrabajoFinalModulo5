package com.tecsup.app.micro.pedidos.infrastructure.web.controller;

import com.tecsup.app.micro.pedidos.application.usecase.GetDeliveryUseCase;
import com.tecsup.app.micro.pedidos.infrastructure.web.dto.DeliveryResponseDTO;
import com.tecsup.app.micro.pedidos.infrastructure.web.mapper.DeliveryWebMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final GetDeliveryUseCase getDeliveryUseCase;
    private final DeliveryWebMapper deliveryWebMapper;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponseDTO> getDeliveryByOrderId(@PathVariable Long orderId) {
        log.info("REST Request - Consultando estado de delivery para la orden ID: {}", orderId);

        return getDeliveryUseCase.getDeliveryByOrderId(orderId)
                .map(deliveryWebMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}