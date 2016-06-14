package com.codesparkle.poker.shared.net;

public interface ClientListener extends MessageListener {

    public void notifyClientAdded(String clientName);

    public void notifyClientRemoved(String clientName);
}
