package ru.example.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.catalogservice.model.entity.ProductOutbox;
import ru.example.catalogservice.model.payload.kafka.enums.ProductOutboxStatus;

import java.util.List;
import java.util.UUID;

public interface ProductOutboxRepository extends JpaRepository<ProductOutbox, UUID> {

    List<ProductOutbox> findAllByStatus(ProductOutboxStatus status);

    void deleteAllByStatus(ProductOutboxStatus status);
}
