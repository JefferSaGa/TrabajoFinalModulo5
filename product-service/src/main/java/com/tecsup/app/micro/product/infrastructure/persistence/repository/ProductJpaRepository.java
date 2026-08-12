package com.tecsup.app.micro.product.infrastructure.persistence.repository;

import com.tecsup.app.micro.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategory(String category);

    List<ProductEntity> findByCreatedBy(Long userId);

    // Para cumplir con findAvailableProducts() (stock > 0)
    List<ProductEntity> findByStockGreaterThan(Integer stock);
}