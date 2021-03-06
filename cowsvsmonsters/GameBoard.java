/**
 * FILE NAME: GameBoard.java
 * WHO: Leah Ferguson and Ashley Thomas
 * WHAT: This class creates the game board from an AdjMatGraphPlus<Cow> and contains various methods that
 * get cows with specific characteristics, such as those that are adjacent to other cows or the living monsters.
 * Also contains a method to move a cow from its current position to an empty vertex. It also has a method
 * to reset the board, which recreates the original graph.
 */

import java.util.*;

public class GameBoard {
  
  //instance variables
  private AdjMatGraphPlus<Cow> gameBoard;
  
  /**Constructor
    * Creates a GameBoard that uses the AdjMatGraphPlus<Cow> constructor from a tgf file we have created
    */
  public GameBoard() {
    this.gameBoard = new AdjMatGraphPlus<Cow>("graph.tgf");
  }
  
    /**
   * Gets the index of a cow object in the graph (ie where the cow is in the graph)
   * @param cow Takes in a cow object
   * @return Return the index of the cow
   */
  public int getIndex (Cow cow) {
    return gameBoard.getIndex(cow);
  }
  
   /**
   * Gets a cow object of a given index (ie vertex)
   * @param index Takes in an index 
   * @return Return the cow at that vertex
   */
  public Cow getCow (int index){
    return gameBoard.getVertex(index);
  }
  
   /**
   * Sets the given index to be the cow object given 
   * @param index The index to be reset
   * @param cow The cow to be placed at that index
   */
  public void setCow (int index, Cow cow){
    gameBoard.setVertex(index, cow);
  }
  
   /**
   * Gets the cows adjacent to living monsters, does not include duplicates of any cows, any dead cows, any invisible cows, 
   * or any monsters.
   * @return Return a LinkedList<Integer> containing the indices of the cows adjacent to living monsters
   */
  public LinkedList<Integer> getCowsAdjacentToMonsters(){
    LinkedList<Integer> cowIndices = new LinkedList<Integer>();
    LinkedList<Integer> monsterIndices = getLivingMonsters();
    for (int i=0; i<monsterIndices.size(); i++){
     LinkedList<Integer> currentMonster = cowsAdjacentTo(monsterIndices.get(i));
     for (int j=0; j<currentMonster.size(); j++){
       if (!getCow(currentMonster.get(j)).isMonster() && !cowIndices.contains(currentMonster.get(j)))
         cowIndices.add(currentMonster.get(j));
     }
    }
    return cowIndices;
  }
  
   /**
   * Gets the cows adjacent to the given index, dead cows and invisible cows are not included.
   * @param index The index of the vertex with which we will get its adjacent cows.
   * @return Return a LinkedList<Integer> containing the indices of the cows adjacent to the given index.
   */
    public LinkedList<Integer> cowsAdjacentTo(int index){
    LinkedList<Integer> cowIndices = new LinkedList<Integer>();
    LinkedList<Integer> predecessorIndices = gameBoard.getPredecessorIndices(index);  //gets all the vertices adjacent to the given vertex (ie index)
    for(int i=0; i<predecessorIndices.size(); i++){
      if(!getCow(predecessorIndices.get(i)).isDead() && getCow(predecessorIndices.get(i)).isVisible())
        cowIndices.add(predecessorIndices.get(i));
    }
    return cowIndices;
  }
   
   /**
   * Gets the living monsters.
   * @return Return a LinkedList<Integer> containing the indices of the living monsters
   */
  public LinkedList<Integer> getLivingMonsters(){
    LinkedList<Integer> monsterIndices = new LinkedList<Integer>();
    for (int i=0; i<gameBoard.n(); i++){
      Cow cow = getCow(i);
      if (cow.isMonster() && !cow.isDead())
        monsterIndices.add(i);
    }
    return monsterIndices;
  }
  
   /**
   * Gets all the living cows, includes monsters but does not include invisible cows.
   * @return Return a LinkedList<Integer> containing the indices of all the living cows in the graph.
   */
  public LinkedList<Integer> getAllLivingCows(){
    LinkedList<Integer> allLivingCows = new LinkedList<Integer>();
    for (int i = 0; i < gameBoard.n(); i++){
      if (!getCow(i).isDead() && getCow(i).isVisible())
        allLivingCows.add(i);
    }
    return allLivingCows;
  }
  
   /**
   * Gets the number of living cows remaining, does not include monsters.
   * @return Return an int that gives the number of living cows remaining
   */
  public int getCowsRemaining(){
    int numLivingMonsters = getLivingMonsters().size();
    int numLivingCows = getAllLivingCows().size();
    return numLivingCows - numLivingMonsters;
  }
  
