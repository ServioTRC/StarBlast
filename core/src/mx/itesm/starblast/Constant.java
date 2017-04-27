package mx.itesm.starblast;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;

public class Constant {
    public static final float SCREEN_WIDTH = 1280;
    public static final float SCREEN_HEIGTH = 800;
    public static final AssetManager MANAGER = new AssetManager();
    public static final float SHIPS_SCALE = -0.3f;
    public static final String SOURCE_TEXT = "Text/ArcadeFont.fnt";
    public static final float TOUCHPAD_DEADZONE = 0.20f;
    public static final ArrayList<String> CODES = new ArrayList<String>(5);

    public static final short CATEGORY_PLAYER = 1;
    public static final short CATEGORY_ENEMY = 2;
    public static final short CATEGORY_BULLET_PLAYER = 4;
    public static final short CATEGORY_BULLET_ENEMY = 8;
    public static final short CATEGORY_BORDERS = 16;
    public static final short CATEGORY_EXPLOSIONS = 32;

    public static final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS | CATEGORY_EXPLOSIONS;
    public static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_ENEMY | CATEGORY_EXPLOSIONS;
    public static final short MASK_BULLET_PLAYER = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS;
    public static final short MASK_BULLET_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_BORDERS;
    public static final short MASK_BORDERS = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_BULLET_ENEMY;
    public static final short MASK_EXPLOSIONS = CATEGORY_ENEMY | CATEGORY_PLAYER;

    public static float toWorldSize(float screen){
        return screen*0.01f;
    }
    public static float toScreenSize(float world){
        return world*100;
    }

    public enum Screens {
        SPLASH,
        START,
        MENU,
        LEVEL1,
        LEVEL2,
        LEVEL3,
        ENDLESS,
        MINIGAMES,
        MINI1,
        MINI2,
        MINI3,
        SCORES,
        OPTIONS,
        CREDITS
    }

    public static final int EXPLOSION_SIZE_X = 100;
    public static final int EXPLOSION_SIZE_Y = 100;
}
