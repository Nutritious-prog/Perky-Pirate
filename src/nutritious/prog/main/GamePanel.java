package nutritious.prog.main;

import nutritious.prog.inputs.KeyboardInputs;
import nutritious.prog.inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private int x = 0;
    private int y = 0;

    public GamePanel() {
        mouseInputs = new MouseInputs();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    //gets called when the game is started
    //graphics object is used to actually paint the panel
    public void paintComponent(Graphics graphics){
        //initialising JPanel paintComponent() method to do its own work,
        //and then we can begin with our method
        super.paintComponent(graphics);

        graphics.fillRect(100 + x, 100 + y, 200, 50);
    }

    public void changeX(int value){
        this.x += value;
        repaint();
    }
    public void changeY(int value){
        this.y += value;
        repaint();
    }
}
