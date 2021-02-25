/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author Artur
 */
public class ColoredRectangle extends ColoredShape {
    
    public ColoredRectangle(Color color, Rectangle rectangle){
        super(color);
        shape = rectangle;
    }
}
