# ContextSchedule: Multi-Tenant Dynamic Scheduler

A production-ready Spring Boot engine built to manage **client-specific, dynamic task scheduling** at runtime. Unlike the standard `@Scheduled` annotation which triggers static intervals for all application data, this project allows you to configure, update, pause, and restore distinct execution schedules (Cron expressions) for individual clients dynamically without restarting the application server.

---

## 🚀 Key Features

*   **Client-Isolated Scheduling:** Every client tracks their own workflow cadence independently (e.g., Client A every 2 minutes, Client B once a day).
*   **Runtime Mutations:** Instantly reschedule or pause active timers on demand via REST APIs.
*   **Database Persistence (H2):** All configuration matrices survive system crashes or manual reboots.
*   **Safe Lifecycle Hydration:** Rebuilds and schedules running tasks dynamically into memory automatically upon server startup.
*   **Optimistic Locking Protection:** Implements transactional native upsert structures to completely prevent Hibernate concurrency errors (`StaleObjectStateException`).

---

## 🛠️ Tech Stack & Dependencies

*   **Java 17+**
*   **Spring Boot 3.x**
*   **Spring Data JPA**
*   **H2 Database** (In-memory engine for seamless local development)

---

## 🏗️ Architecture Blueprint

1.  **`ClientScheduleConfig` (Entity):** Maps the custom tenant configurations directly to persistent storage.
2.  **`ClientSchedulerService` (Engine Core):** Encapsulates Spring's `ThreadPoolTaskScheduler` along with a thread-safe `ConcurrentHashMap` caching active task handlers (`ScheduledFuture<?>`).
3.  **`SchedulerInitializer` (Lifecycle Hook):** Uses an `@EventListener(ApplicationReadyEvent.class)` to query active settings out of the DB and cleanly hydra-hydrate the in-memory engine during the application boot phase.
