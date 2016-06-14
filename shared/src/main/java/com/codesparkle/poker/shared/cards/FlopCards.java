package com.codesparkle.poker.shared.cards;

public class FlopCards {

    public final Card[] Cards = new Card[3];

    public FlopCards(Card... cards) {
        System.arraycopy(cards, 0, Cards, 0, 3);
    }
}
