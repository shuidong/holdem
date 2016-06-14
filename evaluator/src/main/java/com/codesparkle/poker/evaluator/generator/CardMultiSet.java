package com.codesparkle.poker.evaluator.generator;

public class CardMultiSet {

    private int[] content;
    private int cards;
    private CardMultiSet[] succs;
    private int id;
    private CardMultiSet next;
    private int ecnormal, ecflush;
    private int maxvalnormal, maxvalflush;
    private int minvalnormal, minvalflush;

    public int getMaxvalnormal() {
        if (maxvalnormal == 0) {
            maxvalnormal = ecnormal;
            for (CardMultiSet s : succs) {
                if (s != null) {
                    maxvalnormal = Math.max(maxvalnormal, s.getMaxvalnormal());
                }
            }
        }
        return maxvalnormal;
    }

    public int getMaxvalflush() {
        if (maxvalflush == 0) {
            maxvalflush = ecflush;
            for (CardMultiSet s : succs) {
                if (s != null) {
                    maxvalflush = Math.max(maxvalflush, s.getMaxvalflush());
                }
            }
        }
        return maxvalflush;
    }

    public int getMinvalnormal() {
        if (minvalnormal == 0) {
            minvalnormal = ecnormal;
            if (minvalnormal < 0) {
                minvalnormal = Integer.MAX_VALUE;
            }
            for (CardMultiSet s : succs) {
                if (s != null) {
                    minvalnormal = Math.min(minvalnormal, s.getMinvalnormal());
                }
            }
        }
        return minvalnormal;
    }

    public int getMinvalflush() {
        if (minvalflush == 0) {
            minvalflush = ecflush;
            if (minvalflush < 0) {
                minvalflush = Integer.MAX_VALUE;
            }
            for (CardMultiSet s : succs) {
                if (s != null) {
                    if (s.getMinvalflush() >= 0) {
                        minvalflush = Math.min(minvalflush, s.getMinvalflush());
                    }
                }
            }
        }
        if (minvalflush == Integer.MAX_VALUE) {
            minvalflush = -1;
        }
        return minvalflush;
    }

    public int getECFlush() {
        return ecflush;
    }

    public void setECFlush(int ecflush) {
        this.ecflush = ecflush;
    }

    public int getECNormal() {
        return ecnormal;
    }

    public void setECNormal(int ecnormal) {
        this.ecnormal = ecnormal;
    }

    public int getCard(int pos) {
        if (pos >= cards) {
            throw new ArrayIndexOutOfBoundsException("Requested card " + pos + " in a multiset of " + cards);
        }
        return content[pos];
    }

    public int[] getCards(int n) {
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = getCard(i);
        }
        return c;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("[");
        String delim = "";
        for (int i = 0; i < cards; i++) {
            ret.append(delim).append(Constants.RANKS[content[i]]);
            delim = ",";
        }
        return ret.append("]").toString();
    }

    private CardMultiSet(int depth, int cardcnt) {
        content = new int[depth];
        succs = new CardMultiSet[cardcnt];
    }

    public CardMultiSet(int i, int depth, int cardcnt) {
        this(depth, cardcnt);
        content[0] = i;
        cards = 1;
    }

    private CardMultiSet(CardMultiSet set, int j) {
        this(set.content.length, set.succs.length);
        cards = set.cards + 1;
        int ii = 0; // index to insert into
        boolean isset = false;
        for (int i = 0; i < set.cards; i++) { // copy the cards, insert new card
            if (!isset && j > set.content[i]) { // if the new card is the largest
                content[ii++] = j;
                isset = true;
            }
            content[ii++] = set.content[i];
        }
        if (!isset) { // maybe the new card is the smallest, insert last
            content[ii++] = j;
        }
        assert (ii == cards);
    }

    public CardMultiSet duplicateWithNewCard(int j) {
        return new CardMultiSet(this, j);
    }

    public void addLink(int j, CardMultiSet ncms) {
        assert (succs[j] == null);
        succs[j] = ncms;
    }

    @Override
    public boolean equals(Object o) {
        CardMultiSet cms = (CardMultiSet) o;
        if (cms.cards != cards) {
            return false;
        }
        for (int i = 0; i < cards; i++) {
            if (cms.content[i] != content[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = cards;
        for (int i = 0; i < cards; i++) {
            res += (content[i] << (i + 2));
        }
        return res;
    }

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public void setNext(CardMultiSet cms) {
        next = cms;
    }

    public CardMultiSet getNext() {
        return next;
    }

    public int getCardCount() {
        return cards;
    }

    public boolean isRealHand() {
        int last = -1, count = 0;
        for (int i = 0; i < cards; i++) {
            if (content[i] == last) {
                count++;
                if (count > 3) { // found more than four of a kind
                    return false;
                }
            } else {
                last = content[i];
                count = 0;
            }
        }
        return true;
    }

    public CardMultiSet getLink(int i) {
        return succs[i];
    }
}
