/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import components.view.GameFrame;

/**
 *
 * @author Artur
 */
public abstract class GameState {
    
    protected GameStateMachine governingStateMachine;
    protected View view;
    protected boolean paused;
    
    public GameState(GameStateMachine stateMachine, GameFrame window){
        governingStateMachine = stateMachine;
        paused = true;
    }

    public void execute(){
        if(paused){
            reset();
            paused = false;
        }
    }
    
    abstract protected void reset();
    
    abstract protected void closeState();
}
