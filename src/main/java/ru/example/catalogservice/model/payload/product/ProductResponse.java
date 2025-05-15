package ru.example.catalogservice.model.payload.product;

import ru.example.catalogservice.model.payload.productimage.ProductImageResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        BigDecimal discount,
        Long categoryId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Boolean isActive,
        List<ProductImageResponse> images
) {}
