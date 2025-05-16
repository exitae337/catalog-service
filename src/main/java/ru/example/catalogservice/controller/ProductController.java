package ru.example.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.example.catalogservice.model.payload.product.CreateProductRequest;
import ru.example.catalogservice.model.payload.product.ProductPayload;
import ru.example.catalogservice.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createProduct(@RequestPart("body") CreateProductRequest productRequest,
                                              @RequestPart("images") List<MultipartFile> images) {
        UUID createdProductId = productService.createProduct(productRequest, images);
        return ResponseEntity.created(URI.create("/products/" + createdProductId)).build();
    }

    @GetMapping("/{id}")
    public ProductPayload getProductById(@PathVariable("id") UUID id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductPayload> getProductsByCategoryId(@RequestParam("category") String categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
}
