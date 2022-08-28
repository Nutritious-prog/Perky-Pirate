package nutritious.prog.entities;

import nutritious.prog.audio.AudioPlayer;
import nutritious.prog.gameStates.Playing;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.EntityConstants.ANI_TIME;
import static nutritious.prog.utils.Constants.EntityConstants.GRAVITY;
import static nutritious.prog.utils.Constants.PlayerConstants.*;
import static nutritious.prog.utils.HelperMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean left, right, jump;
    private boolean isMoving = false, isAttacking = false;
    private int[][] levelData;
    //offsets from top left corner of whole image to top left corner of our character (big hitBox to small hitBox)
    private final float xDrawOffset = 21 * Game.SCALE;
    private final float yDrawOffset = 4 * Game.SCALE;

    //Jumping and gravity variables
    private final float jumpSpeed = -2.25f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    //status bar UI
    private BufferedImage statusBarImg;

    private final int statusBarWidth = (int) (192 * Game.SCALE);
    private final int statusBarHeight = (int) (58 * Game.SCALE);
    private final int statusBarX = (int) (10 * Game.SCALE);
    private final int statusBarY = (int) (10 * Game.SCALE);

    private final int healthBarWidth = (int) (150 * Game.SCALE);
    private final int healthBarHeight = (int) (4 * Game.SCALE);
    private final int healthBarXStart = (int) (34 * Game.SCALE);
    private final int healthBarYStart = (int) (14 * Game.SCALE);

    private int healthWidth = healthBarWidth;

    //for a mirror reflection of entities
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private final Playing playing;

    private int tileY = 0;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = this.maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
        loadAnimations();
        initHitbox(20, 27); //size of our characters body
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            //we check here if player just got killed, and we proceed to play death animation
            if (state != DEAD) {
                state = DEAD;
                animationTick = 0;
                animationIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (animationIndex == GetSpriteAmount(DEAD) - 1 && animationTick >= ANI_TIME - 1) {     //here we check if animation was fully played
                playing.setGameOver(true);                                                                  //(the last animation frame and last animation tick) and if we can end the game
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAME_OVER);
            } else {
                updateAnimationTick();                                                                      //or else we just play the dying animation
                disableWalking();
                updatePosition();
            }

            return;
        }

        updateAttackBox();

        updatePosition();
        if (isMoving) {
            checkIfPotionIsTouched();
            checkIfSpikesGotTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
        if (isAttacking)
            checkAttack();

        updateAnimationTick();
        setAnimation();
    }

    private void disableWalking() {
        left = right;
        right = left;
    }

    private void checkIfSpikesGotTouched() {
        playing.checkIfSpikesGotTouched(this);
    }

    private void checkIfPotionIsTouched() {
        playing.checkIfPotionIsTouched(hitbox);
    }

    private void checkAttack() {
        //aniIndex 1 is where we check if enemy was in players attack hit box
        if(attackChecked || animationIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkIfEnemyGotHit(attackBox);
        playing.checkIfObjectGotHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    public void render(Graphics graphics, int xLvlOffset) {
        //we retrieve animation frames by giving the array parameters of
        //current player action (in each row of our sprites map we contain different animations)
        //current animation index (in each column of our sprites map we contain different frame of certain animation)
        graphics.drawImage(animations[state][animationIndex],
                        (int)(hitbox.x - xDrawOffset) - xLvlOffset + flipX,
                           (int)(hitbox.y - yDrawOffset),
                            width * flipW, height,  null);
//      drawHitbox(graphics);

        //drawAttackBox(graphics, xLvlOffset);
        drawUI(graphics);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    private void updateAttackBox() {
        if (right)
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10); //max to the right of our character and offset
        else if (left)
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10); //max to the left of our character and offset

        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        //it calculates what percent of full hp is current hp and applies it to health bar red filling
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    private void drawUI(Graphics graphics) {
        //whole status bar image
        graphics.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        //
        graphics.setColor(Color.RED);
        graphics.fillRect(healthBarXStart + statusBarX + 2, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            //loading sub images with all single animations frames to array
            animations = new BufferedImage[7][8];
            for(int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
                }
            }

            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        //falling in the beginning of the game to start on the floor
        if(!IsEntityOnTheFloor(hitbox, levelData)) {
            inAir = true;
        }
    }

    private void setAnimation() {
        int startAnimation = state;

        if(isMoving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if(inAir) {
            if(airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if(isAttacking) {
            state = ATTACK;
            //we weren't attacking when we entered this method
            if(startAnimation != ATTACK) {
                //we set animationIndex to 1 to get faster attack (if we start from 0 animation frame character swigs his sword and only then attacks)
                animationIndex = 1;
                animationTick = 0;
                return;
            }
        }

        //if we changed animation we must reset the animationTick to start new animation from the  beginning
        if(startAnimation != state) {
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
        if(animationTick >= ANI_TIME) {
            animationTick = 0;
            animationIndex++;
            //if we reached the end of an array with animation frames we go back to its beginning,
            //and we replay the animation again
            //we set length of the row in array depending on players current action
            if(animationIndex >= GetSpriteAmount(state)) {
                animationIndex = 0;
                isAttacking = false; // we want only one animation of attack per click (otherwise one click will cause infinite animation)
                attackChecked = false;
            }
        }
    }

    private void updatePosition() {
        isMoving = false;

        if(jump) {
            jump();
        }
        //check if we are moving
        if(!inAir && ((!right && !left) || (left && right)))
            return;

        float xSpeed = 0;

        //checking if player is holding only one of two mutually exclusive buttons
        if (left && !right) {
            xSpeed = -walkSpeed;
            flipX = width;                  //flip variables are used to invert the entity picture, so it faces the direction it walks
            flipW = -1;
        } else if (right && !left) {
            xSpeed = walkSpeed;
            flipX = 0;
            flipW = 1;
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
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {                  // if not, we met an obstacle
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

        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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

    public void changeHealth(int value) {
        currentHealth += value;

        if(currentHealth <= 0) {
            currentHealth = 0;
            //TODO GameOver()
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }
    //this method will be used to just kill the player regardless of how many hp he has
    public void kill() {
        currentHealth = 0;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        isAttacking = false;
        isMoving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnTheFloor(hitbox, levelData))
            inAir = true;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
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

    public int getTileY() {
        return tileY;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        flipX = 0;
        flipW = 1;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void changePower(int bluePotionValue) {
        //TODO power and power attack mechanics
    }


}
