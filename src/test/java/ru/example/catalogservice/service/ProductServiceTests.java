package ru.example.catalogservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import ru.example.catalogservice.exception.NotFoundException;
import ru.example.catalogservice.feign.ProductCategoryClient;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.mapper.ProductMapperImpl;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {
                ProductMapperImpl.class,
                ProductService.class,
        }
)
class ProductServiceTests {

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private ProductCategoryClient productCategoryClient;

    @MockitoBean
    private ProductOutboxService productOutboxService;

    @MockitoBean
    private ProductImageService productImageService;

    @Autowired
    private ProductService productService;

    @Test
    void testCreateProduct_WhenCategoryExistsAndImagesEmpty_ShouldReturnCreatedProductIdWithoutCreationImages() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Product")
                .price(BigDecimal.valueOf(100))
                .categoryId("Category")
                .build();
        CreateProductRequest request = new CreateProductRequest("Product", BigDecimal.valueOf(100), "Category");
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(productCategoryClient.categoryExists(any())).thenReturn(responseEntity);
        when(productRepository.save(any())).thenReturn(product);
        doNothing().when(productOutboxService).createEvent(any());

        assertEquals(product.getId(), productService.createProduct(request, null));
        verify(productOutboxService).createEvent(any());
        verify(productImageService, never()).saveImagesInFileStorage(anyList());
        verify(productImageService, never()).attachImagesToProduct(any(), anyList());
    }

    @Test
    void testCreateProduct_WhenCategoryNotExists_ShouldThrowException() {
        CreateProductRequest createProductRequest = new CreateProductRequest("Product", BigDecimal.valueOf(100), "INVALID_CATEGORY");
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(productCategoryClient.categoryExists(any())).thenReturn(responseEntity);

        assertThrows(
                NotFoundException.class,
                () -> productService.createProduct(createProductRequest, null)
        );
    }

    @Test
    void testCreateProduct_WhenCategoryExistsAndImagesNotEmpty_ShouldReturnCreatedProductIdWithCreationImages() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Product")
                .price(BigDecimal.valueOf(100))
                .categoryId("Category")
                .build();
        CreateProductRequest request = new CreateProductRequest("Product", BigDecimal.valueOf(100), "Category");
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(productCategoryClient.categoryExists(any())).thenReturn(responseEntity);
        when(productRepository.save(any())).thenReturn(product);
        doNothing().when(productOutboxService).createEvent(any());

        assertEquals(product.getId(), productService.createProduct(request, List.of(new MultipartFileMock())));
        verify(productOutboxService).createEvent(any());
        verify(productImageService).saveImagesInFileStorage(anyList());
        verify(productImageService).attachImagesToProduct(any(), anyList());
    }

    @Test
    void testGetProductById_WhenProductExists_ShouldReturnProductPayload() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("TestProduct")
                .price(BigDecimal.valueOf(100))
                .categoryId("TestCategory")
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        assertEquals(product.getId(), productService.getProductById(product.getId()).id());
    }

    @Test
    void testGetProductById_WhenProductNotExists_ShouldThrowException() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    void testGetProductsByCategoryId_WhenProductsNotExists_ShouldReturnEmptyList() {
        when(productRepository.findByCategoryId(any())).thenReturn(List.of());

        assertTrue(productService.getProductsByCategoryId("Category").isEmpty());
    }

    @Test
    void testGetProductsByCategoryId_WhenProductsExists_ShouldReturnNotEmptyList() {
        when(productRepository.findByCategoryId(any())).thenReturn(List.of(new Product(), new Product()));

        assertAll(
                () -> assertFalse(productService.getProductsByCategoryId("Category").isEmpty()),
                () -> assertEquals(2, productService.getProductsByCategoryId("Category").size())
        );
    }

    private static class MultipartFileMock implements MultipartFile {

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getOriginalFilename() {
            return "";
        }

        @Override
        public String getContentType() {
            return "";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return new byte[0];
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }
    }
}
