package nutritious.prog.gameStates;

import nutritious.prog.UI.AudioOptions;
import nutritious.prog.UI.PauseButton;
import nutritious.prog.UI.UrmButton;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static nutritious.prog.utils.Constants.UI.URMButtons.URM_BUTTON_SIZE;

public class GameOptions extends State implements StateMethods{
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBoardImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuButton;

    public GameOptions(Game game) {
        super(game);
        loadImages();
        loadButton();
        audioOptions= game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, menuY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    private void loadImages() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBoardImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU    );

        bgW = (int) (optionsBoardImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBoardImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        graphics.drawImage(optionsBoardImg, bgX, bgY, bgW, bgH, null);

        menuButton.draw(graphics);
        audioOptions.draw(graphics);
    }

    private boolean isMouseOnButton(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isMouseOnButton(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isMouseOnButton(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetBools();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);

        if (isMouseOnButton(e, menuButton)) {
            menuButton.setMouseOver(true);
        }
        else {
            audioOptions.mouseMoved(e);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
