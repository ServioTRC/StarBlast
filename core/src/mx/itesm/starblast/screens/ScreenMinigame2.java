package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;


public class ScreenMinigame2 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame2Scene;

    private boolean isStoryMode = false;
    private Vector3 vector;
    private long startingTime;
    private Text textScore;
    private int score;
    private int piecesGenerated;

    private Sprite topBanner;
    private Sprite bottomBanner;
    private SpriteSB genericSprite;
    private SpriteSB genericSpriteTouch;
    private Sprite backButtonSprite;
    private Sprite endingSprite;
    private int num;
    private boolean exploted = false;
    private String id;

    private ArrayList<SpriteSB> pieces = new ArrayList<SpriteSB>();
    private Random r = new Random();
    private float posY;
    private boolean ended = false;

    private AnimatedImage countdownAnimation;

    ScreenMinigame2(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
        countdownAnimation = new AnimatedImage(new Animation<TextureRegion>(1f, new TextureRegion(Constant.MANAGER.get("Minigame2Screen/erroranimation.png", Texture.class)).split(1280, 800)[0]));
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
        if((score >= (piecesGenerated-5))&&((TimeUtils.millis() - startingTime) > 18000)){
            Gdx.app.log("ScreenMinigame1: ","El jugador ha ganado");
            ended = true;
        } else if (((TimeUtils.millis() - startingTime) >= 20000) || exploted) {
            ended = true;
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
            if (exploted) {
                minigame2Scene.addActor(countdownAnimation);
                countdownAnimation.act(delta * 5);
                if (countdownAnimation.stateTime > 5)
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
                posY = genericSprite.getY();
                posY -= 10;
                if (posY < 0)
                    pieces.remove(i);
                else {
                    genericSprite.setY(posY);
                    genericSprite.draw(batch);
                }
            }
            topBanner.draw(batch);
            textScore.showMessage(batch, Long.toString((20000 - (TimeUtils.millis() - startingTime)) / 1000),
                    Constant.SCREEN_WIDTH / 2 - 45, Constant.SCREEN_HEIGTH - 20, Color.GREEN);
            textScore.showMessage(batch, Integer.toString(score),
                    Constant.SCREEN_WIDTH / 2, 60, Color.GREEN);
            backButtonSprite.draw(batch);
        } else {
            endingSprite.draw(batch);
        }
        bottomBanner.draw(batch);
        batch.end();
    }

    private void addingObjects() {
        num = r.nextInt(33);
        if (num == 7) {
            num = r.nextInt(4);
            switch (num) {
                case 0:
                    genericSprite = new SpriteSB("Minigame2Screen/BadCollectible.png", "BadCollectible");
                    break;
                case 1:
                    genericSprite = new SpriteSB("Minigame2Screen/Collectible1.png", "Collectible");
                    piecesGenerated++;
                    break;
                case 2:
                    genericSprite = new SpriteSB("Minigame2Screen/Collectible2.png", "Collectible");
                    piecesGenerated++;
                    break;
                case 3:
                    genericSprite = new SpriteSB("Minigame2Screen/Collectible3.png", "Collectible");
                    piecesGenerated++;
                    break;
            }
            num = r.nextInt(6);
            switch (num) {
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
            if (isStoryMode) {
                //TODO better handling of back on story mode
                Gdx.app.log("ScreenMinigame1: ", "Es historia y no hago nada");
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
        vector = camera.unproject(new Vector3(screenX, screenY, 0));
        for (int i = pieces.size() - 1; i >= 0; i--) {
            genericSpriteTouch = pieces.get(i);
            if (genericSpriteTouch.touched(vector)) {
                id = genericSpriteTouch.getId();
                if (id.equals("BadCollectible")) {
                    exploted = true;
                } else {
                    score++;
                }
                pieces.remove(i);
            }
        }
        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y))
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        else
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y))
            menu.setScreen(new ScreenMinigamesSelection(menu));

        if (endingSprite.getBoundingRectangle().contains(vector.x, vector.y) && ended) {
            if (isStoryMode)
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL3));
            else
                menu.setScreen(new ScreenMinigamesSelection(menu));
        }

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
        private String id;

        public SpriteSB(String texture, String id) {
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

        public String getId() {
            return this.id;
        }

        public boolean touched(Vector3 vector) {
            return this.sprite.getBoundingRectangle().contains(vector.x, vector.y);
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


