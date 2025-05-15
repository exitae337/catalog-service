package ru.example.catalogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.event.product.ProductEvent;
import ru.example.catalogservice.model.event.product.ProductEventFactory;
import ru.example.catalogservice.model.mapper.ProductMapper;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductResponse;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest productRequest) {
        Product product = ProductMapper.toEntity(productRequest);

        List<ProductImage> images = productRequest.imageUrls().stream()
                .map(url -> ProductImage.builder()
                        .product(product)
                        .imageUrl(url)
                        .build())
                .toList();

        product.setImages(images);
        Product saved = productRepository.save(product);

        ProductEvent event = ProductEventFactory.createEvent(saved, ProductEventFactory.EventType.CREATED);
        kafkaTemplate.send("product-events", product.getId().toString(), event);

        return ProductMapper.toResponse(saved);
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductMapper.toResponse(product);
    }

    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

}
