package ru.example.catalogservice.model.mapper;

import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductResponse;
import ru.example.catalogservice.model.payload.productimage.ProductImageResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .name(request.name())
                .price(request.price())
                .discount(request.discount())
                .categoryId(request.categoryId())
                .isActive(true)
                .build();
    }

    public static ProductResponse toResponse(Product entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDiscount(),
                entity.getCategoryId(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getIsActive(),
                mapImagesToResponses(entity.getImages())
        );
    }

    private static List<ProductImageResponse> mapImagesToResponses(List<ProductImage> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .filter(ProductImage::getIsActive)
                .map(ProductImageMapper::toResponse)
                .collect(Collectors.toList());
    }
}
