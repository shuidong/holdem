package com.codesparkle.poker.evaluator;

import java.util.StringTokenizer;

public class DAGNode {
    private int[] prenext;
    protected DAGNode[] next;
    private int preflushclass, prerankclass;
    protected Hand flushClass, rankClass;

    public DAGNode(String node) {
        StringTokenizer tokens = new StringTokenizer(node, "\t");
        getNextInteger(tokens);
        prenext = new int[13];
        for (int i = 0; i < 13; i++) {
            prenext[i] = getNextInteger(tokens);
        }
        preflushclass = getNextInteger(tokens);
        prerankclass = getNextInteger(tokens);
        tokens.nextToken();
    }

    private int getNextInteger(StringTokenizer tokens) {
        return Integer.parseInt(tokens.nextToken());
    }

    protected void wire(DAGNode[] nodes, DescriptionLoader classes) {
        next = new DAGNode[13];
        for (int i = 0; i < 13; i++) {
            if (prenext[i] > 0) {
                next[i] = nodes[prenext[i]];
            }
        }
        if (preflushclass > 0) {
            flushClass = classes.getHand(preflushclass);
        }
        if (prerankclass > 0) {
            rankClass = classes.getHand(prerankclass);
        }
    }

}
