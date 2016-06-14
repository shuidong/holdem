package com.codesparkle.poker.server.states;

import com.codesparkle.poker.shared.*;

public class CollectBets extends CollectChips {

    private final int timeout = 30;
    private Player lastRaiser;

    public CollectBets(StateListener listener, int currentPlayer) {
        super(listener, currentPlayer);
    }

    @Override
    public void doExecute() {
        if ((allOthersAreInactive() && !someoneNeedsToCallToAllIn()) || !currentPlayer.isActive()) {
            executeNextState();
            return;
        }
        determineLastRaiser();
        BettingState bettingState = BettingState.from(getAmountToCall(), timeout);
        listener.sendAfterDeal(currentPlayer, bettingState);
    }

    private void determineLastRaiser() {
        if (lastRaiser == null)
            lastRaiser = currentPlayer;
    }

    private Chips getAmountToCall() {
        Chips highestPot = listener.getPlayers().getLargestPot();
        Chips amountToCall = highestPot.minus(currentPlayer.getPot());
        if (!currentPlayer.getChips().atLeast(amountToCall))
            return currentPlayer.getChips();
        return amountToCall;
    }

    @Override
    protected void handleResponse(Player sender, PlayerAction action) {
        switch (action) {
            case bet:
            case raise:
            lastRaiser = sender;
            case call:
            sender.bet(action.getAmount());
                break;
            case timeout:
            case fold:
            sender.fold();
                break;
        }
    }

    @Override
    protected int getTimeout() {
        return timeout;
    }

    @Override
    protected boolean shouldRepeat() {
        return lastRaiser != currentPlayer && (!allOthersAreInactive() || someoneNeedsToCallToAllIn());
    }

    private boolean someoneNeedsToCallToAllIn() {
        Chips smallestActivePlayerPotChips = new Chips(Integer.MAX_VALUE);
        Chips highestAllInPlayerPotChips = Chips.Empty;

        for (Player p : listener.getPlayers()) {
            if (!p.hasFolded() && !p.isAllIn()) {
                if (p.getPot().amount() < smallestActivePlayerPotChips.amount()) {
                    smallestActivePlayerPotChips = p.getPot();
                }
            }
        }

        for (Player p : listener.getPlayers()) {
            if (p.isAllIn()) {
                if (p.getPot().amount() > highestAllInPlayerPotChips.amount()) {
                    highestAllInPlayerPotChips = p.getPot();
                }
            }
        }
        return !smallestActivePlayerPotChips.atLeast(highestAllInPlayerPotChips);

    }

    @Override
    protected void beforeTransition() {
        listener.afterCollectBets();
    }

    private boolean allOthersAreInactive() {
        return listener.getPlayers().everyoneElseFoldedOrIsAllIn();
    }
}
