package com.codesparkle.poker.server;

import com.codesparkle.poker.shared.Player;
import com.codesparkle.poker.shared.cards.*;

import java.util.ArrayList;
import java.util.Collection;

public class Dealer {

    private final Deck deck = new PokerDeck();

    public void dealHoleCards(Players players) {
        deck.regenerate();
        for (Player player : players) {
            Card first = deck.takeCard();
            Card second = deck.takeCard();
            player.giveHoleCards(new HoleCards(first, second));
        }
    }

    public FlopCards dealFlop() {
        return new FlopCards(dealCommunityCards(3));
    }

    public Card dealCommunityCard() {
        return dealCommunityCards(1)[0];
    }

    private Card[] dealCommunityCards(int numberOfCards) {
        Collection<Card> communityCards = new ArrayList<>();
        deck.takeCard(); // burn a card
        for (int i = 0; i < numberOfCards; i++) {
            communityCards.add(deck.takeCard());
        }
        return communityCards.toArray(new Card[numberOfCards]);
    }
}
