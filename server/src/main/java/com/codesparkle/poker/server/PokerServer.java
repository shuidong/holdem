package com.codesparkle.poker.server;

import com.codesparkle.poker.shared.Chips;
import com.codesparkle.poker.shared.Player;
import com.codesparkle.poker.shared.net.*;

public class PokerServer implements ClientListener {

    private static GameServer gameServer;
    private int numberOfPlayers;
    private Game game;
    private int port;
    private final Chips startingChips = new Chips(1000);

    public PokerServer(int port, int numberOfPlayers) {
        this.port = port;
        this.numberOfPlayers = numberOfPlayers;
    }

    public void start() {
        gameServer = GameServer.start(port);
        Main.output("server started");
        ClientManager clients = gameServer.getClientManager();
        clients.addClientListener(this);
        game = new Game(clients);
    }

    public void stop() {
        gameServer.stopRunning();
        game.stop();
        Main.output("server stopped");
    }

    @Override
    public void notifyClientAdded(String name) {
        if (game.getPlayers().size() < numberOfPlayers) {
            game.getPlayers().add(new Player(name, startingChips));
            Main.output("%s connected", name);
            if (game.getPlayers().size() == numberOfPlayers)
                game.start();
        }
    }

    @Override
    public void notifyClientRemoved(String name) {
        Main.output("%s disconnected " + name);
    }

    @Override
    public void receivedMessage(String client, String message) {
        Main.output("message from %s: %s", client, message);
    }
}
