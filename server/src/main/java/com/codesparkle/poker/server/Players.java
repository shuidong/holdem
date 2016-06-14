package com.codesparkle.poker.server;

import com.codesparkle.poker.shared.Chips;
import com.codesparkle.poker.shared.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Players implements Iterable<Player> {

    private List<Player> players = new ArrayList<>();

    public void add(Player player) {
        players.add(player);
    }

    public int size() {
        return players.size();
    }

    public Player get(int playerIndex) {
        return players.get(playerIndex);
    }

    public Player get(String playerName) {
        for (Player player : players) {
            if (playerName.equals(player.getName())) {
                return player;
            }
        }
        throw new RuntimeException("No such player!");
    }

    public Chips getLargestPot() {
        return new Chips(allPots().max().orElse(0));
    }

    public Chips getSumOfPots() {
        return new Chips(allPots().sum());
    }

    public Chips collectPots() {
        Chips chips = Chips.Empty;
        for (Player player : players) {
            chips = chips.plus(player.collectPot());
        }
        return chips;
    }

    private IntStream allPots() {
        return players.stream().mapToInt(player -> player.getPot().amount());
    }

    public int getNumberOfUnfoldedPlayers() {
        return (int)players.stream().filter(x -> !x.hasFolded()).count();
    }

    public boolean everyoneElseFolded() {
        return getNumberOfUnfoldedPlayers() < 2;
    }

    public boolean everyoneElseFoldedOrIsAllIn() {
        return getNumerOfActivePlayers() < 2;
    }

    public int getNumerOfActivePlayers() {
        return (int)players.stream().filter(Player::isActive).count();
    }

    public Player byNumber(int number) {
        return get(number % size());
    }

    public List<Player> getSortedAllInPlayersFromLastRound() {
        List<Player> allIn = players.stream().filter(Player::isAllInInLastRound).collect(Collectors.toList());
        Collections.sort(allIn, new InversePotSizeComperator());
        return allIn;
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    public void cleanup() {
        players.forEach(Player::reset);
    }
}
