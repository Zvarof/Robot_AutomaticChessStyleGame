import java.util.Scanner;
//import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class GameManager {
    // Attributes
    private Monde map;
    private int nb_players = 2;             // Set as 2 by default
    private String[] player_name;
    private ArrayList<Robot> robots_inGame = new ArrayList<>();
    private int[] player_score;
    private int nb_turns;
    private int starting_ressources = 20;   // Set as 20 by default

    // Getters
    public Monde getMonde(){return map;}
    public int get_nbPlayers(){return nb_players;}
    public String[] get_playerNames(){return player_name;}
    public int[] get_playerScores(){return player_score;}
    public int get_nbTurns(){return nb_turns;}
    public int get_startRessources(){return starting_ressources;}

    // Setters
    public void set_nbPlayers(int nbPlayers){nb_players = nbPlayers;}
    public void set_playerScore(int index, int score){player_score[index] = score;}
    public void set_nbTurns(int nbTurns){this.nb_turns = nbTurns;}
    public void set_startRessources(int starting_ressources){this.starting_ressources = starting_ressources;}
    
    // Constructors 
    public GameManager(Monde map, int nb_players, int nb_turns, int starting_ressources){
        this.map = map;
        this.nb_players = nb_players;
        this.nb_turns = nb_turns;
        this.starting_ressources = starting_ressources;

        // Getting the players names
        player_name = new String[nb_players];
        Scanner observer = new Scanner(System.in);
        for (int i=0; i<nb_players; i++){
            System.out.println("Enter the name of player " + (i+1));
            player_name[i] = observer.nextLine();
        }
        observer.close();

        // Initializing players score
        player_score = new int[nb_players];         // Implicitly set value to 0 (default for int type)
    }

    public GameManager(Monde map){
        this.map = map;
        this.nb_players = 2;
        this.nb_turns = 2;

        // Getting the players names
        player_name = new String[nb_players];
        Scanner observer = new Scanner(System.in);
        for (int i=0; i<nb_players; i++){
            System.out.println("Enter the name of player " + (i+1));
            player_name[i] = observer.nextLine();
        }
        observer.close();

        // Initializing players score
        player_score = new int[nb_players];         // Implicitly set value to 0 (default for int type)
    }

    // Methods



    public void addRobotInGame(String RobotSelected, int y, int x, String team, Monde mapMonde){
        /* This only add the robot in the list of robots in the game manager
        The display is managed by the Graphique Interface
        For the robot to be displayed, the use of creatAllRobotImage() is required */
        if(RobotSelected == "Basic"){
            Robot newRobot = new RobotBasic(mapMonde, y, x, team);
            robots_inGame.add(newRobot);
        }
    }

    /* CONTROLLING ROBOT DEPLACEMENT, ACTIONS and DISPLAY related to their movement DEPENDING ON THEIR TYPE */
    //// BASIC ROBOT ////
    public void move_Basic(RobotBasic R, GraphiqueInterface graph) throws InterruptedException{
        for (int i=0; i<R.get_step(); i++){
            if (!R.get_functionning()) {
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            int[] previous_move = new int[2];
            int[][] possibleMove;
            int randomChooser;
            int nb_option;
    
            // Note : Should find a way to avoid redoundant calculation of possible move if multiple step
            possibleMove = new int[4][2];
            possibleMove[0] = new int[]{-1, 0};                // TOP
            possibleMove[1] = new int[]{0, 1};                 // RIGHT
            possibleMove[2] = new int[]{1, 0};                 // BOT
            possibleMove[3] = new int[]{0, -1};                // LEFT
            nb_option = 4;

            randomChooser = (int) (Math.random()*nb_option);

            // Check for previous move
            if (!R.getGoBack() && Arrays.equals(possibleMove[randomChooser], previous_move)){
                while (Arrays.equals(possibleMove[randomChooser], previous_move)){
                    randomChooser = (int) (Math.random()*nb_option);                          // re-randomize until not going backward (previous position)
                }
            }

            // Get next x, y and current direction for ease of use latter
            int next_y = R.get_posy() + possibleMove[randomChooser][0];
            int next_x = R.get_posx() + possibleMove[randomChooser][1];
            int[] direction = new int[]{possibleMove[randomChooser][0], possibleMove[randomChooser][1]};

            // Check if the next position is within the map, if not break outsside the while loop
            if (next_y < 0 || next_y >= map.get_nbL() || next_x < 0 || next_x >= map.get_nbC()) {
                R.set_functionning(false);
                break;
            }

            // Get the list of robot in the path
            ArrayList<Robot> robotInPathList = whoIsInMyPath(next_y, next_x, direction);

            // Check if the next position is occupied
            if (robotInPathList.size() == 0){
                // Actualise previous_move
                previous_move[0] = -direction[0];
                previous_move[1] = -direction[1];
        
                // Actualise moving Robot's coordonate and move image
                graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                R.set_posy(R.get_posy() + possibleMove[randomChooser][0]);
                R.set_posx(R.get_posx() + possibleMove[randomChooser][1]);
                graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
        
                // Action in the new position - pollute if in team true, else depolute
                if(R.get_team() == "Dirty"){
                    map.add_dirty(R.get_posy(), R.get_posx());
                    graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Color.BLACK);
                } else if (R.get_team() == "Clean"){
                    map.delete_dirty(R.get_posy(), R.get_posx());
                    graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Color.GREEN);
                }
            } else {
                if (R.get_weight() > 1.2 * Utilities.getRobotListWeightSum(robotInPathList)){
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);                                // The moving Robot disapears
                    R.set_posy(R.get_posy() + possibleMove[randomChooser][0]);
                    R.set_posx(R.get_posx() + possibleMove[randomChooser][1]);
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));       // The moving Robot appears in the next cell

                    if(R.get_team() == "Dirty"){
                        map.add_dirty(R.get_posy(), R.get_posx());
                        graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Color.BLACK);
                    } else if (R.get_team() == "Clean"){
                        map.delete_dirty(R.get_posy(), R.get_posx());
                        graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Color.GREEN);
                    }

                    // deplace all the robots in the path 1 cell backward
                    for (Robot robot : robotInPathList){
                        robot.set_posy(robot.get_posy() + direction[0]);
                        robot.set_posx(robot.get_posx() + direction[1]);
                        graph.get_cells()[robot.get_posy()][robot.get_posx()].setIcon(graph.getImageIconForRobot(robot));   // Switch image of all other robots 1 cell
                    }
                } else {
                    continue; // Come back to the first for-loop
                }
            }  
        }
    };

    public void move_GlasseCanon(RobotGlasseCanon R){};
    public void move_Djikstar(RobotDjikstar R){};
    public void move_Jumper(RobotJumper R){};
    public void move_ToutDroit(RobotpollueurToutDroit R){};
    public void move_Smasher(RobotSmasher R){};


    // Main (try)
    public static void main(String[] args) throws InterruptedException {
        /* Initialize the content of the game and load display */
        Monde monde1 = new Monde();
        GameManager GameManager1 = new GameManager(monde1, 2, 6, 20);
        
        GameManager1.addRobotInGame("Basic", 6, 5, "Dirty", monde1);
        GameManager1.addRobotInGame("Basic", 5, 5, "Clean", monde1);
        GameManager1.addRobotInGame("Basic", 4, 5, "Dirty", monde1);
        GameManager1.addRobotInGame("Basic", 2, 5, "Clean", monde1);
        GameManager1.addRobotInGame("Basic", 1, 5, "Dirty", monde1);

        // Display the robots in the map
        // SwingUtilities.invokeLater(() -> {
            GraphiqueInterface Displayer = new GraphiqueInterface(10, 10);

            Displayer.get_graphFrame().setSize(800, 800);
            Displayer.get_graphPanel().setSize(800,800);

            Displayer.createAllRobotImage(GameManager1.robots_inGame, Displayer); 

            Displayer.get_graphFrame().add(Displayer.get_graphPanel());
            Displayer.get_graphFrame().setVisible(true);

            // Sort robot by ASC Initiative, higher initiative = priority inside the turn
            Utilities.sortRobotListByASCInitiative(GameManager1.robots_inGame);

            for (int i=0; i<GameManager1.nb_turns; i++){                                    // Turns
                for (Robot robot : GameManager1.robots_inGame){                             // Robot
                    System.out.println("Youpi !");
                    GameManager1.move_Basic((RobotBasic) robot, Displayer);
                }
            }

            // Displayer.moveImage(1, 1, Displayer.get_cells(), Displayer.get_cells()[5][5]);
            // Displayer.get_cells()[2][2].setBackground(Color.BLUE);
        //});
        for (Robot robot : GameManager1.robots_inGame){
            System.out.println(robot.get_ID() + " " + " (" + robot.get_posy() + "," + robot.get_posx() + ")");
        }
    }
}