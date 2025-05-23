package ru.example.catalogservice.quartz.job;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ru.example.catalogservice.model.entity.enums.OutboxEventStatus;
import ru.example.catalogservice.service.OutboxService;

@Component
@RequiredArgsConstructor
public class DeleteSentEventsJob implements Job {

    private final OutboxService outboxService;

    @Override
    public void execute(JobExecutionContext context) {
        outboxService.deleteEventsByStatus(OutboxEventStatus.SENT);
    }
}
