/**
 * FILE NAME: Game.java
 * WHO: Leah Ferguson and Ashley Thomas
 * WHAT: This class implements a Game, which has all of the methods for controlling the sequence of the game by using the 
 * methods from ComputerPlayer and GameBoard and changing the mode of the game when appropriate. Also reads high scores
 * in from a file and stores them in a LinkedList. At the end of the game updates this text file.
 * The sequence of a game is as follows in each round:
 *   1)farmer randomly move cows
 *   2)monster chooses 3 monsters
 *   3)monster kills cow
 *   4)monster moves scared cows
 *   5)farmer guesses monster (guesses again if right)
 *  repeat steps 3-5
 *  game ends when either all cows or all monsters are dead
 */ 

import java.util.*;
import java.io.*;

public class Game {
  
  private GameBoard gameBoard;
  private ComputerPlayer computer;
  private Cow recentlyKilledCow;  //the cow object that was most recently killed
  private LinkedList<Integer> recentlyScaredCows;  //the indices of the most recently scared cows
  private int round;  //indicates first or second round
  private String mode;  //indicates which part of the game is happening currently
  private String[] possibleModes;
  private LinkedList<LinkedList<String>> listOfScaredCows;  //keeps track of how many times each cow has been scared
  private int computerScore;
  private int playerScore;
  private LinkedList<Player> scores;  //holds a list of players iw
  private String name;  //the name the user inputs
  
  /**Constructor
    *Creates a new gameboard and populates its vertices with cows.
    */
  public Game () {
    gameBoard = new GameBoard();
    possibleModes = new String[]{"computerMovesCows", "userChoosesMonsters", "userKillsCow", "userMovesScaredCows", "computerGuessesMonster",
      "userMovesCows", "computerChoosesMonsters", "computerKillsCow", "computerMovesScaredCows", "userGuessesMonster", "gameOver", "round1Over"};
    mode = possibleModes[0];
    listOfScaredCows = new LinkedList<LinkedList<String>>();
    listOfScaredCows.add(0, new LinkedList<String>());
    //all cows have been scared 0 times, so add them to list 0
    for (int i = 0; i <= 17; i++)
      listOfScaredCows.get(0).add(Integer.toString(i));
    computer = new ComputerPlayer(gameBoard);
    recentlyKilledCow = null;
    recentlyScaredCows = new LinkedList<Integer>();
    computerScore = 0;
    playerScore = 0;
    round = 0;
    name = "";
    scores = new LinkedList<Player>();
    //create list of high scores
    try {
      Scanner scan = new Scanner(new File("highScores.txt"));
      while (scan.hasNextLine()) { //While there are still vertices to be made, they are created
        String nextLine = scan.nextLine();
        String[] splitLine = nextLine.split("#");
        String theName = splitLine[0];
        int theScore = Integer.parseInt(splitLine[1]);
        Player player = new Player(theName, theScore);
        scores.add(player);
      }
      scan.close();
    }
    catch (IOException ex) {
      System.out.println(" ***(T)ERROR*** The file was not found: " + ex);
    }
  }
  
  /**
    * JEF randomly moves cows at the beginning of the game; changes the mode to userChoosesMonsters.
    * @return Returns a string indicating the result of JEF having moved cows.
    */
  public String computerMovesCows(){
    String message = computer.randomlyMoveCows();
    mode = possibleModes[1]; //userChoosesMonsters
    return (message + "Please choose your monsters now.");
  }
  
  /**
    * JEF randomly moves cows at the beginning of the game; does not change the mode because another method
    * is provided to check whether the user is done choosing monsters.
    * @param index The index of the chosen cow.
    * @return Returns a string indicating the result of the player's move.
    */  
  public String userChoosesMonster(int index){
    LinkedList<Integer> livingMonsters = gameBoard.getLivingMonsters();
    if (livingMonsters.size() == 3 && !livingMonsters.contains(index))
      return "You have already chosen three monsters. Press a monster to undo your choice.";
    Cow cow = getCow(index);
    if (!cow.isVisible()){
      return "You cannot make an empty space a monster.";
    }
    else if (!cow.isMonster()) {
      cow.becomeMonster();
      return "The cow you chose is now a monster.";
    }
    else {
      cow.becomeCow();
      return "The monster you chose is now a cow.";
    }
  }
  
