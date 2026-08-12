package com.tecsup.app.micro.order.infrastructure.web.controller;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.infrastructure.web.dto.OrderRequestDTO;
import com.tecsup.app.micro.order.infrastructure.web.dto.OrderResponseDTO;
import com.tecsup.app.micro.order.infrastructure.web.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final OrderWebMapper orderWebMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO requestDTO,
            // Capturamos el header "Authorization"
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        log.info("REST Request - Recibiendo pedido de delivery para el cliente ID: {}", requestDTO.getCustomerId());

        //  Limpiamos el token (quitamos la palabra "Bearer " si existe)
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Extraemos solo el token puro
        } else if (authHeader != null) {
            jwtToken = authHeader; // Por si lo mandan sin la palabra "Bearer "
        }

        // Mapear JSON a Dominio
        Order orderDomain = orderWebMapper.toDomain(requestDTO);

        // Ejecutamos el servicio pasándole la orden y el token extraído
        Order createdOrder = orderApplicationService.createOrder(orderDomain, jwtToken);

        // Mapear respuesta
        OrderResponseDTO responseDTO = orderWebMapper.toResponseDTO(createdOrder);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}