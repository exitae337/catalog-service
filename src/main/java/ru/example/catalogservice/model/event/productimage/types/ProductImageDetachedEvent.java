package ru.example.catalogservice.model.event.productimage.types;

import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.event.productimage.ProductImageEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductImageDetachedEvent(
        UUID id,
        UUID productId,
        Instant timestamp,
        String eventType
) implements ProductImageEvent {
    public ProductImageDetachedEvent(ProductImage image) {
        this(
                image.getId(),
                image.getProduct().getId(),
                Instant.now(),
                "PRODUCT_IMAGE_DETACHED"
        );
    }
}