  /**
    * Changes the mode to userKillsCow.
    * @return Returns a string indicating the result.
    */
  public String userDoneChoosingMonsters(){
    mode = possibleModes[2]; //userKillsCow
    return "You have now chosen all your monsters. Time to kill your first cow!";
  }
  
  /**
    * Kills a selected cow, if it is allowed to be killed, makes the surrounding cows scared and
    * updates the list of scared cows; If successful, changes the mode to 
    * userMovesScaredCows, and if the round is over, changes the mode to round1Over instead.
    * @param index The index of the selected cow.
    * @return Returns a string indicating the result of the player's move.
    */  
  public String userKillsCow(int index){
    Cow cow = getCow(index);
    if (cow.isDead()){
      return "You cannot kill a dead cow! Please try again.";
    }
    else if (!cow.isVisible()) {
      return "You cannot kill a cow that does not exist! Please try again.";
    }
    else if (cow.isMonster()){
      return "You cannot kill your own monster! Please try again.";
    }
    else {
      LinkedList<Integer> killableCows = gameBoard.getCowsAdjacentToMonsters();
      if(killableCows.contains(index)){
        cow.die();
        recentlyKilledCow = cow;
        listOfScaredCows.get(cow.getTimesScared()).remove(cow.getName());
        recentlyScaredCows = gameBoard.cowsAdjacentTo(index);
        for (Integer i: recentlyScaredCows) {
          getCow(i).becomeScared();
        }
        updateListOfScaredCows();
        if (isGameOver()){
          computerScore = gameBoard.getCowsRemaining();
          mode = possibleModes[11];   //round1Over
          return "The last cow was killed, you won! On to the next round.";
        }
        else{
          mode = possibleModes[3]; //userMovesScaredCows
          return "The cow was successfully killed.";
        }
      }
      else
        return "This cow is not next to a monster. Please try again.";
    }
  }
  
  /**
   * Uses the gameBoard method to get all the cows adjacent to the monsters.
   * @return Returns a LinkedList<Integer> containing the cows adjacent to the monsters.
   */  
  public LinkedList<Integer> getCowsAdjacentToMonsters(){
    return gameBoard.getCowsAdjacentToMonsters();
  }
  
  /**
   * Given the index of the current cow and the index to move the cow to, moves a scared cow and makes
   * the cow no longer scared; when all of the scared cows have been moved, changes the 
   * mode to computerGuessesMonster.
   * @return Returns a String indicating the result of the player's move.
   * @param index1 The current index of the cow to move.
   * @param index2 The location to move the cow to.
   */
  public String userMovesScaredCow(int index1, int index2){
    String result = moveCow(index1, index2);
    if (result.equals("You successfully moved your cow.")){
      getCow(index2).becomeNotScared();
      recentlyScaredCows.remove((Integer) index1);
      if (recentlyScaredCows.isEmpty()){
        mode = possibleModes[4]; //computerGuessesMonster
        return "You have moved all of the scared cows. Now JEF will guess.";
      }
      else
        return "You have moved a scared cow.";
    }
    else {
      return result;
    }
  }
  
