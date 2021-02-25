/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.lobby;

import com.theeyetribe.client.GazeManager;
import components.net.Message;
import components.net.Communicator;
import structure.GameState;
import structure.GameStateMachine;
import components.view.GameFrame;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import structure.View;
import structure.calibration.GameStateCalibration;
import structure.gameplay.GameStateGameplay;

/**
 *
 * @author Artur
 */
public class GameStateLobby extends GameState {
    
    private boolean isStateClosing;
    private Communicator netComs;
    
    // <editor-fold defaultstate="collapsed" desc="UIControls">
    private TextField pingTextHost;
    private TextField pingTextGuest;
    private Checkbox hostPlayerReady;
    private Checkbox guestPlayerReady;
    private Button exitBtn;
    private Button startButton;
    private Button moreRoundsBtn;
    private Button lessRoundsBtn;
    private Button moreTargetsBtn;
    private Button moreHPBtn;
    private Button moreCooldownBtn;
    private Button lessTargetsBtn;
    private Button lessHPBtn;
    private Button lessCooldownBtn;
    private Button recalibrateBtn;
    private TextField roundsToPlayText;
    private TextField targetsText;
    private TextField HPText;
    private TextField cooldownText;
    private TextField hostScore;
    private TextField guestScore;
    private Label pingIndicatorText;
    private Label readyIndicatorText;
    private Label playersIndicatorText;
    private Label roundsIndicatorText;
    private Label targetsIndicatorText;
    private Label crateHPIndicatorText;
    private Label catchCooldownIndicatorText;
    private Label scoreIndicatorText;
    
    private Panel playerStatePanel;
    private Panel controlsPanel;
    // </editor-fold>
    
    private LobbyModel model;
    private final GazeManager gazeManager;
    
    public GameStateLobby(GameStateMachine stateMachine, GameFrame win, String hostIP){
        super(stateMachine, win);
        
        isStateClosing = false;
        netComs = new Communicator(hostIP);
        
        view = new View(win);
        model = new LobbyModel();
        
        model.setPlayheight(view.getHeight());
        model.setPlaywidth(view.getWidth());
        
        model.setRoundsToPlay(1);
        model.setNumberOfTargets(1);
        
        model.setHost(hostIP == null);
        
        gazeManager = GazeManager.getInstance();
    }

    @Override
    public void execute() {
        super.execute();
        if (netComs.isRunning()) {
            Message toSendMessage = new Message();
            Message receivedMessage = netComs.getProcessedMessage();
            if(receivedMessage != null) if(receivedMessage.isCorrect())model.processMessage(receivedMessage);
            
            if (model.isHost()) {
                pingTextGuest.setText("" + model.getPing());
                guestPlayerReady.setState(model.isGuestReady());
                model.setReady(hostPlayerReady.getState());
            } else {
                model.setReady(guestPlayerReady.getState());
                pingTextHost.setText("" + model.getPing());
                hostPlayerReady.setState(model.isGuestReady());
                if (model.isOtherStarting() && guestPlayerReady.getState()) {
                    startGame();
                }
            }
            toSendMessage.setWidth(view.getWidth());
            toSendMessage.setHeight(view.getHeight());
            model.modifyMessage(toSendMessage);
            view.update();

            netComs.sendMessage(toSendMessage.toString());
        } else {
            if (model.isHost()) {
                pingTextGuest.setText("---");
                guestPlayerReady.setState(false);
            } else {
                pingTextHost.setText("---");
                hostPlayerReady.setState(false);
            }
            if (!netComs.isConnectionOpened() && !isStateClosing) {
                netComs.restartConnection();
                model.resetScores();
            }
        }
        view.inputData(model.getDrawableObjects());
        view.update();
        startButton.setEnabled(hostPlayerReady.getState() && guestPlayerReady.getState() && model.isHost());
        roundsToPlayText.setText(Integer.toString(model.getRoundsToPlay()));
        targetsText.setText(Integer.toString(model.getNumberOfTargets()));
        HPText.setText(Integer.toString(model.getCrateHP()));
        cooldownText.setText(Integer.toString(model.getCatchCooldown()));
        hostScore.setText(Integer.toString(model.getHostScore()));
        guestScore.setText(Integer.toString(model.getGuestScore()));
    }
    
