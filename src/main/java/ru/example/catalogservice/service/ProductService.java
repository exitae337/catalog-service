package ru.example.catalogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.config.KafkaProperties;
import ru.example.catalogservice.model.mapper.ProductMapper;
import ru.example.catalogservice.model.payload.CreateProductRequest;
import ru.example.catalogservice.model.payload.ProductResponse;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.ProductEvent;
import ru.example.catalogservice.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest productRequest) {
        Product entity = ProductMapper.toEntity(productRequest);
        Product saved = productRepository.save(entity);

        kafkaTemplate.send(
                kafkaProperties.getCatalogProducts(),
                new ProductEvent(saved.getId(), saved.getName(), saved.getPrice(), saved.getDiscount(), saved.getCategoryId())
        );

        return ProductMapper.toResponse(saved);
    }

    public ProductResponse getProductById(Long id) {
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
