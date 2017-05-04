package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;


class ScreenMinigame2 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame2Scene;

    private boolean isStoryMode = false;
    private long startingTime;
    private long endingTime;
    private Text textScore;
    private int score;
    private int piecesGenerated;

    private Sprite topBanner;
    private Sprite bottomBanner;
    private SpriteSB genericSprite;
    private Sprite backButtonSprite;
    private Sprite endingSprite;
    private boolean exploted = false;
    private boolean timeTaken = false;


    private ArrayList<SpriteSB> pieces = new ArrayList<SpriteSB>();
    private Random r = new Random();
    private boolean ended = false;

    private AnimatedImage countdownAnimation;
    private Sound explotionSound;
    private Sound captureSound;

    ScreenMinigame2(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
        countdownAnimation = new AnimatedImage(new Animation<TextureRegion>(1f, new TextureRegion(Constant.MANAGER.get("Minigame2Screen/ErrorAnimation.png", Texture.class)).split(1280, 800)[0]));
        countdownAnimation.setPosition(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, Align.center);
    }

    @Override
    public void show() {
        createObjects();
        startingTime = TimeUtils.millis();
    }

    private void createObjects() {
        batch = new SpriteBatch();
        minigame2Scene = new Stage(view, batch);
        Image imgFondo = new Image(Constant.MANAGER.get("Minigame2Screen/BackgroundMinigame2.jpg", Texture.class));
        minigame2Scene.addActor(imgFondo);
        textScore = new Text(Constant.SOURCE_TEXT);
        topBanner = new Sprite(Constant.MANAGER.get("Minigame2Screen/TopBanner.png", Texture.class));
        topBanner.setY(Constant.SCREEN_HEIGTH - topBanner.getHeight());
        bottomBanner = new Sprite(Constant.MANAGER.get("Minigame2Screen/BottomBanner.png", Texture.class));
        endingSprite = new Sprite(Constant.MANAGER.get("Minigame2Screen/SplashMinigame2Win.png", Texture.class));
        explotionSound = Constant.MANAGER.get("SoundEffects/Explosion1.mp3", Sound.class);
        captureSound = Constant.MANAGER.get("SoundEffects/PowerupPickupSound.wav", Sound.class);
        endingSprite.setCenterY(Constant.SCREEN_HEIGTH / 2);
        endingSprite.setCenterX(Constant.SCREEN_WIDTH / 2);
        createBackButton();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
    }

    private void createBackButton() {
        backButtonSprite = new Sprite(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        backButtonSprite.setX((12 * Constant.SCREEN_WIDTH / 13) + 10);
        backButtonSprite.setY((9 * Constant.SCREEN_HEIGTH / 10) - 15);
    }

    @Override
    public void render(float delta) {
        if ((score >= (piecesGenerated - 10)) && ((TimeUtils.millis() - startingTime) > 18000)) {
            Gdx.app.log("ScreenMinigame1: ", "El jugador ha ganado");
            ended = true;
            if(!timeTaken) {
                endingTime = TimeUtils.millis();
                timeTaken = true;
            }
        } else if (((TimeUtils.millis() - startingTime) >= 20000) || exploted) {
            ended = true;
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
            if(!timeTaken) {
                endingTime = TimeUtils.millis();
                timeTaken = true;
            }
            if (exploted) {
                minigame2Scene.addActor(countdownAnimation);
                countdownAnimation.act(delta * 3);
                if (countdownAnimation.stateTime >= 5)
                    exploted = false;
            }
        }
        clearScreen();
        minigame2Scene.draw();
        batch.begin();
        if (!ended) {
            if ((TimeUtils.millis() - startingTime) <= 18000)
                addingObjects();
            for (int i = pieces.size() - 1; i >= 0; i--) {
                genericSprite = pieces.get(i);
                float posY = genericSprite.getY();
                posY -= 10;
                if (posY < 0)
                    pieces.remove(i);
                else {
                    genericSprite.setY(posY);
                    genericSprite.draw(batch);
                }
            }
            topBanner.draw(batch);
            bottomBanner.draw(batch);
            textScore.showMessage(batch, Long.toString((20000 - (TimeUtils.millis() - startingTime)) / 1000),
                    Constant.SCREEN_WIDTH / 2 - 45, Constant.SCREEN_HEIGTH - 20, Color.GREEN);
            textScore.showMessage(batch, Integer.toString(score),
                    Constant.SCREEN_WIDTH / 2, 60, Color.GREEN);
            backButtonSprite.draw(batch);
        } else {
            PreferencesSB.saveMinigameProgress(2);
            endingSprite.draw(batch);
        }
        batch.end();
    }

    private void addingObjects() {
        if (r.nextInt(33) == 7) {
            switch (r.nextInt(4)) {
                case 0:
                    genericSprite = new SpriteSB(Constant.MANAGER.get("Minigame2Screen/BadCollectible.png", Texture.class), Types.BAD);
                    break;
                case 1:
                    genericSprite = new SpriteSB(Constant.MANAGER.get("Minigame2Screen/Collectible1.png", Texture.class), Types.GOOD);
                    piecesGenerated++;
                    break;
                case 2:
                    genericSprite = new SpriteSB(Constant.MANAGER.get("Minigame2Screen/Collectible3.png", Texture.class), Types.GOOD);
                    piecesGenerated++;
                    break;
                case 3:
                    genericSprite = new SpriteSB(Constant.MANAGER.get("Minigame2Screen/Collectible3.png", Texture.class), Types.GOOD);
                    piecesGenerated++;
                    break;
            }
            switch (r.nextInt(6)) {
                case 0:
                    genericSprite.setX(225);
                    break;
                case 1:
                    genericSprite.setX(350);
                    break;
                case 2:
                    genericSprite.setX(475);
                    break;
                case 3:
                    genericSprite.setX(600);
                    break;
                case 4:
                    genericSprite.setX(725);
                    break;
                case 5:
                    genericSprite.setX(850);
                    break;
            }
            genericSprite.setY(Constant.SCREEN_HEIGTH);
            pieces.add(genericSprite);
        }
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
        for (int i = pieces.size() - 1; i >= 0; i--) {
            SpriteSB genericSpriteTouch = pieces.get(i);
            if (genericSpriteTouch.touched(vector)) {
                if (genericSpriteTouch.getId() == Types.BAD) {
                    exploted = true;
                    if(PreferencesSB.SOUNDS_ENABLE)
                        explotionSound.play(1f);
                } else {
                    score++;
                    if(PreferencesSB.SOUNDS_ENABLE)
                        captureSound.play(1f);
                }
                pieces.remove(i);
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

    private enum Types {
        BAD,
        GOOD
    }

    private class SpriteSB extends Sprite {
        private Types id;

        SpriteSB(Texture texture, Types id) {
            super(texture);
            this.id = id;
        }

        Types getId() {
            return this.id;
        }

        boolean touched(Vector3 vector) {
            return this.getBoundingRectangle().contains(vector.x, vector.y);
        }

    }

    private class AnimatedImage extends Image {
        Animation<TextureRegion> animation = null;
        float stateTime = 0;

        AnimatedImage(Animation<TextureRegion> animation) {
            super(animation.getKeyFrame(0));
            this.animation = animation;
        }

        @Override
        public void act(float delta) {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
            super.act(delta);
        }
    }

}


