import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Font;

public class RobotBasic extends Robot {
    // Attributes
    private boolean go_back;                        // default set as true

    // Constructors
    public RobotBasic(Monde m, int posy, int posx, String team){
        super(m, posy, posx, team);
        go_back = true;
        super.set_weight(400);
        super.set_initiative(500);
        super.set_name("Basic");
        super.set_team(team);
        super.set_price(20);
    }

    public RobotBasic(Monde m, int posx, int posy, boolean go_back, String team){
        super(m, posx, posy, team);
        this.go_back = go_back;
        super.set_weight(400);
        super.set_initiative(500);
        super.set_name("Basic");
        super.set_team(team);
        super.set_price(20);
    }

    // Unplayable version of the robot
    public RobotBasic(String team){
        super(team);
        super.set_name("Basic");
        super.set_price(20);
    }

    // Getters
    public boolean getGoBack(){return go_back;}

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        // Leave immediatly if the robot is not functionning
        if (!super.get_functionning()){return;}

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
        System.out.println(randomChooser);

        // Check if previous move = next move, if yes : generate a new option until previous_move != next_move 
        if (!go_back && Arrays.equals(possibleMove[randomChooser], previous_move)){
            while (Arrays.equals(possibleMove[randomChooser], previous_move)){
                randomChooser = (int) (Math.random()*nb_option);                          // re-randomize until not going backward (previous position)
            }
        }

        // Check if the next position is within the map and act accordingly
        if (super.get_posy() + possibleMove[randomChooser][0] < 0
        || super.get_posy() + possibleMove[randomChooser][0] >= super.getMonde().get_nbL()
        || super.get_posx() + possibleMove[randomChooser][1] < 0 
        || super.get_posx() + possibleMove[randomChooser][1] >= super.getMonde().get_nbC()) {
            super.set_functionning(false);
        } else {
            // actualize previous move
            previous_move[0] = super.get_posy();
            previous_move[1] = super.get_posx();

            // set new coordinates
            super.set_posx(super.get_posx() + possibleMove[randomChooser][1]);
            super.set_posy(super.get_posy() + possibleMove[randomChooser][0]);

            // Action in the new position : polute or depolute
            if(super.get_team() == "Dirty"){
                super.getMonde().add_dirty(super.get_posy(), super.get_posx());
            } else if (super.get_team() == "Clean"){
                super.getMonde().add_clean(super.get_posy(),super.get_posx());
            }
        }
    }

    @Override
    public void moveAndAct(Robot RobotBasic, ScreenPlaying graph, Monde map, ArrayList<Robot> robots_inGame){
        RobotBasic R = (RobotBasic)RobotBasic;         // Down-casting to access RobotBasic specific attributes
        if (!R.get_functionning()) {return;}

        int[] previous_move = new int[2];
        int[][] possibleMove;
        int randomChooser;
        int nb_option;
    
        // generating possible moving options for the Robot
        possibleMove = new int[4][2];
        possibleMove[0] = new int[]{-1, 0};                // TOP
        possibleMove[1] = new int[]{0, 1};                 // RIGHT
        possibleMove[2] = new int[]{1, 0};                 // BOT
        possibleMove[3] = new int[]{0, -1};                // LEFT
        nb_option = 4;

        // choosing randomly one of the option
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

        // Check if the next position is within the map, if not return
        if (Utilities.IsOutOfMap(next_y, next_x, graph.get_gridSizeY(), graph.get_gridSizeX())){
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
            graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
            graph.get_cells()[R.get_posy()][R.get_posx()].setFont(new Font("Arial", Font.BOLD, 24));
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
            graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
            R.set_posy(next_y);
            R.set_posx(next_x);
            graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
        
            // Action in the new position - pollute if in team true, else depolute
            Utilities.ActDirtyClean(R, graph, map);

        } else {
            Utilities.dealWithBump(R, robotInPathList, direction, graph, map);
            if (R.get_posy() == next_y && R.get_posx() == next_x){
                // The robot has moved
                Utilities.ActDirtyClean(R, graph, map);
            } 
            // Otherwise the robot has not moved and should do nothing
        }
    }  

    // Main (try)
    public static void main(String[] args){}
}