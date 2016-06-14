package com.codesparkle.poker.server;

import com.codesparkle.poker.evaluator.Hand;
import com.codesparkle.poker.shared.Chips;
import com.codesparkle.poker.shared.Player;
import com.codesparkle.poker.shared.cards.CommunityCards;

import java.util.*;

public class Pot {

    private Players possiblePlayers = new Players();
    private Chips chips = Chips.Empty;

    public Pot(Players players) {
        for (Player p : players) {
            if (!p.hasFolded() && (!p.isAllIn() || p.isAllInInLastRound())) {
                possiblePlayers.add(p);
            }
        }
    }

    public void add(Chips n) {
        chips = chips.plus(n);
    }

    public Chips getChips() {
        return chips;
    }

    public void subtract(Chips n) {
        chips = chips.minus(n);
    }

    public String getWinnersString(CommunityCards communityCards) {
        if (chips.equals(Chips.Empty))
            return "";
        Map<Player, Hand> winners = evaluateWinners(communityCards);
        Map<Player, Chips> winnings = divideWinningsBetween(winners.keySet());
        return getWinnerMessage(winners, winnings);
    }

    private String getWinnerMessage(Map<Player, Hand> winnerHands, Map<Player, Chips> playerWinnings) {
        String message = "";
        for (Player winner : winnerHands.keySet()) {
            Chips winnings = playerWinnings.get(winner);
            if (possiblePlayers.everyoneElseFolded())
                message += String.format("%s won %s because everyone else folded.", winner, winnings);
            else
                message += String.format("%s won %s with %s.", winner, winnings, winnerHands.get(winner).description);
            message += ",";
        }
        return message;
    }

    private Map<Player, Hand> evaluateWinners(CommunityCards communityCards) {
        Hands hands = Hands.evaluate(possiblePlayers, communityCards);
        return hands.getWinners();
    }

    private Map<Player, Chips> divideWinningsBetween(Collection<Player> winners) {
        Map<Player, Chips> playerWinnings = new HashMap<>();

        int winningsPerPlayer = getChips().amount() / winners.size();
        int remaining = getChips().amount() % winners.size();
        for (Player winner : winners) {
            int winnings = winningsPerPlayer;
            if (remaining > 0) {
                winnings++;
                remaining--;
            }
            Chips winningsChips = new Chips(winnings);
            winner.addChips(winningsChips);
            subtract(winningsChips);

            playerWinnings.put(winner, winningsChips);

        }
        return playerWinnings;
    }
}
