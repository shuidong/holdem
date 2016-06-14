package com.codesparkle.poker.server.states;


public class Round extends GameState {

    private Runnable action;
    private String name;

    public Round(StateListener listener, Runnable action, String name) {
        super(listener);
        this.action = action;
        this.name = name;
    }

    @Override
    protected void doExecute() {
        action.run();
    }
    
    @Override
    public String toString(){
        return name;
    }

}
