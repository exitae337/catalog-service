package ru.example.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.catalogservice.model.entity.ProductOutbox;

import java.util.UUID;

public interface ProductOutboxRepository extends JpaRepository<ProductOutbox, UUID> {
}
