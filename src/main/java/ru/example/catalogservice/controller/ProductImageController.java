package ru.example.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.catalogservice.service.ProductImageService;
import ru.example.catalogservice.util.ContentTypeResolver;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;
    private final ContentTypeResolver contentTypeResolver;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImageByFileName(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(contentTypeResolver.resolveContentType(fileName))
                .body(productImageService.getImageByFileName(fileName));
    }
}
