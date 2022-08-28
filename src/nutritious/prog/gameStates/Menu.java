package nutritious.prog.gameStates;

import nutritious.prog.UI.MenuButton;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods{
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage boardImage, backgroundImagePink;
    private int menuX, menuY, menuWidth, menuHeight;


    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground(); //background tile for buttons
        backgroundImagePink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG); // background color image
    }

    private void loadBackground() {
        boardImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(boardImage.getWidth() * Game.SCALE);
        menuHeight = (int)(boardImage.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(290 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton mg : buttons) {
            mg.update();
        }
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImagePink, 0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        graphics.drawImage(boardImage, menuX, menuY, menuWidth, menuHeight, null);

        for(MenuButton mg : buttons) {
            mg.draw(graphics);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isInBox(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isInBox(e, mb)) {
                if(mb.isMousePressed()) {
                    mb.applyGameState();
                }
                if(mb.getGameState() == GameState.PLAYING) {
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for(MenuButton mb : buttons) {
            if(isInBox(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
