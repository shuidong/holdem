package com.codesparkle.poker.evaluator.generator.evaluators;


import com.codesparkle.poker.evaluator.generator.CardMultiSet;
import com.codesparkle.poker.evaluator.generator.Constants;

public abstract class EvaluatorChain {

    private EvaluatorChain next;

    public EvaluatorChain(EvaluatorChain next) {
        this.next = next;
    }

    public String getTopCards(CardMultiSet set) {
        String topCards = calculateTopCards(set);
        if (topCards != null)
            return topCards;
        if (next != null)
            return next.getTopCards(set);
        throw new RuntimeException("Cannot find equivalence class for " + set);
    }

    protected abstract String calculateTopCards(CardMultiSet set);

    protected String buildCards(int... cards) {
        return String.format("%s %s %s %s %s", r(cards[0]), r(cards[1]), r(cards[2]), r(cards[3]), r(cards[4]));
    }

    private String r(int card) {
        return Constants.RANKS[card];
    }

}
