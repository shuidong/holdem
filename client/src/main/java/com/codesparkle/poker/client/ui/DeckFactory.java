package com.codesparkle.poker.client.ui;


import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import com.codesparkle.poker.shared.cards.Rank;
import com.codesparkle.poker.shared.cards.Suit;

public class DeckFactory {

    private DeckFactory() {
    }

    public static Deck getDeck() {
        return new Deck(Suit.values(), Rank.values(), "cover");
    }

    public static Card getCard(Suit suit, Rank rank) {
        return new Card(getDeck(), suit, rank);
    }
}
