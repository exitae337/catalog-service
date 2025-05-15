package ru.example.catalogservice.model.event.productimage;

import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.event.productimage.types.ProductImageAttachedEvent;
import ru.example.catalogservice.model.event.productimage.types.ProductImageDetachedEvent;
import ru.example.catalogservice.model.event.productimage.types.ProductImageUpdatedEvent;

import java.time.Instant;

public class ProductImageEventFactory {

    public static ProductImageEvent createEvent(ProductImage image, EventType type) {
        return switch (type) {
            case ATTACHED -> new ProductImageAttachedEvent(
                    image.getId(),
                    image.getProduct().getId(),
                    image.getImageUrl(),
                    Instant.now(),
                    "PRODUCT_CREATED"
            );
            case UPDATED -> new ProductImageUpdatedEvent(
                    image.getId(),
                    image.getProduct().getId(),
                    image.getImageUrl(),
                    Instant.now(),
                    "PRODUCT_CREATED"
            );
            case DETACHED -> new ProductImageDetachedEvent(
                    image.getId(),
                    image.getProduct().getId(),
                    Instant.now(),
                    "PRODUCT_IMAGE_DETACHED"
            );
        };
    }

    public enum EventType { ATTACHED, UPDATED, DETACHED }
}
