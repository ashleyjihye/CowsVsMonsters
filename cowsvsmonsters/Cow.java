/** 
* FILE NAME: Cow.java
* WHO: Ashley Thomas and Leah Ferguson
* WHAT: Implements a class to represent a cow object. Provides getters and setters for the 
* different characteristics of a cow.
*/

public class Cow {
  
  private String name;
  private boolean visible;
  private boolean scared;
  private boolean dead;
  private boolean monster;
  private int timesScared;
  
  /**Constructor
   * Creates a cow that is by default not a monster, not scared, not dead, and has been scared 0 times.
   * User specifies the name and visibility.
   * @param name The name of the cow, which is an integer between 0 and 23
   * @param visible A boolean which indicates whether the cow is visible
   */
  public Cow (String name, boolean visible) {
    this.name = name;
    this.visible = visible;
    monster = false;
    scared = false;
    dead = false;
    timesScared = 0;
  }
  
   /**
   * Makes the cow scared and increments the timesScared variable by 1.
   */
  public void becomeScared() {
    scared = true;
    timesScared++;
  }
  
   /**
   * Makes the cow not scared.
   */  
  public void becomeNotScared() {
    scared = false;
  }

   /**
   * If the cow is visible makes it a monster, otherwise does nothing.
   */  
  public void becomeMonster() {
    if (visible) {
      monster = true;
    }
  }

   /**
   * Makes the cow a cow, rather than a monster.
   */  
  public void becomeCow() {
    monster = false;
  }
  
   /**
   * Sets the dead instance variable to true.
   */  
  public void die() {
    dead = true;
  }
  
   /**
   * Checks if a cow is dead
   * @return Returns a boolean indicating whether the cow is dead.
   */  
  public boolean isDead() {
    return dead;
  }
  
   /**
   * Checks if a cow is visible
   * @return Returns a boolean indicating whether the cow is visible.
   */  
  public boolean isVisible() {
    return visible; 
  }
  
   /**
   * Checks if a cow is scared
   * @return Returns a boolean indicating whether the cow is scared.
   */  
  public boolean isScared() {
    return scared; 
  }
  
   /**
   * Checks if a cow is a monster
   * @return Returns a boolean indicating whether the cow is a monster.
   */   
  public boolean isMonster() {
    return monster;
  }
  
   /**
   * Gets the cow's name
   * @return Returns a String of the cow's name.
   */   
  public String getName() {
    return name; 
  }
  
   /**
   * Gets the number of times a cow has been scared
   * @return Returns an int indicating how many times the cow has been scared.
   */   
  public int getTimesScared() {
    return timesScared;
  }
  
   /**
   * Returns the name of the cow.
   * @return The String representation of a cow.
   */
  public String toString() {
    return name;
  }
  
  /**
   * Main method for local testing
   */
  public static void main(String[] args){
   System.out.println("Creating a visible cow named test1");
   Cow test1 = new Cow("test1", true);
   System.out.println("Is test1 visible? [true]: " + test1.isVisible());
   System.out.println("What is the name of test1? [test1]: " + test1.getName());
   System.out.println("Is test1 dead? [false]: " + test1.isDead());
   System.out.println("Is test1 a monster? [false]: " + test1.isMonster());
   System.out.println("Is test1 scared? [false]: " + test1.isScared());
   System.out.println("How many times has test1 been scared? [0]: " + test1.getTimesScared());
   System.out.println("Make test1 scared");
   test1.becomeScared();
   System.out.println("Is test1 scared? [true]: " + test1.isScared());
   System.out.println("How many times has test1 been scared? [1]: " + test1.getTimesScared());
   System.out.println("Make test1 not scared");
   test1.becomeNotScared();
   System.out.println("Is test1 scared? [false]: " + test1.isScared());
   System.out.println("How many times has test1 been scared? [1]: " + test1.getTimesScared());
   System.out.println("Make test1 a monster");
   test1.becomeMonster();   
   System.out.println("Is test1 a monster? [true]: " + test1.isMonster());
   System.out.println("Make test1 a cow");   
   test1.becomeCow();
   System.out.println("Is test1 a monster? [false]: " + test1.isMonster());
   System.out.println("Make test1 dead");
   test1.die();
   System.out.println("Is test1 dead? [true]: " + test1.isDead());
  }
}