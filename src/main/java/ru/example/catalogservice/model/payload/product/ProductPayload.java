package ru.example.catalogservice.model.payload.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductPayload(
        UUID id,
        String name,
        BigDecimal price,
        String categoryId,
        List<String> images
) implements Serializable {
}
