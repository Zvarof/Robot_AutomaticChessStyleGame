import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class ScreenPlaying {
    // Attributs
    private GameManager gameManager;
    private int gridSizeY;
    private int gridSizeX;
    private JLabel[][] cells;
    private JPanel playPanel = new JPanel();
    private JFrame playFrame = new JFrame();
    
    // Enable to load the image corresponding to the Robot's name
    public static Dictionary<String, String> mapImageName = new Hashtable<>() {{
    put("BasicClean", "Images/BasicClean.jpg");
    put("BasicDirty", "Images/BasicDirty.jpg");
    put("GlasseCanonClean", "Images/GlasseCanonClean.jpg");
    put("GlasseCanonDirty", "Images/GlasseCanonDirty.jpg");
    put("SmasherClean", "Images/SmasherClean.jpg");
    put("SmasherDirty", "Images/SmasherDirty.jpg");
    put("JumperClean", "Images/JumperClean.jpg");
    put("JumperDirty", "Images/JumperDirty.jpg");
    put("UndercoverClean", "Images/UndercoverClean.jpg");
    put("UndercoverDirty", "Images/UndercoverDirty.jpg");
    }};

    // Constructors
    public ScreenPlaying(int gridSizeY, int gridSizeX, GameManager gameManager){
        this.gameManager = gameManager;
        this.gridSizeY = gridSizeY;
        this.gridSizeX = gridSizeX;
        initializeCellsReference(gridSizeY, gridSizeX);
        createMapWithEmptyCell(gridSizeY, gridSizeX, cells);
        playPanel.setSize(1000, 1000);
        playFrame.setVisible(false);
    }

    public ScreenPlaying(int gridSizeY, int gridSizeX, String[] availableRobots, GameManager gameManager){
        this.gameManager = gameManager;
        this.gridSizeY = gridSizeY;
        this.gridSizeX = gridSizeX;
        initializeCellsReference(gridSizeY, gridSizeX);
        createMapWithEmptyCell(gridSizeY, gridSizeX, cells);
        playPanel.setSize(1000, 1000);
        playFrame.setVisible(false);
    }

    // Getters
    public int get_gridSizeY(){return gridSizeY;}
    public int get_gridSizeX(){return gridSizeX;}
    public JLabel[][] get_cells(){return cells;}
    public JPanel get_playPanel(){return playPanel;}
    public JFrame get_playFrame(){return playFrame;}

    // Setters

    // Methods
        
    // Adding image at the specified position
    public void addImage(int y, int x, ImageIcon Icon, JLabel[][] cells){
        Image image = Icon.getImage();
        Image scaledImage = image.getScaledInstance(playPanel.getWidth()/gridSizeX, playPanel.getHeight()/gridSizeY, java.awt.Image.SCALE_SMOOTH);
        cells[y][x].setIcon(new ImageIcon(scaledImage));
    }
    
    // Moving existing image to the specified position
    public void moveImage(int new_y, int new_x, JLabel[][] cells, JLabel cell){
        if (cell.getIcon() == null){
            return;
        } else {
            cells[new_y][new_x].setIcon(cell.getIcon());
            cell.setIcon(null);
        }
    }

    public ImageIcon getImageIconForRobot(Robot R){
        Image image = new ImageIcon(mapImageName.get(R.getName() + R.get_team())).getImage();
        Image scaledImage = image.getScaledInstance(playPanel.getWidth()/gridSizeX, playPanel.getHeight()/gridSizeY, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Creating the initial image for the Robot at the start of a game
    public void createAllRobotImage(ArrayList<Robot> RobotList){
        for (Robot robot : RobotList){
            ImageIcon I = getImageIconForRobot(robot);
            addImage(robot.get_posy(), robot.get_posx(), I, cells);
        }
    }

    // Initialize the cell 2D array reference that allow easy manipulation of the map through indexing
    public void initializeCellsReference(int y, int x){
        cells = new JLabel[y][x];
        for (int i=0; i<y; i++) {
            for (int j=0; j<x; j++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j] = cell;
                cells[i][j].setOpaque(true);        // Necessary for proper display of background and images
            }
        }
    }

    // Creating a map in the form of JLabel managed by a GridLayout
    public void createMapWithEmptyCell(int y, int x, JLabel[][] cells){
        playPanel.setLayout(new GridLayout(y, x));
        for (int i=0; i<y; i++) {
            for (int j=0; j<x; j++) {
                playPanel.add(cells[i][j]);           // Note that cell are reference        
            }
        }
    }

    public void matchChoosingPanel(ScreenRobotChoosing sRC){
        // cleaning previous cells's icons
        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[0].length; j++){
                cells[i][j].setIcon(null);
            }
        }

        // adding actualized cells's icons
        for(Robot robot : gameManager.get_robotInGame()){
            cells[robot.get_posy()][robot.get_posx()].setIcon(getImageIconForRobot(robot));            
        }
    }

    // Main (try)
    public static void main(String[] args){}
}