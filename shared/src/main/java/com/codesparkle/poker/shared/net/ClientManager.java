package com.codesparkle.poker.shared.net;

import com.codesparkle.poker.shared.Player;

import java.util.*;

public class ClientManager implements MessageListener {

    private Map<String, ClientMessenger> clients = new HashMap<>();
    private List<ClientListener> listeners = new ArrayList<>();

    public void addClient(ClientMessenger client) {
        String clientName = client.getClientName();
        clients.put(clientName, client);
        for (ClientListener listener : listeners) {
            listener.notifyClientAdded(clientName);
        }
    }

    public void removeClient(ClientMessenger client) {
        String clientName = client.getClientName();
        clients.remove(clientName);
        for (ClientListener listener : listeners) {
            listener.notifyClientRemoved(clientName);
        }
    }

    public void addClientListener(ClientListener listener) {
        listeners.add(listener);
    }

    public void removeClientListener(ClientListener listener) {
        listeners.remove(listener);
    }

    public <T> void sendMessageToAllClients(T message) {
        for (ClientMessenger client : clients.values()) {
            client.sendMessage(message.toString());
        }
    }

    public <T> void sendMessageToClient(Player client, T message) {
        clients.get(client.getName()).sendMessage(message.toString());
    }

    public int getNumberOfClients() {
        return clients.size();
    }

    @Override
    public void receivedMessage(String sender, String message) {
        for (ClientListener listener : listeners) {
            listener.receivedMessage(sender, message);
        }
    }
}
