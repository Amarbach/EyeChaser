/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author Artur
 */
public class Sprite implements Drawable {
    
    Rectangle bounds;
    Image image;
    
    public Sprite(Image image, Rectangle bounds){
        this.image = image;
        this.bounds = bounds;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (image != null){
            if (bounds != null) {
                if(bounds.getWidth() > 0 && bounds.getHeight() > 0) g2d.drawImage(image, (int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight(), null);
                else g2d.drawImage(image, (int)bounds.getX(), (int)bounds.getY(), null);
            }
            else {
                g2d.drawImage(image, 0, 0, null);
            }
        } else {
            g2d.setColor(Color.RED);
            if (bounds != null) g2d.fill(bounds);
            else g2d.fill(new Rectangle(0, 0, 32, 32));
        }
    }
    
}
