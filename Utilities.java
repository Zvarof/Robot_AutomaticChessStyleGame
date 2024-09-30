import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class Utilities {
    // Attributs
    public static ArrayList<Robot> default_robotAvailableList = new ArrayList<>(Arrays.asList(
        new RobotBasic("Clean"), 
        new RobotBasic("Dirty"),
        new RobotJumper("Clean"),
        new RobotJumper("Dirty"),
        new RobotGlasseCanon("Clean"),
        new RobotGlasseCanon("Dirty"),
        new RobotSmasher("Clean"),
        new RobotSmasher("Dirty"),
        new RobotUndercover("Clean"),
        new RobotUndercover("Dirty")
    ));

    public static String currentPlayerStrg = "<html><h2 align='center'>Current<br>Player :</h2>";
    public static String bankAccountStrg = "<html><h2 align='right'>Bank<br>account </h2>";
    public static String timerStrg = "<html><h2 align='center'>Timer </h2>";
    public static String action = "<html><h2 align='center'>Action : </h2>";
    public static String pass = "<html><h2 align='center'>Pass</h2>";
    public static String ready = "<html><h2 align='center'>Ready</h2>";

    public static Color lightRed = new Color(187, 41, 118);
    public static Color lightBlue = new Color(160, 190, 255);

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

    public static Dictionary<String, String> mapGifName = new Hashtable<>() {{
        put("VictoryDanceMusk", "Videos/ElonMuskVictoryDance");
        put("RobotFall", "Videos/RobotFall.gif");
    }};

    // Methods
    public static int getRobotListWeightSum(ArrayList<Robot> RobotList){
        int weight_sum = 0;
        for (Robot robot : RobotList){
            weight_sum += robot.get_weight();
        }
        return weight_sum;
    }

    public static void sortRobotListByASCInitiative(ArrayList<Robot> robotList){
        robotList.sort(Comparator.comparingInt(robot -> -robot.get_initiative()));       // Sort by DESC initiative (dirty way)
    }

    public static int getTotalPointClean(Monde map){
        int clean_sum = 0;
        for (int i=0; i<map.get_nbL(); i++){
            for (int j=0; j<map.get_nbC(); j++){
                if (map.get_world_matrix()[i][j].equals("clean")){
                    clean_sum += 1;
                }
            }
        }
        return clean_sum;
    }

    public static int getTotalPointDirty(Monde map){
        int dirty_sum = 0;
        for (int i=0; i<map.get_nbL(); i++){
            for (int j=0; j<map.get_nbC(); j++){
                if (map.get_world_matrix()[i][j].equals("dirty")){
                    dirty_sum += 1;
                }
            }
        }
        return dirty_sum;
    }

    public static ArrayList<Robot> whoIsInMyPath(int y, int x, int[] direction, ArrayList<Robot> robots_inGame){
        ArrayList<Robot> robot_to_consider = new ArrayList<>();
        boolean reloop = false;

        do {
            reloop = false;
            for (Robot robot : robots_inGame){
                if (robot.get_posy() == y && robot.get_posx() == x){
                    robot_to_consider.add(robot);
                    y += direction[0];
                    x += direction[1];
                    reloop = true;
                    break;
                }
            }
        }  
        while (reloop);

        return robot_to_consider;
    }

    public static boolean IsOutOfMap(int y, int x, int mapNbL, int mapNbC){
        if (y<0 || y >= mapNbL || x<0 || x >= mapNbC){
            return true;
        } else {
            return false;
        }
    }

    public static void dealWithOutOfMap(Robot robot){
        robot.set_functionning(false);
        robot.set_posy(999);
        robot.set_posx(999);
    }

    public static void removeRobotOutOfMap(ArrayList<Robot> robot_inGame){
        // Using iterator to avoid in-place modification issue
        Iterator<Robot> iterator = robot_inGame.iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            if(!robot.get_functionning()){
                iterator.remove();
            }
        }
    }

    public static int indexOfGlasseCanonIfPresent(ArrayList<Robot> robotInPathList){
        for (int i=0; i<robotInPathList.size(); i++){
            if (robotInPathList.get(i).getName() == "GlasseCanon"){
                return i;
            }
        }
        return -1;
    }

    public static boolean isCellAccessible(Player currentPlayer, int y, int x){
        if(currentPlayer.getPlayerTeam().equals("Clean")){
            return (y>=0 && y<=3) || currentPlayer.hasInsided(y, x);
        } else if (currentPlayer.getPlayerTeam().equals("Dirty")){
            return (y>=5 && y<=8) || currentPlayer.hasInsided(y, x);
        } else {
            return false;
        }
    }

    /* dealWithBump 
     * Scenario 1 : At least 1 glasse canon is present in the robotInPathList, so it will get destroyed.
     * Scenario 1a : The total sum of robotInPathList is too great to allow movement -> No movement
     * Scenario 1b : The total sum of robotInPathList is light enough, every robot before the first glasse canon and the moving robot will be moved
     * Scenario 2 : There is no glasse canon in robotInPathList
     * Scenario 2a : The total sum of robotInPathList is too great to allow movement -> No movement
     * Scenario 2b : The total sum of robotInPathList is light enough, every robot will me moved
    */

    public static void dealWithBump(Robot movingRobot, ArrayList<Robot> robotInPathList, int[] direction, ScreenPlaying graph, Monde map){
        if (indexOfGlasseCanonIfPresent(robotInPathList) != -1){
            int gCIndex = indexOfGlasseCanonIfPresent(robotInPathList);     // gC = glasse Canon
            // Remove the glasse canon from the map
            dealWithOutOfMap(robotInPathList.get(gCIndex));
            graph.get_cells()[robotInPathList.get(gCIndex).get_posy()][robotInPathList.get(gCIndex).get_posx()].setIcon(null);
            if (movingRobot.get_weight() > 1.2 * getRobotListWeightSum(robotInPathList)){
                // Move each robot, starting by the end of list (from the first glasse canon)
                for (int i=gCIndex-1; i>=0; i--){
                    Robot R = robotInPathList.get(i);
                    if (IsOutOfMap(R.get_posy() + direction[0], R.get_posx() + direction[1], map.get_nbL(), map.get_nbC())){
                        graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                        R.set_functionning(false);
                        continue;
                    }
                    String priority = graph.get_cells()[R.get_posy()][R.get_posx()].getText();
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
                    R.set_posy(R.get_posy() + direction[0]);
                    R.set_posx(R.get_posx() + direction[1]);
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText(priority);
                }
                // Moving robot (can not be out of map if other robot in the path, so no need to deal with that)
                String priority = graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].getText();
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setIcon(null);
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setText("");
                movingRobot.set_posy(movingRobot.get_posy() + direction[0]);
                movingRobot.set_posx(movingRobot.get_posx() + direction[1]);
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setIcon(graph.getImageIconForRobot(movingRobot));
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setText(priority);
            }
        } else {
            if (movingRobot.get_weight() > 1.2 * getRobotListWeightSum(robotInPathList)){
                // The moving robot is heavy enough to move other Robots in his path
                for (int i=robotInPathList.size()-1; i>=0; i--){
                    Robot R = robotInPathList.get(i);
                    if (IsOutOfMap(R.get_posy() + direction[0], R.get_posx() + direction[1], map.get_nbL(), map.get_nbC())){
                        graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                        graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
                        R.set_functionning(false);
                        continue;
                    }
                    String priority = graph.get_cells()[R.get_posy()][R.get_posx()].getText();
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(null);
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText("");
                    R.set_posy(R.get_posy() + direction[0]);
                    R.set_posx(R.get_posx() + direction[1]);
                    graph.get_cells()[R.get_posy()][R.get_posx()].setIcon(graph.getImageIconForRobot(R));
                    graph.get_cells()[R.get_posy()][R.get_posx()].setText(priority);
                }
                // Moving robot (can not be out of map is other robot in the path, so no need to deal with that)
                String priority = graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].getText();
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setIcon(null);
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setText("");
                movingRobot.set_posy(movingRobot.get_posy() + direction[0]);
                movingRobot.set_posx(movingRobot.get_posx() + direction[1]);
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setIcon(graph.getImageIconForRobot(movingRobot));
                graph.get_cells()[movingRobot.get_posy()][movingRobot.get_posx()].setText(priority);
            } else {return;}
        }
    }

    public static void ActDirtyClean(Robot R, ScreenPlaying graph, Monde map){
        // Change the color of the background and add cell-value to map for score counting
        if(R.get_team().equals("Dirty")){
            map.add_dirty(R.get_posy(), R.get_posx());
            graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Utilities.lightRed);
        } else if (R.get_team().equals("Clean")){
            map.add_clean(R.get_posy(), R.get_posx());
            graph.get_cells()[R.get_posy()][R.get_posx()].setBackground(Utilities.lightBlue);
        }
    }

    public static ImageIcon getImageIconForRobot(Robot R, int height, int width){
        // Return a scaled instance of the image corresponding to the robot
        Image image = new ImageIcon(mapImageName.get(R.getName() + R.get_team())).getImage();
        Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static void changeJLabel(JPanel jp, int index, String newText){
        Component[] components = jp.getComponents();
        if (index >= 0 && index < components.length){
            if (components[index] instanceof JLabel){
                JLabel label = (JLabel) components[index];
                label.setText(newText);
            } else {
                System.out.println("The component is NOT a JLabel");
            }
        } else {
            System.out.println("The index is invalid");
        }
    }

    public static void displayPriorities(ArrayList<Robot> robot_inGame, JLabel[][] cells){
        int sizeList = robot_inGame.size();
        for (int i = 0; i < sizeList; i++){
            Robot robot = robot_inGame.get(i);
            int priority = i + 1;

            JLabel robotLabel = cells[robot.get_posy()][robot.get_posx()];

            robotLabel.setText(String.valueOf(priority));
            robotLabel.setFont(new Font("Arial", Font.BOLD, 24));
            robotLabel.setHorizontalTextPosition(JLabel.CENTER);
            robotLabel.setVerticalTextPosition(JLabel.CENTER);
            robotLabel.setForeground(Color.RED);
        }
    }

    // Main
    public static void main(String[] args){}
}