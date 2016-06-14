package com.codesparkle.poker.shared;

public enum MessageFormat {
    round("round"),
    cards("com/codesparkle/poker/shared/cards"),
    collectBets("collectBets"),
    afterDeal("afterDeal"),
    afterdealResponse("afterdealResponse"),
    timeout("timeout"),
    minRaise("minRaise"),
    gamePot("gamePot"),
    playerChips("playerChips"),
    amount("amount"),
    preflop("preflop"),
    flop("flop"),
    turn("turn"),
    river("river"),
    showMessage("showmessage"),
    newRound("newround"),
    blind("blind"),
    activePlayers("activeplayers"),
    bigBlind("bigblind"),
    displayWinners("displaywinners");

    String code;

    private MessageFormat(String messageCode) {
        code = messageCode;
    }

    //TODO: use hashmap instead of iteration!
    public static MessageFormat fromString(String messageCode) {
        for (MessageFormat m : values()) {
            if (m.toString().equals(messageCode)) {
                return m;
            }
        }
        throw new RuntimeException(messageCode);
    }

    @Override
    public String toString() {
        return code;
    }
}
