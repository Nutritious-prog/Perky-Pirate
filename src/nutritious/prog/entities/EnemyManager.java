package nutritious.prog.entities;

import nutritious.prog.gameStates.Playing;
import nutritious.prog.levels.Level;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static nutritious.prog.utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;                            //images of enemy
    private ArrayList<Crabby> crabbies = new ArrayList<>();         //enemy objects

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyEnemyAlive = false;
        for (Crabby c : crabbies)
            if(c.isAlive()) {
                c.update(lvlData, player);
                //if we find an enemy we change value of alive enemies
                isAnyEnemyAlive = true;
            }
        if(!isAnyEnemyAlive) {
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies)
            if(c.isAlive()){
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                        (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
                //c.drawAttackBox(g, xLvlOffset);
            }
    }

    //method for enemies getting hit
    public void checkIfEnemyGotHit(Rectangle2D.Float attackBox) {
        for(Crabby c : crabbies)
            if(c.isAlive()){
                if(attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
            }
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++)
            for (int i = 0; i < crabbyArr[j].length; i++)
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for (Crabby c : crabbies)
            c.resetEnemy();
    }
}
