/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.gameplay;

import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;
import components.net.Message;
import components.gameplay.Player;
import components.gameplay.Target;
import components.view.ColoredEllipse;
import components.view.ColoredRectangle;
import components.view.ColoredShape;
import components.view.ColoredText;
import components.view.Drawable;
import components.view.Sprite;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import javax.sound.sampled.Clip;
import structure.AssetManager;
import structure.Model;

/**
 *
 * @author Artur
 */
public class GameplayModel extends Model implements IGazeListener, MouseMotionListener, MouseListener{
    
    private final int TARGET_SIZE_FACTOR = 16;
    private final int CATCHING_DISTANCE = 50;

    @Override
    public void mouseDragged(MouseEvent me) {
        if(isHost) {
            host.setPosition(me.getX(), me.getY());
        }
        else {
            guest.setPosition(me.getX(), me.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if(isHost) {
            host.setPosition(me.getX(), me.getY());
        }
        else {
            guest.setPosition(me.getX(), me.getY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        currentPlayer.setFocused(true);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        currentPlayer.setFocused(false);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    private enum GamePhase{
        WAIT,
        PLAY,
        RESULTS;
    }

    private Player host;
    private Player guest;
    private Player currentPlayer;
    private Player oppositePlayer;
    private boolean isHost;
    
    private Timer timer;
    private int currentRoundMinutes;
    private int currentRoundSeconds;
    private int round;
    private int roundsToPlay;
    private int timesCaught;
    private int timesToBeCaught;
    private int catchCooldown;
    private int numberOfTargets;
    private int catchMaxCooldown;
    private int crateMaxHP;
    private boolean otherFinished;
    
    private Point2D hostStartingPoint;
    private Point2D guestStartingPoint;
    private GamePhase phase;
    private int playWidth;
    private int playHeight;
    private int otherWidth;
    private int otherHeight;
    private ArrayList<Target> targets;
    
    
    private int won;
    private boolean ended;
    
    private ColoredRectangle cover;
    private ColoredEllipse startingArea;
    
    public GameplayModel(Player host, Player guest, boolean isHost, int width, int height, int roundsToPlay, int otherwidth, int otherheight, int numberOfTargets, int crateMaxHP, int catchMaxCooldown) {
        this.host = host;
        this.guest = guest;
        this.isHost = isHost;
        won = 0;
        ended = false;
        phase = GamePhase.WAIT;
        playWidth = width;
        playHeight = height;
        otherWidth = otherwidth;
        otherHeight = otherheight;
        hostStartingPoint = new Point2D.Double(width/5, height/5);
        guestStartingPoint = new Point2D.Double(4*width/5, 4*height/5);
        timesCaught = 0;
        catchCooldown = 0;
        otherFinished = false;
        this.numberOfTargets = numberOfTargets;
        timesToBeCaught = (int)Math.ceil((double)numberOfTargets/2);
        this.catchMaxCooldown = catchMaxCooldown;
        this.crateMaxHP = crateMaxHP;
        
        targets = new ArrayList<>();
        if (isHost) initiateTargets();
        
        if(isHost) {
            currentPlayer = host;
            oppositePlayer = guest;
        }
        else {
            currentPlayer = guest;
            oppositePlayer = host;
        }
        
        currentRoundMinutes = 0;
        currentRoundSeconds = 0;
        round = 1;
        this.roundsToPlay = roundsToPlay;
        
        cover = new ColoredRectangle(Color.GRAY, new Rectangle(0, 0, playWidth, playHeight));
        if(isHost) {
            startingArea = new ColoredEllipse(Color.WHITE, new Ellipse2D.Double((int)hostStartingPoint.getX() - 40, (int)hostStartingPoint.getY() - 40, 80, 80));
        }
        else {
            startingArea = new ColoredEllipse(Color.WHITE, new Ellipse2D.Double((int)guestStartingPoint.getX() - 40, (int)guestStartingPoint.getY() - 40, 80, 80));
        }
    }

    public void updateGazePos(int x, int y) {
        if(isHost) host.setPosition(x, y);
        else guest.setPosition(x, y);
    }
    
    public void updateMessagePos(int x, int y){
        if(isHost) guest.setPosition(x, y);
        else host.setPosition(x, y);
    }

    @Override
    public void update() {
        if (catchCooldown > 0) catchCooldown -= 1;
        switch(phase){
            case WAIT: 
                if (hostStartingPoint.distance(host.getPosition()) < 40 && guestStartingPoint.distance(guest.getPosition()) < 40) {
                    phase = GamePhase.PLAY;
                    resetTimerPlay();
                }
                break;
            case PLAY:
                processTargets();
                processCatch();
                checkCheating();
                if (!isHost && otherFinished) {
                    phase = GamePhase.RESULTS;
                    resetTimerResults();
                }
                break;
        }
    }

    @Override
    public ArrayList<Drawable> getDrawableObjects() {
        ArrayList<Drawable> ret = new ArrayList<>();
        Player chaser;
        if (host.isChasing()) chaser = host;
        else chaser = guest;
        switch(phase){
            case WAIT:
                ret.add(cover);
                ret.add(startingArea);
                ret.add(new ColoredEllipse(Color.RED, new Ellipse2D.Double(chaser.getPosition().getX() - CATCHING_DISTANCE/2,
                                                                           chaser.getPosition().getY() - CATCHING_DISTANCE/2,
                                                                           CATCHING_DISTANCE, CATCHING_DISTANCE)));
                ret.add(new ColoredEllipse(Color.BLUE, new Ellipse2D.Double(currentPlayer.getPosition().getX() - 10, currentPlayer.getPosition().getY() - 10, 20, 20)));
                ret.add(new ColoredEllipse(Color.BLUE, new Ellipse2D.Double(oppositePlayer.getPosition().getX() - 10, oppositePlayer.getPosition().getY() - 10, 20, 20)));
                break;
            case PLAY:
                for(Target target : targets){
                    Rectangle targetBounds = target.getBounds();
                    ret.add(new Sprite(AssetManager.getInstance().getImage("Crate"), targetBounds));
                    if(!currentPlayer.isChasing())ret.add(new ColoredText(targetBounds.getX() + targetBounds.getWidth()/2, targetBounds.getY() + targetBounds.getHeight()/2, ""+target.getHP(), Color.RED, 32));
                }
                ret.add(new ColoredEllipse(Color.RED, new Ellipse2D.Double(chaser.getPosition().getX() - CATCHING_DISTANCE/2,
                                                                           chaser.getPosition().getY() - CATCHING_DISTANCE/2,
                                                                           CATCHING_DISTANCE, CATCHING_DISTANCE)));
                ret.add(new ColoredEllipse(Color.BLUE, new Ellipse2D.Double(currentPlayer.getPosition().getX() - 10, currentPlayer.getPosition().getY() - 10, 20, 20)));
                if(catchCooldown > 0) {
                    String text;
                    if (currentPlayer.isChasing())text = "You caught the other player! ";
                    else text = "You've been caught!";
                    ret.add(new ColoredText(playWidth / 2, playHeight / 2, text, Color.RED, 32));
                }
                ret.add(new ColoredText(playWidth / 2, playHeight / 6, getTimeText(), Color.WHITE, 32));
                break;
            case RESULTS:
                ret.add(cover);
                ret.add(new ColoredEllipse(Color.RED, new Ellipse2D.Double(chaser.getPosition().getX() - CATCHING_DISTANCE/2,
                                                                           chaser.getPosition().getY() - CATCHING_DISTANCE/2,
                                                                           CATCHING_DISTANCE, CATCHING_DISTANCE)));
                ret.add(new ColoredEllipse(Color.BLUE, new Ellipse2D.Double(currentPlayer.getPosition().getX() - 10, currentPlayer.getPosition().getY() - 10, 20, 20)));
                String results = "";
                if (won == 1) results = "Host has won.";
                else if (won == 2) results = "Guest has won";
                else if (won < 1 || won > 2)results = "ERROR";
                ret.add(new ColoredText(playWidth / 2, playHeight / 6, results, Color.WHITE, 32));
                break;
        }
        ret.add(new ColoredText(playWidth/5, 20, "Score: "+currentPlayer.getScore(), Color.WHITE,32));
        return ret;
    }

    public void getDataToSend(Message toSendMessage) {
        if (isHost) {
            toSendMessage.setX((int) (host.getPosition().getX() / playWidth * otherWidth));
            toSendMessage.setY((int) (host.getPosition().getY() / playHeight * otherHeight));
            toSendMessage.setFocused(host.isFocused());
            if (!targets.isEmpty()) {
                String map = "";
                for (Target target : targets) {
                    map += ((int)((double)(target.getBounds().x)/playWidth*otherWidth)) + "," + (int)((double)(target.getBounds().y)/playHeight*otherHeight) + ";";
                }
                toSendMessage.setMap(map);
            }
        } else {
            toSendMessage.setX((int) (guest.getPosition().getX()/playWidth * otherWidth));
            toSendMessage.setY((int) (guest.getPosition().getY()/playHeight * otherHeight));
            toSendMessage.setFocused(guest.isFocused());
        }
        toSendMessage.setStart(true);
        toSendMessage.setWon(won);   
        toSendMessage.setWidth(playWidth);
        toSendMessage.setHeight(playHeight);
        toSendMessage.setRoundsToPlay(roundsToPlay);
        toSendMessage.setNumberOfTargets(numberOfTargets);
        toSendMessage.setCatchCooldown(catchMaxCooldown);
        toSendMessage.setCrateHP(crateMaxHP);
    }
    
    public void reset(){
        if(round >= roundsToPlay) {
            ended = true;
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            if (!host.isChasing()){
                host.changeRole();
                guest.changeRole();
            }
        }else {
            host.changeRole();
            guest.changeRole();
            timesCaught = 0;
            round+=1;
        }
        catchCooldown = 0;
        currentRoundMinutes = 0;
        currentRoundSeconds = 0;
        host.resetCheatingInfractions();
        guest.resetCheatingInfractions();
        phase = GamePhase.WAIT;
        targets.clear();
        if (isHost) initiateTargets();
    }
    
    public void processMessage(Message message){
        if (isHost){
            guest.setPosition(message.getX(), message.getY());
            guest.setFocused(message.isFocused());
        } else {
            host.setPosition(message.getX(), message.getY());
            host.setFocused(message.isFocused());
            won = message.getWon();
        }
        if(!isHost && targets.isEmpty() && phase != GamePhase.RESULTS){
            String[] map = message.getMap().split(";");
            for (String coords : map){
                if (coords != null) if(!coords.equals("") && !coords.equals("null")){
                    String[] XY = coords.split(",");
                    int x = Integer.parseInt(XY[0]);
                    int y = Integer.parseInt(XY[1]);
                    targets.add(new Target(crateMaxHP, new Rectangle(x, y, playWidth / TARGET_SIZE_FACTOR, playHeight * 2 /TARGET_SIZE_FACTOR)));
                }
            }
        }
        otherFinished = message.isEnded();
    }
    
    private String getTimeText(){
        String ret = "";
        if (currentRoundMinutes < 10) {
            ret += "0";
        }
        ret += currentRoundMinutes + ":";
        if (currentRoundSeconds < 10) {
            ret += "0";
        }
        ret += currentRoundSeconds;
        return ret;
    }
    
    public boolean hasEnded(){
        return ended;
    }

    @Override
    public void onGazeUpdate(GazeData gazeData) {
        if(isHost) {
            host.setPosition(gazeData.smoothedCoordinates.x, gazeData.smoothedCoordinates.y);
            host.setFocused(gazeData.isFixated);
        }
        else {
            guest.setPosition(gazeData.smoothedCoordinates.x, gazeData.smoothedCoordinates.y);
            guest.setFocused(gazeData.isFixated);
        }
    }
    
    private void resetTimerPlay(){
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentRoundSeconds += 1;
                if (currentRoundSeconds >= 60) {
                    currentRoundSeconds = 0;
                    currentRoundMinutes += 1;
                }
            }
        }, 1000, 1000);
    }
    
    private void resetTimerResults(){
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reset();
            }
        }, 5000);
    }
    
    private void processCatch(){
        if(host.getPosition().distance(guest.getPosition()) <= CATCHING_DISTANCE && host.isFocused() && guest.isFocused() && catchCooldown <= 0){
            playSound(AssetManager.getInstance().getSound("Catch"));
            timesCaught += 1;
            catchCooldown = catchMaxCooldown;
            if(host.isChasing()) host.addScore((int)((double)numberOfTargets/(double)timesToBeCaught * 100));
            else guest.addScore((int)((double)numberOfTargets/(double)timesToBeCaught * 100));
            if (timesCaught >= timesToBeCaught){
                phase = GamePhase.RESULTS;
                if(!host.isChasing()) won = 2;
                else won = 1;
                resetTimerResults();
            }
        }
    }
    
    private void processTargets(){
        if(!currentPlayer.isChasing()){
            if (currentPlayer.isFocused()){
                if (!targets.isEmpty()) {                
                    for (Target target : targets) {
                        if (target.getBounds().contains(currentPlayer.getPosition())) {
                            target.subHP(1);
                            if(target.getHP()<= 0) {
                                currentPlayer.addScore(100);
                                playSound(AssetManager.getInstance().getSound("Crate"));
                            }
                        }
                    }
                }
            }
        } else {
            if (oppositePlayer.isFocused()){
                if (!targets.isEmpty()) {
                    for (Target target : targets) {
                        if (target.getBounds().contains(oppositePlayer.getPosition())) {
                            target.subHP(1);
                            if(target.getHP()<= 0) {
                                oppositePlayer.addScore(100);
                                playSound(AssetManager.getInstance().getSound("Crate"));
                            }
                        }
                    }
                }
            }
        }
        if(!targets.isEmpty()){
            targets.removeIf((Target target) ->{
                return target.getHP() <= 0;
            });
            if (targets.isEmpty()) {
                phase = GamePhase.RESULTS;
                resetTimerResults();
                if(!host.isChasing()) won = 1;
                else won = 2;
            }
        }
    }
    
    private void initiateTargets() {
        for (int i = 0; i < numberOfTargets;i++){
            Point2D targetPosition = new Point2D.Double(ThreadLocalRandom.current().nextDouble(0, playWidth - playWidth / TARGET_SIZE_FACTOR),
                                                        ThreadLocalRandom.current().nextDouble(0, playHeight - playHeight / TARGET_SIZE_FACTOR));
            if(!targets.isEmpty()){
                for (Target target : targets) {
                    if(target.getBounds().contains(targetPosition)){
                        targetPosition = new Point2D.Double(ThreadLocalRandom.current().nextDouble(0, playWidth - playWidth / TARGET_SIZE_FACTOR),
                                                            ThreadLocalRandom.current().nextDouble(0, playHeight - playHeight / TARGET_SIZE_FACTOR));
                    }
                }
            }
            targets.add(new Target(crateMaxHP, new Rectangle((int)targetPosition.getX(), (int)targetPosition.getY(), playWidth / TARGET_SIZE_FACTOR, playHeight * 2 / TARGET_SIZE_FACTOR)));
        }
    }
    
    private void checkCheating(){
        if((host.getPosition().getX() == 0.0 && host.getPosition().getY() == 0.0) ||
           (host.getPosition().getX() > playWidth || host.getPosition().getX() < 0 || 
            host.getPosition().getY() > playHeight || host.getPosition().getY() < 0)){
            host.increaseCheatingInfractions();
        }
        if((guest.getPosition().getX() == 0.0 && guest.getPosition().getY() == 0.0) ||
           (guest.getPosition().getX() > playWidth || guest.getPosition().getX() < 0 || 
            guest.getPosition().getY() > playHeight || guest.getPosition().getY() < 0)){
            guest.increaseCheatingInfractions();
        }
        if(host.getCheatingInfractions() >= 250){
            phase = GamePhase.RESULTS;
            resetTimerResults();
            won = 2;
        }
        if(guest.getCheatingInfractions() >= 250){
            phase = GamePhase.RESULTS;
            resetTimerResults();
            won = 1;
        }
    }
    
    private void playSound(Clip sound){
        sound.setFramePosition(0);
        sound.start();
    }
}
