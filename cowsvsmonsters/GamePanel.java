 /** FILE NAME: GamePanel.java
 * WHO: Leah Ferguson and Ashley Thomas
 * WHAT: This class makes the GUI interface, and runs the game by checking if conditions are met in the game (such as
 * the mode or the messages that are printed out). It has panels for Introduction, Rules, Game, and High Scores.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.*;

public class GamePanel extends JPanel {
  
  //instance variables
  JTabbedPane tabbedPane;
  private Game game;

  //for Introduction Tab
  private JButton toRulesTab;
  private JButton quitGame0;
  
  //for Rules Tab
  private JButton toIntroductionTab;
  private JButton toGameTab;
  private JButton quitGame1;
  
  //for Game panel
  private JLabel gameInfo;
  private JLabel score;
  private JLabel monsters;
  private JButton userDoneChoosingMonsters;
  private JButton startGame;
  private JButton startNextRound;
  private JButton doneMovingCows;
  private JButton newGame;
  private JButton quitGame2;
  private javax.swing.Timer movingScaredCowsTimer;               ///////////////HERE
  private javax.swing.Timer killCowTimer;
  private javax.swing.Timer guessingMonsterTimer;
  
  //private JPanel cowPanel;
  JPanel[] cows = new JPanel[24];
  JLabel[] cowLabels = new JLabel[24];
  
  //for high scores panel
  private JButton backToGame;
  private JLabel highScoresLabel;
  private JButton quitGame3;
  
  //all images
  ImageIcon cow = new ImageIcon("Images/cowwithbackground.gif");
  ImageIcon monster = new ImageIcon("Images/monsterwithbackground.gif");
  ImageIcon circle = new ImageIcon("Images/circle.png");
  ImageIcon zeromdead = new ImageIcon("Images/0monstersdead.gif");
  ImageIcon onemdead = new ImageIcon("Images/1monsterdead.gif");
  ImageIcon twomdead = new ImageIcon("Images/2monstersdead.gif");
  ImageIcon threemdead = new ImageIcon("Images/3monstersdead.gif");
  ImageIcon killableCow = new ImageIcon("Images/redcow.png");
  ImageIcon scaredCow = new ImageIcon("Images/scaredcow.png");
  ImageIcon highlightedCow = new ImageIcon("Images/bluecow1.png");
  ImageIcon highlightedScaredCow = new ImageIcon("Images/bluescaredcow.png");
  ImageIcon scaredMonster = new ImageIcon("Images/purplemonster1.png");
  ImageIcon highlightedMonster = new ImageIcon("Images/bluemonster1.png");
  ImageIcon deadMonster = new ImageIcon("Images/deadmonster.png"); 
  ImageIcon deadCow = new ImageIcon("Images/deadcow.png");
  ImageIcon introcowpic = new ImageIcon("Images/cow.jpg");
  ImageIcon icons = new ImageIcon("Images/icons.png");
  
    /**
   * Makes the entire Game Panel, which is a tabbed pane
   * @param p Takes in a Game object
   */
  public GamePanel(Game p){
    this.game = p;
    setLayout(new BorderLayout());
    
    tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Serif", Font.BOLD, 14));
    
    //Make the three tabs and add them to panel
    tabbedPane.addTab("Introduction", makeIntroductionPanel());
    tabbedPane.addTab("Rules", makeRulesPanel());
    tabbedPane.addTab("Game", makeGamePanel());
    tabbedPane.addTab("High Scores", makeHighScoresPanel());
    tabbedPane.setPreferredSize(new Dimension (850,475));
    setPreferredSize (new Dimension(1000,700));
    add(tabbedPane);
  }
  
    /**
   * Makes the Introduction Panel, which is the first tab
   * @return Returns the Introduction Panel
   */
  private JPanel makeIntroductionPanel(){
    JPanel introductionPanel = new JPanel();
    introductionPanel.setLayout(new BorderLayout());
    JPanel introButtonPanel = new JPanel();
    introductionPanel.setBackground(new Color(240,221,182));
    introButtonPanel.setBackground(new Color(240,221,182));
    
    //make, initialize elements for this panel
    JLabel introductionInfo = new JLabel();
    toRulesTab = new JButton("Next");
    quitGame0 = new JButton("Quit Game");
    introButtonPanel.add(toRulesTab);
    introButtonPanel.add(quitGame0);

    introductionInfo.setText("<html>Cows vs. Monsters<br><br>You are a farmer with a wonderful herd of cows. Suddenly, a lightning beam strikes 3 cows, "+
                               "making them monsters! You must kill them before they kill the others. Will you accept your mission?" +
                             "-----------------------------------------------------------------------<br>" +
                             "There are two rounds to this game. In the first round, you choose three cows to be monsters. " +
                               "On each turn, you can kill any cow that is next to any of your monsters. Then you move the cows " + 
                             "that were next to the killed cow to any location that is empty. Jeeves Elvis Fergumas (our computer's name! nickname: JEF) (farmer) then guesses which of the " +
                               "cows are monsters by killing one of them. If JEF is right, he has the choice to guess again (by killing another monster). " +
                             "The round repeats in this fashion until you kill all of the other cows, or until JEF guesses all your monsters. " +
                             "In the second round, the roles are switched as you try to guess JEF's monsters before all the other cows die. " +
                               "At the beginning of every game, you will be prompted for your name. This information will be stored, and then your score will be recorded " +
                             "at the end of the game. A box will appear asking if you wish to see the High Scores tab.<br>" +
                             "-----------------------------------------------------------------------<br>Fergumas Industries<br><br>" +
                             "<img src=\"http://resources3.news.com.au/images/2012/03/27/1226311/607271-cow.jpg\" alt=\"Cow\" height=\"250\" width=\"350\">" +
                             "</html>");
    
    //Add elements to this panel
    introductionPanel.add(introductionInfo, BorderLayout.CENTER);
    introductionPanel.add(introButtonPanel, BorderLayout.SOUTH);
    JLabel left = new JLabel();
    left.setText("                                ");
    JLabel right = new JLabel();
    right.setText("                                ");
    introductionPanel.add(left, BorderLayout.WEST);
    introductionPanel.add(right, BorderLayout.EAST);
    
    //Add all the listeners
    ButtonListener blistener = new ButtonListener();
    toRulesTab.addActionListener(blistener);
    quitGame0.addActionListener(blistener);
    
    return introductionPanel;
  }
  
    /**
   * Makes the Rules Panel, which is the second panel
   * @return Returns the Rules Panel
   */
  private JPanel makeRulesPanel(){
    JPanel rulesPanel = new JPanel();
    JPanel rulesButtonPanel = new JPanel();
    toIntroductionTab = new JButton("Back to Introduction");
    toGameTab = new JButton("Play Game");
    quitGame1 = new JButton("Quit Game");
    JLabel theIcons = new JLabel();
    JLabel rulesInfo = new JLabel();
    rulesInfo.setText("<html>" + 
                      "Round One:<br>" +
                      "1. Start the round by pressing the Start Button and enter your name.<br>" +
                      "2. Select 3 cows to be monsters by clicking on any cow. You can unselect your choice by clicking the monster again. Once you have selected 3 monsters, press the Done Choosing Monsters? button.<br>" +
                      "3. Now kill a cow that is adjacent to any of your monsters (highlighted in red) by clicking on it.<br>" +
                      "4. Move all the cows that are adjacent to the cow you just killed (highlighted in purple) by clicking it and then clicking an empty spot you wish to move it to. Any cow you click will turn blue to remind you of your choice. You must move all of the scared cows before you can continue.<br>" +
                      "5. Now JEF will guess which of the cows are monsters by killing one. If he's right, he can guess again. Otherwise, kill another cow! Remember, if none of your monsters are adjacent to any cows, you will have to skip your turn and let JEF guess again. The round continues until you have either killed all the cows or JEF has guessed all your monsters.<br>" +
                      "Round Two:<br>"+
                      "6. Press the Start Next Round Button.<br>" +
                      "7. Move cows around to confuse JEF and make for a more strategic game. Move cows the same way you moved scared cows in the first round. When you're done, press the Done Moving Cows? button.<br>" +
                      "8. Now JEF will choose his monsters. You won't be able to see them, because you're the farmer this round! Then he'll kill his first cow and move the scared cows around.<br>" +
                      "9. Now it's your turn to guess! Click on a cow you think is a monster to kill it. If you're right, the cow will turn into a dead monster icon, and if not, it will just become a dead cow. If you guess right, you get to guess again.<br>" +
                      "10. The game ends when you have killed all the monsters or all your cows are dead.<br>" +
                      "<br>If you ever get stuck, look at the message at the top of the screen (in the purple bar)! They'll help you out. If you're ready to play, click Play Game!<br></html>");
    theIcons.setIcon(icons);
    
    rulesPanel.setLayout(new BorderLayout());
    rulesPanel.setBackground(new Color(179,138,194));
    rulesButtonPanel.setBackground(new Color(179,138,194));
    //Add elements to this panel
    rulesPanel.add(rulesInfo, BorderLayout.NORTH);
    rulesPanel.add(theIcons, BorderLayout.CENTER);
    rulesPanel.add(rulesButtonPanel, BorderLayout.SOUTH);
    rulesButtonPanel.add(toIntroductionTab);
    rulesButtonPanel.add(toGameTab);
    rulesButtonPanel.add(quitGame1);
    JLabel left = new JLabel();
    left.setText("                                                                                ");
    rulesPanel.add(left, BorderLayout.WEST);

    //Add all the listeners
    ButtonListener blistener = new ButtonListener();
    toIntroductionTab.addActionListener(blistener);
    toGameTab.addActionListener(blistener);
    quitGame1.addActionListener(blistener);
    
    return rulesPanel;
  }
  
    /**
   * Makes the Game Panel, which is the third panel
   * @return Returns the Game Panel
   */
  private JPanel makeGamePanel(){
    JPanel gamePanel = new JPanel();
    gamePanel.setLayout(new BorderLayout());
    gamePanel.setBackground(new Color(179,138,194));
    
    JPanel scorePanel = new JPanel();
    scorePanel.setBackground(new Color(240,221,182));
    scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(240,221,182));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    JPanel buttonPanel1 = new JPanel();
    buttonPanel1.setBackground(new Color(240,221,182));
    buttonPanel1.setLayout(new BoxLayout(buttonPanel1, BoxLayout.Y_AXIS));
    
    //adds buttons and sets their visibility to false (except startGame and quitGame). They become visible only when the 
    //user needs them to streamline the gaming experience
    userDoneChoosingMonsters = new JButton("Done Choosing Monsters?");
    userDoneChoosingMonsters.setVisible(false);
    startGame = new JButton("Start Game");
    startNextRound = new JButton("Start Next Round");
    startNextRound.setVisible(false);
    doneMovingCows = new JButton("Done Moving Cows?");
    doneMovingCows.setVisible(false);
    newGame = new JButton("New Game");
    newGame.setVisible(false);
    quitGame2 = new JButton("Quit Game");
    
    monsters = new JLabel();
    monsters.setIcon(zeromdead);
    
    score = new JLabel("Cows Remaining:");
    
    //add elements to scorePanel
    scorePanel.add(buttonPanel1);
    buttonPanel1.setPreferredSize(new Dimension(50, 150));
    scorePanel.add(Box.createRigidArea(new Dimension(0,50)));
    scorePanel.add(monsters);
    scorePanel.add(Box.createRigidArea(new Dimension(0,50)));
    scorePanel.add(buttonPanel);
    buttonPanel.setPreferredSize(new Dimension(50, 200));
    scorePanel.add(Box.createRigidArea(new Dimension(0,50)));
    scorePanel.add(score);
    buttonPanel.add(userDoneChoosingMonsters);
    buttonPanel.add(startGame);
    buttonPanel.add(startNextRound);
    buttonPanel.add(doneMovingCows);
    scorePanel.add(Box.createRigidArea(new Dimension(0,50)));
    buttonPanel1.add(newGame);
    buttonPanel1.add(quitGame2);
    
    gameInfo = new JLabel("<html>This is where you will see updates as the game progresses.</html>", SwingConstants.CENTER);
    
    gameInfo.setFont(gameInfo.getFont().deriveFont(20.0f));
    score.setFont(score.getFont().deriveFont(20.0f));
    
    //Add the elements to this panel
    gamePanel.add(makeGameBoardPanel(), BorderLayout.CENTER);
    gamePanel.add(scorePanel, BorderLayout.WEST);
    gamePanel.add(gameInfo, BorderLayout.NORTH);
    
    //Add all the listeners
    ButtonListener blistener = new ButtonListener();
    userDoneChoosingMonsters.addActionListener(blistener);
    startGame.addActionListener(blistener);
    startNextRound.addActionListener(blistener);
    doneMovingCows.addActionListener(blistener);
    newGame.addActionListener(blistener);
    quitGame2.addActionListener(blistener);
    
    return gamePanel;
  }
  
    /**
   * Makes the GameBoard panel, which is essentially our game board
   * @return Returns the GameBoard panel, which will be in the Game Panel
   */
  private JPanel makeGameBoardPanel(){
    JPanel gameBoardPanel = new BackgroundPanel();
    gameBoardPanel.setBackground(new Color(240,221,182));
    gameBoardPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints(); 
    
    //Timers
    ButtonListener timerListener = new ButtonListener();
    movingScaredCowsTimer = new javax.swing.Timer(1000, timerListener);  ///////////////HERE
    killCowTimer = new javax.swing.Timer(1500, timerListener);
    guessingMonsterTimer = new javax.swing.Timer(1500, timerListener);
    
    //add the cowpanels to the gridBag layout to make the gameboard
    int count = 0;
    CowListener clistener = new CowListener();
    for (int i = 0; i <= 6; i++) {
      for (int j = 0; j <= 6; j++) {
        if ((i + j)%2 != 0){
          cows[count] = new JPanel();
          cows[count].setBackground(new Color(0,0,0,0));
          cowLabels[count] = new JLabel();
          cows[count].addMouseListener(clistener);
          c.gridx = j;
          c.gridy = i;
          gameBoardPanel.add(cows[count], c);
          if (game.getCow(count).isVisible()){
            cowLabels[count].setIcon(cow);
            cows[count].add(cowLabels[count]);
          }
          else{
            cowLabels[count].setIcon(circle);
            cows[count].add(cowLabels[count]);
          }
          count++;
        }
      }
    }
    return gameBoardPanel;
  }
  
    /**
     * Makes the High Scores panel, which is the fourth panel
   * @return Returns the HighScores panel
   */
  private JPanel makeHighScoresPanel() {
    
    JPanel highScoresPanel = new JPanel();
    highScoresPanel.setBackground(new Color(175, 187, 240));
    highScoresPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints(); 
    
    //create elements
    highScoresLabel = new JLabel("");
    backToGame = new JButton("Back to Game");
    quitGame3 = new JButton("Quit Game");
    
    //add elements
    c.gridy = 0;
    highScoresPanel.add(highScoresLabel, c);
    c.gridy = 1;
    highScoresPanel.add(backToGame,c);
    c.gridy = 2;
    highScoresPanel.add(quitGame3,c);
    
    highScoresLabel.setText(game.getHighScores());  //gets the high scores from the game object
    highScoresLabel.setFont(score.getFont().deriveFont(25.0f));
    
    //add listeners
    ButtonListener blistener = new ButtonListener();
    backToGame.addActionListener(blistener);
    quitGame3.addActionListener(blistener);
    
    return highScoresPanel;
  }

    /**
   * Changes the appearance of the GUI in the first round so that the user is able to see different icons
   */
  public void updateGUIAppearanceUserMonsters(){
    for (int i=0; i<cowLabels.length; i++){
      Cow currentCow = game.getCow(i);
      if (!currentCow.isVisible()){
        cowLabels[i].setIcon(circle);
      }
      else if (currentCow.isMonster()){
        if (currentCow.isScared())
          cowLabels[i].setIcon(scaredMonster);
        else if (currentCow.isDead())
          cowLabels[i].setIcon(deadMonster);
        else
          cowLabels[i].setIcon(monster);
      }
      else if (currentCow.isScared()){
        cowLabels[i].setIcon(scaredCow);
      }
      else if (currentCow.isDead()){
        cowLabels[i].setIcon(deadCow);
      }
      else if (currentCow.isVisible()){
        cowLabels[i].setIcon(cow);
      }
    }
    score.setText(game.getCurrentScores());
  }

    /**
   * Changes the appearance of the GUI so that the user is able to see different icons, does not show monsters unless they're dead
   */
  public void updateGUIAppearanceJEFMonsters(){
    for (int i=0; i<cowLabels.length; i++){
      Cow currentCow = game.getCow(i);
      if (!currentCow.isVisible()){
        cowLabels[i].setIcon(circle);
      }
      else if (currentCow.isMonster() && currentCow.isDead()){
        cowLabels[i].setIcon(deadMonster);
      }
      else if (currentCow.isScared()){
        cowLabels[i].setIcon(scaredCow);
      }
      else if (currentCow.isDead()){
        cowLabels[i].setIcon(deadCow);
      }
      else if (currentCow.isVisible()){
        cowLabels[i].setIcon(cow);
      }
    }
    score.setText(game.getCurrentScores());
  }
  
  //our ButtonListener class which handles all the buttons
  private class ButtonListener implements ActionListener
  {
    public void actionPerformed (ActionEvent event)
    {
      if (event.getSource() == killCowTimer){   //JEF will automatically kill a cow
        String message = game.computerKillsCow();
        gameInfo.setText(message);
        updateGUIAppearanceJEFMonsters();
        killCowTimer.stop();
        if (message.equals("JEF killed the last cow. Game over!")){ //if the game is over, show a dialog box asking if user wants to see high scores
          highScoresLabel.setText(game.getHighScores());
          String finalMessage = ((game.getFinalScore()>0) ? "You won! " : "You lost. ")
            + "Would you like to view the high scores?";
          int response = JOptionPane.showConfirmDialog(null, finalMessage);
          if (response == 0){
            tabbedPane.setSelectedIndex(3);
          }  
        }
        else { //let JEF move the scared cows
          movingScaredCowsTimer.start();
        }
      }
      if (event.getSource() == movingScaredCowsTimer){
        String message = game.computerMovesScaredCows();
        gameInfo.setText(message);
        updateGUIAppearanceJEFMonsters();
        if (message.equals("JEF has moved the scared cows. Now it is your turn to guess which cows are monsters.")){ //JEF has moved all the cows
         movingScaredCowsTimer.stop(); 
        }
      }
      if (event.getSource() == guessingMonsterTimer){ //JEF will guess which cow is a monster by killing it
        String message = game.computerGuessesMonster();
        int nummonsters = game.getLivingMonsters().size();
        gameInfo.setText(message);
        updateGUIAppearanceUserMonsters();
        if (nummonsters == 2){ //update the label on the left
          monsters.setIcon(onemdead);
        }
        if (nummonsters == 1){
          monsters.setIcon(twomdead);
        }
      if (game.getMode().equals("round1Over")){
        guessingMonsterTimer.stop();
        startNextRound.setVisible(true);
        monsters.setIcon(threemdead);
      }
      else if (game.getMode().equals("userKillsCow")){
        guessingMonsterTimer.stop();
        LinkedList<Integer> cowsAdjacentToMonsters = game.getCowsAdjacentToMonsters();
        for (int index: cowsAdjacentToMonsters){
          cowLabels[index].setIcon(killableCow); //show the killable cows highlighted red
        }
      }
    }
      if (event.getSource() == toRulesTab){
        tabbedPane.setSelectedIndex(1);
      }
      if (event.getSource() == toIntroductionTab){
        tabbedPane.setSelectedIndex(0);
      }
      if (event.getSource() == toGameTab){
        tabbedPane.setSelectedIndex(2); 
      }
      if (event.getSource() == userDoneChoosingMonsters){
        if (game.getLivingMonsters().size() != 3)
          gameInfo.setText("You have not chosen three monsters yet!");
        else{
          String message = game.userDoneChoosingMonsters();
          gameInfo.setText(message);
          userDoneChoosingMonsters.setVisible(false);
          LinkedList<Integer> cowsAdjacentToMonsters = game.getCowsAdjacentToMonsters();
          for (int index: cowsAdjacentToMonsters) //make killable cows visible to the user
            cowLabels[index].setIcon(killableCow);
        }
      }
      if (event.getSource() == startGame){
        String message1 = JOptionPane.showInputDialog("What is your name?");
        if (!(message1 == null)){ //if the user inputs a name, do stuff, else do nothing
          game.setName(message1);
          String message = game.computerMovesCows();
          startGame.setVisible(false);
          newGame.setVisible(true);            
          gameInfo.setText(message);
          updateGUIAppearanceUserMonsters();
          userDoneChoosingMonsters.setVisible(true);
        }
      }
      if (event.getSource() == startNextRound){
        game.resetGame(); //start the next round, so reset all values in the game class
        monsters.setIcon(zeromdead);
        startNextRound.setVisible(false);
        doneMovingCows.setVisible(true);
        gameInfo.setText("Move around some cows if you wish! Once you're done, JEF will choose 3 monsters and kill a cow.");
        updateGUIAppearanceJEFMonsters();
      }
      if (event.getSource() == doneMovingCows){
        game.userDoneMovingCows();
        doneMovingCows.setVisible(false);
        game.computerChoosesMonsters();  //let JEF choose monsters (will be unseen to user)
        killCowTimer.start();   
      }
      if (event.getSource() == newGame){ //reset everything in GUI
        newGame.setVisible(false);
        startNextRound.setVisible(false);
        startGame.setVisible(true);
        userDoneChoosingMonsters.setVisible(false);
        doneMovingCows.setVisible(false);
        monsters.setIcon(zeromdead);
        killCowTimer.stop();
        movingScaredCowsTimer.stop();
        guessingMonsterTimer.stop();
        game.newGame();
        updateGUIAppearanceUserMonsters();
        gameInfo.setText("You have a started a new game. Please press the Start Game button to begin.");
      }
      if (event.getSource() == backToGame){
        tabbedPane.setSelectedIndex(2);
      }
      if (event.getSource() == quitGame0 || event.getSource() == quitGame1 || event.getSource() == quitGame2 || event.getSource() == quitGame3){
        System.exit(0);
      }
    }
  }
  
  private class CowListener implements MouseListener{
    
    //instance variables for our two move methods
    private int index;
    private int step = 0;
    
    private int getIndexOfCow(MouseEvent event){ //if the user clicks on a cow, get the index of the cow the user clicked on
      int result = -1;
      for (int i = 0; i < cows.length; i++){
        if (event.getComponent() == cows[i])
          result = i;
      }
      return result;
    }
    
    //move a cow
    private void moveCow(MouseEvent event) {
      if (step == 0){
        index = getIndexOfCow(event);
        if (game.getCow(index).isVisible()) {
          cowLabels[index].setIcon(highlightedCow);
          step = 1;
          gameInfo.setText("Please move your cow to an empty location.");
        }
        else{
          gameInfo.setText("You did not select a cow.");
        }
      }
      else if (step == 1){
        step = 0;
        int index2 = getIndexOfCow(event);
        String message = game.userMovesCow(index, index2); //actually move the cow in the Game
        if (message.equals("You successfully moved your cow.")){
          cowLabels[index].setIcon(circle); //change the GUI appearance
          cowLabels[index2].setIcon(cow);
          gameInfo.setText(message);
        }
        else{
          cowLabels[index].setIcon(cow);
          gameInfo.setText(message);
        }
      }
    }
    
    private void moveScaredCow(MouseEvent event) {
      if (step == 0){
        index = getIndexOfCow(event);
        if (!game.getCow(index).isScared()) {
          gameInfo.setText("You cannot move this cow, it is not currently scared.");
        }
        else if (game.getCow(index).isVisible()) {
          cowLabels[index].setIcon(game.getCow(index).isMonster() ? highlightedMonster : highlightedScaredCow);
          step = 1;
          gameInfo.setText("Please move your cow to an empty location.");
        }
        else{
          gameInfo.setText("You did not select a cow.");
        }
      }
      else if (step == 1){
        step = 0;
        int index2 = getIndexOfCow(event);
        String message = game.userMovesScaredCow(index, index2);
        if (message.equals("You have moved a scared cow.")){
          cowLabels[index].setIcon(circle);
          cowLabels[index2].setIcon(game.getCow(index2).isMonster() ? monster : cow);
          gameInfo.setText(message);
        }
        else if (message.equals("You have moved all of the scared cows. Now JEF will guess.")) {
          cowLabels[index].setIcon(circle);
          cowLabels[index2].setIcon(game.getCow(index2).isMonster() ? monster : cow);
          gameInfo.setText(message);
          guessingMonsterTimer.start();
        }
        else{
          cowLabels[index].setIcon(game.getCow(index).isMonster() ? scaredMonster : scaredCow);
          gameInfo.setText(message);
        }
      }
    }
    
    public void mouseClicked(MouseEvent event){
      if (game.getMode().equals("userMovesCows")){
        moveCow(event);
      }
      if (game.getMode().equals("userChoosesMonsters")){
        int index = getIndexOfCow(event);
        String message = game.userChoosesMonster(index);
        gameInfo.setText(message);
        updateGUIAppearanceUserMonsters();
      }
      if (game.getMode().equals("userKillsCow")){
        int index = getIndexOfCow(event);
        String message = game.userKillsCow(index);
        gameInfo.setText(message);
        if (message.equals("The cow was successfully killed.")){
          updateGUIAppearanceUserMonsters();      
       }
       if (message.equals("The last cow was killed, you won! On to the next round.")){
         startNextRound.setVisible(true);
         updateGUIAppearanceUserMonsters();
       }
      }
      else if (game.getMode().equals("userMovesScaredCows")){
        moveScaredCow(event);
      }
      if (game.getMode().equals("userGuessesMonster")){
        String message = game.userGuessesMonster(getIndexOfCow(event));
        gameInfo.setText(message);
        updateGUIAppearanceJEFMonsters();
        if (message.equals("The cow you guessed was not a monster. Now JEF will kill a cow again.")){
          killCowTimer.start();  
        }
        if (message.equals("The cow you guessed was a monster. Guess again!")){
          if (game.getLivingMonsters().size() == 2){
            monsters.setIcon(onemdead);
          }
          if (game.getLivingMonsters().size() == 1){
            monsters.setIcon(twomdead);
          }
        }
        if (message.equals("You killed the last monster, you won!")){
          monsters.setIcon(threemdead);
          highScoresLabel.setText(game.getHighScores());
          String finalMessage = ((game.getFinalScore()>0) ? "You won! " : "You lost. ")
            + "Would you like to view the high scores?";
          int response = JOptionPane.showConfirmDialog(null, finalMessage);
          if (response == 0){
            tabbedPane.setSelectedIndex(3);
          }
        }
      }
    }
    
    //have these methods because MouseListener is an interface, but we don't use them
    public void mouseEntered(MouseEvent event){
    }
    public void mouseExited(MouseEvent event){
    }
    public void mousePressed(MouseEvent event){
    }
    public void mouseReleased(MouseEvent event){
    }
  }
}