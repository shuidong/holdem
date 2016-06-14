package com.codesparkle.poker.shared;

public class Chips {

    public final static Chips Empty = new Chips(0);
    private final int amount;

    public Chips(int amount) {
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

    public Chips plus(Chips n) {
        return plus(n.amount());
    }

    public Chips plus(int n) {
        return new Chips(amount + n);
    }

    public Chips minus(Chips n) {
        return minus(n.amount());
    }

    public Chips minus(int n) {
        return plus(-n);
    }

    public boolean atLeast(Chips chips) {
        return atLeast(chips.amount);
    }

    public boolean atLeast(int chips) {
        return amount >= chips;
    }

    public boolean equals(Chips chips) {
        return amount == chips.amount;
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }
}
