package mx.itesm.starblast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class StarBlast extends Game {

    private Music backgroundMusic;


    @Override
    public void create() {
        Gdx.app.log("StarBlast", "Creating application");
        PreferencesSB.readSoundPreferences();
        loadMusicEffects();
        if (PreferencesSB.MUSIC_ENABLE) {
            playMusic();
        }
        setScreen(new mx.itesm.starblast.screens.ScreenSplashTec(this));
    }

    @Override
    public void dispose() {
        Gdx.app.log("StarBlast", "Disposing application");
        Constant.MANAGER.clear();
    }

    @Override
    public void pause() {
        Gdx.app.log("StarBlast", "Pausing music");
        pauseMusic();
    }

    private void loadMusicEffects() {
        Gdx.app.log("StarBlast", "Loading Background Music");
        Preferences prefs = Gdx.app.getPreferences("Codes");
        boolean darude = prefs.getBoolean("darude", false);
        Constant.MANAGER.load(darude ? Constant.DARUDE : Constant.ORIGINAL_MUSIC, Music.class);
        Constant.MANAGER.finishLoading();
        backgroundMusic = Constant.MANAGER.get(darude ? Constant.DARUDE : Constant.ORIGINAL_MUSIC);
        backgroundMusic.setLooping(true);
    }


    public void playMusic() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.setVolume(0.7f);
            backgroundMusic.play();
        }
    }

    public void pauseMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void changeMusic(String music) {
        if (Gdx.app.getPreferences("Codes").getBoolean("darude", false)) {
            return;
        }
        pauseMusic();
        Constant.MANAGER.load(music, Music.class);
        Constant.MANAGER.finishLoading();
        backgroundMusic = Constant.MANAGER.get(music);
        if (PreferencesSB.MUSIC_ENABLE) {
            playMusic();
        }
    }

}
