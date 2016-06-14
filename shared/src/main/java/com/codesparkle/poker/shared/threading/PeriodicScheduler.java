package com.codesparkle.poker.shared.threading;

import java.util.concurrent.*;

public class PeriodicScheduler {

    private final int tolerance = 2;

    private ScheduledExecutorService scheduledExecutor;

    public static PeriodicScheduler withThreads(int threadsToKeepAlive) {
        return new PeriodicScheduler(threadsToKeepAlive);
    }

    public PeriodicScheduler() {
        this(1);
    }

    public PeriodicScheduler(int threadsToKeepAlive) {
        scheduledExecutor = Executors.newScheduledThreadPool(threadsToKeepAlive);
    }

    public Future<?> scheduleTimeout(Runnable task, long delaySeconds) {
        return scheduledExecutor.schedule(task, delaySeconds + tolerance, TimeUnit.SECONDS);
    }

    public Future<?> scheduleCountdown(Runnable task, long periodSeconds) {
        return scheduledExecutor.scheduleAtFixedRate(task, 0, periodSeconds, TimeUnit.SECONDS);
    }
}
