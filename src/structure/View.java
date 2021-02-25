/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import components.view.ColoredShape;
import components.view.Drawable;
import java.awt.Component;
import java.util.ArrayList;
import components.view.GameFrame;
import components.view.Sprite;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Artur
 */
public class View {
    
    protected GameFrame window;
    protected Sprite background;
    
    public View(GameFrame win){
        if (win != null){
            window = win;
        }
        else{
            window = new GameFrame();
        }
        window.removeAll();
        background = null;
    }
    
    
    public void inputData(ArrayList<Drawable> toDrawList){
        if(background != null) window.draw(background);
        if(toDrawList != null) toDrawList.forEach((toDraw) -> {
            window.draw(toDraw);
        });
    }
    
    public void setBackground(Image backgroundImage){
        background = new Sprite(backgroundImage, new Rectangle(0, 0, getWidth(), getHeight()));
    }
    
    public void update(){
        window.display();
    }
    
    public void addComponent(Component toAddComponent){
        window.add(toAddComponent);
    }
    
    public GameFrame getFrame(){
        return window;
    }
    
    public int getHeight(){
        return window.getHeight();
    }
    
    public int getWidth(){
        return window.getWidth();
    }
    
    public void removeComponent(Component toRemoveComponent){
        window.remove(toRemoveComponent);
    }
    
    public void removeComponents(){
        window.removeAll();
    }
    
    public void addMouseMotionListener(MouseMotionListener mL){
        window.addMouseMotionListener(mL);
    }
    
    public void addMouseListener(MouseListener mL){
        window.addMouseListener(mL);
    }
    
    public void setMessageText(String text){
        window.setTextToiDraw(text);
    }

    public void close() {
        window.dispose();
    }
}
