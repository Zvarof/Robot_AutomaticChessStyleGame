import java.util.ArrayList;

public class RobotpollueurToutDroit extends Robot {
    // Attributes
    int ColDepart;

    // Constructors
    public RobotpollueurToutDroit(Monde m, int posx, int posy, String team, int ColDepart){
        super(m, posx, posy, team);
        this.ColDepart = ColDepart;
    }

    // Methods
    public void parcourir() throws IndexOutOfBoundsException {
        int nbL = super.getMonde().get_world_matrix().length;
        super.set_posx(ColDepart);
        Monde T = super.getMonde();
        
        for (int i=0; i<nbL; i++){
            super.set_posy(i);
            T.add_dirty(i, ColDepart);
        }
    }

    public void moveAndAct(Robot R, ScreenPlaying graph, Monde map, ArrayList<Robot> robots_inGame){};
}

