package ru.example.catalogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.model.event.product.ProductEvent;
import ru.example.catalogservice.model.event.product.ProductEventFactory;
import ru.example.catalogservice.model.event.productimage.ProductImageEvent;
import ru.example.catalogservice.model.event.productimage.ProductImageEventFactory;
import ru.example.catalogservice.repository.ProductImageRepository;
import ru.example.catalogservice.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductImageEvent> kafkaTemplate;

    @Transactional
    public ProductImage attachImage(UUID productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .isActive(true)
                .build();

        ProductImage saved = imageRepository.save(image);
        ProductImageEvent event = ProductImageEventFactory.createEvent(saved, ProductImageEventFactory.EventType.ATTACHED);
        kafkaTemplate.send("product-image-events", image.getId().toString(), event);

        return saved;
    }

    @Transactional
    public void detachImage(UUID imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Product image not found with id: " + imageId));

        image.setIsActive(false);
        ProductImage saved = imageRepository.save(image);

        ProductImageEvent event = ProductImageEventFactory.createEvent(saved, ProductImageEventFactory.EventType.ATTACHED);
        kafkaTemplate.send("product-image-events", image.getId().toString(), event);
    }
}
