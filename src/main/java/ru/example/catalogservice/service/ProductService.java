package ru.example.catalogservice.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ru.example.catalogservice.exception.NotFoundException;
import ru.example.catalogservice.feign.ProductCategoryClient;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.mapper.ProductMapper;
import ru.example.catalogservice.model.payload.kafka.NewProductEvent;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductPayload;
import ru.example.catalogservice.repository.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryClient productCategoryClient;
    private final ProductOutboxService productOutboxService;
    private final ProductMapper productMapper;
    private final ProductImageService productImageService;

    @CacheEvict(value = "products_with_category", key = "#createProductRequest.categoryId()")
    @Transactional
    public UUID createProduct(@Valid CreateProductRequest createProductRequest, List<MultipartFile> images) {
        ResponseEntity<Void> categoryResponse = productCategoryClient.categoryExists(createProductRequest.categoryId());
        if (categoryResponse.getStatusCode().is2xxSuccessful()) {
            Product product = productRepository.save(Product.builder()
                    .name(createProductRequest.name())
                    .price(createProductRequest.price())
                    .categoryId(createProductRequest.categoryId())
                    .build());
            productOutboxService.createEvent(new NewProductEvent(product.getId(), product.getName(), product.getPrice()));
            if (images != null && !images.isEmpty()) {
                CompletableFuture
                        .supplyAsync(() -> productImageService.saveImagesInFileStorage(images))
                        .thenAccept(imageUrls -> productImageService.attachImagesToProduct(product, imageUrls));
            }
            return product.getId();
        }
        throw new NotFoundException("Category with ID: '%s' not found".formatted(createProductRequest.categoryId()));
    }

    @Cacheable(value = "products", key = "#id")
    public ProductPayload getProductById(UUID id) {
        Product product = getEntityById(id);
        return productMapper.mapToProductPayload(product, productImageService.getImagesUrls(product.getImages()));
    }

    @Cacheable(value = "products_with_category", key = "#categoryId")
    public List<ProductPayload> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(product -> productMapper.mapToProductPayload(product, productImageService.getImagesUrls(product.getImages())))
                .toList();
    }

    public Product getEntityById(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product with ID: '%s' not found".formatted(id))
        );
    }
}
