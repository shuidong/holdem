package com.codesparkle.poker.shared.net;

import com.codesparkle.poker.shared.threading.TaskScheduler;

import java.util.concurrent.Future;

public class GameServer {

    private final ClientManager clientManager = new ClientManager();
    private Future<?> connector;
    private TaskScheduler scheduler = new TaskScheduler();

    public static GameServer start(int port) {
        return new GameServer(port);
    }

    public static GameServer start() {
        return new GameServer(4444);
    }

    private GameServer(int port) {
        connector = scheduler.schedule(new ConnectionManager(port, clientManager));
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void stopRunning() {
        connector.cancel(true);
    }

}
