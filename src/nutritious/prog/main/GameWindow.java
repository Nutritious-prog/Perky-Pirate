package nutritious.prog.main;

import javax.swing.*;

public class GameWindow {
    private JFrame jFrame;
    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();
        jFrame.setSize(400, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminate app on close button
        jFrame.add(gamePanel);
        jFrame.setLocationRelativeTo(null); //spawn window in the center of our screen

        jFrame.setVisible(true);
    }
}
