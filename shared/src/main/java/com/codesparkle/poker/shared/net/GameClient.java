package com.codesparkle.poker.shared.net;

import java.net.InetSocketAddress;
import java.util.*;

public class GameClient extends CommunicationChannel {

    private final List<MessageListener> messageListeners = new ArrayList<>();
    private InetSocketAddress server;

    public GameClient(String name, String address, int port) {
        server = new InetSocketAddress(address, port);
        endpointName = name;
    }

    public void sendMessageToServer(String message) {
        messageTarget.println(message);
    }

    public void addMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    @Override
    public void receivedMessage(String sender, String message) {
        for (MessageListener listener : messageListeners) {
            listener.receivedMessage(sender, message);
        }
    }

    @Override
    public void connect() {
        createSocket();
        super.establishConnection();
        startListeningToServer();
        sendMessageToServer(endpointName);
    }

    @Override
    protected void cleanup() {
        sendMessageToServer("EXIT");
    }

    private void createSocket() {
        try {
            socket.connect(server);
        } catch (Exception e) {
            throw new NetException(e);
        }
    }

    private void startListeningToServer() {
        messageListener = new MessageReceiverThread();
        messageListener.addListener(this);
        messageListener.start("server", messageSource);
    }
}
