package nutritious.prog.entities;

import nutritious.prog.main.Game;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.EnemyConstants.*;
import static nutritious.prog.utils.HelperMethods.*;

public abstract class Enemy extends Entity{
    //animations
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;

    //beginning of a game
    protected boolean firstUpdate = true;

    //falling
    protected boolean inAir = false;
    protected float fallSpeed, gravity = 0.04f * Game.SCALE;

    //patrolling
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;

    //interaction with player
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected float visualDistance = 5 * attackDistance;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if(!IsEntityOnTheFloor(hitbox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        //falling
        if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        }
        //we are on the ground
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveTheFloor(hitbox, fallSpeed);
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
        this.enemyState = enemyState;
        //resetting animations
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
        if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
            aniIndex = 0;
            //as soon as we finish one animation on attack we go back to idle
            //and we check again if player is in range for attack
            if(enemyState == ATTACK) {
                enemyState = IDLE;
            }
        }
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    public int getAniIndex() {
        return aniIndex;
    }
    public int getEnemyState() {
        return enemyState;
    }
}