  /**
   * Uses the computerPlayer method guessMonster to kill a cow that has been scared the most number of times, and
   * if the round is over, changes the mode to round1Over; if the cow guessed was not a monster, changes the 
   * mode to userKillsCow; if JEF was correct or there are no more moves, he guesses again; also updates the
   * computer score.
   * @return Returns a String indicating the result of the move.
   */  
  public String computerGuessesMonster() {
    Cow cow = getCow(computer.guessMonster(listOfScaredCows));
    listOfScaredCows.get(cow.getTimesScared()).remove(cow.getName());
    if (getCowsAdjacentToMonsters().isEmpty()){
      if (isGameOver()) {
        computerScore = gameBoard.getCowsRemaining();
        mode = possibleModes[11];  //round1Over
        if (getLivingMonsters().isEmpty()){
          return "JEF guessed all the monsters. Game Over. YOU LOSE! On to the next round.";
        }
        else {
          return "You won! All of the cows are dead. On to the next round.";
        }
      }
      else{
        return "You cannot kill any more cows because there are no cows next to your monsters. JEF will guess again.";
      }
    }
    if (cow.isMonster()) {
      if (isGameOver()) {
        mode = possibleModes[11];  //round1Over
        computerScore = gameBoard.getCowsRemaining();
        if (getLivingMonsters().isEmpty()){
          return "JEF guessed all the monsters. Game Over. YOU LOSE! On to the next round.";
        }
        else {
          return "You won! All of the cows are dead. On to the next round.";
        }
      }
      else
        return "The cow JEF guessed was a monster. JEF will guess again.";
    }
    else {
      mode = possibleModes[2]; //userKillsCow
      return "The cow JEF guessed was not a monster. Your turn to choose another cow to kill!";
    }
  }
  
  /**
   * Calls the moveCow method to move a cow; does not change the mode because there is another method to do that.
   * @param index1 The index of the cow to be moved.
   * @param index2 The index of the location to move it to.
   * @return Returns a String indicating the result of the move.
   */    
  public String userMovesCow(int index1, int index2){
    return moveCow(index1, index2);
  }
  
  /**
   * Changes the mode to computerChoosesMonsters.
   * @return Returns a String indicating the result of the move.
   */    
  public String userDoneMovingCows(){
    mode = possibleModes[6]; //computerChoosesMonsters
    return "Now JEF will choose its monsters.";
  }
  
  /**
   * Calls the ComputerPlayer method chooseMonsters to choose a monster, and changes the mode to computerKillsCow.
   * @return Returns a String indicating the result of the move.
   */    
  public String computerChoosesMonsters() {
    String result = computer.chooseMonsters();
    mode = possibleModes[7]; //computerKillsCow
    return result;
  }
  
  /**
   * Calls the ComputerPlayer method killCow to kill a cow and updates the information about the scared cows; if the
   * game is over, changes the mode to gameOver and update the scores, otherwise updates the mode 
   * to computerMovesScaredCows.
   * @return Returns a String indicating the result of the move.
   */    
  public String computerKillsCow(){
    int index = computer.killCow();
    Cow cow = getCow(index);
    recentlyKilledCow = cow;
    listOfScaredCows.get(cow.getTimesScared()).remove(cow.getName());
    recentlyScaredCows = gameBoard.cowsAdjacentTo(index);
    for (Integer i: recentlyScaredCows) {
      getCow(i).becomeScared();
    }
    updateListOfScaredCows();
    if (isGameOver()){
      updateScores();
      mode = possibleModes[10]; //gameOver
      return "JEF killed the last cow. Game over!";
    }
    else{
      mode = possibleModes[8]; //computerMovesScaredCows
      return "JEF killed a cow.";
    }
  }
  
  /**
   * Calls the ComputerPlayer method moveScaredCow to move a scared cow, and updates this cow to no longer be scared.;
   * if all of the scared cows have been moved, change the mode to userGuessesMonster.
   * @return Returns a String indicating the result of the move.
   */    
  public String computerMovesScaredCows() {
    Integer index = recentlyScaredCows.remove(0);
    getCow(index).becomeNotScared();
    computer.moveScaredCow(index);
    if (!recentlyScaredCows.isEmpty()){
      return "JEF has moved a scared cow.";
    }
    else {
      mode = possibleModes[9]; //userGuessesMonster
      return "JEF has moved the scared cows. Now it is your turn to guess which cows are monsters.";
    }
  }
  
