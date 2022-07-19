package nutritious.prog.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.Directions.DOWN;
import static nutritious.prog.utils.Constants.PlayerConstants.*;

public class Player extends Character{
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean isMoving = false;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }
    public void render(Graphics graphics) {
        //we retrieve animation frames by giving the array parameters of
        //current player action (in each row of our sprites map we contain different animations)
        //current animation index (in each column of our sprites map we contain different frame of certain animation)
        graphics.drawImage(animations[playerAction][animationIndex], (int)this.x, (int)this.y, 256, 160,  null);
    }

    private void loadAnimations() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            BufferedImage img = ImageIO.read(is);
            animations = new BufferedImage[9][6];

            //loading sub images with all single animations frames to array
            for(int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
                }
            }
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

    public void setDirection(int direction){
        this.playerDirection = direction;
        this.isMoving = true;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
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

    private void updatePosition() {
        //using KeyboardInputs we set playerDirection and with this information
        //which key was pressed we move our character
        if(isMoving) {
            switch (playerDirection) {
                case LEFT:
                    x += -5;
                    break;
                case UP:
                    y += -5;
                    break;
                case RIGHT:
                    x += 5;
                    break;
                case DOWN:
                    y += 5;
                    break;
            }
        }
    }
}
