package ru.example.catalogservice.model.event.product;

import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.event.product.types.ProductCreatedEvent;
import ru.example.catalogservice.model.event.product.types.ProductDeletedEvent;
import ru.example.catalogservice.model.event.product.types.ProductUpdatedEvent;

import java.time.Instant;

public class ProductEventFactory {

    public static ProductEvent createEvent(Product product, EventType type) {
        return switch (type) {
            case CREATED -> new ProductCreatedEvent(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getDiscount(),
                    product.getCategoryId(),
                    Instant.now(),
                    "PRODUCT_CREATED"
            );
            case UPDATED -> new ProductUpdatedEvent(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getDiscount(),
                    product.getCategoryId(),
                    Instant.now(),
                    "PRODUCT_UPDATED"
            );
            case DELETED -> new ProductDeletedEvent(
                    product.getId(),
                    Instant.now(),
                    "PRODUCT_DELETED"
            );
        };
    }

    public enum EventType { CREATED, UPDATED, DELETED }
}
