package com.codesparkle.poker.client;


import com.codesparkle.poker.shared.SharedHelpers;

public class Main {

    public static void main(String[] args) {
        SharedHelpers.setLookAndFeel();
        startGame();
    }

    private static void startGame() {
        Game game = new Game();
        game.prepareGame();
    }
}
