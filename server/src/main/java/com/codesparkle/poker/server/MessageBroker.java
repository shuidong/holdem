package com.codesparkle.poker.server;

import com.codesparkle.poker.server.states.GameState;
import com.codesparkle.poker.shared.*;
import com.codesparkle.poker.shared.cards.Card;
import com.codesparkle.poker.shared.net.ClientListener;
import com.codesparkle.poker.shared.net.ClientManager;

public class MessageBroker implements ClientListener {

    private ClientManager clients;
    private AfterDealResponseListener game;

    public MessageBroker(ClientManager clients, AfterDealResponseListener game) {
        this.clients = clients;
        this.game = game;
        clients.addClientListener(this);
    }

    public void informPlayers(Players players, Chips gamePot) {
        sendPlayersChips(players);
        Message message = new Message();
        message.add(MessageFormat.gamePot, gamePot);
        message.add(MessageFormat.activePlayers, players.getNumberOfUnfoldedPlayers());
        clients.sendMessageToAllClients(message);
    }

    public void sendCardsToAll(Players players, GameState round, Card... cards) {
        for (Player p : players) {
            sendCards(p, round, cards);
        }
    }

    public void sendCards(Player player, GameState round, Card... cards) {
        Message message = new Message();
        message.add(MessageFormat.round, round);
        message.add(MessageFormat.cards, (Object[])cards);
        clients.sendMessageToClient(player, message);
    }

    public void sendAfterDeal(Player player, BettingState bettingState) {
        Message message = new Message();
        message.add(MessageFormat.afterDeal, bettingState);
        message.add(MessageFormat.timeout, bettingState.getTimeout());
        message.add(MessageFormat.minRaise, bettingState.getMinRaise());
        clients.sendMessageToClient(player, message);
    }

    public void requestBlind(Player player, Chips blind) {
        Message message = new Message();
        message.add(MessageFormat.blind, blind);
        clients.sendMessageToClient(player, message);
    }

    public void sendNewRound() {
        Message message = new Message();
        message.add(MessageFormat.newRound, "null"); //TODO find cleaner solution
        clients.sendMessageToAllClients(message);
    }

    private void sendPlayersChips(Players players) {
        for (Player player : players) {
            Message message = new Message();
            message.add(MessageFormat.playerChips, player.getChips());
            clients.sendMessageToClient(player, message);
        }
    }

    @Override
    public void receivedMessage(String sender, String message) {
        Message parsed = Message.parse(message);
        if (parsed.has(MessageFormat.afterdealResponse)) {
            PlayerAction playerAction = parsed.getEnum(MessageFormat.afterdealResponse, PlayerAction.class);
            playerAction.setAmount(parsed.getChips(MessageFormat.amount));
            game.gotAfterDealResponse(sender, playerAction);
        }
    }

    @Override
    public void notifyClientAdded(String clientName) {
        // egal

    }

    @Override
    public void notifyClientRemoved(String clientName) {
        // egal

    }

    public void sendMessageToAllClients(String messageToShow) {
        Message message = new Message();
        message.add(MessageFormat.showMessage, messageToShow);
        clients.sendMessageToAllClients(message);
    }

    public void sendClientMessage(Player player, String messageToShow) {
        Message message = new Message();
        message.add(MessageFormat.showMessage, messageToShow);
        clients.sendMessageToClient(player, message);
    }

    public void sendBlindHeight(Chips bigBlind) {
        Message message = new Message();
        message.add(MessageFormat.bigBlind, bigBlind);
        clients.sendMessageToAllClients(message);
    }

    public void sendWinnerMessage(String winnerMessage) {
        Message message = new Message();
        message.add(MessageFormat.displayWinners, winnerMessage);
        clients.sendMessageToAllClients(message);
    }
}
