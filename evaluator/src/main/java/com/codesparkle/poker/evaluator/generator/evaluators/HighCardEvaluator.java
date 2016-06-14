package com.codesparkle.poker.evaluator.generator.evaluators;


import com.codesparkle.poker.evaluator.generator.CardMultiSet;

public class HighCardEvaluator extends EvaluatorChain {

    public HighCardEvaluator(EvaluatorChain next) {
        super(next);
    }

    @Override
    protected String calculateTopCards(CardMultiSet set) {
        // we now know that the hand is utterly uninteresting. Since the cards
        // are already sorted, we take the first five cards and calculate the equivalence
        // class, although this is slightly awkward. 
        return buildCards(set.getCards(5));
    }

}
