package nutritious.prog.entities;

import nutritious.prog.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static nutritious.prog.utils.Constants.Directions.*;
import static nutritious.prog.utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    // AttackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        //30 (offset) + 22 (crabby) + 30 (offset) = 82 (whole width of attack hit box)
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehaviour(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    /**method for changing behaviour of an enemy
     from idle to patrolling or chasing and attacking player*/
    private void updateBehaviour(int[][] lvlData, Player player){
        //we check if we are in the beginning of a game
        if(firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if(inAir) {
            updateInAir(lvlData);
        }
        //if we have fallen on the ground now our enemy will perform
        //certain actions depending on his current state (patrol, attack, chase the player)
        else {
            switch (enemyState) {
                case IDLE:
                    changeState(RUNNING);
                    break;
                case RUNNING:
                    walkSpeed = 0.35f;
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if(isPlayerCloseForAttack(player)) {
                            changeState(ATTACK);
                        }
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    //at the beginning of each attack animation we reset attackChecked
                    if(aniIndex == 0) {
                        attackChecked = false;
                    }
                    //we only check if crabby hit when his claws are fully extended (3rd frame of animation)
                    if(aniIndex == 3 && !attackChecked) {
                        checkIfPlayerGotHit(attackBox, player);
                    }
                    break;
                case GETTING_HIT:
                    break;
            }
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    //this method is needed to add the width to print the image when we turn it to the other side
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    //this method will reverse the width so the picture is printed to the other side (left <=> right)
    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;

    }

}
