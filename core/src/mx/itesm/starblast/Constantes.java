package mx.itesm.starblast;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;

/**
 * Created by Servio T on 05/02/2017.
 */

public class Constantes {
    public static final float ANCHO_PANTALLA = 1280;
    public static final float ALTO_PANTALLA = 800;
    public static AssetManager ASSET_GENERAL = new AssetManager();
    public static final float ESCALA_NAVES = -0.3f;
    public static final String TEXTO_FUENTE = "Textos/ArcadeFont.fnt";
    public static final float TOUCHPAD_DEADZONE = 0.20f;
    public static ArrayList<String> CODIGOS = new ArrayList<String>(5);

    public static float toWorldSize(float screen){
        return screen*0.01f;
    }
    public static float toScreenSize(float world){
        return world*100;
    }
    
}
