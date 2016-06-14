package com.codesparkle.poker.server.states;


import com.codesparkle.poker.server.Players;
import com.codesparkle.poker.shared.*;

public interface StateListener {

    public Players getPlayers();

    public void stateChanged(GameState s);

    public void sendAfterDeal(Player currentPlayer, BettingState bettingState);

    public void afterCollectBets();

    public void requestBlind(Player from, Chips blind);

    public void sendBlindHeight(Chips bigBlind);

    public void playerTimeout(String name);

}
