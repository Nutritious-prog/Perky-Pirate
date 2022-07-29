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
    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, unpauseButton, replayButton;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE); // both buttons have the same x
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = (int)(backgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int)(backgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2; // center of screen
        backgroundY = (int)(25 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();
    }

    public void draw(Graphics graphics) {
        //Background
        graphics.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        //Sound buttons
        musicButton.draw(graphics);
        sfxButton.draw(graphics);
        //URM buttons
        menuButton.draw(graphics);
        replayButton.draw(graphics);
        unpauseButton.draw(graphics);
        //Volume slider
        volumeButton.draw(graphics);
    }

    public void mouseDragged() {

    }

    public void mouseMoved() {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if(isMouseOnButton(e, musicButton)) {
            musicButton.setMousePressed(true);
            System.out.println("Music button pressed");
        } else if (isMouseOnButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if(isMouseOnButton(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isMouseOnButton(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isMouseOnButton(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isMouseOnButton(e, musicButton)) {
            if(musicButton.isMousePressed()) {
                // if we click the button it will toggle whatever value there was before
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isMouseOnButton(e, sfxButton)) {
            if(sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } else if (isMouseOnButton(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.unpauseGame();
            }
        } else if (isMouseOnButton(e, replayButton)) {
            if (replayButton.isMousePressed())
                System.out.println("replay lvl!");
        } else if (isMouseOnButton(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
        menuButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if(isMouseOnButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isMouseOnButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isMouseOnButton(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isMouseOnButton(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        } else if (isMouseOnButton(e, replayButton)) {
            replayButton.setMouseOver(true);
        }
    }

    private boolean isMouseOnButton(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
