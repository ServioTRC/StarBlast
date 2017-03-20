package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;

/**
 * Created by delag on 3/17/2017.
 */

public class Preferencias {
    public static boolean MUSICA_HABILITADA = true;
    public static boolean SONIDO_HABILITADO = true;

    public static void leerPreferenciasSonidos(){
        Preferences prefs = Gdx.app.getPreferences("Sonido");
        MUSICA_HABILITADA = prefs.getBoolean("MUSICA_HABILITADA",true);
        SONIDO_HABILITADO = prefs.getBoolean("SONIDO_HABILITADO",true);
    }

    public static void escribirPreferenciasSonidos(){
        Preferences prefs = Gdx.app.getPreferences("Sonido");
        prefs.putBoolean("MUSICA_HABILITADA",MUSICA_HABILITADA);
        prefs.putBoolean("SONIDO_HABILITADO",SONIDO_HABILITADO);
        prefs.flush();
    }

    public static void guardarPuntaje(String nombreUsuario, int puntajeUsuario){
        //Comparando Valores
        Preferences prefs = Gdx.app.getPreferences("Puntajes Mas Altos");
        ArrayList<Integer> puntajes = new ArrayList<Integer>(6);
        ArrayList<String> nombres = new ArrayList<String>(6);
        String[] arrInfo;
        for(int i = 1; i <= 5; i++) {
            arrInfo = prefs.getString("punt"+i, "----- 00000").split(" ");
            puntajes.add(Integer.parseInt(arrInfo[1]));
            nombres.add(arrInfo[0]);
        }
        for(int i = 0; i < 5; i++){
            if(puntajes.get(i) <= puntajeUsuario){
                puntajes.add(i, puntajeUsuario);
                nombres.add(i, nombreUsuario);
                Gdx.app.log("Preferencias ",String.valueOf(i));
                break;
            }
        }
        //Guardando Puntajes
        for(int i = 1; i <= 5; i++){
            prefs.putString("punt"+i, nombres.get(i-1)+" "+puntajes.get(i-1));
        }
        prefs.flush();
    }

}
