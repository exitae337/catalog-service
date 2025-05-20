package ru.example.catalogservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
class ProductRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("catalog-service-test")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindByCategoryId_WhenCategoryExists_ShouldReturnProductsWithCategory() {
        assertThat(productRepository.findByCategoryId("PHONES"))
                .isNotEmpty()
                .hasSize(4);
    }

    @Test
    void testFindByCategoryId_WhenCategoryDoesNotExist_ShouldReturnEmptyProducts() {
        assertThat(productRepository.findByCategoryId("INVALID_CATEGORY"))
                .isEmpty();
    }
}
