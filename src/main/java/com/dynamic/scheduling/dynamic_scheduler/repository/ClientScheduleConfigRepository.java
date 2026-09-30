package com.dynamic.scheduling.dynamic_scheduler.repository;

import com.dynamic.scheduling.dynamic_scheduler.model.ClientScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientScheduleConfigRepository extends JpaRepository<ClientScheduleConfig, String> {
    // Used at system startup to only boot up active client timers
    List<ClientScheduleConfig> findByEnabledTrue();
}
