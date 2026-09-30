package com.dynamic.scheduling.dynamic_scheduler.config;

public class ClientEmailJob implements Runnable{
    private final String clientId;
    // You can pass required Spring beans (like EmailService) through this constructor if needed

    public ClientEmailJob(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run() {
        // Wrap in a try-catch so a single client error doesn't break the thread pool engine
        try {
            System.out.println("[EXECUTION] Executing personalized email routine for Client: " + clientId);

            // Put your actual business logic here:
            // 1. Fetch users belonging to 'clientId'
            // 2. Compile their individual emails
            // 3. Dispatch emails

        } catch (Exception e) {
            System.err.println("[ERROR] Failed executing email routine for client " + clientId + ": " + e.getMessage());
        }
    }
}
