package com.codesparkle.poker.server;

import com.codesparkle.poker.server.states.*;
import com.codesparkle.poker.shared.*;
import com.codesparkle.poker.shared.cards.*;
import com.codesparkle.poker.shared.net.ClientManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Game implements StateListener {

    private static final long timeBetweenGames = TimeUnit.SECONDS.toMillis(10);
    private final MessageBroker messageBroker;
    private final List<GameState> states = new ArrayList<>();
    private final List<Pot> pots = new ArrayList<>();
    private final Dealer cardDealer = new Dealer();
    private final CommunityCards communityCards = new CommunityCards();
    private final Players players = new Players();

    private GameState currentState;
    private boolean hasStopped = false;
    private int firstPlayerToAct = 0;
    private Pot currentPot;


    public Game(ClientManager clients) {
        messageBroker = new MessageBroker(clients, this::gotAfterDealResponse);
    }

    public void stop() {
        hasStopped = true;
    }

    public void start() {
        if (hasStopped)
            return;
        addNewPot();
        startStateMachine();
    }

    public void addNewPot() {
        if (players.getNumerOfActivePlayers() == 0)
            return;
        currentPot = new Pot(players);
        pots.add(currentPot);
        Main.output("created new pot");
    }

    private void startStateMachine() {
        generateStates();
        addCollectChipStates();
        currentState = states.get(0);
        currentState.setNextState(states.get(1));
        currentState.execute();
    }

    private void generateStates() {
        states.add(new CollectBlinds(this, firstPlayerToAct));
        states.add(new Round(this, this::preflop, "preflop"));
        states.add(new Round(this, this::flop, "flop"));
        states.add(new Round(this, this::turn, "turn"));
        states.add(new Round(this, this::river, "river"));
        states.add(new Round(this, this::showdown, "showdown"));
        
    }

    private void addCollectChipStates() {
        for (int i = 1; i < states.size() - 1; i++) {
            int playerNumber = (i != 1) ? firstPlayerToAct : firstPlayerToAct + 2;
            GameState collectBets = new CollectBets(this, playerNumber);
            states.get(i).setNextState(collectBets);
            collectBets.setNextState(states.get(i + 1));
        }
    }

    public void preflop() {
        cardDealer.dealHoleCards(players);
        for (Player player : players) {
            HoleCards holeCards = player.getHoleCards();
            messageBroker.sendCards(player, currentState, holeCards.First, holeCards.Second);
        }
    }

    public void flop() {
        FlopCards flop = cardDealer.dealFlop();
        communityCards.setFlop(flop);
        messageBroker.sendCardsToAll(players, currentState, flop.Cards);
    }

    public void turn() {
        Card turn = cardDealer.dealCommunityCard();
        communityCards.setTurn(turn);
        messageBroker.sendCardsToAll(players, currentState, turn);
    }

    public void river() {
        Card river = cardDealer.dealCommunityCard();
        communityCards.setRiver(river);
        messageBroker.sendCardsToAll(players, currentState, river);
    }

    @Override
    public void stateChanged(GameState newState) {
        currentState = newState;
        Main.output("round: %s", newState);
    }

    @Override
    public void playerTimeout(String player) {
        gotAfterDealResponse(player, PlayerAction.timeout);
    }
    
    public void gotAfterDealResponse(String sender, PlayerAction playerAction) {
        Player player = players.get(sender);
        if (hasStopped || player.hasFolded())
            return;
        playerAction.setPlayer(player);
        CollectChips currentCollectChips = (CollectChips) currentState;
        currentCollectChips.gotResponse(playerAction);
    }

    @Override
    public void sendAfterDeal(Player currentPlayer, BettingState bettingState) {
        informPlayers();
        messageBroker.sendAfterDeal(currentPlayer, bettingState);
    }

    @Override
    public void requestBlind(Player currentPlayer, Chips blind) {
        informPlayers();
        messageBroker.requestBlind(currentPlayer, blind);
    }

    public void informPlayers() {
        Chips gamePot = getGamePot();
        messageBroker.informPlayers(players, gamePot);
    }

    private Chips getGamePot() {
        Chips gamePot = Chips.Empty;
        for (Pot p : pots) {
            gamePot = gamePot.plus(p.getChips());
        }
        gamePot = gamePot.plus(players.getSumOfPots());
        return gamePot;
    }

    @Override
    public void afterCollectBets() {
        List<Player> allInPlayers = players.getSortedAllInPlayersFromLastRound();
        for (Player allInPlayer : allInPlayers) {
            Chips sidePotHeight = allInPlayer.getPot();
            for (Player player : players) {
                if (!player.hasFolded() && player.getPot().atLeast(1)) {
                    currentPot.add(player.collectPot(sidePotHeight));
                }
            }
            addNewPot();
        }
        currentPot.add(players.collectPots());
        informPlayers();
    }

    public void showdown() {
        findAndInformWinners();
        cleanup();
        start();
    }

    private void findAndInformWinners() {
        String winnerMessage = "";
        for (Pot pot : pots) {
            winnerMessage += pot.getWinnersString(communityCards);
        }
        messageBroker.sendWinnerMessage(winnerMessage);
        informPlayers();
    }

    private void cleanup() {
        players.cleanup();
        communityCards.clear();
        pots.clear();
        SharedHelpers.sleep(timeBetweenGames);
        messageBroker.sendNewRound();
        firstPlayerToAct++;
    }

    @Override
    public void sendBlindHeight(Chips bigBlind) {
        messageBroker.sendBlindHeight(bigBlind);
    }

    @Override
    public Players getPlayers() {
        return players;
    }
}
