package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

/**
 * Created by a01371719 on 02/05/17.
 */

public class ScreenMinigame3 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame3Scene;

    private boolean isStoryMode = false;

    private Texture cristalTexture;
    private Texture backgroundTexture;
    private Texture rockTexture;
    private Sprite backButtonSprite;
    private SpriteSB crystal;
    private SpriteSB rock;
    private ArrayList<SpriteSB> crystals = new ArrayList<SpriteSB>(3);
    private ArrayList<SpriteSB> rocks = new ArrayList<SpriteSB>(9);
    private ArrayList<Integer> positions = new ArrayList<Integer>(9);
    private Random r = new Random();

    ScreenMinigame3 (StarBlast menu, boolean isStoryMode){
        this.menu = menu;
        this.isStoryMode = isStoryMode;
    }

    @Override
    public void show() {
        loadingTextures();
        createObjects();
    }

    private void createObjects() {
        batch = new SpriteBatch();
        minigame3Scene = new Stage(view, batch);
        Image imgFondo = new Image(backgroundTexture);
        minigame3Scene.addActor(imgFondo);
        /*textScore = new Text(Constant.SOURCE_TEXT);
        topBanner = new Sprite(Constant.MANAGER.get("Minigame2Screen/TopBanner.png", Texture.class));
        topBanner.setY(Constant.SCREEN_HEIGTH - topBanner.getHeight());
        bottomBanner = new Sprite(Constant.MANAGER.get("Minigame2Screen/BottomBanner.png", Texture.class));
        endingSprite = new Sprite(Constant.MANAGER.get("Minigame2Screen/SplashMinigame2Win.png", Texture.class));
        endingSprite.setCenterY(Constant.SCREEN_HEIGTH / 2);
        endingSprite.setCenterX(Constant.SCREEN_WIDTH / 2);*/
        crystal = new SpriteSB("Minigame3Screen/Minigame3Crystal.png", 1);
        rock = new SpriteSB("Minigame3Screen/RockSpriteNew.png", 1);

        /* Pos 0
        crystal.setX(1*Constant.SCREEN_WIDTH/5 +20);
        crystal.setY(4*Constant.SCREEN_HEIGTH/5-160);
        crystals.add(crystal);*/

        /*Pos 1
        crystal.setX(2*Constant.SCREEN_WIDTH/5);
        crystal.setY(4*Constant.SCREEN_HEIGTH/5-160);
        crystals.add(crystal);*/

        /*
        Pos 2
        crystal.setX(3*Constant.SCREEN_WIDTH/5);
        crystal.setY(4*Constant.SCREEN_HEIGTH/5-160);
        crystals.add(crystal);*/

        /* Pos 3
        crystal.setX(1*Constant.SCREEN_WIDTH/5 +20);
        crystal.setY(2*Constant.SCREEN_HEIGTH/5-80);*/
        /*
        Pos 4
        crystal.setX(2*Constant.SCREEN_WIDTH/5);
        crystal.setY(2*Constant.SCREEN_HEIGTH/5-80);*/
        /*
        Pos 5
        crystal.setX(3*Constant.SCREEN_WIDTH/5);
        crystal.setY(2*Constant.SCREEN_HEIGTH/5-80);*/

        /*Pos 6
        crystal.setX(1*Constant.SCREEN_WIDTH/5+20);
        crystal.setY(1*Constant.SCREEN_HEIGTH/5-120);
        crystals.add(crystal);*/

        /*Pos 7
        crystal.setX(2*Constant.SCREEN_WIDTH/5);
        crystal.setY(1*Constant.SCREEN_HEIGTH/5-120);
        crystals.add(crystal);*/

        /*Pos 8
        crystal.setX(3*Constant.SCREEN_WIDTH/5);
        crystal.setY(1*Constant.SCREEN_HEIGTH/5-120);
        crystals.add(crystal);*/
        randomPos();
        addingRocks();
        createBackButton();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
    }

    private void addingRocks() {
        for(int i = 0; i < 3; i++) {
            int pos = r.nextInt(9);
            while (positions.contains(pos))
                pos = r.nextInt(9);
            crystal = new SpriteSB("Minigame3Screen/Minigame3Crystal.png", 1);
            switch (pos) {
                case 0:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 1:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 2:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 3:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 4:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 5:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 6:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 7:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 8:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
            }
            positions.add(pos);
            crystals.add(crystal);
        }
    }

    private void randomPos(){
        for(int i = 0; i < 3; i++) {
            int pos = r.nextInt(9);
            while (positions.contains(pos))
                pos = r.nextInt(9);
            crystal = new SpriteSB("Minigame3Screen/Minigame3Crystal.png", 1);
            switch (pos) {
                case 0:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 1:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 2:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 3:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 4:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 5:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 6:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 7:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 8:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
            }
            positions.add(pos);
            crystals.add(crystal);
        }
    }

    private void loadingTextures() {
        backgroundTexture = new Texture("Minigame3Screen/Minigame3Background.jpg");
    }

    private void createBackButton() {
        backButtonSprite = new Sprite(new Texture("SettingsScreen/Back.png"));
        backButtonSprite.setX((12 * Constant.SCREEN_WIDTH / 13) + 10);
        backButtonSprite.setY((9 * Constant.SCREEN_HEIGTH / 10) - 15);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        minigame3Scene.draw();
        batch.begin();
        for(SpriteSB crys: crystals){
            crys.draw(batch);
        }
        backButtonSprite.draw(batch);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (isStoryMode) {
                //TODO better handling of back on story mode
                Gdx.app.log("ScreenMinigame3: ", "Es historia y no hago nada");
                return true;
            }
            Gdx.app.log("ScreenMinigame1: ", "Going to minigames selection");
            menu.setScreen(new ScreenMinigamesSelection(menu));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private class SpriteSB {
        private Sprite sprite;
        private Integer id;

        public SpriteSB(String texture, Integer id) {
            this.sprite = new Sprite(new Texture(texture));
            this.id = id;
        }

        public void setY(float y) {
            this.sprite.setY(y);
        }

        public void setX(float x) {
            this.sprite.setX(x);
        }

        public float getX() {
            return this.sprite.getX();
        }

        public float getY() {
            return this.sprite.getY();
        }

        public void draw(SpriteBatch batch) {
            this.sprite.draw(batch);
        }

        public Integer getId() {
            return this.id;
        }

        public boolean touched(Vector3 vector) {
            return this.sprite.getBoundingRectangle().contains(vector.x, vector.y);
        }

    }

}