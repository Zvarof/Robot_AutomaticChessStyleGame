import java.util.Arrays;

public class RobotGlasseCanon extends Robot {
    // Attributes
    int move_range = 1;                     // default set as 1
    boolean go_back;                        // default set as true
    String name = "GlasseCanon";

    // Constructors
    public RobotGlasseCanon(Monde m, int posy, int posx, String team){
        super(m, posy, posx, team);
        go_back = true;
        super.set_weight(50);
        super.set_initiative(1200);
        super.set_step(2);
    }

    public RobotGlasseCanon(Monde m, int posx, int posy, boolean go_back, String team){
        super(m, posx, posy, team);
        this.go_back = go_back;
        super.set_weight(50);
        super.set_initiative(1200);
        super.set_step(2);
    }

    public RobotGlasseCanon(Monde m, int posx, int posy, boolean go_back, int step, String team){
        super(m, posx, posy, team);
        this.go_back = go_back;
        super.set_weight(50);
        super.set_initiative(1200);
        super.set_step(step);
    }

    // Getters
    public int getMoveRange(){return move_range;}
    public boolean getGoBack(){return go_back;}
    public String getName(){return name;}

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        boolean IsWithin = true;
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
                if(super.get_team() == "Dirty"){
                    super.getMonde().add_dirty(super.get_posy(), super.get_posx());
                } else if (super.get_team() == "Clean"){
                    super.getMonde().delete_dirty(super.get_posy(),super.get_posx());
                }
            }
        }
    }

    // Main (try)
    public static void main(String[] args){
        Monde Monde1 = new Monde();
        System.out.println("World before Robot Jumper going in");
        Monde1.displayWorldMatrix();

        RobotGlasseCanon RobotGlasseCanon1 = new RobotGlasseCanon(Monde1, 5, 4, "Clean");
        RobotGlasseCanon1.parcourir();

        System.out.println("World before Robot Jumper after going in");
        Monde1.displayWorldMatrix();
    }
}