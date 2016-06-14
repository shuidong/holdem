package com.codesparkle.poker.shared.net;

import java.io.IOException;
import java.net.Socket;

public class ClientMessenger extends CommunicationChannel {

    private final ClientManager clientManager;

    public ClientMessenger(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
    }

    public String getClientName() {
        return endpointName;
    }

    public void sendMessage(String message) {
        messageTarget.println(message);
    }

    @Override
    public void connect() {
        super.establishConnection();
        determineNameFromFirstMessage();
        clientManager.addClient(this);
        messageListener = new MessageReceiverThread();
        messageListener.addListener(clientManager);
        messageListener.addListener(this);
        messageListener.start(endpointName, messageSource);
    }

    @Override
    public void receivedMessage(String sender, String message) {
        if (message.equals("EXIT")) {
            disconnect(); // should disconnect really be handled here?
        }
    }

    @Override
    protected void cleanup() {
        clientManager.removeClient(this);
    }

    private void determineNameFromFirstMessage() {
        try {
            endpointName = messageSource.readLine();
        } catch (IOException e) {
            throw new NetException(e);
        }
    }
}
