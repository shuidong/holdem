package com.codesparkle.poker.server.states;


public abstract class GameState {

    private GameState nextState;
    protected StateListener listener;

    protected abstract void doExecute();

    public GameState(StateListener listener) {
        this.listener = listener;
    }

    public void setNextState(GameState state) {
        nextState = state;
    }

    public void execute() {
        doExecute();
        executeNextState();
    }

    protected void executeNextState() {
        if (nextState != null) {
            listener.stateChanged(nextState);
            nextState.execute();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
