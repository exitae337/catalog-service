package ru.example.catalogservice.model.payload;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be grater than zero")
        BigDecimal price,

        @NotNull(message = "Discount cannot be null")
        @DecimalMin(value = "0.0", message = "Discount cannot be negative")
        @DecimalMax(value = "1.0", message = "Discount cannot be greater than 1")
        BigDecimal discount,

        @NotNull(message = "Category ID cannot be null")
        Long categoryId,

        ProductPayload parent
) { }
