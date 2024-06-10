package com.findme.config;

import com.findme.cron.GCSDeleteProfileImageJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

@Configuration
public class CronConfig {

    @Bean
    public Scheduler scheduler(Scheduler scheduler) throws SchedulerException {
        scheduler.start();
        return scheduler;
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(GCSDeleteProfileImageJob.class)
                .withIdentity("GCSDeleteProfileImageJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("GCSDeleteProfileImageJobTrigger")
                .withSchedule(dailyAtHourAndMinute(12, 55))  // 00:00 UTC
                .build();
    }

}