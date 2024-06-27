package com.findme.config;

import com.findme.cron.GCSDeleteProfileImageJob;
import com.findme.cron.RedisJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

@Configuration
public class CronConfig {

    @Bean
    public Scheduler scheduler(Scheduler scheduler) throws SchedulerException {
        scheduler.start();
        return scheduler;
    }

    @Bean
    public JobDetail gcsDeleteProfileImageJobDetail() {
        return JobBuilder.newJob(GCSDeleteProfileImageJob.class)
                .withIdentity("GCSDeleteProfileImageJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger gcsDeleteProfileImageJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(gcsDeleteProfileImageJobDetail())
                .withIdentity("GCSDeleteProfileImageJobTrigger")
                .withSchedule(dailyAtHourAndMinute(0, 0))
                .build();
    }

    @Bean
    public JobDetail redisJobDetail() {
        return JobBuilder.newJob(RedisJob.class)
                .withIdentity("RedisJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger redisJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(redisJobDetail())
                .withIdentity("RedisJobTrigger")
                .withSchedule(cronSchedule("* * * ? * *"))
                .build();
    }

}
