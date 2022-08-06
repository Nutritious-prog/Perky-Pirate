package nutritious.prog.entities;

import nutritious.prog.main.Game;

import java.util.concurrent.TimeUnit;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.EnemyConstants.GetSpriteAmount;
import static nutritious.prog.utils.Constants.EnemyConstants.*;
import static nutritious.prog.utils.HelperMethods.*;

public abstract class Enemy extends Entity{
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;                 //beginning of a game

    //falling
    protected boolean inAir = false;
    protected float fallSpeed, gravity = 0.04f * Game.SCALE;

    //patrolling
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;

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
    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
        if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
            aniIndex = 0;
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
