/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import com.theeyetribe.client.GazeManager;
import structure.menu.GameStateMenu;
import components.view.GameFrame;

/**
 *
 * @author Artur
 */
public class Game {
    
    private GameStateMachine stateMachine;
    private boolean isRunning;
    
    public Game() {
        stateMachine = new GameStateMachine();
        GameFrame gameWindow = new GameFrame();
        stateMachine.addState(new GameStateMenu(stateMachine, gameWindow));
        isRunning = true;
    }    
    
    public void Run(){
        GazeManager.getInstance().activate(GazeManager.ApiVersion.VERSION_1_0, GazeManager.ClientMode.PUSH);
        long lastFrame = 0;
        while(isRunning){
            if(System.currentTimeMillis() - lastFrame > 16){
                if(!stateMachine.empty()) stateMachine.runTopState();
                lastFrame = System.currentTimeMillis();
            }
            if (stateMachine.empty()) isRunning = false;
        }
        GazeManager.getInstance().deactivate();
    }
}
