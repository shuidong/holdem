package com.codesparkle.poker.server;


import com.codesparkle.poker.shared.cards.Card;

public interface Deck {

    public void regenerate();

    public Card takeCard();
}
