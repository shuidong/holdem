package com.codesparkle.poker.evaluator.generator;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        LookupTableSaver eval = HandEvalGenerator.buildCardSetDAG(7, 13);
        eval.saveGraphAs("build/generated/carddag");
    }
}
