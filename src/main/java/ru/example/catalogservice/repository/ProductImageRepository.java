package ru.example.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.catalogservice.model.entity.ProductImage;

import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
}
