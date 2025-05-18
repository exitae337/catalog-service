package ru.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.model.entity.ProductOutbox;
import ru.example.catalogservice.model.payload.kafka.NewProductEvent;
import ru.example.catalogservice.model.payload.kafka.enums.ProductOutboxStatus;
import ru.example.catalogservice.repository.ProductOutboxRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOutboxService {

    private final ProductOutboxRepository productOutboxRepository;
    private final KafkaTemplate<String, NewProductEvent> kafkaTemplate;

    @Value("${topic.new-product}")
    private String topicName;

    public void createEvent(NewProductEvent event) {
        productOutboxRepository.save(ProductOutbox.builder()
                .status(ProductOutboxStatus.CREATED)
                .productId(event.id())
                .payload(event)
                .build());
    }

    public void deleteAllByStatus(ProductOutboxStatus status) {
        productOutboxRepository.deleteAllByStatus(status);
    }

    public List<ProductOutbox> getEventsByStatus(ProductOutboxStatus status) {
        return productOutboxRepository.findAllByStatus(status);
    }

    public void update(ProductOutbox productOutbox) {
        productOutboxRepository.save(productOutbox);
    }

    public void sendEvents(List<ProductOutbox> events) {
        events.forEach(
                productOutbox -> kafkaTemplate
                        .send(topicName, productOutbox.getProductId().toString(), productOutbox.getPayload())
                        .whenComplete((result, error) -> {
                            if (error != null) {
                                productOutbox.setStatus(ProductOutboxStatus.FAILED);
                                update(productOutbox);
                                log.error("Error while sending new product event", error);
                            } else {
                                productOutbox.setStatus(ProductOutboxStatus.SENT);
                                update(productOutbox);
                                log.info("Successfully sent new product event");
                            }
                        })
        );
    }
}
