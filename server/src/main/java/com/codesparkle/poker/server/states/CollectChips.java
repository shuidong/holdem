package com.codesparkle.poker.server.states;

import com.codesparkle.poker.server.ServerTimeout;
import com.codesparkle.poker.shared.*;
import com.codesparkle.poker.shared.threading.PeriodicScheduler;

import java.util.concurrent.Future;

public abstract class CollectChips extends GameState {

    protected int currentPlayerNumber;
    protected Player currentPlayer;
    private PeriodicScheduler timeouts = new PeriodicScheduler();
    private Future<?> currentTimeout;

    protected abstract void handleResponse(Player sender, PlayerAction action);

    protected abstract int getTimeout();

    protected abstract boolean shouldRepeat();

    public CollectChips(StateListener listener, int playerToActNumber) {
        super(listener);
        updateCurrentPlayer(playerToActNumber);
    }

    @Override
    public void execute() {
        doExecute();
        currentTimeout = timeouts.scheduleTimeout(new ServerTimeout(listener, currentPlayer.getName()), getTimeout());
    }

    @Override
    protected void executeNextState() {
        nextPlayer();
        if (shouldRepeat())
            doExecute();
        else {
            beforeTransition();
            super.executeNextState();
        }
    }

    public void gotResponse(PlayerAction action) {
        Player sender = action.getPlayer();
        if (theWrongPlayerAnswered(sender))
            return;
        currentTimeout.cancel(false);
        handleResponse(sender, action);
        if (sender.getChips().equals(Chips.Empty)) {
            sender.goAllIn();
        }
        executeNextState();
    }

    protected void beforeTransition() {
    }

    private boolean theWrongPlayerAnswered(Player player) {
        return player != currentPlayer;
    }

    private void nextPlayer() {
        updateCurrentPlayer(currentPlayerNumber + 1);
    }

    private void updateCurrentPlayer(int playerNumber) {
        currentPlayerNumber = playerNumber;
        currentPlayer = listener.getPlayers().byNumber(currentPlayerNumber);
    }
}
