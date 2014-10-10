/**
 * FILE NAME: BackgroundPanel.java
 * SOURCE: http://robbamforth.wordpress.com/2009/02/02/add-a-background-image-to-a-jpanel/
 * WHO: Rob Bamforth
 * WHAT: This class extends a JPanel, making it possible to add a background image to our panel.
 * 
 */

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
  
  Image image;
  /**Constructor
    * Creates a new BackgroundPanel with image as its background.
    */  
  public BackgroundPanel() {
    try
    {
      image = (new ImageIcon(getClass().getResource("/Images/grass1.jpg"))).getImage();
    }
    catch(Exception e){/*handled in paintComponent()*/}
  }
  
  /**
    * Draws the image.
    * @param Graphics g
    */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if(image != null) g.drawImage(image, (this.getWidth()/2) - (image.getWidth(this) / 2),
                                  (this.getHeight()/2) - (image.getHeight(this) / 2),image.getWidth(this),
                                  image.getHeight(this),this); //(image,location x, location y, size x, size y)
  }
}