package ru.example.catalogservice.model.payload.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 255, message = "Product name must be less than 255 characters")
        String name,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 digits and 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Category ID cannot be null")
        String categoryId
) {
}
