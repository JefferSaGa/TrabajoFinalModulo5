package com.tecsup.app.micro.pedidos.application.service;

import com.tecsup.app.micro.pedidos.application.usecase.AssignDeliveryUseCase;
import com.tecsup.app.micro.pedidos.application.usecase.GetDeliveryUseCase;
import com.tecsup.app.micro.pedidos.domain.model.Delivery;
import com.tecsup.app.micro.pedidos.domain.model.DeliveryStatus;
import com.tecsup.app.micro.pedidos.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryApplicationService implements AssignDeliveryUseCase,GetDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery assignDeliveryForOrder(Long orderId) {
        log.info("Iniciando asignación de motorizado para la orden: {}", orderId);

        if (deliveryRepository.existsByOrderId(orderId)) {
            log.warn("La orden {} tiene un delivery asignado.", orderId);
            return deliveryRepository.findByOrderId(orderId).orElseThrow();
        }

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .status(DeliveryStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        delivery.assignDriver("Oleg Sovero", "111155533");

        log.info("Motorizado asignado: {} para la orden {}", delivery.getDriverName(), orderId);

        return deliveryRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }
}