package nutritious.prog.entities;

import nutritious.prog.main.Game;

import java.util.concurrent.TimeUnit;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.EnemyConstants.GetSpriteAmount;
import static nutritious.prog.utils.Constants.EnemyConstants.*;
import static nutritious.prog.utils.HelperMethods.*;

public abstract class Enemy extends Entity{
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;
    private boolean firstUpdate = true;                 //beginning of a game

    //falling
    private boolean inAir = false;
    private float fallSpeed, gravity = 0.04f * Game.SCALE;

    //patrolling
    private float walkSpeed = 0.35f * Game.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
        if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
            aniIndex = 0;
        }
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    //method for changing behaviour of an enemy
    //from idle to patrolling or chasing and attacking player
    private void updateMove(int[][] lvlData){
        //we check if we are in the beginning of a game
        if(firstUpdate) {
            if(!IsEntityOnTheFloor(hitbox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }
        if(inAir) {
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
        //if we have fallen on the ground now our enemy will perform
        //certain actions depending on his current state (patrol, attack, chase the player)
        else {
            switch (enemyState) {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
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

                    break;
            }
        }
    }

    private void changeWalkDir() {
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
