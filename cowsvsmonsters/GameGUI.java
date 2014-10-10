 /** FILE NAME: GameGUI.java
 * WHO: Leah Ferguson and Ashley Thomas
 * WHAT: Run this class to produce the GUI.
 */
import javax.swing.JFrame;

public class GameGUI {

  public static void main (String[] args) {
    // creates and shows a Frame 
    JFrame frame = new JFrame("Cows vs. Monsters");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //create an instance of the Game class
    Game game = new Game();
    
    //create a panel, and add it to the frame
    GamePanel panel = new GamePanel(game);
    frame.getContentPane().add(panel);
    
    frame.pack();
    frame.setVisible(true);
  }
}
