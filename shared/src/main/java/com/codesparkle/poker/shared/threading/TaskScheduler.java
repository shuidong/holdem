package com.codesparkle.poker.shared.threading;

import java.util.concurrent.*;

public class TaskScheduler {

    private ExecutorService executor = Executors.newCachedThreadPool();

    public Future<?> schedule(Runnable task) {
        return executor.submit(task);
    }

}
