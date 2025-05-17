package ru.example.catalogservice.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    // TODO
    /*

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private final String API_URL = "/products";

    private final ProductResponse testProduct = new ProductResponse(
            1L,
            "Test Product",
            BigDecimal.valueOf(100.50),
            BigDecimal.valueOf(0.1),
            2L
    );

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        CreateProductRequest request = new CreateProductRequest(
                "Test Product",
                BigDecimal.valueOf(100.50),
                BigDecimal.valueOf(0.1),
                2L,
                null
        );

        given(productService.createProduct(any(CreateProductRequest.class)))
                .willReturn(testProduct);

        // When & Then
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100.50))
                .andExpect(jsonPath("$.discount").value(0.1))
                .andExpect(jsonPath("$.categoryId").value(2L));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        given(productService.getProductById(1L))
                .willReturn(testProduct);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        given(productService.getProductById(anyLong()))
                .willThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsByCategoryId_ShouldReturnList() throws Exception {
        List<ProductResponse> products = List.of(
                testProduct,
                new ProductResponse(2L, "Another Product", BigDecimal.valueOf(50), BigDecimal.ZERO, 2L)
        );

        given(productService.getProductsByCategoryId(2L))
                .willReturn(products);

        mockMvc.perform(get(API_URL).param("categoryId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoryId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Another Product"));
    }

    @Test
    void createProduct_WhenInvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateProductRequest invalidRequest = new CreateProductRequest(
                null,
                BigDecimal.valueOf(-10),
                BigDecimal.valueOf(1.5),
                null,
                null
        );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(4));
    }

    */
}
