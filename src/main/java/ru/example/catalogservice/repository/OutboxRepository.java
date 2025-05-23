package ru.example.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.catalogservice.model.entity.Outbox;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    List<Outbox> findAllByEventStatus(OutboxEventStatus status);

    void deleteAllByEventStatus(OutboxEventStatus status);
}
