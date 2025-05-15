package ru.example.catalogservice.model.event.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface ProductEvent {
    UUID id();
    String name();
    BigDecimal price();
    BigDecimal discount();
    Long categoryId();
    Instant timestamp();
    String eventType();
}
