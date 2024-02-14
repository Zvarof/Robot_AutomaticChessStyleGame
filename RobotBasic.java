import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;

public class RobotBasic extends Robot {
    // Attributes
    int move_range = 1;                     // default set as 1
    boolean go_back;                        // default set as true

    // Constructors
    public RobotBasic(Monde m, int posy, int posx, String team){
        super(m, posy, posx, team);
        go_back = true;
        super.set_weight(400);
        super.set_initiative(500);
        super.set_name("Basic");
        super.set_team(team);
    }

    public RobotBasic(Monde m, int posx, int posy, boolean go_back, String team){
        super(m, posx, posy, team);
        this.go_back = go_back;
        super.set_weight(400);
        super.set_initiative(500);
        super.set_name("Basic");
        super.set_team(team);
    }

    // Getters
    public int getMoveRange(){return move_range;}
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
        possibleMove[0] = new int[]{-move_range, 0};                // TOP
        possibleMove[1] = new int[]{0, move_range};                 // RIGHT
        possibleMove[2] = new int[]{move_range, 0};                 // BOT
        possibleMove[3] = new int[]{0, -move_range};                // LEFT
        nb_option = 4;

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
            super.set_functionning(false);
        } else {
            previous_move[0] = super.get_posy();
            previous_move[1] = super.get_posx();

            super.set_posx(super.get_posx() + possibleMove[randomChooser][1]);
            super.set_posy(super.get_posy() + possibleMove[randomChooser][0]);

            // Action in the new position - pollute if in team true, else depolute
            if(super.get_team() == "Dirty"){
                super.getMonde().add_dirty(super.get_posy(), super.get_posx());
            } else if (super.get_team() == "Clean"){
                super.getMonde().delete_dirty(super.get_posy(),super.get_posx());
            }
        }
    }

    @Override
    public void moveAndAct(Robot RobotBasic, GraphiqueInterface graph, Monde map, ArrayList<Robot> robots_inGame) throws InterruptedException{
        RobotBasic R = (RobotBasic)RobotBasic;         // Down-casting to access RobotBasic specific attributes
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
            ArrayList<Robot> robotInPathList = Utilities.whoIsInMyPath(next_y, next_x, direction, robots_inGame);

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

    // Main (try)
    public static void main(String[] args){
        Monde Monde1 = new Monde();
        System.out.println("World before Robot Jumper going in");
        Monde1.displayWorldMatrix();

        RobotBasic RobotBasic1 = new RobotBasic(Monde1, 5, 4, "Dirty");
        RobotBasic1.parcourir();

        System.out.println("World before Robot Jumper after going in");
        Monde1.displayWorldMatrix();
    }
}