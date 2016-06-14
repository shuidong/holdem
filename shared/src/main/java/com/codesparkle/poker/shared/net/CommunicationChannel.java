package com.codesparkle.poker.shared.net;

import com.codesparkle.poker.shared.SharedHelpers;

import java.io.*;
import java.net.Socket;

public abstract class CommunicationChannel implements MessageListener {
    protected String endpointName;
    protected Socket socket = new Socket();
    protected PrintWriter messageTarget;
    protected BufferedReader messageSource;
    protected MessageReceiverThread messageListener;
    protected boolean isConnected = false;

    protected abstract void cleanup();

    public abstract void connect();

    public void disconnect() {
        isConnected = false;
        cleanup();
        SharedHelpers.close(messageListener, messageTarget, messageSource, SharedHelpers.closeableFrom(socket));
    }

    public boolean isConnected() {
        return isConnected;
    }

    protected void establishConnection() {
        try {
            messageTarget = new PrintWriter(socket.getOutputStream(), true);
            messageSource = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
        } catch (IOException e) {
            throw new NetException(e);
        }
    }

}
