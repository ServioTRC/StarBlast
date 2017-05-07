package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;
import mx.itesm.starblast.gameEntities.AutoAnimation;

class ScreenMinigame3 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame3Scene;

    private boolean isStoryMode = false;

    private Sprite backButtonSprite;
    private ArrayList<Sprite> crystals = new ArrayList<Sprite>(3);
    private ArrayList<Rock> rocks = new ArrayList<Rock>(9);
    private ArrayList<Integer> positions = new ArrayList<Integer>(9);
    private ArrayList<AutoAnimation> animations = new ArrayList<AutoAnimation>();
    private Random r = new Random();
    private Text textScore;
    private int tries = 4;
    private int crystalFound = 0;
    private Sprite endingSprite;
    private boolean ended;
    private long endingTime = 0;
    private Sound breakingRockSound;
    private Sound crystalSelectedSound;
    private boolean timeTaken = false;
    private boolean won = false;

    private class Rock {
        int numClicks = 0;
        Texture[] states;
        int id;
        Sprite sprite;

        Rock(int id, Texture[] textures) {
            states = textures;
            this.id = id;
            sprite = new Sprite(textures[0]);
        }

        boolean getDamage() {
            numClicks++;
            if (numClicks < states.length) {
                sprite.setTexture(states[numClicks]);
                return false;
            } else {
                return true;
            }
        }
    }

    ScreenMinigame3(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
        Gdx.app.getPreferences("Tutos").putBoolean("" + 3, true).flush();
    }

    @Override
    public void show() {
        menu.changeMusic("SoundEffects/MinigameMusic" + (new Random().nextInt(2) + 1) + ".mp3");
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
        breakingRockSound = Constant.MANAGER.get("SoundEffects/RockDiggingSound.wav", Sound.class);
        crystalSelectedSound = Constant.MANAGER.get("SoundEffects/SelectionSound.mp3", Sound.class);
        randomPos();
        addRocks();
        createBackButton();
    }

    private void addRocks() {
        Texture[] textures = {Constant.MANAGER.get("Minigame3Screen/RockSpriteNew.png", Texture.class),
                Constant.MANAGER.get("Minigame3Screen/RockSpriteDamaged.png", Texture.class),
                Constant.MANAGER.get("Minigame3Screen/RockSpriteVeryDamaged.png", Texture.class)};
        for (int i = 0; i < 9; i++) {
            Rock rock = new Rock(i, textures);
            switch (i) {
                case 0:
                    rock.sprite.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    rock.sprite.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 1:
                    rock.sprite.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    rock.sprite.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 2:
                    rock.sprite.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    rock.sprite.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 3:
                    rock.sprite.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    rock.sprite.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 4:
                    rock.sprite.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    rock.sprite.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 5:
                    rock.sprite.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    rock.sprite.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 6:
                    rock.sprite.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    rock.sprite.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
                    break;
                case 7:
                    rock.sprite.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    rock.sprite.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
                    break;
                case 8:
                    rock.sprite.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    rock.sprite.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
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
            Sprite crystal = new Sprite(Constant.MANAGER.get("Minigame3Screen/Minigame3Crystal.png", Texture.class));
            switch (pos) {
                case 0:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 1:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 2:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    crystal.setY(4 * Constant.SCREEN_HEIGTH / 5 - 160);
                    break;
                case 3:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 4:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 5:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    crystal.setY(2 * Constant.SCREEN_HEIGTH / 5 - 70);
                    break;
                case 6:
                    crystal.setX(1 * Constant.SCREEN_WIDTH / 5 + 40);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
                    break;
                case 7:
                    crystal.setX(2 * Constant.SCREEN_WIDTH / 5 + 15);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
                    break;
                case 8:
                    crystal.setX(3 * Constant.SCREEN_WIDTH / 5 - 10);
                    crystal.setY(1 * Constant.SCREEN_HEIGTH / 5 - 140);
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
            won = true;
            if (!timeTaken) {
                endingTime = TimeUtils.millis();
                timeTaken = true;
            }
        } else if (tries <= 0) {
            won = false;
            ended = true;
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
            if (!timeTaken) {
                endingTime = TimeUtils.millis();
                timeTaken = true;
            }
        }

        for (Sprite crys : crystals) {
            crys.draw(batch);
        }

        Iterator<AutoAnimation> it = animations.iterator();
        AutoAnimation animation;
        while (it.hasNext()) {
            animation = it.next();
            if (animation.draw(batch, delta)) {
                it.remove();
            }
        }

        if (!ended) {
            for (Rock rock : rocks) {
                rock.sprite.draw(batch);
            }
            backButtonSprite.draw(batch);
            textScore.showMessage(batch, Integer.toString(tries),
                    Constant.SCREEN_WIDTH / 2 - 30, Constant.SCREEN_HEIGTH - 50, Color.GREEN);
        } else {
            PreferencesSB.saveMinigameProgress(3, isStoryMode, won);

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
            PreferencesSB.clickedSound();
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu, false));
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
            Rock rock = rocks.get(i);
            if (rock.sprite.getBoundingRectangle().contains(vector.x, vector.y)) {
                if (rock.getDamage()) {
                    animations.add(new AutoAnimation(Constant.MANAGER.get("Minigame3Screen/RockBreakingAnimation.png", Texture.class),
                            0.1f, rock.sprite.getX() + rock.sprite.getWidth() / 2, rock.sprite.getY() + rock.sprite.getHeight() / 2, 218, 218, batch));
                    rocks.remove(i);
                    tries--;
                    if (positions.contains(rock.id)) {
                        crystalFound++;
                        tries++;
                        if (PreferencesSB.SOUNDS_ENABLE)
                            crystalSelectedSound.play(1f);
                    }
                    if (PreferencesSB.SOUNDS_ENABLE)
                        breakingRockSound.play(1f);
                }

            }
        }

        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y)) {
            PreferencesSB.clickedSound();
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));
        }

        if (endingSprite.getBoundingRectangle().contains(vector.x, vector.y) && ended && ((TimeUtils.millis() - endingTime) > 500)) {
            PreferencesSB.clickedSound();
            menu.setScreen(isStoryMode ? new ScreenLoading(menu, Constant.Screens.NEXT_LEVEL) : new ScreenMinigamesSelection(menu, false));
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
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu, false));

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

}