  /**
   * If the cow at the given index is allowed to be guessed (it is not dead or invisible) then it is killed, and if
   * the game is over, change the mode to gameOver and update the scores; if the cow was a monster or if JEF has 
   * no more moves the mode stays the same, but if the cow was not a monster the mode changes to computerKillsCow.
   * @param index The index of the cow to be guessed.
   * @return Returns a String indicating the result of the move.
   */
  public String userGuessesMonster(int index) {
    Cow cow = getCow(index);
    if (cow.isDead()){
      return "This cow is already dead! Please try again.";
    }else if(!cow.isVisible()){
      return "You cannot kill a cow that does not exist! Please try again.";
    }else{
      cow.die();
      listOfScaredCows.get(cow.getTimesScared()).remove(cow.getName());
      if (cow.isMonster()) {
        if (isGameOver()){
          updateScores();
          mode = possibleModes[10]; //gameOver
          return "You killed the last monster, you won!";
        }
        else {
          return "The cow you guessed was a monster. Guess again!";
        }
      }
      if (getCowsAdjacentToMonsters().isEmpty()){
        return "The cow you guessed was not a monster, but JEF cannot make a move, so guess again.";
      }
      else{
        mode = possibleModes[7]; //computerKillsCow
        return "The cow you guessed was not a monster. Now JEF will kill a cow again.";
      }
    }
  }
  
  /**
   * Updates the list keeping track of how many times each cow has been scared.
   */
  private void updateListOfScaredCows() {
    LinkedList<Integer> scaredCows = recentlyScaredCows;
    for (int i = 0; i < scaredCows.size(); i++) {
      int numTimesScared = getCow(scaredCows.get(i)).getTimesScared();
      while (listOfScaredCows.size() <= numTimesScared) {
        listOfScaredCows.add(new LinkedList<String>());
      }
      listOfScaredCows.get(numTimesScared - 1).remove(getCow(scaredCows.get(i)).getName());
      listOfScaredCows.get(numTimesScared).add(getCow(scaredCows.get(i)).getName());
    }
    listOfScaredCows.get(recentlyKilledCow.getTimesScared()).remove(recentlyKilledCow.getName());
  }
  
  /**
   * Returns the list keeping track of how many times each cow has been scared.
   * @return Returns the LinkedList<LinkedList<String>> keeping track of how many times each cow has been scared.
   */
  public LinkedList<LinkedList<String>> getListOfScaredCows() {
    return listOfScaredCows;
  }
  
  /**
   * Calls the GameBoard getCow method to return a cow object given an index representing its location.
   * @param index The index of the cow to return.
   * @return Returns a cow object.
   */
  public Cow getCow(int index){
    return gameBoard.getCow(index);
  }
  
  /**
   * Calls the GameBoard moveCow method to move a cow, given the index of the cow and the 
   * index of its desired location.
   * @param index1 The index of the cow to be moved.
   * @param index2 The index of the location to move the cow to.
   * @return Returns a message indicating the result of the move.
   */
  public String moveCow(int index1, int index2){
    return gameBoard.moveCow(index1, index2);
  }
  
  /**
   * Returns a String indicating the current mode the game is in.
   * @return Returns a String indicating the current mode.
   */
  public String getMode(){
    return mode;
  }
  
  /**
   * If there are no more living monsters or not more living cows, returns true.
   * @return Returns a boolean indicating whether the game is over or not.
   */  
  public boolean isGameOver(){
    int numLivingMonsters = gameBoard.getLivingMonsters().size();
    if (numLivingMonsters == 0 || gameBoard.getCowsRemaining() == 0)
      return true;
    return false;
  }
  
  /**
   * Returns playerScore - computerScore, which is the final score of the game.
   * @return Returns an int indicating the final score.
   */
  public int getFinalScore(){
    int playerScore = gameBoard.getCowsRemaining();
    return playerScore - computerScore;
  }
  
