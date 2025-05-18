package ru.example.catalogservice.quartz.job;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ru.example.catalogservice.model.payload.kafka.enums.ProductOutboxStatus;
import ru.example.catalogservice.service.ProductOutboxService;

@Component
@RequiredArgsConstructor
public class DeleteSentEventsJob implements Job {

    private final ProductOutboxService productOutboxService;

    @Override
    public void execute(JobExecutionContext context) {
        productOutboxService.deleteAllByStatus(ProductOutboxStatus.SENT);
    }
}
