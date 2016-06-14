package com.codesparkle.poker.shared.net;


import com.codesparkle.poker.shared.SharedHelpers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ConnectionManager implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private ClientManager clientManager;

    public ConnectionManager(int port, ClientManager clientManager) {
        this.port = port;
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        openSocket(port);
        while (!currentThread.isInterrupted()) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (Exception e) {
                continue;
            }
            ClientMessenger messenger = new ClientMessenger(socket, clientManager);
            messenger.connect();
        }
        closeSocket();
    }

    private void openSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(50);
        } catch (IOException e) {
            throw new NetException("Could not listen on port: " + port, e);
        }
    }

    private void closeSocket() {
        SharedHelpers.close(serverSocket);
    }
}
