package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

class ScreenMinigame3 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame3Scene;

    private boolean isStoryMode = false;

    private Sprite backButtonSprite;
    private SpriteSB genericSprite;
    private SpriteSB crystal;
    private SpriteSB rock;
    private ArrayList<SpriteSB> crystals = new ArrayList<SpriteSB>(3);
    private ArrayList<SpriteSB> rocks = new ArrayList<SpriteSB>(9);
    private ArrayList<Integer> positions = new ArrayList<Integer>(9);
    private Random r = new Random();
    private Text textScore;
    private int tries = 5;
    private int crystalFound = 0;
    private Sprite endingSprite;
    private boolean ended;
    private long endingTime;

    ScreenMinigame3(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
    }

    @Override
    public void show() {
        createObjects();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
    }

    private void createObjects() {
        batch = new SpriteBatch();
        minigame3Scene = new Stage(view, batch);
        Image imgFondo = new Image(Constant.MANAGER.get("Minigame3Screen/Minigame3Background.jpg", Texture.class));
        minigame3Scene.addActor(imgFondo);
        textScore = new Text(Constant.SOURCE_TEXT);
        endingSprite = new Sprite(Constant.MANAGER.get("Minigame3Screen/SplashMinigame3Win.png", Texture.class));
        endingSprite.setCenterY(Constant.SCREEN_HEIGTH / 2);
        endingSprite.setCenterX(Constant.SCREEN_WIDTH / 2);
        randomPos();
        addingRocks();
        createBackButton();
    }

    private void addingRocks() {
        for (int i = 0; i < 9; i++) {
            rock = new SpriteSB("Minigame3Screen/RockSpriteNew.png", i);
            switch (i) {
                case 0:
                    rock.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    rock.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 1:
                    rock.setX(2 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 2:
                    rock.setX(3 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 3:
                    rock.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    rock.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 4:
                    rock.setX(2 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 5:
                    rock.setX(3 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(2 * Constant.SCREEN_HEIGTH / 5 - 80);
                    break;
                case 6:
                    rock.setX(1 * Constant.SCREEN_WIDTH / 5 + 20);
                    rock.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 7:
                    rock.setX(2 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
                case 8:
                    rock.setX(3 * Constant.SCREEN_WIDTH / 5);
                    rock.setY(1 * Constant.SCREEN_HEIGTH / 5 - 120);
                    break;
            }
            rocks.add(rock);
        }
    }

    private void randomPos() {
        for (int i = 0; i <= 3; i++) {
            int pos = r.nextInt(9);
            while (positions.contains(pos))
                pos = r.nextInt(9);
            crystal = new SpriteSB("Minigame3Screen/Minigame3Crystal.png", pos);
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
        if (crystalFound >= 3) {
            ended = true;
            endingTime = TimeUtils.millis();
        } else if (tries <= 0) {
            ended = true;
            endingTime = TimeUtils.millis();
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
        }

        for (SpriteSB crys : crystals) {
            crys.draw(batch);
        }

        if (!ended) {
            for (SpriteSB rock : rocks) {
                rock.draw(batch);
            }
            backButtonSprite.draw(batch);
            textScore.showMessage(batch, Integer.toString(tries),
                    Constant.SCREEN_WIDTH / 2 - 30, Constant.SCREEN_HEIGTH - 50, Color.GREEN);
        } else {
            PreferencesSB.saveMinigameProgress(3);
            endingSprite.draw(batch);
        }
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
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu));
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
        Vector3 vector = camera.unproject(new Vector3(screenX, screenY, 0));
        for (int i = rocks.size() - 1; i >= 0; i--) {
            genericSprite = rocks.get(i);
            if (genericSprite.touched(vector)) {
                rocks.remove(i);
                tries--;
                if (positions.contains(genericSprite.getId())) {
                    crystalFound++;
                    tries++;
                }
            }
        }

        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y))
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));

        if (endingSprite.getBoundingRectangle().contains(vector.x, vector.y) && ended && ((TimeUtils.millis()-endingTime)>1000)) {
            if (isStoryMode)
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.ENDLESS));
            else
                menu.setScreen(new ScreenMinigamesSelection(menu));
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 vector = camera.unproject(new Vector3(screenX, screenY, 0));
        if (backButtonSprite.getTexture().equals(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class))) {
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        }
        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y))
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu));

        return true;
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
