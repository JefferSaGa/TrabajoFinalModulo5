package com.tecsup.app.micro.ordenes.infrastructure.web.controller;

import com.tecsup.app.micro.ordenes.application.service.OrderApplicationService;
import com.tecsup.app.micro.ordenes.domain.model.Order;
import com.tecsup.app.micro.ordenes.infrastructure.web.dto.OrderRequestDTO;
import com.tecsup.app.micro.ordenes.infrastructure.web.dto.OrderResponseDTO;
import com.tecsup.app.micro.ordenes.infrastructure.web.mapper.OrderWebMapper;
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

            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        log.info("REST Request - Recibiendo pedido de delivery para el cliente ID: {}", requestDTO.getCustomerId());


        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Extraemos solo el token puro
        } else if (authHeader != null) {
            jwtToken = authHeader; // Por si lo mandan sin la palabra "Bearer "
        }


        Order orderDomain = orderWebMapper.toDomain(requestDTO);


        Order createdOrder = orderApplicationService.createOrder(orderDomain, jwtToken);


        OrderResponseDTO responseDTO = orderWebMapper.toResponseDTO(createdOrder);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}