package com.codesparkle.poker.shared.cards;

public enum Rank {

    Ace('A', 12),
    King('K', 11),
    Queen('Q', 10),
    Jack('J', 9),
    Ten('T', 8),
    Nine('9', 7),
    Eight('8', 6),
    Seven('7', 5),
    Six('6', 4),
    Five('5', 3),
    Four('4', 2),
    Trey('3', 1),
    Deuce('2', 0);

    private final char rankCode;
    private final int number;

    private Rank(char code, int number) {
        rankCode = code;
        this.number = number;
    }

    public char getCode() {
        return rankCode;
    }

    public static Rank fromCode(char code) {
        for (Rank rank : values()) {
            if (rank.getCode() == code) {
                return rank;
            }
        }
        throw new RuntimeException(code + " invalid");
    }

    public byte getNumber() {
        return (byte) number;
    }
}