    @Override
    public void reset() {
        if(gazeManager.isConnected()) gazeManager.addGazeListener(model);
        
        addPlayerStatePanel();
        addControlsPanel();
        addExitBtn();
        addStartBtn();
        addRecalibrateBtn();
        
        hostPlayerReady.setEnabled(model.isHost());
        guestPlayerReady.setEnabled(!model.isHost());
    }

    @Override
    protected void closeState() {
        isStateClosing = true;
        netComs.close();
        view.removeComponents();
        gazeManager.removeGazeListener(model);
        governingStateMachine.removeState();
    }    
    
    private void addplayerOneText(){
        Label playerOneText = new Label("Host");
        playerOneText.setBounds(0, 40, 80, 30);
        playerOneText.setVisible(true);
        playerStatePanel.add(playerOneText);
    }
    
    private void addPlayerTwoText(){
        Label playerTwoText = new Label("Guest");
        playerTwoText.setBounds(0, 80, 80, 30);
        playerTwoText.setVisible(true);
        playerStatePanel.add(playerTwoText);
    }
    
    private void addPlayerOneReadyBox(){
        hostPlayerReady = new Checkbox("");
        hostPlayerReady.setBounds(90, 40, 30, 30);
        hostPlayerReady.setVisible(true);
        playerStatePanel.add(hostPlayerReady);
    }
    
    private void addPlayerTwoReadyBox(){
        guestPlayerReady = new Checkbox("");
        guestPlayerReady.setBounds(90, 80, 30, 30);
        guestPlayerReady.setVisible(true);
        playerStatePanel.add(guestPlayerReady);
    }
    
    private void addExitBtn(){
        exitBtn = new Button("Exit");
        exitBtn.setBounds(0, view.getHeight()-30, 40, 30);
        exitBtn.addActionListener((ActionEvent e) -> {
            closeState();
        });
        view.addComponent(exitBtn);
    }
    
    private void addRecalibrateBtn(){
        recalibrateBtn = new Button("RE-CALIBRATE");
        recalibrateBtn.setBounds(50, view.getHeight()-30, 100, 30);
        recalibrateBtn.addActionListener((ActionEvent e) -> {
            calibrate();
        });
        view.addComponent(recalibrateBtn);
    }
    
    private void addPingTextOne(){
        pingTextHost = new TextField("---");
        pingTextHost.setBounds(130, 40, 40, 30);
        pingTextHost.setEditable(false);
        pingTextHost.setVisible(true);
        playerStatePanel.add(pingTextHost);
    }
    
    private void addPingTextTwo(){
        pingTextGuest = new TextField("---");
        pingTextGuest.setBounds(130, 80, 40, 30);
        pingTextGuest.setEditable(false);
        pingTextGuest.setVisible(true);
        playerStatePanel.add(pingTextGuest);
    }
    
    private void addStartBtn(){
        startButton = new Button("Start");
        startButton.setBounds(view.getWidth() - 60, view.getHeight() - 30, 60, 30);
        startButton.addActionListener((ActionEvent e) -> {
            startGame();
        });
        startButton.setEnabled(false);
        view.addComponent(startButton);
    }

    private void startGame() {
        view.removeComponents();
        gazeManager.removeGazeListener(model);
        model.setStarting(true);
        governingStateMachine.addState(new GameStateGameplay(governingStateMachine, view.getFrame(), netComs, model.getPlaywidth(), model.getPlayheight(),model.deriveGameplayModel()));
        paused = true;
    }
    
    private void calibrate(){
        view.removeComponents();
        gazeManager.removeGazeListener(model);
        governingStateMachine.addState(new GameStateCalibration(governingStateMachine, view.getFrame(), netComs));
        paused = true;
    }
    
    private void addLessRoundsBtn(){
        lessRoundsBtn = new Button("-");
        lessRoundsBtn.setBounds(60, 20, 20, 20);
        lessRoundsBtn.addActionListener((ActionEvent e) -> {
                model.subRoundsToPlay(1);
        });
        lessRoundsBtn.setVisible(true);
        controlsPanel.add(lessRoundsBtn);
    }
    
