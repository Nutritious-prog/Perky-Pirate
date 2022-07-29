package nutritious.prog.gameStates;

import nutritious.prog.UI.PauseOverlay;
import nutritious.prog.entities.Player;
import nutritious.prog.levels.LevelManager;
import nutritious.prog.main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods{
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean isPaused = false;

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
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void draw(Graphics graphics) {
        levelManager.draw(graphics);
        player.render(graphics);
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
}
