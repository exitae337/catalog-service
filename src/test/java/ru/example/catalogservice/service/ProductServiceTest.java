package ru.example.catalogservice.service;

import ru.example.catalogservice.config.KafkaProperties;
import ru.example.catalogservice.model.payload.CreateProductRequest;
import ru.example.catalogservice.model.payload.ProductResponse;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.ProductEvent;
import ru.example.catalogservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Mock
    private KafkaProperties kafkaProperties;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProductAndSendEvent() {
        CreateProductRequest request = new CreateProductRequest(
                "Test",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.1),
                1L,
                null
        );

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Test")
                .price(BigDecimal.valueOf(100))
                .discount(BigDecimal.valueOf(0.1))
                .categoryId(1L)
                .build();

        given(repository.save(any(Product.class))).willReturn(savedProduct);
        given(kafkaProperties.getCatalogProducts()).willReturn("test-topic");

        ProductResponse response = service.createProduct(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test");

        verify(kafkaTemplate).send(
                eq("test-topic"),
                eq("1"),
                any(ProductEvent.class)
        );
    }

    @Test
    void shouldGetProductById() {
        Product product = new Product(1L, "Test", BigDecimal.TEN, BigDecimal.ZERO, 1L);
        given(repository.findById(1L)).willReturn(Optional.of(product));

        ProductResponse response = service.getProductById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test");
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        given(repository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void shouldGetProductsByCategoryId() {
        Product p1 = new Product(1L, "P1", BigDecimal.TEN, BigDecimal.ZERO, 1L);
        Product p2 = new Product(2L, "P2", BigDecimal.ONE, BigDecimal.ZERO, 1L);
        given(repository.findByCategoryId(1L)).willReturn(List.of(p1, p2));

        List<ProductResponse> products = service.getProductsByCategoryId(1L);

        assertThat(products).hasSize(2)
                .extracting(ProductResponse::name)
                .containsExactly("P1", "P2");
    }
}
