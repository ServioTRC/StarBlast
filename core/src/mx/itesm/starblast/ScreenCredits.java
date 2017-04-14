package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

class ScreenCredits extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage creditsScene;

    //Text
    private Text text;

    ScreenCredits(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        loadingTexture();
        creatingObjects();
    }

    private void creatingObjects() {
        SpriteBatch batch = new SpriteBatch();
        creditsScene = new Stage(view, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenCredits ","Going to ScreenMenu");
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(backgroundTexture);
        text = new Text(Constant.SOURCE_TEXT);
        creditsScene.addActor(imgFondo);
        createBackButton();
        Gdx.input.setInputProcessor(creditsScene);
    }

    private void createBackButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/Back.png"));
        skin.add("Down", new Texture("PantallaOpciones/BackYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new ScreenMenu(menu));
            }
        });
        btn.setPosition(12* Constant.SCREEN_WIDTH /13,
                Constant.SCREEN_HEIGTH /8, Align.center);
        creditsScene.addActor(btn);
    }

    private void loadingTexture() {
        backgroundTexture = new Texture("PantallaCreditos/Creditos.jpg");
    }

    @Override
    public void render(float delta) {
        clearScreen();
        creditsScene.draw();
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
