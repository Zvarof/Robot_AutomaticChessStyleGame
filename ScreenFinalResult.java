import java.awt.*;
import javax.swing.*;

public class ScreenFinalResult {
    // Attributs
    private GameManager gameManager;
    private JPanel left;
    private JPanel right;
    private JPanel top;
    private JPanel bot;
    private String proverbe1 = "La force ne résout rien, si on ne frappe pas assez fort";


    // Constructors
    public ScreenFinalResult(GameManager gameManager){
        this.gameManager = gameManager;
        /*
        victoryPanel = new JPanel();
        victoryPanel.setLayout(new BorderLayout());
        */
    }

    // Methods
    public void getWinnerAndScores(){
        Image temp = new ImageIcon("Images/WinningRobot.jpg").getImage();
        ImageIcon winningRobot = new ImageIcon(temp.getScaledInstance((int) (gameManager.get_mainJFrame().getWidth()*0.5), (int) (gameManager.get_mainJFrame().getHeight()*0.65), java.awt.Image.SCALE_SMOOTH));
        temp = new ImageIcon("Images/LosingRobot.jpg").getImage();
        ImageIcon losingRobot = new ImageIcon(temp.getScaledInstance((int) (gameManager.get_mainJFrame().getWidth()*0.5), (int) (gameManager.get_mainJFrame().getHeight()*0.65), java.awt.Image.SCALE_SMOOTH));
        Player playerL = gameManager.get_playerList().get(0);
        Player playerR = gameManager.get_playerList().get(1);


        // Left part
        left = new JPanel();
        left.setSize((int) (gameManager.get_mainJFrame().getWidth()*0.48), (int) (gameManager.get_mainJFrame().getHeight()*0.80));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(new JLabel("<html> <h3 align='center'> Team " + playerL.getPlayerTeam() + "</h3>", SwingConstants.CENTER));
        if (playerL.getScore() > playerR.getScore()){left.add(new JLabel("<html> <h3 align='center'>" + "The Winner is : " + playerL.getPlayerName() + "</h3>", SwingConstants.CENTER));} 
        else {left.add(new JLabel("<html> <h3 align='center'> The Loser is : " + playerL.getPlayerName()+ "</h3>", SwingConstants.CENTER));}
        left.add(new JLabel("<html> <h3 align='center'> Score : " + playerL.getScore() + " points </h3>", SwingConstants.CENTER));
        if (playerL.getScore() > playerR.getScore()){left.add(new JLabel(winningRobot));}
        else {left.add(new JLabel(losingRobot));}


        // Right part
        right = new JPanel();
        right.setSize((int) (gameManager.get_mainJFrame().getWidth()*0.48), (int) (gameManager.get_mainJFrame().getHeight()*0.80));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(new JLabel("<html> <h3 align='center'> Team " + playerR.getPlayerTeam() + "</h3>", SwingConstants.CENTER));
        if (playerR.getScore() > playerL.getScore()){right.add(new JLabel("<html> <h3 align='center'> The Winner is : " + playerR.getPlayerName() + "</h3>", SwingConstants.CENTER));} 
        else {right.add(new JLabel("<html> <h3 align='center'> The Loser is : " + playerR.getPlayerName() + "</h3>", SwingConstants.CENTER));}
        right.add(new JLabel("<html> <h3 align='center'> Score : " + playerR.getScore() + " points </h3>", SwingConstants.CENTER));
        if (playerR.getScore() > playerL.getScore()){right.add(new JLabel(winningRobot));}
        else {right.add(new JLabel(losingRobot));}

        // Bot part
        bot = new JPanel();
        bot.setSize(gameManager.get_mainJFrame().getWidth(), (int) (gameManager.get_mainJFrame().getHeight()*0.1));
        bot.add(new JLabel("<html> <h2 align='center'>" + proverbe1 + "</h2>"));

        // Top part 
        top = new JPanel();
        top.setSize(gameManager.get_mainJFrame().getWidth(), (int) (gameManager.get_mainJFrame().getHeight()*0.1));
        top.add(new JLabel("<html> <h2 align='center'> And the moment of truth... </h2>"));

        /* Ici pour l'instant mais probablement à changer */
        gameManager.get_mainJFrame().getContentPane().removeAll();
        gameManager.get_mainJFrame().getContentPane().add(left, BorderLayout.WEST);
        gameManager.get_mainJFrame().getContentPane().add(right, BorderLayout.EAST);
        gameManager.get_mainJFrame().getContentPane().add(bot, BorderLayout.SOUTH);
        gameManager.get_mainJFrame().getContentPane().add(top, BorderLayout.NORTH);
    }

    public static void main(String[] args) {}
}
