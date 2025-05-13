package ru.example.catalogservice.service;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.example.catalogservice.config.KafkaProperties;
import ru.example.catalogservice.model.payload.CreateProductRequest;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService service;

    @MockitoBean
    private ProductRepository repository;

    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoBean
    private KafkaProperties kafkaProperties;

    @Test
    void shouldCalculateFinalPriceWithDiscount() {
        CreateProductRequest request = new CreateProductRequest(
                "Test",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.2),
                1L,
                null
        );

        Product saved = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .discount(BigDecimal.valueOf(0.2))
                .build();

        when(repository.save(any())).thenReturn(saved);
        when(kafkaProperties.getCatalogProducts()).thenReturn("test-topic");

        var response = service.createProduct(request);

        assertThat(response.price())
                .isEqualByComparingTo("100");
        assertThat(response.discount())
                .isEqualByComparingTo("0.2");
    }
}
