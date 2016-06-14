package com.codesparkle.poker.server;


import com.codesparkle.poker.shared.Chips;
import com.codesparkle.poker.shared.Player;

import java.util.Comparator;

public class InversePotSizeComperator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        Chips pot1 = o1.getPot();
        Chips pot2 = o2.getPot();
        if (pot1.equals(pot2))
            return 0;
        if (pot1.atLeast(pot2))
            return 1;
        return -1;
    }
}
