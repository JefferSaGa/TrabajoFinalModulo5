package com.tecsup.app.micro.product.infrastructure.persistence.mapper;

import com.tecsup.app.micro.product.domain.model.Product;
import com.tecsup.app.micro.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para transformar entre ProductEntity (Infraestructura) y Product (Dominio).
 */
@Component
public class ProductMapper {

    // De Entidad JPA (Base de datos) -> Modelo de Dominio (Negocio)
    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .category(entity.getCategory())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // De Modelo de Dominio (Negocio) -> Entidad JPA (Base de datos)
    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;

        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .category(domain.getCategory())
                .createdBy(domain.getCreatedBy())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}