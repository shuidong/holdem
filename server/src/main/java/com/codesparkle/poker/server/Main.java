package com.codesparkle.poker.server;


import com.codesparkle.poker.shared.SharedHelpers;

import javax.swing.*;

public class Main {

    private static PokerServer pokerServer;
    private static ServerUserInterface ui;

    public static void main(String[] args) {
        SharedHelpers.setLookAndFeel();
        ui = new ServerUserInterface();
        ui.showUI();
        startServer();
    }

    public static void output(final String output) {
        SwingUtilities.invokeLater(() -> {
            if (ui != null)
                ui.output(output);
            else
                System.out.println(output);
        });
    }

    public static void output(String format, Object... args) {
        output(String.format(format, args));
    }

    public static void startServer() {
        ui.clearOutput();
        ui.setButtonStates(true);
        pokerServer = new PokerServer(ui.getPort(), ui.getPlayerNumber());
        pokerServer.start();
    }

    public static void stopServer() {
        pokerServer.stop();
        ui.setButtonStates(false);
    }
}
