package nutritious.prog.gameStates;

import nutritious.prog.UI.PauseOverlay;
import nutritious.prog.entities.Player;
import nutritious.prog.levels.LevelManager;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods{
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean isPaused = false;

    //offset used to manipulate position of everything
    private int xLvlOffset;
    //if player is beyond one of those two lines we will check if there is anything
    //to display further and "move the camera"
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    //we only want to move our level as far as it has its boundaries,
    //we don't want to exceed them and go into darkness
    //so we create max offset our level can go
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    //the offset is calculated by subtracting our visible game part from the
    //whole levels length
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    //conversion to pixels
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * game.SCALE), (int) (40 * game.SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void unpauseGame() {
        isPaused = false;
    }

    @Override
    public void update() {
        if (!isPaused) {
            levelManager.update();
            player.update();
            checkIfPlayerIsCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }
    //checking if player met left or right border and if we should move our level
    private void checkIfPlayerIsCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        //difference between player position and game window position
        //we need it to calculate how far has player go into the level
        //depending on that value we will check if we need to move our level
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;       //we increase the xLvlOffset everytime we exceed right border
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;        //we decrease the xLvlOffset everytime we exceed left border

        //check if we reached the end of the level (or beginning)
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics graphics) {
        levelManager.draw(graphics, xLvlOffset);
        player.render(graphics, xLvlOffset);
        if(isPaused)pauseOverlay.draw(graphics);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isPaused) {
            System.out.println("mousePressedInPlaying");
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isPaused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(isPaused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                isPaused = !isPaused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(isPaused) {
            pauseOverlay.mouseDragged(e);
        }
    }
}
