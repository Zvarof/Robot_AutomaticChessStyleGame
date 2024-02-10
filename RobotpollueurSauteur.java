import java.util.Arrays;

public class RobotpollueurSauteur extends Robot {
    // Attributes
    int jump_range;             // default set as 2
    boolean jump_diagonal;      // default set as false
    boolean jump_back;          // default set as true

    // Constructors
    public RobotpollueurSauteur(Monde m, int posx, int posy){
        super(m, posx, posy);
        jump_range = 2;
        jump_diagonal = false;
        jump_back = true;
    }

    public RobotpollueurSauteur(Monde m, int posx, int posy, int jump_range){
        super(m, posx, posy);
        this.jump_range = jump_range;
        this.jump_diagonal = false;
        this.jump_back = true;
    }

    public RobotpollueurSauteur(Monde m, int posx, int posy, int jump_range, boolean jump_diagonal){
        super(m, posx, posy);
        this.jump_range = jump_range;
        this.jump_diagonal = jump_diagonal;
        this.jump_back = true;
    }

    public RobotpollueurSauteur(Monde m, int posx, int posy, int jump_range, boolean jump_diagonal, boolean jump_back){
        super(m, posx, posy);
        this.jump_range = jump_range;
        this.jump_diagonal = jump_diagonal;
        this.jump_back = jump_back;
    }

    // 

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        boolean IsWithin = true;
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
            possibleMove = new int[8][2];
            possibleMove[0] = new int[]{-jump_range, 0};                // TOP
            possibleMove[1] = new int[]{0, jump_range};                 // RIGHT
            possibleMove[2] = new int[]{jump_range, 0};                 // BOT
            possibleMove[3] = new int[]{0, -jump_range};                // LEFT
            nb_option = 4;
        }

        while (IsWithin){
            randomChooser = (int) Math.random()*nb_option;

            // Check for previous move
            if (jump_back && Arrays.equals(possibleMove[randomChooser], previous_move)){
                continue;
            }

            // Check if the next position is within the map, if not break outsside the while loop
            if (super.get_posx() + possibleMove[randomChooser][2] < 0 
            || super.get_posx() + possibleMove[randomChooser][2] >= super.getMonde().get_nbC()
            || super.get_posy() + possibleMove[randomChooser][1] < 0
            || super.get_posy() + possibleMove[randomChooser][1] >= super.getMonde().get_nbL()){
                IsWithin = false;
            } else {
                super.set_posx(super.get_posx() + possibleMove[randomChooser][2]);
                super.set_posy(super.get_posy() + possibleMove[randomChooser][1]);
                // !!!!!!!!! Need to update the positions for previous position
            }
        }
    }
}