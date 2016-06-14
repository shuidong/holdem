package com.codesparkle.poker.server;

import com.codesparkle.poker.server.states.StateListener;

public class ServerTimeout implements Runnable {

    private final StateListener listener;
    private final String name;

    public ServerTimeout(StateListener stateListener, String playerName) {
        listener = stateListener;
        name = playerName;
    }

    @Override
    public void run() {
        listener.playerTimeout(name);
    }

}
