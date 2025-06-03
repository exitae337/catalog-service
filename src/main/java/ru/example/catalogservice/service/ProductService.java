package ru.example.catalogservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ru.example.catalogservice.exception.NotFoundException;
import ru.example.catalogservice.feign.ProductCategoryClient;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.entity.enums.OutboxEventType;
import ru.example.catalogservice.model.mapper.ProductMapper;
import ru.example.catalogservice.model.payload.PageResponse;
import ru.example.catalogservice.model.payload.kafka.NewProductEvent;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductPayload;
import ru.example.catalogservice.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryClient productCategoryClient;
    private final OutboxService outboxService;
    private final ProductMapper productMapper;
    private final ProductImageService productImageService;
    private final ObjectMapper objectMapper;

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

            NewProductEvent newProductEvent = new NewProductEvent(product.getId(), product.getName(), product.getPrice());
            try {
                outboxService.create(OutboxEventType.NEW_PRODUCT, objectMapper.writeValueAsString(newProductEvent));
            } catch (JsonProcessingException e) {
                log.error("Ошибка сериализации outbox события: {}. Сообщение: {}", newProductEvent.id(), e.getMessage());
            }

            if (images != null && !images.isEmpty()) {
                List<String> imageUrls = productImageService.saveImagesInFileStorage(images);
                productImageService.attachImagesToProduct(product, imageUrls);
            }

            return product.getId();
        }
        throw new NotFoundException("Категория с ID: '%s' не найдена".formatted(createProductRequest.categoryId()));
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

    public PageResponse<ProductPayload> getProductsPage(int pageNumber, int pageSize) {
        Page<Product> products = productRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return new PageResponse<>(
                products.getNumber(),
                products.getNumberOfElements(),
                products.getTotalPages(),
                products.getContent()
                        .stream()
                        .map(p -> productMapper.mapToProductPayload(p, productImageService.getImagesUrls(p.getImages())))
                        .toList()
        );
    }

    public Product getEntityById(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Товар с ID: '%s' не найден".formatted(id))
        );
    }
}