    private void addMoreRoundsBtn(){
        moreRoundsBtn = new Button("+");
        moreRoundsBtn.setBounds(0, 20, 20, 20);
        moreRoundsBtn.addActionListener((ActionEvent e) -> {
                model.addRoundsToPlay(1);
        });
        moreRoundsBtn.setVisible(true);
        controlsPanel.add(moreRoundsBtn);
    }
    
    private void addRoundsText(){
        roundsToPlayText = new TextField();
        roundsToPlayText.setBounds(20, 20, 40, 20);
        roundsToPlayText.setEditable(false);
        roundsToPlayText.setText(Integer.toString(model.getRoundsToPlay()));
        roundsToPlayText.setVisible(true);
        controlsPanel.add(roundsToPlayText);
    }
    
    private void addCooldownText(){
        cooldownText = new TextField();
        cooldownText.setBounds(200, 20, 40, 20);
        cooldownText.setEditable(false);
        cooldownText.setText(Integer.toString(model.getCatchCooldown()));
        cooldownText.setVisible(true);
        controlsPanel.add(cooldownText);
    }
    
    private void addHPText(){
        HPText = new TextField();
        HPText.setBounds(290, 20, 40, 20);
        HPText.setEditable(false);
        HPText.setText(Integer.toString(model.getCrateHP()));
        HPText.setVisible(true);
        controlsPanel.add(HPText);
    }
    
    private void addMoreTargetsBtn(){
        moreTargetsBtn = new Button("+");
        moreTargetsBtn.setBounds(90, 20, 20, 20);
        moreTargetsBtn.addActionListener((ActionEvent e) -> {
                model.addNumberOfTargets(1);
        });
        moreTargetsBtn.setVisible(true);
        controlsPanel.add(moreTargetsBtn);
    }
    
    private void addLessTargetsBtn(){
        lessTargetsBtn = new Button("-");
        lessTargetsBtn.setBounds(150, 20, 20, 20);
        lessTargetsBtn.addActionListener((ActionEvent e) -> {
                model.subNumberOfTargets(1);
        });
        lessTargetsBtn.setVisible(true);
        controlsPanel.add(lessTargetsBtn);
    }
    
    private void addMoreHPBtn(){
        moreHPBtn = new Button("+");
        moreHPBtn.setBounds(270, 20, 20, 20);
        moreHPBtn.addActionListener((ActionEvent e) -> {
                model.addCrateHP(30);
        });
        moreHPBtn.setVisible(true);
        controlsPanel.add(moreHPBtn);
    }
    
    private void addLessHPBtn(){
        lessHPBtn = new Button("-");
        lessHPBtn.setBounds(330, 20, 20, 20);
        lessHPBtn.addActionListener((ActionEvent e) -> {
                model.subCrateHP(30);
        });
        lessHPBtn.setVisible(true);
        controlsPanel.add(lessHPBtn);
    }
    
    private void addMoreCooldownBtn(){
        moreCooldownBtn = new Button("+");
        moreCooldownBtn.setBounds(180, 20, 20, 20);
        moreCooldownBtn.addActionListener((ActionEvent e) -> {
                model.addCatchCooldown(1);
        });
        moreCooldownBtn.setVisible(true);
        controlsPanel.add(moreCooldownBtn);
    }
    
    private void addLessCooldownBtn(){
        lessCooldownBtn = new Button("-");
        lessCooldownBtn.setBounds(240, 20, 20, 20);
        lessCooldownBtn.addActionListener((ActionEvent e) -> {
                model.subCatchCooldown(1);
        });
        lessCooldownBtn.setVisible(true);
        controlsPanel.add(lessCooldownBtn);
    }
    
    private void addTargetsText(){
        targetsText = new TextField();
        targetsText.setBounds(110, 20, 40, 20);
        targetsText.setEditable(false);
        targetsText.setText(Integer.toString(model.getNumberOfTargets()));
        targetsText.setVisible(true);
        controlsPanel.add(targetsText);
    }
    
    private void addHostScore(){
        hostScore = new TextField();
        hostScore.setBounds(180, 40, 40, 30);
        hostScore.setEditable(false);
        hostScore.setVisible(true);
        playerStatePanel.add(hostScore);
    }
    
