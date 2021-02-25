/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.view;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Artur
 */
public class ColoredEllipse extends ColoredShape{
    
    public ColoredEllipse(Color color, Ellipse2D ellipse) {
        super(color);
        shape = ellipse;
    }
    
}
