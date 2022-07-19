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
    private boolean up, down, left, right;
    private boolean isMoving = false, isAttacking = false;
    private float playerSpeed = 2.0f;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
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

    private void setAnimation() {
        int startAnimation = playerAction;

        if(isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if(isAttacking) {
            playerAction = ATTACK_1;
        }

        //if we changed animation we must reset the animationTick to start new animation from the  beginning
        if(startAnimation != playerAction) {
            resetAnimationTick();
        }
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
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
                isAttacking = false; // we want only one animation of attack per click
            }
        }
    }

    private void updatePosition() {
        isMoving = false;

        if (left && !right) {
            x += -playerSpeed;
            isMoving = true;
        } else if (right && !left) {
            x += playerSpeed;
            isMoving = true;
        }

        if (up && !down) {
            y += -playerSpeed;
            isMoving = true;
        } else if (down && !up) {
            y += playerSpeed;
            isMoving = true;
        }
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
