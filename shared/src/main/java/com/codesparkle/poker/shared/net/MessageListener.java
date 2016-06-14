package com.codesparkle.poker.shared.net;

public interface MessageListener {
    public void receivedMessage(String sender, String message);
}
