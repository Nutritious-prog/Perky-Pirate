package nutritious.prog.gameStates;

import nutritious.prog.UI.MenuButton;
import nutritious.prog.audio.AudioPlayer;
import nutritious.prog.main.Game;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isInBox(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(GameState state) {
        switch (state) {
            case MENU:
                game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
                break;
            case PLAYING:
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
        }

        GameState.state = state;
    }
}
