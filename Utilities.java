import java.util.ArrayList;
import java.util.Comparator;

public class Utilities {
    // Attributs


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
                if (map.get_world_matrix()[i][j]){
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
                if (! map.get_world_matrix()[i][j]){
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

    // Main (for testing)
    public static void main(String[] args){
        Monde monde1 = new Monde();
        ArrayList<Robot> robotList = new ArrayList<>();

        Robot robotTest = new RobotBasic(monde1, 6, 5, "Clean");         // ID 1
        Robot robot1 = new RobotBasic(monde1, 5, 5, "Dirty");            // ID 2
        Robot robot2 = new RobotBasic(monde1, 4, 5, "Dirty");            // ID 3
        Robot robot3 = new RobotBasic(monde1, 2, 5, "Clean");            // ID 4
        Robot robot4 = new RobotBasic(monde1, 1, 5, "Clean");            // ID 5


        robotList.add(robotTest); robotList.add(robot1); robotList.add(robot2); robotList.add(robot3); robotList.add(robot4);
        robotTest.set_initiative(450);
        robot1.set_initiative(388);
        robot2.set_initiative(512);
        robot3.set_initiative(708);
        robot4.set_initiative(388);

        for (Robot robot : robotList){
            System.out.println("Robot : " + robot.get_ID());
        }
        
        System.out.println();
        System.out.println();
        sortRobotListByASCInitiative(robotList);

        for (Robot robot : robotList){
            System.out.println("Robot : " + robot.get_ID() + "    "  + robot.get_initiative());
        }
    }
}
