package nutritious.prog.entities;

import nutritious.prog.main.Game;

import static nutritious.prog.utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }
}
