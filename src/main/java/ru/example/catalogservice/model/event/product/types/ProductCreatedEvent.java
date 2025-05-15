package ru.example.catalogservice.model.event.product.types;

import jakarta.annotation.Nullable;
import ru.example.catalogservice.model.event.product.ProductEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductCreatedEvent(
        UUID id,
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId,
        Instant timestamp,
        String eventType
) implements ProductEvent {
    public ProductCreatedEvent {
        eventType = "PRODUCT_CREATED";
        timestamp = Instant.now();
    }
}
