package com.dynamic.scheduling.dynamic_scheduler.config;

import com.dynamic.scheduling.dynamic_scheduler.model.ClientScheduleConfig;
import com.dynamic.scheduling.dynamic_scheduler.repository.ClientScheduleConfigRepository;
import com.dynamic.scheduling.dynamic_scheduler.service.ClientSchedulerService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerInitializer {
    private final ClientScheduleConfigRepository repository;
    private final ClientSchedulerService schedulerService;

    public SchedulerInitializer(ClientScheduleConfigRepository repository, ClientSchedulerService schedulerService) {
        this.repository = repository;
        this.schedulerService = schedulerService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void hydrateEngineOnBootUp() {
        System.out.println("[STARTUP] Server is fully online. Rebuilding individual client engines...");

        List<ClientScheduleConfig> activeConfigs = repository.findByEnabledTrue();

        for (ClientScheduleConfig config : activeConfigs) {
            schedulerService.registerOrUpdateClientSchedule(
                    config.getClientId(),
                    config.getCronExpression()
            );
        }
        System.out.println("[STARTUP] Finished hydration. " + activeConfigs.size() + " custom schedules restored.");
    }
}
