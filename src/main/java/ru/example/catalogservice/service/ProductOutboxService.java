package ru.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.model.entity.ProductOutbox;
import ru.example.catalogservice.model.payload.kafka.NewProductEvent;
import ru.example.catalogservice.model.payload.kafka.enums.ProductOutboxStatus;
import ru.example.catalogservice.repository.ProductOutboxRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductOutboxService {

    private final ProductOutboxRepository productOutboxRepository;

    public void save(NewProductEvent event) {
        productOutboxRepository.save(ProductOutbox.builder()
                .status(ProductOutboxStatus.CREATED)
                .productId(event.id())
                .payload(event)
                .build());
    }

    public void deleteById(UUID id) {
        productOutboxRepository.deleteById(id);
    }
}
