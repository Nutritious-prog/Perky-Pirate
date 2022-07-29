package nutritious.prog.UI;

import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.URMButtons.URM_BUTTON_DEFAULT_SIZE;
import static nutritious.prog.utils.Constants.UI.URMButtons.URM_BUTTON_SIZE;

public class UrmButton extends PauseButton {
    private BufferedImage[] images;

    private int rowIndex, index; //row index is for different button types, index is for one button animations
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++)
            images[i] = temp.getSubimage(i * URM_BUTTON_DEFAULT_SIZE, rowIndex * URM_BUTTON_DEFAULT_SIZE, URM_BUTTON_DEFAULT_SIZE, URM_BUTTON_DEFAULT_SIZE);

    }

    public void update() {
        //changing button animation depending on mouse event
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void draw(Graphics g) {
        g.drawImage(images[index], x, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, null);
    }

    //for releasing mouse (reset bools to get default index (default button))
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
