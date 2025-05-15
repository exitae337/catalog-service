package ru.example.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.service.ProductImageService;

import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService imageService;

    @PostMapping
    public ProductImage attachImage(
            @PathVariable UUID productId,
            @RequestParam String imageUrl
    ) {
        return imageService.attachImage(productId, imageUrl);
    }

    @DeleteMapping("/{imageId}")
    public void detachImage(@PathVariable UUID imageId) {
        imageService.detachImage(imageId);
    }
}
