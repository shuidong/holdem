package com.codesparkle.poker.shared.net;

import java.io.*;

final class MessageReceiver implements Runnable {

    private MessageListener listener;
    private BufferedReader source;
    private String sender;

    MessageReceiver(MessageListener messageListener, BufferedReader messageSource, String messageSender) {
        listener = messageListener;
        source = messageSource;
        sender = messageSender;
    }

    @Override
    public void run() {
        try {
            passMessagesOnToListeners();
        } catch (IOException e) {
            // e.printStackTrace();
            // SocketException occurs every time a client disconnects.
            // FIXME: find the cause of this exception and fix it.
            // throw new NetException(e);
        }
    }

    private void passMessagesOnToListeners() throws IOException {
        String message;
        Thread currentThread = Thread.currentThread();
        while (!currentThread.isInterrupted() && (message = source.readLine()) != null) {
            listener.receivedMessage(sender, message);
        }
    }
}
