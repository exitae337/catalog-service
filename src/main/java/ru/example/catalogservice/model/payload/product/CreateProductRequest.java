package ru.example.catalogservice.model.payload.product;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;


public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 255, message = "Product name must be less than 255 characters")
        String name,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 digits and 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Discount cannot be null")
        @DecimalMin(value = "0.00", message = "Discount cannot be negative")
        @DecimalMax(value = "1.00", message = "Discount cannot be greater than 1")
        @Digits(integer = 1, fraction = 2, message = "Discount must have up to 1 digit and 2 decimal places")
        BigDecimal discount,

        @NotNull(message = "Category ID cannot be null")
        Long categoryId,

        List<@URL String> imageUrls
) { }
