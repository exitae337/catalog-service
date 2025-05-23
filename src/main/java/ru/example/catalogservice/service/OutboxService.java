package ru.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.catalogservice.model.entity.Outbox;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;
import ru.example.catalogservice.model.entity.enums.OutboxEventType;
import ru.example.catalogservice.repository.OutboxRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public void create(OutboxEventType eventType, String payload) {
        outboxRepository.save(Outbox.builder()
                .eventStatus(OutboxEventStatus.CREATED)
                .eventType(eventType)
                .payload(payload)
                .build());
    }

    @Transactional
    public void updateStatus(UUID id, OutboxEventStatus status) {
        outboxRepository.findById(id).ifPresent(
                outbox -> outbox.setEventStatus(status)
        );
    }

    public List<Outbox> getEventsByStatus(OutboxEventStatus status) {
        return outboxRepository.findAllByEventStatus(status);
    }

    public void deleteEventsByStatus(OutboxEventStatus status) {
        outboxRepository.deleteAllByEventStatus(status);
    }
}
