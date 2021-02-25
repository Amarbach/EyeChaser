/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;

/**
 *
 * @author Artur
 */
public class GameFrame extends Frame{

    ArrayList<Drawable> toDrawList;
    private String textToDraw;
    
    public GameFrame() {
        super("EyeChaser Game");
        toDrawList = new ArrayList<>();
        setBackground(Color.white);
        setResizable(false);
        setUndecorated(true);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLayout(null);
        setVisible(true);
        createBufferStrategy(2);
    }   
    
    public void draw(Drawable shape){
        toDrawList.add(shape);
    }
    
    public void setTextToiDraw(String text){
        textToDraw = text;
    }
    
    public void display(){
        Graphics2D g2 = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getWidth(), getHeight());
        
        if (toDrawList != null && !toDrawList.isEmpty()){
            toDrawList.forEach((toDraw) -> {
                toDraw.draw(g2);
            });
        }
        toDrawList.clear();
        if (textToDraw != null) {
            Font font = new Font("Serif", Font.PLAIN, 32);
            g2.setFont(font);
            g2.setColor(Color.BLACK);
            FontRenderContext frc = g2.getFontRenderContext();
            g2.drawString(textToDraw, getWidth() / 2 - (int)font.getStringBounds(textToDraw, frc).getCenterX(), 40);
            textToDraw = "";
        }
        getBufferStrategy().show();
    }
}
