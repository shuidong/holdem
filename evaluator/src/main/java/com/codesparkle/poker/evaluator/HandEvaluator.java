package com.codesparkle.poker.evaluator;

import com.codesparkle.poker.evaluator.generator.Main;
import com.codesparkle.poker.shared.cards.*;

import java.io.*;
import java.nio.file.Paths;

public class HandEvaluator {

    protected DAGNode[] nodes;

    private HandEvaluator(String file) {
        File dagfile = Paths.get(file).toFile();

        try {
            if (!dagfile.exists())
                Main.main(new String[0]);

            nodes = new DAGNode[76154];
            BufferedReader graph = new BufferedReader(new FileReader(dagfile));
            String currentLine;
            int i = 0;
            while ((currentLine = graph.readLine()) != null)
                nodes[i++] = new DAGNode(currentLine);
            graph.close();
            for (DAGNode n : nodes)
                n.wire(nodes, DescriptionLoader.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HandEvaluator() {
        this("build/generated/carddag");
    }

    private Hand calculateHand(Card[] cards) {
        byte[] cardNumbers = new byte[cards.length];
        for (int i = 0; i < cardNumbers.length; i++) {
            int numberOfSuits = Suit.length();
            int rank = cards[i].Rank.getNumber();
            int suitOffset = cards[i].Suit.getNumber();
            cardNumbers[i] = (byte) (rank * numberOfSuits + suitOffset);
        }
        return calculateHand(cardNumbers);
    }

    public Hand calculateHand(HoleCards holeCards, CommunityCards communityCards) {
        Card[] cards = new Card[7];
        cards[0] = holeCards.First;
        cards[1] = holeCards.Second;
        System.arraycopy(communityCards.getFlop().Cards, 0, cards, 2, 3);
        cards[5] = communityCards.getTurn();
        cards[6] = communityCards.getRiver();
        return calculateHand(cards);
    }

    public Hand calculateHand(byte[] hand) {
        DAGNode fn[] = new DAGNode[4];
        DAGNode currentNode = null;
        for (byte currentByte : hand) {
            byte column = (byte) (currentByte & 3);
            int nextByte = currentByte >> 2;
            if (fn[column] == null)
                fn[column] = nodes[nextByte];
            else
                fn[column] = fn[column].next[nextByte];
            if (currentNode == null)
                currentNode = nodes[nextByte];
            else
                currentNode = currentNode.next[nextByte];
        }
        for (DAGNode f : fn) {
            if (f != null && f.flushClass != null)
                return f.flushClass;
        }
        return currentNode.rankClass;
    }
}
