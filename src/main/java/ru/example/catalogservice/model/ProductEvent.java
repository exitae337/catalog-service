package ru.example.catalogservice.model;

import java.math.BigDecimal;

public record ProductEvent(
        Long id,
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId
) {}
