package nutritious.prog.entities;

import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.PlayerConstants.*;
import static nutritious.prog.utils.HelperMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationTime = 25;
    private int playerAction = IDLE;
    private boolean up, down, left, right, jump;
    private boolean isMoving = false, isAttacking = false;
    private float playerSpeed = 2.0f;
    private int[][] levelData;
    //offsets from top left corner of whole image to top left corner of our character (big hitBox to small hitBox)
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    //Jumping and gravity variables
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 20 * Game.SCALE, 27 * Game.SCALE); //size of our characters body
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }
    public void render(@NotNull Graphics graphics) {
        //we retrieve animation frames by giving the array parameters of
        //current player action (in each row of our sprites map we contain different animations)
        //current animation index (in each column of our sprites map we contain different frame of certain animation)
        graphics.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xDrawOffset), (int)(hitbox.y - yDrawOffset), width, height,  null);
//        drawHitbox(graphics);
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
        //falling in the beginning of the game to start on the floor
        if(!IsEntityOnTheFloor(hitbox, levelData)) {
            inAir = true;
        }
    }

    private void setAnimation() {
        int startAnimation = playerAction;

        if(isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if(inAir) {
            if(airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
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

        if(jump) {
            jump();
        }
        //check if we are moving
        if(!left && !right && !inAir)
            return;

        float xSpeed = 0;

        //checking if player is holding only one of two mutually exclusive buttons
        if (left && !right) {
            xSpeed = -playerSpeed;
        } else if (right && !left) {
            xSpeed = playerSpeed;
        }

        //this block of code gets rid of the issue occurring
        //when the entity walks past the edge of some type of block
        //and stays in air because we didn't jump (we invoke gravity only for jumps)
        if(!inAir) {
            if(!IsEntityOnTheFloor(hitbox, levelData)){
                //if we check that the entity is not on the floor
                // we go to the next block with gravitation implementation
                inAir = true;
            }
        }

        if(inAir) {
            //if we are in air (we jumped) we are checking if further jump trajectory is available
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {    // if yes we just add speed to our y-axis
                //we are adding decreasing value of airSpeed to jump (gravity is making it smaller and smaller)
                //until we hit airSpeed == 0, and then we start falling
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {                  // if not we met an obstacle
                hitbox.y = GetEntityYPosUnderRoofOrAboveTheFloor(hitbox, airSpeed);     //we hit floor or the roof
                if(airSpeed > 0) {          //we hit the floor
                    resetInAir();
                } else {                    //we hit the roof
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }
        isMoving = true;
    }

    private void jump() {
        if(inAir) return;       //if we are in air we won't jump again

        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    //Placing the player right next to the wall
    //Sometimes player can't move further to the wall because the next step is too big for the place that left
    //In those situations we want to place our player as far to the wall as possible
    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
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

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
