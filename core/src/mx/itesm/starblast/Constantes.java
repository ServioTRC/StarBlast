package mx.itesm.starblast;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;

class Constantes {
    static final float ANCHO_PANTALLA = 1280;
    static final float ALTO_PANTALLA = 800;
    static final AssetManager MANAGER = new AssetManager();
    static final float ESCALA_NAVES = -0.3f;
    static final String TEXTO_FUENTE = "Textos/ArcadeFont.fnt";
    static final float TOUCHPAD_DEADZONE = 0.20f;
    static final ArrayList<String> CODIGOS = new ArrayList<String>(5);

    static final short CATEGORY_PLAYER = 1;
    static final short CATEGORY_ENEMY = 2;
    static final short CATEGORY_BULLET = 4;
    static final short CATEGORY_BULLET_ENEMY = 8;
    static final short CATEGORY_BORDERS = 16;

    static final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS;
    static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET ;
    static final short MASK_BULLET = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS;
    static final short MASK_BULLET_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET | CATEGORY_BORDERS;
    static final short MASK_BORDERS = CATEGORY_PLAYER | CATEGORY_BULLET | CATEGORY_BULLET_ENEMY;

    static float toWorldSize(float screen){
        return screen*0.01f;
    }
    static float toScreenSize(float world){
        return world*100;
    }

    enum Pantallas{
        SPLASH,
        INICIO,
        MENU,
        HISTORIA,
        NIVEL1,
        ENDLESS,
        MINIJUEGOS,
        MINI1,
        MINI2,
        MINI3,
        PUNTAJES,
        OPCIONES,
        CREDITOS
    }
}
