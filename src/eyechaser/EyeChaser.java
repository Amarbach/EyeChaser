/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eyechaser;

import static java.lang.System.exit;
import structure.Game;

/**
 *
 * @author Artur
 */
public class EyeChaser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.Run();
        exit(0);
    }
    
}
