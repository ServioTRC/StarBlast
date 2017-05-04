package mx.itesm.starblast.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

class StageResults extends Stage {

    private final StarBlast menu;
    private final mx.itesm.starblast.gameEntities.IPausable parent;
    private boolean winner;
    private int score;

    StageResults(Viewport viewport, Batch batch, StarBlast menu, mx.itesm.starblast.gameEntities.IPausable parent) {
        super(viewport, batch);
        this.menu = menu;
        this.parent = parent;
        this.winner = false;
        this.score = 0;
        init();
    }

    public void setWinnnerAndScore(boolean winner, int score){
        this.winner = winner;
        this.score = score;
        askForName();
        showScore();
        showText();
    }

    private void init() {
        Image background = new Image(new Texture("HighScoresScreen/ResultsWindow.png"));
        background.setPosition(Constant.SCREEN_WIDTH / 2 - background.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - background.getHeight() / 2);
        addActor(background);
        createBackButton();
        createReturnButton();
    }

    private void createReturnButton() {Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonReset.png"));
        skin.add("Down", new Texture("SettingsScreen/ButtonResetYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                menu.setScreen(new LevelProof(menu));
            }
        });
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4 - 15,
                Constant.SCREEN_HEIGTH / 6 + 50, Align.center);
        addActor(btn);
    }

    private void createBackButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/Back.png"));
        skin.add("Down", new Texture("SettingsScreen/BackYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                menu.setScreen(new mx.itesm.starblast.screens.ScreenMenu(menu));
            }
        });
        btn.setPosition(Constant.SCREEN_WIDTH / 4 - 10,
                Constant.SCREEN_HEIGTH / 6 + 40, Align.center);
        addActor(btn);
    }

    private void showText(){
        Texture texture;
        if(this.winner){
            texture = new Texture("HighScoresScreen/WinMessage.png");
        }else{
            texture = new Texture("HighScoresScreen/LoseMessage.png");
        }
        Image text = new Image(texture);
        text.setPosition(Constant.SCREEN_WIDTH / 2 - text.getWidth() / 2,
                3* Constant.SCREEN_HEIGTH / 4 - 50 - text.getHeight() / 2);
        addActor(text);
    }

    private void showScore(){
        Gdx.app.log("FINAL", Integer.toString(this.score));
        Text text = new Text(Constant.SOURCE_TEXT);
        TextButton.TextButtonStyle textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton score = new TextButton(Integer.toString(this.score), textButtonStyle);
        score.setPosition(Constant.SCREEN_WIDTH /2+100 - score.getWidth() / 2,
                Constant.SCREEN_HEIGTH /2-90 - score.getHeight() / 2);
        addActor(score);
    }

    private void askForName(){
        PreferencesSB.clickedSound();
        Input.TextInputListener textListener = new Input.TextInputListener() {
            @Override
            public void input(String input) {
                Gdx.app.log("Nombre Ingresado: ", input);
                PreferencesSB.saveScore(input, score);
            }

            @Override
            public void canceled() {
                Gdx.app.log("Nombre Ingresado: ", "Salida del teclado");
            }
        };

        Gdx.input.getTextInput(textListener, "Ingrese su Nombre: ", "", "");
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            PreferencesSB.clickedSound();
            menu.setScreen(new mx.itesm.starblast.screens.ScreenMenu(menu));
            return true;
        }
        return super.keyDown(keyCode);
    }

}
