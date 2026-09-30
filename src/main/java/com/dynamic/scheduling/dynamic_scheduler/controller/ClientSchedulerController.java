package com.dynamic.scheduling.dynamic_scheduler.controller;

import com.dynamic.scheduling.dynamic_scheduler.model.ClientScheduleConfig;
import com.dynamic.scheduling.dynamic_scheduler.repository.ClientScheduleConfigRepository;
import com.dynamic.scheduling.dynamic_scheduler.service.ClientSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduler")
public class ClientSchedulerController {
    private final ClientScheduleConfigRepository repository;
    private final ClientSchedulerService schedulerService;

    public ClientSchedulerController(ClientScheduleConfigRepository repository, ClientSchedulerService schedulerService) {
        this.repository = repository;
        this.schedulerService = schedulerService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> updateClientSchedule(@RequestParam String clientId, @RequestParam String cronExpression) {
        // 1. Create the setting configuration in persistent storage
        ClientScheduleConfig config = repository.findById(clientId)
                .map(existingConfig -> {
                    existingConfig.setCronExpression(cronExpression);
                    existingConfig.setEnabled(true);
                    return existingConfig;
                })
                .orElseGet(() -> new ClientScheduleConfig(clientId, cronExpression, true));

        // 2. Persist the clean state back to your database
        repository.save(config);

        // 3. Update the running in-memory scheduler engine immediately
        schedulerService.registerOrUpdateClientSchedule(clientId, cronExpression);

        return ResponseEntity.ok("Successfully updated scheduler settings for Client " + clientId);
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseClientSchedule(@RequestParam String clientId) {
        // 1. Update the database record status
        repository.findById(clientId).ifPresent(config -> {
            config.setEnabled(false);
            repository.save(config);
        });

        // 2. Kill the thread handle loop execution context
        schedulerService.cancelClientSchedule(clientId);

        return ResponseEntity.ok("Successfully paused/stopped scheduler executions for Client " + clientId);
    }
}
