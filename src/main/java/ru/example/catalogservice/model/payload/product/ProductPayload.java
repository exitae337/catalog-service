package ru.example.catalogservice.model.payload.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductPayload(
        UUID id,
        String name,
        BigDecimal price,

        @JsonProperty("category_id")
        String categoryId,
        List<String> images
) implements Serializable {
}
