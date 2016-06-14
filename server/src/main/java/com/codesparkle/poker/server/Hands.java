package com.codesparkle.poker.server;

import com.codesparkle.poker.evaluator.Hand;
import com.codesparkle.poker.evaluator.HandEvaluator;
import com.codesparkle.poker.shared.Player;
import com.codesparkle.poker.shared.cards.CommunityCards;

import java.util.*;

public class Hands {

    private static HandEvaluator evaluator = new HandEvaluator();

    private Map<Player, Hand> hands = new HashMap<>();

    private Hands() {
    }

    public static Hands evaluate(Players players, CommunityCards communityCards) {
        Hands evaluated = new Hands();
        for (Player player : players) {
            if (player.hasFolded())
                continue;
            Hand result = evaluator.calculateHand(player.getHoleCards(), communityCards);
            evaluated.add(player, result);
            Main.output("evaluated: %s %s %s", player.getName(), result.weakness, result.description);
        }
        return evaluated;
    }

    private void add(Player player, Hand result) {
        hands.put(player, result);
    }

    public Map<Player, Hand> getWinners() {
        Map<Player, Hand> winners = new HashMap<>();
        int lowestWeakness = getLowestWeakness();
        for (Player player : hands.keySet()) {
            Hand hand = hands.get(player);
            if (hand.weakness == lowestWeakness)
                winners.put(player, hand);
        }
        return winners;
    }

    private int getLowestWeakness() {
        return Collections.min(getWeaknesses());
    }

    private List<Integer> getWeaknesses() {
        List<Integer> weaknesses = new ArrayList<>();
        for (Hand h : hands.values()) {
            weaknesses.add(h.weakness);
        }
        return weaknesses;
    }
}
