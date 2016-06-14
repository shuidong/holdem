package com.codesparkle.poker.client.ui;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientCountdown implements Runnable {

    private AtomicInteger remaining;
    private CountdownListener listener;

    public ClientCountdown(int numberOfExecutions, CountdownListener countdownListener) {
        remaining = new AtomicInteger(numberOfExecutions);
        listener = countdownListener;
    }

    @Override
    public void run() {
        int current = remaining.decrementAndGet();
        if (current > 0) {
            listener.updateDelay(current);
        } else {
            listener.timeoutExpired();
        }
    }
}
