package ru.example.catalogservice.model.event.productimage;

import java.time.Instant;
import java.util.UUID;

public interface ProductImageEvent {
    UUID id();
    UUID productId();
    String eventType();
    Instant timestamp();
}
