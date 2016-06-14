package com.codesparkle.poker.shared.cards;

public class Card {

    public final Suit Suit;
    public final Rank Rank;

    public Card(Suit suit, Rank rank) {
        Suit = suit;
        Rank = rank;
    }

    @Override
    public String toString() {
        return String.format("%s%s", Rank.getCode(), Suit.getCode());
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Card) {
            Card other = (Card) that;
            return Suit == other.Suit && Rank == other.Rank;
        }
        return false;
    }
}
