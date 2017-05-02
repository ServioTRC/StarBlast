package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

class ScreenMinigame1 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame1Scene;

    private boolean won = false;
    private boolean isStoryMode = false;
    private final Sprite[] pieces = new Sprite[25];
    private final Vector2[] places = new Vector2[25];
    private final boolean[] done = new boolean[25];
    private int selectedPieceIdx = -1;
    private Vector3 vector;
    private static final float BOARD_START_X = 275;
    private static final float BOARD_START_Y = 733;
    private static final float PIECE_WIDTH = 136;
    private static final float PIECE_HEIGHT = 136;

    private long startingTime;
    private Text textScore;

    private Sprite backButtonSprite;
    private Sprite endingSprite;
    private boolean ended = false;


    ScreenMinigame1(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        minigame1Scene = new Stage(view, batch);
        Image imgFondo = new Image(Constant.MANAGER.get("Minigame1Screen/Minigame1Background.jpg", Texture.class));
        endingSprite = new Sprite(Constant.MANAGER.get("Minigame1Screen/SplashMinigame1Win.png", Texture.class));
        endingSprite.setY(Constant.SCREEN_HEIGTH / 2 - endingSprite.getHeight() / 2);
        endingSprite.setX(Constant.SCREEN_WIDTH / 2 - endingSprite.getWidth() / 2);
        minigame1Scene.addActor(imgFondo);
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i * 5 + j] = new Sprite(Constant.MANAGER.get("Minigame1Screen/PuzzlePieces/Pieza" + String.format("%c", 'A' + i) + (j + 1) + ".png", Texture.class));
                places[i * 5 + j] = new Vector2(BOARD_START_X + PIECE_WIDTH * (j + 1f / 2f), BOARD_START_Y - PIECE_HEIGHT * (i + 1f / 2f));

                pieces[i * 5 + j].setCenter(BOARD_START_X + r.nextFloat() * PIECE_WIDTH * 5, BOARD_START_Y - r.nextFloat() * PIECE_HEIGHT * 5);
            }
        }
        textScore = new Text(Constant.SOURCE_TEXT);
        creatingBackButton();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
        startingTime = TimeUtils.millis();
    }

    private void creatingBackButton() {
        backButtonSprite = new Sprite(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        backButtonSprite.setX(12 * Constant.SCREEN_WIDTH / 13);
        backButtonSprite.setY(9 * Constant.SCREEN_HEIGTH / 10);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        minigame1Scene.draw();
        batch.begin();

        if (won) {
            Gdx.app.log("ScreenMinigame1: ", "El jugador ha ganado");
            ended = true;
        } else if ((TimeUtils.millis() - startingTime) >= 90000) {
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
            ended = true;
        }

        for (Sprite piece : pieces) {
            piece.draw(batch);
        }

        backButtonSprite.draw(batch);
        if (ended) {
            PreferencesSB.saveMinigameProgress(1);
            endingSprite.draw(batch);
        } else {
            textScore.showMessage(batch, Long.toString((90000 - (TimeUtils.millis() - startingTime)) / 1000),
                    Constant.SCREEN_WIDTH / 2 - 60, Constant.SCREEN_HEIGTH - 20, Color.GREEN);
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
        for (int i = pieces.length - 1; i >= 0; i--) {
            if (pieces[i].getBoundingRectangle().contains(vector.x, vector.y)) {
                if (!done[i]) {
                    selectedPieceIdx = i;
                }
                break;
            }
        }
        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y)) {
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectedPieceIdx != -1) {
            vector = camera.unproject(new Vector3(screenX, screenY, 0));
            if (places[selectedPieceIdx].dst(vector.x, vector.y) < PIECE_WIDTH / 2) {
                pieces[selectedPieceIdx].setCenter(places[selectedPieceIdx].x, places[selectedPieceIdx].y);
                done[selectedPieceIdx] = true;
                won = true;
                for (boolean dn : done) {
                    if (!dn) {
                        won = false;
                        break;
                    }
                }
            }
            selectedPieceIdx = -1;
        }
        if (backButtonSprite.getTexture().equals(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class))) {
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        }

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
        if (selectedPieceIdx == -1) {
            return true;
        }
        vector = camera.unproject(new Vector3(screenX, screenY, 0));
        pieces[selectedPieceIdx].setCenter(vector.x, vector.y);
        return true;
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
