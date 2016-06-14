package com.codesparkle.poker.shared;

public enum PlayerAction {
    check,
    call,
    fold,
    bet,
    raise,
    timeout,
    blind;

    private Chips amount;
    private Player player;

    public void setAmount(Chips amount) {
        this.amount = amount;
    }

    public Chips getAmount() {
        return amount;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
