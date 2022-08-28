package nutritious.prog.UI;

import nutritious.prog.gameStates.GameState;
import nutritious.prog.gameStates.Playing;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.PauseButtons.SOUND_BUTTON_SIZE;
import static nutritious.prog.utils.Constants.UI.URMButtons.URM_BUTTON_SIZE;
import static nutritious.prog.utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImage;
    private int backgroundX, backgroundY, backgroundHeight, backgroundWidth;
    private UrmButton menuButton, unpauseButton, replayButton;

    private AudioOptions audioOptions;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, bY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
        replayButton = new UrmButton(replayX, bY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        unpauseButton = new UrmButton(unpauseX, bY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = (int)(backgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int)(backgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2; // center of screen
        backgroundY = (int)(25 * Game.SCALE);
    }

    public void update() {
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        audioOptions.update();
    }

    public void draw(Graphics graphics) {
        //Background
        graphics.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        //URM buttons
        menuButton.draw(graphics);
        replayButton.draw(graphics);
        unpauseButton.draw(graphics);
        //Audio options overlay
        audioOptions.draw(graphics);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if(isMouseOnButton(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isMouseOnButton(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isMouseOnButton(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isMouseOnButton(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                playing.setGameState(GameState.MENU);
                playing.unpauseGame();
            }
        } else if (isMouseOnButton(e, replayButton)) {
            if (replayButton.isMousePressed())
                playing.resetAll();
        } else if (isMouseOnButton(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        } else {
            audioOptions.mouseReleased(e);
        }

        replayButton.resetBools();
        unpauseButton.resetBools();
        menuButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isMouseOnButton(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isMouseOnButton(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        } else if (isMouseOnButton(e, replayButton)) {
            replayButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    private boolean isMouseOnButton(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
