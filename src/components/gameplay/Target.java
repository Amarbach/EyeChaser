/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.gameplay;

import java.awt.Rectangle;

/**
 *
 * @author Artur
 */
public class Target {
    private Rectangle bounds;
    private int HP;
    
    public Target(int HP, Rectangle bounds){
        this.bounds = bounds;
        this.HP = HP;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHP() {
        return HP;
    }
    
    public void subHP(int value){
        HP -= value;
    }
}
