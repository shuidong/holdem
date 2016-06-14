package com.codesparkle.poker.client.ui;

public interface CountdownListener {

    public void updateDelay(int remaining);

    public void timeoutExpired();
}
