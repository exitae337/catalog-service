package ru.example.catalogservice.model.payload.productimage;

import java.util.UUID;

public record ProductImageResponse(
        UUID id,
        String imageUrl,
        Boolean isActive
) {}
