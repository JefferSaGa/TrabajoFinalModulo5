package com.tecsup.app.micro.ordenes.infrastructure.client;

import com.tecsup.app.micro.ordenes.infrastructure.client.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${PRODUCT_SERVICE_URL:http://product-service.product-service.svc.cluster.local/api/products/}")
    private String productServiceUrl;


    @CircuitBreaker(name = "productService")
    @Retry(name = "productService", fallbackMethod = "getProductFallback")
    public ProductDTO getProductById(Long productId, String jwtToken) {
        log.info("Llamando al Product Service (Catálogo) para el plato ID: {}", productId);

        String url = this.productServiceUrl + productId;

        //Preparar Headers y propagar el JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (jwtToken != null && !jwtToken.isEmpty()) {
            headers.setBearerAuth(jwtToken);
        } else {
            log.warn("No se proporcionó token JWT para la llamada al Catálogo");
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Ejecutar la llamada REST
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

    public ProductDTO getProductFallback(Long productId, String jwtToken, Throwable throwable) {
        log.error("FALLBACK CRÍTICO: Catálogo de productos no disponible. Motivo: {}", throwable.getMessage());


        throw new RuntimeException("Lo sentimos, el catálogo  está experimentando demoras.Intente nuevamente.");
    }
}