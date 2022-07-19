package nutritious.prog.entities;

public abstract class Character {
    protected float x, y;
    protected int width, height;

    public Character(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
