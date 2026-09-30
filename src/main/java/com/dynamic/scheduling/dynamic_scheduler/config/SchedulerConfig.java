package com.dynamic.scheduling.dynamic_scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler clientTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(15); // Handles 15 client tasks executing at the exact same millisecond
        scheduler.setThreadNamePrefix("ClientJob-");

        // CRITICAL MEMORY MANAGEMENT:
        // When we cancel/change a client's timeline, this removes the old job
        // from the memory queue entirely instead of leaving dead references.
        scheduler.setRemoveOnCancelPolicy(true);

        scheduler.initialize();
        return scheduler;
    }
}
