package nutritious.prog.main;

import nutritious.prog.inputs.KeyboardInputs;
import nutritious.prog.inputs.MouseInputs;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.PlayerConstants.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private int xDelta = 0, yDelta = 0;
    private BufferedImage img, subImage;
    private BufferedImage[][] animations; //two-dimensional array containing all sprites from which we retrieve animations
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean isMoving = false;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        
        importImg();
        loadAnimations();
        
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];

        //loading sub images with all single animations frames to array
        for(int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
            }
        }
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        updateAnimationTick();

        setAnimation();
        updatePosition();

        //we retrieve animation frames by giving the array parameters of
        //current player action (in each row of our sprites map we contain different animations)
        //current animation index (in each column of our sprites map we contain different frame of certain animation)
        graphics.drawImage(animations[playerAction][animationIndex], this.xDelta, this.yDelta, 256, 160,  null);
    }

    private void updatePosition() {
        //using KeyboardInputs we set playerDirection and with this information
        //which key was pressed we move our character
        if(isMoving) {
            switch (playerDirection) {
                case LEFT:
                    xDelta += -5;
                    break;
                case UP:
                    yDelta += -5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }

    private void setAnimation() {
        if(isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    private void updateAnimationTick() {
        //Number of one frame paints
        animationTick++;
        //we check if our one frame of animation lasted enough time and if so
        //we reset the counter and increment animationIndex to move to next frame of animation
        if(animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            //if we reached the end of an array with animation frames we go back to its beginning,
            //and we replay the animation again
            //we set length of the row in array depending on players current action
            if(animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }

    public void updateGame() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }

    public void setDirection(int direction){
        this.playerDirection = direction;
        this.isMoving = true;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
