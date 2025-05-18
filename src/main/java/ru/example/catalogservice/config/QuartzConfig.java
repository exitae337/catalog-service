package ru.example.catalogservice.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.example.catalogservice.quartz.job.DeleteSentEventsJob;
import ru.example.catalogservice.quartz.job.SendCreatedNewProductEventJob;
import ru.example.catalogservice.quartz.job.SendFailedNewProductEventJob;

@Configuration
public class QuartzConfig {

    private static final String SEND_CREATED_EVENT_JOB = "sendCreatedEventJob";
    private static final String SEND_FAILED_EVENT_JOB = "sendFailedEventJob";
    private static final String DELETE_SENT_EVENT_JOB = "deleteSentEventJob";

    private static final String SEND_CREATED_EVENT_TRIGGER = "sendCreatedEventTrigger";
    private static final String SEND_FAILED_EVENT_TRIGGER = "sendFailedEventTrigger";
    private static final String DELETE_SENT_EVENT_TRIGGER = "deleteSentEventTrigger";

    private static final String OUTBOX_GROUP = "outbox";

    @Value("${quartz.cron.new-product-event.created}")
    private String createdEventCron;

    @Value("${quartz.cron.new-product-event.failed}")
    private String failedEventCron;

    @Value("${quart.cron.delete-sent-event}")
    private String deleteSentEventCron;

    @Bean
    public JobDetail sendCreatedEventJob() {
        return JobBuilder.newJob(SendCreatedNewProductEventJob.class)
                .withIdentity(SEND_CREATED_EVENT_JOB, OUTBOX_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail sendFailedEventJob() {
        return JobBuilder.newJob(SendFailedNewProductEventJob.class)
                .withIdentity(SEND_FAILED_EVENT_JOB, OUTBOX_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail deleteSentEventJob() {
        return JobBuilder.newJob(DeleteSentEventsJob.class)
                .withIdentity(DELETE_SENT_EVENT_JOB, OUTBOX_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sendCreatedEventTrigger(JobDetail sendCreatedEventJob) {
        return TriggerBuilder.newTrigger()
                .forJob(sendCreatedEventJob)
                .withIdentity(SEND_CREATED_EVENT_TRIGGER, OUTBOX_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(createdEventCron))
                .build();
    }

    @Bean
    public Trigger sendFailedEventTrigger(JobDetail sendFailedEventJob) {
        return TriggerBuilder.newTrigger()
                .forJob(sendFailedEventJob)
                .withIdentity(SEND_FAILED_EVENT_TRIGGER, OUTBOX_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(failedEventCron))
                .build();
    }

    @Bean
    public Trigger deleteSentEventTrigger(JobDetail deleteSentEventJob) {
        return TriggerBuilder.newTrigger()
                .forJob(deleteSentEventJob)
                .withIdentity(DELETE_SENT_EVENT_TRIGGER, OUTBOX_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(deleteSentEventCron))
                .build();
    }
}
