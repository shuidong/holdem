package com.codesparkle.poker.evaluator.generator.evaluators;


import com.codesparkle.poker.evaluator.generator.CardMultiSet;

public class FourOfAKindEvaluator extends EvaluatorChain {

    public FourOfAKindEvaluator(EvaluatorChain next) {
        super(next);
    }

    @Override
    protected String calculateTopCards(CardMultiSet set) {
        int running = 0, last = -1;
        for (int i = 0; i < set.getCardCount(); i++) {
            if (set.getCard(i) == last) {
                running++;
                if (running == 3) { // found four cards 
                    return findKicker(last, set);
                }
            } else {
                running = 0;
                last = set.getCard(i);
            }
        }
        return null;
    }

    private String findKicker(int four, CardMultiSet set) {
        for (int i = 0; i < set.getCardCount(); i++) {
            if (set.getCard(i) != four) {
                return buildCards(four, four, four, four, set.getCard(i));
            }
        }
        return null;
    }
}
