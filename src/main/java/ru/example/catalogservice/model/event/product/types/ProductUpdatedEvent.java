package ru.example.catalogservice.model.event.product.types;

import ru.example.catalogservice.model.event.product.ProductEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductUpdatedEvent(
        UUID id,
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId,
        Instant timestamp,
        String eventType
) implements ProductEvent {
    public ProductUpdatedEvent {
        eventType = "PRODUCT_UPDATED";
        timestamp = Instant.now();
    }
}
