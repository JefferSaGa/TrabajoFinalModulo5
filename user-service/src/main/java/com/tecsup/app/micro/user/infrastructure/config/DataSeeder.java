package com.tecsup.app.micro.user.infrastructure.config;

import com.tecsup.app.micro.user.infrastructure.persistence.entity.RoleEntity;
import com.tecsup.app.micro.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Sembrador de datos iniciales.
 * Se ejecuta automáticamente al levantar la aplicación.
 * Ideal para entornos de desarrollo y Docker limpios.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Consultamos si ya existen usuarios
        Long userCount = entityManager.createQuery("SELECT COUNT(u) FROM UserEntity u", Long.class).getSingleResult();

        if (userCount == 0) {
            log.info("🌱 Base de datos limpia detectada. Sembrando usuario administrador inicial...");

            // 1. Crear el Rol de Administrador
            RoleEntity adminRole = new RoleEntity();
            // Si tu campo en RoleEntity no se llama 'name', cámbialo aquí (ej. setRoleName)
            adminRole.setName("ROLE_ADMIN");
            entityManager.persist(adminRole);

            // 2. Crear la Entidad de Usuario aprovechando tu @Builder
            UserEntity adminUser = UserEntity.builder()
                    .name("Administrador Supremo")
                    .email("admin@empresa.com")
                    .password(passwordEncoder.encode("admin123"))
                    .enabled(true)
                    .roles(Set.of(adminRole)) // Relacionamos el rol creado
                    .build();

            entityManager.persist(adminUser);

            log.info("✅ ¡Usuario semilla creado exitosamente!");
            log.info("🔑 Email: admin@empresa.com");
            log.info("🔑 Password: admin123");
        } else {
            log.info("✅ Base de datos ya contiene usuarios. Omitiendo DataSeeder.");
        }
    }
}