package ru.example.catalogservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productCategoryClient", url = "${service.product-category.url}")
public interface ProductCategoryClient {

    @GetMapping("/categories/{categoryId}")
    ResponseEntity<Void> categoryExists(@PathVariable("categoryId") String categoryId);
}
