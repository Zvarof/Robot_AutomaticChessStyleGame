/* When dealing with coordinate, we choose to set the first value as y (line number) 
and the second value as x (colonne number)
*/
import java.util.ArrayList;

public abstract class Robot {
    // Attributes
    private int ID;
    private String name = "Undefined";      // for definition control
    private int posy;
    private int posx;
    private Monde m;
    private boolean functionning;           // default as true
    private int weight;
    private int initiative;
    private int step = 1;                   // default as 1
    private int price;
    private String team = "Undefined";      
    private static int robotCreated = 0;

    // Constructors
    public Robot(Monde m, int posy, int posx, String team){
        this.ID = robotCreated + 1;
        this.m = m;
        this.posy = posy;
        this.posx = posx;
        this.functionning = true;
        this.team = team;
        robotCreated += 1;
    }

    public Robot(Monde m, String team){
        this.ID = robotCreated + 1;
        this.m = m;
        this.posy = (int) Math.random()*m.get_world_matrix()[0].length;
        this.posx = (int) Math.random()*m.get_world_matrix().length;
        this.functionning = true;
        this.team = team;
        robotCreated += 1;
    }

    /* Robot using those constructors are not readily playable as such, 
    they are lacking some attribute */
    public Robot(String team, String name, int posy, int posx){
        this.posy = posy;
        this.posx = posx;
        this.functionning = true;
        this.team = team;
    }

    public Robot(String team){
        this.team = team;
        this.functionning = true;
    }

    // Getters

    public int get_ID(){return ID;}
    public String getName(){return name;}
    public Monde getMonde(){return m;}
    public int get_posy(){return posy;}
    public int get_posx(){return posx;}
    public boolean get_functionning(){return functionning;}
    public int get_weight(){return weight;}
    public int get_initiative(){return initiative;}
    public int get_step(){return step;}
    public int get_price(){return price;}
    public String get_team(){return team;}

    // Setters

    public void set_name(String s){this.name = s;}
    public void set_posy(int posy){this.posy = posy;}
    public void set_posx(int posx){this.posx = posx;}
    public void set_functionning(boolean functionning){this.functionning = functionning;}
    public void set_weight(int weight){this.weight = weight;}
    public void set_initiative(int initiative){this.initiative = initiative;}
    public void set_step(int step){this.step = step;}
    public void set_price(int price){this.price = price;}
    public void set_team(String team){this.team = team;}

    // Methods

    // enable to move coordonate of a Robot to a new position, if available.
    public void MoveTo(int i, int j) throws IndexOutOfBoundsException {
        int nbL = m.get_world_matrix().length;
        int nbC = m.get_world_matrix()[0].length;
        if (i > nbL || i < 0 || j > nbC || j < 0){
            throw new IndexOutOfBoundsException("The given indexes (" + i + "," + j + ") are invalid");
        } else {
            posy = i;
            posx = j;        
        }
    }

    // manage the robot movements
    public abstract void parcourir() throws IndexOutOfBoundsException;

    // manage the robot movements and actions
    public abstract void moveAndAct(Robot R, ScreenPlaying graph, Monde map, ArrayList<Robot> robots_inGame);

    // Main
    public static void main(String[] args){}
}