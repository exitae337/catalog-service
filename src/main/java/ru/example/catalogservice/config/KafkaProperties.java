package ru.example.catalogservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Configuration
@ConfigurationProperties(prefix = "app.kafka.topic")
public class KafkaProperties {
    private String catalogProducts;
}
