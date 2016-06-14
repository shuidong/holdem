package com.codesparkle.poker.server.states;

import com.codesparkle.poker.server.Main;
import com.codesparkle.poker.shared.*;

public class CollectBlinds extends CollectChips {

    private Chips smallBlind = new Chips(5);
    private Chips bigBlind = new Chips(10);
    private int smallBlindPlayerNumber;

    public CollectBlinds(StateListener listener, int currentPlayer) {
        super(listener, currentPlayer);
        smallBlindPlayerNumber = currentPlayer;
    }

    @Override
    protected void doExecute() {
        listener.sendBlindHeight(bigBlind);
        Chips currentBlind = getBlind();
        if (currentPlayer.getChips().amount() >= currentBlind.amount()) {
            listener.requestBlind(currentPlayer, currentBlind);
        } else {
            playerIsNotAbleToPayBlind(currentPlayer);
            executeNextState();
        }
    }

    private Chips getBlind() {
        return currentPlayerNumber - smallBlindPlayerNumber == 0 ? smallBlind : bigBlind;
    }

    @Override
    protected void handleResponse(Player sender, PlayerAction action) {
        if (action == PlayerAction.blind)
            sender.bet(action.getAmount());
        else {
            playerIsNotAbleToPayBlind(sender);
        }
    }

    private void playerIsNotAbleToPayBlind(Player player) {
        Main.output(player.getName() + " is not able to pay blind");
        smallBlindPlayerNumber++;
        player.fold();
    }

    @Override
    protected int getTimeout() {
        return 0;
    }

    @Override
    protected boolean shouldRepeat() {
        return currentPlayerNumber - smallBlindPlayerNumber < 2;
    }
}
