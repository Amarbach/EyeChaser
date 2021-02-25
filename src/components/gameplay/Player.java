/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.gameplay;

import java.awt.geom.Point2D;

/**
 *
 * @author Artur
 */
public class Player {
    
    private Point2D position;
    private boolean focused;
    private int score;
    private int powerUps;
    private boolean isChasing;
    private int cheatingInfractions;
    
    public Player(boolean chasing){
        score = 0;
        powerUps = 3;
        isChasing = chasing;
        position = new Point2D.Double();
        focused = false;
    }
    @Deprecated
    public void resetPowerUps(){
        powerUps = 3;
    }
    
    public void addScore(int points){
        score += points;
    }
    
    public int getScore(){
        return score;
    }
    
    public void resetScore(){
        score = 0;
    }
    @Deprecated
    public void usePowerUp(){
        powerUps -= 1;
    }
    @Deprecated
    public void addPowerUps(int numberOfPowerUps){
        powerUps += numberOfPowerUps;
    }
    @Deprecated
    public int getPowerUps() {
        return powerUps;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        this.position.setLocation(x, y);
    }
    
    public void changeRole(){
        isChasing = !isChasing;
    }
    
    public boolean isChasing(){
        return isChasing;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getCheatingInfractions() {
        return cheatingInfractions;
    }
    
    public void increaseCheatingInfractions(){
        cheatingInfractions += 1;
    }
    
    public void resetCheatingInfractions(){
        cheatingInfractions = 0;
    }
}
