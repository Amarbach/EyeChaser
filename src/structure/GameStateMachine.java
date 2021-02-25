/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import java.util.Stack;

/**
 *
 * @author Artur
 */
public class GameStateMachine {
    
    private Stack<GameState> stateStack;

    public GameStateMachine() {
        stateStack = new Stack<>();
    }
    
    public void runTopState() {
        if (!stateStack.empty()){
            stateStack.lastElement().execute();
        }
    }
    
    public boolean empty(){
        return stateStack.empty();
    }
    
    public void addState(GameState newState){
        stateStack.add(newState);
    }
    
    public void removeState(){
        stateStack.pop();
    }
    
    public GameState getTopState(){
        return stateStack.lastElement();
    }
}
