package com.tecsup.app.micro.payment.infrastructure.web.controller;

import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.infrastructure.web.dto.PaymentRequestDTO;
import com.tecsup.app.micro.payment.infrastructure.web.dto.PaymentResponseDTO;
import com.tecsup.app.micro.payment.infrastructure.web.mapper.PaymentWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;
    private final PaymentWebMapper paymentWebMapper;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO requestDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        log.info("REST Request - Procesando pago para la orden ID: {}", requestDTO.getOrderId());

        // Limpiamos el token JWT por si el servicio de pagos necesita validar seguridad o propagarlo
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else if (authHeader != null) {
            jwtToken = authHeader;
        }

        // Ejecutamos el servicio pasándole los datos necesarios
        Payment paymentDomain = paymentApplicationService.processPayment(
                requestDTO.getOrderId(),
                requestDTO.getAmount(),
                requestDTO.getMethod()
        );

        // Mapear respuesta
        PaymentResponseDTO responseDTO = paymentWebMapper.toDto(paymentDomain);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("REST Request - Consultando estado de pago para la orden ID: {}", orderId);

        return paymentApplicationService.getPaymentByOrderId(orderId)
                .map(paymentWebMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}