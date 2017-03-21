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
        Preferencias.leerPreferenciasSonidos();
        cargarEfectosSonoros();
        if(Preferencias.MUSICA_HABILITADA) {
            musicaFondo.play();
        }
        Bullet.CargarTextura();
        setScreen(new PantallaSplashTec(this));
    }

    @Override
    public void dispose(){
        Gdx.app.log("Borrando aplicacion", "StarBlAST");
        Constantes.ASSET_GENERAL.clear();
    }

    @Override
    public void pause(){
        Gdx.app.log("PAUsando musica", "StarBlAST");
        pauseMusica();
    }

    private void cargarEfectosSonoros(){
        Gdx.app.log("Creando aplicacion", "Musica");
        Constantes.ASSET_GENERAL.load("EfectosSonoros/MusicaFondo.mp3", Music.class);
        Constantes.ASSET_GENERAL.finishLoading();
        musicaFondo = Constantes.ASSET_GENERAL.get("EfectosSonoros/MusicaFondo.mp3");
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
