package ru.example.catalogservice.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxJobScheduler {

    private final Scheduler scheduler;
    private final JobDetail sendCreatedEventJob;
    private final JobDetail sendFailedEventJob;
    private final JobDetail deleteSentEventJob;
    private final Trigger sendCreatedEventTrigger;
    private final Trigger sendFailedEventTrigger;
    private final Trigger deleteSentEventTrigger;

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleSendCreatedEventJob() {
        try {
            scheduler.scheduleJob(sendCreatedEventJob, sendCreatedEventTrigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling sendCreatedEventJob: {}", e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleSendFailedEventJob() {
        try {
            scheduler.scheduleJob(sendFailedEventJob, sendFailedEventTrigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling sendFailedEventJob: {}", e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleDeleteSentEventJob() {
        try {
            scheduler.scheduleJob(deleteSentEventJob, deleteSentEventTrigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling deleteSentEventJob: {}", e.getMessage());
        }
    }
}
