package nutritious.prog.entities;

import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.PlayerConstants.*;
import static nutritious.prog.utils.HelperMethods.CanMoveHere;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationTime = 15;
    private int playerAction = IDLE;
    private boolean up, down, left, right;
    private boolean isMoving = false, isAttacking = false;
    private float playerSpeed = 2.0f;
    private int[][] levelData;
    //offsets from top left corner of whole image to top left corner of our character (big hitBox to small hitBox)
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE); //size of our characters body
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
        graphics.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xDrawOffset), (int)(hitbox.y - yDrawOffset), 128, 80,  null);
        drawHitbox(graphics);
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            //loading sub images with all single animations frames to array
            animations = new BufferedImage[9][6];
            for(int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
                }
            }
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
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
        if(animationTick >= animationTime) {
            animationTick = 0;
            animationIndex++;
            //if we reached the end of an array with animation frames we go back to its beginning,
            //and we replay the animation again
            //we set length of the row in array depending on players current action
            if(animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                isAttacking = false; // we want only one animation of attack per click (otherwise one click will cause infinite animation)
            }
        }
    }

    private void updatePosition() {
        isMoving = false;
        //check if we are moving
        if(!left && !right && !up && !down)
            return;

        float xSpeed = 0, ySpeed = 0;

        //checking if player is holding only one of two mutually exclusive buttons
        if (left && !right) {
            xSpeed = -playerSpeed;
        } else if (right && !left) {
            xSpeed = playerSpeed;
        }

        if (up && !down) {
            ySpeed = -playerSpeed;
        } else if (down && !up) {
            ySpeed = playerSpeed;
        }

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
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
