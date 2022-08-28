package nutritious.prog.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
    //background music constants
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    //sound effects constants
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAME_OVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean musicMute, sfxMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadEffects();
        loadSongs();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = { "menu", "level1", "level2" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

        return null;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }
    }

    public void setLevelSong(int lvlIndex) {
        //every other level will play every second song
        if (lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        //starting index of attack effects from sfx array
        int attackSound = 4;
        attackSound += rand.nextInt(3);
        playEffect(attackSound);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleMusicMute() {
        this.musicMute = !musicMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(musicMute);
        }
    }

    public void toggleSfxMute() {
        this.sfxMute = !sfxMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(sfxMute);
        }
        if (!sfxMute) {
            playEffect(JUMP);
        }
    }

    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
