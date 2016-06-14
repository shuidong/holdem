package com.codesparkle.poker.server;


import com.codesparkle.poker.shared.PlayerAction;

public interface AfterDealResponseListener {

    public void gotAfterDealResponse(String sender, PlayerAction playerAction);

}