   /**
   * Moves a cow from its current index to a given index
   * @param index1 The index the cow is currently residing at
   * @param index2 The index the cow will be moved to
   * @return Returns a String indicating the result of the attempted move
   */
  public String moveCow(int index1, int index2){
    Cow cow1 = getCow(index1);
    Cow cow2 = getCow(index2);
    if (cow1.isDead()){
      return "You cannot move a dead cow!";
    }else if(!cow1.isVisible()){
      return "You cannot move a cow that does not exist!";
    }else if (cow2.isVisible()){
      return "You cannot move a cow to a spot where a cow already exists.";
    }
    else{
      gameBoard.setVertex(index1, cow2);
      gameBoard.setVertex(index2, cow1);
      return "You successfully moved your cow.";
    }
  }
  
   /**
   * Randomly moves a cow, we only use this at the beginning of the first round, so we don't need to 
   * check for dead cows.
   * @param index The index of the cow to be moved
   */
  public void randomlyMoveCow(int index){
    Random rand = new Random();
    LinkedList<Integer> allEmptySpaces = getAllEmptySpaces();
    moveCow(index, allEmptySpaces.get(rand.nextInt(allEmptySpaces.size())));
  }
  
   /**
    * Gets all the empty spaces, ie vertices containing an invisible cow.
    * @return The LinkedList<Integer> of indices representing "empty spaces"
    */
  public LinkedList<Integer> getAllEmptySpaces(){
    LinkedList<Integer> allEmptySpaces = new LinkedList<Integer>();
    for (int i = 0; i < gameBoard.n(); i++){
      if (!getCow(i).isVisible())
        allEmptySpaces.add(i);
    }
    return allEmptySpaces;
  }
  
   /**
   * Gets the index of the cow given its name.
   * @param name The name of the cow
   * @return Return the index of the cow. Returns -1 if no such cow is found
   */
  public int getIndexWithName(String name){
    for (int i=0; i<=23; i++){
      if (getCow(i).getName().equals(name)){
        return i;
      }
    }
    return -1;  //Didn't make this a private final instance variable NOT_FOUND because we only use it once
  }
  
   /**
   * Resets the gameBoard to be the original graph using the given tgf file
   */
  public void resetBoard(){
    gameBoard = new AdjMatGraphPlus<Cow>("graph.tgf");
  }
  
  /**
   * Uses the toString in the AdjMatGraphPlus class to print the graph contained in the GameBoard.
   * @return Returns a string representation of the gameBoard.
   */
  public String toString(){
    return gameBoard.toString();
  }
  
  /**
   * Main method for local testing
   */
  public static void main(String[] args){
    System.out.println("Creating a new GameBoard");
    GameBoard test = new GameBoard();
    System.out.println(test);
    System.out.println("Create cow1 containing the cow at index 0 in the graph using getCow(0)");
    Cow cow1 = test.getCow(0);
    System.out.println("Printing cow1 [0]: " + cow1);
    System.out.println("Testing getIndex(cow1) [0]: " + test.getIndex(cow1));
    System.out.println("Creating a visible cow named newCow");
    Cow cow2 = new Cow("newCow", true);
    System.out.println("Testing setCow by changing the cow at vertex 0 to newCow");
    test.setCow(0, cow2);
    System.out.println("Printing getCow(0) [newCow]: " + test.getCow(0));
    System.out.println("Make the cows at vertices 0, 1, and 2 monsters");
    test.getCow(0).becomeMonster();
    test.getCow(1).becomeMonster();
    test.getCow(2).becomeMonster();
    System.out.println("Testing getCowsAdjacentToMonsters() [3,4,5,6,7,8,9]: " + test.getCowsAdjacentToMonsters());
    System.out.println("Testing cowsAdjacentTo(23) (only gets the visible cows) [16]: " + test.cowsAdjacentTo(23));
    System.out.println("Testing getLivingMonsters() [0,1,2]: " + test.getLivingMonsters());
    System.out.println("Testing getAllLivingCows() [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17]: " 
                         + test.getAllLivingCows());
    System.out.println("Testing getCowsRemaining() [15]: " + test.getCowsRemaining());
    System.out.println("Testing getAllEmptySpaces() [18,19,20,21,22,23]: " + test.getAllEmptySpaces());
    System.out.println("Testing moveCow() by moving cow at index 10 to index 23");
    String message1 = test.moveCow(10, 23);
    System.out.println(message1);
    System.out.println(test);
    System.out.println("Try to move a cow to a location where a cow already exists by moving cow at index 5 to index 6");
    String message2= test.moveCow(5, 6);
    System.out.println(message2);
    System.out.println(test);
    System.out.println("Testing getIndexWithName(\"10\") to see where cow 10 is currently [23]: " 
                         + test.getIndexWithName("10"));
    System.out.println("Testing randomlyMoveCow() by moving cow 9");
    //look at the graph to see that cow 9 has moved
    //can run test many times to see it moves to a different place each time
    test.randomlyMoveCow(9);
    System.out.println(test);
    System.out.println("Testing resetBoard()");
    test.resetBoard();
    System.out.println(test);
  }
  
}