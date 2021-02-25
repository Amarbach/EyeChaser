/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.lobby;

import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;
import components.net.Message;
import components.gameplay.Player;
import components.view.ColoredEllipse;
import components.view.ColoredShape;
import components.view.Drawable;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import structure.Model;
import structure.gameplay.GameplayModel;

/**
 *
 * @author Artur
 */
public class LobbyModel extends Model implements IGazeListener{
    
    private Player hostPlayer;
    private Player guestPlayer;
    private int playWidth;
    private int playHeight;
    private int otherWidth;
    private int otherHeight;
    private int crateHP;
    private int catchCooldown;
    
    private int numberOfTargets;
    private int roundsToPlay;
    private long ping;
    
    private boolean isHost;
    private boolean isStarting;
    private boolean isReady;
    private boolean isGuestReady;
    private boolean isOtherStarting;

    public boolean isOtherStarting() {
        return isOtherStarting;
    }

    public LobbyModel() {
        playWidth = Integer.MAX_VALUE;
        playHeight = Integer.MAX_VALUE;
        isReady = false;
        isStarting = false;
        
        hostPlayer = new Player(true);
        guestPlayer = new Player(false);
        
        numberOfTargets = 1;
        roundsToPlay = 1;
        catchCooldown = 2;
        crateHP = 120;
    }

    public long getPing() {
        return ping;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public int getOtherWidth() {
        return otherWidth;
    }

    public void setOtherWidth(int otherWidth) {
        this.otherWidth = otherWidth;
    }

    public int getOtherHeight() {
        return otherHeight;
    }

    public void setOtherHeight(int otherHeight) {
        this.otherHeight = otherHeight;
    }

    public int getPlaywidth() {
        return playWidth;
    }

    public void setPlaywidth(int playwidth) {
        this.playWidth = playwidth;
    }

    public int getPlayheight() {
        return playHeight;
    }

    public void setPlayheight(int playheight) {
        this.playHeight = playheight;
    }

    public int getNumberOfTargets() {
        return numberOfTargets;
    }
    
    public void addNumberOfTargets(int value){
        if(numberOfTargets < 8) numberOfTargets += value;
    }
    
    public void subNumberOfTargets(int value){
        if(numberOfTargets > 1)numberOfTargets -= value;
    }

    public void setNumberOfTargets(int value) {
        this.numberOfTargets = value;
    }

    public int getRoundsToPlay() {
        return roundsToPlay;
    }
    
    public void addRoundsToPlay(int value){
        if(roundsToPlay <= 4) roundsToPlay += value;
    }
    
    public void subRoundsToPlay(int value){
        if(roundsToPlay > 1)roundsToPlay -= value;
    }

    public void setRoundsToPlay(int roundsToPlay) {
        this.roundsToPlay = roundsToPlay;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }

    public boolean isGuestReady() {
        return isGuestReady;
    }

    public int getCrateHP() {
        return crateHP;
    }

    public void setCrateHP(int crateHP) {
        this.crateHP = crateHP;
    }
    
    public void addCrateHP(int value){
        if(crateHP <400) this.crateHP += value;
    }
    
    public void subCrateHP(int value){
        if(crateHP > 60) this.crateHP -= value;
    }

    public int getCatchCooldown() {
        return catchCooldown;
    }

    public void setCatchCooldown(int catchCooldown) {
        this.catchCooldown = catchCooldown;
    }
    
    public void addCatchCooldown(int value){
        if(catchCooldown < 6) this.catchCooldown += value;
    }
    
    public void subCatchCooldown(int value){
        if(catchCooldown > 1) this.catchCooldown -= value;
    }
    
    public GameplayModel deriveGameplayModel(){
        GameplayModel product = new GameplayModel(hostPlayer, guestPlayer, isHost, playWidth, playHeight, roundsToPlay, otherWidth, otherHeight, numberOfTargets, crateHP, catchCooldown*60);
        isStarting = false;
        return product;
    }
    
    public void modifyMessage(Message message){
        message.setReady(isReady);
        message.setStart(isStarting);
        message.setRoundsToPlay(roundsToPlay);
        message.setNumberOfTargets(numberOfTargets);
        message.setCatchCooldown(catchCooldown);
        message.setCrateHP(crateHP);
    }
    
    public void processMessage(Message receivedMessage){
        if (receivedMessage != null) {
            if (receivedMessage.isCorrect()) {
                isGuestReady = receivedMessage.isReady();
                ping = receivedMessage.getPing();
                if (!isHost) {
                    roundsToPlay = receivedMessage.getRoundsToPlay();
                    numberOfTargets = receivedMessage.getMinutesToPlay();
                    isOtherStarting = receivedMessage.isStart();
                    crateHP = receivedMessage.getCrateHP();
                    catchCooldown = receivedMessage.getCatchCooldown();
                }
                otherWidth = receivedMessage.getWidth();
                otherHeight = receivedMessage.getHeight();
            }
        }
    }
    
    @Override
    public void update() {
    }

    @Override
    public ArrayList<Drawable> getDrawableObjects() {
        ArrayList<Drawable> ret = new ArrayList();
        Player currentPlayer;
        if(isHost) currentPlayer = hostPlayer;
        else currentPlayer = guestPlayer;
        ret.add(new ColoredEllipse(Color.BLUE, new Ellipse2D.Double(currentPlayer.getPosition().getX() - 10, currentPlayer.getPosition().getY() - 10, 20, 20)));
        return ret;
    }
    
    public int getHostScore(){
        return hostPlayer.getScore();
    }
    
    public int getGuestScore(){
        return guestPlayer.getScore();
    }

    @Override
    public void onGazeUpdate(GazeData gazeData) {
        if(isHost) {
            hostPlayer.setPosition(gazeData.smoothedCoordinates.x, gazeData.smoothedCoordinates.y);
        }
        else {
            guestPlayer.setPosition(gazeData.smoothedCoordinates.x, gazeData.smoothedCoordinates.y);
        }
    }
    
    public void resetScores(){
        hostPlayer.resetScore();
        guestPlayer.resetScore();
    }
}
