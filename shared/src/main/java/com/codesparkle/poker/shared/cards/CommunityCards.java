package com.codesparkle.poker.shared.cards;

public class CommunityCards {

    private FlopCards flop;
    private Card turn;
    private Card river;

    public void setFlop(FlopCards cards) {
        flop = cards;
    }

    public void setTurn(Card card) {
        turn = card;
    }

    public void setRiver(Card card) {
        river = card;
    }

    public FlopCards getFlop() {
        return flop;
    }

    public Card getTurn() {
        return turn;
    }

    public Card getRiver() {
        return river;
    }

    public void clear() {
        flop = null;
        turn = null;
        river = null;
    }

}
