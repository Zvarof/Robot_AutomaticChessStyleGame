import java.awt.*;
import javax.swing.*;

public class ScreenIntermediateResult {
    // Attributs
    private GameManager gameManager;
    private JPanel top;
    private JPanel left;
    private JPanel right;
    private ImageIcon winningRobot;
    private ImageIcon losingRobot;

    // Getters
    public JPanel getJPanelTop(){return top;}
    public JPanel getJPanelLeft(){return left;}
    public JPanel getJPanelRight(){return right;}

    public ScreenIntermediateResult(GameManager gameManager){
        this.gameManager = gameManager;
        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setSize((int) (gameManager.get_mainJFrame().getWidth()), (int) (gameManager.get_mainJFrame().getHeight()*0.15));
        left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setSize((int) (gameManager.get_mainJFrame().getWidth()*0.50), (int) (gameManager.get_mainJFrame().getHeight()*0.85));
        right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setSize((int) (gameManager.get_mainJFrame().getWidth()*0.50), (int) (gameManager.get_mainJFrame().getHeight()*0.85));

        Image temp = new ImageIcon("Images/WinningRobot.jpg").getImage();
        this.winningRobot = new ImageIcon(temp.getScaledInstance((int) (gameManager.get_mainJFrame().getWidth()*0.50), (int) (gameManager.get_mainJFrame().getHeight()*0.80), java.awt.Image.SCALE_SMOOTH));
        temp = new ImageIcon("Images/LosingRobot.jpg").getImage();
        this.losingRobot = new ImageIcon(temp.getScaledInstance((int) (gameManager.get_mainJFrame().getWidth()*0.50), (int) (gameManager.get_mainJFrame().getHeight()*0.80), java.awt.Image.SCALE_SMOOTH));
    }


    public void actualize(){
        top.removeAll();
        top.add(new JLabel("<html> <h2 align='center'> Summary after round " + gameManager.get_currentRound() + "/" + gameManager.get_nbRounds() + "</h2>", SwingConstants.CENTER));

        left.removeAll();
        left.add(new JLabel("<html> <h3 align='center'>" + gameManager.get_playerList().get(0).getPlayerName() + "</h3>", SwingConstants.CENTER));
        left.add(new JLabel("<html> <h3 align='center'>" + "Current score : " + gameManager.get_playerList().get(0).getScore() + "</h3>", SwingConstants.CENTER));
        if (gameManager.get_playerList().get(0).getScore() > gameManager.get_playerList().get(1).getScore()){
            left.add(new JLabel(winningRobot));
        } else {
            left.add(new JLabel(losingRobot));
        }

        right.removeAll();
        right.add(new JLabel("<html> <h3 align='center'>" + gameManager.get_playerList().get(1).getPlayerName() + "</h3>", SwingConstants.CENTER));
        right.add(new JLabel("<html> <h3 align='center'>" + "Current score : " + gameManager.get_playerList().get(1).getScore() + "</h3>", SwingConstants.CENTER));
        if (gameManager.get_playerList().get(1).getScore() > gameManager.get_playerList().get(0).getScore()){
            right.add(new JLabel(winningRobot));
        } else {
            right.add(new JLabel(losingRobot));
        }      
    }

    public static void main(String[] args) {}
}