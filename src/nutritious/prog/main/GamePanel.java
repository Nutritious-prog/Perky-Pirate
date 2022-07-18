package nutritious.prog.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel() {
    }

    //gets called when the game is started
    //graphics object is used to actually paint the panel
    public void paintComponent(Graphics graphics){
        //initialising JPanel paintComponent() method to do its own work,
        //and then we can begin with our method
        super.paintComponent(graphics);

        graphics.fillRect(100, 100, 200, 50);
    }
}
