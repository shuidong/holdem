package com.codesparkle.poker.shared.cards;

public class HoleCards {

    public final Card First;
    public final Card Second;

    public HoleCards(Card first, Card second) {
        First = first;
        Second = second;
    }

    @Override
    public int hashCode() {
        int hashFirst = First != null ? First.hashCode() : 0;
        int hashSecond = Second != null ? Second.hashCode() : 0;
        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof HoleCards) {
            HoleCards other = (HoleCards) that;
            return First.equals(other.First) && Second.equals(other.Second);
        }
        return false;
    }

    @Override
    public String toString() {
        return First + " " + Second;
    }
}
