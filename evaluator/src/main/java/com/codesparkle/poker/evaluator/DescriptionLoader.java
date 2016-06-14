package com.codesparkle.poker.evaluator;

import java.io.*;
import java.util.StringTokenizer;

public class DescriptionLoader {

    private static final int numberOfHandStrengths = 7463;
    private static final DescriptionLoader instance = new DescriptionLoader();
    private Hand[] hands;

    private DescriptionLoader() {
        try {
            BufferedReader descriptions = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("/eqcllist"))
            );

            // 1 AKQJT 8 Royal Flush 100.00 0.0032
            String line;
            hands = new Hand[numberOfHandStrengths]; // 1-offset
            int weakness = 1;
            while ((line = descriptions.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, "\t");
                tokens.nextToken();
                String cards = tokens.nextToken();
                int handCategory = Integer.parseInt(tokens.nextToken());
                String description = tokens.nextToken();
                hands[weakness] = new Hand(weakness, handCategory, cards, description);
                weakness++;
            }
            descriptions.close();
        } catch (IOException e) {
            throw new RuntimeException("Loading evaluation lookup table failed.");
        }
    }

    public static DescriptionLoader getInstance() {
        return instance;
    }

    public Hand getHand(int i) {
        return hands[i];
    }
}
