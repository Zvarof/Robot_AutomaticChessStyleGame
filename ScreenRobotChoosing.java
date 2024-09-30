import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Transferable;

import javax.swing.*;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ScreenRobotChoosing {
    // Attributs
    private ArrayList<Robot> availableRobots = Utilities.default_robotAvailableList;                  
    private JFrame display = new JFrame();
    private JPanel mapDisplayPanel;
    private JPanel robotSelectionPanel;
    private int mainPanelSizeX = 1000;
    private int mainPanelSizeY = 1000;
    private int mapSizeX;
    private int mapSizeY;
    private GameManager gameManager;
    private Player currentPlayerChosing;

    // Getters
    public JFrame get_DisplayFrame(){return display;}
    public JPanel get_mapDisplayPanel(){return mapDisplayPanel;}
    public JPanel get_robotSelectionPanel(){return robotSelectionPanel;}
    public int get_mapSizeX(){return mapSizeX;}
    public int get_mapSizeY(){return mapSizeY;}
    public Player get_currentPlayerChoosing(){return currentPlayerChosing;}

    // Setters
    public void set_currentPlayerChoosing(Player currentPlayerChosing){this.currentPlayerChosing = currentPlayerChosing;}

    // Constructors
    public ScreenRobotChoosing(ArrayList<Robot> availableRobots, ScreenPlaying screenPlay, GameManager gameManager){
        display.setTitle("Robot Placement Interface");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(new BorderLayout());
        this.availableRobots = availableRobots;
        this.mapSizeY = screenPlay.get_gridSizeY();
        this.mapSizeX = screenPlay.get_gridSizeX();
        this.gameManager = gameManager;

        // Map display panel
        mapDisplayPanel = new JPanel();
        mapDisplayPanel.setPreferredSize(new Dimension(mainPanelSizeX, mainPanelSizeY));
        mapDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mapDisplayPanel.setDropTarget(new DropTarget(mapDisplayPanel, new DropListener()));
        mapDisplayPanel.setLayout(new GridLayout(mapSizeX, mapSizeY));
        for (int i=0; i<mapSizeY; i++){
            for (int j=0; j<mapSizeX; j++){
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.setOpaque(true);
                cell.setDropTarget(new DropTarget(cell, new DropListener()));
                if (i <= 3){cell.setBackground(Utilities.lightBlue);}
                else if (i>=5){cell.setBackground(Utilities.lightRed);}
                mapDisplayPanel.add(cell);
            }
        }
        display.add(mapDisplayPanel, BorderLayout.CENTER);

        // Robot selection panel
        JPanel robotSelectionPanel = new JPanel(new GridLayout(availableRobots.size()/2 +2,3));
        robotSelectionPanel.setSize(new Dimension(300, 1000));

        // Ajout de l'en-tête
        this.currentPlayerChosing = gameManager.get_playerList().get((int) Math.random()*2);
        JLabel pN = new JLabel(Utilities.currentPlayerStrg + currentPlayerChosing.getPlayerName());
        pN.setHorizontalAlignment(SwingConstants.CENTER);
        robotSelectionPanel.add(pN);
        JLabel pBA = new JLabel(Utilities.bankAccountStrg + currentPlayerChosing.getPlayerBankAccount());
        pBA.setHorizontalAlignment(SwingConstants.CENTER);
        robotSelectionPanel.add(pBA);
        JLabel timer = new JLabel(Utilities.timerStrg);
        timer.setHorizontalAlignment(SwingConstants.CENTER);
        robotSelectionPanel.add(timer);

        // Creating robot name and icons (JLabels) for selection, and adding drag-n-drop handler/listener
        for (int i=0; i<availableRobots.size(); i+=2){
            JLabel robotName = new JLabel("<html>" + availableRobots.get(i).getName() + "<br>Price: " + availableRobots.get(i).get_price());
            robotName.setHorizontalAlignment(SwingConstants.CENTER);
            robotSelectionPanel.add(robotName);

            JLabel robotIconClean = new JLabel();
            ImageIcon imageIconClean = Utilities.getImageIconForRobot(availableRobots.get(i), robotSelectionPanel.getHeight()/(availableRobots.size()/2 +2), robotSelectionPanel.getWidth()/3);
            robotIconClean.setIcon(imageIconClean);
            robotIconClean.setTransferHandler(new RobotTransferHandler(availableRobots.get(i)));
            robotIconClean.setEnabled(true);
            robotIconClean.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt){
                    if (currentPlayerChosing.getPlayerTeam().equals("Clean")){
                        JComponent comp = (JComponent) evt.getSource();
                        robotIconClean.getTransferHandler().exportAsDrag(comp, evt, TransferHandler.COPY);
                    }
                }
            });
            robotSelectionPanel.add(robotIconClean);

            JLabel robotIconDirty = new JLabel();
            ImageIcon imageIconDirty = Utilities.getImageIconForRobot(availableRobots.get(i+1), robotSelectionPanel.getHeight()/(availableRobots.size()/2 +2), robotSelectionPanel.getWidth()/3);
            robotIconDirty.setIcon(imageIconDirty);
            robotIconDirty.setTransferHandler(new RobotTransferHandler(availableRobots.get(i+1)));
            robotIconDirty.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt){
                    if (currentPlayerChosing.getPlayerTeam().equals("Dirty")){
                        JComponent comp = (JComponent) evt.getSource();
                        robotIconDirty.getTransferHandler().exportAsDrag(comp, evt, TransferHandler.COPY);
                    }
                }
            });
            robotIconDirty.setEnabled(true);
            robotSelectionPanel.add(robotIconDirty);
        }

        // Adding the panel to the main Frame, adjusting it and making it visible
        display.add(robotSelectionPanel, BorderLayout.EAST);
        display.pack();     // Make the display adjust to prefered size of its components
        display.setVisible(false);

        // Ajout des boutons passe et ready
        JLabel action = new JLabel(Utilities.action);
        action.setHorizontalAlignment(SwingConstants.CENTER);
        robotSelectionPanel.add(action);

        JButton pass = new JButton("Pass");
        pass.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (! currentPlayerChosing.getReady()){currentPlayerChosing.setPlayerBankAccount(Math.max(currentPlayerChosing.getPlayerBankAccount()-1, 0));}
                Player nextPlayer = (currentPlayerChosing == gameManager.get_playerList().get(0)) ? gameManager.get_playerList().get(1) : gameManager.get_playerList().get(0);
                currentPlayerChosing = nextPlayer;
                Utilities.changeJLabel(robotSelectionPanel, 0, Utilities.currentPlayerStrg + currentPlayerChosing.getPlayerName());
                Utilities.changeJLabel(robotSelectionPanel, 1, Utilities.bankAccountStrg + currentPlayerChosing.getPlayerBankAccount());

                // Control if we can switch to playingScreen
                if((gameManager.get_playerList().get(0).getReady() || gameManager.get_playerList().get(0).getPlayerBankAccount() <= 0)
                && (gameManager.get_playerList().get(1).getReady() || gameManager.get_playerList().get(1).getPlayerBankAccount() <= 0)){
                    gameManager.switchFromChoiceToPlaying(gameManager.get_ScreenRobotChoosing(), gameManager.get_ScreenPlaying());
                }
            }
        });
        robotSelectionPanel.add(pass); 
        
        JButton ready = new JButton("Ready");
        ready.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Player nextPlayer = (currentPlayerChosing == gameManager.get_playerList().get(0)) ? gameManager.get_playerList().get(1) : gameManager.get_playerList().get(0);
                currentPlayerChosing = nextPlayer;
                currentPlayerChosing.setReady(true);
                Utilities.changeJLabel(robotSelectionPanel, 0, Utilities.currentPlayerStrg + currentPlayerChosing.getPlayerName());
                Utilities.changeJLabel(robotSelectionPanel, 1, Utilities.bankAccountStrg + currentPlayerChosing.getPlayerBankAccount());

                // Control if we can switch to playingScreen
                if((gameManager.get_playerList().get(0).getReady() || gameManager.get_playerList().get(0).getPlayerBankAccount() <= 0)
                && (gameManager.get_playerList().get(1).getReady() || gameManager.get_playerList().get(1).getPlayerBankAccount() <= 0)){
                    gameManager.switchFromChoiceToPlaying(gameManager.get_ScreenRobotChoosing(), gameManager.get_ScreenPlaying());
                }
            }
        });
        robotSelectionPanel.add(ready);

        // Adding as an attribute for an external access
        this.robotSelectionPanel = robotSelectionPanel;
    }

    // Methods

    public void matchPlayPanel(ScreenPlaying sP){
        // Clearing previous Icon
        for(Component component : mapDisplayPanel.getComponents()){
            if(component instanceof JLabel){
                ((JLabel) component).setIcon(null);
            }
        }
        // Setting new Icons, Backgrounds and Text
        for(int i=0; i<sP.get_cells().length; i++){
            for(int j=0; j<sP.get_cells()[0].length; j++){
                if (mapDisplayPanel.getComponent(i*mapSizeX + j) instanceof JLabel){
                    ((JLabel)mapDisplayPanel.getComponent(i*mapSizeX + j)).setIcon(sP.get_cells()[i][j].getIcon());
                    ((JLabel)mapDisplayPanel.getComponent(i*mapSizeX + j)).setBackground(sP.get_cells()[i][j].getBackground());
                    ((JLabel)mapDisplayPanel.getComponent(i*mapSizeX + j)).setText(sP.get_cells()[i][j].getText());
                }
            }
        }
    }

    class RobotTransferHandler extends TransferHandler {
        // Attributs
        private Robot robot;

        public RobotTransferHandler(Robot robot){
            this.robot = robot;
        }

        @Override
        public int getSourceActions(JComponent c){  // Actions allows
            return TransferHandler.COPY;
        }

        @Override 
        protected Transferable createTransferable(JComponent c){
            return new RobotTransferable(robot);
        }
    }

    class RobotTransferable implements Transferable {
        private Robot robot;
        public static final DataFlavor ROBOT_DATA_FLAVOR = new DataFlavor(Robot.class, "Robot");

        public RobotTransferable(Robot robot){
            this.robot = robot;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors(){
            return new DataFlavor[]{ROBOT_DATA_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor){
            return flavor.equals(ROBOT_DATA_FLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(ROBOT_DATA_FLAVOR)){
                return robot;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }


    class DropListener implements DropTargetListener {
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {}

        @Override
        public void dragOver(DropTargetDragEvent dtde) {}

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {}

        @Override
        public void dragExit(DropTargetEvent dte) {}
        
        @Override
        public void drop(DropTargetDropEvent dtde) {
            Transferable transferable = dtde.getTransferable();
            if (transferable.isDataFlavorSupported(RobotTransferable.ROBOT_DATA_FLAVOR)){
                try {
                    Robot robot = (Robot) transferable.getTransferData(RobotTransferable.ROBOT_DATA_FLAVOR);
                    Component target = dtde.getDropTargetContext().getComponent();
                    if (target instanceof JLabel){
                        // Control that the player has enough money
                        if (currentPlayerChosing.getPlayerBankAccount() < robot.get_price()){
                            System.out.println(currentPlayerChosing.getPlayerName() + " n'a pas assez d'argent");
                            dtde.dropComplete(false);
                            return;
                        }
                        // Control if a robot is already present in the cell
                        if (((JLabel)target).getIcon() != null){
                            System.out.println("This cell is already taken !");
                            dtde.dropComplete(false);
                            return;
                        }
                        else {
                            // Obtain robot position within the panel
                                // Note : Point dropPoint = dtde.getLocation() get the relative position within the JLabel NOT JPanel
                            Point dropPoint = SwingUtilities.convertPoint(dtde.getDropTargetContext().getComponent(), dtde.getLocation(), mapDisplayPanel);
                            int cell_length = mapDisplayPanel.getHeight()/mapSizeY;
                            int cell_width = mapDisplayPanel.getWidth()/mapSizeX;
                            int y = dropPoint.y / cell_length;
                            int x = dropPoint.x / cell_width;

                            if (! Utilities.isCellAccessible(currentPlayerChosing, y, x)){
                                dtde.rejectDrop();
                                System.out.println("This cell is not accessible for your team");
                                return;
                            }

                            JLabel targetLabel = (JLabel) target;
                            ImageIcon imageIcon = Utilities.getImageIconForRobot(robot, 105, 105);
                            targetLabel.setIcon(imageIcon);
                            // Store the information about the robot
                            gameManager.addRobotInGame(robot.getName(), y, x, currentPlayerChosing.getPlayerTeam(), gameManager.getMonde());
                            // Actualizing bank account value
                            currentPlayerChosing.setPlayerBankAccount(currentPlayerChosing.getPlayerBankAccount()-robot.get_price());
                        }
                    }
                    dtde.dropComplete(true);
                    Player nextPlayer = (currentPlayerChosing == gameManager.get_playerList().get(0)) ? gameManager.get_playerList().get(1) : gameManager.get_playerList().get(0);
                    currentPlayerChosing = nextPlayer;
                    Utilities.changeJLabel(robotSelectionPanel, 0, Utilities.currentPlayerStrg + currentPlayerChosing.getPlayerName());
                    Utilities.changeJLabel(robotSelectionPanel, 1, Utilities.bankAccountStrg + currentPlayerChosing.getPlayerBankAccount());
                    // Control if we can switch to playingScreen
                    if((gameManager.get_playerList().get(0).getReady() || gameManager.get_playerList().get(0).getPlayerBankAccount() <= 0)
                    && (gameManager.get_playerList().get(1).getReady() || gameManager.get_playerList().get(1).getPlayerBankAccount() <= 0)){
                        gameManager.switchFromChoiceToPlaying(gameManager.get_ScreenRobotChoosing(), gameManager.get_ScreenPlaying());
                }

                } catch (Exception e){
                    e.printStackTrace();
                    dtde.dropComplete(false);
                }
            } else {
                dtde.rejectDrop();
            }
        }        
    }

    // Main (try)
    public static void main(String[] args){}
}