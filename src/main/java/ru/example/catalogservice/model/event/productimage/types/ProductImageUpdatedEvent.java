package ru.example.catalogservice.model.event.productimage.types;

import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.event.productimage.ProductImageEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductImageUpdatedEvent(
        UUID id,
        UUID productId,
        String newImageUrl,
        Instant timestamp,
        String eventType
) implements ProductImageEvent {
    public ProductImageUpdatedEvent(ProductImage image) {
        this(
                image.getId(),
                image.getProduct().getId(),
                image.getImageUrl(),
                Instant.now(),
                "PRODUCT_IMAGE_UPDATED"
        );
    }
}
