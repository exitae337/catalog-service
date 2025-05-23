package ru.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.catalogservice.model.entity.Outbox;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxSenderService {

    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(Outbox event, String topic, String key) {
        kafkaTemplate.send(topic, key, event.getPayload())
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Ошибка при отправке сообщения в топик: {}. Сообщение: {}", topic, error.getMessage());
                        outboxService.updateStatus(event.getId(), OutboxEventStatus.FAILED);
                    } else {
                        log.info("Сообщение успешно отправлено в топик: {}", topic);
                        outboxService.updateStatus(event.getId(), OutboxEventStatus.SENT);
                    }
                });
    }
}
