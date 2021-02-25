/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import components.view.ColoredShape;
import components.view.Drawable;
import java.util.ArrayList;

/**
 *
 * @author Artur
 */
public abstract class Model {
    
    public abstract void update();
    public abstract ArrayList<Drawable> getDrawableObjects();
}
