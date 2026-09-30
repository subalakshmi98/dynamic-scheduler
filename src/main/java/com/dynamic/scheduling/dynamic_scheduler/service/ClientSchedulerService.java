package com.dynamic.scheduling.dynamic_scheduler.service;

import com.dynamic.scheduling.dynamic_scheduler.config.ClientEmailJob;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class ClientSchedulerService {
    private final ThreadPoolTaskScheduler clientTaskScheduler;

    // Tracks active background running loops: Key = clientId, Value = Thread Handle
    private final Map<String, ScheduledFuture<?>> activeClientTimers = new ConcurrentHashMap<>();

    public ClientSchedulerService(ThreadPoolTaskScheduler clientTaskScheduler) {
        this.clientTaskScheduler = clientTaskScheduler;
    }

    /**
     * Schedules (or reschedules) a custom timeline for a single client
     */
    public void registerOrUpdateClientSchedule(String clientId, String cronExpression) {
        // 1. If this client already has an active schedule running, kill it first
        cancelClientSchedule(clientId);

        // 2. Instantiate our client-bound job logic
        ClientEmailJob structuralJob = new ClientEmailJob(clientId);

        // 3. Hand the job over to the thread pool with their custom Cron rule
        ScheduledFuture<?> futureTaskHandle = clientTaskScheduler.schedule(structuralJob, new CronTrigger(cronExpression));

        // 4. Save the handle so we can control/cancel it later
        activeClientTimers.put(clientId, futureTaskHandle);
        System.out.println("[SYSTEM] Client [" + clientId + "] timeline registered to: " + cronExpression);
    }

    /**
     * Completely removes a client from the active running engine queue
     */
    public void cancelClientSchedule(String clientId) {
        ScheduledFuture<?> activeTask = activeClientTimers.get(clientId);
        if (activeTask != null) {
            // false ensures that if the client's email job is actively mid-sending,
            // we let it gracefully finish instead of forcefully cutting the thread.
            activeTask.cancel(false);
            activeClientTimers.remove(clientId);
            System.out.println("[SYSTEM] Cancelled running scheduler loops for Client [" + clientId + "]");
        }
    }
}
