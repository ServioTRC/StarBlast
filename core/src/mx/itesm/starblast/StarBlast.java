package mx.itesm.starblast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class StarBlast extends Game {

    private Music backgroundMusic;


    @Override
    public void create () {
        Gdx.app.log("StarBlast", "Creating application");
        PreferencesSB.readingSoundPreferences();
        loadMusicEffects();
        if(PreferencesSB.MUSIC_ENABLE) {
            backgroundMusic.play();
        }
        setScreen(new ScreenSplashTec(this));
    }

    @Override
    public void dispose(){
        Gdx.app.log("StarBlast","Disposing application");
        Constant.MANAGER.clear();
    }

    @Override
    public void pause(){
        Gdx.app.log("StarBlast", "Pausing music");
        pauseMusic();
    }

    private void loadMusicEffects(){
        Gdx.app.log("StarBlast", "Loading Background Music");
        Constant.MANAGER.load("SoundEffects/MusicaFondo.mp3", Music.class);
        Constant.MANAGER.finishLoading();
        backgroundMusic = Constant.MANAGER.get("SoundEffects/MusicaFondo.mp3");
        backgroundMusic.setLooping(true);
    }

    public void playMusic(){
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void pauseMusic(){
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }


}
