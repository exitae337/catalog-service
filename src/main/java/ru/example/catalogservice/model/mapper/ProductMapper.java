package ru.example.catalogservice.model.mapper;

import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.payload.CreateProductRequest;
import ru.example.catalogservice.model.payload.ProductResponse;

public class ProductMapper {

    public static Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .name(request.name())
                .price(request.price())
                .discount(request.discount())
                .categoryId(request.categoryId())
                .build();
    }

    public static ProductResponse toResponse(Product entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDiscount(),
                entity.getCategoryId()
        );
    }
}
