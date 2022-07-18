package nutritious.prog.main;

import nutritious.prog.inputs.KeyboardInputs;
import nutritious.prog.inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;
    private BufferedImage img, subImage;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        
        importImg();
        
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280,800);
        setPreferredSize(size);
    }

    //gets called when the game is started
    //graphics object is used to actually paint the panel
    public void paintComponent(Graphics graphics){
        //initialising JPanel paintComponent() method to do its own work,
        //and then we can begin with our method
        super.paintComponent(graphics);

        // in the x and y arguments we specify position of sprite we want to extract on the map of sprites (column and row)
        subImage = img.getSubimage(1 * 64, 8 * 40, 64, 40);
        graphics.drawImage(subImage, this.xDelta, this.yDelta, 128, 80,  null);
    }

    public void changeX(int value){
        this.xDelta += value;
    }
    public void changeY(int value){
        this.yDelta += value;
    }
}
