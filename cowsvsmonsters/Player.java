/** 
* FILE NAME: Player.java
* WHO: Ashley Thomas and Leah Ferguson
* WHAT: Implements a player class to store the name and the score of the user of the game. Our player implements
* the Comparable interface to compare two players according to their scores.
* 
*/

public class Player implements Comparable<Player>{
  
  private String name;
  private int score;
  
  /**Constructor
    * Creates a player object with the name and score specified by the user.
    * @param name The name of the player, which they input at the beginnning of the game.
    * @param score An int that indicates the player's final score.
    */
  public Player(String name, int score){
    this.name = name;
    this.score = score;
  }
  
  /**
   * Implements the Comparable interface, compares two palyers according to their scores.
   * @param player The player object we wish to compare our player with.
   * @return Returns an int that is the difference between the scores.
   */
  public int compareTo(Player player){
    return score-player.getScore();
  }
  
  /**
   * Gets the score of the player
   * @return Returns the score of the player as an int.
   */
  public int getScore(){
    return score;
  }
  
  /**
   * Gets the name of the player
   * @return Returns the name of the player as a String.
   */
  public String getName(){
    return name;
  }
  
  /**
   * Returns a String containing the name and score of the player.
   * @return Returns a string representing the player.
   */
  public String toString(){
    String result = name + " " + score;
    return result;
  }
  
  /**
   * Main method for local testing
   */
  public static void main(String[] args){
    System.out.println("Creating a player named player1 with score of 10");
    Player player1 = new Player("player1", 10);
    System.out.println("Creating a player named player2 with score of 0");
    Player player2 = new Player("player1", 0);
    System.out.println("What is the name of player1? [player1]: " + player1.getName());
    System.out.println("What is the score of player1? [10]: " + player1.getScore());
    System.out.println("What is the name of player2? [player2]: " + player2.getName());
    System.out.println("What is the score of player2? [0]: " + player2.getScore());
    System.out.println("Compare player1 and player2 [10]:  " + player1.compareTo(player2));
    System.out.println("Compare player2 and player1 [-10]: " + player2.compareTo(player1));
    System.out.println("Compare player1 with itself [0]: " + player1.compareTo(player1));
    
  }
}