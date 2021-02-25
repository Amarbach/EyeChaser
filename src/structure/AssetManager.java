/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure;

import java.awt.Image;
import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Artur
 */
public class AssetManager {
    
    private static AssetManager instance;
    
    private HashMap<String, Image> images;
    private HashMap<String, Clip> sounds;
    
    private AssetManager(){
        if(instance != null) throw new IllegalStateException("An instance already exists");
        
        if(images == null)images = new HashMap<>();
        else images.clear();
        if(sounds == null)sounds = new HashMap<>();
        else sounds.clear();
    }
    
    public static AssetManager getInstance(){
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }
    
    public Clip getSound(String name){
        try {
            if (!sounds.containsKey(name)) {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inStream = AudioSystem.getAudioInputStream(new File("assets\\sounds\\"+name+".au"));
                clip.open(inStream);
                sounds.put(name, clip);
            }
        } catch (Exception e) {
            return null;
        }
        return sounds.get(name);
    }
    
    public Image getImage(String name){
        try{
            if (!images.containsKey(name)){
                Image image = ImageIO.read(new File("assets\\graphic\\"+name+".png"));
                images.put(name, image);
            }
        } catch(Exception e){
            return null;
        }
        return images.get(name);
    }
}
