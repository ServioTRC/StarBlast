package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

public abstract class Constant {
    public static final float SCREEN_WIDTH = 1280;
    public static final float SCREEN_HEIGTH = 800;
    public static final AssetManager MANAGER = new AssetManager();
    public static final float SHIPS_SCALE = -0.3f;
    public static final String SOURCE_TEXT = "Text/ArcadeFont.fnt";
    public static final float TOUCHPAD_DEADZONE = 0.20f;

    public static final short CATEGORY_PLAYER = 1;
    public static final short CATEGORY_ENEMY = 2;
    public static final short CATEGORY_BULLET_PLAYER = 4;
    public static final short CATEGORY_BULLET_ENEMY = 8;
    public static final short CATEGORY_BORDERS = 16;
    public static final short CATEGORY_EXPLOSIONS = 32;
    public static final short CATEGORY_POWER_UP = 64;

    public static final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS | CATEGORY_EXPLOSIONS | CATEGORY_POWER_UP;
    public static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_ENEMY | CATEGORY_EXPLOSIONS;
    public static final short MASK_BULLET_PLAYER = CATEGORY_ENEMY | CATEGORY_BULLET_ENEMY | CATEGORY_BORDERS;
    public static final short MASK_BULLET_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_BORDERS;
    public static final short MASK_BORDERS = CATEGORY_PLAYER | CATEGORY_BULLET_PLAYER | CATEGORY_BULLET_ENEMY | CATEGORY_POWER_UP;
    public static final short MASK_EXPLOSIONS = CATEGORY_ENEMY | CATEGORY_PLAYER;
    public static final short MASK_POWER_UP = CATEGORY_PLAYER  | CATEGORY_BORDERS;

    public static float toWorldSize(float screen){
        return screen*0.01f;
    }
    public static float toScreenSize(float world){
        return world*100;
    }

    public final static String ORIGINAL_MUSIC = "SoundEffects/BackgroundLoopingMusic.mp3";
    public final static String DARUDE = "SoundEffects/Darude - Sandstorm.mp3";

    public enum Screens {
        SPLASH,
        START,
        MENU,
        LEVEL1,
        LEVEL2,
        LEVEL3,
        NEXT_LEVEL,
        ENDLESS,
        MINIGAMES,
        MINI1,
        MINI2,
        MINI3,
        SCORES,
        OPTIONS,
        HELP,
        CREDITS
    }

    public static final int EXPLOSION_SIZE_X = 100;
    public static final int EXPLOSION_SIZE_Y = 100;
    public static final int MISSILE_EXPLOSION_SIZE_X = 150;
    public static final int MISSILE_EXPLOSION_SIZE_Y = 150;

    public static Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SelectionSound.mp3"));

}
