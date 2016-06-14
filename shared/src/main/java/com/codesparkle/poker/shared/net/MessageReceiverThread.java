package com.codesparkle.poker.shared.net;

import com.codesparkle.poker.shared.threading.TaskScheduler;

import java.io.BufferedReader;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MessageReceiverThread implements Closeable, MessageListener {

    private TaskScheduler scheduler = new TaskScheduler();
    private final List<MessageListener> listeners = new ArrayList<>();
    private Future<?> receiver;

    public void start(String sender, BufferedReader messageSource) {
        receiver = scheduler.schedule(new MessageReceiver(this, messageSource, sender));
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void close() {
        receiver.cancel(true);
    }

    @Override
    public void receivedMessage(String sender, String message) {
        for (MessageListener listener : listeners) {
            listener.receivedMessage(sender, message);
        }
    }
}
