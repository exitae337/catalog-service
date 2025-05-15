package ru.example.catalogservice.model.mapper;

import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.payload.productimage.CreateProductImageRequest;
import ru.example.catalogservice.model.payload.productimage.ProductImageResponse;

public class ProductImageMapper {

    public static ProductImage toEntity(CreateProductImageRequest request, Product product) {
        return ProductImage.builder()
                .imageUrl(request.imageUrl())
                .product(product)
                .isActive(true)
                .build();
    }

    public static ProductImageResponse toResponse(ProductImage entity) {
        return new ProductImageResponse(
                entity.getId(),
                entity.getImageUrl(),
                entity.getIsActive()
        );
    }

}
