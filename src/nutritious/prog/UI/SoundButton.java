package nutritious.prog.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import nutritious.prog.utils.LoadSave;

import static nutritious.prog.utils.Constants.UI.PauseButtons.SOUND_BUTTON_SIZE_DEFAULT;

public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImages;
    private boolean isMouseOver, isMousePressed;
    private boolean isMuted;
    private int rowIndex, colIndex; //in rows we have different buttons, in columns we have different button effects

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImages();
    }

    private void loadSoundImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages = new BufferedImage[2][3];
        for (int j = 0; j < soundImages.length; j++)
            for (int i = 0; i < soundImages[j].length; i++)
                soundImages[j][i] = temp.getSubimage(i * SOUND_BUTTON_SIZE_DEFAULT, j * SOUND_BUTTON_SIZE_DEFAULT, SOUND_BUTTON_SIZE_DEFAULT, SOUND_BUTTON_SIZE_DEFAULT);
    }

    public void update() {
        if (isMuted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;
        if (isMouseOver)
            colIndex = 1;
        if (isMousePressed)
            colIndex = 2;

    }

    public void resetBools() {
        isMouseOver = false;
        isMousePressed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImages[rowIndex][colIndex], x, y, width, height, null);
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.isMouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.isMousePressed = mousePressed;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
    }

}
