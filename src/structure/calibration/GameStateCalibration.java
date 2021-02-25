/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.calibration;

import com.theeyetribe.client.GazeManager;
import components.net.Communicator;
import components.view.GameFrame;
import java.awt.Button;
import java.awt.event.ActionEvent;
import structure.GameState;
import structure.GameStateMachine;
import structure.View;

/**
 *
 * @author Artur
 */
public class GameStateCalibration extends GameState{
    
    private CalibrationModel model;
    private Button returnBtn;
    private Button startBtn;
    private Communicator netComs;
   
    public GameStateCalibration(GameStateMachine stateMachine, GameFrame window, Communicator netComs) {
        super(stateMachine, window);
        view = new View(window);       
        model = new CalibrationModel(view.getWidth(), view.getHeight());
        this.netComs = netComs;
    }

    @Override
    public void execute() {
        super.execute();
        if (netComs != null){
            if(netComs.isRunning()){
                netComs.clearBuffer();
                netComs.sendMessage("null");
            }
        }
        model.update();
        view.inputData(model.getDrawableObjects());
        view.setMessageText(model.getInfoText());
        view.update();
        if (model.hasEnded()) {
            reset();
            model.reset();
        }
    }

    @Override
    protected void reset() {
        addReturnButton();
        addStartButton();
    }
    
    @Override
    protected void closeState(){
        view.removeComponents();
        governingStateMachine.removeState();
    }

    private void addReturnButton() {
        returnBtn = new Button("Back");
        returnBtn.setBounds(0, view.getHeight()-30, 80, 30);
        returnBtn.addActionListener((ActionEvent e) -> {
            closeState();
        });
        returnBtn.setVisible(true);
        view.addComponent(returnBtn);
    }
    
    private void addStartButton() {
        startBtn = new Button("Start Calibration");
        startBtn.setBounds(view.getWidth()/2 - 40, view.getHeight()/2 -15, 80, 30);
        startBtn.addActionListener((ActionEvent e) -> {
            view.removeComponents();
            model.startCalibration();
        });
        startBtn.setVisible(true);
        view.addComponent(startBtn);
    }
}
