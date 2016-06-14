package com.codesparkle.poker.shared;


import com.codesparkle.poker.shared.cards.HoleCards;

public class Player {

    private final String name;
    private HoleCards cards;
    private Chips chips = Chips.Empty;
    private Chips pot = Chips.Empty;

    private boolean folded;
    private boolean allIn;

    public Player(String name, Chips chips) {
        this.name = name;
        this.chips = chips;
    }

    public Chips getChips() {
        return chips;
    }

    public void addChips(Chips numberOfChips) {
        if (numberOfChips.amount() > 0) {
            chips = chips.plus(numberOfChips);
        }
    }

    public void removeChips(Chips chipsToRemove) {
        if (chipsToRemove.amount() <= chips.amount()) {
            chips = chips.minus(chipsToRemove);
        }
    }

    public void giveHoleCards(HoleCards holeCards) {
        cards = holeCards;
    }

    public HoleCards getHoleCards() {
        return cards;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void fold() {
        folded = true;
    }

    public boolean hasFolded() {
        return folded;
    }

    public void goAllIn() {
        allIn = true;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public boolean isAllInInLastRound() {
        return allIn && !pot.equals(Chips.Empty);
    }

    public boolean isActive() {
        return !allIn && !folded;
    }

    public Chips getPot() {
        return pot;
    }

    public void bet(Chips numberOfChips) {
        removeChips(numberOfChips);
        pot = pot.plus(numberOfChips);
    }

    public Chips collectPot() {
        Chips oldPot = pot;
        pot = Chips.Empty;
        return oldPot;
    }

    public Chips collectPot(Chips n) {
        pot = pot.minus(n);
        return n;
    }

    public void clearHoleCards() {
        cards = null;
    }

    public void reset() {
        folded = false;
        allIn = false;
        clearHoleCards();
    }
}
