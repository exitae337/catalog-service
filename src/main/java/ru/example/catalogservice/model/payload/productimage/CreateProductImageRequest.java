package ru.example.catalogservice.model.payload.productimage;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateProductImageRequest (
        @NotBlank @URL String imageUrl
) {}
