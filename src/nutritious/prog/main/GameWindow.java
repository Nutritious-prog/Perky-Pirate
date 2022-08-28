package nutritious.prog.main;

import nutritious.prog.utils.LoadSave;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame jFrame;
    public GameWindow(GamePanel gamePanel) {
        ImageIcon img = new ImageIcon("res\\pirate_icon.png");

        jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminate app on close button
        jFrame.add(gamePanel);
        jFrame.setResizable(false);
        jFrame.pack(); //fit size of the window to preferred size of its components
        jFrame.setLocationRelativeTo(null); //spawn window in the center of our screen
        jFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });

        jFrame.setVisible(true);
        jFrame.setIconImage(img.getImage());
    }
}
