import java.util.ArrayList;
import java.util.Arrays;

public class RobotSmasher extends Robot {
    // Attributes
    boolean go_back;                        // default set as false

    // Getters
    public boolean getGoBack(){return go_back;}

    // Constructors
    public RobotSmasher(Monde m, int posy, int posx, String team){
        super(m, posy, posx, team);
        go_back = false;
        super.set_weight(600);
        super.set_initiative(400);
        super.set_name("Smasher");
        super.set_team(team);
        super.set_price(30);
    }

    public RobotSmasher(Monde m, int posx, int posy, boolean go_back, String team){
        super(m, posx, posy, team);
        this.go_back = go_back;
        super.set_weight(600);
        super.set_initiative(400);
        super.set_name("Smasher");
        super.set_team(team);
        super.set_price(30);
    }

    public RobotSmasher(String team){
        super(team);
        super.set_name("Smasher");
        super.set_price(30);
    }

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        boolean IsWithin = true;
        int[] previous_move = new int[2];
        int[][] possibleMove;
        int randomChooser;
        int nb_option;

        possibleMove = new int[4][2];
        possibleMove[0] = new int[]{-1, 0};                // TOP
        possibleMove[1] = new int[]{0, 1};                 // RIGHT
        possibleMove[2] = new int[]{1, 0};                 // BOT
        possibleMove[3] = new int[]{0, -1};                // LEFT
        nb_option = 4;

        while (IsWithin){
            randomChooser = (int) (Math.random()*nb_option);
            System.out.println(randomChooser);

            // Check for previous move
            if (!go_back && Arrays.equals(possibleMove[randomChooser], previous_move)){
                while (Arrays.equals(possibleMove[randomChooser], previous_move)){
                    randomChooser = (int) (Math.random()*nb_option);                          // re-randomize until not going backward (previous position)
                }
            }

            // Check if the next position is within the map, if not break outsside the while loop
            if (super.get_posy() + possibleMove[randomChooser][0] < 0
            || super.get_posy() + possibleMove[randomChooser][0] >= super.getMonde().get_nbL()
            || super.get_posx() + possibleMove[randomChooser][1] < 0 
            || super.get_posx() + possibleMove[randomChooser][1] >= super.getMonde().get_nbC()) {
                IsWithin = false;
                super.set_functionning(false);
            } else {
                previous_move[0] = super.get_posy();
                previous_move[1] = super.get_posx();

                super.set_posx(super.get_posx() + possibleMove[randomChooser][1]);
                super.set_posy(super.get_posy() + possibleMove[randomChooser][0]);

                // Action in the new position - pollute if in team true, else depolute
                if(super.get_team() == "Clean"){
                    // To add later
                } else {
                    // To add later
                }
            }
        }
    }

    @Override
    public void moveAndAct(Robot RobotSmasher, ScreenPlaying graph, Monde map, ArrayList<Robot> robots_inGame){
        RobotSmasher R = (RobotSmasher)RobotSmasher;         // Down-casting to access RobotBasic specific attributes
        if (!R.get_functionning()) {return;}

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

        // Check if previous_move = next_move, if so, reload an option until this isn't the case anymore
        if (!R.getGoBack() && Arrays.equals(possibleMove[randomChooser], previous_move)){
            while (Arrays.equals(possibleMove[randomChooser], previous_move)){
                randomChooser = (int) (Math.random()*nb_option);                          // re-randomize until not going backward (previous position)
            }
        }

        // Get next x, y and current direction for ease of use latter
        int next_y = R.get_posy() + possibleMove[randomChooser][0];
        int next_x = R.get_posx() + possibleMove[randomChooser][1];
        int[] direction = new int[]{possibleMove[randomChooser][0], possibleMove[randomChooser][1]};

        // Check if the next position is within the map, if NOT break outside the for loop
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

        } else {
            if (robotInPathList.get(0).get_team() != R.get_team()){
                graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
                graph.get_cells()[robotInPathList.get(0).get_posy()][robotInPathList.get(0).get_posx()].setIcon(null);
                graph.get_cells()[robotInPathList.get(0).get_posy()][robotInPathList.get(0).get_posx()].setText("");
                Utilities.dealWithOutOfMap(robotInPathList.get(0));
                R.set_posy(next_y);
                R.set_posx(next_x);
                graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
            }
        }
    }

    // Main (try)
    public static void main(String[] args){}
}