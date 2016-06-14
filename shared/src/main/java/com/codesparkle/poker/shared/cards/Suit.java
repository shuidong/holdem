package com.codesparkle.poker.shared.cards;

public enum Suit {

    Spades('s', 1),
    Hearts('h', 0),
    Diamonds('d', 3),
    Clubs('c', 2);

    private final char suitCode;
    private final int number;

    private Suit(char code, int number) {
        suitCode = code;
        this.number = number;
    }

    public char getCode() {
        return suitCode;
    }

    public static Suit fromCode(char code) {
        for (Suit suit : values()) {
            if (suit.getCode() == code) {
                return suit;
            }
        }
        throw new RuntimeException(code + " invalid");
    }

    public byte getNumber() {
        return (byte) number;
    }

    public static int length() {
        return values().length;
    }
}
