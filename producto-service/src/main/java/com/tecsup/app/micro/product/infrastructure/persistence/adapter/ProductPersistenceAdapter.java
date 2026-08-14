package com.tecsup.app.micro.product.infrastructure.persistence.adapter;

import com.tecsup.app.micro.product.domain.model.Product;
import com.tecsup.app.micro.product.domain.repository.ProductRepository;
import com.tecsup.app.micro.product.infrastructure.persistence.entity.ProductEntity;
import com.tecsup.app.micro.product.infrastructure.persistence.mapper.ProductMapper;
import com.tecsup.app.micro.product.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCreatedBy(Long userId) {
        return jpaRepository.findByCreatedBy(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAvailableProducts() {
        // Lógica de negocio: productos con stock mayor a 0
        return jpaRepository.findByStockGreaterThan(0).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}