import java.util.Scanner;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class GameManager {
    // Attributes
    private ScreenPlaying sP;
    private ScreenRobotChoosing sRC;
    private ScreenFinalResult sFR;
    private ScreenIntermediateResult sIR;
    private JFrame mainJFrame;              // default size is 1000*1000
    private Monde map;
    private ArrayList<Robot> robots_inGame = new ArrayList<>();
    private int nb_rounds = 3;
    private int current_round = 0;
    private int nb_turns;
    private int current_turn = 0;
    private ArrayList<Player> player_list = new ArrayList<>();
    private Timer robotMoveTimer;

    // Getters
    public ScreenPlaying get_ScreenPlaying(){return sP;}
    public ScreenRobotChoosing get_ScreenRobotChoosing(){return sRC;}
    public ScreenFinalResult get_ScreenFinalResult(){return sFR;}
    public JFrame get_mainJFrame(){return mainJFrame;}
    public Monde getMonde(){return map;}
    public int get_nbRounds(){return nb_rounds;}
    public int get_currentRound(){return current_round;}
    public int get_nbTurns(){return nb_turns;}
    public int get_currentTurn(){return current_turn;}
    public ArrayList<Player> get_playerList(){return player_list;}
    public ArrayList<Robot> get_robotInGame(){return robots_inGame;}

    // Setters
    public void set_nbRounds(int nb_rounds){this.nb_rounds = nb_rounds;}
    public void set_nbTurns(int nbTurns){this.nb_turns = nbTurns;}
    public void set_CurrentTurn(int current_turn){this.current_turn = current_turn;}
    public void set_ScreenPlaying(ScreenPlaying sP){this.sP = sP;}
    public void set_ScreenRobotChoosing(ScreenRobotChoosing sRC){this.sRC = sRC;}
    public void set_ScreenIntermediateResult(ScreenIntermediateResult sIR){this.sIR = sIR;}
    public void set_ScreenFinalResult(ScreenFinalResult sFR){this.sFR = sFR;}
    
    // Constructors 
    public GameManager(Monde map, int nb_turns){
        this.map = map;
        this.nb_turns = nb_turns;

        // Getting the players names and creating Player instances
        Scanner observer = new Scanner(System.in);
        for (int i=0; i<2; i++){
            System.out.println("Enter the name of player " + (i+1));
            String team = (i%2 == 0)? "Clean" : "Dirty";
            player_list.add(new Player(observer.nextLine(), team));
        }
        observer.close();

        // Initializing main JFrame and setting size|name
        mainJFrame = new JFrame();
        mainJFrame.setSize(1000, 1000);
        mainJFrame.setTitle("'Robot' : Zvarof's wonderful game !");

        // Setting the timer for Robot move to avoid GUI freeze when using thread.sleep()
        robotMoveTimer = new Timer(500, new ActionListener() {
            private int index = 0;
            private int turnCounter = 0;
            private int stepCounter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == 0) {
                    Utilities.removeRobotOutOfMap(robots_inGame); 
                    Utilities.displayPriorities(robots_inGame, sP.get_cells());
                }

                if (index < robots_inGame.size()) {
                    Robot robot = robots_inGame.get(index);
                    // Realising each step, before moving to the next robot (index++)
                    synchronized (robot) {
                        if (stepCounter < robot.get_step()){
                            robot.moveAndAct(robot, sP, map, robots_inGame);
                            stepCounter ++;
                        } else {
                            stepCounter = 0;
                            index++;
                        }
                    }
                } else {
                    // Stop the timer if all robots have been processed
                    index = 0;
                    turnCounter += 1;
                    if(turnCounter >= nb_turns){
                        robotMoveTimer.stop();
                        turnCounter = 0;
                        // Handle the transition to the next phase depending on current_round
                        if(current_round >= nb_rounds){
                            switchFromPlayingToFinalResults(sP, sIR, sRC, sFR);
                        } else {
                            switchFromPlayingToIntermediateResults(sP, sIR, sRC);
                        }
                    } else {
                        index = 0;
                    }
                }
            }
        });
    }

    public GameManager(Monde map){
        this.map = map;
        this.nb_turns = 2;

        // Getting the players names
        Scanner observer = new Scanner(System.in);
        for (int i=0; i<2; i++){
            System.out.println("Enter the name of player " + (i+1));
            String team = (i%2 == 0 )? "Clean" : "Dirty";
            player_list.add(new Player(observer.nextLine(), team));
        }
        observer.close();

        // Set mainJFrame Size and name
        mainJFrame = new JFrame();
        mainJFrame.setSize(1000, 1000);
        mainJFrame.setName("'Robot' : Zvarof's wonderful game !");

        // Setting the timer for Robot move to avoid GUI freeze when using thread.sleep()
        robotMoveTimer = new Timer(500, new ActionListener() {
            private int index = 0;
            private int turnCounter = 0;
            private int stepCounter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == 0 && stepCounter == 0) {
                    Utilities.removeRobotOutOfMap(robots_inGame);
                    Utilities.displayPriorities(robots_inGame, sP.get_cells());
                }

                if (index < robots_inGame.size()) {
                    Robot robot = robots_inGame.get(index);
                    // Realising each step, before moving to the next robot (index++)
                    synchronized (robot) {
                        if (stepCounter < robot.get_step()){
                            robot.moveAndAct(robot, sP, map, robots_inGame);
                            stepCounter ++;
                        } else {
                            stepCounter = 0;
                            index ++;
                        }
                    }
                } else {
                    // Stop the timer if all robots have been processed
                    index = 0;
                    turnCounter += 1;
                    if(turnCounter >= nb_turns){
                        robotMoveTimer.stop();
                        turnCounter = 0;
                        // Handle the transition to the next phase depending on current_round
                        if(current_round >= nb_rounds){
                            switchFromPlayingToFinalResults(sP, sIR, sRC, sFR);
                        } else {
                            switchFromPlayingToIntermediateResults(sP, sIR, sRC);
                        }
                    } else {
                        index = 0;
                    }
                }
            }
        });
    }

    // Methods

    public void addRobotInGame(String RobotSelected, int y, int x, String team, Monde mapMonde){
        /* This only add the robot in the list of robots in the game manager
        The display is managed by the Graphique Interface
        For the robot to be displayed, the use of creatAllRobotImage() is required */
        switch (RobotSelected) {
            case "Basic":
                Robot newRobot = new RobotBasic(mapMonde, y, x, team);
                robots_inGame.add(newRobot);
                break;             
            case "Jumper":
                newRobot = new RobotJumper(mapMonde, y, x, team);
                robots_inGame.add(newRobot);
                break;
            case "GlasseCanon":
                newRobot = new RobotGlasseCanon(mapMonde, y, x, team);
                robots_inGame.add(newRobot);
                break;
            case "Smasher":
                newRobot = new RobotSmasher(mapMonde, y, x, team);
                robots_inGame.add(newRobot);
                break;
            case "Undercover":
                newRobot = new RobotUndercover(mapMonde, y, x, team, this);
                robots_inGame.add(newRobot);
                break;
            default:
                System.out.println("The selected robot has no valid type");
                break;
        }
    }

    public void resetReadyStatus(){
        for(Player player : player_list){
            player.setReady(false);
        }
    }

    public void actualizePlayerScore(){
        player_list.get(0).setScore(0);
        player_list.get(1).setScore(0);
        for(int i=0; i<map.get_world_matrix().length; i++){
            for (String string : map.get_world_matrix()[i]){
                if (string.equals("clean")){
                    if (player_list.get(0).getPlayerTeam().equals("Clean")){
                        player_list.get(0).setScore(player_list.get(0).getScore() + 1);
                    } else {
                        player_list.get(1).setScore(player_list.get(1).getScore() + 1);
                    }
                } else if (string.equals("dirty")){
                    if (player_list.get(0).getPlayerTeam().equals("Dirty")){
                        player_list.get(0).setScore(player_list.get(0).getScore() + 1);
                    } else {
                        player_list.get(1).setScore(player_list.get(1).getScore() + 1);
                    }
                }
            }
        }
    }

    public void addTurnEcon(){
    // At the start of each turn, each player gain a certain amount of gold to buy new robots
        for(Player player : player_list){
            player.setPlayerBankAccount(player.getPlayerBankAccount() + 30);
        }
        Utilities.changeJLabel(sRC.get_robotSelectionPanel(), 1, Utilities.bankAccountStrg + sRC.get_currentPlayerChoosing().getPlayerBankAccount());
    }

    public void switchFromChoiceToPlaying(ScreenRobotChoosing sRC, ScreenPlaying sP){
        Utilities.removeRobotOutOfMap(robots_inGame);               // clear un-functionnal robots
        Utilities.sortRobotListByASCInitiative(robots_inGame);
        sP.matchChoosingPanel(sRC);                                 // Update choosingPanel based on playingPanel
        mainJFrame.getContentPane().removeAll();
        mainJFrame.getContentPane().add(sP.get_playPanel());
        mainJFrame.pack();
        mainJFrame.setVisible(true);

        robotMoveTimer.start();     // Do a Robot move every 400ms and avoid GUI freeze | Also contains switching to result screens
    }

    public void switchFromPlayingToIntermediateResults(ScreenPlaying sP, ScreenIntermediateResult sIR, ScreenRobotChoosing sRC){
        current_round += 1;
        if (current_round >= nb_rounds){
            switchFromPlayingToFinalResults(sP, sIR, sRC, sFR);
            return;
        }
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchFromIntermediateResultsToChoice(sP, sIR, sRC);
            }
        });

        this.actualizePlayerScore();
        sIR.actualize();
        mainJFrame.getContentPane().removeAll();
        mainJFrame.getContentPane().add(sIR.getJPanelTop(), BorderLayout.NORTH);
        mainJFrame.getContentPane().add(sIR.getJPanelLeft(), BorderLayout.WEST);
        mainJFrame.getContentPane().add(sIR.getJPanelRight(), BorderLayout.EAST);
        mainJFrame.setVisible(true);

        // Start the timer after 5000 and only runs it once
        timer.setRepeats(false);
        timer.start();
    }

    public void switchFromPlayingToFinalResults(ScreenPlaying sP, ScreenIntermediateResult sIR, ScreenRobotChoosing sRC, ScreenFinalResult sFR){
        sIR.actualize();
        sFR.getWinnerAndScores();
        mainJFrame.setVisible(true);
    }

    public void switchFromIntermediateResultsToChoice(ScreenPlaying sP, ScreenIntermediateResult sIR, ScreenRobotChoosing sRC){
        resetReadyStatus();
        addTurnEcon();
        Utilities.removeRobotOutOfMap(robots_inGame);
        sRC.matchPlayPanel(sP);
        mainJFrame.getContentPane().removeAll();
        mainJFrame.getContentPane().add(sRC.get_mapDisplayPanel(), BorderLayout.CENTER);
        mainJFrame.getContentPane().add(sRC.get_robotSelectionPanel(), BorderLayout.EAST);
        mainJFrame.setVisible(true);
    }

    // Main (try)
    public static void main(String[] args){
        // Initializeing the required instances for the game
        // Using invokeLater to avoid GUI not being actualized when needed
        SwingUtilities.invokeLater(() -> {
            Monde monde1 = new Monde();
            GameManager gameManager1 = new GameManager(monde1, 4);
            ScreenPlaying sP = new ScreenPlaying(9, 9, gameManager1);
            ScreenRobotChoosing sRC = new ScreenRobotChoosing(Utilities.default_robotAvailableList, sP, gameManager1);
            ScreenIntermediateResult sIR = new ScreenIntermediateResult(gameManager1);
            ScreenFinalResult sFR = new ScreenFinalResult(gameManager1);
            gameManager1.set_ScreenRobotChoosing(sRC);
            gameManager1.set_ScreenPlaying(sP);
            gameManager1.set_ScreenIntermediateResult(sIR);
            gameManager1.set_ScreenFinalResult(sFR);

            // First initialization. After, screen switcher functions are used.
            gameManager1.get_mainJFrame().add(sRC.get_mapDisplayPanel(), BorderLayout.CENTER);
            gameManager1.get_mainJFrame().add(sRC.get_robotSelectionPanel(), BorderLayout.EAST);
            gameManager1.get_mainJFrame().setVisible(true);
        });
    }
}