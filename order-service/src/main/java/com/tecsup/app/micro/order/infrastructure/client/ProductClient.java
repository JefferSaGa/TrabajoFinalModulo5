package com.tecsup.app.micro.order.infrastructure.client;

import com.tecsup.app.micro.order.infrastructure.client.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP para comunicarse con product-service (Catálogo de Platos).
 * Implementa propagación de JWT y Tolerancia a Fallos con Resilience4j.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${PRODUCT_SERVICE_URL:http://product-service.product-service.svc.cluster.local/api/products/}")
    private String productServiceUrl;

    /**
     * Obtiene el precio y stock real de un plato desde el Catálogo.
     *
     * @CircuitBreaker: Si el catálogo falla repetidamente, abre el circuito.
     * @Retry: Intenta llamar al catálogo varias veces antes de rendirse.
     */
    @CircuitBreaker(name = "productService")
    @Retry(name = "productService", fallbackMethod = "getProductFallback")
    public ProductDTO getProductById(Long productId, String jwtToken) {
        log.info("Llamando al Product Service (Catálogo) para el plato ID: {}", productId);

        String url = this.productServiceUrl + productId;

        // 1. Preparar Headers y propagar el JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (jwtToken != null && !jwtToken.isEmpty()) {
            headers.setBearerAuth(jwtToken);
        } else {
            log.warn("No se proporcionó token JWT para la llamada al Catálogo");
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 2. Ejecutar la llamada REST
            ResponseEntity<ProductDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, ProductDTO.class
            );
            log.info("Plato recuperado con éxito: {}", response.getBody().getName());
            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            log.error("El plato con ID {} no existe en el catálogo", productId);
            return null; // Retorna null para que el UseCase maneje que el ID es inválido
        }
    }

    /**
     * Fallback cuando el Product Service (Catálogo) colapsa por completo.
     * A diferencia de un User, no podemos devolver un plato "falso" porque arruinaría el cálculo del total.
     */
    public ProductDTO getProductFallback(Long productId, String jwtToken, Throwable throwable) {
        log.error("FALLBACK CRÍTICO: Catálogo de productos no disponible. Motivo: {}", throwable.getMessage());

        // Cortamos el flujo para no generar una orden con montos erróneos o en cero
        throw new RuntimeException("Lo sentimos, el catálogo de restaurantes está experimentando demoras. No pudimos validar el precio de sus platos. Intente nuevamente en unos minutos.");
    }
}