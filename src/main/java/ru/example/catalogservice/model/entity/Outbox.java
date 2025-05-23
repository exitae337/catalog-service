package ru.example.catalogservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;
import ru.example.catalogservice.model.entity.enums.OutboxEventType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private OutboxEventType eventType;

    @Column(name = "payload")
    private String payload;

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    private OutboxEventStatus eventStatus;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant modifiedAt;

    @PreUpdate
    public void onModify() {
        modifiedAt = Instant.now();
    }
}
