/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author Artur
 */
public class ColoredShape implements Drawable{
    
    protected Shape shape;
    protected Color color;

    public ColoredShape(Color color) {
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
    
    public void moveTo(int x, int y){
        shape.getBounds().setBounds(x, y, shape.getBounds().width, shape.getBounds().height);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(color != null) {
            g2d.setColor(color);
            g2d.fill(shape);
        }
        g2d.draw(shape);
    }
}
