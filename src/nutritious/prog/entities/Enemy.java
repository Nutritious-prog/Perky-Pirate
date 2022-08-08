package nutritious.prog.entities;

import nutritious.prog.main.Game;

import java.awt.geom.Rectangle2D;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.EnemyConstants.*;
import static nutritious.prog.utils.Constants.EntityConstants.ANI_TIME;
import static nutritious.prog.utils.Constants.EntityConstants.GRAVITY;
import static nutritious.prog.utils.HelperMethods.*;

public abstract class Enemy extends Entity{
    protected int enemyType;

    //beginning of a game
    protected boolean firstUpdate = true;

    //patrolling
    protected int walkDir = LEFT;

    //interaction with player
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected float visualDistance = 5 * attackDistance;

    protected boolean alive = true;

    //attacking
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.walkSpeed = 0.35f * Game.SCALE;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if(!IsEntityOnTheFloor(hitbox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        //falling
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        //we are on the ground
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveTheFloor(hitbox, airSpeed);
            //enemies won't change their y position, so we just check it once they are on the ground
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    /**
     * method checking if enemy can see player and if it has a clear path
     * to attack or chase him (there are no obstacles in between)
     * */
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        //does player and enemy align horizontally
        if(playerTileY == this.tileY) {
            if(isPlayerInVisualRange(player)) {
                if(IsSightClear(lvlData, this.hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isPlayerInVisualRange(Player player) {
        int distanceBetweenPlayerAndEnemy = Math.abs((int)(player.hitbox.x - this.hitbox.x));
        return distanceBetweenPlayerAndEnemy <= visualDistance;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int distanceBetweenPlayerAndEnemy = Math.abs((int)(player.hitbox.x - this.hitbox.x));
        return distanceBetweenPlayerAndEnemy <= attackDistance;
    }

    protected void turnTowardsPlayer(Player player) {
        //changing direction depending on player position
        if(player.hitbox.x > this.hitbox.x) {
            walkDir = RIGHT;
            //chase is faster than walk
            walkSpeed = 0.7f;
        } else {
            walkDir = LEFT;
            //chase is faster than walk
            walkSpeed = 0.7f;
        }
    }

    protected void changeState(int enemyState) {
        this.state = enemyState;
        //resetting animations
        animationTick = 0;
        animationIndex = 0;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if(animationTick >= ANI_TIME) {
            animationTick = 0;
            animationIndex++;
        }
        if(animationIndex >= GetSpriteAmount(enemyType, state)) {
            animationIndex = 0;
            //as soon as we finish one animation on attack we go back to idle,
            //and we check again if player is in range for attack
            switch (state) {
                case ATTACK:
                case GETTING_HIT:
                    state = IDLE;
                    break;
                case DEAD:
                    alive = false;
                    break;
            }
        }
    }

    //method operating getting hurt functionality
    public void hurt(int dmg) {
        currentHealth += -dmg;
        if(currentHealth <= 0) {
            changeState(DEAD);
        } else {
            changeState(GETTING_HIT);
        }
    }

    protected void checkIfPlayerGotHit(Rectangle2D.Float attackBox, Player player) {
        //if player is in enemy's hit box enemy deals damage
        if(attackBox.intersects(player.hitbox)) {
            player.changeHealth(-GetEnemyDmg(CRABBY));
        }
        attackChecked = true;
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    public boolean isAlive() {
        return alive;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        changeState(IDLE);
        alive = true;
        airSpeed = 0;
    }
}
