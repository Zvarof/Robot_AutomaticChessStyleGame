import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class GraphiqueInterface {
    // Attributs
    private int gridSizeY;
    private int gridSizeX;
    private JLabel[][] cells;
    private JPanel graphPanel = new JPanel();
    private JFrame graphFrame = new JFrame();

    public static Dictionary<String, String> mapImageName = new Hashtable<>() {{
    put("BasicClean", "Images/BasicClean.jpg");
    put("BasicDirty", "Images/BasicDirty.jpg");
    put("GlasseCannonClean", "Images/GlasseCannonClean.jpg");
    put("GlasseCannonDirty", "Images/GlasseCannonDirty.jpg");
    put("SmasherClean", "Images/SmasherClean.jpg");
    put("SmasherDirty", "Images/SmasherDirty.jpg");
    }};

    // Constructors
    public GraphiqueInterface(int gridSizeY, int gridSizeX){
        this.gridSizeY = gridSizeY;
        this.gridSizeX = gridSizeX;
        initializeCellsReference(gridSizeY, gridSizeX);
        createMapWithEmptyCell(gridSizeY, gridSizeX, cells);
    }

    // Getters
    public int get_gridSizeY(){return gridSizeY;}
    public int get_gridSizeX(){return gridSizeX;}
    public JLabel[][] get_cells(){return cells;}
    public JPanel get_graphPanel(){return graphPanel;}
    public JFrame get_graphFrame(){return graphFrame;}

    // Setters

    // Methods
        
    // Adding image at the specified position
    public void addImage(int y, int x, ImageIcon Icon, JLabel[][] cells){
        Image image = Icon.getImage();
        Image scaledImage = image.getScaledInstance(graphPanel.getWidth()/gridSizeX, graphPanel.getHeight()/gridSizeY, java.awt.Image.SCALE_SMOOTH);
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
        Image scaledImage = image.getScaledInstance(graphPanel.getWidth()/gridSizeX, graphPanel.getHeight()/gridSizeY, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Creating the initial image for the Robot at the start of a game
    public void createAllRobotImage(ArrayList<Robot> RobotList, GraphiqueInterface graph){
        for (Robot robot : RobotList){
            ImageIcon I = graph.getImageIconForRobot(robot);
            addImage(robot.get_posy(), robot.get_posx(), I, graph.cells);
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
                cells[i][j].setOpaque(true);        // Super important for proper display of background and images
            }
        }
    }

    // Creating a map in the form of JLabel managed by a GridLayout
    public void createMapWithEmptyCell(int y, int x, JLabel[][] cells){
        graphPanel.setLayout(new GridLayout(y, x));
        for (int i=0; i<y; i++) {
            for (int j=0; j<x; j++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                graphPanel.add(cells[i][j]);           // Note that cell are reference        
            }
        }
    }

    // Main (try)
    public static void main(String[] args) throws InterruptedException {}
}