package nutritious.prog.UI;

import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton{
    private BufferedImage[] images;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX; // used for positioning button on slider
    private int minimalX, maximalX; // boundaries where the button can go on slider
    private float sliderPosOnTheBar = 0f; //value representing how much % of volume is currently set on a slider


    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x +=  -VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;

        minimalX = x + VOLUME_WIDTH / 2;
        maximalX = x + width - VOLUME_WIDTH / 2;

        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++)
            images[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);    }

    public void update() {
        index = 0;
        if(mouseOver) {
            index = 1;
        } if(mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y , width, height, null);
        g.drawImage(images[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    //method for moving button on the slider
    public void changeX(int x) {
        if(x < minimalX) {
            buttonX = minimalX;
        } else if(x > maximalX) {
            buttonX = maximalX;
        } else {
            buttonX = x;
        }

        updateSliderPosOnTheBar();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateSliderPosOnTheBar() {
        float range = maximalX - minimalX;
        float value = buttonX - minimalX;
        sliderPosOnTheBar = value/range;
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

    public float getSliderPosOnTheBar() {
        return sliderPosOnTheBar;
    }
}
