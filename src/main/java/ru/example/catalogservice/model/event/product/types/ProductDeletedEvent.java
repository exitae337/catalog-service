package ru.example.catalogservice.model.event.product.types;

import ru.example.catalogservice.model.event.product.ProductEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductDeletedEvent(
        UUID id,
        Instant timestamp,
        String eventType
) implements ProductEvent {
    public ProductDeletedEvent {
        eventType = "PRODUCT_DELETED";
        timestamp = Instant.now();
    }

    @Override
    public String name() { return null; }

    @Override
    public BigDecimal price() { return null; }

    @Override
    public BigDecimal discount() { return null; }

    @Override
    public Long categoryId() { return null; }
}
