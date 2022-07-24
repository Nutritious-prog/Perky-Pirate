package nutritious.prog.UI;

import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.PauseButtons.SOUND_BUTTON_SIZE;

public class PauseOverlay {
    private BufferedImage backgroundImage;
    private int backgroundX, backgroundY, backgroundHeight, backgroundWidth;
    private SoundButton musicButton, sfxButton;

    public PauseOverlay() {
        loadBackground();
        createSoundButtons();
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
    }

    public void draw(Graphics graphics) {
        //Background
        graphics.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        //Sound buttons
        musicButton.draw(graphics);
        sfxButton.draw(graphics);
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
        }
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        if(isMouseOnButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isMouseOnButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
    }

    private boolean isMouseOnButton(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
