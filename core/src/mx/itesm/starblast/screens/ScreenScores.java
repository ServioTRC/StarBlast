package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;


public class ScreenScores extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage scoreScene;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    public ScreenScores(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        loadingTextures();
        creatingObjects();
    }

    private void creatingObjects() {
        SpriteBatch batch = new SpriteBatch();
        scoreScene = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenScores", " Going to ScreenMenu");
                    PreferencesSB.clickedSound();
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImage = new Image(backgroundTexture);
        text = new Text(Constant.SOURCE_TEXT);
        scoreScene.addActor(backgroundImage);
        createBackButton();
        createTitle();
        printScores();
        Gdx.input.setInputProcessor(scoreScene);
    }

    private void createBackButton() {
        textButtonStyle = text.generateText(Color.RED, Color.GOLD, 2);
        TextButton btnPlay = new TextButton("X", textButtonStyle);
        btnPlay.setPosition(7 * Constant.SCREEN_WIDTH / 8 - btnPlay.getWidth() / 2 + 100, Constant.SCREEN_HEIGTH / 8 - btnPlay.getHeight() / 2);

        scoreScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenScores", " Going to ScreenMenu");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenMenu(menu));
            }
        });
    }

    private void createTitle() {
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, 9 * Constant.SCREEN_HEIGTH / 10 - btnPlay.getHeight() / 2);
        scoreScene.addActor(btnPlay);
    }

    private void printScores() {
        float tmp = 3 * Constant.SCREEN_HEIGTH / 4;
        float[] heights = {tmp, tmp - 100, tmp - 200, tmp - 300, tmp - 400};
        String[] st;
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        for (int i = 0; i < 5; i++) {
            createScores(String.valueOf(i + 1), 200, heights[i]);
            st = prefs.getString(i + "", "----- 00000").split(" ");
            createScores(st[0], 380, heights[i]);
            createScores(st[1], 800, heights[i]);
        }
    }

    private void createScores(String nombre, float x, float y) {
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton scores = new TextButton(nombre, textButtonStyle);
        scores.setPosition(x, y - scores.getHeight() / 2);
        scoreScene.addActor(scores);
    }

    private void loadingTextures() {
        backgroundTexture = new Texture("HighScoresScreen/BackgroundHighScores.jpg");
    }

    @Override
    public void render(float delta) {
        clearScreen();
        scoreScene.draw();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


}
