package nutritious.prog.UI;

import nutritious.prog.gameStates.GameState;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {
    private BufferedImage backgroundImage;
    private int backgroundX, backgroundY, backgroundHeight, backgroundWidth;

    public PauseOverlay() {
        loadBackground();
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = (int)(backgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int)(backgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2; // center of screen
        backgroundY = (int)(25 * Game.SCALE);
    }

    public void update() {

    }

    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
    }

    public void mouseDragged() {

    }

    public void mouseMoved() {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

}
