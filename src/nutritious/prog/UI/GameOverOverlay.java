package nutritious.prog.UI;

import nutritious.prog.gameStates.GameState;
import nutritious.prog.gameStates.Playing;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.URMButtons.*;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage boardImage;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menuButton, playAgainButton;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImage();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        playAgainButton = new UrmButton(playX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        menuButton = new UrmButton(menuX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);

    }

    private void createImage() {
        boardImage = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (boardImage.getWidth() * Game.SCALE);
        imgH = (int) (boardImage.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * Game.SCALE);

    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(boardImage, imgX, imgY, imgW, imgH, null);

        menuButton.draw(g);
        playAgainButton.draw(g);
    }

    public void update() {
        menuButton.update();
        playAgainButton.update();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }

    private boolean isMouseOnButton(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        playAgainButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isMouseOnButton(menuButton, e))
            menuButton.setMouseOver(true);
        else if (isMouseOnButton(playAgainButton, e))
            playAgainButton.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isMouseOnButton(menuButton, e)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isMouseOnButton(playAgainButton, e))
            if (playAgainButton.isMousePressed()) {
                playing.resetAll();
            }

        menuButton.resetBools();
        playAgainButton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isMouseOnButton(menuButton, e))
            menuButton.setMousePressed(true);
        else if (isMouseOnButton(playAgainButton, e))
            playAgainButton.setMousePressed(true);
    }
}
