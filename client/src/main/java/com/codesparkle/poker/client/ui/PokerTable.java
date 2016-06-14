package com.codesparkle.poker.client.ui;


import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import com.codesparkle.poker.shared.Chips;

import java.awt.*;
import java.util.List;

public class PokerTable extends CardGame {

    public PokerTable() {
        super();
        setCellSize(1);
        setNbHorzCells(600);
        setNbVertCells(600);
        setSimulationPeriod(5);
        setBgColor(new Color(20, 80, 0));
        doRun();
    }

    private Location burnCardsLocation = new Location(100, 100);
    private Location deckLocation = new Location(500, 100);
    private Location flopCardsLocation = new Location(200, 300);
    private Location turnCardsLocation = new Location(400, 300);
    private Location riverCardsLocation = new Location(500, 300);
    private Location playerLocation = new Location(300, 500);
    private Location chipLocation = new Location(20, 560);
    private Location potLocation = new Location(20, 580);
    private Location playerNumberLocation = new Location(20, 540);
    private Location blindLocation = new Location(20, 520);
    private Deck tempDeck = DeckFactory.getDeck();
    private Hand dealerCards = new Hand(tempDeck);
    private Hand flopCards = new Hand(tempDeck);
    private Hand turnCards = new Hand(tempDeck);
    private Hand riverCards = new Hand(tempDeck);
    private Hand burnCards = new Hand(tempDeck);
    private Hand playerCards = new Hand(tempDeck);
    private TextActor playerChips;
    private TextActor gamePot;
    private TextActor playerNumber;
    private TextActor bigBlind;
    Hand[] hands;

    public void prepareTable() {
        dealerCards = tempDeck.dealingOut(0, 0)[0];
        dealerCards.setView(this, new StackLayout(deckLocation));
        dealerCards.setVerso(true);

        burnCards.setView(this, new StackLayout(burnCardsLocation));
        burnCards.setVerso(true);

        RowLayout flopRowLayout = new RowLayout(flopCardsLocation, 300);
        flopRowLayout.setStepDelay(1);
        flopCards.setView(this, flopRowLayout);

        RowLayout turnRowLayout = new RowLayout(turnCardsLocation, 100);
        turnRowLayout.setStepDelay(1);
        turnCards.setView(this, turnRowLayout);

        RowLayout riverRowLayout = new RowLayout(riverCardsLocation, 100);
        riverRowLayout.setStepDelay(1);
        riverCards.setView(this, riverRowLayout);

        RowLayout playerRowLayout = new RowLayout(playerLocation, 150);
        playerRowLayout.setStepDelay(1);
        playerCards.setView(this, playerRowLayout);

        //temporal coupling: has to be called after setStepDelay and setView!
        hands = new Hand[] { dealerCards, flopCards, turnCards, riverCards, burnCards, playerCards };

        for (Hand hand : hands) {
            hand.draw();
        }
    }

    public void dealHoleCards(List<Card> cards) {
        deal(cards, playerCards);
    }

    public void dealFlop(List<Card> cards) {
        dealCommunity(cards, flopCards);
    }

    public void dealTurn(List<Card> cards) {
        dealCommunity(cards, turnCards);
    }

    public void dealRiver(List<Card> cards) {
        dealCommunity(cards, riverCards);
    }

    private void dealCommunity(List<Card> cards, Hand targetHand) {
        dealerCards.transfer(dealerCards.getFirst(), burnCards, true);
        deal(cards, targetHand);
    }

    private void deal(List<Card> cards, Hand targetHand) {
        //this code is suspected to cause an intermittent ArrayIndexOutOfBoundsException.
        for (Card card : cards) {
            dealerCards.transfer(dealerCards.getFirst(), targetHand, false);
        }
        targetHand.removeAll(false);
        for (Card c : cards) {
            targetHand.insert(c, false);
        }
        targetHand.setVerso(false);
        targetHand.draw();
    }

    public void updateChips(Chips chips) {
        if (playerChips != null)
            playerChips.removeSelf();
        playerChips = new TextActor("Your Chips: " + chips);
        addActor(playerChips, chipLocation);
    }

    public void updatePot(Chips chips) {
        if (gamePot != null)
            gamePot.removeSelf();
        gamePot = new TextActor("Game Pot: " + chips);
        addActor(gamePot, potLocation);
    }

    public void cleanUp() {
        for (Hand hand : hands) {
            hand.removeAll(true);
        }
        prepareTable();
    }

    public void updatePlayerNumber(int numberOfActivePlayers) {
        if (playerNumber != null)
            playerNumber.removeSelf();
        playerNumber = new TextActor("Active Players: " + numberOfActivePlayers);
        addActor(playerNumber, playerNumberLocation);

    }

    public void updateBigBlind(Chips height) {
        if (bigBlind != null)
            bigBlind.removeSelf();
        bigBlind = new TextActor("Big blind: " + height);
        addActor(bigBlind, blindLocation);
    }
}
