package nutritious.prog.main;

import javax.swing.*;

public class GameWindow {
    private JFrame jFrame;
    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminate app on close button
        jFrame.add(gamePanel);
        jFrame.setResizable(false);
        jFrame.pack(); //fit size of the window to preferred size of its components
        jFrame.setLocationRelativeTo(null); //spawn window in the center of our screen


        jFrame.setVisible(true);
    }
}
