package ru.example.catalogservice.model.payload.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public record NewProductEvent(
        UUID id,
        String name,
        BigDecimal price
) {
}
