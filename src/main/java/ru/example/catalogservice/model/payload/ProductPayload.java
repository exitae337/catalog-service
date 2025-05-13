package ru.example.catalogservice.model.payload;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductPayload(
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId
) implements Serializable { }
