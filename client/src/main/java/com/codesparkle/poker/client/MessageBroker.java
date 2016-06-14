package com.codesparkle.poker.client;

import ch.aplu.jcardgame.Card;
import com.codesparkle.poker.client.ui.DeckFactory;
import com.codesparkle.poker.shared.*;
import com.codesparkle.poker.shared.cards.Rank;
import com.codesparkle.poker.shared.cards.Suit;
import com.codesparkle.poker.shared.net.GameClient;
import com.codesparkle.poker.shared.net.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class MessageBroker implements MessageListener {

    private Game game;
    private GameClient networkClient;

    public MessageBroker(Game game) {
        super();
        this.game = game;
    }

    public void connectToServer(String address, int port, String name) {
        networkClient = new GameClient(name, address, port);
        networkClient.addMessageListener(this);
        networkClient.connect();
    }

    private <T> void sendToServer(T message) {
        networkClient.sendMessageToServer(message.toString());
    }

    public List<Card> getCards(Message message, MessageFormat key) {
        // TODO: move to client
        List<Card> cards = new ArrayList<>();
        for (String card : message.getMultiple(key)) {
            Rank rank = Rank.fromCode(card.charAt(0));
            Suit suit = Suit.fromCode(card.charAt(1));
            cards.add(DeckFactory.getCard(suit, rank));
        }
        return cards;
    }

    @Override
    public void receivedMessage(String sender, String rawMessage) {
        Message message = Message.parse(rawMessage);
        if (message.has(MessageFormat.round)) {
            List<Card> cards = getCards(message, MessageFormat.cards);
            MessageFormat round = MessageFormat.fromString(message.get(MessageFormat.round));
            switch (round) {
                case preflop:
                game.preflop(cards);
                    break;
                case flop:
                game.flop(cards);
                    break;
                case turn:
                game.turn(cards);
                    break;
                case river:
                game.river(cards);
                    break;
            }
        }

        if (message.has(MessageFormat.afterDeal)) {
            BettingState bettingState = message.getEnum(MessageFormat.afterDeal, BettingState.class);
            int timeout = message.getInt(MessageFormat.timeout);
            Chips minRaise = message.getChips(MessageFormat.minRaise);
            game.afterDeal(bettingState, minRaise, timeout);
        }
        if (message.has(MessageFormat.playerChips)) {
            Chips playerChips = message.getChips(MessageFormat.playerChips);
            game.updatePlayerChips(playerChips);
        }
        if (message.has(MessageFormat.gamePot)) {
            Chips gamePot = message.getChips(MessageFormat.gamePot);
            game.updateGamePot(gamePot);
        }
        if (message.has(MessageFormat.activePlayers)) {
            int numberOfActivePlayers = message.getInt(MessageFormat.activePlayers);
            game.setNumberOfActivePlayers(numberOfActivePlayers);
        }
        if (message.has(MessageFormat.showMessage)) {
            String messageToShow = message.get(MessageFormat.showMessage);
            game.displayServerMessage(messageToShow);
        }
        if (message.has(MessageFormat.displayWinners)) {
            String messageToShow = message.get(MessageFormat.displayWinners);
            game.displayWinnerMessage(messageToShow);
        }
        if (message.has(MessageFormat.newRound)) {
            game.newRound();
        }
        if (message.has(MessageFormat.blind)) {
            Chips blind = message.getChips(MessageFormat.blind);
            game.requestBlind(blind);
        }
        if (message.has(MessageFormat.bigBlind)) {
            Chips blind = message.getChips(MessageFormat.bigBlind);
            game.updateBigBlind(blind);
        }
    }

    public void sendAfterDealResponse(PlayerAction action, Chips amount) {
        Message message = new Message();
        message.add(MessageFormat.afterdealResponse, action);
        message.add(MessageFormat.amount, amount);
        sendToServer(message);
    }
}
