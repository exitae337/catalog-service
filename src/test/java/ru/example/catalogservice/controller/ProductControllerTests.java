package ru.example.catalogservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.example.catalogservice.exception.NotFoundException;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductPayload;
import ru.example.catalogservice.service.ProductService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateProduct_WhenPayloadCorrect_shouldReturnStatusCode201() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("TEST_PRODUCT", BigDecimal.valueOf(100), "TEST_CATEGORY");
        MockPart requestPart = new MockPart("body", objectMapper.writeValueAsBytes(createProductRequest));
        requestPart.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        when(productService.createProduct(createProductRequest, null)).thenReturn(UUID.randomUUID());

        mockMvc.perform(multipart("/products")
                        .part(requestPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateProduct_WhenPayloadWithErrors_shouldReturnStatusCode400() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("", BigDecimal.valueOf(100), "");
        MockPart requestPart = new MockPart("body", objectMapper.writeValueAsBytes(createProductRequest));
        requestPart.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Validation error");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        when(productService.createProduct(createProductRequest, null)).thenThrow(exception);

        mockMvc.perform(multipart("/products")
                        .part(requestPart))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductById_WhenProductExists_shouldReturnStatusCode200() throws Exception {
        ProductPayload product = new ProductPayload(UUID.randomUUID(), "Product", BigDecimal.ZERO, "Category", null);
        when(productService.getProductById(any())).thenReturn(product);

        mockMvc.perform(get("/products/" + product.id()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductById_WhenProductNotExists_shouldReturnStatusCode404() throws Exception {
        when(productService.getProductById(any())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/products/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductsByCategory_WhenProductsExists_shouldReturnStatusCode200AndNotEmptyBody() throws Exception {
        String category = "Category";
        List<ProductPayload> products = IntStream.range(0, 5)
                .mapToObj(i -> new ProductPayload(UUID.randomUUID(), "Product " + i, BigDecimal.valueOf(100), category, null))
                .toList();
        when(productService.getProductsByCategoryId(any())).thenReturn(products);

        MvcResult result = mockMvc.perform(get("/products")
                        .param("category", category))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(5, objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<?>>() {
        }).size());
    }

    @Test
    void testGetProductsByCategory_WhenProductsNotExists_shouldReturnStatusCode200AndEmptyArray() throws Exception {
        String category = "Category";
        when(productService.getProductsByCategoryId(any())).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/products")
                        .param("category", category))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(0, objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<?>>() {
        }).size());
    }
}
