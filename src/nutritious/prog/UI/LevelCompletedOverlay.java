package nutritious.prog.UI;

import nutritious.prog.gameStates.GameState;
import nutritious.prog.gameStates.Playing;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.URMButtons.URM_BUTTON_SIZE;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menuButton, nextLvlButton;
    private BufferedImage background;
    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        nextLvlButton = new UrmButton(nextX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        menuButton = new UrmButton(menuX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    private void initImg() {
        background = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED_BACKGROUND);
        bgW = (int) (background.getWidth() * Game.SCALE);
        bgH = (int) (background.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g) {
        // Added after youtube upload
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(background, bgX, bgY, bgW, bgH, null);
        nextLvlButton.draw(g);
        menuButton.draw(g);
    }

    public void update() {
        nextLvlButton.update();
        menuButton.update();
    }

    private boolean isMouseOnButton(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        nextLvlButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isMouseOnButton(menuButton, e))
            menuButton.setMouseOver(true);
        else if (isMouseOnButton(nextLvlButton, e))
            nextLvlButton.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isMouseOnButton(menuButton, e)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isMouseOnButton(nextLvlButton, e))
            if (nextLvlButton.isMousePressed())
                //playing.loadNextLevel();

        menuButton.resetBools();
        nextLvlButton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isMouseOnButton(menuButton, e))
            menuButton.setMousePressed(true);
        else if (isMouseOnButton(nextLvlButton, e))
            nextLvlButton.setMousePressed(true);
    }
}
