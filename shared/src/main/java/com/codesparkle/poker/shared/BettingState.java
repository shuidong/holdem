package com.codesparkle.poker.shared;

public enum BettingState {
    unchanged,
    changed;

    private Chips minRaise;
    private long timeout;

    public static BettingState from(Chips toCall, long timeoutSeconds) {
        BettingState state = toCall.amount() > 0 ? changed : unchanged;
        state.setMinRaise(toCall);
        state.setTimeout(timeoutSeconds);
        return state;
    }

    public Chips getMinRaise() {
        return minRaise;
    }

    public void setMinRaise(Chips minRaise) {
        this.minRaise = minRaise;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
