package com.codesparkle.poker.client;

import ch.aplu.jcardgame.Card;
import com.codesparkle.poker.client.ui.PokerTable;
import com.codesparkle.poker.client.ui.TableWindow;
import com.codesparkle.poker.shared.*;

import java.util.List;


public class Game {

    private PokerTable table = new PokerTable();
    private TableWindow ui = new TableWindow(this, table);
    private MessageBroker messageBroker = new MessageBroker(this);

    private Chips playerChips = Chips.Empty;
    private Chips gamePot = Chips.Empty;
    private Chips toCall = Chips.Empty;
    private Chips bigBlind = Chips.Empty;
    private BettingState bettingState;

    public void prepareGame() {
        ui.displayGameStatus("Offline");
        ui.setVisible(true);
        table.prepareTable();
    }

    public void connectToServer(String address, int port, String name) {
        ui.displayGameStatus("Connecting");
        messageBroker.connectToServer(address, port, name);
        ui.displayGameStatus("Connected");
    }

    public void preflop(List<Card> cards) {
        ui.displayGameStatus(MessageFormat.preflop.toString());
        table.dealHoleCards(cards);
    }

    public void flop(List<Card> cards) {
        ui.displayGameStatus(MessageFormat.flop.toString());
        table.dealFlop(cards);
    }

    public void turn(List<Card> cards) {
        ui.displayGameStatus(MessageFormat.turn.toString());
        table.dealTurn(cards);
    }

    public void river(List<Card> cards) {
        ui.displayGameStatus(MessageFormat.river.toString());
        table.dealRiver(cards);

    }

    public void afterDeal(BettingState bettingState, Chips toCall, int timeout) {
        this.bettingState = bettingState;
        this.toCall = toCall;
        ui.displayGameStatus("Collect Bets");
        ui.requestUserAction(toCall, timeout);

    }

    public void updatePlayerChips(Chips chips) {
        playerChips = chips;
        table.updateChips(chips);
    }

    public void updateGamePot(Chips gamePot) {
        this.gamePot = gamePot;
        table.updatePot(gamePot);
    }

    public void sendAfterDealResponse(PlayerAction action, Chips amount) {
        messageBroker.sendAfterDealResponse(action, amount);
    }

    public Chips getPlayerChips() {
        return playerChips;
    }

    public Chips getGamePot() {
        return gamePot;
    }

    public void displayServerMessage(String messageToShow) {
        ui.displayServerMessage(messageToShow);
    }

    public BettingState getBettingState() {
        return bettingState;
    }

    public Chips getAmountToCall() {
        return toCall;
    }

    public void newRound() {
        //TODO: move this message string out of Game!
        ui.displayGameStatus("New Round");
        table.cleanUp();
    }

    public void setNumberOfActivePlayers(int numberOfActivePlayers) {
        table.updatePlayerNumber(numberOfActivePlayers);
    }

    public boolean canCall() {
        return playerChips.atLeast(toCall) && getBettingState() == BettingState.changed;
    }

    public boolean playerCanRaiseBy(Chips chips) {
        return playerChips.atLeast(chips) && chips.atLeast(2 * toCall.amount()) && chips.atLeast(bigBlind);
    }

    public void requestBlind(Chips blind) {
        //TODO: move this message string out of Game!
        ui.displayServerMessage("Blind with value of " + blind + " was collected from you!");
        sendAfterDealResponse(PlayerAction.blind, blind);
    }

    public void updateBigBlind(Chips height) {
        bigBlind = height;
        table.updateBigBlind(height);
    }

    public void displayWinnerMessage(String messageToShow) {
        String[] messageLines = messageToShow.split(",");
        messageToShow = SharedHelpers.join(messageLines, "\n");
        displayServerMessage(messageToShow);
    }
}
