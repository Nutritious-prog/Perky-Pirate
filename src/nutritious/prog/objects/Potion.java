package nutritious.prog.objects;

import nutritious.prog.main.Game;

public class Potion extends GameObject{
    private float idleAnimtionOffset;
    private int maxidleAnimtionOffset, idleAnimtionDir = 1;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        playAnimation = true;                           //potions animation is infinite
        initHitbox(7,14);
        xDrawOffset = (int)(3 * Game.SCALE);            //sprites offset (between the beginning of  sprite to actual image of potion)
        yDrawOffset = (int)(2 * Game.SCALE);

        maxidleAnimtionOffset = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateidleAnimtion();
    }

    private void updateidleAnimtion() {
        idleAnimtionOffset += (0.075f * Game.SCALE * idleAnimtionDir);

        if (idleAnimtionOffset >= maxidleAnimtionOffset)
            idleAnimtionDir = -1;
        else if (idleAnimtionOffset < 0)
            idleAnimtionDir = 1;

        hitbox.y = y + idleAnimtionOffset;
    }
}
