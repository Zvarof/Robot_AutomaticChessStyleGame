import java.util.ArrayList;
import java.awt.Point;

public class Player {
    // Attributs
    private String playerName;
    private String playerTeam;
    private int playerBankAccount = 100;         // default = 100
    private boolean ready = false;
    private int score = 0;
    private ArrayList<Point> InsidedCells = new ArrayList<>();

    // Getters
    public String getPlayerName(){return playerName;}
    public String getPlayerTeam(){return playerTeam;}
    public int getPlayerBankAccount(){return playerBankAccount;}
    public boolean getReady(){return ready;}
    public int getScore(){return score;}
    public ArrayList<Point> getInsidedCells(){return InsidedCells;};

    // Setters
    public void setPlayerName(String playerName){this.playerName = playerName;}
    public void setPlayerTeam(String playerTeam){this.playerTeam = playerTeam;}
    public void setPlayerBankAccount(int playerBankAccount){this.playerBankAccount = playerBankAccount;}
    public void setReady(boolean ready){this.ready = ready;}
    public void setScore(int score){this.score = score;}

    // Constructors
    public Player(String playerName, String playerTeam){
        this.playerName = playerName;
        this.playerTeam = playerTeam;
        InsidedCells = new ArrayList<>();
    }

    // Method
    public boolean hasInsided(int y, int x){
        for (Point point : InsidedCells){
            if(point.y == y && point.x == x){
                return true;
            }
        }
        return false;
    }

    // Main (try)
    public static void main(String[] args) throws InterruptedException {}
}
