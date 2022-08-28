package nutritious.prog.UI;

import nutritious.prog.gameStates.GameState;
import nutritious.prog.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static nutritious.prog.utils.Constants.UI.PauseButtons.SOUND_BUTTON_SIZE;
import static nutritious.prog.utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static nutritious.prog.utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {
    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;
    private Game game;

    public AudioOptions(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE); // both buttons have the same x
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
    }

    public void update() {
        volumeButton.update();
        musicButton.update();
        sfxButton.update();
    }

    public void draw(Graphics graphics) {
        //Sound buttons
        musicButton.draw(graphics);
        sfxButton.draw(graphics);
        //Volume slider
        volumeButton.draw(graphics);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getSliderPosOnTheBar();
            volumeButton.changeX(e.getX());
            float valueAfter = volumeButton.getSliderPosOnTheBar();
            if(valueBefore != valueAfter) {
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if(isMouseOnButton(e, musicButton)) {
            musicButton.setMousePressed(true);
            System.out.println("Music button pressed");
        } else if (isMouseOnButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isMouseOnButton(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isMouseOnButton(e, musicButton)) {
            if(musicButton.isMousePressed()) {
                game.getAudioPlayer().toggleMusicMute();
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isMouseOnButton(e, sfxButton)) {
            if(sfxButton.isMousePressed()) {
                game.getAudioPlayer().toggleSfxMute();
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isMouseOnButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isMouseOnButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isMouseOnButton(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    private boolean isMouseOnButton(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