  /**
   * Updates the list of high scores and calls saveScores to save the updated list to the external txt file; only
   * allows the players who have the top 10 scores to remain in this list.
   */  
  public void updateScores(){
    Integer score = getFinalScore();
    Player player = new Player(name, score);
    if (scores.size() == 0){
      scores.add(player);
    }
    else if (scores.getLast().compareTo(player) >= 0) {
      scores.add(player);
    }
    else {
      for (int i = 0; i < scores.size(); i++) { 
        Player currentPlayer = scores.get(i); 
        if (player.compareTo(currentPlayer) > 0){
          scores.add(i, player);
          break;
        }
      }
    }
    if (scores.size() > 10) {
      scores.removeLast();
    }
    saveScores("highScores.txt");
  }
  
  /**
   * Saves the list of high scores to a file with the specified name.
   * @param fileName The name of the file to save the scores to.
   */    
  public void saveScores(String fileName) {
    try {
      PrintWriter writer = new PrintWriter(new File(fileName));
      for (int i = 0; i < scores.size(); i++){
        writer.print(scores.get(i).getName() + "#" + scores.get(i).getScore() + "\n");
      }
      writer.close();
    }
    catch (IOException ex) {
      System.out.println("***ERROR*** The file could not be written: " + ex);
    }
  }
  
  /**
   * Returns a String containing the current scores of JEF and the player.
   * @return Returns a String indicating the current scores
   */     
  public String getCurrentScores(){
    String result = "<html>Cows Remaining<br>";
    if (round == 0){
    computerScore = gameBoard.getCowsRemaining();
    }
    else {
      playerScore = gameBoard.getCowsRemaining();
    }
    result += "JEF: " + computerScore + "<br>" + name + ": " + playerScore + "<br></html>";
    return result;
  }
  
  /**
   * Returns a String containing the list of the top 10 scores.
   * @return Returns a String containing the list of high scores.
   */     
  public String getHighScores(){
    String result = "<html>"; 
    for (int i = 0; i < scores.size(); i++){
      result += Integer.toString(i+1) + ". " + scores.get(i).getName() + ": " + scores.get(i).getScore() + "<br>";
    }
    result += "<br><br></html>";
    return result;
  }
  
  /**
   * Calls the getLivingMonsters method in GameBoard to get a list of the indices of all living monsters.
   * @return Returns a LinkedList<Integer> containing the indices of the living monsters.
   */   
  public LinkedList<Integer> getLivingMonsters(){
    return gameBoard.getLivingMonsters();
  }
  
  /**
   * Resets the game (except for the mode and round and computerScore) to prepare for the second round. 
   */     
  public void resetGame(){
    gameBoard.resetBoard();
    listOfScaredCows = new LinkedList<LinkedList<String>>();
    listOfScaredCows.add(0, new LinkedList<String>());
    for (int i = 0; i <= 17; i++)
      listOfScaredCows.get(0).add(Integer.toString(i));
    recentlyKilledCow = null;
    recentlyScaredCows = new LinkedList<Integer>();
    mode = possibleModes[5];
    round = 1;
  }
  
  /**
   * Resets the entire game to prepare for a new game.
   */       
  public void newGame(){
    gameBoard.resetBoard();
    listOfScaredCows = new LinkedList<LinkedList<String>>();
    listOfScaredCows.add(0, new LinkedList<String>());
    for (int i = 0; i <= 17; i++)
      listOfScaredCows.get(0).add(Integer.toString(i));
    recentlyKilledCow = null;
    recentlyScaredCows = new LinkedList<Integer>();
    computerScore = 0;
    playerScore = 0;
    mode = possibleModes[0];
    round = 0;
  }
  
  /**
   * Sets the name of the player to be the given name.
   * @param name The name we wish the player to have.
   */       
  public void setName(String name){
    this.name = name;
  }
  
  /**
   * Returns the GameBoard object in Game.
   * @return Returns the gameBoard.
   */
  public GameBoard getGameBoard(){
   return gameBoard; 
  }
 
  
  /**
   * Calls the GameBoard toString method to print the gameBoard.
   * @return Returns a string representation of the gameBoard.
   */   
  public String toString(){
   return gameBoard.toString();
  }
  
