package nutritious.prog.gameStates;

import nutritious.prog.UI.GameOverOverlay;
import nutritious.prog.UI.LevelCompletedOverlay;
import nutritious.prog.UI.PauseOverlay;
import nutritious.prog.entities.EnemyManager;
import nutritious.prog.entities.Player;
import nutritious.prog.levels.LevelManager;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static nutritious.prog.utils.Constants.Environment.*;

public class Playing extends State implements StateMethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean isPaused = false;

    //offset used to manipulate position of everything
    private int xLvlOffset;
    //if player is beyond one of those two lines we will check if there is anything
    //to display further and "move the camera"
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);

      private int maxLvlOffsetX;

    //background stuff
    private BufferedImage backgroundImage, bigCloud, smallCLoud;
    private int[] smallCloudPos; // random positions for small clouds
    private Random rng = new Random();

    private boolean gameOver = false;
    private boolean lvlCompleted = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        loadBackground();

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    public void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCLoud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[8];
        for(int i =0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int)(rng.nextInt((int)(120 * Game.SCALE)) + 70 * Game.SCALE);
        }
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (64 * game.SCALE), (int) (40 * game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
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
        if (isPaused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (!gameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkIfPlayerIsCloseToBorder();
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
        graphics.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(graphics);
        levelManager.draw(graphics, xLvlOffset);
        player.render(graphics, xLvlOffset);
        enemyManager.draw(graphics, xLvlOffset);
        if(isPaused) {
            graphics.setColor(new Color(0,0,0, 150));
            graphics.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(graphics);
        } else if(gameOver) {
            gameOverOverlay.draw(graphics);
        } else if(lvlCompleted) {
            levelCompletedOverlay.draw(graphics);
        }
    }

    private void drawClouds(Graphics graphics) {
        //the level offset calculation is for cloud movement
        for(int i = 0; i < 3; i++) {
            graphics.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int)(204 *  Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        for(int i = 0; i <  smallCloudPos.length; i++) {
            graphics.drawImage(smallCLoud, 50 + SMALL_CLOUD_WIDTH * 3 * i - (int)(xLvlOffset * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void resetAll() {
        //TODO reset player, enemy, lvl etc.
        lvlCompleted = false;
        gameOver = false;
        isPaused = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void checkedEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkIfEnemyGotHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (isPaused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (isPaused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (isPaused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver) {
            switch (e.getKeyCode()) {
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

    public void mouseDragged(MouseEvent e) {
        if(!gameOver) {
            if (isPaused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
    }
}
