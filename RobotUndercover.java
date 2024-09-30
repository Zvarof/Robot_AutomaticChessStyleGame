import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;

public class RobotUndercover extends Robot {
    // Attributes
    boolean go_back = true;                        // default set as true
    GameManager gameManager;

    // Constructors
    public RobotUndercover(Monde m, int posy, int posx, String team, GameManager gameManager){
        super(m, posy, posx, team);
        go_back = true;
        this.gameManager = gameManager;
        super.set_weight(300);
        super.set_initiative(850);
        super.set_name("Undercover");
        super.set_team(team);
        super.set_price(30);
        super.set_step(2);
    }

    // Unplayable version of the robot
    public RobotUndercover(String team){
        super(team);
        super.set_name("Undercover");
        super.set_price(30);
        super.set_step(2);
    }

    // Getters
    public boolean getGoBack(){return go_back;}
    public GameManager getGameManager(){return gameManager;}

    // Methods
    public void parcourir(){}

    /* The RobotInsider will get the owner player access to previously unacessible cells when moving on top of them
     * He avoids any unsolicited interactions to stay undercover (ie, not bumping other robots)
     * When the cell get "undercovered" : either "I_C" or "I_D" is added to it
     * Where 'I' is for 'Insided', 'C' is for 'Clean' and 'D' is for 'Dirty'.
     */
    public void ActInsider(RobotUndercover R, ScreenPlaying graph, Monde map){
        Player cleanPlayer = gameManager.get_playerList().get(0);
        Player dirtyPlayer = gameManager.get_playerList().get(1);
        if (R.get_team().equals("Clean")){
            if (!cleanPlayer.hasInsided(R.get_posy(), R.get_posx()) && R.get_posy()>=4 && R.get_posy()<=8){
                cleanPlayer.getInsidedCells().add(new Point(R.get_posx(), R.get_posy()));
                String current_text = graph.get_cells()[R.get_posy()][R.get_posx()].getText();
                if (current_text == null || current_text.isEmpty()){
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText("I_C");
                } else {
                    if (current_text.contains("I_C")){
                        // do nothing
                    } else {
                        graph.get_cells()[R.get_posy()][R.get_posx()].setText(current_text + " I_C");
                    }
                }
            }
        } else if (R.get_team().equals("Dirty")){
            if(!dirtyPlayer.hasInsided(R.get_posy(), R.get_posx()) && R.get_posy()<=4 && R.get_posy()>=0){
                dirtyPlayer.getInsidedCells().add(new Point(R.get_posx(), R.get_posy()));
                String current_text = graph.get_cells()[R.get_posy()][R.get_posx()].getText();
                if(current_text == null || current_text.isEmpty()){
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText("I_D");
                } else {
                    if(current_text.contains("I_D")){
                        // do nothing
                    } else {
                        graph.get_cells()[R.get_posy()][R.get_posx()].setText(current_text + " I_D");
                    }
                }
            }
        }
    }

    @Override
    public void moveAndAct(Robot RobotUndercover, ScreenPlaying graph, Monde map, ArrayList<Robot> robots_inGame){
        RobotUndercover R = (RobotUndercover) RobotUndercover;         // Down-casting to access RobotUndercover specific attributes
        if (!R.get_functionning()) {return;}

        int[] previous_move = new int[2];
        int[][] possibleMove;
        int randomChooser;
        int nb_option;
        
        // This robot is two times more likely (40% vs 20%) to go towards ennemy camp rather than in a given other direction
        possibleMove = new int[5][2];
        possibleMove[0] = new int[]{-1, 0};                // TOP
        possibleMove[1] = new int[]{0, 1};                 // RIGHT
        possibleMove[2] = new int[]{1, 0};                 // BOT
        possibleMove[3] = new int[]{0, -1};                // LEFT
        possibleMove[4] = R.get_team().equals("Clean") ? new int[]{1, 0} : new int[]{-1, 0};
        nb_option = 5;
    
        // Randomly choosing an moving option
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
    
        // Check if the next position is within the map, if not break outside the for loop
        if (Utilities.IsOutOfMap(next_y, next_x, graph.get_gridSizeY(), graph.get_gridSizeX())){
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
            graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
            Utilities.dealWithOutOfMap(R);
            return;              
        }
    
        // Get the list of robot in the path and actualise previous tried move (even if not effective)
        ArrayList<Robot> robotInPathList = Utilities.whoIsInMyPath(next_y, next_x, direction, robots_inGame);
        previous_move[0] = -direction[0];
        previous_move[1] = -direction[1];
    
        // Check if the next position is occupied
        if (robotInPathList.size() == 0){
            // Actualise moving Robot's coordonate and move image
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
            graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
            R.set_posy(next_y);
            R.set_posx(next_x);
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
            
            // Action in the new position - specific to RobotUndercover
            ActInsider(R, graph, map);
        } else {
            // Do nothing, better be discrete not to be discovered
        }
    }

    // Main (try)
    public static void main(String[] args){}
}
