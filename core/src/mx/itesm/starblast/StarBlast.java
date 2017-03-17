package mx.itesm.starblast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Servio T on 05/02/2017.
 */

public class StarBlast extends Game {

    private Music musicaFondo;


    @Override
    public void create () {
        Gdx.app.log("Creando aplicacion", "StarBlAST");
        Preferencias.leerPreferencias();
        cargarEfectosSonoros();
        if(Preferencias.MUSICA_HABILITADA) {
            musicaFondo.play();
        }
        setScreen(new PantallaSplashTec(this));
    }

    @Override
    public void resume(){
        Gdx.app.log("OnResume", "StarBlast");
        //musicaFondo = Constantes.ASSET_GENERAL.get("EfectosSonoros/StarWars.mp3");
        musicaFondo.play();
    }

    private void cargarEfectosSonoros(){
        Constantes.ASSET_GENERAL.load("EfectosSonoros/StarWars.mp3", Music.class);
        Constantes.ASSET_GENERAL.finishLoading();
        musicaFondo = Constantes.ASSET_GENERAL.get("EfectosSonoros/StarWars.mp3");
        musicaFondo.setLooping(true);
    }

    public void playMusica(){
        if (!musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    public void pauseMusica(){
        if (musicaFondo.isPlaying()) {
            musicaFondo.pause();
        }
    }


}
