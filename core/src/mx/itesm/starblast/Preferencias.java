package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by delag on 3/17/2017.
 */

public class Preferencias {
    public static boolean MUSICA_HABILITADA = true;
    public static boolean SONIDO_HABILITADO = true;

    public static void leerPreferencias(){
        Preferences prefs = Gdx.app.getPreferences("Sonido");
        MUSICA_HABILITADA = prefs.getBoolean("MUSICA_HABILITADA",true);
        SONIDO_HABILITADO = prefs.getBoolean("SONIDO_HABILITADO",true);
    }

    public static void escribirPreferencias(){
        Preferences prefs = Gdx.app.getPreferences("Sonido");
        prefs.putBoolean("MUSICA_HABILITADA",MUSICA_HABILITADA);
        prefs.putBoolean("SONIDO_HABILITADO",SONIDO_HABILITADO);
        prefs.flush();
    }
}
