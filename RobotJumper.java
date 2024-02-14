import java.util.ArrayList;
import java.util.Arrays;

public class RobotJumper extends Robot {
    // Attributes
    int jump_range;                     // default set as 2
    boolean jump_diagonal;              // default set as false
    boolean jump_back;                  // default set as true

    // Constructors
    public RobotJumper(Monde m, int posy, int posx, String team){
        super(m, posy, posx, team);
        jump_range = 2;
        jump_diagonal = false;
        jump_back = true;
        super.set_weight(250);
        super.set_initiative(800);
    }

    public RobotJumper(Monde m, int posx, int posy, int jump_range, String team){
        super(m, posx, posy, team);
        this.jump_range = jump_range;
        this.jump_diagonal = false;
        this.jump_back = true;
        super.set_weight(250);
        super.set_initiative(800);
    }

    public RobotJumper(Monde m, int posx, int posy, int jump_range, boolean jump_diagonal, String team){
        super(m, posx, posy, team);
        this.jump_range = jump_range;
        this.jump_diagonal = jump_diagonal;
        this.jump_back = true;
        super.set_weight(250);
        super.set_initiative(800);
    }

    public RobotJumper(Monde m, int posx, int posy, int jump_range, boolean jump_diagonal, boolean jump_back, String team){
        super(m, posx, posy, team);
        this.jump_range = jump_range;
        this.jump_diagonal = jump_diagonal;
        this.jump_back = jump_back;
        super.set_weight(250);
        super.set_initiative(800);
    }

    // Getters
    public int getJumpRange(){return jump_range;}
    public boolean getJumpBack(){return jump_back;}
    public boolean getJumpDiagonal(){return jump_diagonal;}

    // Setters
    public void setJumpRange(int jump_range){this.jump_range = jump_range;}
    public void setJumpBack(boolean jump_back){this.jump_back = jump_back;}
    public void setJumpDiagonal(boolean jump_diagonal){this.jump_diagonal = jump_diagonal;}

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        boolean IsWithin = true;
        int[] previous_move = new int[2];
        int[][] possibleMove;
        int randomChooser;
        int nb_option;

        // Note : Should find a way to avoid redoundant calculation of possible move if multiple step
        if(jump_diagonal){
            possibleMove = new int[8][2];
            possibleMove[0] = new int[]{-jump_range, 0};                // TOP
            possibleMove[1] = new int[]{0, jump_range};                 // RIGHT
            possibleMove[2] = new int[]{jump_range, 0};                 // BOT
            possibleMove[3] = new int[]{0, -jump_range};                // LEFT
            possibleMove[4] = new int[]{-jump_range, jump_range};       // DIAG UP-RIGHT
            possibleMove[5] = new int[]{jump_range, jump_range};        // DIAG BOT-RIGHT
            possibleMove[6] = new int[]{jump_range, -jump_range};       // DIAG BOT-LEFT
            possibleMove[7] = new int[]{-jump_range, -jump_range};      // DIAG UP-LEFT
            nb_option = 8;
        } else {
            possibleMove = new int[4][2];
            possibleMove[0] = new int[]{-jump_range, 0};                // TOP
            possibleMove[1] = new int[]{0, jump_range};                 // RIGHT
            possibleMove[2] = new int[]{jump_range, 0};                 // BOT
            possibleMove[3] = new int[]{0, -jump_range};                // LEFT
            nb_option = 4;
        }

        while (IsWithin){
            randomChooser = (int) (Math.random()*nb_option);
            System.out.println(randomChooser);

            /* Scenari 1 : Can jump back and does so -> nothing special
             * Scenari 2 : Can't jump back, if try to do so -> comeback to a new randomChooser until if doesn't go backward
             * Scenari 3 : Can't jump back, don't try to do so -> nothing special
             */
            // Check for previous move
            if (!jump_back && Arrays.equals(possibleMove[randomChooser], previous_move)){
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
                if(super.get_team() == "Dirty"){
                    super.getMonde().add_dirty(super.get_posy(), super.get_posx());
                } else if (super.get_team() == "Clean"){
                    super.getMonde().delete_dirty(super.get_posy(),super.get_posx());
                }
            }
        }
    }

    @Override
    public void moveAndAct(Robot RobotJumper, GraphiqueInterface graph, Monde map, ArrayList<Robot> robots_inGame) throws InterruptedException{
        RobotJumper R = (RobotJumper) RobotJumper;         // Down-casting to access RobotBasic specific attributes
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

            if(jump_diagonal){
                possibleMove = new int[8][2];
                possibleMove[0] = new int[]{-jump_range, 0};                // TOP
                possibleMove[1] = new int[]{0, jump_range};                 // RIGHT
                possibleMove[2] = new int[]{jump_range, 0};                 // BOT
                possibleMove[3] = new int[]{0, -jump_range};                // LEFT
                possibleMove[4] = new int[]{-jump_range, jump_range};       // DIAG UP-RIGHT
                possibleMove[5] = new int[]{jump_range, jump_range};        // DIAG BOT-RIGHT
                possibleMove[6] = new int[]{jump_range, -jump_range};       // DIAG BOT-LEFT
                possibleMove[7] = new int[]{-jump_range, -jump_range};      // DIAG UP-LEFT
                nb_option = 8;
            } else {
                possibleMove = new int[4][2];
                possibleMove[0] = new int[]{-jump_range, 0};                // TOP
                possibleMove[1] = new int[]{0, jump_range};                 // RIGHT
                possibleMove[2] = new int[]{jump_range, 0};                 // BOT
                possibleMove[3] = new int[]{0, -jump_range};                // LEFT
                nb_option = 4;
            }

            randomChooser = (int) (Math.random()*nb_option);

            // Check for previous move
            if (!R.getJumpBack() && Arrays.equals(possibleMove[randomChooser], previous_move)){
                while (Arrays.equals(possibleMove[randomChooser], previous_move)){
                    randomChooser = (int) (Math.random()*nb_option);                          // re-randomize until not going backward (previous position)
                }
            }

            // Get next x, y and current direction for ease of use later
            int next_y = R.get_posy() + possibleMove[randomChooser][0];
            int next_x = R.get_posx() + possibleMove[randomChooser][1];
            int[] direction = new int[]{possibleMove[randomChooser][0], possibleMove[randomChooser][1]};

            // Get the list of robot in the path
            ArrayList<Robot> robotInPathList = Utilities.whoIsInMyPath(next_y, next_x, direction, robots_inGame);

            if (next_y + robotInPathList.size()*possibleMove[randomChooser][0] < 0 || 
            next_y + robotInPathList.size()*possibleMove[randomChooser][0] >= map.get_nbL() || 
            next_x + robotInPathList.size()*possibleMove[randomChooser][1] < 0 || 
            next_x + robotInPathList.size()*possibleMove[randomChooser][1] >= map.get_nbC()) {
                R.set_functionning(false);
                break;
            }

            // Check if the next position is occupied
            if (robotInPathList.size() == 0){
                // Actualise previous_move
                previous_move[0] = -direction[0];
                previous_move[1] = -direction[1];

                ////// CONTINUE HERE !!!
            }
        }
    }

    // Main (try)
    public static void main(String[] args){
        Monde Monde1 = new Monde();
        System.out.println("World before Robot Jumper going in");
        Monde1.displayWorldMatrix();

        RobotJumper RobotJumper1 = new RobotJumper(Monde1, 7, 4, "Clean");
        RobotJumper1.parcourir();

        System.out.println("World before Robot Jumper after going in");
        Monde1.displayWorldMatrix();
    }
}