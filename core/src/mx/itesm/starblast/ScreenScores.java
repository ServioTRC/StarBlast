package mx.itesm.starblast;

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


class ScreenScores extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage scoreScene;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    ScreenScores(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        loadingTextures();
        creatingObjects();
    }

    private void creatingObjects() {
        SpriteBatch batch = new SpriteBatch();
        scoreScene = new Stage(view, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenScores"," Going to ScreenMenu");
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImage = new Image(backgroundTexture);
        text = new Text(Constant.SOURCE_TEXT);
        scoreScene.addActor(backgroundImage);
        creatingBackButton();
        creatingTitle();
        printingScores();
        Gdx.input.setInputProcessor(scoreScene);
    }

    private void creatingBackButton() {
        textButtonStyle = text.generateText(Color.RED,Color.GOLD,2);
        TextButton btnPlay = new TextButton("X", textButtonStyle);
        btnPlay.setPosition(7* Constant.SCREEN_WIDTH /8-btnPlay.getWidth()/2+100, Constant.SCREEN_HEIGTH /8-btnPlay.getHeight()/2);

        scoreScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenScores"," Going to ScreenMenu");
                menu.setScreen(new ScreenMenu(menu));
            }
        });
    }

    private void creatingTitle() {
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, 9 * Constant.SCREEN_HEIGTH / 10 - btnPlay.getHeight() / 2);
        scoreScene.addActor(btnPlay);
    }

    private void printingScores(){
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        String punt1 = "1. "+ prefs.getString("punt1", "----- 00000");
        creatingScores(punt1, Constant.SCREEN_WIDTH / 2, 3* Constant.SCREEN_HEIGTH / 4);
        String punt2 = "2. " + prefs.getString("punt2", "----- 00000");
        creatingScores(punt2, Constant.SCREEN_WIDTH / 2, 3* Constant.SCREEN_HEIGTH / 4-100);
        String punt3 = "3. " + prefs.getString("punt3", "----- 00000");
        creatingScores(punt3, Constant.SCREEN_WIDTH / 2, 3* Constant.SCREEN_HEIGTH / 4-200);
        String punt4 = "4. " + prefs.getString("punt4", "----- 00000");
        creatingScores(punt4, Constant.SCREEN_WIDTH / 2, 3* Constant.SCREEN_HEIGTH / 4-300);
        String punt5 = "5. " + prefs.getString("punt5", "----- 00000");
        creatingScores(punt5, Constant.SCREEN_WIDTH / 2, 3* Constant.SCREEN_HEIGTH / 4-400);
    }

    private void creatingScores(String nombre, float x, float y){
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton scores = new TextButton(nombre, textButtonStyle);
        scores.setPosition(x - scores.getWidth() / 2, y - scores.getHeight() / 2);
        scoreScene.addActor(scores);
    }

    private void loadingTextures() {
        backgroundTexture = new Texture("PantallaPuntajes/FondoSimple.jpg");
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