    private void addGuestScore(){
        guestScore = new TextField();
        guestScore.setBounds(180, 80, 40, 30);
        guestScore.setEditable(false);
        guestScore.setVisible(true);
        playerStatePanel.add(guestScore);
    }
    
    private void addPlayerStateDescriptionTexts(){
        pingIndicatorText = new Label();
        pingIndicatorText.setBounds(130, 0, 40, 30);
        pingIndicatorText.setText("PING");
        pingIndicatorText.setVisible(true);
        playerStatePanel.add(pingIndicatorText);
        
        readyIndicatorText = new Label();
        readyIndicatorText.setBounds(80, 0, 42, 30);
        //readyIndicatorText.setEditable(false);
        readyIndicatorText.setText("READY");
        readyIndicatorText.setVisible(true);
        playerStatePanel.add(readyIndicatorText);
        
        playersIndicatorText = new Label();
        playersIndicatorText.setBounds(0, 0, 80, 30);
        //playersIndicatorText.setEditable(false);
        playersIndicatorText.setText("PLAYERS");
        playersIndicatorText.setVisible(true);
        playerStatePanel.add(playersIndicatorText);
        
        scoreIndicatorText = new Label();
        scoreIndicatorText.setBounds(170, 0, 45, 30);
        //scoreIndicatorText.setEditable(false);
        scoreIndicatorText.setText("SCORE");
        scoreIndicatorText.setVisible(true);
        playerStatePanel.add(scoreIndicatorText); 
    }
    
    private void addControlDescriptionTexts(){
        roundsIndicatorText = new Label();
        roundsIndicatorText.setBounds(0, 0, 80, 20);
        //roundsIndicatorText.setEditable(false);
        roundsIndicatorText.setText("ROUNDS");
        roundsIndicatorText.setVisible(true);
        controlsPanel.add(roundsIndicatorText);
        
        targetsIndicatorText = new Label();
        targetsIndicatorText.setBounds(90, 0, 80, 20);
        //targetsIndicatorText.setEditable(false);
        targetsIndicatorText.setText("CRATES");
        targetsIndicatorText.setVisible(true);
        controlsPanel.add(targetsIndicatorText);
        
        crateHPIndicatorText = new Label();
        crateHPIndicatorText.setBounds(270, 0, 80, 20);
        //crateHPIndicatorText.setEditable(false);
        crateHPIndicatorText.setText("CRATE HP");
        crateHPIndicatorText.setVisible(true);
        controlsPanel.add(crateHPIndicatorText);
        
        catchCooldownIndicatorText = new Label();
        catchCooldownIndicatorText.setBounds(180, 0, 80, 20);
        //CooldownIndicatorText.setEditable(false);
        catchCooldownIndicatorText.setText("CATCH COOLDOWN");
        catchCooldownIndicatorText.setVisible(true);
        controlsPanel.add(catchCooldownIndicatorText);
    }
    
    private void addControlsPanel(){
        controlsPanel = new Panel();
        controlsPanel.setBounds(view.getWidth()/2 - 175, 2*view.getHeight()/3, 350, 40);
        controlsPanel.setVisible(true);
        view.addComponent(controlsPanel);
        
        if (model.isHost()) {
            addLessRoundsBtn();
            addMoreRoundsBtn();
            addMoreTargetsBtn();
            addLessTargetsBtn();
            addMoreHPBtn();
            addMoreCooldownBtn();
            addLessHPBtn();
            addLessCooldownBtn();
        }
        addHPText();
        addCooldownText();
        addRoundsText();
        addTargetsText();
        addControlDescriptionTexts();
    }
    
    private void addPlayerStatePanel(){
        playerStatePanel = new Panel();
        playerStatePanel.setBounds(view.getWidth()/2 - 110, view.getHeight()/3, 220, 110);
        playerStatePanel.setVisible(true);
        view.addComponent(playerStatePanel);
        
        addplayerOneText();
        addPlayerTwoText();
        addPlayerOneReadyBox();
        addPlayerTwoReadyBox();
        addPingTextOne();
        addPingTextTwo();
        addHostScore();
        addGuestScore();
        
        addPlayerStateDescriptionTexts();
    }
}
