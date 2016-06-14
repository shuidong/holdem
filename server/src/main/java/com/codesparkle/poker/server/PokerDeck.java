package com.codesparkle.poker.server;


import com.codesparkle.poker.shared.cards.*;

import java.security.SecureRandom;
import java.util.*;

class PokerDeck implements Deck {

    private final List<Card> allCards = new ArrayList<>(52);
    private final Stack<Card> deck = new Stack<>();
    private final SecureRandom randomizer = new SecureRandom();

    public PokerDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                allCards.add(new Card(suit, rank));
            }
        }
        regenerate();
    }

    @Override
    public Card takeCard() {
        return deck.pop();
    }

    @Override
    public void regenerate() {
        deck.clear();
        for (Card card : allCards) {
            deck.push(card);
        }
        Collections.shuffle(deck, randomizer);
    }
}