  /**
   * Main method for local testing
   */
  public static void main (String[] args) {
    Game game = new Game();
    System.out.println(game);
    System.out.println("Testing computerMovesCows");
    game.computerMovesCows();
    System.out.println("Current mode [userChoosesMonsters]: " + game.getMode());
    System.out.println(game);
    System.out.println("Testing userChoosesMonster, make the cow at 0 a monster");
    game.userChoosesMonster(0);
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isMonster()){
        System.out.println("The cow at index " + i + " is now a monster");
      }
    }
    
    System.out.println("Testing userDoneChoosingMonsters");
    game.userDoneChoosingMonsters();
    System.out.println("Current mode [userKillsCow]: " + game.getMode());
    
    System.out.println("\nTesting userKillsCow by killing the cow at index 2");
    String message1 = game.userKillsCow(2);
    System.out.println(message1);
    System.out.println("The cow at index 2 is dead [false]: " + game.getCow(2).isDead());
    
    System.out.println("Testing userKillsCow by killing the cow at index 1");
    String message2 = game.userKillsCow(1);
    System.out.println(message2);
    System.out.println("The cow at index 1 is dead [true]: " + game.getCow(1).isDead());
    System.out.println("\nTesting userMovesScaredCow by moving the cow at index 2 to index 23");
    game.userMovesScaredCow(2, 23);
    System.out.println(game);
    System.out.println("Testing computerGuessesMonster");
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isDead()){
        System.out.println("The cow at index " + i + " is already dead");
      }
    }
    System.out.println("Computer guesses a monster");
    game.computerGuessesMonster();
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isDead()){
        System.out.println("The cow at index " + i + " is now dead");
      }
    }
    System.out.println("\nTesting userDoneMovingCows");
    game.userDoneMovingCows();
    System.out.println("Current mode [computerChoosesMonsters]: " + game.getMode());
    System.out.println("\nTesting computerChoosesMonsters");
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isMonster()){
        System.out.println("The cow at index " + i + " is already a monster");
      }
    }
    System.out.println("computerChoosesMonsters()");
    game.computerChoosesMonsters();
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isMonster()){
        System.out.println("The cow at index " + i + " is now a monster");
      }
    }
    System.out.println("Current mode [computerKillsCow]: " + game.getMode());
    System.out.println("\nTesting computerKillsCow");
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isDead()){
        System.out.println("The cow at index " + i + " is already dead");
      }
    }
    System.out.println("computerKillsCow()");
    game.computerKillsCow();
    for (int i=0; i<=23; i++){
      if (game.getGameBoard().getCow(i).isDead()){
        System.out.println("The cow at index " + i + " is now dead");
      }
    }
    System.out.println("Current mode [computerMovesScaredCows]: " + game.getMode());
    System.out.println("\nTesting computerMovesScaredCows");
    System.out.println(game);
    System.out.println("computerMovesScaredCows()");
    game.computerMovesScaredCows();
    System.out.println("One scared cow has been moved.");
    System.out.println(game);
    System.out.println("\nTesting userGuessesMonster by guessing the cow at index 15");
    String message5 = game.userGuessesMonster(15);
    System.out.println(message5);
    System.out.println("Current mode : " + game.getMode());
    System.out.println("\nTesting getListOfScaredCows");
    System.out.println("List of scared Cows: " + game.getListOfScaredCows());
    System.out.println("\nCurrent scores: " + game.getCurrentScores());
    System.out.println("\nHigh Scores: " + game.getHighScores());
    System.out.println("\nReset Game");
    game.resetGame();
    System.out.println("Current scores: " + game.getCurrentScores());
    System.out.println(game);
    System.out.println("\nNew Game");
    game.newGame();
    System.out.println("Current scores: " + game.getCurrentScores());
    System.out.println(game);
  }
}
