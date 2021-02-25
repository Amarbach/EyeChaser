/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.menu;
import com.theeyetribe.client.GazeManager;
import components.view.Drawable;
import java.awt.Button;
import java.awt.event.ActionEvent;
import structure.GameState;
import structure.GameStateMachine;
import structure.lobby.GameStateLobby;
import components.view.GameFrame;
import components.view.Sprite;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TextField;
import java.util.ArrayList;
import structure.AssetManager;
import structure.View;
import structure.calibration.GameStateCalibration;

/**
 *
 * @author Artur
 */
public class GameStateMenu extends GameState {
    
    final private int BUTTON_HEIGHT = 30;
    final private int BUTTON_WIDTH = 180;
    final private int BUTTON_GAP = 10;
    
    private Button startGameBtn;
    private Button joinGameBtn;
    private TextField ipTextField;
    private Button calibrateBtn;
    
    GazeManager gazeManager;

    public GameStateMenu(GameStateMachine stateMachine, GameFrame win) {
        super(stateMachine, win);
        GameFrame window;
        if (win != null) window = win;
        else window = new GameFrame();
        view = new View(window);
        gazeManager = GazeManager.getInstance();
        
        view.setBackground(AssetManager.getInstance().getImage("Background"));
    }

    @Override
    public void execute() {
        super.execute();
        //startGameBtn.setEnabled(gazeManager.isConnected() && gazeManager.isCalibrated());
        //joinGameBtn.setEnabled(gazeManager.isConnected() && gazeManager.isCalibrated());
        calibrateBtn.setEnabled(gazeManager.isConnected());
        ArrayList<Drawable> menuGraphics = new ArrayList<>();
        Image titleText1 = AssetManager.getInstance().getImage("TitleText1");
        menuGraphics.add(new Sprite(titleText1, new Rectangle(view.getWidth()/2 - titleText1.getWidth(null)/2, view.getHeight()/4 - titleText1.getHeight(null)/2, 0, 0)));
        view.inputData(menuGraphics);
        view.update();
    }
    
    @Override
    public void reset(){
        addStartGameBtn();
        addQuitGameBtn();
        addJoinGameBtn();
        addCalibrateBtn();
        addIPTextBox();
    }
 
    private void startNewGame(){
        view.removeComponents();
        governingStateMachine.addState(new GameStateLobby(governingStateMachine, view.getFrame(), null));
        paused = true;
    }
    
    private void joinGame() {
        view.removeComponents();
        governingStateMachine.addState(new GameStateLobby(governingStateMachine, view.getFrame(), ipTextField.getText()));
        paused = true;
    }
    
    private void addStartGameBtn(){
        startGameBtn = new Button("Start New Game");
        startGameBtn.setBounds(view.getWidth()/2 - BUTTON_WIDTH/2,
                               2 * view.getHeight()/3,
                               BUTTON_WIDTH,
                               BUTTON_HEIGHT);
        startGameBtn.addActionListener((ActionEvent e) -> {
            startNewGame();
        });
        startGameBtn.setVisible(true);
        view.addComponent(startGameBtn);
    }
    
    private void addQuitGameBtn(){
        Button quitGameBtn = new Button("Quit");
        quitGameBtn.setBounds(view.getWidth()/2 - BUTTON_WIDTH/2,
                              2 * view.getHeight()/3 + 3 * (BUTTON_HEIGHT + BUTTON_GAP),
                              BUTTON_WIDTH,
                              BUTTON_HEIGHT);
        quitGameBtn.addActionListener((ActionEvent e) -> {
            closeState();
        });
        quitGameBtn.setVisible(true);
        view.addComponent(quitGameBtn);
    }
    
    private void addJoinGameBtn(){
        joinGameBtn = new Button("Join Game");
        joinGameBtn.setBounds(view.getWidth()/2 - BUTTON_WIDTH/2,
                              2 * view.getHeight()/3 + 1 * (BUTTON_HEIGHT + BUTTON_GAP),
                              BUTTON_WIDTH,
                              BUTTON_HEIGHT);
        joinGameBtn.addActionListener((ActionEvent e) -> {
            joinGame();
        });
        joinGameBtn.setVisible(true);
        view.addComponent(joinGameBtn);
    }
    
    private void addCalibrateBtn(){
        calibrateBtn = new Button("Calibrate");
        calibrateBtn.setBounds(view.getWidth()/2 - BUTTON_WIDTH/2, 
                                   2 * view.getHeight()/3 + 2 * (BUTTON_HEIGHT + BUTTON_GAP),
                                   BUTTON_WIDTH,
                                   BUTTON_HEIGHT);
        calibrateBtn.addActionListener((ActionEvent e) -> {
            calibrate();
        });
        calibrateBtn.setVisible(true);
        view.addComponent(calibrateBtn);
    }

    private void addIPTextBox() {
        ipTextField = new TextField("Enter Host IP here...");
        ipTextField.setBounds(view.getWidth()/2 + BUTTON_WIDTH/2 + BUTTON_GAP,
                              2 * view.getHeight()/3 + 1 * (BUTTON_HEIGHT + BUTTON_GAP),
                              BUTTON_WIDTH,
                              BUTTON_HEIGHT);
        ipTextField.setVisible(true);
        view.addComponent(ipTextField);
    }

    @Override
    protected void closeState() {
        governingStateMachine.removeState();
        view.close();
    }

    private void calibrate() {
        view.removeComponents();
        governingStateMachine.addState(new GameStateCalibration(governingStateMachine, view.getFrame(), null));
        paused = true;
    }
}
