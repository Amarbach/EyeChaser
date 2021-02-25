/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import com.theeyetribe.client.data.Point2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import static java.awt.font.TextAttribute.FONT;

/**
 *
 * @author Artur
 */
public class ColoredText implements Drawable{
    
    private Point2D position;
    private String text;
    private Color color;
    private int size;
    
    public ColoredText(double x, double y, String text, Color color, int size ){
        this.position = new Point2D(x, y);
        this.text = text;
        this.color = color;
        this.size = size;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        String toDrawText;
        if(text != null) toDrawText = text;
        else toDrawText = "###";
        g2d.setColor(color);
        Font font = new Font("Serif", Font.PLAIN, size);
        g2d.setFont(font);
        FontRenderContext fontContext = g2d.getFontRenderContext();
        g2d.drawString(toDrawText, (int)position.x - (int)font.getStringBounds(toDrawText, fontContext).getCenterX(), (int)position.y - (int)font.getStringBounds(toDrawText, fontContext).getCenterY());
    }
    
}
