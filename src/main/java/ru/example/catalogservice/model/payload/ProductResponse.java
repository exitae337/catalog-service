package ru.example.catalogservice.model.payload;

import java.math.BigDecimal;


public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId
) {}
