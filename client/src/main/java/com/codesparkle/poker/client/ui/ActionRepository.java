package com.codesparkle.poker.client.ui;

import com.codesparkle.poker.client.Game;
import com.codesparkle.poker.shared.*;
import com.codesparkle.poker.shared.threading.PeriodicScheduler;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
// TODO: actionChosen(), canBetOrRaise() and the timer should be separated from 
// ActionRepository

public class ActionRepository implements CountdownListener {

    private PeriodicScheduler countdowns = PeriodicScheduler.withThreads(1);
    private Future<?> currentCountdown;
    private TableWindow ui;
    private Game game;

    public void startTimer(int timeout) {
        currentCountdown = countdowns.scheduleCountdown(new ClientCountdown(timeout, this), 1);
    }

    public ActionRepository(TableWindow userInterface, Game game) {
        ui = userInterface;
        this.game = game;
    }

    public Action getAction(Command key) {
        return actions.get(key);
    }

    public KeyListener getKeyAction(Command key) {
        return keyActions.get(key);
    }

    public void actionChosen(PlayerAction action, Chips amount) {
        currentCountdown.cancel(false);
        game.sendAfterDealResponse(action, amount);
        ui.setButtonsEnabled(false);
        ui.showPanel(ControlPanel.Empty);
        ui.updateDelay("");
    }

    public void actionChosen(PlayerAction action) {
        actionChosen(action, Chips.Empty);
    }

    public boolean canBetOrRaise() {
        return ui.isAmountValid() && game.playerCanRaiseBy(ui.getAmount());
    }

    private Map<Command, Action> actions = new HashMap<Command, Action>() {

        {
            put(Command.connectToServer, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String user = ui.getUsername();
                    game.connectToServer(ui.getAddress(), ui.getPort(), user);
                    ui.showPanel(ControlPanel.Empty);
                    ui.setTitle(ui.getTitle() + ": " + user);
                }
            });

            put(Command.fold, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionChosen(PlayerAction.fold);
                }
            });

            put(Command.raise, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionChosen(PlayerAction.raise, ui.getAmount());
                }
            });

            put(Command.bet, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionChosen(PlayerAction.bet, ui.getAmount());
                }
            });

            put(Command.check, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionChosen(PlayerAction.check);
                }
            });

            put(Command.call, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionChosen(PlayerAction.call, game.getAmountToCall());
                }
            });
        }
    };

    private Map<Command, KeyListener> keyActions = new HashMap<Command, KeyListener>() {

        {
            put(Command.validateAmount, new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    ui.updateButtonStates();
                }
            });
        }
    };

    public boolean isBettingStateUnchanged() {
        return game.getBettingState() == BettingState.unchanged;
    }

    public boolean canCall() {
        return game.canCall();
    }

    @Override
    public void updateDelay(int delay) {
        ui.updateDelay(Integer.toString(delay));
    }

    @Override
    public void timeoutExpired() {
        actionChosen(PlayerAction.timeout);
    }

}
