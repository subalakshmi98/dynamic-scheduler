package com.dynamic.scheduling.dynamic_scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class ClientScheduleConfig {

    @Id
    private String clientId;       // The unique identifier for your client/tenant
    private String cronExpression;  // Custom timeline (e.g., "0 0 12 * * ?" for noon daily)
    private boolean enabled;// Easily pause a client's scheduler without deleting the row


    // Standard Boilerplate Constructors, Getters, and Setters
    public ClientScheduleConfig() {}

    public ClientScheduleConfig(String clientId, String cronExpression, boolean enabled) {
        this.clientId = clientId;
        this.cronExpression = cronExpression;
        this.enabled = enabled;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
