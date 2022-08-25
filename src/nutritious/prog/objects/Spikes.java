package nutritious.prog.objects;

import nutritious.prog.main.Game;

public class Spikes extends GameObject{

    public Spikes(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32,16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffset;                    //this line is needed to place spikes directly on the "floor"
    }
}
