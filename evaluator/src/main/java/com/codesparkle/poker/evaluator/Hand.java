package com.codesparkle.poker.evaluator;

public class Hand {

    public final int weakness;
    public final int handCategory;
    public final String cards;
    public final String group;
    public final String description;

    @Override
    public String toString() {
        if (group == null)
            return weakness + " (" + description + ": " + cards + ")";
        return weakness + ": " + cards + " (" + group + ") " + description;
    }

    public Hand(int weakness, String group, String cards, String description) {
        this(weakness, -1, group, cards, description);
    }

    public Hand(int weakness, int groupId, String cards, String description) {
        this(weakness, groupId, null, cards, description);
    }

    private Hand(int weakness, int groupId, String group, String cards, String description) {
        this.weakness = weakness;
        handCategory = groupId;
        this.group = group;
        this.cards = cards;
        this.description = description;
    }
}
