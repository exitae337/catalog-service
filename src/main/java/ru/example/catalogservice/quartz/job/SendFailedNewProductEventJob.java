package ru.example.catalogservice.quartz.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;
import ru.example.catalogservice.service.OutboxSenderService;
import ru.example.catalogservice.service.OutboxService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendFailedNewProductEventJob implements Job {

    private final OutboxService outboxService;
    private final OutboxSenderService outboxSenderService;

    @Value("${topic.new-product}")
    private String newProductEventTopic;

    @Override
    public void execute(JobExecutionContext context) {
        outboxService.getEventsByStatus(OutboxEventStatus.FAILED)
                .forEach(event -> outboxSenderService.sendEvent(event, newProductEventTopic, null));
    }
}
