/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.gameplay;

import components.net.Message;
import components.net.Communicator;
import components.view.GameFrame;
import structure.GameState;
import structure.GameStateMachine;
import com.theeyetribe.client.GazeManager;
import structure.AssetManager;
import structure.View;

/**
 *
 * @author Artur
 */
public class GameStateGameplay extends GameState{
    
    private Communicator netComs;
    GazeManager gazeManager;
    
    GameplayModel model;
    public GameStateGameplay(GameStateMachine stateMachine, GameFrame window, 
                            Communicator coms, 
                            int playwidth, int playheight,
                            GameplayModel model
                            ){
        
        super(stateMachine, window);
        
        netComs = coms;
        this.model = model;
        view = new View(window);

        gazeManager = GazeManager.getInstance();
        if (gazeManager.isConnected()) gazeManager.addGazeListener(this.model);
        else {
            view.addMouseMotionListener(this.model);
            view.addMouseListener(this.model);
        }
    }

    @Override
    public void execute() {
        super.execute();
        if (netComs.isRunning()) {
            Message receivedMessage = netComs.getProcessedMessage();
            if (receivedMessage != null) {
                if (receivedMessage.isCorrect()) {
                    model.processMessage(receivedMessage);
                }
            }
            Message toSendMessage = new Message();
            model.update();
            model.getDataToSend(toSendMessage);
            view.inputData(model.getDrawableObjects());
            view.update();

            netComs.sendMessage(toSendMessage.toString());
            
            if (model.hasEnded()) closeState();
        } else {
            closeState();
        }
    }

    @Override
    public void reset() {
        view.setBackground(AssetManager.getInstance().getImage("Background"));
    }
    
    @Override
    protected void closeState() {
        //gazeManager.removeGazeListener(this.model);
        governingStateMachine.removeState();
    }
}
